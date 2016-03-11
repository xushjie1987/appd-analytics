/*    */ package com.appdynamics.analytics.processor.event.meter;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QuotaRemaining
/*    */ {
/*    */   long accountQuotaRemaining;
/*    */   long eventTypeQuotaRemaining;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 19 */     if (o == this) return true; if (!(o instanceof QuotaRemaining)) return false; QuotaRemaining other = (QuotaRemaining)o; if (!other.canEqual(this)) return false; if (getAccountQuotaRemaining() != other.getAccountQuotaRemaining()) return false; return getEventTypeQuotaRemaining() == other.getEventTypeQuotaRemaining(); } public boolean canEqual(Object other) { return other instanceof QuotaRemaining; } public int hashCode() { int PRIME = 31;int result = 1;long $accountQuotaRemaining = getAccountQuotaRemaining();result = result * 31 + (int)($accountQuotaRemaining >>> 32 ^ $accountQuotaRemaining);long $eventTypeQuotaRemaining = getEventTypeQuotaRemaining();result = result * 31 + (int)($eventTypeQuotaRemaining >>> 32 ^ $eventTypeQuotaRemaining);return result; } public String toString() { return "QuotaRemaining(accountQuotaRemaining=" + getAccountQuotaRemaining() + ", eventTypeQuotaRemaining=" + getEventTypeQuotaRemaining() + ")"; } @ConstructorProperties({"accountQuotaRemaining", "eventTypeQuotaRemaining"})
/* 20 */   public QuotaRemaining(long accountQuotaRemaining, long eventTypeQuotaRemaining) { this.accountQuotaRemaining = accountQuotaRemaining;this.eventTypeQuotaRemaining = eventTypeQuotaRemaining; }
/*    */   
/* 22 */   public long getAccountQuotaRemaining() { return this.accountQuotaRemaining; } public void setAccountQuotaRemaining(long accountQuotaRemaining) { this.accountQuotaRemaining = accountQuotaRemaining; }
/* 23 */   public long getEventTypeQuotaRemaining() { return this.eventTypeQuotaRemaining; } public void setEventTypeQuotaRemaining(long eventTypeQuotaRemaining) { this.eventTypeQuotaRemaining = eventTypeQuotaRemaining; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   boolean hasQuotaRemaining()
/*    */   {
/* 30 */     return getLeastQuotaRemaining() > 0L;
/*    */   }
/*    */   
/*    */   long getLeastQuotaRemaining() {
/* 34 */     return Math.min(this.accountQuotaRemaining, this.eventTypeQuotaRemaining);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/QuotaRemaining.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */