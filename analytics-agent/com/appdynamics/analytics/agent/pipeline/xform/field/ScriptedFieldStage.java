/*    */ package com.appdynamics.analytics.agent.pipeline.xform.field;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.common.util.var.Variables;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ class ScriptedFieldStage
/*    */   extends FieldAdditionStage
/*    */ {
/*    */   final String[] scriptKeys;
/*    */   final ScriptedField[] scriptValues;
/*    */   final String[] varKeys;
/*    */   final String[] varValues;
/*    */   
/*    */   ScriptedFieldStage(PipelineStageParameters<Map<String, Object>> parameters, Map<String, Object> fieldKeyValues, Map<String, ScriptedField> scriptedFieldKeyValues, Map<String, String> variableKeyValues)
/*    */   {
/* 28 */     super(parameters, fieldKeyValues);
/*    */     
/* 30 */     this.scriptKeys = new String[scriptedFieldKeyValues.size()];
/* 31 */     this.scriptValues = new ScriptedField[scriptedFieldKeyValues.size()];
/* 32 */     int i = 0;
/* 33 */     for (Map.Entry<String, ScriptedField> entry : scriptedFieldKeyValues.entrySet()) {
/* 34 */       this.scriptKeys[i] = ((String)entry.getKey());
/* 35 */       this.scriptValues[(i++)] = ((ScriptedField)entry.getValue());
/*    */     }
/*    */     
/* 38 */     this.varKeys = new String[variableKeyValues.size()];
/* 39 */     this.varValues = new String[variableKeyValues.size()];
/* 40 */     i = 0;
/* 41 */     for (Map.Entry<String, String> entry : variableKeyValues.entrySet()) {
/* 42 */       this.varKeys[i] = ((String)entry.getKey());
/* 43 */       this.varValues[(i++)] = ((String)entry.getValue());
/*    */     }
/*    */   }
/*    */   
/*    */   public void process(Map<String, Object> input)
/*    */   {
/* 49 */     for (int i = 0; i < this.scriptKeys.length; i++) {
/* 50 */       Object o = this.scriptValues[i].value(this.pipelineId, input);
/* 51 */       input.put(this.scriptKeys[i], o);
/*    */     }
/* 53 */     for (int i = 0; i < this.varKeys.length; i++) {
/* 54 */       Map<String, Object> map = Variables.resolveValues(this.varValues[i], Variables.VARIABLE_RESOLVER_SYSTEM_AND_INCLUDE);
/*    */       
/* 56 */       if (map != null)
/*    */       {
/* 58 */         boolean picked = false;
/* 59 */         Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
/* 60 */         while (iterator.hasNext()) {
/* 61 */           if (!picked) {
/* 62 */             Map.Entry<String, Object> entry = (Map.Entry)iterator.next();
/* 63 */             Object o = entry.getValue();
/* 64 */             input.put(this.varKeys[i], o);
/* 65 */             picked = true;
/*    */           }
/* 67 */           iterator.remove();
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 72 */     super.process(input);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/field/ScriptedFieldStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */