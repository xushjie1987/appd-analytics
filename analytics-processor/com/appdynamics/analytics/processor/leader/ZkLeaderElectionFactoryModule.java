/*    */ package com.appdynamics.analytics.processor.leader;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import org.apache.curator.framework.CuratorFramework;
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
/*    */ public class ZkLeaderElectionFactoryModule
/*    */   extends Module<Object>
/*    */ {
/*    */   @Provides
/*    */   @Singleton
/*    */   LeaderElectionFactory makeZkLeaderElectionFactory(CuratorFramework zkClient)
/*    */   {
/* 27 */     return new ZkLeaderElectionFactory(zkClient);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/leader/ZkLeaderElectionFactoryModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */