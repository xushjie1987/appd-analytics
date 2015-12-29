/*    */ package com.appdynamics.common.util.health;
/*    */ 
/*    */ import com.codahale.metrics.health.HealthCheck;
/*    */ import com.codahale.metrics.health.HealthCheck.Result;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Throwables;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SimpleHealthCheck
/*    */   extends HealthCheck
/*    */   implements HealthCheckable
/*    */ {
/*    */   final String name;
/*    */   
/*    */   protected SimpleHealthCheck(String name)
/*    */   {
/* 21 */     Preconditions.checkNotNull(name);
/* 22 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 26 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract HealthCheck.Result check();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static HealthCheck.Result callAndCheck(Runnable runnable)
/*    */   {
/*    */     try
/*    */     {
/* 45 */       runnable.run();
/* 46 */       return HealthCheck.Result.healthy();
/*    */     } catch (Throwable t) {
/* 48 */       Throwable root = Throwables.getRootCause(t);
/* 49 */       String message = root.getMessage();
/* 50 */       if (message == null) {
/* 51 */         message = Throwables.getStackTraceAsString(root);
/*    */       }
/* 53 */       return HealthCheck.Result.unhealthy(message);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/SimpleHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */