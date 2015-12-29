/*    */ package com.appdynamics.analytics.processor.event.configuration;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ import javax.validation.constraints.Min;
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
/*    */ 
/*    */ public class EventServiceConfiguration
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 21 */     if (o == this) return true; if (!(o instanceof EventServiceConfiguration)) return false; EventServiceConfiguration other = (EventServiceConfiguration)o; if (!other.canEqual(this)) return false; Object this$eventTypeMetaDataDefaults = getEventTypeMetaDataDefaults();Object other$eventTypeMetaDataDefaults = other.getEventTypeMetaDataDefaults(); if (this$eventTypeMetaDataDefaults == null ? other$eventTypeMetaDataDefaults != null : !this$eventTypeMetaDataDefaults.equals(other$eventTypeMetaDataDefaults)) return false; if (getUpdateConflictRetries() != other.getUpdateConflictRetries()) return false; if (getEventTypeCacheSize() != other.getEventTypeCacheSize()) return false; if (getEventTypeCacheTtlMinutes() != other.getEventTypeCacheTtlMinutes()) return false; if (getEventTypeMetaDataCacheTtlMinutes() != other.getEventTypeMetaDataCacheTtlMinutes()) return false; return isAllFieldDisabled() == other.isAllFieldDisabled(); } public boolean canEqual(Object other) { return other instanceof EventServiceConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $eventTypeMetaDataDefaults = getEventTypeMetaDataDefaults();result = result * 31 + ($eventTypeMetaDataDefaults == null ? 0 : $eventTypeMetaDataDefaults.hashCode());result = result * 31 + getUpdateConflictRetries();result = result * 31 + getEventTypeCacheSize();result = result * 31 + getEventTypeCacheTtlMinutes();result = result * 31 + getEventTypeMetaDataCacheTtlMinutes();result = result * 31 + (isAllFieldDisabled() ? 1231 : 1237);return result; } public String toString() { return "EventServiceConfiguration(eventTypeMetaDataDefaults=" + getEventTypeMetaDataDefaults() + ", updateConflictRetries=" + getUpdateConflictRetries() + ", eventTypeCacheSize=" + getEventTypeCacheSize() + ", eventTypeCacheTtlMinutes=" + getEventTypeCacheTtlMinutes() + ", eventTypeMetaDataCacheTtlMinutes=" + getEventTypeMetaDataCacheTtlMinutes() + ", allFieldDisabled=" + isAllFieldDisabled() + ")"; } @ConstructorProperties({"eventTypeMetaDataDefaults", "updateConflictRetries", "eventTypeCacheSize", "eventTypeCacheTtlMinutes", "eventTypeMetaDataCacheTtlMinutes", "allFieldDisabled"})
/* 22 */   public EventServiceConfiguration(EventTypeMetaDataDefaults eventTypeMetaDataDefaults, int updateConflictRetries, int eventTypeCacheSize, int eventTypeCacheTtlMinutes, int eventTypeMetaDataCacheTtlMinutes, boolean allFieldDisabled) { this.eventTypeMetaDataDefaults = eventTypeMetaDataDefaults;this.updateConflictRetries = updateConflictRetries;this.eventTypeCacheSize = eventTypeCacheSize;this.eventTypeCacheTtlMinutes = eventTypeCacheTtlMinutes;this.eventTypeMetaDataCacheTtlMinutes = eventTypeMetaDataCacheTtlMinutes;this.allFieldDisabled = allFieldDisabled; } boolean allFieldDisabled = true;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 29 */   public EventTypeMetaDataDefaults getEventTypeMetaDataDefaults() { return this.eventTypeMetaDataDefaults; } public void setEventTypeMetaDataDefaults(EventTypeMetaDataDefaults eventTypeMetaDataDefaults) { this.eventTypeMetaDataDefaults = eventTypeMetaDataDefaults; }
/*    */   
/*    */ 
/*    */ 
/*    */   @Min(0L)
/* 34 */   int updateConflictRetries = 1000;
/* 35 */   public int getUpdateConflictRetries() { return this.updateConflictRetries; } public void setUpdateConflictRetries(int updateConflictRetries) { this.updateConflictRetries = updateConflictRetries; }
/*    */   
/*    */ 
/*    */ 
/*    */   @Min(0L)
/* 40 */   int eventTypeCacheSize = 5000;
/* 41 */   public int getEventTypeCacheSize() { return this.eventTypeCacheSize; } public void setEventTypeCacheSize(int eventTypeCacheSize) { this.eventTypeCacheSize = eventTypeCacheSize; }
/*    */   
/*    */ 
/*    */ 
/*    */   @Min(1L)
/* 46 */   int eventTypeCacheTtlMinutes = 60;
/* 47 */   public int getEventTypeCacheTtlMinutes() { return this.eventTypeCacheTtlMinutes; } public void setEventTypeCacheTtlMinutes(int eventTypeCacheTtlMinutes) { this.eventTypeCacheTtlMinutes = eventTypeCacheTtlMinutes; }
/*    */   
/*    */ 
/*    */ 
/*    */   @Min(1L)
/* 52 */   int eventTypeMetaDataCacheTtlMinutes = 15;
/* 53 */   public int getEventTypeMetaDataCacheTtlMinutes() { return this.eventTypeMetaDataCacheTtlMinutes; } public void setEventTypeMetaDataCacheTtlMinutes(int eventTypeMetaDataCacheTtlMinutes) { this.eventTypeMetaDataCacheTtlMinutes = eventTypeMetaDataCacheTtlMinutes; }
/*    */   
/*    */   EventTypeMetaDataDefaults eventTypeMetaDataDefaults;
/*    */   public boolean isAllFieldDisabled()
/*    */   {
/* 58 */     return this.allFieldDisabled; } public void setAllFieldDisabled(boolean allFieldDisabled) { this.allFieldDisabled = allFieldDisabled; }
/*    */   
/*    */   public EventServiceConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/configuration/EventServiceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */