/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.single;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
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
/*    */ public class ElasticSearchSingleNodeConfiguration
/*    */ {
/*    */   public String toString()
/*    */   {
/* 26 */     return "ElasticSearchSingleNodeConfiguration(callTimeout=" + getCallTimeout() + ", nodeSettings=" + getNodeSettings() + ")"; }
/* 27 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ElasticSearchSingleNodeConfiguration)) return false; ElasticSearchSingleNodeConfiguration other = (ElasticSearchSingleNodeConfiguration)o; if (!other.canEqual(this)) return false; Object this$callTimeout = getCallTimeout();Object other$callTimeout = other.getCallTimeout(); if (this$callTimeout == null ? other$callTimeout != null : !this$callTimeout.equals(other$callTimeout)) return false; Object this$nodeSettings = getNodeSettings();Object other$nodeSettings = other.getNodeSettings();return this$nodeSettings == null ? other$nodeSettings == null : this$nodeSettings.equals(other$nodeSettings); } public boolean canEqual(Object other) { return other instanceof ElasticSearchSingleNodeConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $callTimeout = getCallTimeout();result = result * 31 + ($callTimeout == null ? 0 : $callTimeout.hashCode());Object $nodeSettings = getNodeSettings();result = result * 31 + ($nodeSettings == null ? 0 : $nodeSettings.hashCode());return result;
/*    */   }
/*    */   
/* 30 */   public static final long DEFAULT_REMOTE_CALL_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(10L);
/*    */   
/*    */ 
/* 33 */   public TimeUnitConfiguration getCallTimeout() { return this.callTimeout; } public void setCallTimeout(TimeUnitConfiguration callTimeout) { this.callTimeout = callTimeout; }
/*    */   
/* 35 */   public Map<String, String> getNodeSettings() { return this.nodeSettings; } public void setNodeSettings(Map<String, String> nodeSettings) { this.nodeSettings = nodeSettings; }
/*    */   
/*    */   public ElasticSearchSingleNodeConfiguration() {
/* 38 */     this.callTimeout = new TimeUnitConfiguration(DEFAULT_REMOTE_CALL_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
/* 39 */     this.nodeSettings = new HashMap();
/*    */   }
/*    */   
/*    */   @Valid
/*    */   TimeUnitConfiguration callTimeout;
/*    */   private Map<String, String> nodeSettings;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/single/ElasticSearchSingleNodeConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */