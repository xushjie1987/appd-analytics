/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConflictRestException
/*    */   extends RestException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE = 409;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ConflictRestException(String message, Throwable cause)
/*    */   {
/* 28 */     super(409, message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ConflictRestException(String message)
/*    */   {
/* 35 */     super(409, message);
/*    */   }
/*    */   
/*    */   public ConflictRestException(RestExceptionPayload payload) {
/* 39 */     super(payload);
/*    */   }
/*    */   
/*    */   public ConflictRestException() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/ConflictRestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */