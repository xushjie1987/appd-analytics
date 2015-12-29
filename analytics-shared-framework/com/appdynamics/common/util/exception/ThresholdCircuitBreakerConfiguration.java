/*    */ package com.appdynamics.common.util.exception;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.math.BigDecimal;
/*    */ import javax.validation.constraints.DecimalMin;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThresholdCircuitBreakerConfiguration
/*    */ {
/*    */   public static final long DEFAULT_MIN_ERROR_COUNT = 1000L;
/*    */   public static final String DEFAULT_ERROR_RATIO = "0.75";
/*    */   public static final long DEFAULT_MAX_ERROR_COUNT = 2000L;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 19 */     if (o == this) return true; if (!(o instanceof ThresholdCircuitBreakerConfiguration)) return false; ThresholdCircuitBreakerConfiguration other = (ThresholdCircuitBreakerConfiguration)o; if (!other.canEqual(this)) return false; if (getMinErrorCount() != other.getMinErrorCount()) return false; Object this$maxErrorRatio = getMaxErrorRatio();Object other$maxErrorRatio = other.getMaxErrorRatio(); if (this$maxErrorRatio == null ? other$maxErrorRatio != null : !this$maxErrorRatio.equals(other$maxErrorRatio)) return false; return getMaxErrorCount() == other.getMaxErrorCount(); } public boolean canEqual(Object other) { return other instanceof ThresholdCircuitBreakerConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;long $minErrorCount = getMinErrorCount();result = result * 31 + (int)($minErrorCount >>> 32 ^ $minErrorCount);Object $maxErrorRatio = getMaxErrorRatio();result = result * 31 + ($maxErrorRatio == null ? 0 : $maxErrorRatio.hashCode());long $maxErrorCount = getMaxErrorCount();result = result * 31 + (int)($maxErrorCount >>> 32 ^ $maxErrorCount);return result; } public String toString() { return "ThresholdCircuitBreakerConfiguration(minErrorCount=" + getMinErrorCount() + ", maxErrorRatio=" + getMaxErrorRatio() + ", maxErrorCount=" + getMaxErrorCount() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */   @JsonProperty
/*    */   @Min(1L)
/* 25 */   long minErrorCount = 1000L;
/*    */   
/* 27 */   public long getMinErrorCount() { return this.minErrorCount; } public void setMinErrorCount(long minErrorCount) { this.minErrorCount = minErrorCount; } @JsonProperty
/*    */   @DecimalMin("0.75")
/*    */   @NotNull
/* 29 */   BigDecimal maxErrorRatio = new BigDecimal("0.75");
/*    */   
/*    */ 
/* 32 */   public BigDecimal getMaxErrorRatio() { return this.maxErrorRatio; } public void setMaxErrorRatio(BigDecimal maxErrorRatio) { this.maxErrorRatio = maxErrorRatio; } @JsonProperty
/*    */   @Min(1L)
/* 34 */   long maxErrorCount = 2000L;
/*    */   
/* 36 */   public long getMaxErrorCount() { return this.maxErrorCount; } public void setMaxErrorCount(long maxErrorCount) { this.maxErrorCount = maxErrorCount; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/ThresholdCircuitBreakerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */