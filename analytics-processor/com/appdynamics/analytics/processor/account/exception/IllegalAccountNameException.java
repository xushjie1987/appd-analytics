/*    */ package com.appdynamics.analytics.processor.account.exception;
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
/*    */ 
/*    */ public class IllegalAccountNameException
/*    */   extends PermanentException
/*    */ {
/*    */   public IllegalAccountNameException(String message)
/*    */   {
/* 21 */     super(message);
/*    */   }
/*    */   
/*    */   public IllegalAccountNameException(String message, Throwable cause) {
/* 25 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public IllegalAccountNameException(Throwable cause) {
/* 29 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/account/exception/IllegalAccountNameException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */