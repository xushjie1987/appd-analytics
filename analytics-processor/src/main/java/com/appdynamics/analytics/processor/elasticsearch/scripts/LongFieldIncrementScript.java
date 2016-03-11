/*    */ package com.appdynamics.analytics.processor.elasticsearch.scripts;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.elasticsearch.script.AbstractExecutableScript;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LongFieldIncrementScript
/*    */   extends AbstractExecutableScript
/*    */ {
/* 18 */   private static final Logger log = LoggerFactory.getLogger(LongFieldIncrementScript.class);
/*    */   
/*    */   static final String ES_FIELD_CTX = "ctx";
/*    */   
/*    */   static final String ES_FIELD_SOURCE = "_source";
/*    */   
/*    */   private final String fieldName;
/*    */   
/*    */   private final long incrementBy;
/*    */   
/*    */   private Map ctx;
/*    */   
/*    */   public LongFieldIncrementScript(String fieldName, long incrementBy)
/*    */   {
/* 32 */     this.fieldName = fieldName;
/* 33 */     this.incrementBy = incrementBy;
/*    */   }
/*    */   
/*    */   public void setNextVar(String name, Object value)
/*    */   {
/* 38 */     if (((value instanceof Map)) && ("ctx".equals(name))) {
/* 39 */       this.ctx = ((Map)value);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public Object run()
/*    */   {
/* 46 */     Map source = (Map)this.ctx.get("_source");
/* 47 */     if (source == null) {
/* 48 */       throw new IllegalStateException("The context does not contain [_source]");
/*    */     }
/*    */     
/* 51 */     long longValue = this.incrementBy;
/*    */     
/*    */ 
/* 54 */     Object value = source.get(this.fieldName);
/* 55 */     if (value != null) {
/* 56 */       if ((value instanceof Number)) {
/* 57 */         longValue += ((Number)value).longValue();
/*    */       } else {
/* 59 */         String s = String.valueOf(value);
/* 60 */         longValue += Long.parseLong(s);
/*    */       }
/*    */     }
/*    */     
/* 64 */     source.put(this.fieldName, Long.valueOf(longValue));
/*    */     
/* 66 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/scripts/LongFieldIncrementScript.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */