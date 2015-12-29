/*    */ package com.appdynamics.analytics.processor.event.query;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueryResult
/*    */ {
/*    */   private String eventType;
/*    */   private OutputStream outputStream;
/*    */   
/* 18 */   public String getEventType() { return this.eventType; }
/* 19 */   public OutputStream getOutputStream() { return this.outputStream; }
/*    */   
/*    */   public QueryResult(String eventType, OutputStream outputStream) {
/* 22 */     this.eventType = eventType;
/* 23 */     this.outputStream = outputStream;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/query/QueryResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */