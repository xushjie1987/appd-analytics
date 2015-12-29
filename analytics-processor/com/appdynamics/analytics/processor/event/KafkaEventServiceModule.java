/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.kafka.producer.KafkaEventService;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Scopes;
/*    */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*    */ import com.google.inject.binder.ScopedBindingBuilder;
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
/*    */ public class KafkaEventServiceModule
/*    */   extends Module<Object>
/*    */ {
/*    */   public void configure()
/*    */   {
/* 25 */     bind(EventService.class).to(KafkaEventService.class).in(Scopes.SINGLETON);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/KafkaEventServiceModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */