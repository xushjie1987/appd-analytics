/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnavailableRestException
/*    */   extends RestException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int STATUS_CODE = 503;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public UnavailableRestException(String message, Throwable cause)
/*    */   {
/* 27 */     super(503, message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public UnavailableRestException(String message)
/*    */   {
/* 34 */     super(503, message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public UnavailableRestException(RestExceptionPayload payload)
/*    */   {
/* 41 */     super(payload);
/*    */   }
/*    */   
/*    */   public UnavailableRestException() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/UnavailableRestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */