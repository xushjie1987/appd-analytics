/*    */ package com.appdynamics.analytics.shared.rest.dto.metadata;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonSubTypes;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*    */ import java.beans.ConstructorProperties;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
/*    */ @JsonSubTypes({@com.fasterxml.jackson.annotation.JsonSubTypes.Type(value=MetaDataArray.class, name="array"), @com.fasterxml.jackson.annotation.JsonSubTypes.Type(value=MetaDataObject.class, name="object")})
/*    */ public class MetaDataField
/*    */ {
/*    */   @NotNull
/*    */   MetaDataFieldType type;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 22 */     if (o == this) return true; if (!(o instanceof MetaDataField)) return false; MetaDataField other = (MetaDataField)o; if (!other.canEqual(this)) return false; Object this$type = getType();Object other$type = other.getType();return this$type == null ? other$type == null : this$type.equals(other$type); } public boolean canEqual(Object other) { return other instanceof MetaDataField; } public int hashCode() { int PRIME = 31;int result = 1;Object $type = getType();result = result * 31 + ($type == null ? 0 : $type.hashCode());return result; } public String toString() { return "MetaDataField(type=" + getType() + ")"; }
/*    */   @ConstructorProperties({"type"})
/* 24 */   public MetaDataField(MetaDataFieldType type) { this.type = type; }
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
/* 36 */   public MetaDataFieldType getType() { return this.type; } public void setType(MetaDataFieldType type) { this.type = type; }
/*    */   
/*    */   public MetaDataField() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/metadata/MetaDataField.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */