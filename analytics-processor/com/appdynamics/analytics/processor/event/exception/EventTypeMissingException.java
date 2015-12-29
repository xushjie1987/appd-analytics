/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.PermanentException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventTypeMissingException
/*    */   extends PermanentException
/*    */ {
/*    */   public EventTypeMissingException(String accountName, String eventType)
/*    */   {
/* 17 */     super("Event type does not exist for account [" + accountName + "] and event type [" + eventType + "]");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/EventTypeMissingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */