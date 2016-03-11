/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.PermanentException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventTypeDeletionFailureException
/*    */   extends PermanentException
/*    */ {
/*    */   public EventTypeDeletionFailureException(String accountName, String eventType)
/*    */   {
/* 15 */     super("Failed to delete event type for account [" + accountName + "] and event type [" + eventType + "]");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/EventTypeDeletionFailureException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */