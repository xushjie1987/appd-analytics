/*    */ package com.appdynamics.analytics.processor.query.node;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EsItem
/*    */   implements EsNode
/*    */ {
/*    */   private String label;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private EsNode value;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 20 */   public String getLabel() { return this.label; } public void setLabel(String label) { this.label = label; }
/* 21 */   public EsNode getValue() { return this.value; } public void setValue(EsNode value) { this.value = value; }
/*    */   
/*    */   public EsItem(String label, EsNode value) {
/* 24 */     this.label = label;
/* 25 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/node/EsItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */