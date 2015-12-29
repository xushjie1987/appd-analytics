/*    */ package com.appdynamics.analytics.processor.rest;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.Exceptions;
/*    */ import com.google.common.base.Throwables;
/*    */ import java.beans.ConstructorProperties;
/*    */ import javax.ws.rs.WebApplicationException;
/*    */ import javax.ws.rs.core.Response;
/*    */ import javax.ws.rs.core.Response.ResponseBuilder;
/*    */ import javax.ws.rs.core.Response.Status;
/*    */ import javax.xml.bind.annotation.XmlRootElement;
/*    */ import javax.xml.bind.annotation.XmlType;
/*    */ import lombok.NonNull;
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
/*    */ 
/*    */ 
/*    */ public class RestError
/*    */ {
/*    */   public static WebApplicationException create(RestErrorCode restErrorCode, String message)
/*    */   {
/* 36 */     return create(restErrorCode, message, "");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static WebApplicationException create(RestErrorCode restErrorCode, String message, Throwable t)
/*    */   {
/* 46 */     Throwable root = Throwables.getRootCause(t);
/*    */     
/* 48 */     String trace = Throwables.getStackTraceAsString(root);
/*    */     
/* 50 */     return create(restErrorCode, message, String.format("Exception Message Chain : %s%nTrace : %s", new Object[] { Exceptions.collectMessages(t), trace }));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static WebApplicationException create(RestErrorCode restErrorCode, String message, String developerMessage)
/*    */   {
/* 62 */     RestErrorBody errorBody = new RestErrorBody(restErrorCode.getStatus().getStatusCode(), restErrorCode.getSubStatus(), message, developerMessage);
/*    */     
/*    */ 
/* 65 */     Response response = Response.status(restErrorCode.getStatus()).type("application/json").entity(errorBody).build();
/*    */     
/*    */ 
/*    */ 
/* 69 */     return new RestServerException(response);
/*    */   }
/*    */   
/*    */ 
/*    */   static class RestServerException
/*    */     extends WebApplicationException
/*    */   {
/*    */     RestServerException(Response response)
/*    */     {
/* 78 */       super();
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 83 */     public String getMessage() { return "Status [" + getResponse().getStatus() + "], message [" + getResponse().getEntity() + "]"; } }
/*    */   
/*    */   @XmlRootElement
/*    */   @XmlType(propOrder={"statusCode", "code", "message", "developerMessage"})
/*    */   public static class RestErrorBody { @ConstructorProperties({"statusCode", "code", "message", "developerMessage"})
/* 88 */     RestErrorBody(int statusCode, @NonNull String code, String message, String developerMessage) { if (code == null) throw new NullPointerException("code"); this.statusCode = statusCode;this.code = code;this.message = message;this.developerMessage = developerMessage; }
/*    */     
/*    */     int statusCode;
/*    */     
/* 92 */     public String toString() { return "RestError.RestErrorBody(statusCode=" + getStatusCode() + ", code=" + getCode() + ", message=" + getMessage() + ", developerMessage=" + getDeveloperMessage() + ")"; }
/*    */     
/* 94 */     public int getStatusCode() { return this.statusCode; }
/*    */     @NonNull
/* 96 */     public String getCode() { return this.code; }
/*    */     
/* 98 */     public String getMessage() { return this.message; }
/* 99 */     public String getDeveloperMessage() { return this.developerMessage; }
/*    */     
/*    */     @NonNull
/*    */     String code;
/*    */     String message;
/*    */     String developerMessage;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/RestError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */