/*    */ package com.appdynamics.common.util.health;
/*    */ 
/*    */ import com.appdynamics.common.util.lifecycle.RunningState;
/*    */ import com.codahale.metrics.health.HealthCheck.Result;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RunningStateHealthCheck
/*    */   extends SimpleHealthCheck
/*    */ {
/*    */   private volatile RunningState runningState;
/*    */   
/*    */   public RunningStateHealthCheck(String name, RunningState runningState)
/*    */   {
/* 19 */     super(name);
/*    */     
/* 21 */     this.runningState = runningState;
/*    */   }
/*    */   
/*    */   public HealthCheck.Result check()
/*    */   {
/* 26 */     return this.runningState.get() ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy("Not running");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/RunningStateHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */