/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
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
/*    */ public class NotAcceptableRestException
/*    */   extends RestException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
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
/*    */   public static final int STATUS_CODE = 406;
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
/*    */   public NotAcceptableRestException(String message, Throwable cause)
/*    */   {
/* 40 */     super(406, message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public NotAcceptableRestException(String message)
/*    */   {
/* 47 */     super(406, message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public NotAcceptableRestException(RestExceptionPayload payload)
/*    */   {
/* 54 */     super(payload);
/*    */   }
/*    */   
/*    */   public NotAcceptableRestException() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/NotAcceptableRestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */