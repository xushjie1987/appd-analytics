/*    */ package com.appdynamics.common.util.priority;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ 
/*    */ public abstract class PriorityRunnable
/*    */   implements Comparable<PriorityRunnable>, Runnable
/*    */ {
/*    */   long priorityValue;
/*    */   
/*    */   @ConstructorProperties({"priorityValue"})
/*    */   public PriorityRunnable(long priorityValue)
/*    */   {
/* 14 */     this.priorityValue = 0L;this.priorityValue = priorityValue; } public PriorityRunnable() { this.priorityValue = 0L; }
/*    */   
/*    */   public abstract boolean equals(Object paramObject);
/*    */   
/*    */   public abstract int hashCode();
/*    */   
/* 20 */   public int compareTo(PriorityRunnable other) { long diff = this.priorityValue - other.priorityValue;
/* 21 */     if (diff == 0L)
/* 22 */       return 0;
/* 23 */     if (diff > 0L) {
/* 24 */       return 1;
/*    */     }
/* 26 */     return -1;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/priority/PriorityRunnable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */