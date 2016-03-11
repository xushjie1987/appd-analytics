/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.rolling;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NopRollingIndexControllerModule
/*    */   extends Module<Object>
/*    */ {
/*    */   @Provides
/*    */   @Singleton
/*    */   RollingIndexController makeRollingIndexController()
/*    */   {
/* 19 */     return new NopRollingIndexController();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/NopRollingIndexControllerModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */