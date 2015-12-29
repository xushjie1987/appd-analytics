/*    */ package com.appdynamics.analytics.processor.leader;
/*    */ 
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleAware;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ 
/*    */ public abstract class LeaderElection
/*    */   implements LifecycleAware
/*    */ {
/*    */   private String configurationPath;
/*    */   
/*    */   public abstract boolean isLeader();
/*    */   
/*    */   public abstract void waitToBeLeader() throws Exception;
/*    */   
/*    */   public abstract boolean waitToBeLeader(long paramLong, TimeUnit paramTimeUnit) throws Exception;
/*    */   
/* 18 */   public String getConfigurationPath() { return this.configurationPath; }
/* 19 */   public void setConfigurationPath(String configurationPath) { this.configurationPath = configurationPath; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/leader/LeaderElection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */