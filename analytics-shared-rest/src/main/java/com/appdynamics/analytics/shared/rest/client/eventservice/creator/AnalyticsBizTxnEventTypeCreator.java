/*    */ package com.appdynamics.analytics.shared.rest.client.eventservice.creator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnalyticsBizTxnEventTypeCreator
/*    */   extends FileBasedEventTypeCreator
/*    */ {
/*    */   private static final String EVENT_TYPE = "biz_txn_v1";
/*    */   
/*    */ 
/*    */ 
/*    */   private static final String MAPPING_JSON = "/eventservice/biz_txn_v1-mapping.json";
/*    */   
/*    */ 
/*    */ 
/*    */   public AnalyticsBizTxnEventTypeCreator()
/*    */   {
/* 19 */     super("biz_txn_v1", "/eventservice/biz_txn_v1-mapping.json");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/creator/AnalyticsBizTxnEventTypeCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */