/*     */ package com.appdynamics.analytics.processor.zookeeper.server;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.zookeeper.util.ZooKeeperToolResource;
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.util.configuration.Parameters;
/*     */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*     */ import com.google.common.net.HostAndPort;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Provides;
/*     */ import com.google.inject.Singleton;
/*     */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.apache.curator.framework.CuratorFramework;
/*     */ import org.apache.curator.framework.CuratorFrameworkFactory;
/*     */ import org.apache.curator.retry.RetryOneTime;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZooKeeperModule
/*     */   extends Module<Map<String, String>>
/*     */ {
/*  36 */   private static final Logger log = LoggerFactory.getLogger(ZooKeeperModule.class);
/*     */   
/*     */ 
/*     */   public static final String ZK_PROP_CLIENT_ADDRESS = "clientPortAddress";
/*     */   
/*     */ 
/*     */   public static final String ZK_PROP_CLIENT_PORT = "clientPort";
/*     */   
/*     */ 
/*     */   private volatile AbstractZooKeeperServer zkServer;
/*     */   
/*     */ 
/*     */   private volatile CuratorFramework curatorFramework;
/*     */   
/*     */ 
/*     */ 
/*     */   static Properties toProperties(Map<String, String> configuration)
/*     */   {
/*  54 */     for (Iterator<Map.Entry<String, String>> iterator = configuration.entrySet().iterator(); iterator.hasNext();) {
/*  55 */       Map.Entry<String, String> entry = (Map.Entry)iterator.next();
/*  56 */       String key = (String)entry.getKey();
/*  57 */       String value = (String)entry.getValue();
/*     */       
/*  59 */       if ((Pattern.matches("server\\.\\d+", key)) && (Parameters.defaultIfBlank(value, null) == null)) {
/*  60 */         iterator.remove();
/*     */       }
/*     */     }
/*     */     
/*  64 */     Properties props = new Properties();
/*  65 */     props.putAll(configuration);
/*  66 */     return props;
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   AbstractZooKeeperServer provideServer() {
/*  72 */     Properties props = toProperties((Map)getConfiguration());
/*  73 */     return AbstractZooKeeperServer.makeServer(props);
/*     */   }
/*     */   
/*     */   @Inject
/*     */   void start(AbstractZooKeeperServer zkServer, ConsolidatedHealthCheck healthCheck, Environment environment) {
/*  78 */     this.zkServer = zkServer;
/*     */     
/*  80 */     String hostName = (String)((Map)getConfiguration()).get("clientPortAddress");
/*     */     
/*  82 */     if (hostName != null) {
/*  83 */       int port = Integer.parseInt((String)((Map)getConfiguration()).get("clientPort"));
/*  84 */       HostAndPort hostAndPort = HostAndPort.fromParts(hostName, port);
/*  85 */       ZooKeeperHealthCheck zooKeeperHealthCheck = new ZooKeeperHealthCheck(getUri(), Collections.singleton(hostAndPort));
/*     */       
/*  87 */       healthCheck.register(zooKeeperHealthCheck);
/*     */       
/*  89 */       String zkConnectString = hostName + ":" + port;
/*  90 */       this.curatorFramework = CuratorFrameworkFactory.newClient(zkConnectString, new RetryOneTime(100));
/*  91 */       this.curatorFramework.start();
/*  92 */       environment.jersey().register(new ZooKeeperToolResource(this.curatorFramework, zooKeeperHealthCheck));
/*     */     }
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   public void stop() {
/*  98 */     log.info("Shutting down ZooKeeper service");
/*  99 */     if (this.curatorFramework != null) {
/* 100 */       this.curatorFramework.close();
/*     */     }
/* 102 */     this.zkServer.close();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/server/ZooKeeperModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */