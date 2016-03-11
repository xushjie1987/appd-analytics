/*     */ package com.appdynamics.analytics.processor.migration.elasticsearch;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.MetaDataIndexConfiguration;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpEntityEnclosingRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpRequestFactory;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.elasticsearch.action.ActionFuture;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
/*     */ import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
/*     */ import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.search.SearchScrollRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchType;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.index.query.QueryBuilder;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.elasticsearch.search.SearchHits;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ElasticSearchHelper
/*     */ {
/*  41 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchHelper.class);
/*     */   
/*  43 */   private static TimeValue ES_CALL_TIMEOUT = new TimeValue(120L, TimeUnit.SECONDS);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void createIndex(IndicesAdminClient adminClient, String indexName, MetaDataIndexConfiguration indexConfig)
/*     */   {
/*  50 */     log.info("Attempting to create index [" + indexName + "]");
/*     */     
/*  52 */     CreateIndexRequestBuilder builder = new CreateIndexRequestBuilder(adminClient).setIndex(indexName);
/*     */     
/*     */ 
/*  55 */     Map<String, Object> settings = indexConfig.getSettings();
/*  56 */     if (settings != null) {
/*  57 */       builder.setSettings(settings);
/*     */     }
/*     */     
/*  60 */     Map<String, Map<String, Object>> mappings = indexConfig.getMappings();
/*  61 */     if (mappings != null) {
/*  62 */       for (Map.Entry<String, Map<String, Object>> entry : mappings.entrySet()) {
/*  63 */         builder.addMapping((String)entry.getKey(), (Map)entry.getValue());
/*     */       }
/*     */     }
/*     */     
/*  67 */     CreateIndexResponse response = (CreateIndexResponse)builder.get(TimeValue.timeValueMinutes(1L));
/*  68 */     if (!response.isAcknowledged()) {
/*  69 */       throw new RuntimeException("Index [" + indexName + "] could not be created. Reason unknown");
/*     */     }
/*     */     
/*  72 */     log.info("Index [" + indexName + "] created");
/*     */   }
/*     */   
/*     */   public static boolean updateIndexSettings(IndicesAdminClient client, String index, Map<String, String> settings) {
/*  76 */     log.info("Attempting to update index [{}] settings {}", index, settings);
/*     */     
/*  78 */     UpdateSettingsRequest request = new UpdateSettingsRequest(new String[0]).indices(new String[] { index }).settings(settings);
/*     */     
/*     */ 
/*  81 */     UpdateSettingsResponse response = (UpdateSettingsResponse)client.updateSettings(request).actionGet();
/*     */     
/*  83 */     log.info("Index [{}] settings were {}updated", index, response.isAcknowledged() ? "" : "not ");
/*  84 */     return response.isAcknowledged();
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
/*     */   public static void fetchResults(Client client, QueryBuilder queryBuilder, TimeValue timeout, int fetchSize, int retries, Listener<SearchHit[]> batchListener, String... sourceIndexNames)
/*     */   {
/* 102 */     fetchResults(client, queryBuilder, timeout, fetchSize, retries, batchListener, sourceIndexNames, null, ES_CALL_TIMEOUT);
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
/*     */   public static void fetchResults(final Client client, final QueryBuilder queryBuilder, final TimeValue timeout, final int fetchSize, int retries, final Listener<SearchHit[]> batchListener, String[] sourceIndexNames, final String[] sourceTypeNames, final TimeValue esCallTimeout)
/*     */   {
/* 123 */     fetchResults(new FetchRequestHandler()
/*     */     {
/*     */       SearchResponse scrollResponse;
/*     */       SearchHit[] hits;
/*     */       
/*     */       public String[] indices() {
/* 129 */         return this.val$sourceIndexNames;
/*     */       }
/*     */       
/*     */       public void startScroll()
/*     */       {
/* 134 */         SearchRequestBuilder builder = client.prepareSearch(this.val$sourceIndexNames);
/* 135 */         builder.setSearchType(SearchType.SCAN).setScroll(timeout).setQuery(queryBuilder).setSize(fetchSize);
/*     */         
/*     */ 
/*     */ 
/* 139 */         if (sourceTypeNames != null) {
/* 140 */           builder.setTypes(sourceTypeNames);
/*     */         }
/*     */         
/* 143 */         this.scrollResponse = ((SearchResponse)builder.setTimeout(esCallTimeout).execute().actionGet(esCallTimeout));
/*     */       }
/*     */       
/*     */       public boolean hasErrors(int retries, int attempt)
/*     */       {
/* 148 */         if (this.scrollResponse.isTimedOut()) {
/* 149 */           ElasticSearchHelper.log.warn("Fetch attempt failed due to timeout. Retry attempts remaining [{}]", Integer.valueOf(retries - attempt));
/* 150 */           return true;
/*     */         }
/* 152 */         if ((this.scrollResponse.getShardFailures() != null) && (this.scrollResponse.getShardFailures().length > 0)) {
/* 153 */           ElasticSearchHelper.log.warn("Fetch attempt failed due to failure {}. Retry attempts remaining [{}]", Arrays.asList(this.scrollResponse.getShardFailures()), Integer.valueOf(retries - attempt));
/*     */           
/* 155 */           return true;
/*     */         }
/* 157 */         return false;
/*     */       }
/*     */       
/*     */       public long totalHits()
/*     */       {
/* 162 */         return this.scrollResponse.getHits().totalHits();
/*     */       }
/*     */       
/*     */       public void next()
/*     */       {
/* 167 */         this.scrollResponse = ((SearchResponse)client.prepareSearchScroll(this.scrollResponse.getScrollId()).setScroll(timeout).execute().actionGet(esCallTimeout));
/*     */       }
/*     */       
/*     */ 
/*     */       private void guardHits()
/*     */       {
/* 173 */         if ((this.scrollResponse != null) && (this.scrollResponse.getHits() != null)) {
/* 174 */           this.hits = this.scrollResponse.getHits().hits();
/*     */         }
/*     */       }
/*     */       
/*     */       public int batchSize()
/*     */       {
/* 180 */         guardHits();
/* 181 */         return this.hits.length;
/*     */       }
/*     */       
/*     */       public void onStart()
/*     */       {
/* 186 */         batchListener.onStart();
/*     */       }
/*     */       
/*     */       public ElasticSearchHelper.ListenerResponse onBatch()
/*     */       {
/* 191 */         guardHits();
/* 192 */         return batchListener.onBatch(this.hits);
/*     */       }
/*     */       
/*     */       public void onEnd()
/*     */       {
/* 197 */         batchListener.onEnd();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 202 */       public boolean handleRetryFailure() { return batchListener.handleRetryFailure(); } }, retries);
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
/*     */   public static void fetchResults(final HttpRequestFactory requestFactory, final String query, final TimeValue timeout, final int fetchSize, int retries, final Listener<ArrayNode> batchListener, String index, final String docType)
/*     */     throws IOException
/*     */   {
/* 224 */     fetchResults(new FetchRequestHandler()
/*     */     {
/*     */       JsonNode scrollResponse;
/*     */       ArrayNode hits;
/*     */       
/*     */       public String[] indices() {
/* 230 */         return new String[] { this.val$index };
/*     */       }
/*     */       
/*     */       public void startScroll()
/*     */       {
/* 235 */         this.scrollResponse = ((JsonNode)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)requestFactory.post().appendPath("/" + this.val$index + "/" + docType + "/_search", new String[0])).addQueryParam("search_type", "scan")).addQueryParam("scroll", "1m")).addQueryParam("timeout", String.valueOf(timeout))).setRequestEntity("{\"query\":{" + (Strings.isNullOrEmpty(query) ? "\"match_all\":{}" : query) + "}," + "\"size\":" + fetchSize + "}").execute(JsonNode.class));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public boolean hasErrors(int retries, int attempt)
/*     */       {
/* 248 */         if ((this.scrollResponse.get("timed_out") != null) && (Boolean.parseBoolean(this.scrollResponse.get("timed_out").toString())))
/*     */         {
/* 250 */           ElasticSearchHelper.log.warn("Fetch attempt failed due to timeout. Retry attempts remaining [{}]", Integer.valueOf(retries - attempt));
/* 251 */           return true;
/*     */         }
/* 253 */         if (this.scrollResponse.get("error") != null) {
/* 254 */           ElasticSearchHelper.log.warn("Fetch attempt failed due to failure {}. Retry attempts remaining [{}]", this.scrollResponse.get("error").toString(), Integer.valueOf(retries - attempt));
/*     */           
/* 256 */           return true;
/*     */         }
/* 258 */         return false;
/*     */       }
/*     */       
/*     */       public long totalHits()
/*     */       {
/* 263 */         return Long.parseLong(this.scrollResponse.get("hits").get("total").asText());
/*     */       }
/*     */       
/*     */       public void next()
/*     */       {
/* 268 */         String scrollId = ((JsonNode)Preconditions.checkNotNull(this.scrollResponse.get("_scroll_id"), "Expected to find _scroll_id in scroll response: " + this.scrollResponse.toString())).asText();
/*     */         
/* 270 */         this.scrollResponse = ((JsonNode)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)requestFactory.post().appendPath("/_search/scroll", new String[0])).addQueryParam("scroll", "1m")).addQueryParam("timeout", String.valueOf(timeout))).setRequestEntity(scrollId).execute(JsonNode.class));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public int batchSize()
/*     */       {
/* 279 */         guardHits();
/* 280 */         return this.hits == null ? 0 : this.hits.size();
/*     */       }
/*     */       
/*     */       private void guardHits() {
/* 284 */         if ((this.scrollResponse != null) && (this.scrollResponse.get("hits") != null)) {
/* 285 */           this.hits = ((ArrayNode)this.scrollResponse.get("hits").get("hits"));
/*     */         }
/*     */       }
/*     */       
/*     */       public void onStart()
/*     */       {
/* 291 */         batchListener.onStart();
/*     */       }
/*     */       
/*     */       public ElasticSearchHelper.ListenerResponse onBatch()
/*     */       {
/* 296 */         guardHits();
/* 297 */         return batchListener.onBatch(this.hits);
/*     */       }
/*     */       
/*     */       public void onEnd()
/*     */       {
/* 302 */         batchListener.onEnd();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 307 */       public boolean handleRetryFailure() { return batchListener.handleRetryFailure(); } }, retries);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void fetchResults(FetchRequestHandler requestHandler, int retries)
/*     */   {
/*     */     label318:
/*     */     
/*     */ 
/*     */ 
/* 320 */     for (int attempt = 1; 
/*     */         
/* 322 */         attempt <= retries; attempt++) {
/* 323 */       log.info("Attempting to fetch results from indices [{}]. Attempt [{}]", Joiner.on(",").join(requestHandler.indices()), Integer.valueOf(attempt));
/*     */       
/* 325 */       requestHandler.startScroll();
/* 326 */       if (!requestHandler.hasErrors(retries, attempt))
/*     */       {
/*     */ 
/*     */ 
/* 330 */         long total = requestHandler.totalHits();
/* 331 */         log.info("Scan query found [{}] results", Long.valueOf(total));
/* 332 */         if (total == 0L) {
/*     */           break;
/*     */         }
/*     */         
/* 336 */         log.info("Results will be fetched next");
/* 337 */         requestHandler.onStart();
/*     */         try {
/* 339 */           int batch = 1;
/*     */           for (;;)
/*     */           {
/* 342 */             requestHandler.next();
/* 343 */             if (requestHandler.hasErrors(retries, attempt)) {
/* 344 */               log.error("The current batch will stop and the entire fetch process will be retried if possible");
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
/*     */ 
/* 367 */               requestHandler.onEnd();
/*     */               break label318;
/*     */             }
/* 348 */             if (requestHandler.batchSize() == 0) {
/*     */               break;
/*     */             }
/* 351 */             log.info("Batch [{}] found [{}] results", Integer.valueOf(batch), Integer.valueOf(requestHandler.batchSize()));
/* 352 */             ListenerResponse response = requestHandler.onBatch();
/* 353 */             if (response == ListenerResponse.STOP) {
/* 354 */               log.info("Stop has been requested");
/* 355 */               break; }
/* 356 */             if (response == ListenerResponse.RESTART) {
/* 357 */               log.warn("Result listener has requested that the operation be restarted if possible");
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 367 */               requestHandler.onEnd();
/*     */               break label318;
/*     */             }
/* 359 */             if (response == ListenerResponse.CONTINUE) {
/* 360 */               log.trace("Proceeding to fetch more results");
/*     */             } else {
/* 362 */               throw new IllegalArgumentException("Unrecognized response [" + response.name() + "]");
/*     */             }
/* 364 */             batch++;
/*     */           }
/*     */         } finally {
/* 367 */           requestHandler.onEnd();
/*     */         }
/*     */         
/*     */ 
/* 371 */         break;
/*     */       }
/*     */     }
/* 374 */     if ((attempt == retries) && 
/* 375 */       (!requestHandler.handleRetryFailure())) {
/* 376 */       throw new RuntimeException("Result fetch failed due to errors. Retry attempts were exhausted");
/*     */     }
/*     */     
/* 379 */     log.info("Completed fetching results");
/*     */   }
/*     */   
/*     */ 
/*     */   private static abstract interface FetchRequestHandler
/*     */   {
/*     */     public abstract String[] indices();
/*     */     
/*     */ 
/*     */     public abstract void startScroll();
/*     */     
/*     */ 
/*     */     public abstract boolean hasErrors(int paramInt1, int paramInt2);
/*     */     
/*     */ 
/*     */     public abstract long totalHits();
/*     */     
/*     */ 
/*     */     public abstract void next();
/*     */     
/*     */     public abstract int batchSize();
/*     */     
/*     */     public abstract void onStart();
/*     */     
/*     */     public abstract ElasticSearchHelper.ListenerResponse onBatch();
/*     */     
/*     */     public abstract void onEnd();
/*     */     
/*     */     public abstract boolean handleRetryFailure();
/*     */   }
/*     */   
/*     */   public static abstract interface Listener<T>
/*     */   {
/*     */     public abstract void onStart();
/*     */     
/*     */     public abstract ElasticSearchHelper.ListenerResponse onBatch(T paramT);
/*     */     
/*     */     public abstract void onEnd();
/*     */     
/*     */     public abstract boolean handleRetryFailure();
/*     */   }
/*     */   
/*     */   public static abstract class BaseListener<T>
/*     */     implements ElasticSearchHelper.Listener<T>
/*     */   {
/*     */     public void onStart() {}
/*     */     
/*     */     public void onEnd() {}
/*     */     
/*     */     public boolean handleRetryFailure()
/*     */     {
/* 430 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum ListenerResponse
/*     */   {
/* 436 */     CONTINUE, 
/* 437 */     RESTART, 
/* 438 */     STOP;
/*     */     
/*     */     private ListenerResponse() {}
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/migration/elasticsearch/ElasticSearchHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */