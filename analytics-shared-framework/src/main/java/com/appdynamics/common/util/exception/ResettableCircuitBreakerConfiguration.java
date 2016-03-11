/*    */ package com.appdynamics.common.util.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResettableCircuitBreakerConfiguration
/*    */ {
/*    */   @NotNull
/*    */   @Valid
/*    */   TimeUnitConfiguration downTime;
/*    */   @NotNull
/*    */   @Valid
/*    */   TimeUnitConfiguration lifeTime;
/*    */   @NotNull
/*    */   @Valid
/*    */   ThresholdCircuitBreakerConfiguration threshold;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 24 */     if (o == this) return true; if (!(o instanceof ResettableCircuitBreakerConfiguration)) return false; ResettableCircuitBreakerConfiguration other = (ResettableCircuitBreakerConfiguration)o; if (!other.canEqual(this)) return false; Object this$downTime = getDownTime();Object other$downTime = other.getDownTime(); if (this$downTime == null ? other$downTime != null : !this$downTime.equals(other$downTime)) return false; Object this$lifeTime = getLifeTime();Object other$lifeTime = other.getLifeTime(); if (this$lifeTime == null ? other$lifeTime != null : !this$lifeTime.equals(other$lifeTime)) return false; Object this$threshold = getThreshold();Object other$threshold = other.getThreshold();return this$threshold == null ? other$threshold == null : this$threshold.equals(other$threshold); } public boolean canEqual(Object other) { return other instanceof ResettableCircuitBreakerConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $downTime = getDownTime();result = result * 31 + ($downTime == null ? 0 : $downTime.hashCode());Object $lifeTime = getLifeTime();result = result * 31 + ($lifeTime == null ? 0 : $lifeTime.hashCode());Object $threshold = getThreshold();result = result * 31 + ($threshold == null ? 0 : $threshold.hashCode());return result; } public String toString() { return "ResettableCircuitBreakerConfiguration(downTime=" + getDownTime() + ", lifeTime=" + getLifeTime() + ", threshold=" + getThreshold() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 32 */   public TimeUnitConfiguration getDownTime() { return this.downTime; } public void setDownTime(TimeUnitConfiguration downTime) { this.downTime = downTime; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */   public TimeUnitConfiguration getLifeTime() { return this.lifeTime; } public void setLifeTime(TimeUnitConfiguration lifeTime) { this.lifeTime = lifeTime; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 48 */   public ThresholdCircuitBreakerConfiguration getThreshold() { return this.threshold; } public void setThreshold(ThresholdCircuitBreakerConfiguration threshold) { this.threshold = threshold; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/ResettableCircuitBreakerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */