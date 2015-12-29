/*     */ package com.appdynamics.analytics.agent.pipeline.xform.field;
/*     */ 
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.appdynamics.common.util.configuration.Parameters;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.appdynamics.common.util.var.EchoVariableResolver;
/*     */ import com.appdynamics.common.util.var.Variables;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptedFieldStageFactory
/*     */   extends FieldAdditionStageFactory
/*     */ {
/*     */   static final String VAR_DATE_TIME = "${calendar.now.utc}";
/*     */   static final String VAR_PIPELINE_ID = "${pipeline.id}";
/*     */   
/*     */   public ScriptedFieldStage create(PipelineStageParameters<Map<String, Object>> parameters)
/*     */   {
/*  40 */     Map<String, Object> map = Parameters.consolidate(((FieldAdditionStageFactoryConfiguration)getConfiguration()).getFields(), (Map)extract(parameters));
/*     */     
/*  42 */     Map<String, ScriptedField> scriptMap = new HashMap(map.size());
/*  43 */     Map<String, String> varMap = new HashMap(map.size());
/*  44 */     for (Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
/*  45 */       Map.Entry<String, Object> entry = (Map.Entry)iterator.next();
/*     */       
/*  47 */       String value = Parameters.asString(entry.getValue(), null);
/*  48 */       if (value != null) {
/*  49 */         boolean foundStandardField = false;
/*  50 */         for (ScriptedFields scriptedField : ScriptedFields.values()) {
/*  51 */           if (value.equals(scriptedField.toString())) {
/*  52 */             scriptMap.put(entry.getKey(), scriptedField);
/*  53 */             iterator.remove();
/*  54 */             foundStandardField = true;
/*  55 */             break;
/*     */           }
/*     */         }
/*  58 */         if (!foundStandardField) {
/*  59 */           Map<String, Object> vars = Variables.resolveValues(value, EchoVariableResolver.INSTANCE);
/*     */           
/*  61 */           if (vars != null) {
/*  62 */             varMap.put(entry.getKey(), value);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  68 */     if ((scriptMap.isEmpty()) && (varMap.isEmpty())) {
/*  69 */       throw new ConfigurationException("Scripted fields have not been specified. Standard fields that can be placed on the value side of key-value pairs are " + Arrays.asList(new String[] { "${calendar.now.utc}", "${pipeline.id}" }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  75 */     validateMandatoryFields(map);
/*     */     
/*     */ 
/*  78 */     for (String key : varMap.keySet()) {
/*  79 */       map.remove(key);
/*     */     }
/*     */     
/*  82 */     return new ScriptedFieldStage(parameters, map, scriptMap, varMap);
/*     */   }
/*     */   
/*     */   protected Set<String> getMandatoryFieldNames()
/*     */   {
/*  87 */     return Collections.emptySet();
/*     */   }
/*     */   
/*     */   static abstract enum ScriptedFields implements ScriptedField {
/*  91 */     DATE_TIME, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     PIPELINE_ID;
/*     */     
/*     */     private ScriptedFields() {}
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/field/ScriptedFieldStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */