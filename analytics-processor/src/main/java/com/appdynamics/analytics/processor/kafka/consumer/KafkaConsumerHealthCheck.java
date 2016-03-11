/*     */ package com.appdynamics.analytics.processor.kafka.consumer;
/*     */ 
/*     */ import com.appdynamics.common.util.health.SimpleHealthCheck;
/*     */ import com.codahale.metrics.health.HealthCheck.Result;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.ReflectionException;
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
/*     */ class KafkaConsumerHealthCheck
/*     */   extends SimpleHealthCheck
/*     */ {
/*     */   private static final String JMX_NAME_BYTES = "kafka.consumer:type=ConsumerTopicMetrics,name=BytesPerSec,clientId=%s";
/*     */   private static final String JMX_NAME_MSGS = "kafka.consumer:type=ConsumerTopicMetrics,name=MessagesPerSec,clientId=%s";
/*     */   private static final String JMX_NAME_CONSUMER_LAG = "kafka.consumer:type=ConsumerFetcherManager,name=MaxLag,clientId=%s";
/*     */   private final int maxHealthyConsumerLag;
/*     */   private final MBeanServer mBeanServer;
/*     */   private final KafkaMessageSourceSupplier supplier;
/*     */   private final ConcurrentHashMap<String, EnumMap<JmxObject, ObjectName>> consumerGroupToJmxObject;
/*     */   
/*     */   KafkaConsumerHealthCheck(KafkaMessageSourceConfiguration config, KafkaMessageSourceSupplier supplier)
/*     */   {
/*  49 */     super("KafkaConsumer");
/*  50 */     this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
/*  51 */     this.maxHealthyConsumerLag = config.getMaxHealthyConsumerLag();
/*  52 */     this.supplier = supplier;
/*  53 */     this.consumerGroupToJmxObject = new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */   public HealthCheck.Result check()
/*     */   {
/*  58 */     Set<String> consumerGroups = this.supplier.getConsumerGroups();
/*  59 */     if (consumerGroups.isEmpty()) {
/*  60 */       return HealthCheck.Result.unhealthy("There are currently no Kafka consumers configured");
/*     */     }
/*     */     try {
/*  63 */       StringBuilder healthStatusMessage = new StringBuilder();
/*     */       
/*  65 */       boolean isFirst = true;
/*  66 */       for (String consumerGroup : consumerGroups) {
/*  67 */         EnumMap<JmxObject, ObjectName> jmxObjects = (EnumMap)this.consumerGroupToJmxObject.get(consumerGroup);
/*  68 */         if (jmxObjects == null) {
/*  69 */           EnumMap<JmxObject, ObjectName> objects = new EnumMap(JmxObject.class);
/*  70 */           for (JmxObject object : JmxObject.values()) {
/*  71 */             ObjectName objectName = new ObjectName(String.format(object.getObjectNameTemplate(), new Object[] { consumerGroup }));
/*     */             
/*  73 */             objects.put(object, objectName);
/*     */           }
/*     */           
/*  76 */           jmxObjects = (EnumMap)this.consumerGroupToJmxObject.putIfAbsent(consumerGroup, objects);
/*     */           
/*  78 */           if (jmxObjects == null) {
/*  79 */             jmxObjects = objects;
/*     */           }
/*     */         }
/*     */         
/*  83 */         Long countBytesIn = (Long)this.mBeanServer.getAttribute((ObjectName)jmxObjects.get(JmxObject.COUNT_BYTES_IN), "Count");
/*  84 */         Long maxLag = (Long)this.mBeanServer.getAttribute((ObjectName)jmxObjects.get(JmxObject.MAX_LAG), "Value");
/*  85 */         Double bytesInRate = (Double)this.mBeanServer.getAttribute((ObjectName)jmxObjects.get(JmxObject.BYTES_IN_RATE), "FiveMinuteRate");
/*     */         
/*  87 */         Double msgInRate = (Double)this.mBeanServer.getAttribute((ObjectName)jmxObjects.get(JmxObject.MSG_IN_RATE), "FiveMinuteRate");
/*     */         
/*     */ 
/*  90 */         if (!isFirst) {
/*  91 */           healthStatusMessage.append(",    ");
/*     */         } else {
/*  93 */           isFirst = false;
/*     */         }
/*     */         
/*  96 */         healthStatusMessage.append(String.format("[ConsumerGroupId: %s, CountBytes: %d bytes, FiveMinRate: %f bytes/s, FiveMinMsgRate: %f msgs/s, MaxConsumerLagOverAllPartitions: %d messages]", new Object[] { consumerGroup, countBytesIn, bytesInRate, msgInRate, maxLag }));
/*     */       }
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
/* 109 */       return HealthCheck.Result.healthy(healthStatusMessage.toString());
/*     */     }
/*     */     catch (MBeanException|AttributeNotFoundException|InstanceNotFoundException|ReflectionException|MalformedObjectNameException e) {
/* 112 */       return HealthCheck.Result.unhealthy(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static enum JmxObject
/*     */   {
/* 121 */     COUNT_BYTES_IN("kafka.consumer:type=ConsumerTopicMetrics,name=BytesPerSec,clientId=%s"), 
/* 122 */     MAX_LAG("kafka.consumer:type=ConsumerFetcherManager,name=MaxLag,clientId=%s"), 
/* 123 */     BYTES_IN_RATE("kafka.consumer:type=ConsumerTopicMetrics,name=BytesPerSec,clientId=%s"), 
/* 124 */     MSG_IN_RATE("kafka.consumer:type=ConsumerTopicMetrics,name=MessagesPerSec,clientId=%s");
/*     */     
/*     */     private String objectNameTemplate;
/*     */     
/*     */     private JmxObject(String objectNameTemplate) {
/* 129 */       this.objectNameTemplate = objectNameTemplate;
/*     */     }
/*     */     
/*     */     private String getObjectNameTemplate() {
/* 133 */       return this.objectNameTemplate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/KafkaConsumerHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */