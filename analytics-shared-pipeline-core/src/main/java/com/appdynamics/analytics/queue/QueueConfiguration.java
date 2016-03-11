/*    */ package com.appdynamics.analytics.queue;
/*    */ 
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueueConfiguration
/*    */ {
/*    */   @NotNull
/*    */   String queueName;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 16 */     if (o == this) return true; if (!(o instanceof QueueConfiguration)) return false; QueueConfiguration other = (QueueConfiguration)o; if (!other.canEqual(this)) return false; Object this$queueName = getQueueName();Object other$queueName = other.getQueueName(); if (this$queueName == null ? other$queueName != null : !this$queueName.equals(other$queueName)) return false; if (getQueueSize() != other.getQueueSize()) return false; return isMessageSource() == other.isMessageSource(); } public boolean canEqual(Object other) { return other instanceof QueueConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $queueName = getQueueName();result = result * 31 + ($queueName == null ? 0 : $queueName.hashCode());result = result * 31 + getQueueSize();result = result * 31 + (isMessageSource() ? 1231 : 1237);return result; } public String toString() { return "QueueConfiguration(queueName=" + getQueueName() + ", queueSize=" + getQueueSize() + ", messageSource=" + isMessageSource() + ")"; }
/*    */   
/*    */ 
/* 19 */   public String getQueueName() { return this.queueName; } public void setQueueName(String queueName) { this.queueName = queueName; }
/*    */   @Min(0L)
/* 21 */   int queueSize = 48;
/* 22 */   public int getQueueSize() { return this.queueSize; } public void setQueueSize(int queueSize) { this.queueSize = queueSize; }
/*    */   
/* 24 */   public boolean isMessageSource() { return this.messageSource; } public void setMessageSource(boolean messageSource) { this.messageSource = messageSource; } boolean messageSource = false;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/queue/QueueConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */