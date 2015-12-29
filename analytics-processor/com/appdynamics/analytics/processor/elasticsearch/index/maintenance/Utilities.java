/*     */ package com.appdynamics.analytics.processor.elasticsearch.index.maintenance;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper;
/*     */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper.Listener;
/*     */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper.ListenerResponse;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
/*     */ import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
/*     */ import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
/*     */ import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
/*     */ import org.elasticsearch.action.bulk.BulkRequestBuilder;
/*     */ import org.elasticsearch.action.bulk.BulkResponse;
/*     */ import org.elasticsearch.action.index.IndexRequest;
/*     */ import org.elasticsearch.action.index.IndexRequestBuilder;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.cluster.metadata.MappingMetaData;
/*     */ import org.elasticsearch.common.collect.ImmutableOpenMap;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.index.Index;
/*     */ import org.elasticsearch.index.query.QueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.indices.IndexMissingException;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class Utilities
/*     */ {
/*  44 */   private static final Logger log = LoggerFactory.getLogger(Utilities.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int DEFAULT_TIMEOUT_MILLIS = 30000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int DEFAULT_FETCH_SIZE = 500;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int DEFAULT_RETRIES = 5;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int WAIT_BEFORE_TERMINATE_SECS = 30;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean copy(Client sourceClusterClient, String sourceIndex, String sourceType, String destinationIndex, String destinationType)
/*     */     throws IndexMissingException
/*     */   {
/*  71 */     return copy(sourceClusterClient, sourceClusterClient, sourceIndex, sourceType, destinationIndex, destinationType, QueryBuilders.matchAllQuery(), Optional.absent(), 30000L, 500, null);
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
/*     */   public static boolean copy(Client sourceClusterClient, String sourceIndex, String sourceType, String destinationIndex, String destinationType, HitTransformer transformer)
/*     */     throws IndexMissingException
/*     */   {
/*  94 */     return copy(sourceClusterClient, sourceClusterClient, sourceIndex, sourceType, destinationIndex, destinationType, QueryBuilders.matchAllQuery(), Optional.of(transformer), 30000L, 500, null);
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
/*     */   public static boolean copy(Client sourceClusterClient, String sourceIndex, String sourceType, String destinationIndex, String destinationType, QueryBuilder queryBuilder)
/*     */     throws IndexMissingException
/*     */   {
/* 117 */     return copy(sourceClusterClient, sourceClusterClient, sourceIndex, sourceType, destinationIndex, destinationType, queryBuilder, Optional.absent(), 30000L, 500, null);
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
/*     */   public static boolean copy(Client sourceClusterClient, String sourceIndex, String sourceType, String destinationIndex, String destinationType, QueryBuilder queryBuilder, Optional<HitTransformer> transformer)
/*     */     throws IndexMissingException
/*     */   {
/* 142 */     return copy(sourceClusterClient, sourceClusterClient, sourceIndex, sourceType, destinationIndex, destinationType, queryBuilder, transformer, 30000L, 500, null);
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
/*     */   public static boolean copy(Client sourceClusterClient, Client destinationClusterClient, String sourceIndex, String sourceType, String destinationIndex, String destinationType)
/*     */     throws IndexMissingException
/*     */   {
/* 167 */     return copy(sourceClusterClient, destinationClusterClient, sourceIndex, sourceType, destinationIndex, destinationType, QueryBuilders.matchAllQuery(), Optional.absent(), 30000L, 500, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean copy(Client sourceClusterClient, Client destinationClusterClient, String sourceIndex, String sourceType, String destinationIndex, String destinationType, QueryBuilder queryBuilder, Optional<HitTransformer> transformer, long scrollTimeToLiveMillis, int scrollBatchSize, TimeValue esCallTimeout)
/*     */     throws IndexMissingException
/*     */   {
/* 199 */     if (esCallTimeout == null) {
/* 200 */       esCallTimeout = new TimeValue(120L, TimeUnit.SECONDS);
/*     */     }
/* 202 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(sourceType), "Must specify both source index AND type");
/* 203 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(destinationType), "Must specify both destination index AND type");
/*     */     
/* 205 */     verifyIndexExists(sourceClusterClient, sourceIndex, esCallTimeout);
/* 206 */     verifyIndexExists(destinationClusterClient, destinationIndex, esCallTimeout);
/* 207 */     ensureTypeInDestination(sourceClusterClient, destinationClusterClient, sourceIndex, sourceType, destinationIndex, destinationType, esCallTimeout);
/*     */     try
/*     */     {
/* 210 */       ElasticSearchHelper.fetchResults(sourceClusterClient, queryBuilder, new TimeValue(scrollTimeToLiveMillis, TimeUnit.MILLISECONDS), scrollBatchSize, 5, new CopyListener(destinationClusterClient, destinationIndex, destinationType, transformer, esCallTimeout, null), new String[] { sourceIndex }, new String[] { sourceType }, esCallTimeout);
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/* 216 */       throw Throwables.propagate(e);
/*     */     }
/* 218 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private static void ensureTypeInDestination(Client sourceClusterClient, Client destinationClusterClient, String sourceIndex, String sourceType, String destinationIndex, String destinationType, TimeValue esCallTimeout)
/*     */   {
/* 224 */     GetMappingsResponse getMappingsResponse = (GetMappingsResponse)((GetMappingsRequestBuilder)((GetMappingsRequestBuilder)sourceClusterClient.admin().indices().prepareGetMappings(new String[] { sourceIndex }).setTypes(new String[] { sourceType })).setMasterNodeTimeout(esCallTimeout)).execute().actionGet(esCallTimeout);
/*     */     
/*     */ 
/* 227 */     ensureTypeInDestination(destinationClusterClient, destinationIndex, destinationType, (MappingMetaData)((ImmutableOpenMap)getMappingsResponse.mappings().get(sourceIndex)).get(sourceType), esCallTimeout);
/*     */   }
/*     */   
/*     */   private static void ensureTypeInDestination(Client destinationClusterClient, String destinationIndex, String destinationType, MappingMetaData mappingMetaData, TimeValue esCallTimeout)
/*     */   {
/*     */     PutMappingResponse putMappingResponse;
/*     */     try
/*     */     {
/* 235 */       putMappingResponse = (PutMappingResponse)((PutMappingRequestBuilder)((PutMappingRequestBuilder)destinationClusterClient.admin().indices().preparePutMapping(new String[] { destinationIndex }).setType(destinationType).setSource(Reader.DEFAULT_JSON_MAPPER.writeValueAsString(mappingMetaData.sourceAsMap())).setMasterNodeTimeout(esCallTimeout)).setTimeout(esCallTimeout)).execute().actionGet(esCallTimeout);
/*     */ 
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 240 */       throw Throwables.propagate(e);
/*     */     }
/* 242 */     if (!putMappingResponse.isAcknowledged()) {
/* 243 */       throw new IllegalStateException("Unable to create new mappings in index [" + destinationIndex + "] for type [" + destinationType + "]: " + putMappingResponse);
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
/*     */   public static boolean move(Client clusterClient, String sourceIndex, String destinationIndex, TimeValue esCallTimeout)
/*     */     throws IndexMissingException
/*     */   {
/* 266 */     return move(clusterClient, clusterClient, sourceIndex, destinationIndex, Optional.absent(), esCallTimeout);
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
/*     */   public static boolean move(Client clusterClient, String sourceIndex, String destinationIndex, Optional<HitTransformer> transformer, TimeValue esCallTimeout)
/*     */     throws IndexMissingException
/*     */   {
/* 288 */     return move(clusterClient, clusterClient, sourceIndex, destinationIndex, transformer, esCallTimeout);
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
/*     */   public static boolean move(Client sourceClusterClient, Client destinationClusterClient, String sourceIndex, String destinationIndex, Optional<HitTransformer> transformer, TimeValue esCallTimeout)
/*     */     throws IndexMissingException
/*     */   {
/* 312 */     GetMappingsResponse getMappingsResponse = (GetMappingsResponse)((GetMappingsRequestBuilder)sourceClusterClient.admin().indices().prepareGetMappings(new String[] { sourceIndex }).setMasterNodeTimeout(esCallTimeout)).execute().actionGet(esCallTimeout);
/*     */     
/*     */ 
/* 315 */     ImmutableOpenMap<String, MappingMetaData> mappingMetaData = (ImmutableOpenMap)getMappingsResponse.mappings().get(sourceIndex);
/* 316 */     for (Iterator<String> keyIter = mappingMetaData.keysIt(); keyIter.hasNext();) {
/* 317 */       String type = (String)keyIter.next();
/* 318 */       ensureTypeInDestination(destinationClusterClient, destinationIndex, type, (MappingMetaData)mappingMetaData.get(type), esCallTimeout);
/*     */       
/* 320 */       if (!copy(sourceClusterClient, destinationClusterClient, sourceIndex, type, destinationIndex, type, QueryBuilders.matchAllQuery(), transformer, 30000L, 500, null))
/*     */       {
/* 322 */         return false;
/*     */       }
/*     */     }
/* 325 */     return delete(sourceClusterClient, sourceIndex, esCallTimeout);
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
/*     */   public static boolean delete(Client client, String index, TimeValue esCallTimeout)
/*     */   {
/* 338 */     return ((DeleteIndexResponse)((DeleteIndexRequestBuilder)client.admin().indices().prepareDelete(new String[] { index }).setMasterNodeTimeout(esCallTimeout)).setTimeout(esCallTimeout).execute().actionGet(esCallTimeout)).isAcknowledged();
/*     */   }
/*     */   
/*     */   private static void verifyIndexExists(Client clusterClient, String indexName, TimeValue esCallTimeout)
/*     */   {
/* 343 */     IndicesExistsResponse response = (IndicesExistsResponse)((IndicesExistsRequestBuilder)clusterClient.admin().indices().prepareExists(new String[] { indexName }).setMasterNodeTimeout(esCallTimeout)).execute().actionGet(esCallTimeout);
/*     */     
/* 345 */     if (!response.isExists())
/* 346 */       throw new IndexMissingException(new Index(indexName)); }
/*     */   
/*     */   public static abstract interface HitTransformer { public abstract Map<String, Object> transform(Map<String, Object> paramMap); }
/*     */   
/* 350 */   private static class CopyListener implements ElasticSearchHelper.Listener<SearchHit[]> { private static final Logger log = LoggerFactory.getLogger(CopyListener.class);
/*     */     
/*     */     private final Client destinationClient;
/*     */     private final String destinationIndex;
/*     */     private final String destinationType;
/*     */     private final Optional<Utilities.HitTransformer> transformer;
/*     */     private final TimeValue esCallTimeout;
/*     */     
/*     */     private CopyListener(Client destinationClient, String destinationIndex, String destinationType, Optional<Utilities.HitTransformer> transformer, TimeValue esCallTimeout)
/*     */     {
/* 360 */       this.destinationClient = destinationClient;
/* 361 */       this.destinationIndex = destinationIndex;
/* 362 */       this.destinationType = destinationType;
/* 363 */       this.transformer = transformer;
/* 364 */       this.esCallTimeout = esCallTimeout;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void onStart() {}
/*     */     
/*     */ 
/*     */     public ElasticSearchHelper.ListenerResponse onBatch(SearchHit[] searchHits)
/*     */     {
/* 374 */       if (searchHits.length == 0) {
/* 375 */         return ElasticSearchHelper.ListenerResponse.STOP;
/*     */       }
/*     */       
/* 378 */       log.info("Copying total of [" + searchHits.length + "] records to index [" + this.destinationIndex + "] for " + "type [" + this.destinationType + "]");
/*     */       
/* 380 */       BulkRequestBuilder bulkRequestBuilder = this.destinationClient.prepareBulk();
/* 381 */       for (SearchHit searchHit : searchHits) {
/* 382 */         Map<String, Object> hit = searchHit.getSource();
/* 383 */         if (this.transformer.isPresent()) {
/* 384 */           hit = ((Utilities.HitTransformer)this.transformer.get()).transform(hit);
/*     */         }
/*     */         
/* 387 */         IndexRequest indexRequest = (IndexRequest)this.destinationClient.prepareIndex(this.destinationIndex, this.destinationType).setSource(hit).request();
/*     */         
/* 389 */         bulkRequestBuilder.add(indexRequest);
/*     */       }
/*     */       try
/*     */       {
/* 393 */         BulkResponse bulkResponse = (BulkResponse)bulkRequestBuilder.setTimeout(this.esCallTimeout).execute().actionGet(this.esCallTimeout);
/*     */         
/* 395 */         if (bulkResponse.hasFailures()) {
/* 396 */           log.error("Failed in bulk request: " + bulkResponse.buildFailureMessage());
/*     */         }
/*     */       } catch (Exception e) {
/* 399 */         log.error(e.getMessage(), e);
/* 400 */         throw Throwables.propagate(e);
/*     */       }
/*     */       
/* 403 */       return ElasticSearchHelper.ListenerResponse.CONTINUE;
/*     */     }
/*     */     
/*     */ 
/*     */     public void onEnd() {}
/*     */     
/*     */ 
/*     */     public boolean handleRetryFailure()
/*     */     {
/* 412 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/maintenance/Utilities.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */