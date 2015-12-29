/*    */ package com.appdynamics.analytics.processor.event.relevantfields;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogisticScorer
/*    */   extends AbstractScorer
/*    */ {
/*    */   private static final double SIGMOID_K = 1.8D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LogisticScorer(double min, double max)
/*    */   {
/* 20 */     super(min, max);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public LogisticScorer()
/*    */   {
/* 27 */     super(0.0D, 100.0D);
/*    */   }
/*    */   
/*    */   public double normalize(double score)
/*    */   {
/* 32 */     double logit = sigmoidFunction(transform(score));
/* 33 */     double normalized = (logit * 2.0D - 1.0D) * (this.max - this.min) + this.min;
/* 34 */     if (normalized > this.max) {
/* 35 */       return this.max;
/*    */     }
/* 37 */     return normalized;
/*    */   }
/*    */   
/*    */   private double transform(double x)
/*    */   {
/* 42 */     double y = Math.abs(x);
/* 43 */     return Math.cbrt(y);
/*    */   }
/*    */   
/*    */   private double sigmoidFunction(double x) {
/* 47 */     return 1.0D / (1.0D + Math.exp(-x * 1.8D));
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/relevantfields/LogisticScorer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */