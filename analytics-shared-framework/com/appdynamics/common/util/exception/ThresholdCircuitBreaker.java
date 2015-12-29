/*    */ package com.appdynamics.common.util.exception;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import javax.annotation.concurrent.ThreadSafe;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ThreadSafe
/*    */ public class ThresholdCircuitBreaker
/*    */   extends CircuitBreaker
/*    */ {
/*    */   final long minErrorCount;
/*    */   final double maxErrorRatio;
/*    */   final long maxErrorCount;
/*    */   
/*    */   ThresholdCircuitBreaker(long minErrorCount, double maxErrorRatio, long maxErrorCount)
/*    */   {
/* 37 */     Preconditions.checkArgument(minErrorCount > 0L, "Min error count should be greater than 0");
/* 38 */     Preconditions.checkArgument(minErrorCount <= maxErrorCount, "Min error count should be less than or equal to max");
/*    */     
/* 40 */     Preconditions.checkArgument(maxErrorRatio > 0.0D, "Min error ratio should be greater than 0.0");
/*    */     
/* 42 */     this.minErrorCount = minErrorCount;
/* 43 */     this.maxErrorRatio = maxErrorRatio;
/* 44 */     this.maxErrorCount = maxErrorCount;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final long getMinErrorCount()
/*    */   {
/* 51 */     return this.minErrorCount;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final double getMaxErrorRatio()
/*    */   {
/* 58 */     return this.maxErrorRatio;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final long getMaxErrorCount()
/*    */   {
/* 65 */     return this.maxErrorCount;
/*    */   }
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
/*    */   protected boolean isCircuitDown(long success, long error)
/*    */   {
/* 80 */     if (error < this.minErrorCount) {
/* 81 */       return false;
/*    */     }
/*    */     
/* 84 */     double pct = error / (success + error);
/* 85 */     if ((error >= this.maxErrorCount) || (pct - this.maxErrorRatio >= 0.001D)) {
/* 86 */       return true;
/*    */     }
/*    */     
/* 89 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/ThresholdCircuitBreaker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */