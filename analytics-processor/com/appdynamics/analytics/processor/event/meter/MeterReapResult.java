/*    */ package com.appdynamics.analytics.processor.event.meter;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
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
/*    */ class MeterReapResult
/*    */ {
/*    */   final long usage;
/*    */   final long excess;
/*    */   
/* 20 */   public long getUsage() { return this.usage; }
/* 21 */   public long getExcess() { return this.excess; }
/*    */   
/*    */   public MeterReapResult(long usage, long excess) {
/* 24 */     Preconditions.checkArgument((usage >= 0L) && (excess >= 0L), "Usage & Excess should be positive or equal to zero");
/* 25 */     this.usage = usage;
/* 26 */     this.excess = excess;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/MeterReapResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */