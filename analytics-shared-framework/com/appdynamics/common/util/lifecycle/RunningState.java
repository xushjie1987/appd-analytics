/*    */ package com.appdynamics.common.util.lifecycle;
/*    */ 
/*    */ import com.appdynamics.common.util.concurrent.ManualResetEvent;
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
/*    */ public class RunningState
/*    */ {
/*    */   private final ManualResetEvent runningEvent;
/*    */   
/*    */   public RunningState(boolean initial)
/*    */   {
/* 22 */     this.runningEvent = new ManualResetEvent(!initial);
/*    */   }
/*    */   
/*    */   public void set(boolean running) {
/* 26 */     if (running) {
/* 27 */       this.runningEvent.clear();
/*    */     } else {
/* 29 */       this.runningEvent.signal();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean get() {
/* 34 */     return !this.runningEvent.get();
/*    */   }
/*    */   
/*    */   public void sleepWhileRunning(long milliseconds) throws InterruptedException {
/* 38 */     this.runningEvent.waitForSingleEvent(milliseconds);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/lifecycle/RunningState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */