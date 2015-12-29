/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NotFoundRestException
/*    */   extends RestException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE = 404;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public NotFoundRestException(String message, Throwable cause)
/*    */   {
/* 28 */     super(404, message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public NotFoundRestException(String message)
/*    */   {
/* 35 */     super(404, message);
/*    */   }
/*    */   
/*    */   public NotFoundRestException(RestExceptionPayload payload) {
/* 39 */     super(payload);
/*    */   }
/*    */   
/*    */   public NotFoundRestException() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/NotFoundRestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */