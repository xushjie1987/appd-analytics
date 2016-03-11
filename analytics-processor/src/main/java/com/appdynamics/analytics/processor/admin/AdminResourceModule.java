/*    */ package com.appdynamics.analytics.processor.admin;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.compaction.IndexCompactionManager;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.RollingIndexController;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
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
/*    */ 
/*    */ 
/*    */ public class AdminResourceModule
/*    */   extends Module<Object>
/*    */ {
/*    */   AdminResource adminResource;
/*    */   @Inject
/*    */   ConsolidatedHealthCheck healthCheck;
/*    */   @Inject
/*    */   RollingIndexController rollingIndexLeader;
/*    */   @Inject
/*    */   IndexCompactionManager indexCompactionManager;
/*    */   @Inject
/*    */   volatile Environment environment;
/*    */   
/*    */   @PostConstruct
/*    */   public void onStart()
/*    */   {
/* 36 */     this.adminResource = new AdminResource(this.healthCheck, this.environment, this.rollingIndexLeader, this.indexCompactionManager);
/* 37 */     this.environment.jersey().register(this.adminResource);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/AdminResourceModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */