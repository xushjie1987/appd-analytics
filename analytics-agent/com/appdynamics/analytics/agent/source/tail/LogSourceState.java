/*    */ package com.appdynamics.analytics.agent.source.tail;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.source.LogSourceConfiguration;
/*    */ import com.appdynamics.analytics.agent.source.LogWatermarkState;
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogSourceState
/*    */ {
/*    */   LogSourceConfiguration logSourceConfiguration;
/*    */   LogWatermarkState watermarkState;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 17 */     if (o == this) return true; if (!(o instanceof LogSourceState)) return false; LogSourceState other = (LogSourceState)o; if (!other.canEqual(this)) return false; Object this$logSourceConfiguration = getLogSourceConfiguration();Object other$logSourceConfiguration = other.getLogSourceConfiguration(); if (this$logSourceConfiguration == null ? other$logSourceConfiguration != null : !this$logSourceConfiguration.equals(other$logSourceConfiguration)) return false; Object this$watermarkState = getWatermarkState();Object other$watermarkState = other.getWatermarkState();return this$watermarkState == null ? other$watermarkState == null : this$watermarkState.equals(other$watermarkState); } public boolean canEqual(Object other) { return other instanceof LogSourceState; } public int hashCode() { int PRIME = 31;int result = 1;Object $logSourceConfiguration = getLogSourceConfiguration();result = result * 31 + ($logSourceConfiguration == null ? 0 : $logSourceConfiguration.hashCode());Object $watermarkState = getWatermarkState();result = result * 31 + ($watermarkState == null ? 0 : $watermarkState.hashCode());return result; } public String toString() { return "LogSourceState(logSourceConfiguration=" + getLogSourceConfiguration() + ", watermarkState=" + getWatermarkState() + ")"; }
/*    */   
/* 19 */   public LogSourceConfiguration getLogSourceConfiguration() { return this.logSourceConfiguration; } public void setLogSourceConfiguration(LogSourceConfiguration logSourceConfiguration) { this.logSourceConfiguration = logSourceConfiguration; }
/* 20 */   public LogWatermarkState getWatermarkState() { return this.watermarkState; } public void setWatermarkState(LogWatermarkState watermarkState) { this.watermarkState = watermarkState; }
/*    */   
/*    */   @JsonCreator
/*    */   public LogSourceState(@JsonProperty("logSourceConfiguration") LogSourceConfiguration logSourceConfiguration, @JsonProperty("watermarkState") LogWatermarkState watermarkState)
/*    */   {
/* 25 */     this.logSourceConfiguration = logSourceConfiguration;
/* 26 */     this.watermarkState = watermarkState;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/tail/LogSourceState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */