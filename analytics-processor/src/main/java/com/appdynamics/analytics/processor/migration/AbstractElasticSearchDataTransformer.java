/*    */ package com.appdynamics.analytics.processor.migration;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper.BaseListener;
/*    */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper.ListenerResponse;
/*    */ import java.util.Map;
/*    */ import org.elasticsearch.action.ActionFuture;
/*    */ import org.elasticsearch.action.bulk.BulkRequest;
/*    */ import org.elasticsearch.action.bulk.BulkRequestBuilder;
/*    */ import org.elasticsearch.action.bulk.BulkResponse;
/*    */ import org.elasticsearch.action.update.UpdateRequest;
/*    */ import org.elasticsearch.action.update.UpdateRequestBuilder;
/*    */ import org.elasticsearch.client.Client;
/*    */ import org.elasticsearch.common.xcontent.XContentBuilder;
/*    */ import org.elasticsearch.search.SearchHit;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractElasticSearchDataTransformer
/*    */   extends ElasticSearchHelper.BaseListener<SearchHit[]>
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(AbstractElasticSearchDataTransformer.class);
/*    */   final Client client;
/*    */   final String indexName;
/*    */   
/*    */   AbstractElasticSearchDataTransformer(Client client, String indexName)
/*    */   {
/* 33 */     this.client = client;
/* 34 */     this.indexName = indexName;
/*    */   }
/*    */   
/*    */   public ElasticSearchHelper.ListenerResponse onBatch(SearchHit[] searchHits)
/*    */   {
/* 39 */     if (searchHits.length == 0) {
/* 40 */       return ElasticSearchHelper.ListenerResponse.STOP;
/*    */     }
/*    */     
/* 43 */     BulkRequestBuilder builder = this.client.prepareBulk();
/* 44 */     for (SearchHit searchHit : searchHits) {
/* 45 */       Map<String, Object> source = searchHit.getSource();
/* 46 */       XContentBuilder upsertBuilder = getContentBuilder(source);
/* 47 */       UpdateRequest updateRequest = (UpdateRequest)this.client.prepareUpdate(this.indexName, searchHit.getType(), searchHit.getId()).setDoc(upsertBuilder).request();
/*    */       
/* 49 */       builder.add(updateRequest);
/*    */     }
/*    */     
/* 52 */     BulkResponse bulkResponse = (BulkResponse)this.client.bulk((BulkRequest)builder.request()).actionGet();
/* 53 */     if (bulkResponse.hasFailures()) {
/* 54 */       log.warn("Batch encountered errors [{}]. A restart will be requested", bulkResponse.buildFailureMessage());
/*    */       
/* 56 */       return ElasticSearchHelper.ListenerResponse.RESTART;
/*    */     }
/* 58 */     return ElasticSearchHelper.ListenerResponse.CONTINUE;
/*    */   }
/*    */   
/*    */   protected abstract XContentBuilder getContentBuilder(Map<String, Object> paramMap);
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/migration/AbstractElasticSearchDataTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */