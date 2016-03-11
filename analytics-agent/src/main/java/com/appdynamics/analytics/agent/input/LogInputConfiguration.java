/*    */ package com.appdynamics.analytics.agent.input;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
/*    */ import com.fasterxml.jackson.annotation.JsonSubTypes;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
/*    */ @JsonSubTypes({@com.fasterxml.jackson.annotation.JsonSubTypes.Type(value=com.appdynamics.analytics.agent.input.tail.TailLogInputConfiguration.class, name="tailLogInputConfiguration")})
/*    */ public class LogInputConfiguration
/*    */ {
/*    */   @NotNull
/*    */   PipelineConfiguration pipelineConfiguration;
/*    */   
/* 22 */   public String toString() { return "LogInputConfiguration(pipelineConfiguration=" + getPipelineConfiguration() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $pipelineConfiguration = getPipelineConfiguration();result = result * 31 + ($pipelineConfiguration == null ? 0 : $pipelineConfiguration.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof LogInputConfiguration; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof LogInputConfiguration)) return false; LogInputConfiguration other = (LogInputConfiguration)o; if (!other.canEqual(this)) return false; Object this$pipelineConfiguration = getPipelineConfiguration();Object other$pipelineConfiguration = other.getPipelineConfiguration();return this$pipelineConfiguration == null ? other$pipelineConfiguration == null : this$pipelineConfiguration.equals(other$pipelineConfiguration);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 27 */   public void setPipelineConfiguration(PipelineConfiguration pipelineConfiguration) { this.pipelineConfiguration = pipelineConfiguration; } public PipelineConfiguration getPipelineConfiguration() { return this.pipelineConfiguration; }
/*    */   
/*    */ 
/*    */ 
/*    */   public LogInputConfiguration(PipelineConfiguration pipelineConfiguration)
/*    */   {
/* 33 */     this.pipelineConfiguration = pipelineConfiguration;
/*    */   }
/*    */   
/*    */   public LogInputConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/input/LogInputConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */