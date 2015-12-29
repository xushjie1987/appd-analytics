/*    */ package com.appdynamics.analytics.agent.source.tail;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.input.tail.TailLogInputConfiguration;
/*    */ import com.appdynamics.analytics.agent.source.LogSourceConfiguration;
/*    */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ public class TailLogSourceConfiguration
/*    */   extends LogSourceConfiguration
/*    */ {
/*    */   @Valid
/*    */   @NotNull
/*    */   FilePathConfiguration sourcePath;
/*    */   @Valid
/*    */   @NotNull
/*    */   TailLogInputConfiguration tailLogInputConfiguration;
/*    */   
/* 25 */   public String toString() { return "TailLogSourceConfiguration(sourcePath=" + getSourcePath() + ", tailLogInputConfiguration=" + getTailLogInputConfiguration() + ", directoryPollingInterval=" + getDirectoryPollingInterval() + ", stopIdleInputsTimeout=" + getStopIdleInputsTimeout() + ", discardTailingStateTimeout=" + getDiscardTailingStateTimeout() + ", discardTailingStateCheckInterval=" + getDiscardTailingStateCheckInterval() + ", maximumInputPoolSize=" + getMaximumInputPoolSize() + ")"; }
/* 26 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof TailLogSourceConfiguration)) return false; TailLogSourceConfiguration other = (TailLogSourceConfiguration)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$sourcePath = getSourcePath();Object other$sourcePath = other.getSourcePath(); if (this$sourcePath == null ? other$sourcePath != null : !this$sourcePath.equals(other$sourcePath)) return false; Object this$tailLogInputConfiguration = getTailLogInputConfiguration();Object other$tailLogInputConfiguration = other.getTailLogInputConfiguration(); if (this$tailLogInputConfiguration == null ? other$tailLogInputConfiguration != null : !this$tailLogInputConfiguration.equals(other$tailLogInputConfiguration)) return false; Object this$directoryPollingInterval = getDirectoryPollingInterval();Object other$directoryPollingInterval = other.getDirectoryPollingInterval(); if (this$directoryPollingInterval == null ? other$directoryPollingInterval != null : !this$directoryPollingInterval.equals(other$directoryPollingInterval)) return false; Object this$stopIdleInputsTimeout = getStopIdleInputsTimeout();Object other$stopIdleInputsTimeout = other.getStopIdleInputsTimeout(); if (this$stopIdleInputsTimeout == null ? other$stopIdleInputsTimeout != null : !this$stopIdleInputsTimeout.equals(other$stopIdleInputsTimeout)) return false; Object this$discardTailingStateTimeout = getDiscardTailingStateTimeout();Object other$discardTailingStateTimeout = other.getDiscardTailingStateTimeout(); if (this$discardTailingStateTimeout == null ? other$discardTailingStateTimeout != null : !this$discardTailingStateTimeout.equals(other$discardTailingStateTimeout)) return false; Object this$discardTailingStateCheckInterval = getDiscardTailingStateCheckInterval();Object other$discardTailingStateCheckInterval = other.getDiscardTailingStateCheckInterval(); if (this$discardTailingStateCheckInterval == null ? other$discardTailingStateCheckInterval != null : !this$discardTailingStateCheckInterval.equals(other$discardTailingStateCheckInterval)) return false; return getMaximumInputPoolSize() == other.getMaximumInputPoolSize(); } public boolean canEqual(Object other) { return other instanceof TailLogSourceConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $sourcePath = getSourcePath();result = result * 31 + ($sourcePath == null ? 0 : $sourcePath.hashCode());Object $tailLogInputConfiguration = getTailLogInputConfiguration();result = result * 31 + ($tailLogInputConfiguration == null ? 0 : $tailLogInputConfiguration.hashCode());Object $directoryPollingInterval = getDirectoryPollingInterval();result = result * 31 + ($directoryPollingInterval == null ? 0 : $directoryPollingInterval.hashCode());Object $stopIdleInputsTimeout = getStopIdleInputsTimeout();result = result * 31 + ($stopIdleInputsTimeout == null ? 0 : $stopIdleInputsTimeout.hashCode());Object $discardTailingStateTimeout = getDiscardTailingStateTimeout();result = result * 31 + ($discardTailingStateTimeout == null ? 0 : $discardTailingStateTimeout.hashCode());Object $discardTailingStateCheckInterval = getDiscardTailingStateCheckInterval();result = result * 31 + ($discardTailingStateCheckInterval == null ? 0 : $discardTailingStateCheckInterval.hashCode());result = result * 31 + getMaximumInputPoolSize();return result;
/*    */   }
/*    */   
/*    */ 
/* 30 */   public FilePathConfiguration getSourcePath() { return this.sourcePath; } public void setSourcePath(FilePathConfiguration sourcePath) { this.sourcePath = sourcePath; }
/*    */   
/*    */ 
/*    */ 
/* 34 */   public TailLogInputConfiguration getTailLogInputConfiguration() { return this.tailLogInputConfiguration; } public void setTailLogInputConfiguration(TailLogInputConfiguration tailLogInputConfiguration) { this.tailLogInputConfiguration = tailLogInputConfiguration; }
/*    */   
/*    */   @Valid
/*    */   @NotNull
/* 38 */   TimeUnitConfiguration directoryPollingInterval = new TimeUnitConfiguration(20L, TimeUnit.SECONDS);
/*    */   
/* 40 */   public TimeUnitConfiguration getDirectoryPollingInterval() { return this.directoryPollingInterval; } public void setDirectoryPollingInterval(TimeUnitConfiguration directoryPollingInterval) { this.directoryPollingInterval = directoryPollingInterval; } @Valid
/*    */   @NotNull
/* 42 */   TimeUnitConfiguration stopIdleInputsTimeout = new TimeUnitConfiguration(30L, TimeUnit.SECONDS);
/*    */   
/* 44 */   public TimeUnitConfiguration getStopIdleInputsTimeout() { return this.stopIdleInputsTimeout; } public void setStopIdleInputsTimeout(TimeUnitConfiguration stopIdleInputsTimeout) { this.stopIdleInputsTimeout = stopIdleInputsTimeout; }
/*    */   
/*    */ 
/*    */   @Valid
/*    */   @NotNull
/* 49 */   TimeUnitConfiguration discardTailingStateTimeout = new TimeUnitConfiguration(30L, TimeUnit.DAYS);
/*    */   
/* 51 */   public TimeUnitConfiguration getDiscardTailingStateTimeout() { return this.discardTailingStateTimeout; } public void setDiscardTailingStateTimeout(TimeUnitConfiguration discardTailingStateTimeout) { this.discardTailingStateTimeout = discardTailingStateTimeout; }
/*    */   
/*    */ 
/*    */   @Valid
/*    */   @NotNull
/* 56 */   TimeUnitConfiguration discardTailingStateCheckInterval = new TimeUnitConfiguration(1L, TimeUnit.HOURS);
/*    */   
/* 58 */   public TimeUnitConfiguration getDiscardTailingStateCheckInterval() { return this.discardTailingStateCheckInterval; } public void setDiscardTailingStateCheckInterval(TimeUnitConfiguration discardTailingStateCheckInterval) { this.discardTailingStateCheckInterval = discardTailingStateCheckInterval; }
/*    */   @Min(1L)
/* 60 */   int maximumInputPoolSize = 5;
/* 61 */   public int getMaximumInputPoolSize() { return this.maximumInputPoolSize; } public void setMaximumInputPoolSize(int maximumInputPoolSize) { this.maximumInputPoolSize = maximumInputPoolSize; }
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
/*    */   @com.fasterxml.jackson.annotation.JsonCreator
/*    */   public TailLogSourceConfiguration(@JsonProperty("name") String name, @JsonProperty("sourceType") String sourceType, @JsonProperty("extractedFieldPatterns") Set<String> extractedFieldPatterns, @JsonProperty("watermarkPath") FilePathConfiguration sourcePath, @JsonProperty("tailLogInputConfiguration") TailLogInputConfiguration tailLogInputConfiguration, @JsonProperty("directoryPollingInterval") TimeUnitConfiguration directoryPollingInterval, @JsonProperty("stopIdleInputsTimeout") TimeUnitConfiguration stopIdleInputsTimeout, @JsonProperty("discardTailingStateTimeout") TimeUnitConfiguration discardTailingStateTimeout, @JsonProperty("discardTailingStateCheckInterval") TimeUnitConfiguration discardTailingStateCheckInterval, @JsonProperty("maximumInputPoolSize") int maximumInputPoolSize)
/*    */   {
/* 80 */     super(name, sourceType, extractedFieldPatterns);
/*    */     
/* 82 */     this.sourcePath = sourcePath;
/* 83 */     this.tailLogInputConfiguration = tailLogInputConfiguration;
/* 84 */     this.directoryPollingInterval = directoryPollingInterval;
/* 85 */     this.stopIdleInputsTimeout = stopIdleInputsTimeout;
/* 86 */     this.discardTailingStateTimeout = discardTailingStateTimeout;
/* 87 */     this.discardTailingStateCheckInterval = discardTailingStateCheckInterval;
/* 88 */     this.maximumInputPoolSize = maximumInputPoolSize;
/*    */   }
/*    */   
/*    */   public TailLogSourceConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/tail/TailLogSourceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */