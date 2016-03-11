/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.configuration;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import java.util.Map;
/*    */ import lombok.NonNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class MetaDataIndexConfiguration
/*    */ {
/*    */   String alias;
/*    */   @NonNull
/*    */   Map<String, Object> settings;
/*    */   @NonNull
/*    */   Map<String, Map<String, Object>> mappings;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 24 */     if (o == this) return true; if (!(o instanceof MetaDataIndexConfiguration)) return false; MetaDataIndexConfiguration other = (MetaDataIndexConfiguration)o; if (!other.canEqual(this)) return false; Object this$alias = getAlias();Object other$alias = other.getAlias(); if (this$alias == null ? other$alias != null : !this$alias.equals(other$alias)) return false; Object this$settings = getSettings();Object other$settings = other.getSettings(); if (this$settings == null ? other$settings != null : !this$settings.equals(other$settings)) return false; Object this$mappings = getMappings();Object other$mappings = other.getMappings();return this$mappings == null ? other$mappings == null : this$mappings.equals(other$mappings); } public boolean canEqual(Object other) { return other instanceof MetaDataIndexConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $alias = getAlias();result = result * 31 + ($alias == null ? 0 : $alias.hashCode());Object $settings = getSettings();result = result * 31 + ($settings == null ? 0 : $settings.hashCode());Object $mappings = getMappings();result = result * 31 + ($mappings == null ? 0 : $mappings.hashCode());return result; } public String toString() { return "MetaDataIndexConfiguration(alias=" + getAlias() + ", settings=" + getSettings() + ", mappings=" + getMappings() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 32 */   public String getAlias() { return this.alias; } public void setAlias(String alias) { this.alias = alias; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @NonNull
/* 38 */   public Map<String, Object> getSettings() { return this.settings; } public void setSettings(@NonNull Map<String, Object> settings) { if (settings == null) throw new NullPointerException("settings"); this.settings = settings;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @NonNull
/* 44 */   public Map<String, Map<String, Object>> getMappings() { return this.mappings; } public void setMappings(@NonNull Map<String, Map<String, Object>> mappings) { if (mappings == null) throw new NullPointerException("mappings"); this.mappings = mappings;
/*    */   }
/*    */   
/* 47 */   public MetaDataIndexConfiguration(Map<String, Object> settings, Map<String, Map<String, Object>> mappings) { this.settings = settings;
/* 48 */     this.mappings = mappings;
/*    */   }
/*    */   
/*    */   public MetaDataIndexConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/configuration/MetaDataIndexConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */