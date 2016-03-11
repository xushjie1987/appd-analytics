/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BadRequestRestException
/*    */   extends RestException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE = 400;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BadRequestRestException(String message, Throwable cause)
/*    */   {
/* 27 */     super(400, message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public BadRequestRestException(String message)
/*    */   {
/* 34 */     super(400, message);
/*    */   }
/*    */   
/*    */   public BadRequestRestException(RestExceptionPayload payload) {
/* 38 */     super(payload);
/*    */   }
/*    */   
/*    */   public BadRequestRestException() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/BadRequestRestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */