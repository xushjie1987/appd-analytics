/*     */ package com.appdynamics.common.util.exception;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import javax.annotation.concurrent.NotThreadSafe;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class ResettableCircuitBreaker
/*     */   extends ThresholdCircuitBreaker
/*     */ {
/*  28 */   private static final Logger log = LoggerFactory.getLogger(ResettableCircuitBreaker.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final long downTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final long metricLifeTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long lastMetricCollectionTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long lastDownTime;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isDown;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ResettableCircuitBreaker(long downTime, long metricLifeTime, long minErrorCount, double maxErrorRatio, long maxErrorCount)
/*     */   {
/*  75 */     super(minErrorCount, maxErrorRatio, maxErrorCount);
/*  76 */     Preconditions.checkArgument(downTime > 0L, "downTime must be greater than 0");
/*  77 */     Preconditions.checkArgument(metricLifeTime > 0L, "metricLifeTime must be greater than 0");
/*     */     
/*  79 */     this.downTime = downTime;
/*  80 */     this.metricLifeTime = metricLifeTime;
/*  81 */     this.lastMetricCollectionTime = System.currentTimeMillis();
/*  82 */     this.isDown = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isCircuitDown(long success, long error)
/*     */   {
/* 108 */     long now = System.currentTimeMillis();
/*     */     
/* 110 */     if (now - this.lastMetricCollectionTime >= this.metricLifeTime) {
/* 111 */       resetCounts();
/*     */     }
/*     */     
/* 114 */     if (this.isDown) {
/* 115 */       if (now - this.lastDownTime >= this.downTime) {
/* 116 */         log.info("Circuit breaker down time has elapsed and the circuit breaker is now back up");
/* 117 */         resetCounts();
/* 118 */         this.isDown = false;
/*     */       } else {
/* 120 */         return true;
/*     */       }
/* 122 */     } else if (super.isCircuitDown(success, error)) {
/* 123 */       log.warn("Circuit breaker has gone down as its error threshold was exceeded: success count [{}] error count [{}]", Long.valueOf(success), Long.valueOf(error));
/*     */       
/* 125 */       this.isDown = true;
/* 126 */       this.lastDownTime = System.currentTimeMillis();
/* 127 */       return true;
/*     */     }
/*     */     
/* 130 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetCounts()
/*     */   {
/* 139 */     this.lastMetricCollectionTime = System.currentTimeMillis();
/* 140 */     super.resetCounts();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/ResettableCircuitBreaker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */