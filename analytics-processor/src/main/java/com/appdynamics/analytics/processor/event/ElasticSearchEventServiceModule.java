/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.configuration.EventServiceConfiguration;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.annotation.Raw;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Scopes;
/*    */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*    */ import com.google.inject.binder.LinkedBindingBuilder;
/*    */ import com.google.inject.binder.ScopedBindingBuilder;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElasticSearchEventServiceModule
/*    */   extends Module<EventServiceConfiguration>
/*    */ {
/* 20 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchEventServiceModule.class);
/*    */   
/*    */   @Provides
/*    */   EventServiceConfiguration provideStoreConfig()
/*    */   {
/* 25 */     return (EventServiceConfiguration)getConfiguration();
/*    */   }
/*    */   
/*    */ 
/*    */   public void configure()
/*    */   {
/* 31 */     bind(ElasticSearchEventService.class).in(Scopes.SINGLETON);
/* 32 */     bind(EventService.class).annotatedWith(Raw.class).to(ElasticSearchEventService.class).in(Scopes.SINGLETON);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ElasticSearchEventServiceModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */