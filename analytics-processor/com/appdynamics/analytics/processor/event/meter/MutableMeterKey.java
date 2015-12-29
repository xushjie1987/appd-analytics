/*    */ package com.appdynamics.analytics.processor.event.meter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MutableMeterKey
/*    */   extends MeterKey
/*    */ {
/*    */   private String accountName;
/*    */   
/*    */ 
/*    */   private String eventType;
/*    */   
/*    */ 
/*    */   private String meterType;
/*    */   
/*    */   private long time;
/*    */   
/*    */   private boolean persistable;
/*    */   
/*    */   private int hashCode;
/*    */   
/*    */ 
/*    */   public MutableMeterKey(String accountName, String eventType, String meterType, long time, boolean persistable)
/*    */   {
/* 25 */     reuse(accountName, eventType, meterType, time, persistable);
/*    */   }
/*    */   
/*    */   final void reuse(String accountKey, String eventType, String meterType, long time, boolean persistable) {
/* 29 */     this.accountName = accountKey;
/* 30 */     this.eventType = eventType;
/* 31 */     this.meterType = meterType;
/* 32 */     this.time = time;
/* 33 */     this.persistable = persistable;
/* 34 */     this.hashCode = hashCode(this);
/*    */   }
/*    */   
/*    */   public String getAccountName()
/*    */   {
/* 39 */     return this.accountName;
/*    */   }
/*    */   
/*    */   public String getEventType()
/*    */   {
/* 44 */     return this.eventType;
/*    */   }
/*    */   
/*    */   public String getMeterType()
/*    */   {
/* 49 */     return this.meterType;
/*    */   }
/*    */   
/*    */   public long getTimeWindowStart()
/*    */   {
/* 54 */     return this.time;
/*    */   }
/*    */   
/*    */   boolean isPersistable()
/*    */   {
/* 59 */     return this.persistable;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 64 */     return this.hashCode;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/MutableMeterKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */