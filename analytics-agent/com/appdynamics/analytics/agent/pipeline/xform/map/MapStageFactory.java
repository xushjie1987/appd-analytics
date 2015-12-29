/*    */ package com.appdynamics.analytics.agent.pipeline.xform.map;
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
/*    */ public class MapStageFactory
/*    */   extends AbstractPipelineStageFactory<Object, Map<String, Object>, MapConfiguration, MapConfiguration>
/*    */ {
/* 22 */   static final MapConfiguration DEFAULT_CONFIG = new MapConfiguration();
/*    */   
/*    */   public MapStage create(PipelineStageParameters<Map<String, Object>> parameters)
/*    */   {
/* 26 */     MapConfiguration mc = (MapConfiguration)Parameters.firstNonNull(extract(parameters), getConfiguration(), DEFAULT_CONFIG);
/*    */     
/* 28 */     return new MapStage(parameters, mc.getFieldName());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/map/MapStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */