/*    */ package com.appdynamics.analytics.agent.pipeline.xform.field;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.appdynamics.common.util.configuration.Parameters;
/*    */ import java.util.Map;
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
/*    */ public class FieldRemovalStageFactory
/*    */   extends AbstractPipelineStageFactory<Map<String, Object>, Map<String, Object>, Map<String, Object>, Map<String, Object>>
/*    */ {
/*    */   public FieldRemovalStage create(PipelineStageParameters<Map<String, Object>> parameters)
/*    */   {
/* 29 */     Map<String, Object> map = Parameters.consolidate((Map)getConfiguration(), (Map)extract(parameters));
/*    */     
/* 31 */     return new FieldRemovalStage(parameters, map.keySet());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/field/FieldRemovalStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */