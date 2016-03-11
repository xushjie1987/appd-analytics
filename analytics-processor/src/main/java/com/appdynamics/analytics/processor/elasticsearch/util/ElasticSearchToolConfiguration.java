/*    */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ import javax.validation.constraints.Min;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElasticSearchToolConfiguration
/*    */ {
/*    */   ElasticSearchStoreToolConfiguration storeToolConfiguration;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 17 */     if (o == this) return true; if (!(o instanceof ElasticSearchToolConfiguration)) return false; ElasticSearchToolConfiguration other = (ElasticSearchToolConfiguration)o; if (!other.canEqual(this)) return false; Object this$storeToolConfiguration = getStoreToolConfiguration();Object other$storeToolConfiguration = other.getStoreToolConfiguration();return this$storeToolConfiguration == null ? other$storeToolConfiguration == null : this$storeToolConfiguration.equals(other$storeToolConfiguration); } public boolean canEqual(Object other) { return other instanceof ElasticSearchToolConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $storeToolConfiguration = getStoreToolConfiguration();result = result * 31 + ($storeToolConfiguration == null ? 0 : $storeToolConfiguration.hashCode());return result; } public String toString() { return "ElasticSearchToolConfiguration(storeToolConfiguration=" + getStoreToolConfiguration() + ")"; }
/*    */   @ConstructorProperties({"storeToolConfiguration"})
/* 19 */   public ElasticSearchToolConfiguration(ElasticSearchStoreToolConfiguration storeToolConfiguration) { this.storeToolConfiguration = storeToolConfiguration; }
/*    */   
/*    */ 
/* 22 */   public ElasticSearchStoreToolConfiguration getStoreToolConfiguration() { return this.storeToolConfiguration; } public void setStoreToolConfiguration(ElasticSearchStoreToolConfiguration storeToolConfiguration) { this.storeToolConfiguration = storeToolConfiguration; }
/*    */   
/* 24 */   public static class ElasticSearchStoreToolConfiguration { public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ElasticSearchStoreToolConfiguration)) return false; ElasticSearchStoreToolConfiguration other = (ElasticSearchStoreToolConfiguration)o; if (!other.canEqual(this)) return false; if (getSamplePeriodInSeconds() != other.getSamplePeriodInSeconds()) return false; if (getSlidingWindowLengthInMinutes() != other.getSlidingWindowLengthInMinutes()) return false; return isEnabled() == other.isEnabled(); } public boolean canEqual(Object other) { return other instanceof ElasticSearchStoreToolConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + getSamplePeriodInSeconds();result = result * 31 + getSlidingWindowLengthInMinutes();result = result * 31 + (isEnabled() ? 1231 : 1237);return result; } public String toString() { return "ElasticSearchToolConfiguration.ElasticSearchStoreToolConfiguration(samplePeriodInSeconds=" + getSamplePeriodInSeconds() + ", slidingWindowLengthInMinutes=" + getSlidingWindowLengthInMinutes() + ", enabled=" + isEnabled() + ")"; }
/*    */     
/* 26 */     public ElasticSearchStoreToolConfiguration() { this.enabled = true; } @ConstructorProperties({"samplePeriodInSeconds", "slidingWindowLengthInMinutes", "enabled"})
/* 26 */     public ElasticSearchStoreToolConfiguration(int samplePeriodInSeconds, int slidingWindowLengthInMinutes, boolean enabled) { this.enabled = true;this.samplePeriodInSeconds = samplePeriodInSeconds;this.slidingWindowLengthInMinutes = slidingWindowLengthInMinutes;this.enabled = enabled; }
/*    */     @Min(1L)
/* 28 */     int samplePeriodInSeconds = 30;
/* 29 */     public int getSamplePeriodInSeconds() { return this.samplePeriodInSeconds; } public void setSamplePeriodInSeconds(int samplePeriodInSeconds) { this.samplePeriodInSeconds = samplePeriodInSeconds; }
/*    */     @Min(1L)
/* 31 */     int slidingWindowLengthInMinutes = 5;
/* 32 */     public int getSlidingWindowLengthInMinutes() { return this.slidingWindowLengthInMinutes; } public void setSlidingWindowLengthInMinutes(int slidingWindowLengthInMinutes) { this.slidingWindowLengthInMinutes = slidingWindowLengthInMinutes; }
/*    */     
/* 34 */     public boolean isEnabled() { return this.enabled; } public void setEnabled(boolean enabled) { this.enabled = enabled; }
/*    */     
/*    */     boolean enabled;
/*    */   }
/*    */   
/*    */   public ElasticSearchToolConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ElasticSearchToolConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */