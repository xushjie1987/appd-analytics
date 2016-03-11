/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import com.google.common.base.Throwables;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import java.util.concurrent.locks.ReentrantLock;
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
/*    */ public class SingleNodeClusterLock
/*    */   implements ClusterLock
/*    */ {
/* 25 */   private final Lock lock = new ReentrantLock();
/*    */   
/*    */   public <T> T acquireAndExecute(String lockId, long time, TimeUnit unit, Callable<? extends T> callable)
/*    */   {
/*    */     try
/*    */     {
/* 31 */       if (this.lock.tryLock(time, unit)) {
/*    */         try {
/* 33 */           return (T)callable.call();
/*    */         } finally {
/* 35 */           this.lock.unlock();
/*    */         }
/*    */       }
/*    */       
/* 39 */       throw new TimeoutException("Unable to acquire lock within specified time period of " + time + " " + unit);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 43 */       throw Throwables.propagate(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/SingleNodeClusterLock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */