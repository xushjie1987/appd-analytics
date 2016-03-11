/*    */ package com.appdynamics.analytics.processor.leader;
/*    */ 
/*    */ import org.apache.curator.framework.CuratorFramework;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZkLeaderElectionFactory
/*    */   implements LeaderElectionFactory
/*    */ {
/*    */   private final CuratorFramework zkClient;
/*    */   
/*    */   public ZkLeaderElectionFactory(CuratorFramework zkClient)
/*    */   {
/* 17 */     this.zkClient = zkClient;
/*    */   }
/*    */   
/*    */   public LeaderElection makeLeader(String leaderPath) {
/* 21 */     return new ZkLeaderElection(this.zkClient, leaderPath);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/leader/ZkLeaderElectionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */