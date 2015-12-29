/*    */ package com.appdynamics.analytics.agent.pipeline.xform.json;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
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
/*    */ public class MapsToJsonStageFactory
/*    */   extends AbstractPipelineStageFactory<Map<Object, Object>, String, Object, MapsToJsonStageConfiguration>
/*    */ {
/*    */   public PipelineStage<Map<Object, Object>, String> create(PipelineStageParameters<String> parameters)
/*    */   {
/* 27 */     MapsToJsonStageConfiguration configuration = (MapsToJsonStageConfiguration)extract(parameters);
/* 28 */     return new MapsToJsonStage(parameters, configuration.isClearMaps());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/json/MapsToJsonStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */