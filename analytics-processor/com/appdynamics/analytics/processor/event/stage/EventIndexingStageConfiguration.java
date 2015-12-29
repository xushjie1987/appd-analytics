/*    */ package com.appdynamics.analytics.processor.event.stage;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.admin.ActionType;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.util.EnumSet;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import javax.validation.constraints.Size;
/*    */ 
/*    */ public class EventIndexingStageConfiguration
/*    */ {
/*    */   @NotNull
/*    */   @Size(min=1, max=1)
/*    */   EnumSet<ActionType> topicTypes;
/*    */   @NotNull
/*    */   String name;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 22 */     if (o == this) return true; if (!(o instanceof EventIndexingStageConfiguration)) return false; EventIndexingStageConfiguration other = (EventIndexingStageConfiguration)o; if (!other.canEqual(this)) return false; Object this$topicTypes = getTopicTypes();Object other$topicTypes = other.getTopicTypes(); if (this$topicTypes == null ? other$topicTypes != null : !this$topicTypes.equals(other$topicTypes)) return false; Object this$name = getName();Object other$name = other.getName(); if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false; Object this$maxTimeWithoutUpsertFlush = getMaxTimeWithoutUpsertFlush();Object other$maxTimeWithoutUpsertFlush = other.getMaxTimeWithoutUpsertFlush(); if (this$maxTimeWithoutUpsertFlush == null ? other$maxTimeWithoutUpsertFlush != null : !this$maxTimeWithoutUpsertFlush.equals(other$maxTimeWithoutUpsertFlush)) return false; return getMaxBytesAccumulatedUpserts() == other.getMaxBytesAccumulatedUpserts(); } public boolean canEqual(Object other) { return other instanceof EventIndexingStageConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $topicTypes = getTopicTypes();result = result * 31 + ($topicTypes == null ? 0 : $topicTypes.hashCode());Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());Object $maxTimeWithoutUpsertFlush = getMaxTimeWithoutUpsertFlush();result = result * 31 + ($maxTimeWithoutUpsertFlush == null ? 0 : $maxTimeWithoutUpsertFlush.hashCode());long $maxBytesAccumulatedUpserts = getMaxBytesAccumulatedUpserts();result = result * 31 + (int)($maxBytesAccumulatedUpserts >>> 32 ^ $maxBytesAccumulatedUpserts);return result; } public String toString() { return "EventIndexingStageConfiguration(topicTypes=" + getTopicTypes() + ", name=" + getName() + ", maxTimeWithoutUpsertFlush=" + getMaxTimeWithoutUpsertFlush() + ", maxBytesAccumulatedUpserts=" + getMaxBytesAccumulatedUpserts() + ")"; }
/*    */   
/*    */ 
/*    */ 
/* 26 */   public EnumSet<ActionType> getTopicTypes() { return this.topicTypes; } public void setTopicTypes(EnumSet<ActionType> topicTypes) { this.topicTypes = topicTypes; }
/*    */   
/*    */ 
/* 29 */   public String getName() { return this.name; } public void setName(String name) { this.name = name; }
/*    */   
/*    */ 
/*    */   @Valid
/*    */   @NotNull
/* 34 */   TimeUnitConfiguration maxTimeWithoutUpsertFlush = new TimeUnitConfiguration(30L, TimeUnit.SECONDS);
/*    */   
/* 36 */   public TimeUnitConfiguration getMaxTimeWithoutUpsertFlush() { return this.maxTimeWithoutUpsertFlush; } public void setMaxTimeWithoutUpsertFlush(TimeUnitConfiguration maxTimeWithoutUpsertFlush) { this.maxTimeWithoutUpsertFlush = maxTimeWithoutUpsertFlush; }
/*    */   
/*    */ 
/*    */ 
/*    */   @Min(1L)
/* 41 */   long maxBytesAccumulatedUpserts = 134217728L;
/* 42 */   public long getMaxBytesAccumulatedUpserts() { return this.maxBytesAccumulatedUpserts; } public void setMaxBytesAccumulatedUpserts(long maxBytesAccumulatedUpserts) { this.maxBytesAccumulatedUpserts = maxBytesAccumulatedUpserts; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/stage/EventIndexingStageConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */