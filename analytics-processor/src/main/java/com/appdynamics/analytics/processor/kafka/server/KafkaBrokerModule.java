/*     */ package com.appdynamics.analytics.processor.kafka.server;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.kafka.EventServiceKafkaConstants;
/*     */ import com.appdynamics.analytics.processor.kafka.util.KafkaToolResource;
/*     */ import com.appdynamics.analytics.processor.zookeeper.client.ZookeeperVersionManager;
/*     */ import com.appdynamics.analytics.processor.zookeeper.server.ZooKeeperHealthCheck;
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.io.IoHelper;
/*     */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*     */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*     */ import com.google.common.net.HostAndPort;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Provides;
/*     */ import com.google.inject.Singleton;
/*     */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import kafka.server.KafkaConfig;
/*     */ import kafka.server.KafkaServerStartable;
/*     */ import org.apache.curator.framework.CuratorFramework;
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
/*     */ public class KafkaBrokerModule
/*     */   extends Module<Map<String, String>>
/*     */ {
/*  42 */   private static final Logger log = LoggerFactory.getLogger(KafkaBrokerModule.class);
/*     */   @Inject
/*     */   private volatile LifecycleInjector lifecycleInjector;
/*     */   @Inject
/*     */   private volatile Environment environment;
/*     */   @Inject
/*     */   private volatile ConsolidatedHealthCheck healthCheck;
/*     */   @Inject
/*     */   private volatile KafkaBrokerHealthCheck brokerHealthCheck;
/*     */   @Inject
/*     */   private volatile KafkaServerStartable kafkaServer;
/*     */   @Inject
/*     */   private volatile CuratorFramework curatorFramework;
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   KafkaServerStartable provideServer()
/*     */   {
/*  60 */     Properties props = new Properties();
/*  61 */     props.putAll((Map)getConfiguration());
/*     */     
/*     */ 
/*  64 */     if (((Map)getConfiguration()).containsKey("zookeeper.connect")) {
/*  65 */       String zkConnectString = props.getProperty("zookeeper.connect");
/*  66 */       zkConnectString = zkConnectString + EventServiceKafkaConstants.KAFKA_ZOOKEEPER_CHROOT;
/*  67 */       props.setProperty("zookeeper.connect", zkConnectString);
/*     */     }
/*     */     
/*  70 */     KafkaConfig kafkaConfig = new KafkaConfig(props);
/*  71 */     KafkaServerStartable server = new KafkaServerStartable(kafkaConfig);
/*     */     
/*  73 */     log.info("Starting");
/*  74 */     server.startup();
/*  75 */     log.info("Started");
/*     */     
/*  77 */     return server;
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   void start() {
/*  82 */     String zkConnectString = (String)((Map)getConfiguration()).get("zookeeper.connect");
/*     */     
/*  84 */     List<HostAndPort> hostAndPorts = IoHelper.parseHostAndPortCsv(zkConnectString);
/*  85 */     this.healthCheck.register(new ZooKeeperHealthCheck(getUri() + "'s zookeeper connection", hostAndPorts));
/*  86 */     this.healthCheck.register(this.brokerHealthCheck);
/*     */     
/*  88 */     KafkaToolResource brokerResource = new KafkaToolResource(zkConnectString + EventServiceKafkaConstants.KAFKA_ZOOKEEPER_CHROOT);
/*  89 */     brokerResource = (KafkaToolResource)this.lifecycleInjector.inject(brokerResource);
/*  90 */     this.environment.jersey().register(brokerResource);
/*     */     try
/*     */     {
/*  93 */       brokerResource.report();
/*     */     } catch (RuntimeException e) {
/*  95 */       log.warn("Error occurred while retrieving cluster status report", e);
/*     */     }
/*     */     try
/*     */     {
/*  99 */       ZookeeperVersionManager zookeeperVersionManager = new ZookeeperVersionManager(this.curatorFramework, EventServiceKafkaConstants.KAFKA_ZOOKEEPER_PATH);
/*     */       
/* 101 */       zookeeperVersionManager.checkSchemaVersion("Kafka", 1);
/*     */     } catch (IllegalStateException e) {
/* 103 */       log.warn("Current Kafka version of the running process did not match the authoritative Kafka version in Zookeeper.  This most likely means that you are running an incorrect version of the Kafka broker or consumer.  However, there may be situations where this is acceptable, for instance, if you're in the middle of migrating Kafka from one version to the next you may have multiple Kafka instances running on different versions at the same time.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @PreDestroy
/*     */   public void stop()
/*     */   {
/* 113 */     log.info("Stopping");
/* 114 */     this.kafkaServer.shutdown();
/* 115 */     this.kafkaServer.awaitShutdown();
/* 116 */     log.info("Stopped");
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/server/KafkaBrokerModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */