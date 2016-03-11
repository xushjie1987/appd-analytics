/*    */ package com.appdynamics.analytics.processor.event.meter;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ public abstract class MeterKey
/*    */ {
/*    */   abstract String getAccountName();
/*    */   
/*    */   abstract String getEventType();
/*    */   
/*    */   abstract String getMeterType();
/*    */   
/*    */   abstract long getTimeWindowStart();
/*    */   
/*    */   abstract boolean isPersistable();
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 29 */     if (this == o) {
/* 30 */       return true;
/*    */     }
/* 32 */     if (!(o instanceof MeterKey)) {
/* 33 */       return false;
/*    */     }
/* 35 */     MeterKey that = (MeterKey)o;
/*    */     
/* 37 */     return (Objects.equals(getAccountName(), that.getAccountName())) && (Objects.equals(getEventType(), that.getEventType())) && (Objects.equals(getMeterType(), that.getMeterType())) && (getTimeWindowStart() == that.getTimeWindowStart());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 45 */     return hashCode(this);
/*    */   }
/*    */   
/*    */   static int hashCode(MeterKey thisKey) {
/* 49 */     return Objects.hash(new Object[] { thisKey.getAccountName(), thisKey.getEventType(), thisKey.getMeterType(), Long.valueOf(thisKey.getTimeWindowStart()) });
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/MeterKey.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */