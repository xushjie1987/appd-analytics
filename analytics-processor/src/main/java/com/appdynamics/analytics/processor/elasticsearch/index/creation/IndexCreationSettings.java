/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.creation;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ public class IndexCreationSettings
/*    */ {
/*    */   private final Map<String, Object> settings;
/*    */   
/*    */   public IndexCreationSettings()
/*    */   {
/* 30 */     this.settings = new HashMap();
/*    */   }
/*    */   
/*    */   public IndexCreationSettings(Map<String, Object> settings) {
/* 34 */     this.settings = settings;
/*    */   }
/*    */   
/*    */   public IndexCreationSettings setNumShards(int numShards) {
/* 38 */     this.settings.put("number_of_shards", Integer.valueOf(numShards));
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public IndexCreationSettings setNumReplicas(int numReplicas) {
/* 43 */     this.settings.put("number_of_replicas", Integer.valueOf(numReplicas));
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public IndexCreationSettings setIndexMapperDynamic(boolean indexMapperDynamic) {
/* 48 */     this.settings.put("index.mapper.dynamic", Boolean.valueOf(indexMapperDynamic));
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getSettings() {
/* 53 */     return this.settings;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/creation/IndexCreationSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */