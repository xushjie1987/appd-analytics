/*    */ package com.appdynamics.common.util.priority;
/*    */ 
/*    */ import java.util.concurrent.FutureTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PriorityFutureTask<V>
/*    */   extends FutureTask<V>
/*    */   implements Comparable<Runnable>
/*    */ {
/*    */   private PriorityRunnable priorityRunnable;
/*    */   
/*    */   public PriorityRunnable getPriorityRunnable()
/*    */   {
/* 16 */     return this.priorityRunnable;
/*    */   }
/*    */   
/*    */   public PriorityFutureTask(Runnable runnable, V result) {
/* 20 */     super(runnable, result);
/* 21 */     this.priorityRunnable = ((PriorityRunnable)runnable);
/*    */   }
/*    */   
/*    */   public int compareTo(Runnable other)
/*    */   {
/* 26 */     PriorityFutureTask otherPriorityFutureTask = (PriorityFutureTask)other;
/* 27 */     return this.priorityRunnable.compareTo(otherPriorityFutureTask.getPriorityRunnable());
/*    */   }
/*    */   
/*    */   public boolean equals(Object other)
/*    */   {
/* 32 */     if ((other == null) || (!(other instanceof PriorityFutureTask))) {
/* 33 */       return false;
/*    */     }
/*    */     
/* 36 */     PriorityFutureTask otherPriorityFutureTask = (PriorityFutureTask)other;
/* 37 */     return this.priorityRunnable.equals(otherPriorityFutureTask.getPriorityRunnable());
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 42 */     return this.priorityRunnable.hashCode();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/priority/PriorityFutureTask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */