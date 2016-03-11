/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.PermanentException;
/*    */ 
/*    */ public class InvalidHiddenFieldException
/*    */   extends PermanentException
/*    */ {
/*    */   String hiddenFieldName;
/*    */   
/*    */   public String getHiddenFieldName()
/*    */   {
/* 12 */     return this.hiddenFieldName;
/*    */   }
/*    */   
/*    */   public InvalidHiddenFieldException(String hiddenFieldName, String reason) {
/* 16 */     super("Hidden field " + hiddenFieldName + " is not a valid hidden field declaration due to [" + reason + "].");
/*    */     
/* 18 */     this.hiddenFieldName = hiddenFieldName;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/InvalidHiddenFieldException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */