/*    */ package com.appdynamics.analytics.agent.input.tail;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.input.LogInputConfiguration;
/*    */ import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
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
/*    */ public class TailLogInputConfiguration
/*    */   extends LogInputConfiguration
/*    */ {
/* 23 */   public String toString() { return "TailLogInputConfiguration(tailInterval=" + getTailInterval() + ", startAtEnd=" + isStartAtEnd() + ")"; }
/* 24 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof TailLogInputConfiguration)) return false; TailLogInputConfiguration other = (TailLogInputConfiguration)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$tailInterval = getTailInterval();Object other$tailInterval = other.getTailInterval(); if (this$tailInterval == null ? other$tailInterval != null : !this$tailInterval.equals(other$tailInterval)) return false; return isStartAtEnd() == other.isStartAtEnd(); } public boolean canEqual(Object other) { return other instanceof TailLogInputConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $tailInterval = getTailInterval();result = result * 31 + ($tailInterval == null ? 0 : $tailInterval.hashCode());result = result * 31 + (isStartAtEnd() ? 1231 : 1237);return result; }
/*    */   @Valid
/*    */   @NotNull
/* 27 */   TimeUnitConfiguration tailInterval = new TimeUnitConfiguration(50L, TimeUnit.MILLISECONDS);
/*    */   
/* 29 */   public TimeUnitConfiguration getTailInterval() { return this.tailInterval; } public void setTailInterval(TimeUnitConfiguration tailInterval) { this.tailInterval = tailInterval; }
/*    */   
/* 31 */   public boolean isStartAtEnd() { return this.startAtEnd; } public void setStartAtEnd(boolean startAtEnd) { this.startAtEnd = startAtEnd; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   boolean startAtEnd;
/*    */   
/*    */ 
/*    */ 
/*    */   @JsonCreator
/*    */   public TailLogInputConfiguration(@JsonProperty("pipelineConfiguration") PipelineConfiguration pipelineConfiguration, @JsonProperty("tailInterval") TimeUnitConfiguration tailInterval, @JsonProperty("startAtEnd") boolean startAtEnd)
/*    */   {
/* 43 */     super(pipelineConfiguration);
/*    */     
/* 45 */     this.tailInterval = tailInterval;
/* 46 */     this.startAtEnd = startAtEnd;
/*    */   }
/*    */   
/*    */   public TailLogInputConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/input/tail/TailLogInputConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */