/*    */ package com.appdynamics.analytics.processor.event.configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NewClusterEvent
/*    */ {
/*    */   final String clusterName;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getClusterName()
/*    */   {
/* 16 */     return this.clusterName;
/*    */   }
/*    */   
/*    */   public NewClusterEvent(String clusterName) {
/* 20 */     this.clusterName = clusterName;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/configuration/NewClusterEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */