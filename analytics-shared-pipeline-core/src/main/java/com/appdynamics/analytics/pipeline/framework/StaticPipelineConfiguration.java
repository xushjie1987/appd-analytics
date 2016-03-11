/*    */ package com.appdynamics.analytics.pipeline.framework;
/*    */ 
/*    */ import javax.validation.constraints.Min;
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
/*    */ public class StaticPipelineConfiguration
/*    */   extends PipelineConfiguration
/*    */ {
/*    */   @Min(1L)
/* 19 */   int instances = 1;
/* 20 */   public int getInstances() { return this.instances; }
/* 21 */   public void setInstances(int instances) { this.instances = instances; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/StaticPipelineConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */