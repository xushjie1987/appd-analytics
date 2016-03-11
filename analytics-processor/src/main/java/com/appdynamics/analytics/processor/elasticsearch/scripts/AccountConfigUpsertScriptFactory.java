/*    */ package com.appdynamics.analytics.processor.elasticsearch.scripts;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.elasticsearch.common.Nullable;
/*    */ import org.elasticsearch.script.ExecutableScript;
/*    */ import org.elasticsearch.script.NativeScriptFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccountConfigUpsertScriptFactory
/*    */   implements NativeScriptFactory
/*    */ {
/*    */   public ExecutableScript newScript(@Nullable Map<String, Object> params)
/*    */   {
/* 20 */     return new AccountConfigUpsertScript(params);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/scripts/AccountConfigUpsertScriptFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */