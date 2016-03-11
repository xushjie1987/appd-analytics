/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.configuration;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
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
/*    */ public class RollingIndexConfiguration
/*    */ {
/*    */   public static final int DEFAULT_ROLLING_IDX_CHECK_INTERVAL_MINUTES = 60;
/*    */   
/* 18 */   public String toString() { return "RollingIndexConfiguration(rollingIndexCheckInterval=" + getRollingIndexCheckInterval() + ", shardSizeThreshold=" + getShardSizeThreshold() + ", targetIndexLifeInDays=" + getTargetIndexLifeInDays() + ", maximumIndexLifespanInDays=" + getMaximumIndexLifespanInDays() + ")"; }
/* 19 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof RollingIndexConfiguration)) return false; RollingIndexConfiguration other = (RollingIndexConfiguration)o; if (!other.canEqual(this)) return false; Object this$rollingIndexCheckInterval = getRollingIndexCheckInterval();Object other$rollingIndexCheckInterval = other.getRollingIndexCheckInterval(); if (this$rollingIndexCheckInterval == null ? other$rollingIndexCheckInterval != null : !this$rollingIndexCheckInterval.equals(other$rollingIndexCheckInterval)) return false; if (getShardSizeThreshold() != other.getShardSizeThreshold()) return false; if (getTargetIndexLifeInDays() != other.getTargetIndexLifeInDays()) return false; return getMaximumIndexLifespanInDays() == other.getMaximumIndexLifespanInDays(); } public boolean canEqual(Object other) { return other instanceof RollingIndexConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $rollingIndexCheckInterval = getRollingIndexCheckInterval();result = result * 31 + ($rollingIndexCheckInterval == null ? 0 : $rollingIndexCheckInterval.hashCode());long $shardSizeThreshold = getShardSizeThreshold();result = result * 31 + (int)($shardSizeThreshold >>> 32 ^ $shardSizeThreshold);result = result * 31 + getTargetIndexLifeInDays();result = result * 31 + getMaximumIndexLifespanInDays();return result;
/*    */   }
/*    */   
/*    */   @Valid
/*    */   @NotNull
/* 24 */   TimeUnitConfiguration rollingIndexCheckInterval = new TimeUnitConfiguration(60L, TimeUnit.MINUTES);
/*    */   
/* 26 */   public TimeUnitConfiguration getRollingIndexCheckInterval() { return this.rollingIndexCheckInterval; } public void setRollingIndexCheckInterval(TimeUnitConfiguration rollingIndexCheckInterval) { this.rollingIndexCheckInterval = rollingIndexCheckInterval; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 33 */   public long getShardSizeThreshold() { return this.shardSizeThreshold; } public void setShardSizeThreshold(long shardSizeThreshold) { this.shardSizeThreshold = shardSizeThreshold; } long shardSizeThreshold = 26843545600L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 39 */   public int getTargetIndexLifeInDays() { return this.targetIndexLifeInDays; } public void setTargetIndexLifeInDays(int targetIndexLifeInDays) { this.targetIndexLifeInDays = targetIndexLifeInDays; } int targetIndexLifeInDays = 14;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 44 */   public int getMaximumIndexLifespanInDays() { return this.maximumIndexLifespanInDays; } public void setMaximumIndexLifespanInDays(int maximumIndexLifespanInDays) { this.maximumIndexLifespanInDays = maximumIndexLifespanInDays; } int maximumIndexLifespanInDays = 30;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/configuration/RollingIndexConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */