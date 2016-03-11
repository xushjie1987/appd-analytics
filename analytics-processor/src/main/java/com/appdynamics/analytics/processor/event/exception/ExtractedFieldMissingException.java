/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.ExtractedFieldDefinition;
/*    */ import com.appdynamics.common.util.exception.PermanentException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtractedFieldMissingException
/*    */   extends PermanentException
/*    */ {
/*    */   public ExtractedFieldMissingException(String accountName, String eventType, String name)
/*    */   {
/* 13 */     super("Event type extracted field does not exist for account [" + accountName + "] event type [" + eventType + "] name [" + name + "]");
/*    */   }
/*    */   
/*    */   public ExtractedFieldMissingException(ExtractedFieldDefinition def)
/*    */   {
/* 18 */     this(def.getAccountName(), def.getEventType(), def.getName());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/ExtractedFieldMissingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */