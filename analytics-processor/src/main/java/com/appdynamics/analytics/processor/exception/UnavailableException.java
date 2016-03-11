/*    */ package com.appdynamics.analytics.processor.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.TransientException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnavailableException
/*    */   extends TransientException
/*    */ {
/*    */   public UnavailableException()
/*    */   {
/* 16 */     super("Event Service is unavailable");
/*    */   }
/*    */   
/*    */   public UnavailableException(String message) {
/* 20 */     super(message);
/*    */   }
/*    */   
/*    */   public UnavailableException(String message, Throwable cause) {
/* 24 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public UnavailableException(Throwable cause) {
/* 28 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/exception/UnavailableException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */