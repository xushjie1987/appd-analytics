/*    */ package com.appdynamics.analytics.processor.event.configuration;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ import javax.validation.constraints.Min;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventTypeMetaDataDefaults
/*    */ {
/*    */   @Min(1L)
/*    */   Integer hotLifespanInDays;
/*    */   @Min(0L)
/*    */   Long lookBackTimePeriodSeconds;
/*    */   
/*    */   @ConstructorProperties({"hotLifespanInDays", "lookBackTimePeriodSeconds"})
/*    */   public EventTypeMetaDataDefaults(Integer hotLifespanInDays, Long lookBackTimePeriodSeconds)
/*    */   {
/* 20 */     this.hotLifespanInDays = hotLifespanInDays;this.lookBackTimePeriodSeconds = lookBackTimePeriodSeconds; }
/*    */   
/* 22 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof EventTypeMetaDataDefaults)) return false; EventTypeMetaDataDefaults other = (EventTypeMetaDataDefaults)o; if (!other.canEqual(this)) return false; Object this$hotLifespanInDays = getHotLifespanInDays();Object other$hotLifespanInDays = other.getHotLifespanInDays(); if (this$hotLifespanInDays == null ? other$hotLifespanInDays != null : !this$hotLifespanInDays.equals(other$hotLifespanInDays)) return false; Object this$lookBackTimePeriodSeconds = getLookBackTimePeriodSeconds();Object other$lookBackTimePeriodSeconds = other.getLookBackTimePeriodSeconds();return this$lookBackTimePeriodSeconds == null ? other$lookBackTimePeriodSeconds == null : this$lookBackTimePeriodSeconds.equals(other$lookBackTimePeriodSeconds); } public boolean canEqual(Object other) { return other instanceof EventTypeMetaDataDefaults; } public int hashCode() { int PRIME = 31;int result = 1;Object $hotLifespanInDays = getHotLifespanInDays();result = result * 31 + ($hotLifespanInDays == null ? 0 : $hotLifespanInDays.hashCode());Object $lookBackTimePeriodSeconds = getLookBackTimePeriodSeconds();result = result * 31 + ($lookBackTimePeriodSeconds == null ? 0 : $lookBackTimePeriodSeconds.hashCode());return result; } public String toString() { return "EventTypeMetaDataDefaults(hotLifespanInDays=" + getHotLifespanInDays() + ", lookBackTimePeriodSeconds=" + getLookBackTimePeriodSeconds() + ")"; }
/*    */   
/*    */ 
/*    */ 
/* 26 */   public Integer getHotLifespanInDays() { return this.hotLifespanInDays; } public void setHotLifespanInDays(Integer hotLifespanInDays) { this.hotLifespanInDays = hotLifespanInDays; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 32 */   public Long getLookBackTimePeriodSeconds() { return this.lookBackTimePeriodSeconds; } public void setLookBackTimePeriodSeconds(Long lookBackTimePeriodSeconds) { this.lookBackTimePeriodSeconds = lookBackTimePeriodSeconds; }
/*    */   
/*    */   public EventTypeMetaDataDefaults() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/configuration/EventTypeMetaDataDefaults.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */