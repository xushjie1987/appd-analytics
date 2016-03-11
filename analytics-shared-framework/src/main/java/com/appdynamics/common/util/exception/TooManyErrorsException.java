/*    */ package com.appdynamics.common.util.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TooManyErrorsException
/*    */   extends RuntimeException
/*    */ {
/*    */   final long successCount;
/*    */   
/*    */ 
/*    */   final long errorCount;
/*    */   
/*    */ 
/*    */ 
/*    */   public TooManyErrorsException(long successCount, long errorCount)
/*    */   {
/* 18 */     super("[successCount=" + successCount + ", errorCount=" + errorCount + ']');
/* 19 */     this.successCount = successCount;
/* 20 */     this.errorCount = errorCount;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final Throwable fillInStackTrace()
/*    */   {
/* 30 */     return this;
/*    */   }
/*    */   
/*    */   public long getSuccessCount() {
/* 34 */     return this.successCount;
/*    */   }
/*    */   
/*    */   public long getErrorCount() {
/* 38 */     return this.errorCount;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/TooManyErrorsException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */