/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.multi;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ElasticSearchHealthCheck;
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
/*    */ public class ElasticSearchMultiNodeHealthCheck
/*    */   extends ElasticSearchHealthCheck
/*    */ {
/*    */   private ElasticSearchMultiNode elasticSearchMultiNode;
/*    */   
/*    */   public ElasticSearchMultiNodeHealthCheck(String name, ElasticSearchMultiNode elasticSearchMultiNode)
/*    */   {
/* 34 */     super(name);
/* 35 */     this.elasticSearchMultiNode = elasticSearchMultiNode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<Client> getClients()
/*    */   {
/* 43 */     return this.elasticSearchMultiNode.getAllClusterClients();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/multi/ElasticSearchMultiNodeHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */