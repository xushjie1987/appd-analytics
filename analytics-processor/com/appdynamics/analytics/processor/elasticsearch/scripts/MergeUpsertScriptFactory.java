/*    */ package com.appdynamics.analytics.processor.elasticsearch.scripts;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.elasticsearch.common.Nullable;
/*    */ import org.elasticsearch.script.NativeScriptFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MergeUpsertScriptFactory
/*    */   implements NativeScriptFactory
/*    */ {
/*    */   public MergeUpsertScript newScript(@Nullable Map<String, Object> params)
/*    */   {
/* 20 */     return new MergeUpsertScript(params);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/scripts/MergeUpsertScriptFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */