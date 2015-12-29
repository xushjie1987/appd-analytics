/*    */ package com.appdynamics.analytics.processor.event.relevantfields;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractScorer
/*    */ {
/*    */   protected final double min;
/*    */   
/*    */ 
/*    */ 
/*    */   protected final double max;
/*    */   
/*    */ 
/*    */ 
/*    */   protected AbstractScorer(double min, double max)
/*    */   {
/* 18 */     if (min > max) {
/* 19 */       throw new RuntimeException("min [" + min + "] cannot be greater than max [" + max + "]");
/*    */     }
/* 21 */     this.min = min;
/* 22 */     this.max = max;
/*    */   }
/*    */   
/*    */   public abstract double normalize(double paramDouble);
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/relevantfields/AbstractScorer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */