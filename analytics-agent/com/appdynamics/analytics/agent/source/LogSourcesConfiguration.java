/*    */ package com.appdynamics.analytics.agent.source;
/*    */ 
/*    */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class LogSourcesConfiguration
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 22 */     if (o == this) return true; if (!(o instanceof LogSourcesConfiguration)) return false; LogSourcesConfiguration other = (LogSourcesConfiguration)o; if (!other.canEqual(this)) return false; Object this$watermarkPath = getWatermarkPath();Object other$watermarkPath = other.getWatermarkPath(); if (this$watermarkPath == null ? other$watermarkPath != null : !this$watermarkPath.equals(other$watermarkPath)) return false; Object this$saveSourceStateInterval = getSaveSourceStateInterval();Object other$saveSourceStateInterval = other.getSaveSourceStateInterval(); if (this$saveSourceStateInterval == null ? other$saveSourceStateInterval != null : !this$saveSourceStateInterval.equals(other$saveSourceStateInterval)) return false; Object this$jobPaths = getJobPaths();Object other$jobPaths = other.getJobPaths();return this$jobPaths == null ? other$jobPaths == null : this$jobPaths.equals(other$jobPaths); } public boolean canEqual(Object other) { return other instanceof LogSourcesConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $watermarkPath = getWatermarkPath();result = result * 31 + ($watermarkPath == null ? 0 : $watermarkPath.hashCode());Object $saveSourceStateInterval = getSaveSourceStateInterval();result = result * 31 + ($saveSourceStateInterval == null ? 0 : $saveSourceStateInterval.hashCode());Object $jobPaths = getJobPaths();result = result * 31 + ($jobPaths == null ? 0 : $jobPaths.hashCode());return result; } public String toString() { return "LogSourcesConfiguration(watermarkPath=" + getWatermarkPath() + ", saveSourceStateInterval=" + getSaveSourceStateInterval() + ", jobPaths=" + getJobPaths() + ")"; }
/*    */   @Valid
/*    */   @NotNull
/* 25 */   String watermarkPath = null;
/*    */   
/* 27 */   public String getWatermarkPath() { return this.watermarkPath; } public void setWatermarkPath(String watermarkPath) { this.watermarkPath = watermarkPath; } @Valid
/*    */   @NotNull
/* 29 */   TimeUnitConfiguration saveSourceStateInterval = new TimeUnitConfiguration(1L, TimeUnit.SECONDS);
/*    */   
/* 31 */   public TimeUnitConfiguration getSaveSourceStateInterval() { return this.saveSourceStateInterval; } public void setSaveSourceStateInterval(TimeUnitConfiguration saveSourceStateInterval) { this.saveSourceStateInterval = saveSourceStateInterval; } @Valid
/*    */   @NotNull
/* 33 */   List<FilePathConfiguration> jobPaths = new ArrayList();
/*    */   
/* 35 */   public List<FilePathConfiguration> getJobPaths() { return this.jobPaths; } public void setJobPaths(List<FilePathConfiguration> jobPaths) { this.jobPaths = jobPaths; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/LogSourcesConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */