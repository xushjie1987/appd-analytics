/*     */ package com.appdynamics.analytics.processor.elasticsearch.index.rolling;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.admin.ForcedRolloverRequest;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.RollingIndexConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationSettings;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.ElasticSearchEventService;
/*     */ import com.appdynamics.analytics.processor.event.EventTypeMetaData;
/*     */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*     */ import com.appdynamics.analytics.processor.event.configuration.EventServiceConfiguration;
/*     */ import com.appdynamics.analytics.processor.event.metadata.EventTypeMetaDataService;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
/*     */ import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
/*     */ import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndexStats;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.cluster.metadata.AliasMetaData;
/*     */ import org.elasticsearch.cluster.metadata.MappingMetaData;
/*     */ import org.elasticsearch.common.collect.ImmutableOpenMap;
/*     */ import org.elasticsearch.common.compress.CompressedString;
/*     */ import org.elasticsearch.common.hppc.cursors.ObjectCursor;
/*     */ import org.elasticsearch.index.store.StoreStats;
/*     */ import org.joda.time.DateTime;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class RollingIndexManager
/*     */ {
/*  53 */   private static final Logger log = LoggerFactory.getLogger(RollingIndexManager.class);
/*     */   
/*     */   private final ClientProvider clientProvider;
/*     */   
/*     */   private final IndexNameResolver indexNameResolver;
/*     */   private final EventTypeMetaDataService eventTypeMetaDataService;
/*     */   private final IndexCreationManager indexCreationManager;
/*     */   private final RollingIndexConfiguration rollingIndexConfiguration;
/*     */   private final RollingIndexShardSizingStrategy rollingIndexShardSizingStrategy;
/*     */   private final MappingMergeStrategy defaultMergeStrategy;
/*  63 */   private final RollingIndexShardSizingStrategy currentShardCountStrategy = new CurrentShardCountIndexShardSizingStrategy();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final EventServiceConfiguration eventServiceConfiguration;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RollingIndexManager(ClientProvider clientProvider, IndexNameResolver indexNameResolver, EventTypeMetaDataService eventTypeMetaDataService, RollingIndexShardSizingStrategy rollingIndexShardSizingStrategy, IndexCreationManager indexCreationManager, RollingIndexConfiguration rollingIndexConfiguration, EventServiceConfiguration eventServiceConfiguration)
/*     */   {
/*  75 */     this.clientProvider = clientProvider;
/*  76 */     this.indexNameResolver = indexNameResolver;
/*  77 */     this.eventTypeMetaDataService = eventTypeMetaDataService;
/*  78 */     this.rollingIndexShardSizingStrategy = rollingIndexShardSizingStrategy;
/*  79 */     this.indexCreationManager = indexCreationManager;
/*  80 */     this.rollingIndexConfiguration = rollingIndexConfiguration;
/*  81 */     this.eventServiceConfiguration = eventServiceConfiguration;
/*  82 */     this.defaultMergeStrategy = new MappingMergeStrategy()
/*     */     {
/*     */       public JsonNode merge(JsonNode originalMapping, JsonNode updatedMapping) {
/*  85 */         return updatedMapping;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public boolean doRollOver() {
/*  91 */     List<String> clusterNames = this.clientProvider.getAllClusterNames();
/*  92 */     return doRollOver(false, clusterNames, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean doRollOver(boolean forced, List<String> clusterNames, JsonNode mappingOverride, MappingMergeStrategy mergeStrategy)
/*     */   {
/*  98 */     boolean rolledOver = true;
/*     */     
/* 100 */     for (String clusterName : clusterNames) {
/* 101 */       log.info("Checking for index roll over for cluster [{}]", clusterName);
/*     */       
/* 103 */       Client client = this.clientProvider.getClusterClient(clusterName);
/*     */       
/* 105 */       Set<String> insertIndices = buildSetOfInsertIndices(client);
/*     */       
/* 107 */       for (String currentIndex : insertIndices) {
/* 108 */         if (!rolloverIndex(forced, mappingOverride, mergeStrategy, clusterName, client, currentIndex, null))
/*     */         {
/* 110 */           rolledOver = false;
/*     */         }
/*     */       }
/*     */       
/* 114 */       log.debug("Finished checking for index roll over for [{}]", clusterName);
/*     */     }
/* 116 */     return rolledOver;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean rolloverIndex(boolean forced, JsonNode mappingOverride, MappingMergeStrategy mergeStrategy, String clusterName, Client client, String currentIndex, Integer numberOfShardsOverride)
/*     */   {
/* 122 */     if (mergeStrategy == null) {
/* 123 */       mergeStrategy = this.defaultMergeStrategy;
/*     */     }
/*     */     
/* 126 */     Map<String, IndexStats> indicesStats = ((IndicesStatsResponse)((IndicesStatsRequestBuilder)client.admin().indices().prepareStats(new String[0]).setIndices(new String[] { currentIndex })).execute().actionGet()).getIndices();
/*     */     
/*     */ 
/* 129 */     IndexStats indexStats = (IndexStats)indicesStats.get(currentIndex);
/*     */     
/* 131 */     if ((indexStats.getShards() != null) && (indexStats.getTotal() != null) && (indexStats.getShards().length != 0))
/*     */     {
/* 133 */       long numShards = indexStats.getShards().length;
/* 134 */       long storeSize = indexStats.getTotal().getStore().sizeInBytes();
/*     */       
/* 136 */       long averageShardSize = storeSize / numShards;
/* 137 */       long shardSizeThreshold = this.rollingIndexConfiguration.getShardSizeThreshold();
/* 138 */       if ((forced) || (averageShardSize > shardSizeThreshold) || (hasExceededLifespan(currentIndex))) {
/* 139 */         log.info("Starting index roll over for [{}] in cluster [{}]. Current index average shard size is [{}]", new Object[] { currentIndex, clusterName, Long.valueOf(averageShardSize) });
/*     */         
/*     */ 
/* 142 */         rollIndex(client, clusterName, currentIndex, indexStats, forced, mappingOverride, mergeStrategy, numberOfShardsOverride);
/*     */         
/* 144 */         return true;
/*     */       }
/* 146 */       log.debug("cluster [{}] and index [{}] average shard size is [{}] too low to rollover, numShards=[{}], storeSize=[{}], shardSizeThreshold=[{}].", new Object[] { clusterName, currentIndex, Long.valueOf(averageShardSize), Long.valueOf(numShards), Long.valueOf(storeSize), Long.valueOf(shardSizeThreshold) });
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 151 */       log.warn("Failed to retrieve index stats for cluster [{}] and index [{}], possible connection issue.", clusterName, currentIndex);
/*     */     }
/*     */     
/*     */ 
/* 155 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasExceededLifespan(String currentIndex) {
/* 159 */     DateTime indexCreationDate = this.indexNameResolver.indexCreationDateFromFullName(currentIndex);
/* 160 */     int hotLifespan = findHotLifespan(currentIndex) + 1;
/* 161 */     int rollDays = Math.min(hotLifespan, this.rollingIndexConfiguration.getMaximumIndexLifespanInDays());
/*     */     
/* 163 */     DateTime rollByDateThreshold = new DateTime().minusDays(rollDays);
/* 164 */     return indexCreationDate.isBefore(rollByDateThreshold);
/*     */   }
/*     */   
/*     */   private int findHotLifespan(String currentIndex) {
/* 168 */     if (this.indexNameResolver.isOldIndexManagementIndex(currentIndex)) {
/* 169 */       return this.rollingIndexConfiguration.getTargetIndexLifeInDays();
/*     */     }
/*     */     
/* 172 */     String baseIndexName = this.indexNameResolver.baseIndexNameFromFullName(currentIndex);
/* 173 */     String accountName = this.indexNameResolver.accountNameFromIndex(baseIndexName);
/* 174 */     String eventType = this.indexNameResolver.eventTypeFromIndex(baseIndexName);
/* 175 */     EventTypeMetaData eventTypeMetaData = this.eventTypeMetaDataService.getEventTypeMetaData(accountName, eventType);
/*     */     
/* 177 */     int hotLifespan = eventTypeMetaData != null ? eventTypeMetaData.getHotLifespanInDays().intValue() : this.rollingIndexConfiguration.getTargetIndexLifeInDays();
/*     */     
/*     */ 
/*     */ 
/* 181 */     return hotLifespan;
/*     */   }
/*     */   
/*     */   private Set<String> buildSetOfInsertIndices(Client client)
/*     */   {
/* 186 */     ImmutableOpenMap<String, List<AliasMetaData>> aliasMetadatas = ((GetAliasesResponse)((GetAliasesRequestBuilder)client.admin().indices().prepareGetAliases(new String[0]).setAliases(new String[] { "*___insert" })).execute().actionGet()).getAliases();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */     return Sets.newHashSet(aliasMetadatas.keysIt());
/*     */   }
/*     */   
/*     */ 
/*     */   void rollIndex(Client client, String clusterName, String oldIndex, IndexStats oldIndexStats, boolean forced, JsonNode mappingOverride, MappingMergeStrategy mergeStrategy, Integer numberOfShardsOverride)
/*     */   {
/* 198 */     String baseIndexName = this.indexNameResolver.baseIndexNameFromFullName(oldIndex);
/* 199 */     String newIndex = this.indexNameResolver.appendTimestampToBaseIndexName(baseIndexName);
/*     */     
/* 201 */     boolean acknowledged = createNewIndexWithMappings(client, newIndex, oldIndex, oldIndexStats, forced, mappingOverride, mergeStrategy, numberOfShardsOverride);
/*     */     
/*     */ 
/* 204 */     if (acknowledged) {
/* 205 */       updateIndexAliasMappingsForNewIndex(client, clusterName, baseIndexName, oldIndex, newIndex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean createNewIndexWithMappings(Client client, String newIndexName, String oldIndexName, IndexStats oldIndexStats, boolean forced, JsonNode mappingOverride, MappingMergeStrategy mergeStrategy, Integer numberOfShardsOverride)
/*     */   {
/* 213 */     ESUtils.refreshIndices(client, new String[] { oldIndexName });
/*     */     
/*     */ 
/* 216 */     GetMappingsResponse getMappingsResponse = (GetMappingsResponse)((GetMappingsRequestBuilder)client.admin().indices().prepareGetMappings(new String[0]).setIndices(new String[] { oldIndexName })).execute().actionGet();
/*     */     
/* 218 */     ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indexMappings = getMappingsResponse.mappings();
/*     */     
/*     */ 
/* 221 */     int numShards = numberOfShardsOverride == null ? getNumberOfShards(oldIndexStats, forced) : numberOfShardsOverride.intValue();
/*     */     
/*     */ 
/* 224 */     IndexCreationSettings settings = new IndexCreationSettings().setNumShards(numShards);
/* 225 */     Map<String, Map<String, Object>> modifiedMappings = updateMappingFieldProperties(indexMappings, newIndexName, mappingOverride, mergeStrategy);
/*     */     
/* 227 */     return this.indexCreationManager.createIndexLocking(client, newIndexName, settings.getSettings(), null, modifiedMappings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int getNumberOfShards(IndexStats oldIndexStats, boolean forced)
/*     */   {
/* 234 */     if (forced) {
/* 235 */       return this.currentShardCountStrategy.calculateNumberOfShards(oldIndexStats);
/*     */     }
/* 237 */     return this.rollingIndexShardSizingStrategy.calculateNumberOfShards(oldIndexStats);
/*     */   }
/*     */   
/*     */   private ObjectNode getOverrideForType(JsonNode mappingOverride, String type)
/*     */   {
/* 242 */     if (mappingOverride == null) {
/* 243 */       return null;
/*     */     }
/*     */     
/* 246 */     ObjectNode newMapping = Reader.DEFAULT_JSON_MAPPER.createObjectNode();
/* 247 */     newMapping.put(type, mappingOverride.deepCopy());
/* 248 */     return newMapping;
/*     */   }
/*     */   
/*     */   private void updateIndexAliasMappingsForNewIndex(Client client, String clusterName, String activeAlias, String oldIndex, String newIndex)
/*     */   {
/* 253 */     ESUtils.refreshIndices(client, new String[] { newIndex });
/*     */     
/*     */ 
/* 256 */     ImmutableOpenMap<String, List<AliasMetaData>> aliasMappings = ((GetAliasesResponse)((GetAliasesRequestBuilder)client.admin().indices().prepareGetAliases(new String[0]).setIndices(new String[] { oldIndex })).execute().actionGet()).getAliases();
/*     */     
/*     */ 
/* 259 */     if (aliasMappings.size() == 1) {
/* 260 */       List<AliasMetaData> aliasMetaData = (List)aliasMappings.get(oldIndex);
/*     */       
/* 262 */       IndicesAliasesRequestBuilder indicesAliasesRequestBuilder = client.admin().indices().prepareAliases();
/*     */       
/* 264 */       for (AliasMetaData aliasMetadata : aliasMetaData) {
/* 265 */         String alias = aliasMetadata.getAlias();
/* 266 */         if (aliasMetadata.filteringRequired()) {
/* 267 */           indicesAliasesRequestBuilder = indicesAliasesRequestBuilder.addAlias(newIndex, alias, getFilterAsStringOrCroak(aliasMetadata));
/*     */         }
/*     */         else {
/* 270 */           indicesAliasesRequestBuilder = indicesAliasesRequestBuilder.addAlias(newIndex, alias);
/*     */         }
/* 272 */         if ((alias.equals(activeAlias)) || (this.indexNameResolver.isInsertAlias(alias))) {
/* 273 */           indicesAliasesRequestBuilder = indicesAliasesRequestBuilder.removeAlias(oldIndex, alias);
/*     */         }
/*     */       }
/*     */       
/* 277 */       IndicesAliasesResponse indicesAliasesResponse = (IndicesAliasesResponse)indicesAliasesRequestBuilder.execute().actionGet();
/*     */       
/* 279 */       if (!indicesAliasesResponse.isAcknowledged()) {
/* 280 */         log.error("Failed to create search and insert aliases for index [{}] in cluster [{}].  response=[{}]", new Object[] { newIndex, clusterName, indicesAliasesResponse });
/*     */       }
/*     */       else {
/* 283 */         log.info("Successfully created new index [{}] in cluster [{}]", newIndex, clusterName);
/*     */       }
/*     */     } else {
/* 286 */       log.error("Unable to find any alias for index [{}] in cluster [{}].  Unable to update alias mappings for new index!", oldIndex, clusterName);
/*     */     }
/*     */   }
/*     */   
/*     */   private String getFilterAsStringOrCroak(AliasMetaData aliasMetadata)
/*     */   {
/*     */     try {
/* 293 */       return aliasMetadata.filter().string();
/*     */     } catch (IOException e) {
/* 295 */       throw Throwables.propagate(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private Map<String, Map<String, Object>> updateMappingFieldProperties(ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> originalMappings, String newIndexName, JsonNode mappingOverride, MappingMergeStrategy mergeStrategy)
/*     */   {
/* 302 */     Map<String, Map<String, Object>> modifiedMap = new HashMap();
/* 303 */     for (ObjectCursor<String> indexMapping : originalMappings.keys()) {
/* 304 */       mapping = (ImmutableOpenMap)originalMappings.get(indexMapping.value);
/*     */       
/* 306 */       for (ObjectCursor<String> mappingType : mapping.keys()) {
/* 307 */         String type = (String)mappingType.value;
/* 308 */         ObjectNode updatedMapping = null;
/*     */         try
/*     */         {
/* 311 */           ObjectNode overrideMapping = getOverrideForType(mappingOverride, type);
/*     */           
/* 313 */           MappingMetaData metaData = (MappingMetaData)mapping.get(type);
/* 314 */           CompressedString mappingSource = metaData.source();
/* 315 */           String uncompressedSource = mappingSource.string();
/* 316 */           ObjectNode existingMapping = (ObjectNode)Reader.DEFAULT_JSON_MAPPER.readTree(uncompressedSource);
/*     */           
/* 318 */           updatedMapping = existingMapping;
/* 319 */           if (overrideMapping != null) {
/* 320 */             updatedMapping = (ObjectNode)mergeStrategy.merge(existingMapping, overrideMapping);
/*     */           }
/*     */           
/* 323 */           ElasticSearchEventService.performEventServiceMappingTransformation((ObjectNode)updatedMapping.get(type), false, this.eventServiceConfiguration.isAllFieldDisabled());
/*     */           
/*     */ 
/* 326 */           modifiedMap.put(type, Reader.DEFAULT_JSON_MAPPER.convertValue(updatedMapping, new TypeReference() {}));
/*     */         }
/*     */         catch (Exception e) {
/* 329 */           log.error("Failed to add mapping type [{}] when creating index [{}], sourceAsMap is [{}]", new Object[] { type, newIndexName, updatedMapping });
/*     */           
/* 331 */           throw Throwables.propagate(e);
/*     */         }
/*     */       } }
/*     */     ImmutableOpenMap<String, MappingMetaData> mapping;
/* 335 */     return modifiedMap;
/*     */   }
/*     */   
/*     */   public void forceRollOver(ForcedRolloverRequest forcedRolloverRequest) {
/* 339 */     if (forcedRolloverRequest == null) {
/* 340 */       doRollOver(true, this.clientProvider.getAllClusterNames(), null, null);
/*     */     } else {
/* 342 */       Preconditions.checkArgument((!Strings.isNullOrEmpty(forcedRolloverRequest.getIndex())) && (!Strings.isNullOrEmpty(forcedRolloverRequest.getCluster())), "Forced index rollover request must specify both the index and cluster to force the rollover on");
/*     */       
/*     */ 
/*     */ 
/* 346 */       rolloverIndex(true, forcedRolloverRequest.getEventTypeMappingOverride(), forcedRolloverRequest.getMappingMergeStrategy(), forcedRolloverRequest.getCluster(), this.clientProvider.getClusterClient(forcedRolloverRequest.getCluster()), forcedRolloverRequest.getIndex(), forcedRolloverRequest.getNumberOfShards());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/RollingIndexManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */