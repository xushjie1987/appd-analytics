/*    */ package com.appdynamics.analytics.agent.pipeline.xform.map;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class MapStage
/*    */   extends AbstractPipelineStage<Object, Map<String, Object>>
/*    */ {
/*    */   final String fieldName;
/*    */   
/*    */   MapStage(PipelineStageParameters<Map<String, Object>> parameters, String fieldName)
/*    */   {
/* 21 */     super(parameters);
/* 22 */     this.fieldName = fieldName;
/*    */   }
/*    */   
/*    */   public void process(Object input)
/*    */   {
/* 27 */     if (input == null) {
/* 28 */       return;
/*    */     }
/* 30 */     HashMap<String, Object> map = new HashMap(8);
/* 31 */     map.put(this.fieldName, input);
/* 32 */     invokeNext(map);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/map/MapStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */