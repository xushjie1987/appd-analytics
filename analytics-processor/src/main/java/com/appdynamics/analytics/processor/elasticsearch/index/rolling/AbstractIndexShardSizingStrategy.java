/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.rolling;
/*    */ 
/*    */ import org.elasticsearch.action.admin.indices.stats.IndexStats;
/*    */ import org.elasticsearch.action.admin.indices.stats.ShardStats;
/*    */ import org.elasticsearch.cluster.routing.ShardRouting;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractIndexShardSizingStrategy
/*    */   implements RollingIndexShardSizingStrategy
/*    */ {
/*    */   protected int getNumberOfPrimaryShards(IndexStats stats)
/*    */   {
/* 20 */     int numPrimaries = 0;
/* 21 */     for (ShardStats shard : stats.getShards()) {
/* 22 */       if (shard.getShardRouting().primary()) {
/* 23 */         numPrimaries++;
/*    */       }
/*    */     }
/* 26 */     return numPrimaries;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/AbstractIndexShardSizingStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */