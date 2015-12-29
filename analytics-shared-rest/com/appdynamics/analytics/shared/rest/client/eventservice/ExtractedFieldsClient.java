/*     */ package com.appdynamics.analytics.shared.rest.client.eventservice;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.BadRequestRestException;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.ConflictRestException;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.NotFoundRestException;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface ExtractedFieldsClient
/*     */ {
/*     */   public abstract List<ExtractedFieldDefinition> getExtractedFields(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   public abstract List<ExtractedFieldDefinition> getExtractedFields(String paramString1, String paramString2, String paramString3, List<String> paramList);
/*     */   
/*     */   public abstract ExtractedFieldDefinition getExtractedField(String paramString1, String paramString2, String paramString3, String paramString4);
/*     */   
/*     */   public abstract void validateExtractedField(String paramString1, String paramString2, String paramString3, ExtractedFieldDefinition paramExtractedFieldDefinition)
/*     */     throws BadRequestRestException;
/*     */   
/*     */   public abstract void createExtractedField(String paramString1, String paramString2, String paramString3, ExtractedFieldDefinition paramExtractedFieldDefinition)
/*     */     throws BadRequestRestException, ConflictRestException;
/*     */   
/*     */   public abstract void updateExtractedField(String paramString1, String paramString2, String paramString3, ExtractedFieldDefinition paramExtractedFieldDefinition)
/*     */     throws BadRequestRestException, NotFoundRestException;
/*     */   
/*     */   public abstract void deleteExtractedField(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     throws NotFoundRestException;
/*     */   
/*     */   @JsonIgnoreProperties(ignoreUnknown=true)
/*     */   public static class ExtractedFieldDefinition
/*     */   {
/*     */     private String sourceType;
/*     */     private String pattern;
/*     */     private String name;
/*     */     
/*     */     @ConstructorProperties({"sourceType", "pattern", "name"})
/*     */     public ExtractedFieldDefinition(String sourceType, String pattern, String name)
/*     */     {
/* 129 */       this.sourceType = sourceType;this.pattern = pattern;this.name = name; }
/*     */     
/* 131 */     public String toString() { return "ExtractedFieldsClient.ExtractedFieldDefinition(sourceType=" + getSourceType() + ", pattern=" + getPattern() + ", name=" + getName() + ")"; }
/* 132 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ExtractedFieldDefinition)) return false; ExtractedFieldDefinition other = (ExtractedFieldDefinition)o; if (!other.canEqual(this)) return false; Object this$sourceType = getSourceType();Object other$sourceType = other.getSourceType(); if (this$sourceType == null ? other$sourceType != null : !this$sourceType.equals(other$sourceType)) return false; Object this$pattern = getPattern();Object other$pattern = other.getPattern(); if (this$pattern == null ? other$pattern != null : !this$pattern.equals(other$pattern)) return false; Object this$name = getName();Object other$name = other.getName();return this$name == null ? other$name == null : this$name.equals(other$name); } public boolean canEqual(Object other) { return other instanceof ExtractedFieldDefinition; } public int hashCode() { int PRIME = 31;int result = 1;Object $sourceType = getSourceType();result = result * 31 + ($sourceType == null ? 0 : $sourceType.hashCode());Object $pattern = getPattern();result = result * 31 + ($pattern == null ? 0 : $pattern.hashCode());Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 137 */     public String getSourceType() { return this.sourceType; } public void setSourceType(String sourceType) { this.sourceType = sourceType; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 142 */     public String getPattern() { return this.pattern; } public void setPattern(String pattern) { this.pattern = pattern; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 147 */     public String getName() { return this.name; } public void setName(String name) { this.name = name; }
/*     */     
/*     */     public ExtractedFieldDefinition() {}
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/ExtractedFieldsClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */