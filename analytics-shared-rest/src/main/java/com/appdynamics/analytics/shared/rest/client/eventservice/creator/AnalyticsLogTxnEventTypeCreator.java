/*    */ package com.appdynamics.analytics.shared.rest.client.eventservice.creator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnalyticsLogTxnEventTypeCreator
/*    */   extends FileBasedEventTypeCreator
/*    */ {
/*    */   private static final String EVENT_TYPE = "log_v1";
/*    */   
/*    */ 
/*    */ 
/*    */   private static final String MAPPING_JSON = "/eventservice/log_v1-mapping.json";
/*    */   
/*    */ 
/*    */ 
/*    */   public AnalyticsLogTxnEventTypeCreator()
/*    */   {
/* 19 */     super("log_v1", "/eventservice/log_v1-mapping.json");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/creator/AnalyticsLogTxnEventTypeCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */