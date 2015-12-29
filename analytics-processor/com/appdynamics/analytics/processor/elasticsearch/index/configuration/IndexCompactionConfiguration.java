/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.configuration;
/*    */ 
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
/*    */ public class IndexCompactionConfiguration
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 17 */     if (o == this) return true; if (!(o instanceof IndexCompactionConfiguration)) return false; IndexCompactionConfiguration other = (IndexCompactionConfiguration)o; if (!other.canEqual(this)) return false; if (isAutoRun() != other.isAutoRun()) return false; Object this$timeFormat = getTimeFormat();Object other$timeFormat = other.getTimeFormat(); if (this$timeFormat == null ? other$timeFormat != null : !this$timeFormat.equals(other$timeFormat)) return false; Object this$runTime = getRunTime();Object other$runTime = other.getRunTime(); if (this$runTime == null ? other$runTime != null : !this$runTime.equals(other$runTime)) return false; if (getFlushThresholdInPercent() != other.getFlushThresholdInPercent()) return false; return isTestMode() == other.isTestMode(); } public boolean canEqual(Object other) { return other instanceof IndexCompactionConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + (isAutoRun() ? 1231 : 1237);Object $timeFormat = getTimeFormat();result = result * 31 + ($timeFormat == null ? 0 : $timeFormat.hashCode());Object $runTime = getRunTime();result = result * 31 + ($runTime == null ? 0 : $runTime.hashCode());result = result * 31 + getFlushThresholdInPercent();result = result * 31 + (isTestMode() ? 1231 : 1237);return result; } public String toString() { return "IndexCompactionConfiguration(autoRun=" + isAutoRun() + ", timeFormat=" + getTimeFormat() + ", runTime=" + getRunTime() + ", flushThresholdInPercent=" + getFlushThresholdInPercent() + ", testMode=" + isTestMode() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @NotNull
/* 23 */   boolean autoRun = false;
/* 24 */   public boolean isAutoRun() { return this.autoRun; } public void setAutoRun(boolean autoRun) { this.autoRun = autoRun; }
/*    */   
/*    */ 
/*    */ 
/*    */   @NotNull
/* 29 */   String timeFormat = "HH:mm";
/* 30 */   public String getTimeFormat() { return this.timeFormat; } public void setTimeFormat(String timeFormat) { this.timeFormat = timeFormat; }
/*    */   
/*    */ 
/*    */ 
/*    */   @NotNull
/* 35 */   String runTime = "02:30";
/* 36 */   public String getRunTime() { return this.runTime; } public void setRunTime(String runTime) { this.runTime = runTime; }
/*    */   
/*    */ 
/*    */ 
/*    */   @NotNull
/* 41 */   int flushThresholdInPercent = 95;
/* 42 */   public int getFlushThresholdInPercent() { return this.flushThresholdInPercent; } public void setFlushThresholdInPercent(int flushThresholdInPercent) { this.flushThresholdInPercent = flushThresholdInPercent; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 47 */   public boolean isTestMode() { return this.testMode; } public void setTestMode(boolean testMode) { this.testMode = testMode; } boolean testMode = false;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/configuration/IndexCompactionConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */