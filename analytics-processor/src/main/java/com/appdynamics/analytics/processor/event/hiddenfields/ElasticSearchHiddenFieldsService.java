/*     */ package com.appdynamics.analytics.processor.event.hiddenfields;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.DefaultIndexNameResolver;
/*     */ import com.appdynamics.analytics.processor.event.ElasticSearchServiceHelper;
/*     */ import com.appdynamics.analytics.processor.event.configuration.EventServiceConfiguration;
/*     */ import com.appdynamics.analytics.processor.event.exception.HiddenFieldNotExistsException;
/*     */ import com.appdynamics.analytics.processor.event.exception.InvalidHiddenFieldException;
/*     */ import com.appdynamics.analytics.processor.event.metadata.EventTypeMetaDataService;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectReader;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.cache.Cache;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.bulk.BulkRequestBuilder;
/*     */ import org.elasticsearch.action.bulk.BulkResponse;
/*     */ import org.elasticsearch.action.delete.DeleteRequestBuilder;
/*     */ import org.elasticsearch.action.delete.DeleteResponse;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.update.UpdateRequest;
/*     */ import org.elasticsearch.action.update.UpdateRequestBuilder;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.index.query.BoolFilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilders;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.indices.IndexMissingException;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.elasticsearch.search.SearchHits;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ public class ElasticSearchHiddenFieldsService
/*     */   implements HiddenFieldsService
/*     */ {
/*     */   static final String FIELD_SEPARATOR = "___";
/*     */   static final String PROPERTIES_FIELD = "properties";
/*     */   public static final String META_DATA_HIDDEN_FIELDS_IDX_NAME = "event_type_metadata_hidden_fields";
/*     */   public static final String META_DATA_HIDDEN_FIELDS_DOC_NAME = "event_type_metadata_hidden_fields";
/*     */   public static final String EVENT_TYPE = "eventType";
/*     */   volatile Cache<String, List<HiddenField>> eventTypeMetaDataHiddenFieldsCache;
/*     */   final ClientProvider clientProvider;
/*     */   final EventTypeMetaDataService eventTypeMetaDataService;
/*     */   final EventServiceConfiguration eventServiceConfiguration;
/*     */   
/*     */   @Inject
/*     */   public ElasticSearchHiddenFieldsService(ClientProvider clientProvider, EventTypeMetaDataService eventTypeMetaDataService, EventServiceConfiguration eventServiceConfiguration)
/*     */   {
/*  77 */     this.clientProvider = clientProvider;
/*  78 */     this.eventTypeMetaDataService = eventTypeMetaDataService;
/*  79 */     this.eventServiceConfiguration = eventServiceConfiguration;
/*  80 */     this.eventTypeMetaDataHiddenFieldsCache = CacheBuilder.newBuilder().maximumSize(eventServiceConfiguration.getEventTypeCacheSize()).expireAfterWrite(eventServiceConfiguration.getEventTypeMetaDataCacheTtlMinutes(), TimeUnit.MINUTES).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void hideFields(String accountName, String eventType, List<HiddenField> hiddenFields)
/*     */   {
/*  88 */     accountName = AccountConfiguration.normalizeAccountName(accountName);
/*  89 */     eventType = DefaultIndexNameResolver.validateAndResolveEventType(eventType);
/*  90 */     this.eventTypeMetaDataService.verifyEventType(accountName, eventType);
/*     */     try
/*     */     {
/*  93 */       Client client = this.clientProvider.getAdminClient();
/*  94 */       BulkRequestBuilder bulkUpdateBuilder = client.prepareBulk();
/*     */       
/*  96 */       invalidateEventTypeMetaDataHiddenFieldsCache(accountName, eventType);
/*     */       
/*  98 */       for (HiddenField hiddenField : hiddenFields) {
/*  99 */         String hiddenFieldExpression = hiddenField.getFieldName();
/* 100 */         String id = createHiddenFieldId(accountName, eventType, hiddenFieldExpression);
/*     */         
/*     */ 
/* 103 */         if (hiddenFieldExpression.contains("*")) {
/* 104 */           throw new InvalidHiddenFieldException(hiddenFieldExpression, "Wildcards (*) not supported");
/*     */         }
/*     */         
/* 107 */         ObjectNode rootNode = Reader.DEFAULT_JSON_MAPPER.createObjectNode();
/* 108 */         ObjectNode hiddenFieldJson = (ObjectNode)Reader.DEFAULT_JSON_MAPPER.valueToTree(hiddenField);
/* 109 */         hiddenFieldJson.put("eventType", eventType);
/* 110 */         hiddenFieldJson.put("accountName", accountName);
/* 111 */         rootNode.put("doc", hiddenFieldJson);
/* 112 */         UpdateRequest request = (UpdateRequest)client.prepareUpdate("event_type_metadata_hidden_fields", "event_type_metadata_hidden_fields", id).setSource(Reader.DEFAULT_JSON_MAPPER.writeValueAsBytes(rootNode)).setDocAsUpsert(true).setRetryOnConflict(this.eventServiceConfiguration.getUpdateConflictRetries()).request();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 118 */         bulkUpdateBuilder.add(request);
/*     */       }
/*     */       
/* 121 */       BulkResponse bulkResponse = (BulkResponse)bulkUpdateBuilder.execute().actionGet();
/* 122 */       if (bulkResponse.hasFailures()) {
/* 123 */         throw new RuntimeException(String.format("The following error occurred while updating the [%s] index with hidden fields [%s]", new Object[] { "event_type_metadata_hidden_fields", bulkResponse.buildFailureMessage() }));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */       ESUtils.refreshIndices(this.clientProvider.getAdminClient(), new String[] { "event_type_metadata_hidden_fields" });
/*     */     } catch (Exception e) {
/* 132 */       throw ElasticSearchServiceHelper.propagateAsException(e, "Failed to hide fields [{}] for account [{}] eventType [{}]", new Object[] { Joiner.on(",").join(hiddenFields), accountName, eventType });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HiddenField> getHiddenFields(String accountName, String eventType)
/*     */   {
/* 140 */     accountName = AccountConfiguration.normalizeAccountName(accountName);
/* 141 */     eventType = DefaultIndexNameResolver.validateAndResolveEventType(eventType);
/* 142 */     this.eventTypeMetaDataService.verifyEventType(accountName, eventType);
/*     */     
/* 144 */     String key = buildAccountWithEventCacheKey(accountName, eventType);
/* 145 */     List<HiddenField> hiddenFields = (List)this.eventTypeMetaDataHiddenFieldsCache.getIfPresent(key);
/* 146 */     if (hiddenFields == null) {
/* 147 */       hiddenFields = getEventTypeMetaDataHiddenFieldsNoCache(accountName, eventType);
/* 148 */       this.eventTypeMetaDataHiddenFieldsCache.put(key, hiddenFields);
/*     */     }
/*     */     
/* 151 */     return hiddenFields;
/*     */   }
/*     */   
/*     */ 
/*     */   public void unhideField(String accountName, String eventType, String fieldName)
/*     */   {
/* 157 */     accountName = AccountConfiguration.normalizeAccountName(accountName);
/* 158 */     eventType = DefaultIndexNameResolver.validateAndResolveEventType(eventType);
/* 159 */     this.eventTypeMetaDataService.verifyEventType(accountName, eventType);
/*     */     try
/*     */     {
/* 162 */       Client client = this.clientProvider.getAdminClient();
/* 163 */       String id = createHiddenFieldId(accountName, eventType, fieldName);
/*     */       
/* 165 */       DeleteResponse response = (DeleteResponse)client.prepareDelete("event_type_metadata_hidden_fields", "event_type_metadata_hidden_fields", id).execute().actionGet();
/*     */       
/* 167 */       if (!response.isFound()) {
/* 168 */         throw new HiddenFieldNotExistsException(accountName, eventType, fieldName);
/*     */       }
/*     */       
/* 171 */       invalidateEventTypeMetaDataHiddenFieldsCache(accountName, eventType);
/*     */       
/*     */ 
/*     */ 
/* 175 */       ESUtils.refreshIndices(this.clientProvider.getAdminClient(), new String[] { "event_type_metadata_hidden_fields" });
/*     */     } catch (Exception e) {
/* 177 */       throw ElasticSearchServiceHelper.propagateAsException(e, "Failed to hide field [{}] for account [{}] eventType [{}]", new Object[] { fieldName, accountName, eventType });
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
/*     */ 
/*     */   public void pruneHiddenFieldsFromMapping(String accountName, String eventType, ObjectNode mapping)
/*     */   {
/* 203 */     for (HiddenField hiddenField : getHiddenFields(accountName, eventType)) {
/* 204 */       String hiddenFieldName = hiddenField.getFieldName();
/* 205 */       String[] pathComponentsSplit = hiddenFieldName.split("\\.");
/*     */       
/*     */ 
/*     */ 
/* 209 */       pathComponents = new ArrayList();
/* 210 */       for (String pathComponent : pathComponentsSplit) {
/* 211 */         pathComponents.add("properties");
/* 212 */         pathComponents.add(pathComponent);
/*     */       }
/*     */       
/* 215 */       node = mapping;
/* 216 */       for (String pathComponent : pathComponents) {
/* 217 */         if (!node.has(pathComponent)) {
/*     */           break;
/*     */         }
/*     */         
/* 221 */         if (pathComponents.indexOf(pathComponent) == pathComponents.size() - 1)
/*     */         {
/* 223 */           node.remove(pathComponent);
/*     */         } else
/* 225 */           node = (ObjectNode)node.get(pathComponent);
/*     */       }
/*     */     }
/*     */     List<String> pathComponents;
/*     */     ObjectNode node;
/*     */   }
/*     */   
/* 232 */   private List<HiddenField> getEventTypeMetaDataHiddenFieldsNoCache(String accountName, String eventType) { List<HiddenField> hiddenFields = new ArrayList();
/*     */     try {
/* 234 */       Client client = this.clientProvider.getAdminClient();
/* 235 */       SearchResponse response = (SearchResponse)client.prepareSearch(new String[] { "event_type_metadata_hidden_fields" }).setTypes(new String[] { "event_type_metadata_hidden_fields" }).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.boolFilter().must(new FilterBuilder[] { FilterBuilders.termFilter("accountName", accountName), FilterBuilders.termFilter("eventType", eventType) }))).execute().actionGet();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 243 */       for (SearchHit hit : response.getHits().getHits()) {
/* 244 */         String mapping = hit.getSourceAsString();
/* 245 */         hiddenFields.add((HiddenField)Reader.DEFAULT_JSON_MAPPER.reader(HiddenField.class).readValue(mapping));
/*     */       }
/*     */     } catch (IndexMissingException e) {
/* 248 */       return null;
/*     */     } catch (Exception e) {
/* 250 */       throw ElasticSearchServiceHelper.propagateAsException(e);
/*     */     }
/* 252 */     return hiddenFields;
/*     */   }
/*     */   
/*     */   private String createHiddenFieldId(String accountName, String eventType, String fieldName) {
/* 256 */     return accountName + "-" + eventType + "-" + fieldName;
/*     */   }
/*     */   
/*     */   private void invalidateEventTypeMetaDataHiddenFieldsCache(String accountName, String eventType) {
/* 260 */     String key = buildAccountWithEventCacheKey(accountName, eventType);
/* 261 */     this.eventTypeMetaDataHiddenFieldsCache.invalidate(key);
/*     */   }
/*     */   
/*     */   private String buildAccountWithEventCacheKey(String accountName, String eventType) {
/* 265 */     eventType = DefaultIndexNameResolver.validateAndResolveEventType(eventType);
/* 266 */     accountName = AccountConfiguration.normalizeAccountName(accountName);
/* 267 */     return accountName + "___" + eventType;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/hiddenfields/ElasticSearchHiddenFieldsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */