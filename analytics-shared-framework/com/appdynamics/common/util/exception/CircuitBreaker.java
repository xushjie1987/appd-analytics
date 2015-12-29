/*     */ package com.appdynamics.common.util.exception;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class CircuitBreaker
/*     */ {
/*     */   private final AtomicLong successCount;
/*     */   private final AtomicLong errorCount;
/*     */   
/*     */   CircuitBreaker()
/*     */   {
/*  25 */     this.successCount = new AtomicLong();
/*  26 */     this.errorCount = new AtomicLong();
/*     */   }
/*     */   
/*     */   public final long getSuccessCount() {
/*  30 */     return this.successCount.get();
/*     */   }
/*     */   
/*     */   public final long getErrorCount() {
/*  34 */     return this.errorCount.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final long addSuccessCount(long count)
/*     */   {
/*  44 */     return this.successCount.addAndGet(count);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final long addErrorCount(long count)
/*     */   {
/*  55 */     long success = this.successCount.get();
/*  56 */     long error = this.errorCount.addAndGet(count);
/*  57 */     checkCircuit(success, error);
/*     */     
/*  59 */     return error;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resetCounts()
/*     */   {
/*  66 */     this.successCount.set(0L);
/*  67 */     this.errorCount.set(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void checkCircuit()
/*     */   {
/*  94 */     checkCircuit(getSuccessCount(), getErrorCount());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkCircuit(long success, long error)
/*     */   {
/* 106 */     if (isCircuitDown(success, error)) {
/* 107 */       throw new TooManyErrorsException(success, error);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isCircuitDown(long success, long error)
/*     */   {
/* 119 */     return error > success;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/CircuitBreaker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */