/*    */ package com.appdynamics.common.util.health;
/*    */ 
/*    */ import com.codahale.metrics.health.HealthCheck.Result;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.TreeSet;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueueCapacityHealthCheck
/*    */   extends SimpleHealthCheck
/*    */ {
/*    */   final double unhealthyRatio;
/*    */   final ConcurrentMap<?, BlockingQueue<?>> queues;
/*    */   
/*    */   public double getUnhealthyRatio()
/*    */   {
/* 25 */     return this.unhealthyRatio;
/*    */   }
/*    */   
/*    */   public QueueCapacityHealthCheck(String name, double unhealthyRatio, ConcurrentMap<?, BlockingQueue<?>> queues)
/*    */   {
/* 30 */     super(name);
/* 31 */     this.unhealthyRatio = unhealthyRatio;
/* 32 */     this.queues = queues;
/*    */   }
/*    */   
/*    */   public HealthCheck.Result check()
/*    */   {
/* 37 */     int totalNum = 0;
/* 38 */     int almostFullNum = 0;
/* 39 */     TreeSet<String> allNames = new TreeSet();
/*    */     
/* 41 */     for (Map.Entry<?, BlockingQueue<?>> entry : this.queues.entrySet()) {
/* 42 */       BlockingQueue<?> queue = (BlockingQueue)entry.getValue();
/* 43 */       int size = queue.size();
/* 44 */       String qName = String.valueOf(entry.getKey());
/* 45 */       int capacity = size + queue.remainingCapacity();
/* 46 */       double ratio = size / capacity;
/* 47 */       if (ratio >= this.unhealthyRatio) {
/* 48 */         almostFullNum++;
/*    */       }
/* 50 */       allNames.add(String.format("[%s] ratio: [%.2f], size: [%d], capacity: [%d]", new Object[] { qName, Double.valueOf(ratio), Integer.valueOf(size), Integer.valueOf(capacity) }));
/*    */       
/* 52 */       totalNum++;
/*    */     }
/*    */     
/* 55 */     if (almostFullNum == 0) {
/* 56 */       return HealthCheck.Result.healthy(String.format("[%d] queues %s", new Object[] { Integer.valueOf(totalNum), allNames }));
/*    */     }
/*    */     
/* 59 */     return HealthCheck.Result.unhealthy(String.format("[%d] out of [%d] queues are above [%.2f] capacity: %s", new Object[] { Integer.valueOf(almostFullNum), Integer.valueOf(totalNum), Double.valueOf(this.unhealthyRatio), allNames }));
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/QueueCapacityHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */