/*    */ package com.appdynamics.analytics.processor.tool.executor;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.TreeNode;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*    */ import com.fasterxml.jackson.databind.node.TextNode;
/*    */ import java.io.IOException;
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
/*    */ public class ExecutionStepDeserializer
/*    */   extends JsonDeserializer<ExecutionStep>
/*    */ {
/*    */   public ExecutionStep deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException
/*    */   {
/* 27 */     TreeNode tn = jp.readValueAsTree();
/* 28 */     return Executor.buildExecutionStep(((TextNode)tn.get("className")).asText(), (ObjectNode)tn.get("properties"), ((TextNode)tn.get("stepName")).asText());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/ExecutionStepDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */