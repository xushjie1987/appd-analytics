/*    */ package com.appdynamics.analytics.pipeline.framework;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PipelineStageConfiguration
/*    */ {
/*    */   @NotNull
/*    */   @JsonProperty
/*    */   String uri;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 18 */     if (o == this) return true; if (!(o instanceof PipelineStageConfiguration)) return false; PipelineStageConfiguration other = (PipelineStageConfiguration)o; if (!other.canEqual(this)) return false; Object this$uri = getUri();Object other$uri = other.getUri(); if (this$uri == null ? other$uri != null : !this$uri.equals(other$uri)) return false; Object this$properties = getProperties();Object other$properties = other.getProperties();return this$properties == null ? other$properties == null : this$properties.equals(other$properties); } public boolean canEqual(Object other) { return other instanceof PipelineStageConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $uri = getUri();result = result * 31 + ($uri == null ? 0 : $uri.hashCode());Object $properties = getProperties();result = result * 31 + ($properties == null ? 0 : $properties.hashCode());return result; } public String toString() { return "PipelineStageConfiguration(uri=" + getUri() + ", properties=" + getProperties() + ")"; }
/*    */   
/*    */ 
/*    */ 
/* 22 */   public String getUri() { return this.uri; } public void setUri(String uri) { this.uri = uri; } @NotNull
/*    */   @JsonProperty
/* 24 */   Map<String, Object> properties = new HashMap();
/*    */   
/* 26 */   public Map<String, Object> getProperties() { return this.properties; } public void setProperties(Map<String, Object> properties) { this.properties = properties; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/PipelineStageConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */