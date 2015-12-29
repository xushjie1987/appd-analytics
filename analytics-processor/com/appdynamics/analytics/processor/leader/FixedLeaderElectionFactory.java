/*    */ package com.appdynamics.analytics.processor.leader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FixedLeaderElectionFactory
/*    */   implements LeaderElectionFactory
/*    */ {
/*    */   public LeaderElection makeLeader(String leaderId)
/*    */   {
/* 13 */     return new FixedLeaderElection();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/leader/FixedLeaderElectionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */