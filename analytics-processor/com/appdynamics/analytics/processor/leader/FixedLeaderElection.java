/*    */ package com.appdynamics.analytics.processor.leader;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FixedLeaderElection
/*    */   extends LeaderElection
/*    */ {
/*    */   public void start() {}
/*    */   
/*    */   public void stop() {}
/*    */   
/*    */   public boolean isLeader()
/*    */   {
/* 27 */     return true;
/*    */   }
/*    */   
/*    */   public void waitToBeLeader()
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */   public boolean waitToBeLeader(long timeout, TimeUnit timeUnit)
/*    */     throws Exception
/*    */   {
/* 37 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/leader/FixedLeaderElection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */