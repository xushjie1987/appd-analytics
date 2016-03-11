/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.ExtractedFieldDefinition;
/*    */ import com.appdynamics.common.util.exception.PermanentException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtractedFieldAlreadyExistsException
/*    */   extends PermanentException
/*    */ {
/*    */   public ExtractedFieldAlreadyExistsException(String accountName, String eventType, String name)
/*    */   {
/* 13 */     super("Event type extracted field already exists for account [" + accountName + "] event type [" + eventType + "] name [" + name + "]");
/*    */   }
/*    */   
/*    */   public ExtractedFieldAlreadyExistsException(ExtractedFieldDefinition def)
/*    */   {
/* 18 */     this(def.getAccountName(), def.getEventType(), def.getName());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/ExtractedFieldAlreadyExistsException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */