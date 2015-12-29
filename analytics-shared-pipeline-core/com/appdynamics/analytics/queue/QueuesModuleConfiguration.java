/*    */ package com.appdynamics.analytics.queue;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.DecimalMin;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueuesModuleConfiguration
/*    */ {
/*    */   public static final String DEFAULT_ALERT_RATIO = "0.75";
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 20 */     if (o == this) return true; if (!(o instanceof QueuesModuleConfiguration)) return false; QueuesModuleConfiguration other = (QueuesModuleConfiguration)o; if (!other.canEqual(this)) return false; Object this$alertRatio = getAlertRatio();Object other$alertRatio = other.getAlertRatio(); if (this$alertRatio == null ? other$alertRatio != null : !this$alertRatio.equals(other$alertRatio)) return false; Object this$queues = getQueues();Object other$queues = other.getQueues();return this$queues == null ? other$queues == null : this$queues.equals(other$queues); } public boolean canEqual(Object other) { return other instanceof QueuesModuleConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $alertRatio = getAlertRatio();result = result * 31 + ($alertRatio == null ? 0 : $alertRatio.hashCode());Object $queues = getQueues();result = result * 31 + ($queues == null ? 0 : $queues.hashCode());return result; } public String toString() { return "QueuesModuleConfiguration(alertRatio=" + getAlertRatio() + ", queues=" + getQueues() + ")"; }
/*    */   
/*    */   @DecimalMin("0.01")
/*    */   @NotNull
/* 24 */   BigDecimal alertRatio = new BigDecimal("0.75");
/*    */   
/* 26 */   public BigDecimal getAlertRatio() { return this.alertRatio; } public void setAlertRatio(BigDecimal alertRatio) { this.alertRatio = alertRatio; } @Valid
/*    */   @NotNull
/* 28 */   List<QueueConfiguration> queues = new LinkedList();
/*    */   
/* 30 */   public List<QueueConfiguration> getQueues() { return this.queues; } public void setQueues(List<QueueConfiguration> queues) { this.queues = queues; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/queue/QueuesModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */