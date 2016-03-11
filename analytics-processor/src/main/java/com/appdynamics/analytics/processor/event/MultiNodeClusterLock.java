/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ZookeeperConstants;
/*    */ import com.appdynamics.common.util.exception.PermanentException;
/*    */ import com.appdynamics.common.util.exception.TransientException;
/*    */ import com.google.inject.Inject;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
/*    */ import org.apache.curator.framework.CuratorFramework;
/*    */ import org.apache.curator.framework.recipes.locks.InterProcessMutex;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MultiNodeClusterLock
/*    */   implements ClusterLock
/*    */ {
/* 32 */   public static final String ZK_CLUSTER_LOCK_PATH = ZookeeperConstants.ZK_BASE_PATH + "/cluster/locks/%s";
/*    */   final CuratorFramework zkClient;
/*    */   
/*    */   @Inject
/*    */   public MultiNodeClusterLock(CuratorFramework zkClient)
/*    */   {
/* 38 */     this.zkClient = zkClient;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public <T> T acquireAndExecute(String lockId, long time, TimeUnit unit, Callable<? extends T> callable)
/*    */   {
/* 45 */     InterProcessMutex lock = new InterProcessMutex(this.zkClient, String.format(ZK_CLUSTER_LOCK_PATH, new Object[] { lockId }));
/*    */     try
/*    */     {
/* 48 */       if (lock.acquire(time, unit)) {
/*    */         try {
/* 50 */           return (T)callable.call();
/*    */         } finally {
/* 52 */           lock.release();
/*    */         }
/*    */       }
/*    */       
/* 56 */       throw new TransientException(new TimeoutException("Unable to acquire lock within specified time period of " + time + " " + unit));
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 60 */       throw new PermanentException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/MultiNodeClusterLock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */