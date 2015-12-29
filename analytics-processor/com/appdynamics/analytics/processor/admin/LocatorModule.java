/*    */ package com.appdynamics.analytics.processor.admin;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ClusterRouter;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.event.EventBuses;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocatorModule
/*    */   extends Module<Object>
/*    */ {
/* 22 */   private static final Logger log = LoggerFactory.getLogger(LocatorModule.class);
/*    */   static final long CLUSTER_CHANGE_POLL_TIME_MILLIS = 10000L;
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   Locator makeLocator(ClusterRouter router)
/*    */   {
/* 29 */     return new DefaultLocator(router);
/*    */   }
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   ClusterChangeNotifier makeNotifier(ClusterRouter clusterRouter, EventBuses eventBuses, LifecycleInjector injector) {
/* 35 */     return (ClusterChangeNotifier)injector.inject(new ClusterChangeNotifier(clusterRouter, eventBuses, 10000L));
/*    */   }
/*    */   
/*    */   @Inject
/*    */   void onStart(Locator locator, ClusterChangeNotifier notifier) {
/* 40 */     log.debug("Started");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/LocatorModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */