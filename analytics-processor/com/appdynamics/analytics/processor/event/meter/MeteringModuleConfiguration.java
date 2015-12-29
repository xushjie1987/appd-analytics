/*    */ package com.appdynamics.analytics.processor.event.meter;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.NotNull;
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
/*    */ public class MeteringModuleConfiguration
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 21 */     if (o == this) return true; if (!(o instanceof MeteringModuleConfiguration)) return false; MeteringModuleConfiguration other = (MeteringModuleConfiguration)o; if (!other.canEqual(this)) return false; if (isEnabled() != other.isEnabled()) return false; if (getEventTypeMaxSearchesPerMinute() != other.getEventTypeMaxSearchesPerMinute()) return false; if (getMaxDailyEventTypeBytesQuota() != other.getMaxDailyEventTypeBytesQuota()) return false; if (getMaxDailyEventTypeDocumentsQuota() != other.getMaxDailyEventTypeDocumentsQuota()) return false; Object this$usageQueryTimeout = getUsageQueryTimeout();Object other$usageQueryTimeout = other.getUsageQueryTimeout();return this$usageQueryTimeout == null ? other$usageQueryTimeout == null : this$usageQueryTimeout.equals(other$usageQueryTimeout); } public boolean canEqual(Object other) { return other instanceof MeteringModuleConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + (isEnabled() ? 1231 : 1237);result = result * 31 + getEventTypeMaxSearchesPerMinute();long $maxDailyEventTypeBytesQuota = getMaxDailyEventTypeBytesQuota();result = result * 31 + (int)($maxDailyEventTypeBytesQuota >>> 32 ^ $maxDailyEventTypeBytesQuota);long $maxDailyEventTypeDocumentsQuota = getMaxDailyEventTypeDocumentsQuota();result = result * 31 + (int)($maxDailyEventTypeDocumentsQuota >>> 32 ^ $maxDailyEventTypeDocumentsQuota);Object $usageQueryTimeout = getUsageQueryTimeout();result = result * 31 + ($usageQueryTimeout == null ? 0 : $usageQueryTimeout.hashCode());return result; } public String toString() { return "MeteringModuleConfiguration(enabled=" + isEnabled() + ", eventTypeMaxSearchesPerMinute=" + getEventTypeMaxSearchesPerMinute() + ", maxDailyEventTypeBytesQuota=" + getMaxDailyEventTypeBytesQuota() + ", maxDailyEventTypeDocumentsQuota=" + getMaxDailyEventTypeDocumentsQuota() + ", usageQueryTimeout=" + getUsageQueryTimeout() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 26 */   public boolean isEnabled() { return this.enabled; } public void setEnabled(boolean enabled) { this.enabled = enabled; } boolean enabled = true;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 32 */   public int getEventTypeMaxSearchesPerMinute() { return this.eventTypeMaxSearchesPerMinute; } public void setEventTypeMaxSearchesPerMinute(int eventTypeMaxSearchesPerMinute) { this.eventTypeMaxSearchesPerMinute = eventTypeMaxSearchesPerMinute; } int eventTypeMaxSearchesPerMinute = Integer.MAX_VALUE;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 37 */   public long getMaxDailyEventTypeBytesQuota() { return this.maxDailyEventTypeBytesQuota; } public void setMaxDailyEventTypeBytesQuota(long maxDailyEventTypeBytesQuota) { this.maxDailyEventTypeBytesQuota = maxDailyEventTypeBytesQuota; } long maxDailyEventTypeBytesQuota = 268435456000L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 42 */   public long getMaxDailyEventTypeDocumentsQuota() { return this.maxDailyEventTypeDocumentsQuota; } public void setMaxDailyEventTypeDocumentsQuota(long maxDailyEventTypeDocumentsQuota) { this.maxDailyEventTypeDocumentsQuota = maxDailyEventTypeDocumentsQuota; } long maxDailyEventTypeDocumentsQuota = 1250000000L;
/*    */   
/*    */ 
/*    */   @Valid
/*    */   @NotNull
/* 47 */   TimeUnitConfiguration usageQueryTimeout = new TimeUnitConfiguration(2500L, TimeUnit.MILLISECONDS);
/*    */   
/* 49 */   public TimeUnitConfiguration getUsageQueryTimeout() { return this.usageQueryTimeout; } public void setUsageQueryTimeout(TimeUnitConfiguration usageQueryTimeout) { this.usageQueryTimeout = usageQueryTimeout; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/MeteringModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */