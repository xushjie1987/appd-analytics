/*    */ package com.appdynamics.analytics.processor.event.hiddenfields;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import java.beans.ConstructorProperties;
/*    */ import org.joda.time.DateTime;
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
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class HiddenField
/*    */ {
/*    */   String fieldName;
/*    */   DateTime hiddenOnDateTime;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 24 */     if (o == this) return true; if (!(o instanceof HiddenField)) return false; HiddenField other = (HiddenField)o; if (!other.canEqual(this)) return false; Object this$fieldName = getFieldName();Object other$fieldName = other.getFieldName(); if (this$fieldName == null ? other$fieldName != null : !this$fieldName.equals(other$fieldName)) return false; Object this$hiddenOnDateTime = getHiddenOnDateTime();Object other$hiddenOnDateTime = other.getHiddenOnDateTime();return this$hiddenOnDateTime == null ? other$hiddenOnDateTime == null : this$hiddenOnDateTime.equals(other$hiddenOnDateTime); } public boolean canEqual(Object other) { return other instanceof HiddenField; } public int hashCode() { int PRIME = 31;int result = 1;Object $fieldName = getFieldName();result = result * 31 + ($fieldName == null ? 0 : $fieldName.hashCode());Object $hiddenOnDateTime = getHiddenOnDateTime();result = result * 31 + ($hiddenOnDateTime == null ? 0 : $hiddenOnDateTime.hashCode());return result; }
/* 25 */   public String toString() { return "HiddenField(fieldName=" + getFieldName() + ", hiddenOnDateTime=" + getHiddenOnDateTime() + ")"; }
/*    */   @ConstructorProperties({"fieldName", "hiddenOnDateTime"})
/* 27 */   public HiddenField(String fieldName, DateTime hiddenOnDateTime) { this.fieldName = fieldName;this.hiddenOnDateTime = hiddenOnDateTime; }
/*    */   
/* 29 */   public String getFieldName() { return this.fieldName; } public void setFieldName(String fieldName) { this.fieldName = fieldName; }
/* 30 */   public DateTime getHiddenOnDateTime() { return this.hiddenOnDateTime; } public void setHiddenOnDateTime(DateTime hiddenOnDateTime) { this.hiddenOnDateTime = hiddenOnDateTime; }
/*    */   
/*    */   public HiddenField() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/hiddenfields/HiddenField.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */