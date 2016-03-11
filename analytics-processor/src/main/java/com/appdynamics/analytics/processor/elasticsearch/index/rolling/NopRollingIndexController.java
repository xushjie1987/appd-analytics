/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.rolling;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.admin.ForcedRolloverRequest;
/*    */ import com.appdynamics.common.util.lifecycle.RunningState;
/*    */ import org.apache.commons.lang.NotImplementedException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NopRollingIndexController
/*    */   implements RollingIndexController
/*    */ {
/*    */   public void start() {}
/*    */   
/*    */   public void close() {}
/*    */   
/*    */   public RunningState getRunningState()
/*    */   {
/* 20 */     return null;
/*    */   }
/*    */   
/*    */   public void forceRollover(ForcedRolloverRequest forcedRolloverRequest) {
/* 24 */     throw new NotImplementedException("Forcing a roll over is not supported here.");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/NopRollingIndexController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */