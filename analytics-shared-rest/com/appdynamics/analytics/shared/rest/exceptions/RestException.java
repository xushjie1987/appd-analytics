/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ import com.google.common.base.Throwables;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RestException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private int statusCode;
/*    */   private String code;
/*    */   private String message;
/*    */   private String developerMessage;
/*    */   
/*    */   public String toString()
/*    */   {
/* 18 */     return "RestException(statusCode=" + getStatusCode() + ", code=" + getCode() + ", message=" + getMessage() + ", developerMessage=" + getDeveloperMessage() + ")";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 23 */   public int getStatusCode() { return this.statusCode; }
/*    */   
/* 25 */   public String getCode() { return this.code; }
/*    */   
/* 27 */   public String getMessage() { return this.message; }
/*    */   
/* 29 */   public String getDeveloperMessage() { return this.developerMessage; }
/*    */   
/*    */   public RestException(RestExceptionPayload payload) {
/* 32 */     super(payload.toString());
/* 33 */     this.statusCode = payload.getStatusCode();
/* 34 */     this.code = payload.getCode();
/* 35 */     this.message = payload.getMessage();
/* 36 */     this.developerMessage = payload.getDeveloperMessage();
/*    */   }
/*    */   
/*    */   public RestException(int statusCode, String message) {
/* 40 */     super(message);
/* 41 */     this.statusCode = statusCode;
/* 42 */     this.message = message;
/* 43 */     this.developerMessage = Throwables.getStackTraceAsString(this);
/*    */   }
/*    */   
/*    */   public RestException(int statusCode, String message, Throwable cause) {
/* 47 */     super(message, cause);
/* 48 */     this.statusCode = statusCode;
/* 49 */     this.message = message;
/* 50 */     this.developerMessage = Throwables.getStackTraceAsString(this);
/*    */   }
/*    */   
/*    */   public RestException(int statusCode, String message, String developerMessage) {
/* 54 */     super(message);
/* 55 */     this.statusCode = statusCode;
/* 56 */     this.message = message;
/* 57 */     this.developerMessage = developerMessage;
/*    */   }
/*    */   
/*    */   public RestException() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/RestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */