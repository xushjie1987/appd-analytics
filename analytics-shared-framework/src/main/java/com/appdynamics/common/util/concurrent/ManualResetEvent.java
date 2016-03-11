/*    */ package com.appdynamics.common.util.concurrent;
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
/*    */ public class ManualResetEvent
/*    */ {
/* 20 */   private final Object monitor = new Object();
/* 21 */   private volatile boolean signaled = false;
/*    */   
/*    */   public ManualResetEvent(boolean signaled) {
/* 24 */     this.signaled = signaled;
/*    */   }
/*    */   
/*    */ 
/*    */   public void waitForSingleEvent()
/*    */     throws InterruptedException
/*    */   {
/* 31 */     synchronized (this.monitor) {
/* 32 */       while (!this.signaled) {
/* 33 */         this.monitor.wait();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean waitForSingleEvent(long milliseconds)
/*    */     throws InterruptedException
/*    */   {
/* 42 */     synchronized (this.monitor) {
/* 43 */       if (this.signaled) {
/* 44 */         return true;
/*    */       }
/* 46 */       this.monitor.wait(milliseconds);
/* 47 */       return this.signaled;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void signal()
/*    */   {
/* 55 */     synchronized (this.monitor) {
/* 56 */       this.signaled = true;
/* 57 */       this.monitor.notifyAll();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void clear()
/*    */   {
/* 65 */     this.signaled = false;
/*    */   }
/*    */   
/*    */   public boolean get() {
/* 69 */     return this.signaled;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/concurrent/ManualResetEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */