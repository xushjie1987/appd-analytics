/*    */ package com.appdynamics.common.util.health;
/*    */ 
/*    */ import com.codahale.metrics.Meter;
/*    */ import com.codahale.metrics.MetricRegistry;
/*    */ import com.codahale.metrics.health.HealthCheck.Result;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import java.text.NumberFormat;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MeteredHealthCheck
/*    */   extends SimpleHealthCheck
/*    */ {
/*    */   public static final double SMALLEST_VISIBLE_RATE = 1.0E-6D;
/*    */   final Meter meterSuccess;
/*    */   final Meter meterUserError;
/*    */   final Meter meterTimeout;
/*    */   final Meter meterError;
/*    */   
/*    */   public MeteredHealthCheck(String name, Environment environment)
/*    */   {
/* 39 */     super(name);
/* 40 */     this.meterSuccess = environment.metrics().meter(MetricRegistry.name(name, new String[] { "success" }));
/* 41 */     this.meterUserError = environment.metrics().meter(MetricRegistry.name(name, new String[] { "user-error" }));
/* 42 */     this.meterTimeout = environment.metrics().meter(MetricRegistry.name(name, new String[] { "timeout" }));
/* 43 */     this.meterError = environment.metrics().meter(MetricRegistry.name(name, new String[] { "error" }));
/*    */   }
/*    */   
/*    */   public final Meter getMeterSuccess() {
/* 47 */     return this.meterSuccess;
/*    */   }
/*    */   
/*    */   public final Meter getMeterUserError() {
/* 51 */     return this.meterUserError;
/*    */   }
/*    */   
/*    */   public final Meter getMeterTimeout() {
/* 55 */     return this.meterTimeout;
/*    */   }
/*    */   
/*    */   public final Meter getMeterError() {
/* 59 */     return this.meterError;
/*    */   }
/*    */   
/*    */   public static double truncateRate(double d) {
/* 63 */     return d < 1.0E-6D ? 0.0D : d;
/*    */   }
/*    */   
/*    */   public HealthCheck.Result check()
/*    */   {
/* 68 */     NumberFormat numberFormat = NumberFormat.getNumberInstance();
/* 69 */     numberFormat.setMaximumFractionDigits(6);
/* 70 */     numberFormat.setMinimumFractionDigits(6);
/*    */     
/*    */ 
/* 73 */     String success = numberFormat.format(truncateRate(this.meterSuccess.getFiveMinuteRate()));
/* 74 */     String userError = numberFormat.format(truncateRate(this.meterUserError.getFiveMinuteRate()));
/* 75 */     String timeout = numberFormat.format(truncateRate(this.meterTimeout.getFiveMinuteRate()));
/* 76 */     String error = numberFormat.format(truncateRate(this.meterError.getFiveMinuteRate()));
/*    */     
/* 78 */     String msg = String.format("Rates (Avg per second. Avg of last 5 min) success: [%s], user error: [%s], timeout: [%s], error: [%s]", new Object[] { success, userError, timeout, error });
/*    */     
/*    */ 
/*    */ 
/* 82 */     return HealthCheck.Result.healthy(msg);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/MeteredHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */