/*    */ package com.appdynamics.analytics.processor.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.PermanentException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestParsingException
/*    */   extends PermanentException
/*    */ {
/*    */   public RequestParsingException()
/*    */   {
/* 16 */     super("An error occurred parsing the request");
/*    */   }
/*    */   
/*    */   public RequestParsingException(String message) {
/* 20 */     super(message);
/*    */   }
/*    */   
/*    */   public RequestParsingException(String message, Throwable cause) {
/* 24 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public RequestParsingException(Throwable cause) {
/* 28 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/exception/RequestParsingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */