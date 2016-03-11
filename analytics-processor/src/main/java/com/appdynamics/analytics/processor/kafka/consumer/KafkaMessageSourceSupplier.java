/*     */ package com.appdynamics.analytics.processor.kafka.consumer;
/*     */ 
/*     */ import com.appdynamics.analytics.message.MessageSources;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.misc.ParameterAwareSupplier;
/*     */ import com.appdynamics.common.util.var.SystemVariableResolver;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.inject.Injector;
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ import kafka.consumer.Consumer;
/*     */ import kafka.consumer.ConsumerConfig;
/*     */ import kafka.javaapi.consumer.ConsumerConnector;
/*     */ import org.I0Itec.zkclient.exception.ZkTimeoutException;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ @ThreadSafe
/*     */ public class KafkaMessageSourceSupplier
/*     */   implements ParameterAwareSupplier<String, KafkaMessageSource>
/*     */ {
/*  75 */   private static final Logger log = LoggerFactory.getLogger(KafkaMessageSourceSupplier.class);
/*     */   
/*     */   protected final KafkaMessageSourceConfiguration baseConfig;
/*     */   
/*     */   protected final ConcurrentMap<String, KafkaMessageSource> kafkaMessageSources;
/*     */   
/*     */   private final ConcurrentMap<String, TopicFamilyConfiguration> topicFamilies;
/*     */   
/*     */   private final CopyOnWriteArraySet<String> consumerGroups;
/*     */   
/*     */   private final MessageSources allMessageSources;
/*     */   
/*     */   private final String globalUniqueId;
/*     */   
/*     */   private final AtomicInteger localUniqueIdGenerator;
/*     */   
/*     */   private final Injector injector;
/*     */   
/*     */   public KafkaMessageSourceSupplier(MessageSources allMessageSources, KafkaMessageSourceConfiguration baseConfig, Injector injector)
/*     */   {
/*  95 */     this.allMessageSources = allMessageSources;
/*  96 */     this.kafkaMessageSources = new ConcurrentHashMap();
/*  97 */     this.baseConfig = baseConfig;
/*  98 */     this.topicFamilies = new ConcurrentHashMap();
/*  99 */     this.consumerGroups = new CopyOnWriteArraySet();
/* 100 */     if (baseConfig.getTopicFamilies() != null) {
/* 101 */       this.topicFamilies.putAll(baseConfig.getTopicFamilies());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */       Set<String> consumerGroups = new HashSet();
/* 108 */       for (TopicFamilyConfiguration config : baseConfig.getTopicFamilies().values()) {
/* 109 */         consumerGroups.add(config.getConsumerGroupId());
/*     */       }
/* 111 */       this.consumerGroups.addAll(consumerGroups);
/*     */     }
/* 113 */     this.globalUniqueId = ("on-" + SystemVariableResolver.getHostName() + "-pid-" + SystemVariableResolver.getProcessId() + "-" + baseConfig.getMsgSrcId());
/*     */     
/*     */ 
/* 116 */     this.localUniqueIdGenerator = new AtomicInteger(new Random().nextInt(1024));
/* 117 */     this.injector = injector;
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   void start()
/*     */   {
/* 123 */     this.allMessageSources.register(this.baseConfig.getMsgSrcId(), this);
/*     */   }
/*     */   
/*     */   public void addTopicFamilyConfiguration(String topicFamilyName, TopicFamilyConfiguration configuration) {
/* 127 */     TopicFamilyConfiguration oldCfg = (TopicFamilyConfiguration)this.topicFamilies.putIfAbsent(topicFamilyName, configuration);
/* 128 */     Preconditions.checkArgument(oldCfg == null, "A topic family already exists with the name [" + topicFamilyName + "]");
/* 129 */     log.info("Added topic family name [{}] with configuration [\n{}]", topicFamilyName, Reader.toYaml(configuration));
/*     */     
/* 131 */     this.consumerGroups.add(configuration.consumerGroupId);
/*     */   }
/*     */   
/*     */   protected String generateId() {
/* 135 */     return this.globalUniqueId + "-id-" + this.localUniqueIdGenerator.getAndIncrement();
/*     */   }
/*     */   
/*     */ 
/*     */   protected static ConsumerConnector newConsumerConnector(KafkaMessageSourceConfiguration baseConfig, ConsumerConfig consumerConfig)
/*     */   {
/* 141 */     int triesLeft = baseConfig.getNumZookeeperConnectTries();
/* 142 */     ZkTimeoutException lastException = null;
/* 143 */     while (triesLeft > 0) {
/*     */       try {
/* 145 */         return Consumer.createJavaConsumerConnector(consumerConfig);
/*     */       } catch (ZkTimeoutException e) {
/* 147 */         lastException = e;
/* 148 */         triesLeft--;
/*     */       }
/*     */     }
/* 151 */     throw lastException;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public KafkaMessageSource get()
/*     */   {
/* 162 */     throw new UnsupportedOperationException("In order to get a KafkaMessageSource one needs to call the get method which takes a topic family name.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public KafkaMessageSource get(String topicFamily)
/*     */   {
/* 174 */     Set<String> topicsToSubscribe = new HashSet();
/* 175 */     TopicFamilyConfiguration topicFamilyConfiguration = (TopicFamilyConfiguration)this.topicFamilies.get(topicFamily);
/* 176 */     Preconditions.checkArgument(topicFamilyConfiguration != null, "Could not find topic family configuration for family [%s]", new Object[] { topicFamily });
/*     */     
/* 178 */     Set<String> topicsInFamily = topicFamilyConfiguration.getConsumableTopics();
/* 179 */     topicsToSubscribe.addAll(topicsInFamily);
/*     */     
/* 181 */     log.debug("Creating [{}] for {} topics", KafkaMessageSource.class.getSimpleName(), topicsToSubscribe);
/* 182 */     KafkaMessageSourceHook messageSourceHook = (KafkaMessageSourceHook)this.injector.getInstance(this.baseConfig.getMessageSourceHook());
/*     */     
/*     */ 
/* 185 */     String consumerId = generateId();
/* 186 */     String consumerGroupId = topicFamilyConfiguration.getConsumerGroupId();
/* 187 */     ConsumerConfig consumerConfig = this.baseConfig.newParsedConsumerConfig(consumerId, consumerGroupId);
/* 188 */     ConsumerConnector consumerConnector = newConsumerConnector(this.baseConfig, consumerConfig);
/*     */     
/* 190 */     String transientDeadLetterTopic = topicFamilyConfiguration.getTransientDeadLetterTopic();
/* 191 */     String permanentDeadLetterTopic = topicFamilyConfiguration.getPermanentDeadLetterTopic();
/*     */     
/* 193 */     KafkaMessageSource messageSource = new KafkaMessageSource(consumerId, consumerConfig, consumerConnector, topicsToSubscribe, messageSourceHook, transientDeadLetterTopic, permanentDeadLetterTopic, topicFamilyConfiguration.isExplicitCommitOffset());
/*     */     
/*     */ 
/*     */ 
/* 197 */     if (this.kafkaMessageSources.putIfAbsent(consumerId, messageSource) != null) {
/* 198 */       IOUtils.closeQuietly(messageSource);
/* 199 */       throw new IllegalArgumentException("The consumer id [" + consumerId + "] does not appear to be unique on this process");
/*     */     }
/*     */     
/* 202 */     log.info("Message source with consumer id [{}] has been setup to process messages from [{}]", consumerId, topicFamilyConfiguration);
/*     */     
/* 204 */     return messageSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<String> getConsumerGroups()
/*     */   {
/* 211 */     return this.consumerGroups;
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   void stop()
/*     */   {
/* 217 */     for (Map.Entry<String, KafkaMessageSource> entry : this.kafkaMessageSources.entrySet()) {
/* 218 */       String key = (String)entry.getKey();
/* 219 */       KafkaMessageSource messageSource = (KafkaMessageSource)entry.getValue();
/*     */       try {
/* 221 */         messageSource.close();
/*     */       } catch (IOException e) {
/* 223 */         log.warn("Error occurred while stopping message source [" + key + "]", e);
/*     */       }
/*     */     }
/* 226 */     this.kafkaMessageSources.clear();
/*     */     
/*     */ 
/* 229 */     this.allMessageSources.unregister(this.baseConfig.getMsgSrcId());
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/KafkaMessageSourceSupplier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */