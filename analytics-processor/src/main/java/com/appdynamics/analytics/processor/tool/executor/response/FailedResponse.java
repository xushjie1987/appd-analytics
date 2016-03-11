/*    */ package com.appdynamics.analytics.processor.tool.executor.response;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ public class FailedResponse
/*    */   implements ExecutionResponse
/*    */ {
/*    */   private int statusCode;
/*    */   private String responseMessage;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 13 */     if (o == this) return true; if (!(o instanceof FailedResponse)) return false; FailedResponse other = (FailedResponse)o; if (!other.canEqual(this)) return false; if (getStatusCode() != other.getStatusCode()) return false; Object this$responseMessage = getResponseMessage();Object other$responseMessage = other.getResponseMessage();return this$responseMessage == null ? other$responseMessage == null : this$responseMessage.equals(other$responseMessage); } public boolean canEqual(Object other) { return other instanceof FailedResponse; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + getStatusCode();Object $responseMessage = getResponseMessage();result = result * 31 + ($responseMessage == null ? 0 : $responseMessage.hashCode());return result; } public String toString() { return "FailedResponse(statusCode=" + getStatusCode() + ", responseMessage=" + getResponseMessage() + ")"; } @ConstructorProperties({"statusCode", "responseMessage"})
/* 14 */   public FailedResponse(int statusCode, String responseMessage) { this.statusCode = statusCode;this.responseMessage = responseMessage;
/*    */   }
/*    */   
/* 17 */   public int getStatusCode() { return this.statusCode; } public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
/* 18 */   public String getResponseMessage() { return this.responseMessage; } public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/response/FailedResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */