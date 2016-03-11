/*    */ package com.appdynamics.analytics.processor.kafka.consumer;
/*    */ 
/*    */ import com.appdynamics.analytics.message.MessageSources;
/*    */ import com.appdynamics.analytics.processor.kafka.EventServiceKafkaConstants;
/*    */ import com.appdynamics.analytics.processor.zookeeper.client.ZookeeperVersionManager;
/*    */ import com.appdynamics.analytics.processor.zookeeper.server.ZooKeeperHealthCheck;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.io.IoHelper;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */ import com.google.common.net.HostAndPort;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Injector;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.curator.framework.CuratorFramework;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KafkaMessageSourceModule
/*    */   extends Module<KafkaMessageSourceConfiguration>
/*    */ {
/* 34 */   private static final Logger log = LoggerFactory.getLogger(KafkaMessageSourceModule.class);
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   public KafkaMessageSourceSupplier registerMessageSources(MessageSources messageSources, LifecycleInjector lifecycleInjector, ConsolidatedHealthCheck healthCheck, Injector injector)
/*    */   {
/* 40 */     KafkaMessageSourceConfiguration config = (KafkaMessageSourceConfiguration)getConfiguration();
/* 41 */     KafkaMessageSourceSupplier supplier = makeSupplier(messageSources, config, injector);
/* 42 */     supplier = (KafkaMessageSourceSupplier)lifecycleInjector.inject(supplier);
/*    */     
/* 44 */     String zkConnectString = (String)config.getConsumerConfig().get("zookeeper.connect");
/* 45 */     List<HostAndPort> hostAndPorts = IoHelper.parseHostAndPortCsv(zkConnectString);
/* 46 */     healthCheck.register(new ZooKeeperHealthCheck(getUri() + "'s zookeeper connection", hostAndPorts));
/* 47 */     healthCheck.register(new KafkaConsumerHealthCheck(config, supplier));
/*    */     
/* 49 */     return supplier;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected KafkaMessageSourceSupplier makeSupplier(MessageSources messageSources, KafkaMessageSourceConfiguration config, Injector injector)
/*    */   {
/* 61 */     return new KafkaMessageSourceSupplier(messageSources, config, injector);
/*    */   }
/*    */   
/*    */   @Inject
/*    */   public void onStart(KafkaMessageSourceSupplier supplier, CuratorFramework curatorFramework) {
/* 66 */     log.info("Started");
/*    */     try
/*    */     {
/* 69 */       ZookeeperVersionManager zookeeperVersionManager = new ZookeeperVersionManager(curatorFramework, EventServiceKafkaConstants.KAFKA_ZOOKEEPER_PATH);
/*    */       
/* 71 */       zookeeperVersionManager.checkSchemaVersion("Kafka", 1);
/*    */     } catch (IllegalStateException e) {
/* 73 */       log.warn("Current Kafka version of the running process did not match the authoritative Kafka version in Zookeeper.  This most likely means that you are running an incorrect version of the Kafka broker or consumer.  However, there may be situations where this is acceptable, for instance, if you're in the middle of migrating Kafka from one version to the next you may have multiple Kafka instances running on different versions at the same time.", e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/KafkaMessageSourceModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */