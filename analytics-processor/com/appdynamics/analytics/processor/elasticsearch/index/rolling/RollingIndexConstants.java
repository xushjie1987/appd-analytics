/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.rolling;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ZookeeperConstants;
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
/*    */ public class RollingIndexConstants
/*    */ {
/* 17 */   public static final String LEADER_ZK_PATH = ZKPaths.makePath(ZookeeperConstants.ZK_BASE_PATH, "rolling-index");
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/RollingIndexConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */