/*    */ package com.appdynamics.analytics.pipeline.http;
/*    */ 
/*    */ import com.appdynamics.analytics.queue.QueueConfiguration;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PostReceiverConfiguration
/*    */   extends QueueConfiguration
/*    */ {
/*    */   @NotNull
/*    */   String urlPattern;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 18 */     if (o == this) return true; if (!(o instanceof PostReceiverConfiguration)) return false; PostReceiverConfiguration other = (PostReceiverConfiguration)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$urlPattern = getUrlPattern();Object other$urlPattern = other.getUrlPattern(); if (this$urlPattern == null ? other$urlPattern != null : !this$urlPattern.equals(other$urlPattern)) return false; if (getAckTimeoutSeconds() != other.getAckTimeoutSeconds()) return false; return isDebugDrain() == other.isDebugDrain(); } public boolean canEqual(Object other) { return other instanceof PostReceiverConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $urlPattern = getUrlPattern();result = result * 31 + ($urlPattern == null ? 0 : $urlPattern.hashCode());long $ackTimeoutSeconds = getAckTimeoutSeconds();result = result * 31 + (int)($ackTimeoutSeconds >>> 32 ^ $ackTimeoutSeconds);result = result * 31 + (isDebugDrain() ? 1231 : 1237);return result; }
/* 19 */   public String toString() { return "PostReceiverConfiguration(urlPattern=" + getUrlPattern() + ", ackTimeoutSeconds=" + getAckTimeoutSeconds() + ", debugDrain=" + isDebugDrain() + ")"; }
/*    */   
/*    */ 
/* 22 */   public String getUrlPattern() { return this.urlPattern; } public void setUrlPattern(String urlPattern) { this.urlPattern = urlPattern; }
/*    */   @Min(1L)
/* 24 */   long ackTimeoutSeconds = 30L;
/* 25 */   public long getAckTimeoutSeconds() { return this.ackTimeoutSeconds; } public void setAckTimeoutSeconds(long ackTimeoutSeconds) { this.ackTimeoutSeconds = ackTimeoutSeconds; }
/*    */   
/* 27 */   public boolean isDebugDrain() { return this.debugDrain; } public void setDebugDrain(boolean debugDrain) { this.debugDrain = debugDrain; } boolean debugDrain = false;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/http/PostReceiverConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */