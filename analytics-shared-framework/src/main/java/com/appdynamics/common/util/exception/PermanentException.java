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
/*    */ public class PermanentException
/*    */   extends RuntimeException
/*    */ {
/*    */   public PermanentException(String message)
/*    */   {
/* 29 */     super(message);
/*    */   }
/*    */   
/*    */   public PermanentException(String message, Throwable cause) {
/* 33 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public PermanentException(Throwable cause) {
/* 37 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/PermanentException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */