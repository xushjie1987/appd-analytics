/*    */ package com.appdynamics.analytics.pipeline.util;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageFactory;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.google.inject.Binder;
/*    */ import com.google.inject.binder.LinkedBindingBuilder;
/*    */ import com.google.inject.multibindings.MapBinder;
/*    */ import java.util.Objects;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PipelineHelper
/*    */ {
/*    */   public static <STG_OUT> PipelineStageParameters<STG_OUT> parameters(Object pipelineId, PipelineStage<? super STG_OUT, ?> nextStage, Object rawStageConfiguration)
/*    */   {
/* 36 */     return new SimplePipelineStageParameters(pipelineId, nextStage, rawStageConfiguration);
/*    */   }
/*    */   
/*    */   public static LinkedBindingBuilder<PipelineStageFactory> bind(Binder binder, String uri) {
/* 40 */     Objects.requireNonNull(uri, "Uri cannot be null");
/* 41 */     MapBinder<String, PipelineStageFactory> pipelineStageProviders = MapBinder.newMapBinder(binder, String.class, PipelineStageFactory.class);
/*    */     
/*    */ 
/* 44 */     return pipelineStageProviders.addBinding(uri);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/util/PipelineHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */