/*    */ package com.appdynamics.analytics.pipeline.framework;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.validation.Valid;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PipelinesModuleConfiguration
/*    */ {
/*    */   @Valid
/*    */   List<StaticPipelineConfiguration> pipelines;
/*    */   
/* 16 */   public String toString() { return "PipelinesModuleConfiguration(pipelines=" + getPipelines() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $pipelines = getPipelines();result = result * 31 + ($pipelines == null ? 0 : $pipelines.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof PipelinesModuleConfiguration; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof PipelinesModuleConfiguration)) return false; PipelinesModuleConfiguration other = (PipelinesModuleConfiguration)o; if (!other.canEqual(this)) return false; Object this$pipelines = getPipelines();Object other$pipelines = other.getPipelines();return this$pipelines == null ? other$pipelines == null : this$pipelines.equals(other$pipelines);
/*    */   }
/*    */   
/*    */ 
/* 20 */   public void setPipelines(List<StaticPipelineConfiguration> pipelines) { this.pipelines = pipelines; } public List<StaticPipelineConfiguration> getPipelines() { return this.pipelines; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/PipelinesModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */