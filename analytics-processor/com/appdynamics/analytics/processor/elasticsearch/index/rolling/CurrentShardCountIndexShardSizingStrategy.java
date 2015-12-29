/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.rolling;
/*    */ 
/*    */ import org.elasticsearch.action.admin.indices.stats.IndexStats;
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
/*    */ public class CurrentShardCountIndexShardSizingStrategy
/*    */   extends AbstractIndexShardSizingStrategy
/*    */ {
/*    */   public int calculateNumberOfShards(IndexStats indexStats)
/*    */   {
/* 19 */     return getNumberOfPrimaryShards(indexStats);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/CurrentShardCountIndexShardSizingStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */