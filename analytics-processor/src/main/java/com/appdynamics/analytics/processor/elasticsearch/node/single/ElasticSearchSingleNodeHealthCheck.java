/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.single;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ElasticSearchHealthCheck;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import org.elasticsearch.client.Client;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElasticSearchSingleNodeHealthCheck
/*    */   extends ElasticSearchHealthCheck
/*    */ {
/*    */   private ElasticSearchSingleNode elasticSearchSingleNode;
/*    */   
/*    */   public ElasticSearchSingleNodeHealthCheck(String name, ElasticSearchSingleNode elasticSearchSingleNode)
/*    */   {
/* 35 */     super(name);
/* 36 */     this.elasticSearchSingleNode = elasticSearchSingleNode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<Client> getClients()
/*    */   {
/* 44 */     return ImmutableList.of(this.elasticSearchSingleNode.getClient());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/single/ElasticSearchSingleNodeHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */