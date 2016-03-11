/*    */ package com.appdynamics.analytics.agent.source;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ import com.fasterxml.jackson.annotation.JsonSubTypes;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*    */ import java.util.Set;
/*    */ import javax.validation.Valid;
/*    */ 
/*    */ @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
/*    */ @JsonSubTypes({@com.fasterxml.jackson.annotation.JsonSubTypes.Type(value=com.appdynamics.analytics.agent.source.tail.TailLogSourceConfiguration.class, name="tailLogSourceConfiguration")})
/*    */ public class LogSourceConfiguration implements com.appdynamics.common.util.item.Item<Object>
/*    */ {
/*    */   @Valid
/*    */   String name;
/*    */   @org.hibernate.validator.constraints.NotBlank
/*    */   String sourceType;
/*    */   Set<String> extractedFieldPatterns;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 23 */     if (o == this) return true; if (!(o instanceof LogSourceConfiguration)) return false; LogSourceConfiguration other = (LogSourceConfiguration)o; if (!other.canEqual(this)) return false; Object this$name = getName();Object other$name = other.getName(); if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false; Object this$sourceType = getSourceType();Object other$sourceType = other.getSourceType(); if (this$sourceType == null ? other$sourceType != null : !this$sourceType.equals(other$sourceType)) return false; Object this$extractedFieldPatterns = getExtractedFieldPatterns();Object other$extractedFieldPatterns = other.getExtractedFieldPatterns();return this$extractedFieldPatterns == null ? other$extractedFieldPatterns == null : this$extractedFieldPatterns.equals(other$extractedFieldPatterns); } public boolean canEqual(Object other) { return other instanceof LogSourceConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());Object $sourceType = getSourceType();result = result * 31 + ($sourceType == null ? 0 : $sourceType.hashCode());Object $extractedFieldPatterns = getExtractedFieldPatterns();result = result * 31 + ($extractedFieldPatterns == null ? 0 : $extractedFieldPatterns.hashCode());return result; } public String toString() { return "LogSourceConfiguration(name=" + getName() + ", sourceType=" + getSourceType() + ", extractedFieldPatterns=" + getExtractedFieldPatterns() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 28 */   public String getName() { return this.name; } public void setName(String name) { this.name = name; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 34 */   public String getSourceType() { return this.sourceType; } public void setSourceType(String sourceType) { this.sourceType = sourceType; }
/*    */   
/*    */ 
/*    */ 
/* 38 */   public Set<String> getExtractedFieldPatterns() { return this.extractedFieldPatterns; } public void setExtractedFieldPatterns(Set<String> extractedFieldPatterns) { this.extractedFieldPatterns = extractedFieldPatterns; }
/*    */   
/*    */ 
/*    */ 
/*    */   public LogSourceConfiguration(String name, String sourceType, Set<String> extractedFieldPatterns)
/*    */   {
/* 44 */     this.name = name;
/* 45 */     this.sourceType = sourceType;
/* 46 */     this.extractedFieldPatterns = extractedFieldPatterns;
/*    */   }
/*    */   
/*    */   @JsonIgnore
/*    */   public Object getId() {
/* 51 */     return this.name;
/*    */   }
/*    */   
/*    */   public LogSourceConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/LogSourceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */