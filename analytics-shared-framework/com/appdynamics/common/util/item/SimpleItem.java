/*    */ package com.appdynamics.common.util.item;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleItem<ID>
/*    */   implements Item<ID>
/*    */ {
/*    */   @NotNull
/*    */   @JsonProperty
/*    */   ID id;
/*    */   
/*    */   public String toString()
/*    */   {
/* 21 */     return "SimpleItem(id=" + getId() + ")";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ID getId()
/*    */   {
/* 29 */     return (ID)this.id;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Item<? extends ID> setId(ID id)
/*    */   {
/* 37 */     this.id = id;
/*    */     
/* 39 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final int hashCode()
/*    */   {
/* 48 */     return super.hashCode();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final boolean equals(Object obj)
/*    */   {
/* 58 */     return super.equals(obj);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/item/SimpleItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */