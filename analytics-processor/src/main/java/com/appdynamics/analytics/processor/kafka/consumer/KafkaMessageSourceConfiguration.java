/*     */ package com.appdynamics.analytics.processor.kafka.consumer;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.kafka.EventServiceKafkaConstants;
/*     */ import com.appdynamics.common.util.configuration.ManualValidateable;
/*     */ import com.appdynamics.common.util.configuration.ManualValidated;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.validation.ConstraintValidatorContext;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
/*     */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
/*     */ import javax.validation.Valid;
/*     */ import javax.validation.constraints.Min;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import kafka.consumer.ConsumerConfig;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ManualValidated
/*     */ public class KafkaMessageSourceConfiguration
/*     */   implements ManualValidateable
/*     */ {
/*     */   static final String PROP_CONSUMER_ID = "consumer.id";
/*     */   static final String PROP_AUTO_COMMIT_ENABLE = "auto.commit.enable";
/*     */   static final String PROP_AUTO_COMMIT_INTERVAL_MS = "auto.commit.interval.ms";
/*     */   static final String PROP_GROUP_ID = "group.id";
/*     */   static final String PROP_CONSUMER_TIMEOUT_MS = "consumer.timeout.ms";
/*     */   static final String PROP_ZOOKEEPER_CONNECT = "zookeeper.connect";
/*     */   static final long DEFAULT_CONSUMER_TIMEOUT = 500L;
/*  48 */   static final long DEFAULT_AUTOCOMMIT_INTERVAL = TimeUnit.SECONDS.toMillis(10L);
/*     */   
/*     */ 
/*  51 */   public String getMsgSrcId() { return this.msgSrcId; }
/*  52 */   public void setMsgSrcId(String msgSrcId) { this.msgSrcId = msgSrcId; }
/*     */   
/*     */   @NotEmpty
/*     */   String msgSrcId;
/*     */   @Valid
/*     */   Map<String, TopicFamilyConfiguration> topicFamilies;
/*     */   @NotNull
/*     */   Class<? extends KafkaMessageSourceHook> messageSourceHook;
/*  60 */   public Map<String, TopicFamilyConfiguration> getTopicFamilies() { return this.topicFamilies; }
/*  61 */   public void setTopicFamilies(Map<String, TopicFamilyConfiguration> topicFamilies) { this.topicFamilies = topicFamilies; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  66 */   public Class<? extends KafkaMessageSourceHook> getMessageSourceHook() { return this.messageSourceHook; }
/*  67 */   public void setMessageSourceHook(Class<? extends KafkaMessageSourceHook> messageSourceHook) { this.messageSourceHook = messageSourceHook; }
/*     */   
/*     */   @Min(1L)
/*  70 */   int numZookeeperConnectTries = 3;
/*  71 */   public int getNumZookeeperConnectTries() { return this.numZookeeperConnectTries; }
/*  72 */   public void setNumZookeeperConnectTries(int numZookeeperConnectTries) { this.numZookeeperConnectTries = numZookeeperConnectTries; }
/*     */   
/*     */   @Min(1L)
/*  75 */   int maxHealthyConsumerLag = 1000;
/*  76 */   public int getMaxHealthyConsumerLag() { return this.maxHealthyConsumerLag; }
/*  77 */   public void setMaxHealthyConsumerLag(int maxHealthyConsumerLag) { this.maxHealthyConsumerLag = maxHealthyConsumerLag; }
/*     */   
/*     */ 
/*  80 */   public Map<String, String> getConsumerConfig() { return this.consumerConfig; } Map<String, String> consumerConfig = new HashMap();
/*  81 */   public void setConsumerConfig(Map<String, String> consumerConfig) { this.consumerConfig = consumerConfig; }
/*     */   
/*     */   public boolean validate(ConstraintValidatorContext context)
/*     */   {
/*     */     try
/*     */     {
/*  87 */       Preconditions.checkArgument(KafkaMessageSourceHook.class.isAssignableFrom(this.messageSourceHook));
/*     */     } catch (IllegalArgumentException e) {
/*  89 */       context.buildConstraintViolationWithTemplate("The class must be a type of [" + KafkaMessageSourceHook.class.getName() + "]").addPropertyNode("messageSourceHook").addConstraintViolation();
/*     */       
/*     */ 
/*     */ 
/*  93 */       return false;
/*     */     }
/*  95 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConsumerConfig newParsedConsumerConfig(String consumerId, String consumerGroupId)
/*     */   {
/* 106 */     Map<String, String> map = new HashMap(this.consumerConfig);
/*     */     
/* 108 */     map.put("auto.commit.enable", "false");
/*     */     
/*     */ 
/* 111 */     map.put("group.id", consumerGroupId);
/*     */     
/* 113 */     if (!map.containsKey("auto.commit.interval.ms")) {
/* 114 */       map.put("auto.commit.interval.ms", String.valueOf(DEFAULT_AUTOCOMMIT_INTERVAL));
/*     */     }
/*     */     
/*     */ 
/* 118 */     if (!map.containsKey("consumer.timeout.ms")) {
/* 119 */       map.put("consumer.timeout.ms", String.valueOf(500L));
/*     */     }
/* 121 */     map.put("consumer.id", consumerId);
/*     */     
/*     */ 
/* 124 */     if (map.containsKey("zookeeper.connect")) {
/* 125 */       String zkConnectString = (String)map.get("zookeeper.connect");
/* 126 */       zkConnectString = zkConnectString + EventServiceKafkaConstants.KAFKA_ZOOKEEPER_CHROOT;
/* 127 */       map.put("zookeeper.connect", zkConnectString);
/*     */     }
/*     */     
/* 130 */     Properties properties = new Properties();
/* 131 */     properties.putAll(map);
/* 132 */     return new ConsumerConfig(properties);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/KafkaMessageSourceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */