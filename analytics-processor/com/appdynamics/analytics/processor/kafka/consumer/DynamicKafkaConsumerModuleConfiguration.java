/*    */ package com.appdynamics.analytics.processor.kafka.consumer;
/*    */ 
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ public class DynamicKafkaConsumerModuleConfiguration
/*    */ {
/*    */   @NotNull
/*    */   String clusterNameRegexFilter;
/*    */   @NotNull
/*    */   String messageSourceId;
/*    */   @NotNull
/*    */   String templateName;
/*    */   @NotNull
/*    */   java.util.Set<com.appdynamics.analytics.processor.admin.ActionType> topicTypes;
/*    */   @NotNull
/*    */   String consumerGroupIdPrefix;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 20 */     if (o == this) return true; if (!(o instanceof DynamicKafkaConsumerModuleConfiguration)) return false; DynamicKafkaConsumerModuleConfiguration other = (DynamicKafkaConsumerModuleConfiguration)o; if (!other.canEqual(this)) return false; Object this$clusterNameRegexFilter = getClusterNameRegexFilter();Object other$clusterNameRegexFilter = other.getClusterNameRegexFilter(); if (this$clusterNameRegexFilter == null ? other$clusterNameRegexFilter != null : !this$clusterNameRegexFilter.equals(other$clusterNameRegexFilter)) return false; Object this$messageSourceId = getMessageSourceId();Object other$messageSourceId = other.getMessageSourceId(); if (this$messageSourceId == null ? other$messageSourceId != null : !this$messageSourceId.equals(other$messageSourceId)) return false; Object this$templateName = getTemplateName();Object other$templateName = other.getTemplateName(); if (this$templateName == null ? other$templateName != null : !this$templateName.equals(other$templateName)) return false; Object this$topicTypes = getTopicTypes();Object other$topicTypes = other.getTopicTypes(); if (this$topicTypes == null ? other$topicTypes != null : !this$topicTypes.equals(other$topicTypes)) return false; Object this$consumerGroupIdPrefix = getConsumerGroupIdPrefix();Object other$consumerGroupIdPrefix = other.getConsumerGroupIdPrefix(); if (this$consumerGroupIdPrefix == null ? other$consumerGroupIdPrefix != null : !this$consumerGroupIdPrefix.equals(other$consumerGroupIdPrefix)) return false; if (getKafkaTopicPartitions() != other.getKafkaTopicPartitions()) return false; if (getKafkaTopicReplicationFactor() != other.getKafkaTopicReplicationFactor()) return false; Object this$zookeeperConnect = getZookeeperConnect();Object other$zookeeperConnect = other.getZookeeperConnect(); if (this$zookeeperConnect == null ? other$zookeeperConnect != null : !this$zookeeperConnect.equals(other$zookeeperConnect)) return false; if (isExplicitCommitOffset() != other.isExplicitCommitOffset()) return false; Object this$deadLetterTopicRetention = getDeadLetterTopicRetention();Object other$deadLetterTopicRetention = other.getDeadLetterTopicRetention();return this$deadLetterTopicRetention == null ? other$deadLetterTopicRetention == null : this$deadLetterTopicRetention.equals(other$deadLetterTopicRetention); } public boolean canEqual(Object other) { return other instanceof DynamicKafkaConsumerModuleConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $clusterNameRegexFilter = getClusterNameRegexFilter();result = result * 31 + ($clusterNameRegexFilter == null ? 0 : $clusterNameRegexFilter.hashCode());Object $messageSourceId = getMessageSourceId();result = result * 31 + ($messageSourceId == null ? 0 : $messageSourceId.hashCode());Object $templateName = getTemplateName();result = result * 31 + ($templateName == null ? 0 : $templateName.hashCode());Object $topicTypes = getTopicTypes();result = result * 31 + ($topicTypes == null ? 0 : $topicTypes.hashCode());Object $consumerGroupIdPrefix = getConsumerGroupIdPrefix();result = result * 31 + ($consumerGroupIdPrefix == null ? 0 : $consumerGroupIdPrefix.hashCode());result = result * 31 + getKafkaTopicPartitions();result = result * 31 + getKafkaTopicReplicationFactor();Object $zookeeperConnect = getZookeeperConnect();result = result * 31 + ($zookeeperConnect == null ? 0 : $zookeeperConnect.hashCode());result = result * 31 + (isExplicitCommitOffset() ? 1231 : 1237);Object $deadLetterTopicRetention = getDeadLetterTopicRetention();result = result * 31 + ($deadLetterTopicRetention == null ? 0 : $deadLetterTopicRetention.hashCode());return result; } public String toString() { return "DynamicKafkaConsumerModuleConfiguration(clusterNameRegexFilter=" + getClusterNameRegexFilter() + ", messageSourceId=" + getMessageSourceId() + ", templateName=" + getTemplateName() + ", topicTypes=" + getTopicTypes() + ", consumerGroupIdPrefix=" + getConsumerGroupIdPrefix() + ", kafkaTopicPartitions=" + getKafkaTopicPartitions() + ", kafkaTopicReplicationFactor=" + getKafkaTopicReplicationFactor() + ", zookeeperConnect=" + getZookeeperConnect() + ", explicitCommitOffset=" + isExplicitCommitOffset() + ", deadLetterTopicRetention=" + getDeadLetterTopicRetention() + ")"; }
/*    */   
/*    */ 
/* 23 */   public String getClusterNameRegexFilter() { return this.clusterNameRegexFilter; } public void setClusterNameRegexFilter(String clusterNameRegexFilter) { this.clusterNameRegexFilter = clusterNameRegexFilter; }
/*    */   
/*    */ 
/* 26 */   public String getMessageSourceId() { return this.messageSourceId; } public void setMessageSourceId(String messageSourceId) { this.messageSourceId = messageSourceId; }
/*    */   
/*    */ 
/* 29 */   public String getTemplateName() { return this.templateName; } public void setTemplateName(String templateName) { this.templateName = templateName; }
/*    */   
/*    */ 
/* 32 */   public java.util.Set<com.appdynamics.analytics.processor.admin.ActionType> getTopicTypes() { return this.topicTypes; } public void setTopicTypes(java.util.Set<com.appdynamics.analytics.processor.admin.ActionType> topicTypes) { this.topicTypes = topicTypes; }
/*    */   
/*    */ 
/* 35 */   public String getConsumerGroupIdPrefix() { return this.consumerGroupIdPrefix; } public void setConsumerGroupIdPrefix(String consumerGroupIdPrefix) { this.consumerGroupIdPrefix = consumerGroupIdPrefix; }
/*    */   
/*    */ 
/*    */   @javax.validation.constraints.Min(1L)
/*    */   int kafkaTopicPartitions;
/*    */   
/* 41 */   public int getKafkaTopicPartitions() { return this.kafkaTopicPartitions; } public void setKafkaTopicPartitions(int kafkaTopicPartitions) { this.kafkaTopicPartitions = kafkaTopicPartitions; }
/*    */   
/*    */   @javax.validation.constraints.Min(1L)
/*    */   int kafkaTopicReplicationFactor;
/*    */   String zookeeperConnect;
/*    */   
/* 47 */   public int getKafkaTopicReplicationFactor() { return this.kafkaTopicReplicationFactor; } public void setKafkaTopicReplicationFactor(int kafkaTopicReplicationFactor) { this.kafkaTopicReplicationFactor = kafkaTopicReplicationFactor; }
/*    */   
/*    */ 
/*    */   boolean explicitCommitOffset;
/*    */   
/* 52 */   public String getZookeeperConnect() { return this.zookeeperConnect; } public void setZookeeperConnect(String zookeeperConnect) { this.zookeeperConnect = zookeeperConnect; }
/*    */   
/* 54 */   public boolean isExplicitCommitOffset() { return this.explicitCommitOffset; } public void setExplicitCommitOffset(boolean explicitCommitOffset) { this.explicitCommitOffset = explicitCommitOffset; }
/*    */   
/*    */ 
/*    */ 
/* 58 */   public com.appdynamics.common.util.datetime.TimeUnitConfiguration getDeadLetterTopicRetention() { return this.deadLetterTopicRetention; } public void setDeadLetterTopicRetention(com.appdynamics.common.util.datetime.TimeUnitConfiguration deadLetterTopicRetention) { this.deadLetterTopicRetention = deadLetterTopicRetention; }
/*    */   
/*    */   @javax.validation.Valid
/*    */   @NotNull
/*    */   com.appdynamics.common.util.datetime.TimeUnitConfiguration deadLetterTopicRetention;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/DynamicKafkaConsumerModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */