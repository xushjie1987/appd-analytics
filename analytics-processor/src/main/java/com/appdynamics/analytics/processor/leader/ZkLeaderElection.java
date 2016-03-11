/*    */ package com.appdynamics.analytics.processor.leader;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.curator.framework.CuratorFramework;
/*    */ import org.apache.curator.framework.recipes.leader.LeaderLatch;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZkLeaderElection
/*    */   extends LeaderElection
/*    */ {
/* 17 */   private static final Logger log = LoggerFactory.getLogger(ZkLeaderElection.class);
/*    */   
/*    */   private final LeaderLatch leaderLatch;
/*    */   
/*    */   @Inject
/*    */   public ZkLeaderElection(CuratorFramework zkClient, String leaderPath)
/*    */   {
/* 24 */     this.leaderLatch = new LeaderLatch(zkClient, leaderPath);
/*    */   }
/*    */   
/*    */   public void start()
/*    */   {
/*    */     try {
/* 30 */       this.leaderLatch.start();
/*    */     } catch (Exception e) {
/* 32 */       log.error("Failed to start Zookeeper leader latch", e);
/*    */     }
/*    */   }
/*    */   
/*    */   public void stop()
/*    */   {
/*    */     try {
/* 39 */       this.leaderLatch.close();
/*    */     } catch (Exception e) {
/* 41 */       log.debug("Failed to stop Zookeeper leader latch", e);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isLeader() {
/* 46 */     return this.leaderLatch.hasLeadership();
/*    */   }
/*    */   
/*    */   public void waitToBeLeader() throws Exception {
/* 50 */     this.leaderLatch.await();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean waitToBeLeader(long timeout, TimeUnit timeUnit)
/*    */     throws Exception
/*    */   {
/* 58 */     return this.leaderLatch.await(timeout, timeUnit);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/leader/ZkLeaderElection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */