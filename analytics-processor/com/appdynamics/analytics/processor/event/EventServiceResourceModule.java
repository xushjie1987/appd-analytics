/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.resource.EventServiceResource;
/*    */ import com.appdynamics.analytics.processor.event.resource.ExtractedFieldsResource;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Scopes;
/*    */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*    */ import com.google.inject.binder.ScopedBindingBuilder;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventServiceResourceModule
/*    */   extends Module<Object>
/*    */ {
/* 24 */   private static final Logger log = LoggerFactory.getLogger(EventServiceResourceModule.class);
/*    */   
/*    */   @Inject
/*    */   volatile Environment environment;
/*    */   
/*    */   @Inject
/*    */   EventServiceResource eventServiceResource;
/*    */   
/*    */   @Inject
/*    */   ExtractedFieldsResource extractedFieldsResource;
/*    */   
/*    */ 
/*    */   public void configure()
/*    */   {
/* 38 */     bind(ExtractedFieldsService.class).to(ElasticSearchExtractedFieldsService.class).in(Scopes.SINGLETON);
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   public void onStart() {
/* 43 */     this.environment.jersey().register(this.eventServiceResource);
/* 44 */     this.environment.jersey().register(this.extractedFieldsResource);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/EventServiceResourceModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */