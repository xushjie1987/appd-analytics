/*    */ package com.appdynamics.analytics.shared.rest.exceptions;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonAnySetter;
/*    */ import com.google.common.base.Throwables;
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ public class RestExceptionPayload
/*    */ {
/*    */   private int statusCode;
/*    */   private String code;
/*    */   private String message;
/*    */   private String developerMessage;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 16 */     if (o == this) return true; if (!(o instanceof RestExceptionPayload)) return false; RestExceptionPayload other = (RestExceptionPayload)o; if (!other.canEqual(this)) return false; if (getStatusCode() != other.getStatusCode()) return false; Object this$code = getCode();Object other$code = other.getCode(); if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false; Object this$message = getMessage();Object other$message = other.getMessage(); if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false; Object this$developerMessage = getDeveloperMessage();Object other$developerMessage = other.getDeveloperMessage();return this$developerMessage == null ? other$developerMessage == null : this$developerMessage.equals(other$developerMessage); } public boolean canEqual(Object other) { return other instanceof RestExceptionPayload; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + getStatusCode();Object $code = getCode();result = result * 31 + ($code == null ? 0 : $code.hashCode());Object $message = getMessage();result = result * 31 + ($message == null ? 0 : $message.hashCode());Object $developerMessage = getDeveloperMessage();result = result * 31 + ($developerMessage == null ? 0 : $developerMessage.hashCode());return result; }
/* 17 */   public String toString() { return "RestExceptionPayload(statusCode=" + getStatusCode() + ", code=" + getCode() + ", message=" + getMessage() + ", developerMessage=" + getDeveloperMessage() + ")"; } @ConstructorProperties({"statusCode", "code", "message", "developerMessage"})
/* 18 */   public RestExceptionPayload(int statusCode, String code, String message, String developerMessage) { this.statusCode = statusCode;this.code = code;this.message = message;this.developerMessage = developerMessage;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 25 */   public int getStatusCode() { return this.statusCode; }
/* 26 */   public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 32 */   public String getCode() { return this.code; }
/* 33 */   public void setCode(String code) { this.code = code; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 39 */   public String getMessage() { return this.message; }
/* 40 */   public void setMessage(String message) { this.message = message; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDeveloperMessage()
/*    */   {
/* 47 */     return this.developerMessage;
/*    */   }
/*    */   
/*    */   public void setDeveloperMessage(String developerMessage) {
/* 51 */     if (this.developerMessage == null) {
/* 52 */       this.developerMessage = developerMessage;
/*    */     } else {
/* 54 */       this.developerMessage = (this.developerMessage + ", " + developerMessage);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @JsonAnySetter
/*    */   public void set(String name, String value)
/*    */   {
/* 66 */     String newMessage = "[" + name + ":" + value + "]";
/* 67 */     if (this.developerMessage == null) {
/* 68 */       this.developerMessage = newMessage;
/*    */     } else {
/* 70 */       this.developerMessage = (this.developerMessage + ", " + newMessage);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RestExceptionPayload(int statusCode, Exception exception)
/*    */   {
/* 80 */     this(statusCode, "", exception.getMessage(), Throwables.getStackTraceAsString(exception));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RestExceptionPayload(int statusCode, String msg, Exception exception)
/*    */   {
/* 89 */     this(statusCode, "", msg, Throwables.getStackTraceAsString(exception));
/*    */   }
/*    */   
/*    */   public RestExceptionPayload() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/exceptions/RestExceptionPayload.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */