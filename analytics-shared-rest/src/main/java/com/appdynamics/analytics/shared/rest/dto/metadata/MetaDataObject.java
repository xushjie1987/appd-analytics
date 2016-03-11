/*    */ package com.appdynamics.analytics.shared.rest.dto.metadata;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ public class MetaDataObject
/*    */   extends MetaDataField
/*    */ {
/*    */   public static final String TYPE = "object";
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 22 */     int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $properties = getProperties();result = result * 31 + ($properties == null ? 0 : $properties.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof MetaDataObject; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof MetaDataObject)) return false; MetaDataObject other = (MetaDataObject)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$properties = getProperties();Object other$properties = other.getProperties();return this$properties == null ? other$properties == null : this$properties.equals(other$properties);
/*    */   }
/*    */   
/* 25 */   public void setProperties(HashMap<String, MetaDataField> properties) { this.properties = properties; } public HashMap<String, MetaDataField> getProperties() { return this.properties; } private HashMap<String, MetaDataField> properties = new HashMap();
/*    */   
/*    */   public MetaDataObject() {
/* 28 */     super(MetaDataFieldType.OBJECT);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/metadata/MetaDataObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */