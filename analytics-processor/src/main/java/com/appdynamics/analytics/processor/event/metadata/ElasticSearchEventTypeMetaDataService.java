/*     */ package com.appdynamics.analytics.processor.event.metadata;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountLicensingConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.ElasticSearchServiceHelper;
/*     */ import com.appdynamics.analytics.processor.event.EventTypeMetaData;
/*     */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*     */ import com.appdynamics.analytics.processor.event.configuration.EventServiceConfiguration;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeDeletionFailureException;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeMissingException;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectReader;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.ShardOperationFailedException;
/*     */ import org.elasticsearch.action.bulk.BulkRequestBuilder;
/*     */ import org.elasticsearch.action.bulk.BulkResponse;
/*     */ import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
/*     */ import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
/*     */ import org.elasticsearch.action.deletebyquery.IndexDeleteByQueryResponse;
/*     */ import org.elasticsearch.action.search.MultiSearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.MultiSearchResponse;
/*     */ import org.elasticsearch.action.search.MultiSearchResponse.Item;
/*     */ import org.elasticsearch.action.search.SearchRequest;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.update.UpdateRequest;
/*     */ import org.elasticsearch.action.update.UpdateRequestBuilder;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
/*     */ import org.elasticsearch.index.query.BoolFilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilders;
/*     */ import org.elasticsearch.index.query.FilteredQueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.indices.IndexMissingException;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.elasticsearch.search.SearchHits;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ public class ElasticSearchEventTypeMetaDataService
/*     */   implements EventTypeMetaDataService
/*     */ {
/*  62 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchEventTypeMetaDataService.class);
/*     */   
/*     */   public static final int SEARCH_RESULTS_SIZE = 10000;
/*     */   
/*     */   final ClientProvider clientProvider;
/*     */   
/*     */   final EventServiceConfiguration eventServiceConfiguration;
/*     */   
/*     */   final IndexNameResolver indexNameResolver;
/*     */   
/*     */   final EventTypeCache eventTypeCache;
/*     */   
/*     */ 
/*     */   @Inject
/*     */   public ElasticSearchEventTypeMetaDataService(ClientProvider clientProvider, EventServiceConfiguration eventServiceConfiguration, IndexNameResolver indexNameResolver, EventTypeCache eventTypeCache)
/*     */   {
/*  78 */     this.clientProvider = clientProvider;
/*  79 */     this.eventServiceConfiguration = eventServiceConfiguration;
/*  80 */     this.indexNameResolver = indexNameResolver;
/*  81 */     this.eventTypeCache = eventTypeCache;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EventTypeMetaData getEventTypeMetaData(String accountName, String eventType)
/*     */   {
/*  89 */     EventTypeMetaData eventTypeMetaData = this.eventTypeCache.getEventTypeMetadata(accountName, eventType);
/*  90 */     if (eventTypeMetaData == null) {
/*  91 */       eventTypeMetaData = getEventTypeMetaDataNoCache(accountName, eventType);
/*  92 */       if (eventTypeMetaData != null) {
/*  93 */         this.eventTypeCache.storeEventTypeMetadata(accountName, eventType, eventTypeMetaData);
/*     */       }
/*     */     }
/*  96 */     return eventTypeMetaData;
/*     */   }
/*     */   
/*     */ 
/*     */   public EventTypeMetaData deleteEventTypeMetaData(String accountName, String eventType)
/*     */   {
/* 102 */     EventTypeMetaData eventTypeMetaData = getEventTypeMetaDataNoCache(accountName, eventType);
/* 103 */     if (eventTypeMetaData == null) {
/* 104 */       log.error("Attempted to delete account [{}] and event type [{}] which was not registered. ", accountName, eventType);
/*     */       
/* 106 */       throw new EventTypeMissingException(accountName, eventType);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 111 */     Client client = this.clientProvider.getAdminClient();
/* 112 */     DeleteByQueryResponse response = (DeleteByQueryResponse)client.prepareDeleteByQuery(new String[] { "event_type_metadata" }).setTypes(new String[] { "event_type_metadata" }).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.boolFilter().must(new FilterBuilder[] { FilterBuilders.termFilter("accountName", accountName), FilterBuilders.termFilter("eventType", eventType) }))).execute().actionGet();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 120 */     if (response.getIndex("event_type_metadata").getFailures().length != 0) {
/* 121 */       log.error("Failed to remove event_type_metadata for account [{}] and event type [{}] with shard failures [{}]", new Object[] { accountName, eventType, response.getIndex("event_type_metadata").getFailures()[0].reason() });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 126 */       throw new EventTypeDeletionFailureException(accountName, eventType);
/*     */     }
/*     */     
/* 129 */     this.eventTypeCache.invalidateEventTypeMetaData(accountName, eventType);
/* 130 */     return eventTypeMetaData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EventTypeMetaData getEventTypeMetaDataNoCache(String accountName, String eventType)
/*     */   {
/*     */     try
/*     */     {
/* 139 */       eventType = this.indexNameResolver.resolveEventType(eventType);
/* 140 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/*     */       
/*     */ 
/* 143 */       Client client = this.clientProvider.getAdminClient();
/* 144 */       SearchResponse response = (SearchResponse)client.prepareSearch(new String[] { "event_type_metadata" }).setTypes(new String[] { "event_type_metadata" }).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.boolFilter().must(new FilterBuilder[] { FilterBuilders.termFilter("accountName", accountName), FilterBuilders.termFilter("eventType", eventType) }))).execute().actionGet();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */       if (response.getHits().totalHits() == 1L)
/*     */       {
/*     */ 
/* 155 */         if ((response.getHits().getHits() == null) || (response.getHits().getHits().length == 0)) {
/* 156 */           return null;
/*     */         }
/* 158 */         String mapping = response.getHits().getHits()[0].getSourceAsString();
/* 159 */         return (EventTypeMetaData)Reader.DEFAULT_JSON_MAPPER.reader(EventTypeMetaData.class).readValue(mapping);
/*     */       }
/*     */     } catch (IndexMissingException e) {
/* 162 */       return null;
/*     */     } catch (Exception e) {
/* 164 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*     */     }
/*     */     
/* 167 */     return null;
/*     */   }
/*     */   
/*     */   public SearchResponse getRawEventTypeMetaDataForFilter(QueryBuilder queryBuilder)
/*     */   {
/* 172 */     Client client = this.clientProvider.getAdminClient();
/* 173 */     return (SearchResponse)client.prepareSearch(new String[] { "event_type_metadata" }).setTypes(new String[] { "event_type_metadata" }).setQuery(queryBuilder).setSize(Integer.MAX_VALUE).execute().actionGet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EventTypeMetaData storeEventTypeMetaData(EventTypeMetaData metaData, AccountLicensingConfiguration acctLicenseConfig)
/*     */   {
/*     */     try
/*     */     {
/* 191 */       metaData = updateEventTypeMetaDataWithLicense(metaData, acctLicenseConfig);
/*     */       
/* 193 */       String id = AccountConfiguration.normalizeAccountName(metaData.getAccountName()) + "-" + this.indexNameResolver.resolveEventType(metaData.getEventType());
/*     */       try
/*     */       {
/* 196 */         ObjectNode rootNode = Reader.DEFAULT_JSON_MAPPER.createObjectNode();
/* 197 */         rootNode.put("doc", Reader.DEFAULT_JSON_MAPPER.valueToTree(metaData));
/*     */         
/* 199 */         this.clientProvider.getAdminClient().prepareUpdate("event_type_metadata", "event_type_metadata", id).setSource(Reader.DEFAULT_JSON_MAPPER.writeValueAsBytes(rootNode)).setDocAsUpsert(true).setRetryOnConflict(this.eventServiceConfiguration.getUpdateConflictRetries()).execute().actionGet();
/*     */ 
/*     */       }
/*     */       catch (DocumentAlreadyExistsException e)
/*     */       {
/* 204 */         log.warn("EventTypeMetaData with id [" + id + "] already existed - ignoring as this most " + "likely means multiple threads were trying to insert at the same time");
/*     */       }
/*     */       
/*     */ 
/* 208 */       ElasticSearchServiceHelper.refreshAndWaitForIndexReadiness(this.clientProvider.getAdminClient(), new String[] { "event_type_metadata" });
/*     */       
/* 210 */       this.eventTypeCache.invalidateEventTypeMetaData(metaData.getAccountName(), metaData.getEventType());
/* 211 */       return metaData;
/*     */     } catch (Exception e) {
/* 213 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<EventTypeMetaData> updateEventTypeMetaData(List<AccountConfiguration> accountConfigurations)
/*     */   {
/* 225 */     Client client = this.clientProvider.getAdminClient();
/* 226 */     Map<EventTypeMetaData, AccountLicensingConfiguration> metaDataToLicenseMap = new HashMap();
/* 227 */     List<Pair<String, AccountLicensingConfiguration>> accountLicensesList = new ArrayList();
/* 228 */     MultiSearchRequestBuilder multiSearchRequestBuilder = client.prepareMultiSearch();
/*     */     try {
/* 230 */       if (accountConfigurations == null) {
/* 231 */         return new ArrayList();
/*     */       }
/*     */       
/* 234 */       for (AccountConfiguration accConfig : accountConfigurations)
/*     */       {
/* 236 */         String accountName = accConfig.getAccountName();
/* 237 */         List<AccountLicensingConfiguration> licenseConfigs = accConfig.getLicensingConfigurations();
/*     */         
/*     */ 
/* 240 */         List<SearchRequest> accSearchReqs = buildSearchRequests(accountName, client, licenseConfigs);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 245 */         for (int idx = 0; idx < licenseConfigs.size(); idx++) {
/* 246 */           Pair<String, AccountLicensingConfiguration> pair = new Pair(accountName, licenseConfigs.get(idx));
/* 247 */           accountLicensesList.add(pair);
/* 248 */           multiSearchRequestBuilder.add((SearchRequest)accSearchReqs.get(idx));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 255 */       if (accountLicensesList.size() == 0) {
/* 256 */         log.debug("No license configurations found. Nothing to update in [{}] index", "event_type_metadata");
/* 257 */         return new ArrayList();
/*     */       }
/*     */       
/* 260 */       MultiSearchResponse multiSearchResponse = (MultiSearchResponse)multiSearchRequestBuilder.execute().actionGet();
/* 261 */       MultiSearchResponse.Item[] responses = multiSearchResponse.getResponses();
/*     */       
/* 263 */       for (int i = 0; i < responses.length; i++) {
/* 264 */         String accountName = (String)((Pair)accountLicensesList.get(i)).getLeft();
/* 265 */         AccountLicensingConfiguration licensingConfiguration = (AccountLicensingConfiguration)((Pair)accountLicensesList.get(i)).getRight();
/* 266 */         if (responses[i].isFailure()) {
/* 267 */           log.error("Unable to update metadata for account [{}] and event types matching regex [{}] as querying index [{}] resulted in the following error [{}]", new Object[] { accountName, licensingConfiguration.getEventType(), "event_type_metadata", responses[i].getFailureMessage() });
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 272 */           SearchHits hits = responses[i].getResponse().getHits();
/* 273 */           for (SearchHit hit : hits.getHits())
/*     */           {
/* 275 */             EventTypeMetaData metaData = (EventTypeMetaData)Reader.DEFAULT_JSON_MAPPER.reader(EventTypeMetaData.class).readValue(hit.getSourceAsString());
/*     */             
/*     */ 
/*     */ 
/* 279 */             metaDataToLicenseMap.put(metaData, licensingConfiguration);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 285 */       if (!metaDataToLicenseMap.isEmpty())
/*     */       {
/* 287 */         List<EventTypeMetaData> updatedMetaDataList = storeEventTypeMetaDataInBulk(metaDataToLicenseMap);
/*     */         try {
/* 289 */           ESUtils.refreshIndices(this.clientProvider.getAdminClient(), new String[] { "event_type_metadata" });
/*     */         } catch (Exception e) {
/* 291 */           throw ElasticSearchServiceHelper.propagateAsException(e);
/*     */         }
/*     */       }
/* 294 */       return new ArrayList();
/*     */ 
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 299 */       log.error("error occurred while updating the event type metadata", e);
/*     */     }
/*     */     
/* 302 */     return new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */   public EventTypeMetaData updateEventTypeMetaDataWithLicense(EventTypeMetaData metaData, AccountLicensingConfiguration acctLicenseConfig)
/*     */   {
/* 308 */     if (acctLicenseConfig != null) {
/* 309 */       if (acctLicenseConfig.getDataRetentionPeriodDays() != null) {
/* 310 */         metaData.setHotLifespanInDays(Integer.valueOf(acctLicenseConfig.getDataRetentionPeriodDays().intValue()));
/*     */       }
/*     */       
/* 313 */       if (acctLicenseConfig.getMaxDailyBytes() != null) {
/* 314 */         metaData.setMaxDailyDataVolumeBytes(acctLicenseConfig.getMaxDailyBytes());
/*     */       }
/*     */       
/* 317 */       if (acctLicenseConfig.getMaxDailyDocuments() != null) {
/* 318 */         metaData.setDailyDocumentCapVolume(acctLicenseConfig.getMaxDailyDocuments());
/*     */       }
/*     */       
/* 321 */       if (acctLicenseConfig.getExpirationDate() != null) {
/* 322 */         metaData.setExpirationDate(acctLicenseConfig.getExpirationDate());
/*     */       }
/*     */     }
/* 325 */     return metaData;
/*     */   }
/*     */   
/*     */   boolean eventTypeExists(String accountName, String eventType) {
/*     */     try {
/* 330 */       eventType = this.indexNameResolver.resolveEventType(eventType);
/* 331 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/*     */       
/* 333 */       return (this.eventTypeCache.accountNameEventTypeExistsWithCurrentInsertClient(accountName, eventType)) || (getEventTypeMetaData(accountName, eventType) != null);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 337 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void verifyEventType(String accountName, String eventType)
/*     */   {
/* 343 */     if (!eventTypeExists(accountName, eventType)) {
/* 344 */       throw new EventTypeMissingException(accountName, eventType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   public List<EventTypeMetaData> storeEventTypeMetaDataInBulk(Map<EventTypeMetaData, AccountLicensingConfiguration> metaDataToLicense)
/*     */   {
/* 357 */     if (metaDataToLicense.isEmpty()) {
/* 358 */       return Collections.emptyList();
/*     */     }
/* 360 */     Client client = this.clientProvider.getAdminClient();
/* 361 */     BulkRequestBuilder bulkUpdateBuilder = client.prepareBulk();
/* 362 */     List<EventTypeMetaData> updatedMetaDataList = new ArrayList();
/*     */     try {
/* 364 */       for (Map.Entry<EventTypeMetaData, AccountLicensingConfiguration> entry : metaDataToLicense.entrySet())
/*     */       {
/* 366 */         updatedMetaDataList.add(updateEventTypeMetaDataWithLicense((EventTypeMetaData)entry.getKey(), (AccountLicensingConfiguration)entry.getValue()));
/*     */         
/* 368 */         String id = ((EventTypeMetaData)entry.getKey()).getAccountName() + "-" + ((EventTypeMetaData)entry.getKey()).getEventType();
/*     */         
/* 370 */         this.eventTypeCache.invalidateEventTypeMetaData(((EventTypeMetaData)entry.getKey()).getAccountName(), ((EventTypeMetaData)entry.getKey()).getEventType());
/*     */         
/*     */ 
/* 373 */         ObjectNode rootNode = Reader.DEFAULT_JSON_MAPPER.createObjectNode();
/* 374 */         rootNode.put("doc", Reader.DEFAULT_JSON_MAPPER.valueToTree(entry.getKey()));
/* 375 */         UpdateRequest request = (UpdateRequest)client.prepareUpdate("event_type_metadata", "event_type_metadata", id).setSource(Reader.DEFAULT_JSON_MAPPER.writeValueAsBytes(rootNode)).setDocAsUpsert(true).setRetryOnConflict(this.eventServiceConfiguration.getUpdateConflictRetries()).request();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 381 */         bulkUpdateBuilder.add(request);
/*     */       }
/*     */       
/* 384 */       BulkResponse bulkResponse = (BulkResponse)bulkUpdateBuilder.execute().actionGet();
/* 385 */       if (bulkResponse.hasFailures()) {
/* 386 */         throw new RuntimeException(String.format("The following error occurred while updating the [%s] index with latest license configuration [%s]", new Object[] { "event_type_metadata", bulkResponse.buildFailureMessage() }));
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 391 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*     */     }
/* 393 */     return updatedMetaDataList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<SearchRequest> buildSearchRequests(String accountName, Client client, List<AccountLicensingConfiguration> licensingConfigurations)
/*     */   {
/* 408 */     List<SearchRequest> searchRequests = new ArrayList();
/* 409 */     for (int i = 0; i < licensingConfigurations.size(); i++) {
/* 410 */       String eventType = this.indexNameResolver.resolveEventType(((AccountLicensingConfiguration)licensingConfigurations.get(i)).getEventType());
/* 411 */       FilteredQueryBuilder queryBuilder = QueryBuilders.filteredQuery(QueryBuilders.regexpQuery("eventType", eventType), FilterBuilders.termFilter("accountName", accountName));
/*     */       
/*     */ 
/*     */ 
/* 415 */       SearchRequest searchRequest = client.prepareSearch(new String[] { "event_type_metadata" }).setTypes(new String[] { "event_type_metadata" }).setQuery(queryBuilder).setSize(10000).request();
/*     */       
/*     */ 
/* 418 */       searchRequests.add(searchRequest);
/*     */     }
/* 420 */     return searchRequests;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/metadata/ElasticSearchEventTypeMetaDataService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */