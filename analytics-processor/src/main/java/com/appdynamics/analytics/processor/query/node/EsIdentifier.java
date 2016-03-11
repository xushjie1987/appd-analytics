/*    */ package com.appdynamics.analytics.processor.query.node;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EsIdentifier
/*    */   extends EsValue
/*    */ {
/*    */   public String getIdentifier()
/*    */   {
/* 14 */     return (String)this.value;
/*    */   }
/*    */   
/*    */   public EsIdentifier(String identifier) {
/* 18 */     super(identifier);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/node/EsIdentifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */