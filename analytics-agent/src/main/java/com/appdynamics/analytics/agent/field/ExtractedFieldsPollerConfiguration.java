/*    */ package com.appdynamics.analytics.agent.field;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
/*    */ import org.hibernate.validator.constraints.NotBlank;
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
/*    */ public class ExtractedFieldsPollerConfiguration
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 21 */     if (o == this) return true; if (!(o instanceof ExtractedFieldsPollerConfiguration)) return false; ExtractedFieldsPollerConfiguration other = (ExtractedFieldsPollerConfiguration)o; if (!other.canEqual(this)) return false; Object this$initialDelay = getInitialDelay();Object other$initialDelay = other.getInitialDelay(); if (this$initialDelay == null ? other$initialDelay != null : !this$initialDelay.equals(other$initialDelay)) return false; Object this$pollInterval = getPollInterval();Object other$pollInterval = other.getPollInterval(); if (this$pollInterval == null ? other$pollInterval != null : !this$pollInterval.equals(other$pollInterval)) return false; Object this$eventType = getEventType();Object other$eventType = other.getEventType();return this$eventType == null ? other$eventType == null : this$eventType.equals(other$eventType); } public boolean canEqual(Object other) { return other instanceof ExtractedFieldsPollerConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $initialDelay = getInitialDelay();result = result * 31 + ($initialDelay == null ? 0 : $initialDelay.hashCode());Object $pollInterval = getPollInterval();result = result * 31 + ($pollInterval == null ? 0 : $pollInterval.hashCode());Object $eventType = getEventType();result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());return result; } public String toString() { return "ExtractedFieldsPollerConfiguration(initialDelay=" + getInitialDelay() + ", pollInterval=" + getPollInterval() + ", eventType=" + getEventType() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 28 */   private static final TimeUnitConfiguration DEFAULT_INITIAL_DELAY = new TimeUnitConfiguration(30L, TimeUnit.SECONDS);
/* 29 */   private static final TimeUnitConfiguration DEFAULT_POLL_INTERVAL = new TimeUnitConfiguration(60L, TimeUnit.SECONDS);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Valid
/* 36 */   private TimeUnitConfiguration initialDelay = DEFAULT_INITIAL_DELAY;
/* 37 */   public TimeUnitConfiguration getInitialDelay() { return this.initialDelay; } public void setInitialDelay(TimeUnitConfiguration initialDelay) { this.initialDelay = initialDelay; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @Valid
/* 43 */   private TimeUnitConfiguration pollInterval = DEFAULT_POLL_INTERVAL;
/* 44 */   public TimeUnitConfiguration getPollInterval() { return this.pollInterval; } public void setPollInterval(TimeUnitConfiguration pollInterval) { this.pollInterval = pollInterval; }
/*    */   
/*    */   @NotBlank
/*    */   public String eventType;
/*    */   public String getEventType()
/*    */   {
/* 50 */     return this.eventType; } public void setEventType(String eventType) { this.eventType = eventType; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/field/ExtractedFieldsPollerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */