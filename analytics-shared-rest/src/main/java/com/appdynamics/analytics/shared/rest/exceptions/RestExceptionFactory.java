/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.StatusLine;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RestExceptionFactory
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(RestExceptionFactory.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static RestException makeException(RestExceptionPayload payload)
/*    */   {
/*    */     RestException exc;
/*    */     
/*    */ 
/* 36 */     switch (payload.getStatusCode()) {
/*    */     case 400: 
/* 38 */       exc = new BadRequestRestException(payload);
/* 39 */       break;
/*    */     case 401: 
/* 41 */       exc = new UnauthorizedRestException(payload);
/* 42 */       break;
/*    */     case 404: 
/* 44 */       exc = new NotFoundRestException(payload);
/* 45 */       break;
/*    */     case 406: 
/* 47 */       exc = new NotAcceptableRestException(payload);
/* 48 */       break;
/*    */     case 409: 
/* 50 */       exc = new ConflictRestException(payload);
/* 51 */       break;
/*    */     case 500: 
/* 53 */       exc = new InternalServerRestException(payload);
/* 54 */       break;
/*    */     case 503: 
/* 56 */       exc = new UnavailableRestException(payload);
/* 57 */       break;
/*    */     
/*    */     default: 
/* 60 */       log.debug("Unknown exception status code: {}", Integer.valueOf(payload.getStatusCode()));
/* 61 */       exc = new RestException(payload);
/*    */     }
/*    */     
/* 64 */     return exc;
/*    */   }
/*    */   
/*    */   public static RestException makeUnexpectedResponseException(HttpResponse response) {
/* 68 */     return new RestException(response.getStatusLine().getStatusCode(), "No additional information", "No additional information");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/RestExceptionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */