/*     */ package com.appdynamics.analytics.processor.zookeeper.server;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.appdynamics.common.util.configuration.Parameters;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.zookeeper.server.DatadirCleanupManager;
/*     */ import org.apache.zookeeper.server.ServerCnxnFactory;
/*     */ import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
/*     */ import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
/*     */ import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
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
/*     */ class AbstractZooKeeperServer
/*     */   implements Closeable
/*     */ {
/*  29 */   private static final Logger log = LoggerFactory.getLogger(AbstractZooKeeperServer.class);
/*     */   
/*     */   private static final String MY_ID_FILENAME = "myid";
/*     */   private static final String PROP_SERVER_ID = "myid";
/*     */   private static final String PROP_DATA_DIR = "dataDir";
/*     */   
/*  35 */   FileTxnSnapLog getTxnLog() { return this.txnLog; }
/*  36 */   ServerCnxnFactory getCnxnFactory() { return this.cnxnFactory; }
/*  37 */   DatadirCleanupManager getPurgeMgr() { return this.purgeMgr; }
/*     */   
/*     */   AbstractZooKeeperServer(QuorumPeerConfig config) throws IOException {
/*  40 */     this.purgeMgr = new DatadirCleanupManager(config.getDataDir(), config.getDataLogDir(), config.getSnapRetainCount(), config.getPurgeInterval());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */     this.purgeMgr.start();
/*     */     try
/*     */     {
/*  49 */       this.txnLog = new FileTxnSnapLog(new File(config.getDataLogDir()), new File(config.getDataDir()));
/*  50 */       this.cnxnFactory = ServerCnxnFactory.createFactory();
/*  51 */       this.cnxnFactory.configure(config.getClientPortAddress(), config.getMaxClientCnxns());
/*     */     } catch (IOException e) {
/*  53 */       doClose();
/*  54 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final FileTxnSnapLog txnLog;
/*     */   private final ServerCnxnFactory cnxnFactory;
/*     */   private final DatadirCleanupManager purgeMgr;
/*     */   static AbstractZooKeeperServer makeServer(Properties zkConfig)
/*     */   {
/*  64 */     log.info("Starting ZooKeeper server");
/*     */     
/*     */     try
/*     */     {
/*  68 */       writeMyIdFile(zkConfig);
/*     */     } catch (IOException e) {
/*  70 */       throw new RuntimeException("Could not write myid file for zookeeper server", e);
/*     */     }
/*     */     
/*  73 */     QuorumPeerConfig config = new QuorumPeerConfig();
/*     */     try {
/*  75 */       config.parseProperties(zkConfig);
/*     */     } catch (IOException|QuorumPeerConfig.ConfigException e) {
/*  77 */       throw new ConfigurationException("Could not parse zookeeper configuration", e);
/*     */     }
/*     */     AbstractZooKeeperServer server;
/*     */     try {
/*     */       AbstractZooKeeperServer server;
/*  82 */       if (config.getServers().size() > 0) {
/*  83 */         server = new QuorumPeerServer(config);
/*     */       } else {
/*  85 */         server = new StandaloneServer(config);
/*     */       }
/*     */     } catch (IOException e) {
/*  88 */       throw new RuntimeException("Could not start zookeeper service", e);
/*     */     }
/*     */     
/*  91 */     return server;
/*     */   }
/*     */   
/*     */   private static void writeMyIdFile(Properties zkConfig) throws IOException
/*     */   {
/*  96 */     String dataDir = zkConfig.getProperty("dataDir");
/*  97 */     if ((dataDir == null) || (dataDir.length() == 0)) {
/*  98 */       dataDir = "data/zookeeper";
/*  99 */       zkConfig.setProperty("dataDir", dataDir);
/*     */     }
/* 101 */     File myIdFile = new File(dataDir, "myid");
/*     */     
/* 103 */     String serverId = zkConfig.getProperty("myid");
/*     */     
/* 105 */     Parameters.asMandatoryString(serverId, "Server Id is not specified. Use the 'myid' property to set id (must be an integer).");
/*     */     
/*     */ 
/* 108 */     FileUtils.write(myIdFile, serverId);
/*     */   }
/*     */   
/*     */   private void doClose() {
/*     */     try {
/* 113 */       if (this.txnLog != null) {
/* 114 */         this.txnLog.close();
/*     */       }
/*     */     } catch (Exception e) {
/* 117 */       log.warn("Could not close transaction log", e);
/*     */     }
/* 119 */     if (this.cnxnFactory != null) {
/*     */       try {
/* 121 */         this.cnxnFactory.shutdown();
/*     */       } catch (Exception e) {
/* 123 */         log.warn("Could not shutdown connection factory", e);
/*     */       }
/*     */       try {
/* 126 */         this.cnxnFactory.join();
/*     */       } catch (InterruptedException e) {
/* 128 */         log.warn("Interrupted while waiting for connection factory shutdown.");
/*     */       }
/*     */     }
/* 131 */     this.purgeMgr.shutdown();
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 136 */     doClose();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/server/AbstractZooKeeperServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */