/*    */ package com.appdynamics.analytics.agent.pipeline.xform.field;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.configuration.Parameters;
/*    */ import com.google.common.collect.ImmutableSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldAdditionStageFactory
/*    */   extends AbstractPipelineStageFactory<Map<String, Object>, Map<String, Object>, FieldAdditionStageFactoryConfiguration, Map<String, Object>>
/*    */ {
/* 31 */   private static final Set<String> MANDATORY_FIELDS = ImmutableSet.of("host", "source", "sourceType");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FieldAdditionStage create(PipelineStageParameters<Map<String, Object>> parameters)
/*    */   {
/* 38 */     Map<String, Object> map = Parameters.consolidate(((FieldAdditionStageFactoryConfiguration)getConfiguration()).getFields(), (Map)extract(parameters));
/* 39 */     validateMandatoryFields(map);
/* 40 */     return new FieldAdditionStage(parameters, map);
/*    */   }
/*    */   
/*    */   protected Set<String> getMandatoryFieldNames() {
/* 44 */     return MANDATORY_FIELDS;
/*    */   }
/*    */   
/*    */   void validateMandatoryFields(Map<String, Object> map) {
/* 48 */     for (String mandatory : getMandatoryFieldNames()) {
/* 49 */       if (!map.containsKey(mandatory)) {
/* 50 */         throw new ConfigurationException("The following fields have been marked as mandatory but the stage configuration does not seem to have these fields defined " + getMandatoryFieldNames() + ". Found " + map.keySet());
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/field/FieldAdditionStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */