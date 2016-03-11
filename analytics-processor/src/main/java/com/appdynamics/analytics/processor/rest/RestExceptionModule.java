/*    */ package com.appdynamics.analytics.processor.rest;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Inject;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import javax.annotation.PostConstruct;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RestExceptionModule
/*    */   extends Module<Object>
/*    */ {
/*    */   @Inject
/*    */   private Environment environment;
/*    */   
/*    */   @PostConstruct
/*    */   void registerExceptionMapper()
/*    */   {
/* 24 */     this.environment.jersey().register(RestErrorMapper.class);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/RestExceptionModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */