/*    */ package com.appdynamics.analytics.pipeline.framework;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageFactory;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.appdynamics.common.util.health.SimpleHealthCheck;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PipelinesModule
/*    */   extends Module<PipelinesModuleConfiguration>
/*    */ {
/*    */   @Provides
/*    */   @Singleton
/*    */   Pipelines makePipelines(LifecycleInjector lifecycleInjector, Map<String, PipelineStageFactory> pipelineStageFactories)
/*    */   {
/* 30 */     return (Pipelines)lifecycleInjector.inject(new Pipelines(pipelineStageFactories, ((PipelinesModuleConfiguration)getConfiguration()).getPipelines()));
/*    */   }
/*    */   
/*    */   @Inject
/*    */   void onStart(Environment environment, ConsolidatedHealthCheck healthCheck, Pipelines pipelines) {
/* 35 */     PipelinesResource resource = new PipelinesResource(pipelines);
/* 36 */     environment.jersey().register(resource);
/*    */     
/* 38 */     SimpleHealthCheck pipelinesHealthCheck = new PipelinesHealthCheck(getUri(), pipelines);
/* 39 */     healthCheck.register(pipelinesHealthCheck);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/PipelinesModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */