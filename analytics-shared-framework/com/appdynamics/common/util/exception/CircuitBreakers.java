/*    */ package com.appdynamics.common.util.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.math.BigDecimal;
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
/*    */ 
/*    */ public abstract class CircuitBreakers
/*    */ {
/*    */   public static CircuitBreaker newCircuitBreaker()
/*    */   {
/* 30 */     return new CircuitBreaker();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ResettableCircuitBreaker newCircuitBreaker(ResettableCircuitBreakerConfiguration config)
/*    */   {
/* 39 */     TimeUnitConfiguration downTime = config.getDownTime();
/* 40 */     TimeUnitConfiguration metricLifeTime = config.getLifeTime();
/* 41 */     ThresholdCircuitBreakerConfiguration thresholdConfig = config.getThreshold();
/* 42 */     return new ResettableCircuitBreaker(downTime.toMilliseconds(), metricLifeTime.toMilliseconds(), thresholdConfig.getMinErrorCount(), thresholdConfig.getMaxErrorRatio().doubleValue(), thresholdConfig.getMaxErrorCount());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ThresholdCircuitBreaker newCircuitBreaker(ThresholdCircuitBreakerConfiguration cfg)
/*    */   {
/* 55 */     return new ThresholdCircuitBreaker(cfg.getMinErrorCount(), cfg.getMaxErrorRatio().doubleValue(), cfg.getMaxErrorCount());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ThresholdCircuitBreaker newCircuitBreaker(double maxErrorRatio)
/*    */   {
/* 66 */     return new ThresholdCircuitBreaker(1L, maxErrorRatio, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ThresholdCircuitBreaker newCircuitBreaker(long minErrorCount, double maxErrorRatio, long maxErrorCount)
/*    */   {
/* 79 */     return new ThresholdCircuitBreaker(minErrorCount, maxErrorRatio, maxErrorCount);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/CircuitBreakers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */