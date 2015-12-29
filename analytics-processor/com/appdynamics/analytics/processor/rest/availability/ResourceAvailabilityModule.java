/*    */ package com.appdynamics.analytics.processor.rest.availability;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.rest.availability.resource.ResourceAvailabilityResource;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Inject;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.apache.curator.framework.CuratorFramework;
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
/*    */ public class ResourceAvailabilityModule
/*    */   extends Module<Object>
/*    */ {
/*    */   @Inject
/*    */   CuratorFramework zkClient;
/*    */   @Inject
/*    */   volatile Environment environment;
/*    */   @Inject
/*    */   ResourceAvailabilityResource resourceAvailabilityResource;
/*    */   
/*    */   @PostConstruct
/*    */   public void onPreStart()
/*    */     throws Exception
/*    */   {
/* 37 */     this.environment.jersey().register(new ResourceAvailabilityAdapter(this.zkClient));
/* 38 */     this.environment.jersey().register(this.resourceAvailabilityResource);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/availability/ResourceAvailabilityModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */