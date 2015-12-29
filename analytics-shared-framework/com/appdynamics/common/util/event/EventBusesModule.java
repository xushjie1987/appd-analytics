/*    */ package com.appdynamics.common.util.event;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventBusesModule
/*    */   extends Module<List<EventBusConfiguration>>
/*    */ {
/* 21 */   private static final Logger log = LoggerFactory.getLogger(EventBusesModule.class);
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   public EventBuses makeEventBuses(LifecycleInjector injector) {
/* 26 */     return (EventBuses)injector.inject(new EventBuses((List)getConfiguration()));
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/event/EventBusesModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */