/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnauthorizedRestException
/*    */   extends RestException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE = 401;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public UnauthorizedRestException(String message, Throwable cause)
/*    */   {
/* 27 */     super(401, message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public UnauthorizedRestException(String message)
/*    */   {
/* 34 */     super(401, message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public UnauthorizedRestException(RestExceptionPayload payload)
/*    */   {
/* 41 */     super(payload);
/*    */   }
/*    */   
/*    */   public UnauthorizedRestException() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/UnauthorizedRestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */