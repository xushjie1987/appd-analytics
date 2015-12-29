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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventTypeAlreadyExistsException
/*    */   extends PermanentException
/*    */ {
/*    */   public EventTypeAlreadyExistsException(String accountName, String eventType)
/*    */   {
/* 20 */     super("Event type creation failed for account [" + accountName + "] and event type [" + eventType + "] - an event type with that name already exists for this account");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/EventTypeAlreadyExistsException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */