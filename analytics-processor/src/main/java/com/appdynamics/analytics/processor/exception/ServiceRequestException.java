/*    */ package com.appdynamics.analytics.processor.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServiceRequestException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final String code;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCode()
/*    */   {
/* 19 */     return this.code;
/*    */   }
/*    */   
/*    */   public ServiceRequestException(String code, String message) {
/* 23 */     super(message);
/* 24 */     this.code = code;
/*    */   }
/*    */   
/*    */   public ServiceRequestException(String code, String message, Throwable cause) {
/* 28 */     super(message, cause);
/* 29 */     this.code = code;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/exception/ServiceRequestException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */