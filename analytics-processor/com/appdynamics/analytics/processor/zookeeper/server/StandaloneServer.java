/*    */ package com.appdynamics.analytics.processor.zookeeper.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.zookeeper.server.ServerCnxnFactory;
/*    */ import org.apache.zookeeper.server.ZooKeeperServer;
/*    */ import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
/*    */ import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class StandaloneServer
/*    */   extends AbstractZooKeeperServer
/*    */ {
/* 20 */   private static final Logger log = LoggerFactory.getLogger(StandaloneServer.class);
/*    */   private final ZooKeeperServer zkServer;
/*    */   
/*    */   StandaloneServer(QuorumPeerConfig config)
/*    */     throws IOException
/*    */   {
/* 26 */     super(config);
/*    */     
/* 28 */     this.zkServer = new ZooKeeperServer();
/*    */     
/* 30 */     FileTxnSnapLog txnLog = getTxnLog();
/* 31 */     ServerCnxnFactory cnxnFactory = getCnxnFactory();
/*    */     try
/*    */     {
/* 34 */       this.zkServer.setTxnLogFactory(txnLog);
/* 35 */       this.zkServer.setTickTime(config.getTickTime());
/* 36 */       this.zkServer.setMinSessionTimeout(config.getMinSessionTimeout());
/* 37 */       this.zkServer.setMaxSessionTimeout(config.getMaxSessionTimeout());
/*    */       try {
/* 39 */         cnxnFactory.startup(this.zkServer);
/*    */       } catch (InterruptedException e) {
/* 41 */         throw new RuntimeException("Failed to start up connection factory", e);
/*    */       }
/*    */     } catch (IOException|RuntimeException e) {
/* 44 */       close();
/* 45 */       throw e;
/*    */     }
/*    */   }
/*    */   
/*    */   public void close()
/*    */   {
/* 51 */     super.close();
/* 52 */     if (this.zkServer.isRunning()) {
/* 53 */       this.zkServer.shutdown();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/server/StandaloneServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */