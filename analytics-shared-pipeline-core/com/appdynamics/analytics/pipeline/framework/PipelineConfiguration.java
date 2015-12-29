/*    */ package com.appdynamics.analytics.pipeline.framework;
/*    */ 
/*    */ import com.appdynamics.common.util.item.SimpleItem;
/*    */ import java.util.List;
/*    */ import javax.validation.constraints.NotNull;
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
/*    */ public class PipelineConfiguration
/*    */   extends SimpleItem<Object>
/*    */ {
/*    */   @NotNull
/*    */   List<PipelineStageConfiguration> stages;
/*    */   
/* 26 */   public List<PipelineStageConfiguration> getStages() { return this.stages; }
/* 27 */   public void setStages(List<PipelineStageConfiguration> stages) { this.stages = stages; }
/*    */   
/*    */ 
/* 30 */   public boolean isEnabled() { return this.enabled; } boolean enabled = true;
/* 31 */   public void setEnabled(boolean enabled) { this.enabled = enabled; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/PipelineConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */