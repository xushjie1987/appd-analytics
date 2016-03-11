/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.TransientException;
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
/*    */ public class EventTypeRegistrationFailure
/*    */   extends TransientException
/*    */ {
/*    */   public EventTypeRegistrationFailure(String message, Throwable cause)
/*    */   {
/* 19 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public EventTypeRegistrationFailure(String accountName, String eventType) {
/* 23 */     super("Index creation failed for account [" + accountName + "] and event type [" + eventType + "] - acknowledgement failed");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/EventTypeRegistrationFailure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */