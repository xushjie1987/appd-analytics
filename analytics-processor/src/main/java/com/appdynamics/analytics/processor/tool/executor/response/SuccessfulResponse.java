/*    */ package com.appdynamics.analytics.processor.tool.executor.response;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ import lombok.NonNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SuccessfulResponse
/*    */   implements ExecutionResponse
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 14 */     if (o == this) return true; if (!(o instanceof SuccessfulResponse)) return false; SuccessfulResponse other = (SuccessfulResponse)o; if (!other.canEqual(this)) return false; if (getStatusCode() != other.getStatusCode()) return false; Object this$responseMessage = getResponseMessage();Object other$responseMessage = other.getResponseMessage();return this$responseMessage == null ? other$responseMessage == null : this$responseMessage.equals(other$responseMessage); } public boolean canEqual(Object other) { return other instanceof SuccessfulResponse; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + getStatusCode();Object $responseMessage = getResponseMessage();result = result * 31 + ($responseMessage == null ? 0 : $responseMessage.hashCode());return result; } public String toString() { return "SuccessfulResponse(statusCode=" + getStatusCode() + ", responseMessage=" + getResponseMessage() + ")"; } @ConstructorProperties({"responseMessage"})
/* 15 */   public SuccessfulResponse(@NonNull String responseMessage) { if (responseMessage == null) throw new NullPointerException("responseMessage"); this.responseMessage = responseMessage;
/*    */   }
/*    */   
/* 18 */   public int getStatusCode() { return this.statusCode; } public void setStatusCode(int statusCode) { this.statusCode = statusCode; } private int statusCode = 0;
/*    */   @NonNull
/* 20 */   public String getResponseMessage() { return this.responseMessage; } public void setResponseMessage(@NonNull String responseMessage) { if (responseMessage == null) throw new NullPointerException("responseMessage"); this.responseMessage = responseMessage;
/*    */   }
/*    */   
/*    */   @NonNull
/*    */   private String responseMessage;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/response/SuccessfulResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */