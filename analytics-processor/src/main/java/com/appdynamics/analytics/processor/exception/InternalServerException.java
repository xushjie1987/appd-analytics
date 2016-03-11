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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InternalServerException
/*    */   extends TransientException
/*    */ {
/*    */   public InternalServerException(String message)
/*    */   {
/* 21 */     super(message);
/*    */   }
/*    */   
/*    */   public InternalServerException(String message, Throwable cause) {
/* 25 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public InternalServerException(Throwable cause) {
/* 29 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/exception/InternalServerException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */