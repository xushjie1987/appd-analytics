/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.configuration;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.validation.constraints.Min;
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
/*    */ public class IndexManagementModuleConfiguration
/*    */ {
/*    */   public static final long DEFAULT_CACHE_EXPIRY_MILLIS = 300000L;
/*    */   
/*    */   public String toString()
/*    */   {
/* 23 */     return "IndexManagementModuleConfiguration(indexCacheExpiryMillis=" + getIndexCacheExpiryMillis() + ", indexConfigurations=" + getIndexConfigurations() + ", indexCompactionConfiguration=" + getIndexCompactionConfiguration() + ", rollingIndexConfiguration=" + getRollingIndexConfiguration() + ")";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 28 */   public long getIndexCacheExpiryMillis() { return this.indexCacheExpiryMillis; } @JsonProperty
/*    */   @Min(300000L)
/* 28 */   long indexCacheExpiryMillis = 300000L;
/* 29 */   public void setIndexCacheExpiryMillis(long indexCacheExpiryMillis) { this.indexCacheExpiryMillis = indexCacheExpiryMillis; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */   public Map<String, MetaDataIndexConfiguration> getIndexConfigurations() { return this.indexConfigurations; } @JsonProperty
/* 37 */   Map<String, MetaDataIndexConfiguration> indexConfigurations = new HashMap();
/* 38 */   public void setIndexConfigurations(Map<String, MetaDataIndexConfiguration> indexConfigurations) { this.indexConfigurations = indexConfigurations; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 45 */   public IndexCompactionConfiguration getIndexCompactionConfiguration() { return this.indexCompactionConfiguration; } @JsonProperty
/* 45 */   IndexCompactionConfiguration indexCompactionConfiguration = new IndexCompactionConfiguration();
/* 46 */   public void setIndexCompactionConfiguration(IndexCompactionConfiguration indexCompactionConfiguration) { this.indexCompactionConfiguration = indexCompactionConfiguration; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 53 */   public RollingIndexConfiguration getRollingIndexConfiguration() { return this.rollingIndexConfiguration; } @JsonProperty
/* 53 */   RollingIndexConfiguration rollingIndexConfiguration = new RollingIndexConfiguration();
/* 54 */   public void setRollingIndexConfiguration(RollingIndexConfiguration rollingIndexConfiguration) { this.rollingIndexConfiguration = rollingIndexConfiguration; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/configuration/IndexManagementModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */