/*    */ package com.appdynamics.analytics.processor.tool.executor.response;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class BulkStepExecutionResponse
/*    */   implements ExecutionResponse
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 21 */     if (o == this) return true; if (!(o instanceof BulkStepExecutionResponse)) return false; BulkStepExecutionResponse other = (BulkStepExecutionResponse)o; if (!other.canEqual(this)) return false; Object this$responses = getResponses();Object other$responses = other.getResponses();return this$responses == null ? other$responses == null : this$responses.equals(other$responses); } public boolean canEqual(Object other) { return other instanceof BulkStepExecutionResponse; } public int hashCode() { int PRIME = 31;int result = 1;Object $responses = getResponses();result = result * 31 + ($responses == null ? 0 : $responses.hashCode());return result; } public String toString() { return "BulkStepExecutionResponse(responses=" + getResponses() + ")"; }
/*    */   
/* 23 */   public List<ExecutionResponse> getResponses() { return this.responses; } public void setResponses(List<ExecutionResponse> responses) { this.responses = responses; } List<ExecutionResponse> responses = new ArrayList();
/*    */   
/*    */   public int getStatusCode()
/*    */   {
/* 27 */     return anyFailedResponse() ? 1 : 0;
/*    */   }
/*    */   
/*    */   private boolean anyFailedResponse() {
/* 31 */     for (ExecutionResponse response : this.responses) {
/* 32 */       if ((response instanceof FailedResponse)) {
/* 33 */         return true;
/*    */       }
/*    */     }
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public String getResponseMessage()
/*    */   {
/* 41 */     StringBuilder sb = new StringBuilder();
/* 42 */     for (ExecutionResponse response : this.responses) {
/* 43 */       if ((response != null) && (response.getResponseMessage() != null)) {
/* 44 */         if (sb.length() > 0) {
/* 45 */           sb.append("\n");
/*    */         }
/* 47 */         sb.append(response.getResponseMessage());
/*    */       }
/*    */     }
/* 50 */     return sb.toString();
/*    */   }
/*    */   
/*    */   public void add(ExecutionResponse executionResponse) {
/* 54 */     this.responses.add(executionResponse);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/response/BulkStepExecutionResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */