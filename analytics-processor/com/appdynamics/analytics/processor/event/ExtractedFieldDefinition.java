/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import java.beans.ConstructorProperties;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class ExtractedFieldDefinition
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 18 */     if (o == this) return true; if (!(o instanceof ExtractedFieldDefinition)) return false; ExtractedFieldDefinition other = (ExtractedFieldDefinition)o; if (!other.canEqual(this)) return false; Object this$accountName = getAccountName();Object other$accountName = other.getAccountName(); if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$eventType = getEventType();Object other$eventType = other.getEventType(); if (this$eventType == null ? other$eventType != null : !this$eventType.equals(other$eventType)) return false; Object this$sourceType = getSourceType();Object other$sourceType = other.getSourceType(); if (this$sourceType == null ? other$sourceType != null : !this$sourceType.equals(other$sourceType)) return false; Object this$pattern = getPattern();Object other$pattern = other.getPattern(); if (this$pattern == null ? other$pattern != null : !this$pattern.equals(other$pattern)) return false; Object this$name = getName();Object other$name = other.getName();return this$name == null ? other$name == null : this$name.equals(other$name); } public boolean canEqual(Object other) { return other instanceof ExtractedFieldDefinition; } public int hashCode() { int PRIME = 31;int result = 1;Object $accountName = getAccountName();result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $eventType = getEventType();result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());Object $sourceType = getSourceType();result = result * 31 + ($sourceType == null ? 0 : $sourceType.hashCode());Object $pattern = getPattern();result = result * 31 + ($pattern == null ? 0 : $pattern.hashCode());Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());return result; } @ConstructorProperties({"accountName", "eventType", "sourceType", "pattern", "name"})
/* 19 */   public ExtractedFieldDefinition(String accountName, String eventType, String sourceType, String pattern, String name) { this.accountName = accountName;this.eventType = eventType;this.sourceType = sourceType;this.pattern = pattern;this.name = name; }
/*    */   
/* 21 */   public String toString() { return "ExtractedFieldDefinition(accountName=" + getAccountName() + ", eventType=" + getEventType() + ", sourceType=" + getSourceType() + ", pattern=" + getPattern() + ", name=" + getName() + ")"; }
/* 22 */   private static final Logger log = LoggerFactory.getLogger(ExtractedFieldDefinition.class);
/*    */   
/*    */   private String accountName;
/*    */   
/*    */   private String eventType;
/*    */   
/* 28 */   public String getAccountName() { return this.accountName; } public void setAccountName(String accountName) { this.accountName = accountName; }
/*    */   
/*    */   private String sourceType;
/*    */   private String pattern;
/*    */   private String name;
/* 33 */   public String getEventType() { return this.eventType; } public void setEventType(String eventType) { this.eventType = eventType; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 38 */   public String getSourceType() { return this.sourceType; } public void setSourceType(String sourceType) { this.sourceType = sourceType; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 43 */   public String getPattern() { return this.pattern; } public void setPattern(String pattern) { this.pattern = pattern; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 48 */   public String getName() { return this.name; } public void setName(String name) { this.name = name; }
/*    */   
/*    */   public void fill(String accountName, String eventType, String name) {
/* 51 */     this.accountName = accountName;
/* 52 */     this.eventType = eventType;
/* 53 */     this.name = name;
/*    */   }
/*    */   
/*    */   public ExtractedFieldDefinition() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ExtractedFieldDefinition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */