/*    */ package com.appdynamics.analytics.processor.zookeeper.client;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.zookeeper.server.ZooKeeperHealthCheck;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.google.common.net.HostAndPort;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.PreDestroy;
/*    */ import org.apache.curator.RetryPolicy;
/*    */ import org.apache.curator.framework.CuratorFramework;
/*    */ import org.apache.curator.framework.CuratorFrameworkFactory;
/*    */ import org.apache.curator.retry.ExponentialBackoffRetry;
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
/*    */ public class ZookeeperClientModule
/*    */   extends Module<ZookeeperClientConfiguration>
/*    */ {
/* 30 */   private static final Logger log = LoggerFactory.getLogger(ZookeeperClientModule.class);
/*    */   
/*    */ 
/*    */ 
/*    */   CuratorFramework framework;
/*    */   
/*    */ 
/*    */ 
/*    */   @Provides
/*    */   @Singleton
/*    */   public CuratorFramework provideClient()
/*    */   {
/* 42 */     if (this.framework == null) {
/* 43 */       ZookeeperClientConfiguration config = (ZookeeperClientConfiguration)getConfiguration();
/* 44 */       RetryPolicy retryPolicy = new ExponentialBackoffRetry(config.getBaseSleepTimeMs(), config.getMaxRetries());
/* 45 */       this.framework = CuratorFrameworkFactory.newClient(config.getZookeeperConnect(), retryPolicy);
/* 46 */       this.framework.start();
/*    */     }
/* 48 */     return this.framework;
/*    */   }
/*    */   
/*    */   @Inject
/*    */   void start(ConsolidatedHealthCheck healthCheck) {
/* 53 */     String csv = ((ZookeeperClientConfiguration)getConfiguration()).getZookeeperConnect();
/* 54 */     String[] hostPorts = csv.split(",");
/* 55 */     List<HostAndPort> hostAndPorts = new ArrayList();
/* 56 */     for (String hostPort : hostPorts) {
/* 57 */       hostAndPorts.add(HostAndPort.fromString(hostPort));
/*    */     }
/* 59 */     healthCheck.register(new ZooKeeperHealthCheck(getUri(), hostAndPorts));
/*    */   }
/*    */   
/*    */   @PreDestroy
/*    */   public void stop() {
/* 64 */     log.info("Shutting down curator framework");
/* 65 */     if (this.framework != null) {
/* 66 */       this.framework.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/client/ZookeeperClientModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */