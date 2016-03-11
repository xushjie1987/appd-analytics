/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ClientException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ClientException(String message, Throwable cause)
/*    */   {
/* 25 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ClientException(String message)
/*    */   {
/* 32 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ClientException(Throwable cause)
/*    */   {
/* 39 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/ClientException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */