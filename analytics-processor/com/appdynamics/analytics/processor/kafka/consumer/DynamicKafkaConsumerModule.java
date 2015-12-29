/*     */ package com.appdynamics.analytics.processor.kafka.consumer;
/*     */ 
/*     */ import com.appdynamics.analytics.message.MessageSources;
/*     */ import com.appdynamics.analytics.message.api.MessageSource;
/*     */ import com.appdynamics.analytics.pipeline.framework.Pipelines;
/*     */ import com.appdynamics.analytics.pipeline.framework.StaticPipelineConfiguration;
/*     */ import com.appdynamics.analytics.processor.admin.ActionType;
/*     */ import com.appdynamics.analytics.processor.admin.Locator;
/*     */ import com.appdynamics.analytics.processor.event.configuration.NewClusterEvent;
/*     */ import com.appdynamics.analytics.processor.kafka.EventServiceKafkaConstants;
/*     */ import com.appdynamics.analytics.processor.kafka.util.KafkaTool;
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.configuration.Templates;
/*     */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*     */ import com.appdynamics.common.util.event.EventBuses;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMap.Builder;
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import com.google.inject.Inject;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicKafkaConsumerModule
/*     */   extends Module<DynamicKafkaConsumerModuleConfiguration>
/*     */ {
/*  44 */   private static final Logger log = LoggerFactory.getLogger(DynamicKafkaConsumerModule.class);
/*     */   
/*     */   private static final String CONSUMER_GROUP_ID_TEMPLATE = "%s-%s";
/*     */   
/*     */   public static final String KEY_INTERNAL_MESSAGE_SOURCE_ID = "internal.template.message.source.id";
/*     */   
/*     */   public static final String KEY_INTERNAL_TOPIC_FAMILY_NAME = "internal.template.topic.family.name";
/*     */   public static final String KEY_INTERNAL_PIPELINE_NAME = "internal.template.pipeline.name";
/*     */   public static final String KEY_INTERNAL_TOPIC_TYPES = "internal.template.topic.types";
/*     */   
/*     */   @Inject
/*     */   void onStart(MessageSources messageSources, Locator locator, Templates templates, Pipelines pipelines, EventBuses eventBuses)
/*     */   {
/*  57 */     ClusterChangeListener listener = new ClusterChangeListener(messageSources, locator, templates, pipelines);
/*  58 */     eventBuses.registerListener("default-event-bus", listener);
/*     */   }
/*     */   
/*     */   String makeTopicFamilyName(Set<String> topicNames, String consumerGroupId) {
/*  62 */     return consumerGroupId + "-" + topicNames + "-topic-family";
/*     */   }
/*     */   
/*     */   String makePipelineName(Set<String> topicNames) {
/*  66 */     return topicNames + "-pipeline";
/*     */   }
/*     */   
/*     */   private class ClusterChangeListener
/*     */   {
/*     */     final MessageSources messageSources;
/*     */     final Locator locator;
/*     */     final Templates templates;
/*     */     final Pipelines pipelines;
/*     */     final Set<String> seenClusterNames;
/*     */     final int kafkaTopicPartitions;
/*     */     final int kafkaTopicReplicationFactor;
/*     */     final String zookeeperConnect;
/*     */     
/*     */     ClusterChangeListener(MessageSources messageSources, Locator locator, Templates templates, Pipelines pipelines) {
/*  81 */       this.messageSources = messageSources;
/*  82 */       this.locator = locator;
/*  83 */       this.templates = templates;
/*  84 */       this.pipelines = pipelines;
/*  85 */       this.seenClusterNames = Collections.synchronizedSet(new HashSet());
/*  86 */       this.kafkaTopicPartitions = ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getKafkaTopicPartitions();
/*  87 */       this.kafkaTopicReplicationFactor = ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getKafkaTopicReplicationFactor();
/*  88 */       this.zookeeperConnect = (((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getZookeeperConnect() + EventServiceKafkaConstants.KAFKA_ZOOKEEPER_CHROOT);
/*     */     }
/*     */     
/*     */     @Subscribe
/*     */     public void onNewCluster(NewClusterEvent newClusterEvent) {
/*  93 */       String clusterName = newClusterEvent.getClusterName();
/*  94 */       if (this.seenClusterNames.contains(clusterName)) {
/*  95 */         DynamicKafkaConsumerModule.log.debug("Notification about cluster [{}] will be ignored by [{}] because it has been received before", clusterName, DynamicKafkaConsumerModule.this.getUri());
/*     */         
/*  97 */         return;
/*     */       }
/*     */       
/*     */ 
/* 101 */       String filter = ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getClusterNameRegexFilter();
/* 102 */       if (!Pattern.matches(filter, clusterName)) {
/* 103 */         DynamicKafkaConsumerModule.log.info("Notification about cluster [{}] will be ignored by [{}] because it does not match the regex filter [{}]", new Object[] { clusterName, DynamicKafkaConsumerModule.this.getUri(), filter });
/*     */         
/*     */ 
/* 106 */         this.seenClusterNames.add(clusterName);
/* 107 */         return;
/*     */       }
/* 109 */       DynamicKafkaConsumerModule.log.info("Received notification about new cluster [{}] by [{}]", clusterName, DynamicKafkaConsumerModule.this.getUri());
/*     */       
/*     */ 
/* 112 */       String messageSourceId = ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getMessageSourceId();
/* 113 */       Supplier<? extends MessageSource<?, ?>> supplier = this.messageSources.getSupplier(messageSourceId);
/* 114 */       Preconditions.checkArgument(supplier instanceof KafkaMessageSourceSupplier, "The message source supplier [" + messageSourceId + "] is not of the expected type [" + KafkaMessageSourceSupplier.class.getName() + "]");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 119 */       Set<String> topicNames = new HashSet();
/* 120 */       for (ActionType type : ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getTopicTypes()) {
/* 121 */         String topicName = this.locator.findTopicName(clusterName, type);
/* 122 */         topicNames.add(topicName);
/*     */       }
/*     */       
/* 125 */       String consumerGroupId = String.format("%s-%s", new Object[] { ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getConsumerGroupIdPrefix(), clusterName });
/*     */       
/*     */ 
/* 128 */       String topicFamilyName = DynamicKafkaConsumerModule.this.makeTopicFamilyName(topicNames, consumerGroupId);
/* 129 */       TopicFamilyConfiguration topicFamilyCfg = new TopicFamilyConfiguration();
/* 130 */       topicFamilyCfg.setConsumableTopics(topicNames);
/*     */       
/* 132 */       String transientDeadLetterTopic = EventServiceKafkaConstants.makeTransientDeadLetterTopicName(consumerGroupId, this.locator, clusterName);
/*     */       
/* 134 */       String permanentDeadLetterTopic = EventServiceKafkaConstants.makePermanentDeadLetterTopicName(consumerGroupId, this.locator, clusterName);
/*     */       
/* 136 */       topicFamilyCfg.setTransientDeadLetterTopic(transientDeadLetterTopic);
/* 137 */       topicFamilyCfg.setPermanentDeadLetterTopic(permanentDeadLetterTopic);
/* 138 */       topicFamilyCfg.setConsumerGroupId(consumerGroupId);
/* 139 */       topicFamilyCfg.setExplicitCommitOffset(((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).isExplicitCommitOffset());
/* 140 */       topicFamilyCfg.setDeadLetterTopicRetention(((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getDeadLetterTopicRetention());
/*     */       
/* 142 */       KafkaMessageSourceSupplier kafkaSupplier = (KafkaMessageSourceSupplier)supplier;
/*     */       try
/*     */       {
/* 145 */         kafkaSupplier.addTopicFamilyConfiguration(topicFamilyName, topicFamilyCfg);
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (IllegalArgumentException e)
/*     */       {
/*     */ 
/*     */ 
/* 153 */         DynamicKafkaConsumerModule.log.debug("Failed to add topic family configurations because they already exist for cluster [{}]", clusterName, e);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */       createTopicsInTopicFamilyConfig(topicFamilyCfg);
/*     */       
/*     */ 
/*     */ 
/* 167 */       String templateName = ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getTemplateName();
/* 168 */       String pipelineName = DynamicKafkaConsumerModule.this.makePipelineName(topicNames);
/* 169 */       Map<String, Object> variables = ImmutableMap.builder().put("internal.template.message.source.id", messageSourceId).put("internal.template.message.source.id".replaceAll("\\.", "-"), messageSourceId).put("internal.template.topic.family.name", topicFamilyName).put("internal.template.topic.family.name".replaceAll("\\.", "-"), topicFamilyName).put("internal.template.pipeline.name", pipelineName).put("internal.template.pipeline.name".replaceAll("\\.", "-"), pipelineName).put("internal.template.topic.types", ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getTopicTypes()).put("internal.template.topic.types".replaceAll("\\.", "-"), ((DynamicKafkaConsumerModuleConfiguration)DynamicKafkaConsumerModule.this.getConfiguration()).getTopicTypes()).build();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 181 */       DynamicKafkaConsumerModule.log.info("Attempting to create a pipeline configuration by [{}] using the template [{}] and variables [\n{}]", new Object[] { DynamicKafkaConsumerModule.this.getUri(), templateName, Reader.toYaml(variables) });
/*     */       
/*     */ 
/*     */ 
/* 185 */       StaticPipelineConfiguration pipelineConfiguration = (StaticPipelineConfiguration)this.templates.read(templateName, variables, StaticPipelineConfiguration.class);
/*     */       
/* 187 */       Preconditions.checkArgument(pipelineConfiguration != null, "The template [" + templateName + "] does not exist");
/*     */       
/*     */ 
/* 190 */       this.pipelines.createAddStartAndExecuteIfEnabled(pipelineConfiguration);
/*     */       
/*     */ 
/* 193 */       this.seenClusterNames.add(clusterName);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private void createTopicsInTopicFamilyConfig(TopicFamilyConfiguration topicFamilyConfig)
/*     */     {
/* 201 */       for (String topicName : topicFamilyConfig.getConsumableTopics()) {
/* 202 */         KafkaTool.createTopic(this.zookeeperConnect, topicName, this.kafkaTopicPartitions, this.kafkaTopicReplicationFactor);
/*     */       }
/*     */       
/* 205 */       Optional<Long> dlqRetentionMillis = Optional.of(Long.valueOf(topicFamilyConfig.getDeadLetterTopicRetention().toMilliseconds()));
/*     */       
/* 207 */       KafkaTool.createTopic(this.zookeeperConnect, topicFamilyConfig.getTransientDeadLetterTopic(), this.kafkaTopicPartitions, this.kafkaTopicReplicationFactor, dlqRetentionMillis);
/*     */       
/*     */ 
/*     */ 
/* 211 */       KafkaTool.createTopic(this.zookeeperConnect, topicFamilyConfig.getPermanentDeadLetterTopic(), this.kafkaTopicPartitions, this.kafkaTopicReplicationFactor, dlqRetentionMillis);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/DynamicKafkaConsumerModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */