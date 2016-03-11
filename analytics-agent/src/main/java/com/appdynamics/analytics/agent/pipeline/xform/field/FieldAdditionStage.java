/*    */ package com.appdynamics.analytics.agent.pipeline.xform.field;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class FieldAdditionStage
/*    */   extends AbstractPipelineStage<Map<String, Object>, Map<String, Object>>
/*    */ {
/*    */   final String[] keys;
/*    */   final Object[] values;
/*    */   
/*    */   FieldAdditionStage(PipelineStageParameters<Map<String, Object>> parameters, Map<String, Object> fieldKeyValues)
/*    */   {
/* 23 */     super(parameters);
/*    */     
/* 25 */     this.keys = new String[fieldKeyValues.size()];
/* 26 */     this.values = new Object[fieldKeyValues.size()];
/* 27 */     int i = 0;
/* 28 */     for (Map.Entry<String, Object> entry : fieldKeyValues.entrySet()) {
/* 29 */       this.keys[i] = ((String)entry.getKey());
/* 30 */       this.values[(i++)] = entry.getValue();
/*    */     }
/*    */   }
/*    */   
/*    */   public void process(Map<String, Object> input)
/*    */   {
/* 36 */     for (int i = 0; i < this.keys.length; i++) {
/* 37 */       input.put(this.keys[i], this.values[i]);
/*    */     }
/*    */     
/* 40 */     invokeNext(input);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/field/FieldAdditionStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */