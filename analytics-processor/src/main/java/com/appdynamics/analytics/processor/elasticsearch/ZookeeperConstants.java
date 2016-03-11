/*    */ package com.appdynamics.analytics.processor.elasticsearch;
/*    */ 
/*    */ import org.apache.curator.utils.ZKPaths;
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
/*    */ public final class ZookeeperConstants
/*    */ {
/* 20 */   public static final String ZK_BASE_PATH = ZKPaths.makePath("appDynamics", "analytics");
/*    */   public static final String ZK_VERSION_KEY = "version";
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/ZookeeperConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */