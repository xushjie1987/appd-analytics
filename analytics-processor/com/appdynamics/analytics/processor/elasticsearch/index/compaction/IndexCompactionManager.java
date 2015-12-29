/*     */ package com.appdynamics.analytics.processor.elasticsearch.index.compaction;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationSettings;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.maintenance.Utilities;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.annotation.Nullable;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
/*     */ import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
/*     */ import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
/*     */ import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
/*     */ import org.elasticsearch.action.admin.indices.close.CloseIndexRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.close.CloseIndexResponse;
/*     */ import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
/*     */ import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndexStats;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
/*     */ import org.elasticsearch.action.count.CountRequestBuilder;
/*     */ import org.elasticsearch.action.count.CountResponse;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.cluster.ClusterName;
/*     */ import org.elasticsearch.cluster.metadata.AliasMetaData;
/*     */ import org.elasticsearch.cluster.metadata.MappingMetaData;
/*     */ import org.elasticsearch.common.bytes.BytesArray;
/*     */ import org.elasticsearch.common.collect.ImmutableOpenMap;
/*     */ import org.elasticsearch.common.collect.UnmodifiableIterator;
/*     */ import org.elasticsearch.common.compress.CompressedString;
/*     */ import org.elasticsearch.common.hppc.ObjectContainer;
/*     */ import org.elasticsearch.common.hppc.cursors.ObjectCursor;
/*     */ import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
/*     */ import org.elasticsearch.common.settings.Settings;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.index.query.BoolFilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilders;
/*     */ import org.elasticsearch.index.query.FilteredQueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.index.query.WrapperFilterBuilder;
/*     */ import org.elasticsearch.index.store.StoreStats;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class IndexCompactionManager
/*     */ {
/*  75 */   private static final Logger log = LoggerFactory.getLogger(IndexCompactionManager.class);
/*     */   
/*     */ 
/*     */ 
/*     */   public static final long DEFAULT_SCROLL_BATCH_SIZE = 500L;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final long DEFAULT_SCROLL_TIMEOUT = 180000L;
/*     */   
/*     */ 
/*     */ 
/*  87 */   public static final ImmutableMap<String, Object> OPTIMIZED_BULK_INDEX_SETTINGS = ImmutableMap.of("index.number_of_replicas", "0", "index.refresh_interval", "-1");
/*     */   
/*     */   public static final int WAIT_BEFORE_CANCEL_SECS = 86400;
/*     */   
/*     */   private final ClientProvider clientProvider;
/*     */   
/*     */   private final IndexCreationManager indexCreationManager;
/*     */   
/*     */   private final IndexNameResolver indexNameResolver;
/*     */   
/*     */   private final long idealShardSize;
/*     */   
/*     */   private final IndexCompactionParameters indexCompactionParameters;
/*     */   
/*     */   private final TimeValue esCallTimeout;
/*     */   
/*     */ 
/*     */   public IndexCompactionManager(TimeValue esCallTimeout, ClientProvider clientProvider, IndexNameResolver indexNameResolver, int flushThresholdInPercent, long idealShardSize, IndexCreationManager indexCreationManager, boolean debugMode)
/*     */   {
/* 106 */     this.clientProvider = clientProvider;
/* 107 */     this.indexNameResolver = indexNameResolver;
/* 108 */     this.indexCreationManager = indexCreationManager;
/* 109 */     this.idealShardSize = idealShardSize;
/* 110 */     this.indexCompactionParameters = new IndexCompactionParameters();
/* 111 */     this.indexCompactionParameters.debugMode = Boolean.valueOf(debugMode);
/* 112 */     this.indexCompactionParameters.flushThreshold = Double.valueOf(flushThresholdInPercent);
/* 113 */     this.indexCompactionParameters.scrollTimeout = Long.valueOf(180000L);
/* 114 */     this.indexCompactionParameters.scrollBatchSize = Long.valueOf(500L);
/* 115 */     this.indexCompactionParameters.copyThreadPoolSize = Integer.valueOf(3);
/* 116 */     this.indexCompactionParameters.numberOfParallelCopiers = Integer.valueOf(3);
/* 117 */     this.esCallTimeout = esCallTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean compactData(IndexCompactionParameters indexCompactionParameters)
/*     */   {
/* 125 */     log.info("Compaction task started with input body: {}", indexCompactionParameters);
/* 126 */     Client clusterClient = this.clientProvider.getClusterClient(indexCompactionParameters.clusterToProcess);
/* 127 */     List<AliasMetaData> aliasesToCover = getSearchAliasesFromIndex(indexCompactionParameters.indexToCompact, clusterClient);
/*     */     
/* 129 */     if (aliasesToCover == null) {
/* 130 */       log.info("Could not find aliases for index [{}], have to skip this index.", indexCompactionParameters.indexToCompact);
/*     */       
/* 132 */       return false;
/*     */     }
/* 134 */     indexCompactionParameters.mergeIndexCompactionParametersWithDefaults(this.indexCompactionParameters);
/* 135 */     compactIndexNow(clusterClient, indexCompactionParameters.getIndexToCompact(), aliasesToCover, indexCompactionParameters);
/*     */     
/* 137 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<AliasMetaData> getSearchAliasesFromIndex(String index, Client cluster)
/*     */   {
/* 147 */     if (!this.indexNameResolver.isEventServiceIndex(index)) {
/* 148 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 151 */     GetAliasesResponse response = (GetAliasesResponse)((GetAliasesRequestBuilder)cluster.admin().indices().prepareGetAliases(new String[] { "*___search" }).setIndices(new String[] { index })).execute().actionGet();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */     int size = response.getAliases().values().size();
/* 158 */     if (size == 0)
/* 159 */       return Collections.emptyList();
/* 160 */     if (size > 1) {
/* 161 */       throw new IllegalArgumentException("Expected list to have exactly one element, but was [" + size + "]");
/*     */     }
/* 163 */     return (List)response.getAliases().valuesIt().next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean compactData()
/*     */   {
/* 171 */     log.info("Compaction task started on all clusters/indices!");
/* 172 */     boolean allSuccessful = true;
/* 173 */     for (Client cluster : this.clientProvider.getAllInsertClients()) {
/* 174 */       boolean thisClusterSuccessful = compactCluster(cluster);
/* 175 */       if (!thisClusterSuccessful) {
/* 176 */         allSuccessful = false;
/*     */       }
/*     */     }
/* 179 */     return allSuccessful;
/*     */   }
/*     */   
/*     */   private boolean compactCluster(Client cluster)
/*     */   {
/* 184 */     String clusterName = "could not get cluster name";
/*     */     try {
/* 186 */       ClusterStatsResponse clusterStats = ESUtils.getClusterStats(cluster);
/* 187 */       clusterName = clusterStats.getClusterName().toString();
/* 188 */       compactIndices(cluster);
/*     */     } catch (Exception e) {
/* 190 */       log.error("Exception when compacting indices in cluster [{}] of old data: {}", new Object[] { clusterName, e.getMessage(), e });
/*     */       
/* 192 */       return false;
/*     */     }
/* 194 */     return true;
/*     */   }
/*     */   
/*     */   private void compactIndices(Client clusterClient) {
/* 198 */     Map<String, List<AliasMetaData>> indexNamesToSearchAliases = getAllSearchIndices(clusterClient);
/*     */     
/*     */ 
/* 201 */     for (Map.Entry<String, List<AliasMetaData>> entry : indexNamesToSearchAliases.entrySet()) {
/* 202 */       String indexName = (String)entry.getKey();
/* 203 */       List<AliasMetaData> aliasList = (List)entry.getValue();
/* 204 */       compactIndexNow(clusterClient, indexName, aliasList, this.indexCompactionParameters);
/*     */     }
/*     */   }
/*     */   
/*     */   private Map<String, List<AliasMetaData>> getAllSearchIndices(Client clusterClient) {
/* 209 */     GetAliasesResponse aliasesResponse = (GetAliasesResponse)clusterClient.admin().indices().prepareGetAliases(new String[] { "*___search" }).execute().actionGet();
/*     */     
/*     */ 
/* 212 */     Map<String, List<AliasMetaData>> indexNameToSearchAliases = new HashMap();
/* 213 */     for (ObjectObjectCursor<String, List<AliasMetaData>> indexNameToAliasList : aliasesResponse.getAliases()) {
/* 214 */       String indexName = (String)indexNameToAliasList.key;
/* 215 */       List<AliasMetaData> aliasList = (List)indexNameToAliasList.value;
/*     */       
/* 217 */       if (shouldCompactIndex(indexName, clusterClient))
/*     */       {
/* 219 */         List<AliasMetaData> searchAliases = filterSearchAliases(aliasList);
/* 220 */         indexNameToSearchAliases.put(indexName, searchAliases);
/*     */       }
/*     */     }
/* 223 */     return indexNameToSearchAliases;
/*     */   }
/*     */   
/*     */   private void compactIndexNow(Client cluster, String indexName, List<AliasMetaData> allAliases, IndexCompactionParameters indexCompactionParameters)
/*     */   {
/* 228 */     if (shouldCompactIndex(indexName, cluster)) {
/* 229 */       new IndexCompactionTask(this, this.clientProvider, indexName, allAliases, indexCompactionParameters, cluster.settings().get("cluster.name")).run();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private FilteredQueryBuilder getCopyQueryContent(Map<String, WrapperFilterBuilder> aliasToFilter)
/*     */   {
/* 242 */     WrapperFilterBuilder[] filterBuilderArray = (WrapperFilterBuilder[])aliasToFilter.values().toArray(new WrapperFilterBuilder[aliasToFilter.values().size()]);
/*     */     
/*     */ 
/* 245 */     FilterBuilder combinedFilters = FilterBuilders.boolFilter().should(filterBuilderArray);
/*     */     
/* 247 */     FilterBuilder finalFilter = FilterBuilders.boolFilter().must(combinedFilters);
/*     */     
/* 249 */     return QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), finalFilter);
/*     */   }
/*     */   
/*     */   private boolean shouldCompactIndex(String indexName, Client cluster) {
/* 253 */     if ((!this.indexNameResolver.isOldIndexManagementIndex(indexName)) && (!this.indexNameResolver.isEventServiceIndex(indexName)))
/*     */     {
/* 255 */       return false;
/*     */     }
/*     */     boolean isOldIndex;
/*     */     try {
/* 259 */       isOldIndex = hasIndexRolled(indexName, cluster);
/*     */     } catch (IllegalArgumentException e) {
/* 261 */       log.info("Skipping index [" + indexName + "] - probably not a valid event service index");
/* 262 */       return false;
/*     */     }
/* 264 */     if (!isOldIndex) {
/* 265 */       log.info("Skipping index [{}] since it has not rolled yet", indexName);
/* 266 */       return false;
/*     */     }
/* 268 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean hasIndexRolled(String indexName, Client clusterClient)
/*     */   {
/* 278 */     if (this.indexNameResolver.isOldIndexManagementIndex(indexName)) {
/* 279 */       return hasOldIndexManagementIndexRolled(indexName, clusterClient);
/*     */     }
/*     */     
/* 282 */     return !((AliasesExistResponse)((AliasesExistRequestBuilder)clusterClient.admin().indices().prepareAliasesExist(new String[] { this.indexNameResolver.baseIndexNameFromFullName(indexName) }).setIndices(new String[] { indexName })).execute().actionGet()).exists();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean hasOldIndexManagementIndexRolled(String indexName, Client clusterClient)
/*     */   {
/* 289 */     if (!((AliasesExistResponse)((AliasesExistRequestBuilder)clusterClient.admin().indices().prepareAliasesExist(new String[] { this.indexNameResolver.oldIndexManagementDynamicEventTypesInsertAlias() }).setIndices(new String[] { indexName })).execute().actionGet()).exists()) {} return !((AliasesExistResponse)((AliasesExistRequestBuilder)clusterClient.admin().indices().prepareAliasesExist(new String[] { this.indexNameResolver.oldIndexManagementStaticEventTypesInsertAlias() }).setIndices(new String[] { indexName })).execute().actionGet()).exists();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void compactIndexIfRequired(Client client, String indexName, long numberOfExpiredDocs, long numberOfLiveDocuments, IndexCompactionParameters indexCompactionParameters)
/*     */   {
/* 300 */     long total = numberOfExpiredDocs + numberOfLiveDocuments;
/* 301 */     double percentExpired = (total == 0L) || (numberOfExpiredDocs == total) ? 100.0D : 100.0D * (numberOfExpiredDocs / total);
/*     */     
/*     */ 
/* 304 */     log.info("Percent docs expired on index [{}] is now [{}].", indexName, Double.valueOf(percentExpired));
/*     */     
/* 306 */     boolean shouldDeleteOldIndex = false;
/* 307 */     if ((this.indexNameResolver.isOldIndexManagementIndex(indexName)) && (numberOfExpiredDocs < total) && (Double.compare(percentExpired, indexCompactionParameters.flushThreshold.doubleValue()) >= 0))
/*     */     {
/* 309 */       shouldDeleteOldIndex = moveLiveDocumentsToNewIndex(client, indexName, numberOfLiveDocuments, numberOfExpiredDocs, total, indexCompactionParameters);
/*     */     }
/* 311 */     else if (numberOfExpiredDocs == total) {
/* 312 */       log.info((indexCompactionParameters.debugMode.booleanValue() ? "[TEST MODE] - would have deleted " : "Deleting ") + " index [{}] - all documents are expired.", indexName);
/*     */       
/* 314 */       shouldDeleteOldIndex = !indexCompactionParameters.debugMode.booleanValue();
/*     */     }
/*     */     
/* 317 */     if (shouldDeleteOldIndex) {
/* 318 */       log.info("Deleting index [{}]", indexName);
/* 319 */       deleteIndex(client, indexName, "Unable to delete original index [" + indexName + "] - new index already created and does contain the live documents from old index");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   boolean moveLiveDocumentsToNewIndex(Client client, String indexName, long numberOfLiveDocuments, long numberOfExpiredDocuments, long total, IndexCompactionParameters indexCompactionParameters)
/*     */   {
/* 329 */     if (indexCompactionParameters.debugMode.booleanValue()) {
/* 330 */       log.info("[TEST MODE] - would have moved [{}] live documents to new index and deleted [{}]", Long.valueOf(total - numberOfExpiredDocuments), indexName);
/*     */       
/* 332 */       return false;
/*     */     }
/* 334 */     String newIndexName = createIndexToReceiveLiveDocuments(client, indexName, numberOfExpiredDocuments, total);
/* 335 */     log.info("Moving live documents from index [{}] to new index [{}]", indexName, newIndexName);
/*     */     boolean copySuccessful;
/*     */     try {
/* 338 */       copySuccessful = copyLiveDocumentsToNewIndex(client, indexName, newIndexName, indexCompactionParameters);
/*     */     } catch (Exception e) {
/* 340 */       copySuccessful = false;
/* 341 */       log.error("Error while copying documents to new index [{}] from old index [{}]: {}", new Object[] { newIndexName, indexName, e.getMessage(), e });
/*     */     }
/*     */     
/* 344 */     if ((copySuccessful) && (verifyLiveDocumentsInNewIndex(client, newIndexName, numberOfLiveDocuments))) {
/* 345 */       addAliasesToNewIndex(client, indexName, newIndexName);
/* 346 */       return true;
/*     */     }
/* 348 */     log.error("Document count verification failed in new index, deleting new index [{}] - old index [{}]", newIndexName, indexName);
/*     */     
/* 350 */     deleteIndex(client, newIndexName, "Unable to delete new index [" + newIndexName + "] - was cleaning " + "up index because verification failed after copying live documents from old index [" + indexName + "]");
/*     */     
/*     */ 
/* 353 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   void deleteIndex(Client client, String indexName, String msg)
/*     */   {
/* 360 */     if (!((CloseIndexResponse)((CloseIndexRequestBuilder)((CloseIndexRequestBuilder)client.admin().indices().prepareClose(new String[] { indexName }).setMasterNodeTimeout(this.esCallTimeout)).setTimeout(this.esCallTimeout)).execute().actionGet(this.esCallTimeout)).isAcknowledged())
/*     */     {
/*     */ 
/* 363 */       log.error("Could not close index [{}] - failed to acknowledge.", indexName);
/*     */     }
/* 365 */     else if (!((DeleteIndexResponse)((DeleteIndexRequestBuilder)client.admin().indices().prepareDelete(new String[] { indexName }).setMasterNodeTimeout(this.esCallTimeout)).setTimeout(this.esCallTimeout).execute().actionGet(this.esCallTimeout)).isAcknowledged())
/*     */     {
/*     */ 
/* 368 */       log.error(msg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean verifyLiveDocumentsInNewIndex(Client client, String newIndexName, long numberOfLiveDocuments)
/*     */   {
/* 375 */     ESUtils.refreshIndices(client, new String[] { newIndexName });
/* 376 */     long countInNewIndex = ((CountResponse)client.prepareCount(new String[] { newIndexName }).execute().actionGet(this.esCallTimeout)).getCount();
/* 377 */     boolean verified = numberOfLiveDocuments == countInNewIndex;
/* 378 */     if (!verified) {
/* 379 */       log.error("New index [{}] expected to contain [{}] documents, but only contained [{}]", new Object[] { newIndexName, Long.valueOf(numberOfLiveDocuments), Long.valueOf(countInNewIndex) });
/*     */     }
/*     */     
/* 382 */     return verified;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private boolean copyLiveDocumentsToNewIndex(final Client client, final String indexName, final String newIndexName, final IndexCompactionParameters indexCompactionParameters)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokeinterface 43 1 0
/*     */     //   6: invokeinterface 44 1 0
/*     */     //   11: iconst_1
/*     */     //   12: anewarray 45	java/lang/String
/*     */     //   15: dup
/*     */     //   16: iconst_0
/*     */     //   17: aload_3
/*     */     //   18: aastore
/*     */     //   19: invokeinterface 183 2 0
/*     */     //   24: aload_0
/*     */     //   25: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   28: invokevirtual 184	org/elasticsearch/action/admin/indices/settings/get/GetSettingsRequestBuilder:setMasterNodeTimeout	(Lorg/elasticsearch/common/unit/TimeValue;)Lorg/elasticsearch/action/support/master/MasterNodeOperationRequestBuilder;
/*     */     //   31: checkcast 185	org/elasticsearch/action/admin/indices/settings/get/GetSettingsRequestBuilder
/*     */     //   34: invokevirtual 186	org/elasticsearch/action/admin/indices/settings/get/GetSettingsRequestBuilder:execute	()Lorg/elasticsearch/action/ListenableActionFuture;
/*     */     //   37: aload_0
/*     */     //   38: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   41: invokeinterface 164 2 0
/*     */     //   46: checkcast 187	org/elasticsearch/action/admin/indices/settings/get/GetSettingsResponse
/*     */     //   49: astore 5
/*     */     //   51: aload 5
/*     */     //   53: aload_3
/*     */     //   54: ldc -68
/*     */     //   56: invokevirtual 189	org/elasticsearch/action/admin/indices/settings/get/GetSettingsResponse:getSetting	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   59: ldc -66
/*     */     //   61: invokestatic 191	com/google/common/base/Objects:firstNonNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   64: checkcast 45	java/lang/String
/*     */     //   67: astore 6
/*     */     //   69: aload 5
/*     */     //   71: aload_3
/*     */     //   72: ldc -64
/*     */     //   74: invokevirtual 189	org/elasticsearch/action/admin/indices/settings/get/GetSettingsResponse:getSetting	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   77: ldc -63
/*     */     //   79: invokestatic 191	com/google/common/base/Objects:firstNonNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   82: checkcast 45	java/lang/String
/*     */     //   85: astore 7
/*     */     //   87: aload_1
/*     */     //   88: invokeinterface 43 1 0
/*     */     //   93: invokeinterface 44 1 0
/*     */     //   98: iconst_1
/*     */     //   99: anewarray 45	java/lang/String
/*     */     //   102: dup
/*     */     //   103: iconst_0
/*     */     //   104: aload_3
/*     */     //   105: aastore
/*     */     //   106: invokeinterface 194 2 0
/*     */     //   111: getstatic 195	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:OPTIMIZED_BULK_INDEX_SETTINGS	Lcom/google/common/collect/ImmutableMap;
/*     */     //   114: invokevirtual 196	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setSettings	(Ljava/util/Map;)Lorg/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder;
/*     */     //   117: aload_0
/*     */     //   118: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   121: invokevirtual 197	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setMasterNodeTimeout	(Lorg/elasticsearch/common/unit/TimeValue;)Lorg/elasticsearch/action/support/master/MasterNodeOperationRequestBuilder;
/*     */     //   124: checkcast 198	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder
/*     */     //   127: aload_0
/*     */     //   128: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   131: invokevirtual 199	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setTimeout	(Lorg/elasticsearch/common/unit/TimeValue;)Lorg/elasticsearch/action/support/master/AcknowledgedRequestBuilder;
/*     */     //   134: checkcast 198	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder
/*     */     //   137: invokevirtual 200	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:execute	()Lorg/elasticsearch/action/ListenableActionFuture;
/*     */     //   140: aload_0
/*     */     //   141: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   144: invokeinterface 164 2 0
/*     */     //   149: pop
/*     */     //   150: aload_1
/*     */     //   151: invokeinterface 43 1 0
/*     */     //   156: invokeinterface 44 1 0
/*     */     //   161: iconst_1
/*     */     //   162: anewarray 45	java/lang/String
/*     */     //   165: dup
/*     */     //   166: iconst_0
/*     */     //   167: aload_2
/*     */     //   168: aastore
/*     */     //   169: invokeinterface 201 2 0
/*     */     //   174: aload_0
/*     */     //   175: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   178: invokevirtual 202	org/elasticsearch/action/admin/indices/mapping/get/GetMappingsRequestBuilder:setMasterNodeTimeout	(Lorg/elasticsearch/common/unit/TimeValue;)Lorg/elasticsearch/action/support/master/MasterNodeOperationRequestBuilder;
/*     */     //   181: checkcast 203	org/elasticsearch/action/admin/indices/mapping/get/GetMappingsRequestBuilder
/*     */     //   184: invokevirtual 204	org/elasticsearch/action/admin/indices/mapping/get/GetMappingsRequestBuilder:execute	()Lorg/elasticsearch/action/ListenableActionFuture;
/*     */     //   187: aload_0
/*     */     //   188: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   191: invokeinterface 164 2 0
/*     */     //   196: checkcast 205	org/elasticsearch/action/admin/indices/mapping/get/GetMappingsResponse
/*     */     //   199: astore 8
/*     */     //   201: aload 8
/*     */     //   203: invokevirtual 206	org/elasticsearch/action/admin/indices/mapping/get/GetMappingsResponse:mappings	()Lorg/elasticsearch/common/collect/ImmutableOpenMap;
/*     */     //   206: invokevirtual 65	org/elasticsearch/common/collect/ImmutableOpenMap:valuesIt	()Lorg/elasticsearch/common/collect/UnmodifiableIterator;
/*     */     //   209: astore 9
/*     */     //   211: aload 4
/*     */     //   213: getfield 30	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionParameters:numberOfParallelCopiers	Ljava/lang/Integer;
/*     */     //   216: invokevirtual 207	java/lang/Integer:intValue	()I
/*     */     //   219: invokestatic 208	java/util/concurrent/Executors:newFixedThreadPool	(I)Ljava/util/concurrent/ExecutorService;
/*     */     //   222: astore 10
/*     */     //   224: iconst_1
/*     */     //   225: istore 11
/*     */     //   227: aload 9
/*     */     //   229: invokevirtual 209	org/elasticsearch/common/collect/UnmodifiableIterator:hasNext	()Z
/*     */     //   232: ifeq +202 -> 434
/*     */     //   235: aload 9
/*     */     //   237: invokevirtual 66	org/elasticsearch/common/collect/UnmodifiableIterator:next	()Ljava/lang/Object;
/*     */     //   240: checkcast 210	org/elasticsearch/common/collect/ImmutableOpenMap
/*     */     //   243: astore 12
/*     */     //   245: aload 12
/*     */     //   247: invokevirtual 211	org/elasticsearch/common/collect/ImmutableOpenMap:keysIt	()Lorg/elasticsearch/common/collect/UnmodifiableIterator;
/*     */     //   250: astore 13
/*     */     //   252: new 212	java/util/ArrayList
/*     */     //   255: dup
/*     */     //   256: invokespecial 213	java/util/ArrayList:<init>	()V
/*     */     //   259: astore 14
/*     */     //   261: aload 13
/*     */     //   263: invokevirtual 209	org/elasticsearch/common/collect/UnmodifiableIterator:hasNext	()Z
/*     */     //   266: ifeq +77 -> 343
/*     */     //   269: aload 13
/*     */     //   271: invokevirtual 66	org/elasticsearch/common/collect/UnmodifiableIterator:next	()Ljava/lang/Object;
/*     */     //   274: checkcast 45	java/lang/String
/*     */     //   277: astore 15
/*     */     //   279: aload 12
/*     */     //   281: aload 15
/*     */     //   283: invokevirtual 214	org/elasticsearch/common/collect/ImmutableOpenMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   286: checkcast 215	org/elasticsearch/cluster/metadata/MappingMetaData
/*     */     //   289: invokevirtual 216	org/elasticsearch/cluster/metadata/MappingMetaData:source	()Lorg/elasticsearch/common/compress/CompressedString;
/*     */     //   292: invokevirtual 217	org/elasticsearch/common/compress/CompressedString:string	()Ljava/lang/String;
/*     */     //   295: astore 16
/*     */     //   297: goto +11 -> 308
/*     */     //   300: astore 17
/*     */     //   302: aload 17
/*     */     //   304: invokestatic 219	com/google/common/base/Throwables:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
/*     */     //   307: athrow
/*     */     //   308: aload 14
/*     */     //   310: aload 10
/*     */     //   312: new 220	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager$1
/*     */     //   315: dup
/*     */     //   316: aload_0
/*     */     //   317: aload_1
/*     */     //   318: aload_2
/*     */     //   319: aload 15
/*     */     //   321: aload_3
/*     */     //   322: aload 4
/*     */     //   324: aload 16
/*     */     //   326: invokespecial 221	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager$1:<init>	(Lcom/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager;Lorg/elasticsearch/client/Client;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionParameters;Ljava/lang/String;)V
/*     */     //   329: invokeinterface 222 2 0
/*     */     //   334: invokeinterface 223 2 0
/*     */     //   339: pop
/*     */     //   340: goto -79 -> 261
/*     */     //   343: aload 14
/*     */     //   345: invokeinterface 71 1 0
/*     */     //   350: astore 15
/*     */     //   352: aload 15
/*     */     //   354: invokeinterface 72 1 0
/*     */     //   359: ifeq +72 -> 431
/*     */     //   362: aload 15
/*     */     //   364: invokeinterface 73 1 0
/*     */     //   369: checkcast 224	java/util/concurrent/Future
/*     */     //   372: astore 16
/*     */     //   374: aload 16
/*     */     //   376: ldc -31
/*     */     //   378: getstatic 8	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:log	Lorg/slf4j/Logger;
/*     */     //   381: invokestatic 226	com/appdynamics/common/util/concurrent/ConcurrencyHelper:getOrCancel	(Ljava/util/concurrent/Future;ILorg/slf4j/Logger;)Ljava/lang/Object;
/*     */     //   384: pop
/*     */     //   385: goto +43 -> 428
/*     */     //   388: astore 17
/*     */     //   390: getstatic 8	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:log	Lorg/slf4j/Logger;
/*     */     //   393: ldc -29
/*     */     //   395: iconst_4
/*     */     //   396: anewarray 83	java/lang/Object
/*     */     //   399: dup
/*     */     //   400: iconst_0
/*     */     //   401: aload_2
/*     */     //   402: aastore
/*     */     //   403: dup
/*     */     //   404: iconst_1
/*     */     //   405: aload_3
/*     */     //   406: aastore
/*     */     //   407: dup
/*     */     //   408: iconst_2
/*     */     //   409: aload 17
/*     */     //   411: invokevirtual 84	java/lang/Exception:getMessage	()Ljava/lang/String;
/*     */     //   414: aastore
/*     */     //   415: dup
/*     */     //   416: iconst_3
/*     */     //   417: aload 17
/*     */     //   419: aastore
/*     */     //   420: invokeinterface 85 3 0
/*     */     //   425: iconst_0
/*     */     //   426: istore 11
/*     */     //   428: goto -76 -> 352
/*     */     //   431: goto -204 -> 227
/*     */     //   434: aload 10
/*     */     //   436: bipush 30
/*     */     //   438: getstatic 8	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:log	Lorg/slf4j/Logger;
/*     */     //   441: invokestatic 228	com/appdynamics/common/util/concurrent/ConcurrencyHelper:stop	(Ljava/util/concurrent/ExecutorService;ILorg/slf4j/Logger;)Z
/*     */     //   444: pop
/*     */     //   445: goto +19 -> 464
/*     */     //   448: astore 18
/*     */     //   450: aload 10
/*     */     //   452: bipush 30
/*     */     //   454: getstatic 8	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:log	Lorg/slf4j/Logger;
/*     */     //   457: invokestatic 228	com/appdynamics/common/util/concurrent/ConcurrencyHelper:stop	(Ljava/util/concurrent/ExecutorService;ILorg/slf4j/Logger;)Z
/*     */     //   460: pop
/*     */     //   461: aload 18
/*     */     //   463: athrow
/*     */     //   464: iload 11
/*     */     //   466: istore 12
/*     */     //   468: aload_1
/*     */     //   469: invokeinterface 43 1 0
/*     */     //   474: invokeinterface 44 1 0
/*     */     //   479: iconst_1
/*     */     //   480: anewarray 45	java/lang/String
/*     */     //   483: dup
/*     */     //   484: iconst_0
/*     */     //   485: aload_3
/*     */     //   486: aastore
/*     */     //   487: invokeinterface 194 2 0
/*     */     //   492: ldc -68
/*     */     //   494: aload 6
/*     */     //   496: ldc -64
/*     */     //   498: aload 7
/*     */     //   500: invokestatic 229	com/google/common/collect/ImmutableMap:of	(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
/*     */     //   503: invokevirtual 196	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setSettings	(Ljava/util/Map;)Lorg/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder;
/*     */     //   506: aload_0
/*     */     //   507: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   510: invokevirtual 197	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setMasterNodeTimeout	(Lorg/elasticsearch/common/unit/TimeValue;)Lorg/elasticsearch/action/support/master/MasterNodeOperationRequestBuilder;
/*     */     //   513: checkcast 198	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder
/*     */     //   516: aload_0
/*     */     //   517: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   520: invokevirtual 199	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setTimeout	(Lorg/elasticsearch/common/unit/TimeValue;)Lorg/elasticsearch/action/support/master/AcknowledgedRequestBuilder;
/*     */     //   523: checkcast 198	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder
/*     */     //   526: invokevirtual 200	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:execute	()Lorg/elasticsearch/action/ListenableActionFuture;
/*     */     //   529: aload_0
/*     */     //   530: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   533: invokeinterface 164 2 0
/*     */     //   538: pop
/*     */     //   539: iload 12
/*     */     //   541: ireturn
/*     */     //   542: astore 19
/*     */     //   544: aload_1
/*     */     //   545: invokeinterface 43 1 0
/*     */     //   550: invokeinterface 44 1 0
/*     */     //   555: iconst_1
/*     */     //   556: anewarray 45	java/lang/String
/*     */     //   559: dup
/*     */     //   560: iconst_0
/*     */     //   561: aload_3
/*     */     //   562: aastore
/*     */     //   563: invokeinterface 194 2 0
/*     */     //   568: ldc -68
/*     */     //   570: aload 6
/*     */     //   572: ldc -64
/*     */     //   574: aload 7
/*     */     //   576: invokestatic 229	com/google/common/collect/ImmutableMap:of	(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
/*     */     //   579: invokevirtual 196	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setSettings	(Ljava/util/Map;)Lorg/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder;
/*     */     //   582: aload_0
/*     */     //   583: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   586: invokevirtual 197	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setMasterNodeTimeout	(Lorg/elasticsearch/common/unit/TimeValue;)Lorg/elasticsearch/action/support/master/MasterNodeOperationRequestBuilder;
/*     */     //   589: checkcast 198	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder
/*     */     //   592: aload_0
/*     */     //   593: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   596: invokevirtual 199	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:setTimeout	(Lorg/elasticsearch/common/unit/TimeValue;)Lorg/elasticsearch/action/support/master/AcknowledgedRequestBuilder;
/*     */     //   599: checkcast 198	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder
/*     */     //   602: invokevirtual 200	org/elasticsearch/action/admin/indices/settings/put/UpdateSettingsRequestBuilder:execute	()Lorg/elasticsearch/action/ListenableActionFuture;
/*     */     //   605: aload_0
/*     */     //   606: getfield 7	com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager:esCallTimeout	Lorg/elasticsearch/common/unit/TimeValue;
/*     */     //   609: invokeinterface 164 2 0
/*     */     //   614: pop
/*     */     //   615: aload 19
/*     */     //   617: athrow
/*     */     // Line number table:
/*     */     //   Java source line #388	-> byte code offset #0
/*     */     //   Java source line #391	-> byte code offset #51
/*     */     //   Java source line #394	-> byte code offset #69
/*     */     //   Java source line #397	-> byte code offset #87
/*     */     //   Java source line #401	-> byte code offset #150
/*     */     //   Java source line #405	-> byte code offset #201
/*     */     //   Java source line #408	-> byte code offset #211
/*     */     //   Java source line #411	-> byte code offset #224
/*     */     //   Java source line #413	-> byte code offset #227
/*     */     //   Java source line #414	-> byte code offset #235
/*     */     //   Java source line #415	-> byte code offset #245
/*     */     //   Java source line #416	-> byte code offset #252
/*     */     //   Java source line #417	-> byte code offset #261
/*     */     //   Java source line #418	-> byte code offset #269
/*     */     //   Java source line #421	-> byte code offset #279
/*     */     //   Java source line #424	-> byte code offset #297
/*     */     //   Java source line #422	-> byte code offset #300
/*     */     //   Java source line #423	-> byte code offset #302
/*     */     //   Java source line #425	-> byte code offset #308
/*     */     //   Java source line #454	-> byte code offset #340
/*     */     //   Java source line #455	-> byte code offset #343
/*     */     //   Java source line #457	-> byte code offset #374
/*     */     //   Java source line #463	-> byte code offset #385
/*     */     //   Java source line #458	-> byte code offset #388
/*     */     //   Java source line #459	-> byte code offset #390
/*     */     //   Java source line #462	-> byte code offset #425
/*     */     //   Java source line #464	-> byte code offset #428
/*     */     //   Java source line #465	-> byte code offset #431
/*     */     //   Java source line #467	-> byte code offset #434
/*     */     //   Java source line #468	-> byte code offset #445
/*     */     //   Java source line #467	-> byte code offset #448
/*     */     //   Java source line #469	-> byte code offset #464
/*     */     //   Java source line #472	-> byte code offset #468
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	618	0	this	IndexCompactionManager
/*     */     //   0	618	1	client	Client
/*     */     //   0	618	2	indexName	String
/*     */     //   0	618	3	newIndexName	String
/*     */     //   0	618	4	indexCompactionParameters	IndexCompactionParameters
/*     */     //   49	21	5	settingsResponse	org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse
/*     */     //   67	504	6	numberOfReplicasBefore	String
/*     */     //   85	490	7	refreshIntervalBefore	String
/*     */     //   199	3	8	getMappingsResponse	GetMappingsResponse
/*     */     //   209	27	9	iterator	UnmodifiableIterator<ImmutableOpenMap<String, MappingMetaData>>
/*     */     //   222	229	10	batchProcessor	java.util.concurrent.ExecutorService
/*     */     //   225	240	11	succeeded	boolean
/*     */     //   243	297	12	mappings	ImmutableOpenMap<String, MappingMetaData>
/*     */     //   250	20	13	typeIter	UnmodifiableIterator<String>
/*     */     //   259	85	14	batches	List<java.util.concurrent.Future<Void>>
/*     */     //   277	43	15	type	String
/*     */     //   350	13	15	i$	java.util.Iterator
/*     */     //   295	30	16	originalMappingsString	String
/*     */     //   372	3	16	batch	java.util.concurrent.Future<Void>
/*     */     //   300	3	17	e	IOException
/*     */     //   388	30	17	e	Exception
/*     */     //   448	14	18	localObject1	Object
/*     */     //   542	74	19	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   279	297	300	java/io/IOException
/*     */     //   374	385	388	java/lang/Exception
/*     */     //   227	434	448	finally
/*     */     //   448	450	448	finally
/*     */     //   150	468	542	finally
/*     */     //   542	544	542	finally
/*     */   }
/*     */   
/*     */   private QueryBuilder buildFilterForType(Client client, String indexName, String type)
/*     */   {
/* 482 */     String alias = "*";
/* 483 */     if (type.contains("___")) {
/* 484 */       alias = type + "___" + "search";
/*     */     }
/* 486 */     GetAliasesResponse aliasesResponse = (GetAliasesResponse)((GetAliasesRequestBuilder)((GetAliasesRequestBuilder)client.admin().indices().prepareGetAliases(new String[] { alias }).setIndices(new String[] { indexName })).setMasterNodeTimeout(this.esCallTimeout)).execute().actionGet(this.esCallTimeout);
/*     */     
/*     */ 
/* 489 */     List<WrapperFilterBuilder> filterBuilders = new ArrayList();
/*     */     
/* 491 */     for (ObjectCursor<List<AliasMetaData>> objectCursor : aliasesResponse.getAliases().values()) {
/* 492 */       for (AliasMetaData aliasMetaData : (List)objectCursor.value) {
/* 493 */         if ((aliasMetaData.getAlias().contains(type)) && 
/* 494 */           (aliasMetaData.filteringRequired())) {
/*     */           try {
/* 496 */             filterBuilders.add(new WrapperFilterBuilder(new BytesArray(aliasMetaData.filter().string())));
/*     */           }
/*     */           catch (IOException e) {
/* 499 */             throw Throwables.propagate(e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 506 */     if (!filterBuilders.isEmpty())
/*     */     {
/* 508 */       FilterBuilder combinedFilters = FilterBuilders.boolFilter().should((FilterBuilder[])filterBuilders.toArray(new WrapperFilterBuilder[filterBuilders.size()]));
/*     */       
/*     */ 
/*     */ 
/* 512 */       FilterBuilder finalFilter = FilterBuilders.boolFilter().must(combinedFilters);
/*     */       
/* 514 */       return QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), finalFilter);
/*     */     }
/* 516 */     log.error("Could not find any aliases for index [{}], type [{}], and alias [{}]", new Object[] { indexName, type, alias });
/* 517 */     return QueryBuilders.matchAllQuery();
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   String createIndexToReceiveLiveDocuments(Client client, String indexName, long numExpiredDocs, long total)
/*     */   {
/* 524 */     Map<String, IndexStats> indicesStats = ((IndicesStatsResponse)((IndicesStatsRequestBuilder)client.admin().indices().prepareStats(new String[0]).setIndices(new String[] { indexName })).execute().actionGet(this.esCallTimeout)).getIndices();
/*     */     
/*     */ 
/* 527 */     IndexStats indexStats = (IndexStats)indicesStats.get(indexName);
/*     */     
/* 529 */     String baseIndexName = this.indexNameResolver.baseIndexNameFromFullName(indexName);
/* 530 */     String newIndexName = getNewIndexName(client, baseIndexName);
/*     */     
/* 532 */     int numberOfShards = calculateNumberOfShards(indexStats, numExpiredDocs, total);
/* 533 */     IndexCreationSettings settings = new IndexCreationSettings().setNumShards(numberOfShards);
/* 534 */     if (!this.indexCreationManager.createIndexNotLocking(client, newIndexName, settings.getSettings(), null, getMappingsFromOldIndex(client, indexName)))
/*     */     {
/* 536 */       throw new IllegalStateException("Unable to create new index [" + newIndexName + "]");
/*     */     }
/*     */     
/* 539 */     return newIndexName;
/*     */   }
/*     */   
/*     */   private Map<String, Map<String, Object>> getMappingsFromOldIndex(Client client, String indexName) {
/* 543 */     GetMappingsResponse getMappingsResponse = (GetMappingsResponse)((GetMappingsRequestBuilder)client.admin().indices().prepareGetMappings(new String[] { indexName }).setMasterNodeTimeout(this.esCallTimeout)).execute().actionGet(this.esCallTimeout);
/*     */     
/*     */ 
/*     */ 
/* 547 */     ImmutableOpenMap<String, MappingMetaData> mappings = (ImmutableOpenMap)getMappingsResponse.mappings().get(indexName);
/* 548 */     UnmodifiableIterator<String> keyIter = mappings.keysIt();
/* 549 */     Map<String, Map<String, Object>> mappingMetaDataMap = new HashMap(mappings.size());
/* 550 */     while (keyIter.hasNext()) {
/* 551 */       String key = (String)keyIter.next();
/* 552 */       MappingMetaData mappingMetaData = (MappingMetaData)mappings.get(key);
/*     */       try {
/* 554 */         mappingMetaDataMap.put(key, mappingMetaData.sourceAsMap());
/*     */       } catch (IOException e) {
/* 556 */         throw Throwables.propagate(e);
/*     */       }
/*     */     }
/* 559 */     return mappingMetaDataMap;
/*     */   }
/*     */   
/*     */   private void addAliasesToNewIndex(Client client, String indexName, String newIndexName) {
/* 563 */     GetAliasesResponse getAliasesResponse = (GetAliasesResponse)((GetAliasesRequestBuilder)((GetAliasesRequestBuilder)client.admin().indices().prepareGetAliases(new String[] { "*" }).setIndices(new String[] { indexName })).setMasterNodeTimeout(this.esCallTimeout)).execute().actionGet(this.esCallTimeout);
/*     */     
/* 565 */     List<AliasMetaData> aliasMetaDataList = (List)getAliasesResponse.getAliases().get(indexName);
/* 566 */     IndicesAliasesRequestBuilder aliasesRequestBuilder = client.admin().indices().prepareAliases();
/* 567 */     for (AliasMetaData aliasMetaData : aliasMetaDataList) {
/*     */       try {
/* 569 */         if (aliasMetaData.filteringRequired()) {
/* 570 */           aliasesRequestBuilder.addAlias(newIndexName, aliasMetaData.alias(), aliasMetaData.filter().string());
/*     */         }
/*     */         else {
/* 573 */           aliasesRequestBuilder.addAlias(newIndexName, aliasMetaData.alias());
/*     */         }
/*     */       } catch (IOException e) {
/* 576 */         throw Throwables.propagate(e);
/*     */       }
/*     */     }
/* 579 */     if (!((IndicesAliasesResponse)((IndicesAliasesRequestBuilder)((IndicesAliasesRequestBuilder)aliasesRequestBuilder.setMasterNodeTimeout(this.esCallTimeout)).setTimeout(this.esCallTimeout)).execute().actionGet(this.esCallTimeout)).isAcknowledged())
/*     */     {
/* 581 */       log.error("Failed to create aliases for new index [{}]", newIndexName);
/* 582 */       deleteIndex(client, newIndexName, "Unable to delete new index [" + newIndexName + "] - was cleaning " + "up index because creation of aliases failed");
/*     */     }
/*     */   }
/*     */   
/*     */   private String getNewIndexName(Client client, String baseIndexName)
/*     */   {
/* 588 */     String newIndexName = null;
/* 589 */     for (int i = 0; i < 5; i++) {
/* 590 */       newIndexName = this.indexNameResolver.appendTimestampToBaseIndexName(baseIndexName);
/* 591 */       if (!ESUtils.indexExists(client, newIndexName)) {
/*     */         break;
/*     */       }
/* 594 */       ConcurrencyHelper.sleep(1000L);
/*     */     }
/* 596 */     return (String)Preconditions.checkNotNull(newIndexName, "Unable to get an unused index name");
/*     */   }
/*     */   
/*     */   private int calculateNumberOfShards(IndexStats indexStats, long numExpiredDocs, long totalDocs) {
/* 600 */     long primarySize = indexStats.getPrimaries().getStore().getSizeInBytes();
/* 601 */     double bytesPerDoc = primarySize / totalDocs;
/* 602 */     long docsLeft = totalDocs - numExpiredDocs;
/* 603 */     double bytesLeft = docsLeft * bytesPerDoc;
/* 604 */     return (int)Math.ceil(bytesLeft / this.idealShardSize);
/*     */   }
/*     */   
/*     */   private long getTotalNumberOfDocuments(CountRequestBuilder baseRequest) {
/* 608 */     CountResponse searchResponse = (CountResponse)baseRequest.execute().actionGet(this.esCallTimeout);
/* 609 */     return searchResponse.getCount();
/*     */   }
/*     */   
/*     */   private long getNumberOfLiveDocuments(FilteredQueryBuilder finalQuery, CountRequestBuilder baseRequest) {
/* 613 */     CountRequestBuilder builderWithQuery = baseRequest.setQuery(finalQuery);
/* 614 */     CountResponse searchResponse = (CountResponse)builderWithQuery.execute().actionGet(this.esCallTimeout);
/* 615 */     return searchResponse.getCount();
/*     */   }
/*     */   
/*     */   private List<AliasMetaData> filterSearchAliases(List<AliasMetaData> allAliases)
/*     */   {
/* 620 */     Lists.newArrayList(Iterables.filter(allAliases, new Predicate()
/*     */     {
/*     */       public boolean apply(@Nullable AliasMetaData aliasMetaData) {
/* 623 */         return (aliasMetaData != null) && (IndexCompactionManager.this.indexNameResolver.isSearchAlias(aliasMetaData.alias()));
/*     */       }
/*     */     }));
/*     */   }
/*     */   
/*     */ 
/*     */   private Map<String, WrapperFilterBuilder> getAliasToFilterMapping(List<AliasMetaData> aliases)
/*     */   {
/* 631 */     Map<String, WrapperFilterBuilder> aliasToFilter = new HashMap();
/* 632 */     for (AliasMetaData alias : aliases)
/*     */     {
/*     */       WrapperFilterBuilder filterAsBuilderObject;
/*     */       try {
/* 636 */         filterAsBuilderObject = FilterBuilders.wrapperFilter(new BytesArray(alias.getFilter().string()));
/*     */       } catch (IOException e) {
/* 638 */         throw Throwables.propagate(e);
/*     */       }
/* 640 */       aliasToFilter.put(alias.getAlias(), filterAsBuilderObject);
/*     */     }
/* 642 */     return aliasToFilter;
/*     */   }
/*     */   
/* 645 */   private static class IndexCompactionTask implements Runnable { private static final Logger log = LoggerFactory.getLogger(IndexCompactionTask.class);
/*     */     
/*     */     private final ClientProvider clientProvider;
/*     */     
/*     */     private final String indexName;
/*     */     
/*     */     private final IndexCompactionManager indexCompactionManager;
/*     */     private final List<AliasMetaData> searchAliases;
/*     */     private final IndexCompactionParameters indexCompactionParameters;
/*     */     private final String clusterName;
/*     */     
/*     */     IndexCompactionTask(IndexCompactionManager indexCompactionManager, ClientProvider clientProvider, String indexName, List<AliasMetaData> searchAliases, IndexCompactionParameters indexCompactionParameters, String clusterName)
/*     */     {
/* 658 */       this.indexCompactionManager = indexCompactionManager;
/* 659 */       this.clientProvider = clientProvider;
/* 660 */       this.indexName = indexName;
/* 661 */       this.searchAliases = searchAliases;
/* 662 */       this.indexCompactionParameters = indexCompactionParameters;
/* 663 */       this.clusterName = clusterName;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 668 */       Client clusterClient = this.clientProvider.getClusterClient(this.clusterName);
/* 669 */       log.info("Compacting index [{}] in cluster [{}]", this.indexName, this.clusterName);
/*     */       try {
/* 671 */         long startIndexProcessingTime = System.currentTimeMillis();
/*     */         
/*     */ 
/* 674 */         Map<String, WrapperFilterBuilder> aliasToFilter = this.indexCompactionManager.getAliasToFilterMapping(this.searchAliases);
/*     */         
/*     */ 
/* 677 */         CountRequestBuilder baseCountQuery = clusterClient.prepareCount(new String[] { this.indexName });
/* 678 */         FilteredQueryBuilder copyQueryContent = this.indexCompactionManager.getCopyQueryContent(aliasToFilter);
/*     */         
/* 680 */         long totalNumberOfDocuments = this.indexCompactionManager.getTotalNumberOfDocuments(baseCountQuery);
/* 681 */         long numberOfLiveDocuments = this.indexCompactionManager.getNumberOfLiveDocuments(copyQueryContent, baseCountQuery);
/*     */         
/* 683 */         long numberOfExpiredDocuments = totalNumberOfDocuments - numberOfLiveDocuments;
/*     */         
/* 685 */         log.info("Index [{}] has [{}] documents, [{}] are expired.", new Object[] { this.indexName, Long.valueOf(totalNumberOfDocuments), Long.valueOf(numberOfExpiredDocuments) });
/*     */         
/*     */ 
/* 688 */         this.indexCompactionManager.compactIndexIfRequired(clusterClient, this.indexName, numberOfExpiredDocuments, numberOfLiveDocuments, this.indexCompactionParameters);
/*     */         
/*     */ 
/* 691 */         log.info("Index compaction completed on cluster [{}] for index [{}] - took [{}] ms.", new Object[] { this.clusterName, this.indexName, Long.valueOf(System.currentTimeMillis() - startIndexProcessingTime) });
/*     */       }
/*     */       catch (Exception e) {
/* 694 */         log.error("Encountered error while compacting index [{}] in cluster [{}]: ", new Object[] { this.indexName, clusterClient.settings().get("cluster.name"), e.getMessage(), e });
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */