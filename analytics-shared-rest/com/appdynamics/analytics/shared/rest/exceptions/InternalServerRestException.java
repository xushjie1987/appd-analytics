/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InternalServerRestException
/*    */   extends RestException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE = 500;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public InternalServerRestException(String message, Throwable cause)
/*    */   {
/* 27 */     super(500, message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public InternalServerRestException(String message)
/*    */   {
/* 34 */     super(500, message);
/*    */   }
/*    */   
/*    */   public InternalServerRestException(RestExceptionPayload payload) {
/* 38 */     super(payload);
/*    */   }
/*    */   
/*    */   public InternalServerRestException() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/InternalServerRestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */