/*    */ package com.appdynamics.analytics.processor.rest;
/*    */ 
/*    */ import com.google.common.base.Strings;
/*    */ import com.google.common.base.Throwables;
/*    */ import javax.ws.rs.WebApplicationException;
/*    */ import javax.ws.rs.core.MediaType;
/*    */ import javax.ws.rs.core.Response;
/*    */ import javax.ws.rs.core.Response.ResponseBuilder;
/*    */ import javax.ws.rs.ext.ExceptionMapper;
/*    */ import javax.ws.rs.ext.Provider;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Provider
/*    */ public class RestErrorMapper
/*    */   implements ExceptionMapper<Exception>
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(RestErrorMapper.class);
/*    */   
/*    */ 
/*    */ 
/*    */   public Response toResponse(Exception exception)
/*    */   {
/* 29 */     log.trace("Mapping exception", exception);
/*    */     
/* 31 */     if ((exception instanceof RestError.RestServerException))
/* 32 */       return ((RestError.RestServerException)exception).getResponse();
/*    */     RestError.RestErrorBody payload;
/*    */     RestError.RestErrorBody payload;
/* 35 */     if ((exception instanceof WebApplicationException)) {
/* 36 */       Response response = ((WebApplicationException)exception).getResponse();
/* 37 */       String msg = exception.getMessage();
/* 38 */       if ((Strings.isNullOrEmpty(msg)) && (response.getEntity() != null)) {
/* 39 */         msg = response.getEntity().toString();
/*    */       }
/* 41 */       if (Strings.isNullOrEmpty(msg)) {
/* 42 */         msg = exception.getMessage();
/*    */       }
/* 44 */       payload = new RestError.RestErrorBody(response.getStatus(), "Library.Exception", msg, Throwables.getStackTraceAsString(exception));
/*    */ 
/*    */ 
/*    */     }
/*    */     else
/*    */     {
/*    */ 
/*    */ 
/* 52 */       payload = new RestError.RestErrorBody(500, "Unexpected.Exception", exception.getMessage(), Throwables.getStackTraceAsString(exception));
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 61 */     return Response.status(payload.getStatusCode()).type(MediaType.APPLICATION_JSON_TYPE).entity(payload).build();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/RestErrorMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */