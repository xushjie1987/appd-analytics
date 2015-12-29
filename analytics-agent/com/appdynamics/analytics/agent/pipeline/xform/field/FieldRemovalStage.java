/*    */ package com.appdynamics.analytics.agent.pipeline.xform.field;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class FieldRemovalStage
/*    */   extends AbstractPipelineStage<Map<String, Object>, Map<String, Object>>
/*    */ {
/*    */   final Object[] fieldKeys;
/*    */   
/*    */   FieldRemovalStage(PipelineStageParameters<Map<String, Object>> parameters, Set<String> fieldKeys)
/*    */   {
/* 23 */     super(parameters);
/* 24 */     this.fieldKeys = fieldKeys.toArray();
/*    */   }
/*    */   
/*    */ 
/*    */   public void process(Map<String, Object> input)
/*    */   {
/* 30 */     for (Object fieldKey : this.fieldKeys) {
/* 31 */       input.remove(fieldKey);
/*    */     }
/*    */     
/* 34 */     invokeNext(input);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/field/FieldRemovalStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */