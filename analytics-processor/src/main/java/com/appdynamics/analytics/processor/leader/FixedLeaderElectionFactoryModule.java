/*    */ package com.appdynamics.analytics.processor.leader;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FixedLeaderElectionFactoryModule
/*    */   extends Module<Object>
/*    */ {
/*    */   @Provides
/*    */   @Singleton
/*    */   LeaderElectionFactory makeFixedLeaderElectionFactory()
/*    */   {
/* 19 */     return new FixedLeaderElectionFactory();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/leader/FixedLeaderElectionFactoryModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */