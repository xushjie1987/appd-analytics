/*    */ package com.appdynamics.analytics.shared.rest.client.utils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestBuilderException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RequestBuilderException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RequestBuilderException(String message, Throwable cause)
/*    */   {
/* 28 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public RequestBuilderException(String message)
/*    */   {
/* 35 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public RequestBuilderException(Throwable cause)
/*    */   {
/* 42 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/RequestBuilderException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */