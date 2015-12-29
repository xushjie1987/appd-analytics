/*    */ package com.appdynamics.common.util.exception;
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
/*    */ public class TransientException
/*    */   extends RuntimeException
/*    */ {
/*    */   public TransientException(String message)
/*    */   {
/* 17 */     super(message);
/*    */   }
/*    */   
/*    */   public TransientException(String message, Throwable cause) {
/* 21 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public TransientException(Throwable cause) {
/* 25 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/TransientException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */