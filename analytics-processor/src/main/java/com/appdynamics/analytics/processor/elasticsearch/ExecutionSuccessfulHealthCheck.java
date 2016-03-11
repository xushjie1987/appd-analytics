/*    */ package com.appdynamics.analytics.processor.elasticsearch;
/*    */ 
/*    */ import com.appdynamics.common.util.health.SimpleHealthCheck;
/*    */ import com.codahale.metrics.health.HealthCheck.Result;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExecutionSuccessfulHealthCheck
/*    */   extends SimpleHealthCheck
/*    */ {
/*    */   private final AtomicBoolean isRunning;
/*    */   private final AtomicBoolean lastExecutionSuccessful;
/*    */   
/*    */   public ExecutionSuccessfulHealthCheck(String name, AtomicBoolean isRunning, AtomicBoolean lastExecutionSuccessful)
/*    */   {
/* 23 */     super(name);
/*    */     
/* 25 */     this.isRunning = isRunning;
/* 26 */     this.lastExecutionSuccessful = lastExecutionSuccessful;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HealthCheck.Result check()
/*    */   {
/* 35 */     String message = String.format("Healthcheck for [%s]: Is running flag is: [%s], last execution successful flag is: [%s]", new Object[] { super.getName(), Boolean.valueOf(this.isRunning.get()), Boolean.valueOf(this.lastExecutionSuccessful.get()) });
/*    */     
/*    */ 
/*    */ 
/* 39 */     return HealthCheck.Result.healthy(message);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/ExecutionSuccessfulHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */