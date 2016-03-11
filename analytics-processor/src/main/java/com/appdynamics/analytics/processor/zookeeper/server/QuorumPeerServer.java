/*    */ package com.appdynamics.analytics.processor.zookeeper.server;
/*    */ 
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import org.apache.zookeeper.server.ServerCnxnFactory;
/*    */ import org.apache.zookeeper.server.ZKDatabase;
/*    */ import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
/*    */ import org.apache.zookeeper.server.quorum.QuorumPeer;
/*    */ import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class QuorumPeerServer
/*    */   extends AbstractZooKeeperServer
/*    */ {
/* 22 */   private static final Logger log = LoggerFactory.getLogger(QuorumPeerServer.class);
/*    */   private static final long SHUTDOWN_TIMEOUT = 10000L;
/*    */   private final QuorumPeer quorumPeer;
/*    */   
/*    */   public QuorumPeerServer(QuorumPeerConfig config)
/*    */     throws IOException
/*    */   {
/* 29 */     super(config);
/*    */     
/* 31 */     FileTxnSnapLog txnLog = getTxnLog();
/* 32 */     ServerCnxnFactory cnxnFactory = getCnxnFactory();
/*    */     
/* 34 */     this.quorumPeer = new QuorumPeer();
/* 35 */     this.quorumPeer.setClientPortAddress(config.getClientPortAddress());
/* 36 */     this.quorumPeer.setTxnFactory(txnLog);
/*    */     
/* 38 */     int numServers = config.getServers().size();
/* 39 */     if (numServers % 2 == 0) {
/* 40 */       throw new ConfigurationException("Expected to have an odd number of servers. Found " + numServers + ".");
/*    */     }
/* 42 */     this.quorumPeer.setQuorumPeers(config.getServers());
/* 43 */     this.quorumPeer.setElectionType(config.getElectionAlg());
/* 44 */     this.quorumPeer.setMyid(config.getServerId());
/* 45 */     this.quorumPeer.setTickTime(config.getTickTime());
/* 46 */     this.quorumPeer.setMinSessionTimeout(config.getMinSessionTimeout());
/* 47 */     this.quorumPeer.setMaxSessionTimeout(config.getMaxSessionTimeout());
/* 48 */     this.quorumPeer.setInitLimit(config.getInitLimit());
/* 49 */     this.quorumPeer.setSyncLimit(config.getSyncLimit());
/* 50 */     this.quorumPeer.setQuorumVerifier(config.getQuorumVerifier());
/* 51 */     this.quorumPeer.setCnxnFactory(cnxnFactory);
/* 52 */     this.quorumPeer.setZKDatabase(new ZKDatabase(this.quorumPeer.getTxnFactory()));
/* 53 */     this.quorumPeer.setLearnerType(config.getPeerType());
/* 54 */     this.quorumPeer.setSyncEnabled(config.getSyncEnabled());
/* 55 */     this.quorumPeer.setQuorumListenOnAllIPs(config.getQuorumListenOnAllIPs().booleanValue());
/*    */     
/* 57 */     this.quorumPeer.start();
/*    */   }
/*    */   
/*    */   public void close()
/*    */   {
/* 62 */     super.close();
/* 63 */     this.quorumPeer.shutdown();
/*    */     try {
/* 65 */       this.quorumPeer.join(10000L);
/*    */     } catch (InterruptedException e) {
/* 67 */       log.warn("ZooKeeper service did not shutdown in time.");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/server/QuorumPeerServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */