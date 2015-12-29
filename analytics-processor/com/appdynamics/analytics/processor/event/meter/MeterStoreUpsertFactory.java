/*    */ package com.appdynamics.analytics.processor.event.meter;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.scripts.LongFieldIncrementScript;
/*    */ import java.util.Map;
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
/*    */ public class MeterStoreUpsertFactory
/*    */   implements NativeScriptFactory
/*    */ {
/*    */   public ExecutableScript newScript(Map<String, Object> params)
/*    */   {
/* 20 */     Long incrementBy = (Long)params.get("countNow");
/* 21 */     Long incrementOverage = (Long)params.get("overageNow");
/* 22 */     if ((incrementOverage != null) && (incrementOverage.longValue() > 0L)) {
/* 23 */       return new LongFieldIncrementScript("overageTotal", incrementOverage.longValue());
/*    */     }
/* 25 */     return new LongFieldIncrementScript("countTotal", incrementBy.longValue());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/MeterStoreUpsertFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */