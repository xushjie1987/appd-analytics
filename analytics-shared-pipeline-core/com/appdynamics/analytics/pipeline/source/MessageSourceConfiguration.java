/*    */ package com.appdynamics.analytics.pipeline.source;
/*    */ 
/*    */ import com.appdynamics.common.util.execution.RetryConfiguration;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessageSourceConfiguration
/*    */ {
/*    */   @NotNull
/*    */   Object messageSourceId;
/*    */   Object messageSourceParams;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 18 */     if (o == this) return true; if (!(o instanceof MessageSourceConfiguration)) return false; MessageSourceConfiguration other = (MessageSourceConfiguration)o; if (!other.canEqual(this)) return false; Object this$messageSourceId = getMessageSourceId();Object other$messageSourceId = other.getMessageSourceId(); if (this$messageSourceId == null ? other$messageSourceId != null : !this$messageSourceId.equals(other$messageSourceId)) return false; Object this$messageSourceParams = getMessageSourceParams();Object other$messageSourceParams = other.getMessageSourceParams(); if (this$messageSourceParams == null ? other$messageSourceParams != null : !this$messageSourceParams.equals(other$messageSourceParams)) return false; if (getMaxMessagesPerPoll() != other.getMaxMessagesPerPoll()) return false; if (getPollTimeoutMillis() != other.getPollTimeoutMillis()) return false; Object this$retry = getRetry();Object other$retry = other.getRetry(); if (this$retry == null ? other$retry != null : !this$retry.equals(other$retry)) return false; return isRethrowUnknownExceptions() == other.isRethrowUnknownExceptions(); } public boolean canEqual(Object other) { return other instanceof MessageSourceConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $messageSourceId = getMessageSourceId();result = result * 31 + ($messageSourceId == null ? 0 : $messageSourceId.hashCode());Object $messageSourceParams = getMessageSourceParams();result = result * 31 + ($messageSourceParams == null ? 0 : $messageSourceParams.hashCode());result = result * 31 + getMaxMessagesPerPoll();long $pollTimeoutMillis = getPollTimeoutMillis();result = result * 31 + (int)($pollTimeoutMillis >>> 32 ^ $pollTimeoutMillis);Object $retry = getRetry();result = result * 31 + ($retry == null ? 0 : $retry.hashCode());result = result * 31 + (isRethrowUnknownExceptions() ? 1231 : 1237);return result; } public String toString() { return "MessageSourceConfiguration(messageSourceId=" + getMessageSourceId() + ", messageSourceParams=" + getMessageSourceParams() + ", maxMessagesPerPoll=" + getMaxMessagesPerPoll() + ", pollTimeoutMillis=" + getPollTimeoutMillis() + ", retry=" + getRetry() + ", rethrowUnknownExceptions=" + isRethrowUnknownExceptions() + ")"; }
/*    */   
/*    */ 
/* 21 */   public Object getMessageSourceId() { return this.messageSourceId; } public void setMessageSourceId(Object messageSourceId) { this.messageSourceId = messageSourceId; }
/*    */   
/*    */ 
/* 24 */   public Object getMessageSourceParams() { return this.messageSourceParams; } public void setMessageSourceParams(Object messageSourceParams) { this.messageSourceParams = messageSourceParams; }
/*    */   @Min(1L)
/* 26 */   int maxMessagesPerPoll = 1;
/* 27 */   public int getMaxMessagesPerPoll() { return this.maxMessagesPerPoll; } public void setMaxMessagesPerPoll(int maxMessagesPerPoll) { this.maxMessagesPerPoll = maxMessagesPerPoll; }
/*    */   @Min(0L)
/* 29 */   long pollTimeoutMillis = 1000L;
/* 30 */   public long getPollTimeoutMillis() { return this.pollTimeoutMillis; } public void setPollTimeoutMillis(long pollTimeoutMillis) { this.pollTimeoutMillis = pollTimeoutMillis; }
/*    */   @Valid
/*    */   RetryConfiguration retry;
/* 33 */   public RetryConfiguration getRetry() { return this.retry; } public void setRetry(RetryConfiguration retry) { this.retry = retry; }
/*    */   
/* 35 */   public boolean isRethrowUnknownExceptions() { return this.rethrowUnknownExceptions; } public void setRethrowUnknownExceptions(boolean rethrowUnknownExceptions) { this.rethrowUnknownExceptions = rethrowUnknownExceptions; } boolean rethrowUnknownExceptions = false;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/source/MessageSourceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */