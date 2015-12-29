/*    */ package com.appdynamics.common.util.priority;
/*    */ 
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.RunnableFuture;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.annotation.Nonnull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class PriorityExecutorServiceFactory
/*    */ {
/*    */   public static ThreadPoolExecutor makeThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory)
/*    */   {
/* 22 */     new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory)
/*    */     {
/*    */       protected <V> RunnableFuture<V> newTaskFor(@Nonnull Runnable r, @Nonnull V v) {
/* 25 */         return new PriorityFutureTask(r, v);
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/priority/PriorityExecutorServiceFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */