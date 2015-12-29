/*    */ package com.appdynamics.analytics.shared.rest.dto.metadata;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MetaDataArray
/*    */   extends MetaDataField
/*    */ {
/*    */   public static final String TYPE = "array";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   MetaDataField items;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 23 */     if (o == this) return true; if (!(o instanceof MetaDataArray)) return false; MetaDataArray other = (MetaDataArray)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$items = getItems();Object other$items = other.getItems();return this$items == null ? other$items == null : this$items.equals(other$items); } public boolean canEqual(Object other) { return other instanceof MetaDataArray; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $items = getItems();result = result * 31 + ($items == null ? 0 : $items.hashCode());return result;
/*    */   }
/*    */   
/* 26 */   public MetaDataField getItems() { return this.items; } public void setItems(MetaDataField items) { this.items = items; }
/*    */   
/*    */   public MetaDataArray() {
/* 29 */     super(MetaDataFieldType.ARRAY);
/*    */   }
/*    */   
/*    */   public MetaDataArray(MetaDataField items) {
/* 33 */     super(MetaDataFieldType.ARRAY);
/* 34 */     this.items = items;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/metadata/MetaDataArray.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */