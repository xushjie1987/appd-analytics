/*    */ package com.appdynamics.analytics.pipeline.util;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
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
/*    */ public final class SimplePipelineStageParameters<OUT>
/*    */   implements PipelineStageParameters<OUT>
/*    */ {
/*    */   final Object pipelineId;
/*    */   final PipelineStage<? super OUT, ?> optionalNextStage;
/*    */   final Object rawStageConfiguration;
/*    */   
/*    */   SimplePipelineStageParameters(Object pipelineId, PipelineStage<? super OUT, ?> optionalNextStage, Object rawStageConfiguration)
/*    */   {
/* 32 */     this.pipelineId = pipelineId;
/* 33 */     this.optionalNextStage = optionalNextStage;
/* 34 */     Objects.requireNonNull(rawStageConfiguration, "Raw stage configuration cannot be null");
/* 35 */     this.rawStageConfiguration = rawStageConfiguration;
/*    */   }
/*    */   
/*    */   public Object getPipelineId()
/*    */   {
/* 40 */     return this.pipelineId;
/*    */   }
/*    */   
/*    */   public PipelineStage<? super OUT, ?> getOptionalNextStage()
/*    */   {
/* 45 */     return this.optionalNextStage;
/*    */   }
/*    */   
/*    */   public Object getRawStageConfiguration() {
/* 49 */     return this.rawStageConfiguration;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/util/SimplePipelineStageParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */