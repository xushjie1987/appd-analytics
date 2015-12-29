/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*    */ import com.fasterxml.jackson.databind.node.ObjectNode;
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
/*    */ public class BulkFailureException
/*    */   extends RuntimeException
/*    */ {
/* 24 */   public List<BulkFailure> getFailures() { return this.failures; } List<BulkFailure> failures = new ArrayList();
/*    */   
/*    */   public BulkFailureException(String message)
/*    */   {
/* 28 */     super(message);
/*    */   }
/*    */   
/*    */   public BulkFailureException(String message, Throwable cause) {
/* 32 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public BulkFailureException(Throwable cause) {
/* 36 */     super(cause);
/*    */   }
/*    */   
/*    */   public String toJson() {
/* 40 */     ObjectMapper mapper = Reader.DEFAULT_JSON_MAPPER;
/* 41 */     ObjectNode objectNode = mapper.createObjectNode();
/* 42 */     objectNode.put("message", getMessage());
/* 43 */     objectNode.put("failures", (JsonNode)mapper.convertValue(this.failures, ArrayNode.class));
/* 44 */     return objectNode.toString();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/BulkFailureException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */