/*    */ package com.appdynamics.common.util.concurrent;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public class SingleWriterCounter
/*    */ {
/*    */   final long resetAtValue;
/*    */   private final AtomicLong counter;
/*    */   
/*    */   public SingleWriterCounter()
/*    */   {
/* 24 */     this(0L, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public SingleWriterCounter(long startingValue, long resetAtValue) {
/* 28 */     this.counter = new AtomicLong(startingValue);
/* 29 */     this.resetAtValue = resetAtValue;
/*    */   }
/*    */   
/*    */   public final long getResetAtValue() {
/* 33 */     return this.resetAtValue;
/*    */   }
/*    */   
/*    */   public final long get() {
/* 37 */     return this.counter.get();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final long incrementAndGet()
/*    */   {
/* 45 */     long l = this.counter.get();
/*    */     
/* 47 */     l = l == this.resetAtValue ? 0L : l;
/* 48 */     l += 1L;
/*    */     
/* 50 */     this.counter.lazySet(l);
/*    */     
/* 52 */     return l;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/concurrent/SingleWriterCounter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */