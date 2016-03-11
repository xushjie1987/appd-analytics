/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import org.joda.time.DateTime;
/*    */ import org.joda.time.DateTimeZone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultTiming
/*    */   implements Timing
/*    */ {
/*    */   public long currentTimeMillis()
/*    */   {
/* 18 */     return DateTimeZone.UTC.convertLocalToUTC(System.currentTimeMillis(), false);
/*    */   }
/*    */   
/*    */   public DateTime currentDateTime()
/*    */   {
/* 23 */     return new DateTime(DateTimeZone.UTC);
/*    */   }
/*    */   
/*    */   public void sleep(long time) throws InterruptedException
/*    */   {
/* 28 */     Thread.sleep(time);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/DefaultTiming.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */