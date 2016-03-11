/*    */ package com.appdynamics.analytics.processor.event.meter;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import javax.annotation.Nullable;
/*    */ import javax.annotation.concurrent.ThreadSafe;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ThreadSafe
/*    */ public abstract class Meters
/*    */ {
/*    */   final ConcurrentHashMap<MeterKey, Meter> meters;
/*    */   
/*    */   Meters()
/*    */   {
/* 23 */     this.meters = new ConcurrentHashMap();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   ConcurrentMap<MeterKey, Meter> getMeters()
/*    */   {
/* 30 */     return this.meters;
/*    */   }
/*    */   
/*    */   public abstract long updateAndCheckBytes(String paramString1, String paramString2, long paramLong);
/*    */   
/*    */   public abstract long updateAndCheckDocumentFragments(String paramString1, String paramString2, long paramLong);
/*    */   
/*    */   public abstract long updateAndCheckSearchThreshold(String paramString1, @Nullable String paramString2);
/*    */   
/*    */   public abstract long getDailyBytesLimit(String paramString1, String paramString2);
/*    */   
/*    */   public abstract long getDailyDocumentsLimit(String paramString1, String paramString2);
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/Meters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */