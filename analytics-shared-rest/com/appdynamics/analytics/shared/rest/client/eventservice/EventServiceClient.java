/*     */ package com.appdynamics.analytics.shared.rest.client.eventservice;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.client.eventservice.creator.EventTypeCreator;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import lombok.NonNull;
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
/*     */ public abstract interface EventServiceClient
/*     */ {
/*     */   public static final String BASE_URI = "/v2/events";
/*     */   
/*     */   public abstract void registerEventType(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     throws RestException;
/*     */   
/*     */   public abstract void updateEventType(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     throws RestException;
/*     */   
/*     */   public abstract String getEventType(String paramString1, String paramString2, String paramString3)
/*     */     throws RestException;
/*     */   
/*     */   public abstract String getEventTypeUsageNumDocuments(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
/*     */     throws RestException;
/*     */   
/*     */   public abstract String getEventTypeUsageNumDocumentFragments(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
/*     */     throws RestException;
/*     */   
/*     */   public abstract String getEventTypeUsageNumBytes(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
/*     */     throws RestException;
/*     */   
/*     */   public abstract void deleteEventType(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   public abstract void publishEvents(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     throws RestException;
/*     */   
/*     */   public abstract void upsertEvents(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, List<String> paramList)
/*     */     throws RestException;
/*     */   
/*     */   public abstract String searchEvents(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     throws RestException;
/*     */   
/*     */   public abstract String multiSearchEvents(String paramString1, String paramString2, String paramString3)
/*     */     throws RestException;
/*     */   
/*     */   public abstract String queryEvents(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, boolean paramBoolean)
/*     */     throws RestException;
/*     */   
/*     */   public abstract void registerEventTypeCreator(EventTypeCreator... paramVarArgs);
/*     */   
/*     */   public abstract void ping(String paramString1, String paramString2);
/*     */   
/*     */   public abstract void hideFields(String paramString1, String paramString2, String paramString3, List<HiddenField> paramList);
/*     */   
/*     */   public abstract List<HiddenField> listHiddenFields(String paramString1, String paramString2, String paramString3)
/*     */     throws IOException;
/*     */   
/*     */   public abstract void unhideField(String paramString1, String paramString2, String paramString3, String paramString4);
/*     */   
/*     */   public abstract String relevantFields(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     throws RestException;
/*     */   
/*     */   @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
/*     */   @JsonIgnoreProperties(ignoreUnknown=true)
/*     */   public static class HiddenField
/*     */   {
/*     */     @NonNull
/*     */     String fieldName;
/*     */     String hiddenOnDateTime;
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 426 */       if (o == this) return true; if (!(o instanceof HiddenField)) return false; HiddenField other = (HiddenField)o; if (!other.canEqual(this)) return false; Object this$fieldName = getFieldName();Object other$fieldName = other.getFieldName(); if (this$fieldName == null ? other$fieldName != null : !this$fieldName.equals(other$fieldName)) return false; Object this$hiddenOnDateTime = getHiddenOnDateTime();Object other$hiddenOnDateTime = other.getHiddenOnDateTime();return this$hiddenOnDateTime == null ? other$hiddenOnDateTime == null : this$hiddenOnDateTime.equals(other$hiddenOnDateTime); } public boolean canEqual(Object other) { return other instanceof HiddenField; } public int hashCode() { int PRIME = 31;int result = 1;Object $fieldName = getFieldName();result = result * 31 + ($fieldName == null ? 0 : $fieldName.hashCode());Object $hiddenOnDateTime = getHiddenOnDateTime();result = result * 31 + ($hiddenOnDateTime == null ? 0 : $hiddenOnDateTime.hashCode());return result; } public String toString() { return "EventServiceClient.HiddenField(fieldName=" + getFieldName() + ", hiddenOnDateTime=" + getHiddenOnDateTime() + ")"; }
/*     */     @ConstructorProperties({"fieldName", "hiddenOnDateTime"})
/* 428 */     public HiddenField(@NonNull String fieldName, String hiddenOnDateTime) { if (fieldName == null) throw new NullPointerException("fieldName"); this.fieldName = fieldName;this.hiddenOnDateTime = hiddenOnDateTime;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @NonNull
/* 437 */     public String getFieldName() { return this.fieldName; } public void setFieldName(@NonNull String fieldName) { if (fieldName == null) throw new NullPointerException("fieldName"); this.fieldName = fieldName;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 443 */     public String getHiddenOnDateTime() { return this.hiddenOnDateTime; } public void setHiddenOnDateTime(String hiddenOnDateTime) { this.hiddenOnDateTime = hiddenOnDateTime; }
/*     */     
/*     */     public HiddenField() {}
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/EventServiceClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */