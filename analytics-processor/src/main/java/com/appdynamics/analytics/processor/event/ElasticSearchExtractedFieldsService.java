/*     */ package com.appdynamics.analytics.processor.event;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.EsCallTimeout;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.exception.ExtractedFieldAlreadyExistsException;
/*     */ import com.appdynamics.analytics.processor.event.exception.ExtractedFieldMissingException;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectReader;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.elasticsearch.ElasticsearchException;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
/*     */ import org.elasticsearch.action.index.IndexRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.update.UpdateRequestBuilder;
/*     */ import org.elasticsearch.action.update.UpdateResponse;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.index.query.BoolFilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilders;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.elasticsearch.search.SearchHits;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ @Singleton
/*     */ public class ElasticSearchExtractedFieldsService implements ExtractedFieldsService
/*     */ {
/*  36 */   private static final Logger log = org.slf4j.LoggerFactory.getLogger(ElasticSearchExtractedFieldsService.class);
/*     */   
/*     */   public static final String EXTRACTED_FIELDS_INDEX = "event_type_extracted_fields";
/*     */   
/*     */   public static final String EXTRACTED_FIELDS_DOC_TYPE = "event_type_extracted_fields";
/*     */   
/*     */   public static final String ACCOUNT_NAME = "accountName";
/*     */   public static final String EVENT_TYPE = "eventType";
/*     */   public static final String SOURCE_TYPE = "sourceType";
/*     */   public static final String NAME = "name";
/*     */   public static final String SEPERATOR = "_";
/*     */   final ClientProvider clientProvider;
/*     */   final TimeValue callTimeout;
/*     */   
/*     */   @Inject
/*     */   public ElasticSearchExtractedFieldsService(ClientProvider clientProvider, @EsCallTimeout TimeValue callTimeout)
/*     */   {
/*  53 */     this.clientProvider = clientProvider;
/*  54 */     this.callTimeout = callTimeout;
/*     */   }
/*     */   
/*     */   public List<ExtractedFieldDefinition> getExtractedFields(String accountName, String eventType)
/*     */   {
/*  59 */     return getExtractedFields(accountName, eventType, null);
/*     */   }
/*     */   
/*     */   public List<ExtractedFieldDefinition> getExtractedFields(String accountName, String eventType, List<String> sourceTypes)
/*     */   {
/*     */     try
/*     */     {
/*  66 */       List<ExtractedFieldDefinition> fieldDefs = new ArrayList();
/*     */       
/*  68 */       BoolFilterBuilder boolFilter = FilterBuilders.boolFilter().must(new FilterBuilder[] { FilterBuilders.termFilter("accountName", accountName), FilterBuilders.termFilter("eventType", eventType) });
/*     */       
/*  70 */       if (sourceTypes != null) {
/*  71 */         for (String sourceType : sourceTypes) {
/*  72 */           boolFilter.should(FilterBuilders.termFilter("sourceType", sourceType));
/*     */         }
/*     */       }
/*     */       
/*  76 */       SearchResponse response = (SearchResponse)this.clientProvider.getAdminClient().prepareSearch(new String[] { "event_type_extracted_fields" }).setTypes(new String[] { "event_type_extracted_fields" }).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), boolFilter)).execute().actionGet();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  81 */       if (response.getHits().totalHits() > 0L) {
/*  82 */         for (SearchHit hit : response.getHits().getHits()) {
/*  83 */           ExtractedFieldDefinition fieldDef = (ExtractedFieldDefinition)Reader.DEFAULT_JSON_MAPPER.reader(ExtractedFieldDefinition.class).readValue(hit.getSourceAsString());
/*     */           
/*  85 */           fieldDefs.add(fieldDef);
/*     */         }
/*     */       }
/*     */       
/*  89 */       return fieldDefs;
/*     */     } catch (Exception e) {
/*  91 */       throw propagateAsException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ExtractedFieldDefinition getExtractedField(String accountName, String eventType, String name)
/*     */   {
/*  98 */     return getExtractedField(accountName, eventType, null, name);
/*     */   }
/*     */   
/*     */   ExtractedFieldDefinition getExtractedField(ExtractedFieldDefinition fieldDef) {
/* 102 */     return getExtractedField(fieldDef.getAccountName(), fieldDef.getEventType(), fieldDef.getSourceType(), fieldDef.getName());
/*     */   }
/*     */   
/*     */   public ExtractedFieldDefinition getExtractedField(String accountName, String eventType, String sourceType, String name)
/*     */   {
/*     */     try
/*     */     {
/* 109 */       BoolFilterBuilder mustFilter = FilterBuilders.boolFilter().must(new FilterBuilder[] { FilterBuilders.termFilter("accountName", accountName), FilterBuilders.termFilter("eventType", eventType), FilterBuilders.termFilter("name", name) });
/*     */       
/*     */ 
/*     */ 
/* 113 */       if (sourceType != null) {
/* 114 */         mustFilter.must(FilterBuilders.termFilter("sourceType", sourceType));
/*     */       }
/*     */       
/* 117 */       SearchResponse response = (SearchResponse)this.clientProvider.getAdminClient().prepareSearch(new String[] { "event_type_extracted_fields" }).setTypes(new String[] { "event_type_extracted_fields" }).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), mustFilter)).execute().actionGet(this.callTimeout);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 122 */       if (response.getHits().totalHits() < 1L)
/* 123 */         return null;
/* 124 */       if (response.getHits().totalHits() > 1L) {
/* 125 */         log.warn("Found multiple extracted field definitions for account [{}] event [{}] with name [{}]", new Object[] { accountName, eventType, name });
/*     */       }
/*     */       
/*     */ 
/* 129 */       String source = response.getHits().getHits()[0].getSourceAsString();
/* 130 */       return (ExtractedFieldDefinition)Reader.DEFAULT_JSON_MAPPER.reader(ExtractedFieldDefinition.class).readValue(source);
/*     */     } catch (Exception e) {
/* 132 */       throw propagateAsException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void createExtractedField(ExtractedFieldDefinition extractedFieldDefinition)
/*     */   {
/*     */     try {
/* 139 */       String id = getExtractedFieldsId(extractedFieldDefinition);
/* 140 */       if (getExtractedField(extractedFieldDefinition) != null) {
/* 141 */         throw new ExtractedFieldAlreadyExistsException(extractedFieldDefinition);
/*     */       }
/*     */       
/* 144 */       String payload = Reader.DEFAULT_JSON_MAPPER.writeValueAsString(extractedFieldDefinition);
/* 145 */       this.clientProvider.getAdminClient().prepareIndex("event_type_extracted_fields", "event_type_extracted_fields", id).setSource(payload).get(this.callTimeout);
/*     */       
/*     */ 
/*     */ 
/* 149 */       ESUtils.refreshIndices(this.clientProvider.getAdminClient(), new String[] { "event_type_extracted_fields" });
/*     */     } catch (ExtractedFieldAlreadyExistsException e) {
/* 151 */       throw e;
/*     */     } catch (Exception e) {
/* 153 */       throw propagateAsException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateExtractedField(ExtractedFieldDefinition extractedFieldDefinition)
/*     */   {
/*     */     try {
/* 160 */       String id = getExtractedFieldsId(extractedFieldDefinition);
/* 161 */       if (getExtractedField(extractedFieldDefinition) == null) {
/* 162 */         throw new ExtractedFieldMissingException(extractedFieldDefinition);
/*     */       }
/*     */       
/* 165 */       String payload = Reader.DEFAULT_JSON_MAPPER.writeValueAsString(extractedFieldDefinition);
/* 166 */       ((UpdateResponse)this.clientProvider.getAdminClient().prepareUpdate("event_type_extracted_fields", "event_type_extracted_fields", id).setDoc(payload).get(this.callTimeout)).isCreated();
/*     */       
/*     */ 
/*     */ 
/* 170 */       ESUtils.refreshIndices(this.clientProvider.getAdminClient(), new String[] { "event_type_extracted_fields" });
/*     */     } catch (Exception e) {
/* 172 */       throw propagateAsException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteExtractedField(String accountName, String eventType, String name)
/*     */   {
/*     */     try {
/* 179 */       if (getExtractedField(accountName, eventType, name) == null) {
/* 180 */         throw new ExtractedFieldMissingException(accountName, eventType, name);
/*     */       }
/*     */       
/* 183 */       this.clientProvider.getAdminClient().prepareDeleteByQuery(new String[] { "event_type_extracted_fields" }).setTypes(new String[] { "event_type_extracted_fields" }).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.boolFilter().must(new FilterBuilder[] { FilterBuilders.termFilter("accountName", accountName), FilterBuilders.termFilter("eventType", eventType), FilterBuilders.termFilter("name", name) }))).execute().actionGet(this.callTimeout);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */       ESUtils.refreshIndices(this.clientProvider.getAdminClient(), new String[] { "event_type_extracted_fields" });
/*     */     } catch (Exception e) {
/* 194 */       throw propagateAsException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   String getExtractedFieldsId(ExtractedFieldDefinition extractedFieldDefinition) {
/* 199 */     return getExtractedFieldsId(extractedFieldDefinition.getAccountName(), extractedFieldDefinition.getEventType(), extractedFieldDefinition.getSourceType(), extractedFieldDefinition.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   String getExtractedFieldsId(String accountName, String eventType, String sourceType, String name)
/*     */   {
/* 206 */     return accountName + "_" + eventType + "_" + sourceType + "_" + name;
/*     */   }
/*     */   
/*     */   RuntimeException propagateAsException(Exception e) {
/* 210 */     if ((e instanceof ElasticsearchException)) {
/* 211 */       return com.appdynamics.analytics.processor.elasticsearch.exception.ElasticSearchExceptionUtils.propagateAsEventException((ElasticsearchException)e);
/*     */     }
/* 213 */     return com.google.common.base.Throwables.propagate(e);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ElasticSearchExtractedFieldsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */