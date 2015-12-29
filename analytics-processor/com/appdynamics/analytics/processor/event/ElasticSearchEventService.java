/*      */ package com.appdynamics.analytics.processor.event;
/*      */ 
/*      */ import com.appdynamics.analytics.processor.account.AccountManager;
/*      */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*      */ import com.appdynamics.analytics.processor.account.configuration.AccountLicensingConfiguration;
/*      */ import com.appdynamics.analytics.processor.admin.ForcedRolloverRequest;
/*      */ import com.appdynamics.analytics.processor.configuration.ConfigurationPropertyFactory;
/*      */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*      */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*      */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationSettings;
/*      */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.MappingMergeStrategy;
/*      */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.RollingIndexController;
/*      */ import com.appdynamics.analytics.processor.elasticsearch.node.EsCallTimeout;
/*      */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*      */ import com.appdynamics.analytics.processor.event.configuration.EventServiceConfiguration;
/*      */ import com.appdynamics.analytics.processor.event.exception.BulkFailure;
/*      */ import com.appdynamics.analytics.processor.event.exception.BulkFailureException;
/*      */ import com.appdynamics.analytics.processor.event.exception.EventTypeAlreadyExistsException;
/*      */ import com.appdynamics.analytics.processor.event.exception.EventTypeMissingException;
/*      */ import com.appdynamics.analytics.processor.event.exception.EventTypeRegistrationFailure;
/*      */ import com.appdynamics.analytics.processor.event.exception.MoveEventsException;
/*      */ import com.appdynamics.analytics.processor.event.hiddenfields.HiddenField;
/*      */ import com.appdynamics.analytics.processor.event.hiddenfields.HiddenFieldsService;
/*      */ import com.appdynamics.analytics.processor.event.metadata.EventTypeCache;
/*      */ import com.appdynamics.analytics.processor.event.metadata.EventTypeMetaDataService;
/*      */ import com.appdynamics.analytics.processor.event.parsers.ObjectListParser;
/*      */ import com.appdynamics.analytics.processor.event.parsers.ObjectListParser.Factory;
/*      */ import com.appdynamics.analytics.processor.event.query.ElasticSearchADQLQueryProcessor;
/*      */ import com.appdynamics.analytics.processor.event.query.EventSchema;
/*      */ import com.appdynamics.analytics.processor.event.relevantfields.RelevantFieldQueryProcessor;
/*      */ import com.appdynamics.analytics.processor.event.resource.MoveEventTypeRequest;
/*      */ import com.appdynamics.analytics.processor.event.upsert.ElasticSearchUpsertProcessor;
/*      */ import com.appdynamics.analytics.processor.event.upsert.Upsert;
/*      */ import com.appdynamics.analytics.processor.exception.ServiceRequestException;
/*      */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper;
/*      */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper.Listener;
/*      */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper.ListenerResponse;
/*      */ import com.appdynamics.analytics.processor.query.ParsingException;
/*      */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*      */ import com.appdynamics.common.util.configuration.Reader;
/*      */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*      */ import com.appdynamics.common.util.unit.ByteSizes;
/*      */ import com.appdynamics.common.util.var.SystemVariableResolver;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.databind.JsonNode;
/*      */ import com.fasterxml.jackson.databind.ObjectMapper;
/*      */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*      */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Charsets;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.google.inject.Singleton;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import javax.annotation.Nullable;
/*      */ import org.elasticsearch.ElasticsearchException;
/*      */ import org.elasticsearch.ElasticsearchTimeoutException;
/*      */ import org.elasticsearch.action.ListenableActionFuture;
/*      */ import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
/*      */ import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
/*      */ import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequestBuilder;
/*      */ import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
/*      */ import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequestBuilder;
/*      */ import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
/*      */ import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequestBuilder;
/*      */ import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
/*      */ import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
/*      */ import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
/*      */ import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
/*      */ import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
/*      */ import org.elasticsearch.action.bulk.BulkItemResponse;
/*      */ import org.elasticsearch.action.bulk.BulkRequestBuilder;
/*      */ import org.elasticsearch.action.bulk.BulkResponse;
/*      */ import org.elasticsearch.action.count.CountRequestBuilder;
/*      */ import org.elasticsearch.action.count.CountResponse;
/*      */ import org.elasticsearch.action.delete.DeleteRequest;
/*      */ import org.elasticsearch.action.index.IndexRequest;
/*      */ import org.elasticsearch.action.index.IndexRequestBuilder;
/*      */ import org.elasticsearch.action.search.MultiSearchRequestBuilder;
/*      */ import org.elasticsearch.action.search.MultiSearchResponse;
/*      */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*      */ import org.elasticsearch.action.search.SearchResponse;
/*      */ import org.elasticsearch.client.AdminClient;
/*      */ import org.elasticsearch.client.Client;
/*      */ import org.elasticsearch.client.IndicesAdminClient;
/*      */ import org.elasticsearch.cluster.metadata.AliasMetaData;
/*      */ import org.elasticsearch.cluster.metadata.MappingMetaData;
/*      */ import org.elasticsearch.common.Strings;
/*      */ import org.elasticsearch.common.collect.ImmutableOpenMap;
/*      */ import org.elasticsearch.common.collect.UnmodifiableIterator;
/*      */ import org.elasticsearch.common.compress.CompressedString;
/*      */ import org.elasticsearch.common.hppc.cursors.ObjectCursor;
/*      */ import org.elasticsearch.common.settings.Settings;
/*      */ import org.elasticsearch.common.unit.TimeValue;
/*      */ import org.elasticsearch.common.xcontent.ToXContent;
/*      */ import org.elasticsearch.common.xcontent.XContentBuilder;
/*      */ import org.elasticsearch.common.xcontent.XContentFactory;
/*      */ import org.elasticsearch.index.IndexException;
/*      */ import org.elasticsearch.index.mapper.MergeMappingException;
/*      */ import org.elasticsearch.index.query.BoolFilterBuilder;
/*      */ import org.elasticsearch.index.query.FilterBuilder;
/*      */ import org.elasticsearch.index.query.FilterBuilders;
/*      */ import org.elasticsearch.index.query.FilteredQueryBuilder;
/*      */ import org.elasticsearch.index.query.QueryBuilder;
/*      */ import org.elasticsearch.index.query.QueryBuilders;
/*      */ import org.elasticsearch.index.query.RangeFilterBuilder;
/*      */ import org.elasticsearch.index.query.WrapperFilterBuilder;
/*      */ import org.elasticsearch.indices.IndexMissingException;
/*      */ import org.elasticsearch.indices.TypeMissingException;
/*      */ import org.elasticsearch.rest.action.admin.indices.alias.delete.AliasesMissingException;
/*      */ import org.elasticsearch.search.SearchHit;
/*      */ import org.elasticsearch.search.SearchHits;
/*      */ import org.joda.time.DateTime;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
/*      */ 
/*      */ @Singleton
/*      */ public class ElasticSearchEventService implements EventService, ElasticSearchMetaService
/*      */ {
/*  143 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchEventService.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  150 */   public static final String[] EXCLUDED_FIELDS = { "_appliedUpdates" };
/*      */   public static final String PROPERTIES = "properties";
/*      */   public static final String TYPE = "type";
/*      */   public static final String DATE = "date";
/*  154 */   public static final ImmutableMap<String, String> TIMESTAMP_PROPERTIES = ImmutableMap.of("type", "date", "format", "dateOptionalTime");
/*      */   
/*      */   public static final String STRING = "string";
/*      */   
/*      */   public static final int MOVE_BATCH_SIZE = 500;
/*      */   
/*      */   public static final int MOVE_BULK_RETRIES = 5;
/*      */   
/*      */   public static final String INDEX_SUFFIX_PATTERN = "yyyy-MM-dd_HH-mm-ss";
/*      */   
/*      */   public static final int HOT_LIFESPAN_DAYS_PADDING = 1;
/*      */   
/*      */   public static final String META_DATA_DAILY_DATA_VOLUME_CAP_FIELD = "dailyDataCapVolume";
/*      */   
/*      */   final EventTypeMetaDataService eventTypeMetaDataService;
/*      */   
/*      */   final ClientProvider clientProvider;
/*      */   
/*      */   final IndexNameResolver indexNameResolver;
/*      */   
/*      */   final RollingIndexController rollingIndexController;
/*      */   
/*      */   final ObjectListParser.Factory objListParserFactory;
/*      */   
/*      */   final TimeValue callTimeout;
/*      */   
/*      */   final EventServiceConfiguration eventServiceConfiguration;
/*      */   
/*      */   final AccountManager accountManager;
/*      */   
/*      */   final ConfigurationPropertyFactory configurationPropertyFactory;
/*      */   final Timing timing;
/*      */   final ElasticSearchUpsertProcessor upsertProcessor;
/*      */   final ElasticSearchADQLQueryProcessor queryProcessor;
/*      */   final IndexCreationManager indexCreationManager;
/*      */   final EventTypeCache eventTypeCache;
/*      */   final RelevantFieldQueryProcessor sigValueProcessor;
/*      */   final ClusterLock lock;
/*      */   final HiddenFieldsService hiddenFieldsService;
/*      */   final String uniqueQueryIdPrefix;
/*      */   
/*      */   @com.google.inject.Inject
/*      */   public ElasticSearchEventService(EventTypeMetaDataService eventTypeMetaDataService, ClientProvider clientProvider, IndexNameResolver indexNameResolver, RollingIndexController rollingIndexController, EventServiceConfiguration eventServiceConfiguration, ObjectListParser.Factory objListParserFactory, AccountManager accountManager, Timing timing, ConfigurationPropertyFactory configurationPropertyFactory, @EsCallTimeout TimeValue callTimeout, IndexCreationManager indexCreationManager, EventTypeCache eventTypeCache, ClusterLock lock, HiddenFieldsService hiddenFieldsService)
/*      */   {
/*  198 */     this.eventTypeMetaDataService = eventTypeMetaDataService;
/*  199 */     this.clientProvider = clientProvider;
/*  200 */     this.indexNameResolver = indexNameResolver;
/*  201 */     this.rollingIndexController = rollingIndexController;
/*  202 */     this.objListParserFactory = objListParserFactory;
/*  203 */     this.timing = timing;
/*  204 */     this.configurationPropertyFactory = configurationPropertyFactory;
/*  205 */     this.callTimeout = callTimeout;
/*  206 */     this.eventServiceConfiguration = eventServiceConfiguration;
/*  207 */     this.indexCreationManager = indexCreationManager;
/*  208 */     this.eventTypeCache = eventTypeCache;
/*  209 */     this.accountManager = accountManager;
/*  210 */     this.lock = lock;
/*  211 */     this.hiddenFieldsService = hiddenFieldsService;
/*  212 */     this.upsertProcessor = new ElasticSearchUpsertProcessor(this, callTimeout, eventServiceConfiguration.getUpdateConflictRetries());
/*      */     
/*      */ 
/*      */ 
/*  216 */     this.queryProcessor = new ElasticSearchADQLQueryProcessor(clientProvider, indexNameResolver, callTimeout);
/*  217 */     this.sigValueProcessor = new RelevantFieldQueryProcessor();
/*  218 */     this.uniqueQueryIdPrefix = (ElasticSearchEventService.class.getSimpleName() + "-" + SystemVariableResolver.getHostName() + "-" + SystemVariableResolver.getProcessId() + "-");
/*      */   }
/*      */   
/*      */ 
/*      */   public ClientProvider getClientProvider()
/*      */   {
/*  224 */     return this.clientProvider;
/*      */   }
/*      */   
/*      */   public IndexNameResolver getIndexNameResolver()
/*      */   {
/*  229 */     return this.indexNameResolver;
/*      */   }
/*      */   
/*      */   public void registerEventType(int requestVersion, String accountName, String eventType, String body)
/*      */   {
/*  234 */     registerEventType(requestVersion, accountName, eventType, body, null);
/*      */   }
/*      */   
/*      */   public void updateEventType(int requestVersion, String accountName, String eventType, String body)
/*      */   {
/*  239 */     eventType = this.indexNameResolver.resolveEventType(eventType);
/*  240 */     accountName = AccountConfiguration.normalizeAccountName(accountName);
/*      */     
/*      */ 
/*  243 */     JsonNode existingEventType = getEventType(requestVersion, accountName, eventType);
/*  244 */     if (existingEventType == null) {
/*  245 */       throw new EventTypeMissingException(accountName, eventType);
/*      */     }
/*      */     
/*  248 */     ObjectNode bodyNode = ElasticSearchQueryHelper.parseJsonQueryToObjectModel(body);
/*      */     
/*  250 */     mergeOrCopyObject(existingEventType, bodyNode, "metaData");
/*      */     
/*  252 */     EventTypeMetaData metaData = preProcessBodyNodeAndReturnMetaData(requestVersion, accountName, eventType, bodyNode, null);
/*      */     
/*      */ 
/*      */ 
/*  256 */     mergeOrCopyObject(existingEventType, bodyNode, "properties");
/*      */     
/*      */ 
/*      */ 
/*  260 */     JsonNode newMergedEventType = existingEventType.deepCopy();
/*      */     
/*  262 */     copyField("metaData", bodyNode, (ObjectNode)newMergedEventType);
/*  263 */     copyField("properties", bodyNode, (ObjectNode)newMergedEventType);
/*      */     
/*  265 */     EventTypeMetaData existingMetadataOnServer = getEventTypeMetaData(accountName, eventType);
/*  266 */     int lifespan = getHotLifespanInDays(accountName, eventType, metaData);
/*  267 */     metaData.setHotLifespanInDays(Integer.valueOf(lifespan));
/*  268 */     if (log.isDebugEnabled()) {
/*  269 */       log.debug("\nEvent type old:\n[{}]\nEvent type new:\n[{}]\nMetadata old:\n[{}]\nMetadata new:\n[{}]", new Object[] { existingEventType, newMergedEventType, existingMetadataOnServer, metaData });
/*      */     }
/*      */     
/*      */ 
/*  273 */     stripExplicitAnalyzedStrings(newMergedEventType);
/*  274 */     if ((existingEventType.equals(newMergedEventType)) && (existingMetadataOnServer.equals(metaData)))
/*      */     {
/*  276 */       log.info("Update call to event type [{}] for account [{}] has been ignored as there is no change", eventType, accountName);
/*      */     }
/*      */     else {
/*  279 */       upsertEventType(accountName, eventType, bodyNode, metaData);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateEventTypeMetaDataAndAliasFilter(List<AccountConfiguration> accountConfiguration)
/*      */   {
/*  287 */     List<EventTypeMetaData> updatedMetaDataList = this.eventTypeMetaDataService.updateEventTypeMetaData(accountConfiguration);
/*      */     
/*  289 */     for (EventTypeMetaData metaData : updatedMetaDataList)
/*      */     {
/*  291 */       updateSearchAliasIfChanged(metaData);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void stripExplicitAnalyzedStrings(JsonNode node)
/*      */   {
/*  301 */     if ((node.isObject()) && (node.get("index") != null) && (node.get("index").asText().equals("analyzed"))) {
/*  302 */       ((ObjectNode)node).remove("index");
/*  303 */       return;
/*      */     }
/*  305 */     Iterator<JsonNode> nodeIterator = node.elements();
/*  306 */     while (nodeIterator.hasNext()) {
/*  307 */       JsonNode childNode = (JsonNode)nodeIterator.next();
/*  308 */       stripExplicitAnalyzedStrings(childNode);
/*      */     }
/*      */   }
/*      */   
/*      */   private int getHotLifespanInDays(String accountName, String eventType, EventTypeMetaData metaData) {
/*  313 */     int lifespan = metaData.getHotLifespanInDays().intValue();
/*  314 */     AccountLicensingConfiguration acctLicense = (AccountLicensingConfiguration)this.accountManager.findAccountLicensingConfiguration(accountName, eventType).orNull();
/*      */     
/*      */ 
/*  317 */     if ((acctLicense != null) && (acctLicense.getDataRetentionPeriodDays() != null)) {
/*  318 */       lifespan = acctLicense.getDataRetentionPeriodDays().intValue();
/*      */     }
/*      */     
/*  321 */     return lifespan;
/*      */   }
/*      */   
/*      */   private void mergeOrCopyObject(JsonNode existing, ObjectNode updated, String fieldName) {
/*  325 */     ObjectNode metaDataNode = getObjectNodeOrNull(updated, fieldName);
/*  326 */     Iterator<Map.Entry<String, JsonNode>> iter; if (metaDataNode == null) {
/*  327 */       updated.put(fieldName, existing.get(fieldName));
/*      */     } else {
/*  329 */       ObjectNode existingMetaData = getObjectNodeOrNull(existing, fieldName);
/*  330 */       if (existingMetaData != null) {
/*  331 */         for (iter = existingMetaData.fields(); iter.hasNext();) {
/*  332 */           Map.Entry<String, JsonNode> node = (Map.Entry)iter.next();
/*  333 */           if (metaDataNode.get((String)node.getKey()) == null) {
/*  334 */             metaDataNode.put((String)node.getKey(), ((JsonNode)node.getValue()).deepCopy());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private ObjectNode getObjectNodeOrNull(JsonNode node, String fieldName) {
/*  342 */     JsonNode nodeToFetch = node.get(fieldName);
/*  343 */     if ((nodeToFetch == null) || (nodeToFetch.isNull())) {
/*  344 */       return null;
/*      */     }
/*  346 */     return (ObjectNode)nodeToFetch;
/*      */   }
/*      */   
/*      */   private static void copyField(String fieldName, ObjectNode from, ObjectNode to) {
/*  350 */     JsonNode fromNode = from.get(fieldName);
/*  351 */     if (fromNode != null) {
/*  352 */       to.setAll(ImmutableMap.of(fieldName, fromNode.deepCopy()));
/*      */     }
/*      */   }
/*      */   
/*      */   public void bulkUpdateEventType(final int requestVersion, String eventType, final String body)
/*      */   {
/*  358 */     final String finalEventType = this.indexNameResolver.resolveEventType(eventType);
/*  359 */     List<String> accountNames = getAllAccountNamesForEventType(eventType);
/*  360 */     log.info("Performing bulk update for event type [{}] - will update a total of [{}] accounts with mappings [{}]", new Object[] { eventType, Integer.valueOf(accountNames.size()), body });
/*      */     
/*      */ 
/*      */ 
/*  364 */     int numberOfRetries = 3;
/*      */     
/*      */ 
/*  367 */     int maxAllowedFailures = 100;
/*  368 */     final AtomicInteger totalFailures = new AtomicInteger(0);
/*      */     
/*      */ 
/*      */ 
/*  372 */     ExecutorService batchProcessors = Executors.newFixedThreadPool(5);
/*  373 */     List<Future<Exception>> batches = new ArrayList();
/*  374 */     for (final String accountName : accountNames) {
/*  375 */       batches.add(batchProcessors.submit(new Callable()
/*      */       {
/*      */         public Exception call() throws Exception {
/*  378 */           for (int i = 1; i <= 3; i++) {
/*      */             try {
/*  380 */               ElasticSearchEventService.log.info("Updating account [{}] and event type [{}]");
/*  381 */               ElasticSearchEventService.this.updateEventType(requestVersion, accountName, finalEventType, body);
/*      */             }
/*      */             catch (EventTypeMissingException|IndexMissingException|TypeMissingException e) {
/*  384 */               ElasticSearchEventService.log.info("While performing bulk update for event [{}] and account [{}], event type was not found - probably means there was no index with the corresponding mapping data. Actual error [{}]", new Object[] { finalEventType, accountName, e.getMessage() });
/*      */ 
/*      */             }
/*      */             catch (Exception e)
/*      */             {
/*      */ 
/*  390 */               if ((e.getCause() instanceof MergeMappingException)) {
/*  391 */                 ElasticSearchEventService.log.info("Standard update failed due to merge mapping exception, forcing rollover for account [{}] and event type [{}]", accountName, finalEventType);
/*      */                 
/*      */ 
/*  394 */                 ElasticSearchEventService.this.forceRollover(accountName, finalEventType, body);
/*      */               } else {
/*  396 */                 ElasticSearchEventService.log.error("Bulk update for account [{}] and event type [{}] failed with exception [{}] - sleeping and retrying - try {} of {}", new Object[] { accountName, finalEventType, e.getMessage(), Integer.valueOf(i), Integer.valueOf(3) });
/*      */                 
/*      */ 
/*      */ 
/*  400 */                 if ((totalFailures.getAndIncrement() > 100) || (i == 3)) {
/*  401 */                   return e;
/*      */                 }
/*  403 */                 ConcurrencyHelper.sleep(1000 * i);
/*      */               }
/*      */             }
/*      */           }
/*  407 */           return null;
/*      */         }
/*      */       }));
/*      */     }
/*      */     
/*  412 */     for (Future<Exception> batch : batches) {
/*  413 */       Exception e = (Exception)ConcurrencyHelper.getOrCancel(batch, 300, log);
/*  414 */       if (e != null) {
/*  415 */         throw Throwables.propagate(e);
/*      */       }
/*      */     }
/*      */     
/*  419 */     ConcurrencyHelper.stop(batchProcessors, log);
/*      */   }
/*      */   
/*      */   private void forceRollover(String accountName, String eventType, String body) {
/*  423 */     GetAliasesResponse getAliasesResponse = (GetAliasesResponse)getClientProvider().getInsertClient(accountName).admin().indices().prepareGetAliases(new String[] { this.indexNameResolver.resolveInsertAlias(accountName, eventType) }).execute().actionGet();
/*      */     
/*  425 */     if (getAliasesResponse.getAliases().size() == 0) {
/*  426 */       log.info("Cannot force rollover for account [{}] and event type [{}] as there is no insert alias", accountName, eventType);
/*      */       
/*  428 */       return; }
/*  429 */     if (getAliasesResponse.getAliases().size() > 1) {
/*  430 */       log.warn("Error in logic - expected to find one and only one active insert index for account [{}] and event type [{}], but found the following: [{}]", new Object[] { accountName, eventType, Joiner.on(",").join(getAliasesResponse.getAliases().keysIt()) });
/*      */     }
/*      */     
/*      */ 
/*  434 */     String indexName = (String)getAliasesResponse.getAliases().keysIt().next();
/*      */     
/*      */ 
/*  437 */     MappingMergeStrategy mergeStrategy = new MappingMergeStrategy()
/*      */     {
/*      */       public JsonNode merge(JsonNode originalMapping, JsonNode updatedMapping) {
/*  440 */         ObjectNode mergedMapping = (ObjectNode)updatedMapping.deepCopy();
/*  441 */         Iterator<String> types = originalMapping.fieldNames();
/*  442 */         while (types.hasNext()) {
/*  443 */           String type = (String)types.next();
/*  444 */           if (mergedMapping.get(type) != null) {
/*  445 */             ElasticSearchEventService.this.mergeOrCopyObject(originalMapping.get(type), (ObjectNode)mergedMapping.get(type), "properties");
/*      */           }
/*      */         }
/*      */         
/*  449 */         return mergedMapping;
/*      */       }
/*      */       
/*  452 */     };
/*  453 */     ForcedRolloverRequest rolloverRequest = new ForcedRolloverRequest();
/*      */     
/*  455 */     ObjectNode esMapping = ElasticSearchQueryHelper.parseJsonQueryToObjectModel(body);
/*  456 */     esMapping.remove("metaData");
/*  457 */     rolloverRequest.setIndex(indexName);
/*  458 */     rolloverRequest.setCluster(getClientProvider().getInsertClient(accountName).settings().get("cluster.name"));
/*      */     
/*  460 */     rolloverRequest.setEventTypeMappingOverride(esMapping);
/*  461 */     rolloverRequest.setMappingMergeStrategy(mergeStrategy);
/*      */     
/*  463 */     log.info("Updating event type mapping for account [{}] event type [{}] through forcing an index rollover.", accountName, eventType);
/*      */     
/*      */     try
/*      */     {
/*  467 */       this.rollingIndexController.forceRollover(rolloverRequest);
/*      */     } catch (Exception e) {
/*  469 */       throw new EventTypeRegistrationFailure(String.format("Failed to update event type mapping for all accounts with event type [%s]", new Object[] { eventType }), e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonNode getEventType(int requestVersion, String accountName, String eventType)
/*      */   {
/*      */     try
/*      */     {
/*  479 */       eventType = this.indexNameResolver.resolveEventType(eventType);
/*  480 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/*      */       
/*  482 */       verifyEventTypeRepairingIndicesIfNecessary(requestVersion, accountName, eventType);
/*      */       
/*  484 */       JsonNode mappingData = getEventTypeMappingData(accountName, eventType);
/*  485 */       if (mappingData != null)
/*      */       {
/*  487 */         EventTypeMetaData metaData = this.eventTypeMetaDataService.getEventTypeMetaDataNoCache(accountName, eventType);
/*      */         
/*      */ 
/*  490 */         ((ObjectNode)mappingData).put("metaData", (JsonNode)Reader.DEFAULT_JSON_MAPPER.convertValue(metaData, JsonNode.class));
/*      */       }
/*      */       
/*      */ 
/*  494 */       return mappingData;
/*      */     } catch (Exception e) {
/*  496 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean eventTypeExists(int requestVersion, String accountName, String eventType)
/*      */   {
/*      */     try {
/*  503 */       eventType = this.indexNameResolver.resolveEventType(eventType);
/*  504 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/*      */       
/*  506 */       if (this.eventTypeCache.accountNameEventTypeExistsWithCurrentInsertClient(accountName, eventType)) {
/*  507 */         return true;
/*      */       }
/*      */       
/*  510 */       EventTypeMetaData eventTypeMetaData = getEventTypeMetaData(accountName, eventType);
/*  511 */       if (eventTypeMetaData == null) {
/*  512 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  516 */       boolean returnValue = true;
/*  517 */       if (!verifyTypeInIndex(accountName, eventType)) {
/*  518 */         returnValue = repairIndex(requestVersion, accountName, eventType, eventTypeMetaData);
/*      */       }
/*  520 */       this.eventTypeCache.storeAccountNameEventType(accountName, eventType);
/*  521 */       return returnValue;
/*      */     } catch (Exception e) {
/*  523 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void registerEventType(int requestVersion, String accountName, String eventType, String body, EventTypeMetaData existingMetaData)
/*      */   {
/*  529 */     eventType = this.indexNameResolver.resolveEventType(eventType);
/*  530 */     accountName = AccountConfiguration.normalizeAccountName(accountName);
/*      */     
/*  532 */     if ((existingMetaData == null) && (eventTypeExists(requestVersion, accountName, eventType))) {
/*  533 */       throw new EventTypeAlreadyExistsException(accountName, eventType);
/*      */     }
/*      */     
/*  536 */     ObjectNode bodyNode = ElasticSearchQueryHelper.parseJsonQueryToObjectModel(body);
/*      */     
/*  538 */     EventTypeMetaData metaData = preProcessBodyNodeAndReturnMetaData(requestVersion, accountName, eventType, bodyNode, existingMetaData);
/*      */     
/*      */ 
/*  541 */     upsertEventType(accountName, eventType, bodyNode, metaData);
/*      */   }
/*      */   
/*      */   private boolean shouldDefaultNotAnalyzedStringFields(int version) {
/*  545 */     return version >= 2;
/*      */   }
/*      */   
/*      */   private boolean repairIndex(int requestVersion, String accountName, String eventType, EventTypeMetaData eventTypeMetaData)
/*      */   {
/*  550 */     log.info("Repairing current active index for account [{}] and event type [{}]", accountName, eventType);
/*  551 */     String searchAlias = this.indexNameResolver.resolveSearchAlias(accountName, eventType);
/*  552 */     GetAliasesResponse aliasesResponse = (GetAliasesResponse)this.clientProvider.getSearchClient(accountName).admin().indices().prepareGetAliases(new String[] { searchAlias }).execute().actionGet();
/*      */     
/*  554 */     List<String> indicesInOrder = indicesAsOrderedList(aliasesResponse.getAliases().keysIt());
/*  555 */     if (indicesInOrder.isEmpty()) {
/*  556 */       return false;
/*      */     }
/*  558 */     for (String index : indicesInOrder) {
/*  559 */       GetMappingsResponse mappingsResponse = (GetMappingsResponse)this.clientProvider.getSearchClient(accountName).admin().indices().prepareGetMappings(new String[] { index }).execute().actionGet();
/*      */       
/*  561 */       ImmutableOpenMap<String, MappingMetaData> mappingsByType = (ImmutableOpenMap)mappingsResponse.mappings().get(index);
/*  562 */       if (mappingsByType != null)
/*      */       {
/*      */ 
/*      */ 
/*  566 */         MappingMetaData mappingMetaData = getMappingMetaData(accountName, eventType, index, mappingsByType);
/*  567 */         if (mappingMetaData != null)
/*      */         {
/*      */ 
/*      */           try
/*      */           {
/*  572 */             ObjectNode mappingNode = (ObjectNode)Reader.DEFAULT_JSON_MAPPER.readTree(mappingMetaData.source().string());
/*      */             
/*  574 */             ((ObjectNode)mappingNode.get(mappingMetaData.type())).remove("dynamic");
/*  575 */             registerEventType(requestVersion, accountName, eventType, mappingNode.get(mappingMetaData.type()).toString(), eventTypeMetaData);
/*      */             
/*      */ 
/*  578 */             fixSearchAliasForOldIndexManagementIfNecessary(accountName, eventType, index);
/*      */           } catch (IOException e) {
/*  580 */             throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */           }
/*  582 */           return true;
/*      */         } } }
/*  584 */     return false;
/*      */   }
/*      */   
/*      */   private void fixSearchAliasForOldIndexManagementIfNecessary(String accountName, String eventType, String index) {
/*  588 */     if (!this.indexNameResolver.isOldIndexManagementIndex(index)) {
/*  589 */       return;
/*      */     }
/*      */     
/*  592 */     String filterForOldIndex = getFilterForOldSearchAlias(accountName, eventType, index);
/*  593 */     if (filterForOldIndex == null) {
/*  594 */       log.warn("Unable to get search alias for old index management index [{}] for account [{}] and event type [{}]", new Object[] { index, accountName, eventType });
/*      */       
/*  596 */       return;
/*      */     }
/*      */     
/*      */ 
/*  600 */     GetAliasesResponse currentSearchAliases = (GetAliasesResponse)((GetAliasesRequestBuilder)getClientProvider().getInsertClient(accountName).admin().indices().prepareGetAliases(new String[] { this.indexNameResolver.resolveSearchAlias(accountName, eventType) }).setIndices(new String[] { this.indexNameResolver.resolveBaseIndexName(accountName, eventType) })).execute().actionGet();
/*      */     
/*      */ 
/*  603 */     Preconditions.checkState(currentSearchAliases.getAliases().size() == 1, "Expected to find only one index for the current insert index");
/*      */     
/*  605 */     String concreteIndexName = (String)currentSearchAliases.getAliases().keysIt().next();
/*  606 */     List<AliasMetaData> aliasMetaDatas = (List)currentSearchAliases.getAliases().valuesIt().next();
/*  607 */     for (AliasMetaData aliasMetaData : aliasMetaDatas) {
/*  608 */       if (this.indexNameResolver.isSearchAlias(aliasMetaData.alias())) {
/*      */         try {
/*  610 */           String currentFilter = aliasMetaData.getFilter().string();
/*  611 */           if (!currentFilter.equals(filterForOldIndex))
/*      */           {
/*      */ 
/*  614 */             WrapperFilterBuilder[] oldAndNewSearchAliasFilters = new WrapperFilterBuilder[2];
/*  615 */             oldAndNewSearchAliasFilters[0] = FilterBuilders.wrapperFilter(filterForOldIndex);
/*  616 */             oldAndNewSearchAliasFilters[1] = FilterBuilders.wrapperFilter(currentFilter);
/*  617 */             if (!((IndicesAliasesResponse)this.clientProvider.getInsertClient(accountName).admin().indices().prepareAliases().removeAlias(concreteIndexName, this.indexNameResolver.resolveSearchAlias(accountName, eventType)).addAlias(concreteIndexName, this.indexNameResolver.resolveSearchAlias(accountName, eventType), FilterBuilders.boolFilter().should(oldAndNewSearchAliasFilters)).execute().actionGet()).isAcknowledged())
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  623 */               log.error("Update of search aliases failed to acknowledge for account [{}], event type [{}], and index [{}]", new Object[] { accountName, eventType, concreteIndexName });
/*      */             }
/*      */           }
/*      */         } catch (IOException e) {
/*  627 */           throw Throwables.propagate(e);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private String getFilterForOldSearchAlias(String accountName, String eventType, String index) {
/*  634 */     GetAliasesResponse aliasesResponse = (GetAliasesResponse)((GetAliasesRequestBuilder)getClientProvider().getSearchClient(accountName).admin().indices().prepareGetAliases(new String[] { this.indexNameResolver.resolveSearchAlias(accountName, eventType) }).setIndices(new String[] { index })).execute().actionGet();
/*      */     
/*      */ 
/*  637 */     List<AliasMetaData> aliasMetaDatas = (List)aliasesResponse.getAliases().get(index);
/*  638 */     for (AliasMetaData aliasMetaData : aliasMetaDatas) {
/*  639 */       if (this.indexNameResolver.isSearchAlias(aliasMetaData.alias())) {
/*      */         try {
/*  641 */           return aliasMetaData.getFilter().string();
/*      */         } catch (IOException e) {
/*  643 */           throw Throwables.propagate(e);
/*      */         }
/*      */       }
/*      */     }
/*  647 */     return null;
/*      */   }
/*      */   
/*      */   private MappingMetaData getMappingMetaData(String accountName, String eventType, String index, ImmutableOpenMap<String, MappingMetaData> mappingsByType) {
/*      */     String docType;
/*      */     String docType;
/*  653 */     if (this.indexNameResolver.isOldIndexManagementDynamicType(index)) {
/*  654 */       docType = this.indexNameResolver.oldIndexManagementDynamicEventTypeDocType(accountName, eventType); } else { String docType;
/*  655 */       if (this.indexNameResolver.isOldIndexManagementStaticType(index)) {
/*  656 */         docType = this.indexNameResolver.oldIndexManagementStaticEventTypeDocType(accountName, eventType);
/*      */       } else
/*  658 */         docType = this.indexNameResolver.getDocType(accountName, eventType);
/*      */     }
/*  660 */     return (MappingMetaData)mappingsByType.get(docType);
/*      */   }
/*      */   
/*      */   private boolean verifyTypeInIndex(String accountName, String eventType) {
/*  664 */     String insertAlias = this.indexNameResolver.resolveInsertAlias(accountName, eventType);
/*  665 */     String docType = this.indexNameResolver.getDocType(accountName, eventType);
/*      */     try {
/*  667 */       ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = ((GetMappingsResponse)((GetMappingsRequestBuilder)this.clientProvider.getInsertClient(accountName).admin().indices().prepareGetMappings(new String[] { insertAlias }).setTypes(new String[] { docType })).execute().actionGet()).mappings();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  672 */       if (mappings.size() < 1) {
/*  673 */         return false;
/*      */       }
/*  675 */       Preconditions.checkState(mappings.size() == 1, "Expected exactly one record for the insert alias [%s], account [%s], and event type [%s], but was [%s]", new Object[] { insertAlias, accountName, eventType, Integer.valueOf(mappings.size()) });
/*      */       
/*      */ 
/*  678 */       String indexName = (String)mappings.keysIt().next();
/*  679 */       return !this.indexNameResolver.isOldIndexManagementIndex(indexName);
/*      */     } catch (IndexMissingException e) {
/*  681 */       log.debug("Index missing, returning false. [" + e.getMessage() + "]"); }
/*  682 */     return false;
/*      */   }
/*      */   
/*      */   private JsonNode getEventTypeMappingData(String accountName, String eventType) throws IOException
/*      */   {
/*  687 */     String searchAlias = this.indexNameResolver.resolveSearchAlias(accountName, eventType);
/*      */     
/*  689 */     EventTypeMetaData metaData = this.eventTypeMetaDataService.getEventTypeMetaData(accountName, eventType);
/*  690 */     if (metaData == null) {
/*  691 */       throw new EventTypeMissingException(accountName, eventType);
/*      */     }
/*      */     
/*  694 */     String docType = this.indexNameResolver.getDocType(accountName, eventType);
/*  695 */     ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> allMappingMetaData = getIndexMetaData(accountName, eventType, docType);
/*      */     
/*      */ 
/*  698 */     List<String> indices = indicesAsOrderedList(allMappingMetaData.keysIt());
/*  699 */     if (log.isDebugEnabled()) {
/*  700 */       log.debug("indices: {}", indices);
/*      */     }
/*  702 */     if (!indices.isEmpty()) {
/*  703 */       ImmutableOpenMap<String, MappingMetaData> mappingMetaDataForIndex = (ImmutableOpenMap)allMappingMetaData.get(indices.get(0));
/*  704 */       if (mappingMetaDataForIndex == null) {
/*  705 */         log.debug("No mappingMetaDataForIndex found for accountName [{}], eventType [{}]", accountName, eventType);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  710 */         MappingMetaData mappingMetaData = (MappingMetaData)mappingMetaDataForIndex.get(docType);
/*  711 */         if (mappingMetaData == null) {
/*  712 */           log.warn("Mapping metadata not found for account [{}] and event type [{}], this could be a benign race condition between event registry and insertion.", accountName, eventType);
/*      */           
/*      */ 
/*  715 */           throw new EventTypeMissingException(accountName, eventType);
/*      */         }
/*  717 */         CompressedString compressedSource = mappingMetaData.source();
/*  718 */         if (compressedSource == null) {
/*  719 */           throw new NullPointerException("Expected metaData source to NOT be null for account [" + accountName + "], event type [" + eventType + "], search alias [" + searchAlias + " - " + mappingMetaDataForIndex.toString());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  724 */         String mappingMetaDataString = compressedSource.string();
/*  725 */         JsonNode fullMappingData = Reader.DEFAULT_JSON_MAPPER.readTree(mappingMetaDataString);
/*      */         
/*  727 */         ObjectNode mappingData = (ObjectNode)fullMappingData.get(docType);
/*  728 */         this.hiddenFieldsService.pruneHiddenFieldsFromMapping(accountName, eventType, mappingData);
/*      */         
/*  730 */         return mappingData;
/*      */       }
/*      */     } else {
/*  733 */       log.debug("No doctype [{}] found for accountName [{}], eventType [{}]", new Object[] { docType, accountName, eventType });
/*      */     }
/*      */     
/*      */ 
/*  737 */     throw new EventTypeMissingException(accountName, eventType);
/*      */   }
/*      */   
/*      */   private ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> getIndexMetaData(String accountName, String eventType, String docType)
/*      */   {
/*  742 */     String insertAlias = this.indexNameResolver.resolveInsertAlias(accountName, eventType);
/*  743 */     log.debug("Insert alias [{}], doc type [{}]", insertAlias, docType);
/*      */     
/*      */ 
/*      */ 
/*  747 */     Client insertClient = this.clientProvider.getInsertClient(accountName);
/*  748 */     if (log.isDebugEnabled()) {
/*  749 */       log.debug("Insert client: {}, for cluster {}", insertClient, ESUtils.getClusterName(insertClient));
/*      */     }
/*      */     
/*  752 */     GetMappingsRequestBuilder builder = (GetMappingsRequestBuilder)insertClient.admin().indices().prepareGetMappings(new String[] { insertAlias }).setTypes(new String[] { docType });
/*      */     
/*      */     GetMappingsResponse mappingsResponse;
/*      */     try
/*      */     {
/*  757 */       mappingsResponse = (GetMappingsResponse)builder.execute().actionGet();
/*      */     } catch (IndexMissingException e) {
/*  759 */       log.debug("Index was missing, refreshing and trying again");
/*      */       
/*      */ 
/*  762 */       ElasticSearchServiceHelper.refreshAndWaitForIndexReadiness(insertClient, new String[] { insertAlias });
/*  763 */       mappingsResponse = (GetMappingsResponse)builder.execute().actionGet();
/*      */     }
/*  765 */     if (log.isDebugEnabled()) {
/*  766 */       log.debug("mappings: keys: {}", Joiner.on(',').join(mappingsResponse.getMappings().keysIt()));
/*      */     }
/*  768 */     return mappingsResponse.getMappings();
/*      */   }
/*      */   
/*      */ 
/*      */   public ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> getSearchAliasMetaData(String accountName, String docType, String searchAlias)
/*      */   {
/*  774 */     Client client = this.clientProvider.getSearchClient(accountName);
/*      */     
/*  776 */     GetMappingsRequestBuilder builder = (GetMappingsRequestBuilder)client.admin().indices().prepareGetMappings(new String[] { searchAlias }).setTypes(new String[] { docType });
/*      */     GetMappingsResponse mappingsResponse;
/*      */     try
/*      */     {
/*  780 */       mappingsResponse = (GetMappingsResponse)builder.execute().actionGet();
/*      */     }
/*      */     catch (IndexMissingException e)
/*      */     {
/*  784 */       ElasticSearchServiceHelper.refreshAndWaitForIndexReadinessRetrying(client, new String[] { searchAlias });
/*  785 */       mappingsResponse = (GetMappingsResponse)builder.execute().actionGet();
/*      */     }
/*  787 */     return mappingsResponse.getMappings();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void deleteEventType(String accountName, String eventType)
/*      */   {
/*      */     try
/*      */     {
/*  803 */       eventType = this.indexNameResolver.resolveEventType(eventType);
/*  804 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/*      */       
/*  806 */       String indexName = (String)Preconditions.checkNotNull(this.indexNameResolver.resolveSearchAlias(accountName, eventType));
/*      */       
/*  808 */       this.eventTypeMetaDataService.deleteEventTypeMetaData(accountName, eventType);
/*  809 */       this.eventTypeCache.removeAccountNameEventType(accountName, eventType);
/*      */       
/*  811 */       deleteEventTypeDocTypeMapping(accountName, eventType, indexName);
/*  812 */       deleteEventTypeAliasMappings(accountName, eventType, indexName);
/*      */     } catch (Exception e) {
/*  814 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void deleteEventTypeDocTypeMapping(String accountName, String eventType, String indexName)
/*      */   {
/*  821 */     Client client = this.clientProvider.getInsertClient(accountName);
/*      */     
/*  823 */     String docType = this.indexNameResolver.getDocType(accountName, eventType);
/*  824 */     if (((TypesExistsResponse)client.admin().indices().prepareTypesExists(new String[] { indexName }).setTypes(new String[] { docType }).execute().actionGet()).isExists())
/*      */     {
/*  826 */       if (!((DeleteMappingResponse)client.admin().indices().prepareDeleteMapping(new String[] { indexName }).setType(new String[] { docType }).execute().actionGet()).isAcknowledged())
/*      */       {
/*  828 */         log.error("Delete index request for index [" + indexName + "] was not acknowledged");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void deleteEventTypeAliasMappings(String accountName, String eventType, String indexName) {
/*  834 */     Client client = this.clientProvider.getInsertClient(accountName);
/*      */     
/*  836 */     if (!((IndicesAliasesResponse)client.admin().indices().prepareAliases().removeAlias(indexName, this.indexNameResolver.resolveSearchAlias(accountName, eventType)).removeAlias(indexName, this.indexNameResolver.resolveInsertAlias(accountName, eventType)).execute().actionGet()).isAcknowledged())
/*      */     {
/*      */ 
/*      */ 
/*  840 */       log.error("Removal of aliases [{}, {}] for index [{}] was not acknowledged", new Object[] { this.indexNameResolver.resolveSearchAlias(accountName, eventType), this.indexNameResolver.resolveInsertAlias(accountName, eventType), indexName });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void publishEvents(int requestVersion, String accountName, String eventType, ObjectListParser parser, String requestId)
/*      */   {
/*  849 */     publishEvents(requestVersion, accountName, eventType, parser.getPayloads(), parser.getPayloadOffset(), parser.getPayloadLength(), requestId);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public EventTypeMetaData getEventTypeMetaData(String accountName, String eventType)
/*      */   {
/*  856 */     return this.eventTypeMetaDataService.getEventTypeMetaData(accountName, eventType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void publishEvents(int requestVersion, String accountName, String eventType, final byte[] payloads, final int payloadOffset, final int payloadLength, final String requestId)
/*      */   {
/*  863 */     for (int i = 0; i < 2; i++) {
/*      */       try {
/*  865 */         final String eventTypeFinal = this.indexNameResolver.resolveEventType(eventType);
/*  866 */         final String accountNameFinal = AccountConfiguration.normalizeAccountName(accountName);
/*  867 */         verifyEventTypeRepairingIndicesIfNecessary(requestVersion, accountName, eventTypeFinal);
/*      */         
/*  869 */         final String docType = this.indexNameResolver.getDocType(accountNameFinal, eventTypeFinal);
/*  870 */         Callable<BulkRequestBuilder> builderCallable = new Callable()
/*      */         {
/*      */           public BulkRequestBuilder call() throws Exception {
/*  873 */             String indexName = ElasticSearchEventService.this.indexNameResolver.resolveInsertAlias(accountNameFinal, eventTypeFinal);
/*  874 */             ObjectListParser parser = ElasticSearchEventService.this.objListParserFactory.make(payloads, payloadOffset, payloadLength);
/*      */             
/*  876 */             Client client = ElasticSearchEventService.this.clientProvider.getInsertClient(accountNameFinal);
/*  877 */             BulkRequestBuilder builder = client.prepareBulk();
/*      */             
/*  879 */             int itemId = 0;
/*  880 */             JsonNode json; while ((json = parser.nextObject()) != null) {
/*  881 */               if (ElasticSearchEventService.log.isDebugEnabled()) {
/*  882 */                 ElasticSearchEventService.log.debug("Adding to bulk publish operation: [{}]", new String(payloads, parser.getRootObjectOffset(), parser.getRootObjectLength(), Charsets.UTF_8));
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*  888 */               json = ElasticSearchEventService.this.ensurePickupTimestamp(json);
/*  889 */               json = ElasticSearchEventService.this.ensureEventTimestamp(json);
/*  890 */               IndexRequestBuilder irb = client.prepareIndex(indexName, docType).setSource(json.toString());
/*      */               
/*      */ 
/*  893 */               if (requestId != null) {
/*  894 */                 irb.setId(requestId + "-" + itemId++);
/*      */               }
/*  896 */               IndexRequest indexRequest = (IndexRequest)irb.request();
/*  897 */               builder.add(indexRequest);
/*      */             }
/*      */             
/*  900 */             return builder;
/*      */           }
/*  902 */         };
/*  903 */         handleBulkRequest(accountNameFinal, eventTypeFinal, payloads, payloadOffset, payloadLength, builderCallable);
/*      */         
/*  905 */         return;
/*      */       }
/*      */       catch (BulkFailureException e) {
/*  908 */         if (i == 0) {
/*  909 */           for (BulkFailure bulkFailure : e.getFailures())
/*      */           {
/*      */ 
/*  912 */             if (bulkFailure.getMessage().contains("TypeMissingException")) {
/*  913 */               this.eventTypeCache.invalidateEventTypeMetaData(accountName, eventType);
/*  914 */               break;
/*      */             }
/*      */             
/*      */           }
/*      */         } else
/*  919 */           throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */       } catch (Exception e) {
/*  921 */         throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void moveEvents(int requestVersion, String accountName, String sourceEventType, MoveEventTypeRequest request)
/*      */   {
/*  929 */     accountName = AccountConfiguration.normalizeAccountName(accountName);
/*  930 */     sourceEventType = this.indexNameResolver.resolveEventType(sourceEventType);
/*      */     
/*  932 */     verifyEventTypeRepairingIndicesIfNecessary(requestVersion, accountName, sourceEventType);
/*      */     
/*  934 */     final String sourceIndex = this.indexNameResolver.resolveSearchAlias(accountName, sourceEventType);
/*  935 */     final String targetEventType = this.indexNameResolver.resolveEventType(request.getTargetEventType());
/*  936 */     final String targetIndex = this.indexNameResolver.resolveInsertAlias(accountName, targetEventType);
/*  937 */     final String sourceDocType = this.indexNameResolver.getDocType(accountName, sourceEventType);
/*  938 */     final String targetDocType = this.indexNameResolver.getDocType(accountName, targetEventType);
/*  939 */     final Client client = this.clientProvider.getInsertClient(accountName);
/*  940 */     final Set<String> unmovedDocIds = new HashSet(request.getDocIds());
/*      */     
/*  942 */     ElasticSearchHelper.Listener<SearchHit[]> indexListener = new com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper.BaseListener() {
/*  943 */       final Set<String> movedDocIds = new HashSet();
/*  944 */       final Set<String> failedDocIds = new HashSet();
/*      */       
/*      */       public void onStart()
/*      */       {
/*  948 */         this.movedDocIds.clear();
/*  949 */         this.failedDocIds.clear();
/*      */       }
/*      */       
/*      */       public ElasticSearchHelper.ListenerResponse onBatch(SearchHit[] hits)
/*      */       {
/*  954 */         BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
/*  955 */         Set<String> batchDocIds = new HashSet();
/*  956 */         for (SearchHit hit : hits) {
/*  957 */           IndexRequest indexRequest = (IndexRequest)client.prepareIndex(targetIndex, targetDocType).setSource(hit.getSource()).setId(hit.getId()).request();
/*      */           
/*  959 */           bulkRequestBuilder.add(indexRequest);
/*  960 */           batchDocIds.add(hit.getId());
/*      */         }
/*  962 */         BulkResponse bulkResponse = (BulkResponse)bulkRequestBuilder.execute().actionGet();
/*  963 */         if (bulkResponse.hasFailures()) {
/*  964 */           ElasticSearchEventService.this.extractFailedDocIds(bulkResponse, this.failedDocIds);
/*  965 */           ElasticSearchEventService.log.error("bulk indexing failures: " + bulkResponse.buildFailureMessage());
/*  966 */           return ElasticSearchHelper.ListenerResponse.RESTART;
/*      */         }
/*  968 */         this.movedDocIds.addAll(batchDocIds);
/*  969 */         return ElasticSearchHelper.ListenerResponse.CONTINUE;
/*      */       }
/*      */       
/*      */       public void onEnd()
/*      */       {
/*  974 */         ElasticSearchEventService.this.deleteMovedDocs(client, sourceIndex, sourceDocType, this.movedDocIds, unmovedDocIds);
/*      */       }
/*      */       
/*      */       public boolean handleRetryFailure()
/*      */       {
/*  979 */         throw new MoveEventsException("Failed to insert documents into destination index [" + targetIndex + "] for event type [" + targetEventType + "] after [" + 5 + "] " + "retries", this.failedDocIds, unmovedDocIds);
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*  984 */     };
/*  985 */     int end = 0;
/*  986 */     while (end < request.getDocIds().size()) {
/*  987 */       int start = end;
/*  988 */       end += 500;
/*  989 */       end = Math.min(request.getDocIds().size(), end);
/*  990 */       List<String> subList = request.getDocIds().subList(start, end);
/*  991 */       QueryBuilder queryBuilder = QueryBuilders.idsQuery(new String[] { sourceDocType }).addIds((String[])subList.toArray(new String[subList.size()]));
/*      */       
/*  993 */       ElasticSearchHelper.fetchResults(client, queryBuilder, new TimeValue(30L, TimeUnit.SECONDS), 500, 5, indexListener, new String[] { sourceIndex });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void deleteMovedDocs(Client client, String sourceIndex, String sourceDocType, Set<String> movedDocIds, Set<String> unmovedDocIds)
/*      */   {
/* 1002 */     if (movedDocIds.isEmpty()) {
/* 1003 */       return;
/*      */     }
/* 1005 */     unmovedDocIds.removeAll(movedDocIds);
/* 1006 */     Set<String> movedButNotDeletedDocIds = new HashSet(movedDocIds);
/* 1007 */     for (int i = 0; i < 5; i++) {
/*      */       try {
/* 1009 */         BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
/* 1010 */         for (String id : movedDocIds) {
/* 1011 */           DeleteRequest indexRequest = (DeleteRequest)client.prepareDelete(sourceIndex, sourceDocType, id).request();
/* 1012 */           bulkRequestBuilder.add(indexRequest);
/*      */         }
/* 1014 */         BulkResponse bulkResponse = (BulkResponse)bulkRequestBuilder.execute().actionGet();
/* 1015 */         if (bulkResponse.hasFailures()) {
/* 1016 */           extractFailedDocIds(bulkResponse, movedButNotDeletedDocIds);
/* 1017 */           log.error("bulk deletion failures: " + bulkResponse.buildFailureMessage());
/*      */         } else {
/* 1019 */           movedButNotDeletedDocIds.removeAll(movedDocIds);
/* 1020 */           return;
/*      */         }
/*      */       } catch (Exception e) {
/* 1023 */         log.error("Caught exception while trying to execute bulk delete: " + e.getMessage());
/*      */       }
/*      */     }
/*      */     
/* 1027 */     String msg = "Failed to delete moved documents from source index [" + sourceIndex + "] and type [" + sourceDocType + "] - bulk response failures occurred in spite of retrying [" + 5 + "] times. DocIds that were moved but not deleted: [" + Joiner.on(",").join(movedButNotDeletedDocIds) + "], docIds that were not moved: [" + Joiner.on(",").join(unmovedDocIds) + "]";
/*      */     
/*      */ 
/*      */ 
/* 1031 */     log.error(msg);
/* 1032 */     throw new MoveEventsException(msg, movedButNotDeletedDocIds, unmovedDocIds);
/*      */   }
/*      */   
/*      */   private void extractFailedDocIds(BulkResponse bulkResponse, Set<String> movedButNotDeleted) {
/* 1036 */     movedButNotDeleted.clear();
/* 1037 */     for (BulkItemResponse itemResponse : bulkResponse.getItems()) {
/* 1038 */       if (itemResponse.isFailed()) {
/* 1039 */         movedButNotDeleted.add(itemResponse.getId());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private JsonNode ensureNodeValue(JsonNode json, String nodeName, String nodeValue) {
/* 1045 */     JsonNode node = json.get(nodeName);
/* 1046 */     if (node == null) {
/* 1047 */       if ((json instanceof ObjectNode)) {
/* 1048 */         ((ObjectNode)json).put(nodeName, nodeValue);
/*      */       } else {
/* 1050 */         Map<String, Object> map = (Map)Reader.DEFAULT_JSON_MAPPER.convertValue(json, new TypeReference() {});
/* 1053 */         map.put(nodeName, nodeValue);
/* 1054 */         json = (JsonNode)Reader.DEFAULT_JSON_MAPPER.convertValue(map, JsonNode.class);
/*      */       }
/*      */     }
/* 1057 */     return json;
/*      */   }
/*      */   
/*      */   private JsonNode ensureEventTimestamp(JsonNode json) {
/* 1061 */     return ensureNodeValue(json, "eventTimestamp", TimeKeeper.currentUtcTimeIso8601());
/*      */   }
/*      */   
/*      */   private JsonNode ensurePickupTimestamp(JsonNode json) {
/* 1065 */     return ensureNodeValue(json, "pickupTimestamp", TimeKeeper.currentUtcTimeIso8601());
/*      */   }
/*      */   
/*      */   public long getCount(int requestVersion, String accountName, String eventType, DateTime from, DateTime to)
/*      */   {
/*      */     try {
/* 1071 */       eventType = this.indexNameResolver.resolveEventType(eventType);
/* 1072 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/*      */       
/* 1074 */       verifyEventTypeRepairingIndicesIfNecessary(requestVersion, accountName, eventType);
/*      */       
/* 1076 */       Client client = this.clientProvider.getSearchClient(accountName);
/* 1077 */       String searchAlias = this.indexNameResolver.resolveSearchAlias(accountName, eventType);
/* 1078 */       ESUtils.refreshIndices(client, new String[] { searchAlias });
/*      */       
/* 1080 */       FilterBuilder timeFilter = FilterBuilders.rangeFilter("pickupTimestamp").from(from).to(to).includeLower(true).includeUpper(true);
/*      */       
/* 1082 */       FilteredQueryBuilder queryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), timeFilter);
/*      */       
/*      */ 
/* 1085 */       long count = ((CountResponse)client.prepareCount(new String[] { searchAlias }).setQuery(queryBuilder).execute().actionGet(this.callTimeout)).getCount();
/*      */       
/*      */ 
/*      */ 
/* 1089 */       log.debug("Counted [{}] documents for account [{}] event type [{}] with date range [{} : {}]", new Object[] { Long.valueOf(count), accountName, eventType, from, to });
/*      */       
/*      */ 
/* 1092 */       return count;
/*      */     } catch (Exception e) {
/* 1094 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void verifyEventTypeRepairingIndicesIfNecessary(int requestVersion, String accountName, String eventType)
/*      */   {
/* 1101 */     for (int i = 1; i <= 3; i++) {
/*      */       try {
/* 1103 */         if (!eventTypeExists(requestVersion, accountName, eventType)) {
/* 1104 */           throw new EventTypeMissingException(accountName, eventType);
/*      */         }
/*      */       }
/*      */       catch (Exception e) {
/* 1108 */         if ((e instanceof EventTypeMissingException)) {
/* 1109 */           throw ((EventTypeMissingException)e);
/*      */         }
/* 1111 */         if (i == 3) {
/* 1112 */           throw Throwables.propagate(e);
/*      */         }
/* 1114 */         ConcurrencyHelper.sleep(500 * i);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void upsertEvents(int requestVersion, List<? extends Upsert> upserts)
/*      */   {
/*      */     try {
/* 1122 */       this.upsertProcessor.process(requestVersion, upserts);
/*      */     } catch (Exception e) {
/* 1124 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private List<String> getAllAccountNamesForEventType(String eventType) {
/* 1129 */     List<String> accountNames = new ArrayList();
/* 1130 */     eventType = this.indexNameResolver.resolveEventType(eventType);
/*      */     SearchResponse response;
/*      */     try
/*      */     {
/* 1134 */       FilteredQueryBuilder queryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.boolFilter().must(FilterBuilders.termFilter("eventType", eventType)));
/*      */       
/*      */ 
/* 1137 */       response = this.eventTypeMetaDataService.getRawEventTypeMetaDataForFilter(queryBuilder);
/*      */     } catch (IndexMissingException e) {
/* 1139 */       return accountNames;
/*      */     }
/*      */     
/* 1142 */     if (response.getHits().totalHits() > 0L) {
/* 1143 */       for (SearchHit hit : response.getHits().getHits()) {
/*      */         try {
/* 1145 */           accountNames.add(hit.getSource().get("accountName").toString());
/*      */         } catch (Exception e) {
/* 1147 */           throw Throwables.propagate(e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1152 */     return accountNames;
/*      */   }
/*      */   
/*      */   public void searchEvents(int requestVersion, String accountName, String eventType, String searchRequest, OutputStream out)
/*      */   {
/*      */     try
/*      */     {
/* 1159 */       eventType = this.indexNameResolver.resolveEventType(eventType);
/* 1160 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/*      */       
/* 1162 */       verifyEventTypeRepairingIndicesIfNecessary(requestVersion, accountName, eventType);
/*      */       
/* 1164 */       searchIndex(accountName, eventType, searchRequest, out);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1168 */       String clusterName = "Unavailable";
/*      */       try {
/* 1170 */         String tempClusterName = this.clientProvider.getSearchClient(accountName).settings().get("cluster.name");
/*      */         
/* 1172 */         if (tempClusterName != null) {
/* 1173 */           clusterName = tempClusterName;
/*      */         }
/*      */       } catch (Exception ignored) {
/* 1176 */         e.addSuppressed(ignored);
/*      */       }
/*      */       
/* 1179 */       throw ElasticSearchServiceHelper.propagateAsException(e, "Failed to search account [{}] on clusters [{}] with event type [{}] on search request [{}]", new Object[] { accountName, clusterName, eventType, searchRequest });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void multiSearchEvents(int requestVersion, String accountName, String searchRequest, OutputStream out)
/*      */   {
/*      */     try
/*      */     {
/* 1188 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/* 1189 */       JsonNode[] nodes = multilineToJson(searchRequest);
/* 1190 */       verifyAndReplaceMultiSearchHeaders(requestVersion, accountName, nodes);
/*      */       
/* 1192 */       Client client = this.clientProvider.getSearchClient(accountName);
/* 1193 */       MultiSearchRequestBuilder multisearchRequestBuilder = client.prepareMultiSearch();
/* 1194 */       for (int i = 0; i < nodes.length; i += 2) {
/* 1195 */         JsonNode header = nodes[i];
/* 1196 */         JsonNode search = nodes[(i + 1)];
/*      */         
/* 1198 */         String index = find("index", header);
/* 1199 */         String eventType = find("eventType", header);
/*      */         
/* 1201 */         String searchType = find("search_type", header);
/* 1202 */         SearchRequestBuilder request = client.prepareSearch(new String[] { index }).setSearchType(searchType).setStats(new String[] { getUniqueQueryId(accountName, searchType) });
/*      */         
/*      */ 
/*      */ 
/* 1206 */         processAndSetRequest(search.toString(), request, accountName, eventType);
/*      */         
/* 1208 */         multisearchRequestBuilder.add(request);
/*      */       }
/*      */       
/*      */ 
/* 1212 */       MultiSearchResponse response = (MultiSearchResponse)multisearchRequestBuilder.execute().actionGet(this.callTimeout);
/*      */       
/* 1214 */       XContentBuilder builder = XContentFactory.jsonBuilder(out);
/* 1215 */       builder.startObject();
/* 1216 */       response.toXContent(builder, ToXContent.EMPTY_PARAMS);
/* 1217 */       builder.endObject();
/* 1218 */       builder.close();
/*      */     } catch (Exception e) {
/* 1220 */       throw ElasticSearchServiceHelper.propagateAsException(e, "Failed to multi-search account [{}] on search request [{}]", new Object[] { accountName, searchRequest });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private String getUniqueQueryId(String accountName, String searchType)
/*      */   {
/* 1227 */     return this.uniqueQueryIdPrefix + accountName + "-" + searchType + "-" + TimeKeeper.currentUtcTimeIso8601();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void queryEvents(final int requestVersion, String accountName, String queryString, String startTime, String endTime, int limitResults, boolean returnEsJson, OutputStream out)
/*      */   {
/*      */     try
/*      */     {
/* 1241 */       final String normalizedAccountName = AccountConfiguration.normalizeAccountName(accountName);
/*      */       
/*      */ 
/*      */ 
/* 1245 */       Function<String, EventSchema> getEventSchemaFunction = new Function()
/*      */       {
/*      */         public EventSchema apply(String eventType) {
/*      */           try {
/* 1249 */             ElasticSearchEventService.this.verifyEventTypeRepairingIndicesIfNecessary(requestVersion, normalizedAccountName, eventType);
/* 1250 */             JsonNode esMapping = ElasticSearchEventService.this.getEventTypeMappingData(normalizedAccountName, eventType);
/* 1251 */             return new EventSchema(eventType, esMapping);
/*      */           } catch (IOException e) {
/* 1253 */             ElasticSearchEventService.log.error("Failed to get event schema for account [{}] and event type [{}]", new Object[] { normalizedAccountName, eventType, e });
/*      */           }
/* 1255 */           return null;
/*      */         }
/*      */         
/*      */ 
/*      */         public boolean equals(Object object)
/*      */         {
/* 1261 */           return this == object;
/*      */         }
/*      */         
/* 1264 */       };
/* 1265 */       this.queryProcessor.query(normalizedAccountName, queryString, startTime, endTime, limitResults, returnEsJson, getEventSchemaFunction, out);
/*      */ 
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */ 
/* 1271 */       throw ElasticSearchServiceHelper.propagateAsException(e, "Failed to parse date time in query for account [{}] on query [{}]", new Object[] { accountName, queryString });
/*      */     }
/*      */     catch (ParsingException e)
/*      */     {
/* 1275 */       throw ElasticSearchServiceHelper.propagateAsException(e, "Failed to parse query for account [{}] on query [{}]", new Object[] { accountName, queryString });
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1279 */       throw ElasticSearchServiceHelper.propagateAsException(e, "Failed to query account [{}] on query [{}]", new Object[] { accountName, queryString });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public JsonNode relevantFields(int requestVersion, String accountName, String eventType, String queryString)
/*      */   {
/*      */     try
/*      */     {
/* 1288 */       return this.sigValueProcessor.query(this, requestVersion, accountName, eventType, queryString);
/*      */     } catch (Exception e) {
/* 1290 */       throw ElasticSearchServiceHelper.propagateAsException(e, "Failed to query account [{}] on query [{}]", new Object[] { accountName, queryString });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private ObjectNode parseSearchRequest(String searchRequest)
/*      */     throws IOException
/*      */   {
/* 1298 */     return (ObjectNode)Reader.DEFAULT_JSON_MAPPER.readTree(searchRequest);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Map<String, Set<String>> eventTypesByAccount()
/*      */   {
/*      */     try
/*      */     {
/* 1307 */       List<Client> clients = this.clientProvider.getAllInsertClients();
/* 1308 */       Set<String> uniqueAliases = new HashSet();
/*      */       
/* 1310 */       for (Client client : clients)
/*      */       {
/*      */ 
/* 1313 */         GetAliasesResponse response = (GetAliasesResponse)client.admin().indices().prepareGetAliases(new String[] { "*___search" }).execute().actionGet();
/*      */         
/*      */ 
/* 1316 */         ImmutableOpenMap<String, List<AliasMetaData>> aliases = response.getAliases();
/* 1317 */         for (ObjectCursor<List<AliasMetaData>> aliasMetaDataCursor : aliases.values()) {
/* 1318 */           for (AliasMetaData aliasMetaData : (List)aliasMetaDataCursor.value) {
/* 1319 */             uniqueAliases.add(aliasMetaData.alias());
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1324 */       if (uniqueAliases.isEmpty()) {
/* 1325 */         return Collections.emptyMap();
/*      */       }
/*      */       
/* 1328 */       return buildAccountToEventTypeMap(uniqueAliases);
/*      */     } catch (Exception e) {
/* 1330 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private EventTypeMetaData preProcessBodyNodeAndReturnMetaData(int requestVersion, String accountName, String eventType, ObjectNode bodyNode, EventTypeMetaData existingMetaData)
/*      */   {
/* 1347 */     JsonNode meta = bodyNode.remove("metaData");
/*      */     EventTypeMetaData metaData;
/* 1349 */     EventTypeMetaData metaData; if (meta == null) { EventTypeMetaData metaData;
/* 1350 */       if (existingMetaData != null) {
/* 1351 */         metaData = existingMetaData;
/*      */       } else {
/* 1353 */         metaData = new EventTypeMetaData(accountName, eventType, this.eventServiceConfiguration.getEventTypeMetaDataDefaults());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1358 */       JsonNode dataCapNode = ((ObjectNode)meta).remove("dailyDataCapVolume");
/* 1359 */       Long dataCapBytes = null;
/* 1360 */       if (dataCapNode != null) {
/*      */         try {
/* 1362 */           dataCapBytes = Long.valueOf(ByteSizes.parseToBytes(dataCapNode.textValue()));
/*      */         } catch (IllegalArgumentException e) {
/* 1364 */           log.error("Could not resolve number of bytes for data cap for account [{}] event type [{}] with body [{}] using default.", new Object[] { accountName, eventType, bodyNode, e });
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 1372 */         metaData = (EventTypeMetaData)Reader.DEFAULT_JSON_MAPPER.reader(EventTypeMetaData.class).readValue(meta);
/* 1373 */         metaData.fill(accountName, eventType, this.eventServiceConfiguration.getEventTypeMetaDataDefaults());
/* 1374 */         metaData.setMaxDailyDataVolumeBytes(dataCapBytes);
/*      */       } catch (IOException e) {
/* 1376 */         throw Throwables.propagate(e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1381 */     if ((bodyNode.get("properties") != null) && (bodyNode.get("properties").size() > 0)) {
/* 1382 */       boolean shouldDefaultNotAnalyzedStringFields = (existingMetaData == null) && (shouldDefaultNotAnalyzedStringFields(requestVersion));
/*      */       
/* 1384 */       performEventServiceMappingTransformation(bodyNode, shouldDefaultNotAnalyzedStringFields, this.eventServiceConfiguration.isAllFieldDisabled());
/*      */     }
/*      */     
/*      */ 
/* 1388 */     return metaData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void performEventServiceMappingTransformation(ObjectNode mapping, boolean shouldApplyChangesForVersion2, boolean shouldDisableAllField)
/*      */   {
/* 1404 */     if (mapping.get("metaData") != null) {
/* 1405 */       mapping.remove("metaData");
/*      */     }
/*      */     
/* 1408 */     addTimestampFields(mapping);
/* 1409 */     removeSourceSettings(mapping);
/*      */     
/* 1411 */     if (shouldApplyChangesForVersion2) {
/* 1412 */       ElasticSearchMappingHelper.ensureStringNotAnalyzedDefault(mapping);
/* 1413 */       ElasticSearchMappingHelper.ensureFieldDataOptimized(mapping);
/*      */     }
/*      */     
/* 1416 */     ElasticSearchMappingHelper.ensureDocValues(mapping);
/*      */     
/* 1418 */     if (shouldDisableAllField) {
/* 1419 */       ObjectNode allDisabledNode = Reader.DEFAULT_JSON_MAPPER.createObjectNode();
/* 1420 */       allDisabledNode.put("enabled", false);
/*      */       
/* 1422 */       mapping.put("_all", allDisabledNode);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void removeSourceSettings(ObjectNode eventTypeDefinition)
/*      */   {
/* 1439 */     eventTypeDefinition.remove("_source");
/* 1440 */     amendSourceSettingsOnNode(eventTypeDefinition);
/*      */   }
/*      */   
/*      */   private static void amendSourceSettingsOnNode(ObjectNode eventTypeDefinition) {
/* 1444 */     ObjectNode properties = (ObjectNode)eventTypeDefinition.get("properties");
/* 1445 */     if (properties != null) {
/* 1446 */       for (JsonNode property : properties) {
/* 1447 */         if (property.isObject()) {
/* 1448 */           if ((property.has("type")) && (property.has("store"))) {
/* 1449 */             ((ObjectNode)property).remove("store");
/*      */           }
/* 1451 */           amendSourceSettingsOnNode((ObjectNode)property);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void addTimestampFields(ObjectNode body)
/*      */   {
/* 1464 */     JsonNode propNode = body.get("properties");
/* 1465 */     if (propNode == null) {
/* 1466 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1470 */     ObjectNode eventTimestampNode = Reader.DEFAULT_JSON_MAPPER.createObjectNode();
/* 1471 */     for (Map.Entry<String, String> entry : TIMESTAMP_PROPERTIES.entrySet()) {
/* 1472 */       eventTimestampNode.put((String)entry.getKey(), (String)entry.getValue());
/*      */     }
/* 1474 */     ((ObjectNode)propNode).put("eventTimestamp", eventTimestampNode);
/*      */     
/* 1476 */     ObjectNode pickupTimestampNode = Reader.DEFAULT_JSON_MAPPER.createObjectNode();
/* 1477 */     for (Map.Entry<String, String> entry : TIMESTAMP_PROPERTIES.entrySet()) {
/* 1478 */       pickupTimestampNode.put((String)entry.getKey(), (String)entry.getValue());
/*      */     }
/* 1480 */     ((ObjectNode)propNode).put("pickupTimestamp", pickupTimestampNode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JsonNode[] multilineToJson(String body)
/*      */   {
/* 1490 */     String[] lines = body.split("\n");
/* 1491 */     List<JsonNode> nodes = new ArrayList(lines.length);
/* 1492 */     for (String line : lines) {
/* 1493 */       if (org.apache.commons.lang.StringUtils.isNotBlank(line)) {
/* 1494 */         nodes.add(ElasticSearchQueryHelper.parseJsonQueryToObjectModel(line));
/*      */       }
/*      */     }
/* 1497 */     return (JsonNode[])nodes.toArray(new JsonNode[nodes.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void verifyAndReplaceMultiSearchHeaders(int requestVersion, String accountName, JsonNode[] nodes)
/*      */   {
/* 1510 */     if ((nodes.length == 0) || (nodes.length % 2 != 0)) {
/* 1511 */       throw new ServiceRequestException("Invalid.NumberOfLines", "Multisearch requests must have an even number of lines and contain at least 2 lines.");
/*      */     }
/*      */     
/*      */ 
/* 1515 */     for (int i = 0; i < nodes.length; i += 2) {
/* 1516 */       if (nodes[i].get("index") != null) {
/* 1517 */         throw new ServiceRequestException("MustNotBeSpecified.IndexField", "The field 'index' must NOT be supplied in a multi search header.");
/*      */       }
/*      */       
/* 1520 */       JsonNode eventType = nodes[i].get("eventType");
/* 1521 */       if (eventType == null) {
/* 1522 */         throw new ServiceRequestException("MustBeSpecified.EventTypeField", "The field 'eventType' MUST be supplied in a multi search header.");
/*      */       }
/*      */       
/*      */ 
/* 1526 */       verifyEventTypeRepairingIndicesIfNecessary(requestVersion, accountName, eventType.textValue());
/* 1527 */       String indexName = this.indexNameResolver.resolveSearchAlias(accountName, eventType.textValue());
/* 1528 */       ((ObjectNode)nodes[i]).put("index", indexName);
/*      */     }
/*      */   }
/*      */   
/*      */   private void searchIndex(String accountName, String eventType, String searchRequest, OutputStream out) throws IOException
/*      */   {
/* 1534 */     String searchAlias = this.indexNameResolver.resolveSearchAlias(accountName, eventType);
/* 1535 */     Client client = this.clientProvider.getSearchClient(accountName);
/*      */     
/* 1537 */     SearchRequestBuilder req = client.prepareSearch(new String[0]).setIndices(new String[] { searchAlias }).setStats(new String[] { getUniqueQueryId(accountName, eventType) });
/*      */     
/*      */ 
/*      */ 
/* 1541 */     processAndSetRequest(searchRequest, req, accountName, eventType);
/*      */     SearchResponse response;
/*      */     try
/*      */     {
/* 1545 */       response = (SearchResponse)req.execute().actionGet(this.callTimeout);
/*      */     } catch (IndexMissingException|TypeMissingException e) {
/* 1547 */       throw new EventTypeMissingException(accountName, eventType);
/*      */     }
/* 1549 */     if (response.isTimedOut())
/*      */     {
/*      */ 
/* 1552 */       throw new ElasticsearchTimeoutException("Search query timed out");
/*      */     }
/* 1554 */     XContentBuilder builder = XContentFactory.jsonBuilder(out);
/* 1555 */     builder.startObject();
/* 1556 */     response.toXContent(builder, ToXContent.EMPTY_PARAMS);
/* 1557 */     builder.endObject();
/* 1558 */     builder.close();
/*      */   }
/*      */   
/*      */   private void processAndSetRequest(String searchRequest, SearchRequestBuilder req, String accountName, String eventType) throws IOException
/*      */   {
/* 1563 */     ObjectNode requestNode = parseSearchRequest(searchRequest);
/*      */     
/* 1565 */     if (requestNode.get("fields") == null) {
/* 1566 */       String[] includes = Strings.EMPTY_ARRAY;
/*      */       
/*      */ 
/*      */ 
/* 1570 */       JsonNode sourceNode = requestNode.remove("_source");
/* 1571 */       if ((sourceNode == null) || (sourceNode.asBoolean(Boolean.TRUE.booleanValue()))) {
/* 1572 */         if (sourceNode != null)
/*      */         {
/* 1574 */           if (sourceNode.isArray()) {
/* 1575 */             includes = arrayOfStringsFromNode((ArrayNode)sourceNode);
/* 1576 */           } else if (sourceNode.isTextual()) {
/* 1577 */             includes = new String[] { sourceNode.asText() };
/*      */           }
/*      */         }
/* 1580 */         ArrayList<String> excludedFields = Lists.newArrayList(EXCLUDED_FIELDS);
/* 1581 */         for (HiddenField hiddenField : this.hiddenFieldsService.getHiddenFields(accountName, eventType)) {
/* 1582 */           excludedFields.add(hiddenField.getFieldName());
/*      */         }
/* 1584 */         req.setFetchSource(includes, Strings.toStringArray(excludedFields));
/*      */       } else {
/* 1586 */         req.setFetchSource(false);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1591 */     req.setExtraSource(requestNode.toString());
/*      */   }
/*      */   
/*      */   private String[] arrayOfStringsFromNode(ArrayNode sourceNode) {
/* 1595 */     String[] ary = new String[sourceNode.size()];
/* 1596 */     Iterator<JsonNode> elements = sourceNode.elements();
/* 1597 */     int i = 0;
/* 1598 */     while (elements.hasNext()) {
/* 1599 */       JsonNode elem = (JsonNode)elements.next();
/* 1600 */       ary[(i++)] = (elem.isTextual() ? elem.asText() : elem.toString());
/*      */     }
/* 1602 */     return ary;
/*      */   }
/*      */   
/*      */   private String find(String field, JsonNode node) throws JsonProcessingException {
/* 1606 */     String fieldValue = null;
/* 1607 */     JsonNode fieldNode = node.get(field);
/* 1608 */     if (fieldNode != null) {
/* 1609 */       fieldValue = fieldNode.textValue();
/*      */     }
/* 1611 */     return fieldValue;
/*      */   }
/*      */   
/*      */ 
/*      */   private void handleBulkRequest(String accountName, String eventType, byte[] payloads, int payloadOffset, int payloadLength, Callable<BulkRequestBuilder> requestBuilderCallable)
/*      */     throws Exception
/*      */   {
/*      */     BulkResponse resp;
/*      */     try
/*      */     {
/* 1621 */       BulkRequestBuilder requestBuilder = (BulkRequestBuilder)requestBuilderCallable.call();
/* 1622 */       resp = (BulkResponse)requestBuilder.get();
/*      */     } catch (Exception e) {
/* 1624 */       log.error("Event Service Publish Error: account [{}], eventType [{}], payload [{}], exception [{}]", new Object[] { accountName, eventType, new String(payloads, payloadOffset, payloadLength, Charsets.UTF_8), e });
/*      */       
/*      */ 
/* 1627 */       throw e;
/*      */     }
/*      */     
/* 1630 */     if (resp.hasFailures()) {
/* 1631 */       throw com.appdynamics.analytics.processor.elasticsearch.exception.ElasticSearchExceptionUtils.getBulkFailureException(resp);
/*      */     }
/*      */   }
/*      */   
/*      */   public List<String> indicesAsOrderedList(UnmodifiableIterator<String> indexNamesIterator)
/*      */   {
/* 1637 */     Ordering.natural().reverse().onResultOf(new Function()
/*      */     {
/*      */ 
/*      */ 
/*      */       @Nullable
/*      */       public Comparable apply(@Nullable String input) {
/* 1643 */         return input == null ? "" : input.toLowerCase(); } }).sortedCopy(Lists.newArrayList(indexNamesIterator));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void upsertEventType(String accountName, String eventType, ObjectNode bodyNode, EventTypeMetaData metaData)
/*      */   {
/* 1676 */     AccountLicensingConfiguration acctLicense = (AccountLicensingConfiguration)this.accountManager.findAccountLicensingConfiguration(accountName, eventType).orNull();
/*      */     
/* 1678 */     log.info("Found licenses [{}] for account [{}] event type [{}]", new Object[] { acctLicense, accountName, eventType });
/* 1679 */     metaData = this.eventTypeMetaDataService.updateEventTypeMetaDataWithLicense(metaData, acctLicense);
/*      */     try
/*      */     {
/* 1682 */       updateIndexAliasAndTypeMapping(metaData, bodyNode);
/* 1683 */       this.eventTypeMetaDataService.storeEventTypeMetaData(metaData, acctLicense);
/*      */     } catch (Exception e) {
/* 1685 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void updateIndexAliasAndTypeMapping(final EventTypeMetaData metaData, final ObjectNode bodyNode)
/*      */   {
/* 1691 */     final String accountName = AccountConfiguration.normalizeAccountName(metaData.getAccountName());
/* 1692 */     final String eventType = this.indexNameResolver.resolveEventType(metaData.getEventType());
/*      */     
/* 1694 */     final String baseIndexName = this.indexNameResolver.resolveBaseIndexName(accountName, eventType);
/*      */     
/* 1696 */     final String docType = this.indexNameResolver.getDocType(accountName, eventType);
/*      */     
/* 1698 */     final Client insertClient = this.clientProvider.getInsertClient(accountName);
/*      */     
/* 1700 */     this.lock.acquireAndExecute(String.format("index_creation_lock_%s_%s", new Object[] { insertClient.settings().get("cluster.name"), accountName }), 30L, TimeUnit.SECONDS, new Callable()
/*      */     {
/*      */       public Void call()
/*      */         throws Exception
/*      */       {
/* 1705 */         boolean indexExists = ESUtils.indexExists(insertClient, baseIndexName);
/* 1706 */         String indexName; String indexName; if (indexExists) {
/* 1707 */           indexName = (String)((GetAliasesResponse)insertClient.admin().indices().prepareGetAliases(new String[] { baseIndexName }).execute().actionGet()).getAliases().keysIt().next();
/*      */         }
/*      */         else {
/* 1710 */           indexName = ElasticSearchEventService.this.indexNameResolver.appendTimestampToBaseIndexName(baseIndexName);
/*      */         }
/*      */         
/* 1713 */         Map<String, Map<String, Object>> mappingMap = new HashMap();
/* 1714 */         mappingMap.put(docType, Reader.DEFAULT_JSON_MAPPER.convertValue(bodyNode, new TypeReference() {}));
/* 1718 */         boolean updateAliases = true;
/* 1719 */         if (indexExists) {
/* 1720 */           ElasticSearchEventService.this.updateDocTypeMapping(insertClient, eventType, accountName, baseIndexName, docType, mappingMap);
/*      */         }
/*      */         else
/*      */         {
/* 1724 */           updateAliases = ElasticSearchEventService.this.createIndexWithDocTypeMappings(insertClient, indexName, baseIndexName, mappingMap);
/*      */         }
/*      */         
/* 1727 */         if (updateAliases) {
/* 1728 */           ElasticSearchEventService.this.updateAliases(indexName, metaData);
/*      */         } else {
/* 1730 */           ElasticSearchEventService.log.error("Index creation failed either because the index already existed or because index creation could not be verified/timed out");
/*      */           
/* 1732 */           throw new com.appdynamics.analytics.processor.event.exception.IndexCreationFailure(indexName);
/*      */         }
/*      */         
/* 1735 */         String accountInsertAlias = ElasticSearchEventService.this.indexNameResolver.resolveInsertAlias(accountName, eventType);
/* 1736 */         String accountSearchAlias = ElasticSearchEventService.this.indexNameResolver.resolveSearchAlias(accountName, eventType);
/* 1737 */         ElasticSearchServiceHelper.refreshAndWaitForIndexReadiness(insertClient, new String[] { accountInsertAlias, accountSearchAlias, baseIndexName });
/*      */         
/*      */ 
/* 1740 */         Client searchClient = ElasticSearchEventService.this.clientProvider.getSearchClient(accountName);
/* 1741 */         if (insertClient != searchClient)
/*      */         {
/* 1743 */           ElasticSearchServiceHelper.refreshAndWaitForIndexReadinessRetrying(searchClient, new String[] { accountSearchAlias });
/*      */         }
/*      */         
/* 1746 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   private void updateAliases(String indexName, EventTypeMetaData metaData) {
/* 1752 */     String accountName = AccountConfiguration.normalizeAccountName(metaData.getAccountName());
/* 1753 */     String eventType = this.indexNameResolver.resolveEventType(metaData.getEventType());
/*      */     
/* 1755 */     String accountInsertAlias = this.indexNameResolver.resolveInsertAlias(accountName, eventType);
/* 1756 */     String accountSearchAlias = this.indexNameResolver.resolveSearchAlias(accountName, eventType);
/*      */     
/* 1758 */     FilterBuilder searchAliasFilterBuilder = searchAliasFilterBuilder(metaData);
/*      */     
/*      */ 
/* 1761 */     int x = 0;
/*      */     
/* 1763 */     for (Client client : this.clientProvider.getAllInsertClients(accountName))
/*      */     {
/* 1765 */       if (x++ == 0) {
/* 1766 */         IndicesAliasesRequestBuilder rb = client.admin().indices().prepareAliases();
/*      */         
/*      */ 
/* 1769 */         rb.removeAlias("*", accountInsertAlias);
/*      */         
/*      */ 
/* 1772 */         if (searchAliasExists(client, indexName, accountSearchAlias)) {
/* 1773 */           updateExistingAliases(client, rb, accountSearchAlias, metaData);
/*      */         }
/*      */         else {
/* 1776 */           rb.addAlias(indexName, accountSearchAlias, searchAliasFilterBuilder);
/*      */         }
/*      */         
/*      */ 
/* 1780 */         rb.addAlias(indexName, accountInsertAlias);
/*      */         
/*      */ 
/* 1783 */         if (!((IndicesAliasesResponse)rb.execute().actionGet()).isAcknowledged()) {
/* 1784 */           throw new EventTypeRegistrationFailure(accountName, eventType);
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/*      */         try
/*      */         {
/* 1792 */           if (!((IndicesAliasesResponse)client.admin().indices().prepareAliases().removeAlias("*", accountInsertAlias).execute().actionGet()).isAcknowledged())
/*      */           {
/*      */ 
/* 1795 */             throw new EventTypeRegistrationFailure(accountName, eventType);
/*      */           }
/*      */         }
/*      */         catch (IndexMissingException|AliasesMissingException ignore) {}
/*      */         
/*      */ 
/*      */         try
/*      */         {
/* 1803 */           IndicesAliasesRequestBuilder rb = client.admin().indices().prepareAliases();
/* 1804 */           boolean addedAlias = updateExistingAliases(client, rb, accountSearchAlias, metaData);
/* 1805 */           if ((addedAlias) && (!((IndicesAliasesResponse)rb.execute().actionGet()).isAcknowledged())) {
/* 1806 */             throw new EventTypeRegistrationFailure(accountName, eventType);
/*      */           }
/*      */         }
/*      */         catch (IndexMissingException|AliasesMissingException ignore) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean searchAliasExists(Client client, String baseIndexName, String accountSearchAlias)
/*      */   {
/* 1817 */     ImmutableOpenMap<String, List<AliasMetaData>> aliasesOnBaseIndex = ((GetAliasesResponse)((GetAliasesRequestBuilder)client.admin().indices().prepareGetAliases(new String[0]).setIndices(new String[] { baseIndexName })).execute().actionGet()).getAliases();
/*      */     
/*      */ 
/* 1820 */     UnmodifiableIterator<List<AliasMetaData>> iter = aliasesOnBaseIndex.valuesIt();
/* 1821 */     while (iter.hasNext()) {
/* 1822 */       for (AliasMetaData aliasMetaData : (List)iter.next()) {
/* 1823 */         if (aliasMetaData.getAlias().equals(accountSearchAlias)) {
/* 1824 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 1828 */     return false;
/*      */   }
/*      */   
/*      */   private boolean updateExistingAliases(Client client, IndicesAliasesRequestBuilder rb, String accountSearchAlias, EventTypeMetaData metaData)
/*      */   {
/* 1833 */     GetAliasesResponse aliasesResponse = (GetAliasesResponse)client.admin().indices().prepareGetAliases(new String[] { accountSearchAlias }).execute().actionGet();
/*      */     
/* 1835 */     UnmodifiableIterator<String> keysIt = aliasesResponse.getAliases().keysIt();
/* 1836 */     boolean addedAlias = false;
/* 1837 */     String key; while (keysIt.hasNext()) {
/* 1838 */       key = (String)keysIt.next();
/* 1839 */       List<AliasMetaData> aliasMetaDatas = (List)aliasesResponse.getAliases().get(key);
/* 1840 */       for (AliasMetaData aliasMetaData : aliasMetaDatas) {
/* 1841 */         if (aliasMetaData.filteringRequired()) {
/*      */           try {
/* 1843 */             String filter = aliasMetaData.getFilter().string();
/* 1844 */             JsonNode filterNode = Reader.DEFAULT_JSON_MAPPER.readTree(filter);
/* 1845 */             if ((filterNode.get("bool") != null) && (filterNode.get("bool").get("must") != null))
/*      */             {
/* 1847 */               ObjectNode pickupTimestampNode = null;
/* 1848 */               if (filterNode.get("bool").get("must").isArray()) {
/* 1849 */                 for (JsonNode node : filterNode.get("bool").get("must")) {
/* 1850 */                   if (node.has("range")) {
/* 1851 */                     pickupTimestampNode = (ObjectNode)node.get("range").get("pickupTimestamp");
/*      */                   }
/*      */                 }
/* 1854 */               } else if (filterNode.get("bool").get("must").get("range") != null) {
/* 1855 */                 pickupTimestampNode = (ObjectNode)filterNode.get("bool").get("must").get("range").get("pickupTimestamp");
/*      */               }
/*      */               
/* 1858 */               if (pickupTimestampNode != null) {
/* 1859 */                 pickupTimestampNode.put("from", buildHotLifespanFilter(metaData));
/* 1860 */                 pickupTimestampNode.put("include_upper", metaData.getHotLifespanInDays().intValue() > 0);
/*      */               }
/*      */             }
/* 1863 */             rb.addAlias(key, aliasMetaData.alias(), filterNode.toString());
/* 1864 */             addedAlias = true;
/*      */           } catch (IOException e) {
/* 1866 */             throw Throwables.propagate(e);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1872 */     return addedAlias;
/*      */   }
/*      */   
/*      */   private void updateSearchAliasIfChanged(EventTypeMetaData metaData) {
/* 1876 */     String eventType = metaData.getEventType();
/* 1877 */     String accountName = metaData.getAccountName();
/*      */     
/* 1879 */     List<Client> clients = this.clientProvider.getAllInsertClients(accountName);
/* 1880 */     String accountSearchAlias = this.indexNameResolver.resolveSearchAlias(accountName, eventType);
/*      */     
/* 1882 */     for (Client client : clients) {
/* 1883 */       GetAliasesResponse getAliasesResponse = (GetAliasesResponse)client.admin().indices().prepareGetAliases(new String[] { accountSearchAlias }).execute().actionGet();
/*      */       
/*      */ 
/* 1886 */       if (getAliasesResponse.getAliases().size() >= 1) {
/* 1887 */         List<AliasMetaData> aliasMetaDatas = (List)((ObjectCursor)getAliasesResponse.getAliases().values().iterator().next()).value;
/*      */         try
/*      */         {
/* 1890 */           String currentFilter = ((AliasMetaData)aliasMetaDatas.get(0)).filter().string();
/* 1891 */           String newFilter = searchAliasFilterBuilder(metaData).toString();
/* 1892 */           if (!Reader.DEFAULT_JSON_MAPPER.readTree(currentFilter).equals(Reader.DEFAULT_JSON_MAPPER.readTree(newFilter))) {
/* 1893 */             updateSearchAlias(client, metaData, accountSearchAlias);
/*      */           }
/*      */         } catch (IOException e) {
/* 1896 */           throw Throwables.propagate(e);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   void updateSearchAlias(Client client, EventTypeMetaData metaData, String accountSearchAlias) {
/* 1904 */     String eventType = metaData.getEventType();
/* 1905 */     String accountName = metaData.getAccountName();
/*      */     
/* 1907 */     if (!((IndicesAliasesResponse)client.admin().indices().prepareAliases().addAlias(accountSearchAlias, accountSearchAlias, searchAliasFilterBuilder(metaData)).execute().actionGet()).isAcknowledged())
/*      */     {
/*      */ 
/*      */ 
/* 1911 */       throw new EventTypeRegistrationFailure(accountName, eventType);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean createIndexWithDocTypeMappings(Client client, String indexName, String baseIndexName, Map<String, Map<String, Object>> mappingMap)
/*      */   {
/* 1917 */     List<String> aliases = new ArrayList();
/* 1918 */     aliases.add(baseIndexName);
/*      */     
/* 1920 */     IndexCreationSettings settings = new IndexCreationSettings().setIndexMapperDynamic(false);
/* 1921 */     return this.indexCreationManager.createIndexNotLocking(client, indexName, settings.getSettings(), aliases, mappingMap);
/*      */   }
/*      */   
/*      */ 
/*      */   private void updateDocTypeMapping(Client client, String eventType, String accountName, String baseIndexName, String docType, Map<String, Map<String, Object>> mappingMap)
/*      */   {
/* 1927 */     if (!((PutMappingResponse)client.admin().indices().preparePutMapping(new String[] { baseIndexName }).setType(docType).setSource((Map)mappingMap.get(docType)).execute().actionGet()).isAcknowledged())
/*      */     {
/*      */ 
/* 1930 */       throw new EventTypeRegistrationFailure(accountName, eventType);
/*      */     }
/*      */   }
/*      */   
/*      */   private FilterBuilder searchAliasFilterBuilder(EventTypeMetaData metaData) {
/* 1935 */     List<FilterBuilder> filterBuilders = new ArrayList(3);
/* 1936 */     filterBuilders.add(FilterBuilders.typeFilter(this.indexNameResolver.getDocType(metaData.getAccountName(), metaData.getEventType())));
/*      */     
/* 1938 */     filterBuilders.add(FilterBuilders.rangeFilter("pickupTimestamp").from(buildHotLifespanFilter(metaData)).to("now/d").includeLower(true).includeUpper(metaData.getHotLifespanInDays().intValue() > 0));
/*      */     
/*      */ 
/* 1941 */     return FilterBuilders.boolFilter().must((FilterBuilder[])filterBuilders.toArray(new FilterBuilder[filterBuilders.size()]));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String buildHotLifespanFilter(EventTypeMetaData metaData)
/*      */   {
/* 1948 */     return "now/d-" + (metaData.getHotLifespanInDays().intValue() > 0 ? metaData.getHotLifespanInDays().intValue() + 1 : 0) + "d/d";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Map<String, Set<String>> buildAccountToEventTypeMap(Set<String> uniqueAliases)
/*      */   {
/* 1959 */     Map<String, Set<String>> accountToEventTypeMap = new HashMap();
/* 1960 */     for (String alias : uniqueAliases) {
/* 1961 */       String accountName = this.indexNameResolver.accountNameFromSearchAlias(alias);
/* 1962 */       if (accountName != null)
/*      */       {
/*      */ 
/* 1965 */         String eventType = this.indexNameResolver.eventTypeFromSearchAlias(alias);
/* 1966 */         if (eventType == null) {
/* 1967 */           log.warn("Processing alias [" + alias + "] - found account name [" + accountName + "], but couldn't resolve event type");
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/* 1973 */           Set<String> eventTypes = (Set)accountToEventTypeMap.get(accountName);
/* 1974 */           if (eventTypes == null) {
/* 1975 */             eventTypes = new HashSet();
/* 1976 */             accountToEventTypeMap.put(accountName, eventTypes);
/*      */           }
/* 1978 */           eventTypes.add(eventType);
/*      */         } } }
/* 1980 */     return accountToEventTypeMap;
/*      */   }
/*      */   
/*      */   public List<String> getSearchIndexNamesForUpsert(String accountName, String eventType)
/*      */   {
/* 1985 */     for (int i = 0; i < 3; i++) {
/* 1986 */       GetAliasesResponse getAliasesResponse = (GetAliasesResponse)this.clientProvider.getSearchClient(accountName).admin().indices().prepareGetAliases(new String[] { this.indexNameResolver.resolveSearchAlias(accountName, eventType) }).execute().actionGet();
/*      */       
/*      */ 
/* 1989 */       EventTypeMetaData eventTypeMetaData = getEventTypeMetaData(accountName, eventType);
/* 1990 */       List<String> indexNames = indicesAsOrderedList(getAliasesResponse.getAliases().keysIt());
/* 1991 */       if (indexNames.isEmpty())
/*      */       {
/* 1993 */         ElasticSearchServiceHelper.refreshAndWaitForIndexReadiness(this.clientProvider.getSearchClient(accountName), new String[] { this.indexNameResolver.resolveSearchAlias(accountName, eventType) });
/*      */         
/* 1995 */         ConcurrencyHelper.sleep(500 * i);
/*      */       }
/*      */       else {
/* 1998 */         return findRelevantIndicesForParentLookUp(indexNames, eventTypeMetaData.getLookBackTimePeriodSeconds());
/*      */       } }
/* 2000 */     log.warn("Could not get list of search index names for account [{}] and event type [{}] - perhaps the event type has not been registered.", accountName, eventType);
/*      */     
/* 2002 */     throw new EventTypeMissingException(accountName, eventType);
/*      */   }
/*      */   
/*      */   List<String> findRelevantIndicesForParentLookUp(List<String> orderedIndices, Long lookBackTimePeriodSeconds) {
/* 2006 */     if (orderedIndices.isEmpty()) {
/* 2007 */       return Collections.emptyList();
/*      */     }
/*      */     
/* 2010 */     if ((lookBackTimePeriodSeconds == null) || (lookBackTimePeriodSeconds.longValue() < 0L)) {
/* 2011 */       return orderedIndices;
/*      */     }
/* 2013 */     List<String> relevantIndices = new ArrayList();
/* 2014 */     long currentTS = System.currentTimeMillis();
/*      */     
/*      */ 
/* 2017 */     relevantIndices.add(orderedIndices.get(0));
/*      */     
/* 2019 */     for (int i = 1; i < orderedIndices.size(); i++) {
/* 2020 */       String indexName = (String)orderedIndices.get(i);
/* 2021 */       long indexCreationTimeMS = this.indexNameResolver.indexCreationDateFromFullName(indexName).getMillis();
/*      */       
/* 2023 */       if (lookBackTimePeriodSeconds.longValue() >= (currentTS - indexCreationTimeMS) / 1000L) {
/* 2024 */         relevantIndices.add(indexName);
/*      */       }
/*      */     }
/* 2027 */     return relevantIndices;
/*      */   }
/*      */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ElasticSearchEventService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */