/*    */ package com.appdynamics.analytics.processor.kafka.consumer;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.util.Set;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import javax.validation.constraints.Size;
/*    */ import org.hibernate.validator.constraints.NotBlank;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TopicFamilyConfiguration
/*    */ {
/*    */   @Size(min=1)
/*    */   Set<String> consumableTopics;
/*    */   @NotBlank
/*    */   String transientDeadLetterTopic;
/*    */   @NotBlank
/*    */   String permanentDeadLetterTopic;
/*    */   @NotBlank
/*    */   String consumerGroupId;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 28 */     if (o == this) return true; if (!(o instanceof TopicFamilyConfiguration)) return false; TopicFamilyConfiguration other = (TopicFamilyConfiguration)o; if (!other.canEqual(this)) return false; Object this$consumableTopics = getConsumableTopics();Object other$consumableTopics = other.getConsumableTopics(); if (this$consumableTopics == null ? other$consumableTopics != null : !this$consumableTopics.equals(other$consumableTopics)) return false; Object this$transientDeadLetterTopic = getTransientDeadLetterTopic();Object other$transientDeadLetterTopic = other.getTransientDeadLetterTopic(); if (this$transientDeadLetterTopic == null ? other$transientDeadLetterTopic != null : !this$transientDeadLetterTopic.equals(other$transientDeadLetterTopic)) return false; Object this$permanentDeadLetterTopic = getPermanentDeadLetterTopic();Object other$permanentDeadLetterTopic = other.getPermanentDeadLetterTopic(); if (this$permanentDeadLetterTopic == null ? other$permanentDeadLetterTopic != null : !this$permanentDeadLetterTopic.equals(other$permanentDeadLetterTopic)) return false; Object this$consumerGroupId = getConsumerGroupId();Object other$consumerGroupId = other.getConsumerGroupId(); if (this$consumerGroupId == null ? other$consumerGroupId != null : !this$consumerGroupId.equals(other$consumerGroupId)) return false; if (isExplicitCommitOffset() != other.isExplicitCommitOffset()) return false; Object this$deadLetterTopicRetention = getDeadLetterTopicRetention();Object other$deadLetterTopicRetention = other.getDeadLetterTopicRetention();return this$deadLetterTopicRetention == null ? other$deadLetterTopicRetention == null : this$deadLetterTopicRetention.equals(other$deadLetterTopicRetention); } public boolean canEqual(Object other) { return other instanceof TopicFamilyConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $consumableTopics = getConsumableTopics();result = result * 31 + ($consumableTopics == null ? 0 : $consumableTopics.hashCode());Object $transientDeadLetterTopic = getTransientDeadLetterTopic();result = result * 31 + ($transientDeadLetterTopic == null ? 0 : $transientDeadLetterTopic.hashCode());Object $permanentDeadLetterTopic = getPermanentDeadLetterTopic();result = result * 31 + ($permanentDeadLetterTopic == null ? 0 : $permanentDeadLetterTopic.hashCode());Object $consumerGroupId = getConsumerGroupId();result = result * 31 + ($consumerGroupId == null ? 0 : $consumerGroupId.hashCode());result = result * 31 + (isExplicitCommitOffset() ? 1231 : 1237);Object $deadLetterTopicRetention = getDeadLetterTopicRetention();result = result * 31 + ($deadLetterTopicRetention == null ? 0 : $deadLetterTopicRetention.hashCode());return result; } public String toString() { return "TopicFamilyConfiguration(consumableTopics=" + getConsumableTopics() + ", transientDeadLetterTopic=" + getTransientDeadLetterTopic() + ", permanentDeadLetterTopic=" + getPermanentDeadLetterTopic() + ", consumerGroupId=" + getConsumerGroupId() + ", explicitCommitOffset=" + isExplicitCommitOffset() + ", deadLetterTopicRetention=" + getDeadLetterTopicRetention() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 35 */   public Set<String> getConsumableTopics() { return this.consumableTopics; } public void setConsumableTopics(Set<String> consumableTopics) { this.consumableTopics = consumableTopics; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */   public String getTransientDeadLetterTopic() { return this.transientDeadLetterTopic; } public void setTransientDeadLetterTopic(String transientDeadLetterTopic) { this.transientDeadLetterTopic = transientDeadLetterTopic; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 49 */   public String getPermanentDeadLetterTopic() { return this.permanentDeadLetterTopic; } public void setPermanentDeadLetterTopic(String permanentDeadLetterTopic) { this.permanentDeadLetterTopic = permanentDeadLetterTopic; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 54 */   public String getConsumerGroupId() { return this.consumerGroupId; } public void setConsumerGroupId(String consumerGroupId) { this.consumerGroupId = consumerGroupId; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 62 */   public boolean isExplicitCommitOffset() { return this.explicitCommitOffset; } public void setExplicitCommitOffset(boolean explicitCommitOffset) { this.explicitCommitOffset = explicitCommitOffset; } boolean explicitCommitOffset = false;
/*    */   
/*    */ 
/*    */ 
/* 66 */   public TimeUnitConfiguration getDeadLetterTopicRetention() { return this.deadLetterTopicRetention; } public void setDeadLetterTopicRetention(TimeUnitConfiguration deadLetterTopicRetention) { this.deadLetterTopicRetention = deadLetterTopicRetention; }
/*    */   
/*    */   @Valid
/*    */   @NotNull
/*    */   TimeUnitConfiguration deadLetterTopicRetention;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/TopicFamilyConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */