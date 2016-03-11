/*    */ package com.appdynamics.common.util.execution;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import javax.validation.constraints.Min;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RetryConfiguration
/*    */ {
/*    */   @Min(1L)
/*    */   final int totalAttempts;
/*    */   @Min(1L)
/*    */   final long retryPauseMillis;
/*    */   
/*    */   public int getTotalAttempts()
/*    */   {
/* 22 */     return this.totalAttempts;
/*    */   }
/*    */   
/* 25 */   public long getRetryPauseMillis() { return this.retryPauseMillis; }
/*    */   
/*    */   public RetryConfiguration() {
/* 28 */     this.totalAttempts = 1;
/* 29 */     this.retryPauseMillis = 1L;
/*    */   }
/*    */   
/*    */   @JsonCreator
/*    */   public RetryConfiguration(@JsonProperty("totalAttempts") int totalAttempts, @JsonProperty("retryPauseMillis") long retryPauseMillis)
/*    */   {
/* 35 */     this.totalAttempts = totalAttempts;
/* 36 */     this.retryPauseMillis = retryPauseMillis;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/execution/RetryConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */