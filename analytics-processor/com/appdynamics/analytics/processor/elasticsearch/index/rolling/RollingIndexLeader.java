/*     */ package com.appdynamics.analytics.processor.elasticsearch.index.rolling;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.admin.ForcedRolloverRequest;
/*     */ import com.appdynamics.analytics.processor.event.ClusterLock;
/*     */ import com.appdynamics.analytics.processor.leader.LeaderElection;
/*     */ import com.appdynamics.analytics.processor.leader.LeaderElectionFactory;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.lifecycle.RunningState;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.io.EOFException;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RollingIndexLeader
/*     */   implements RollingIndexController
/*     */ {
/*  31 */   private static final Logger log = LoggerFactory.getLogger(RollingIndexLeader.class);
/*     */   private static final String LOCK_ID = "rolling_index_manager_rollover_lock";
/*     */   private final LeaderElection leaderElection;
/*     */   private final RollingIndexManager rollingIndexManager;
/*     */   private LeaderTask rollingTask;
/*     */   private Future leaderTaskFuture;
/*     */   private long scheduleIntervalMilliseconds;
/*     */   private final ExecutorService executorService;
/*     */   private final ClusterLock clusterLock;
/*     */   RunningState leaderRunning;
/*     */   
/*     */   public RunningState getLeaderRunning()
/*     */   {
/*  44 */     return this.leaderRunning;
/*     */   }
/*     */   
/*     */ 
/*     */   public RollingIndexLeader(LeaderElectionFactory leaderElectionFactory, RollingIndexManager rollingIndexManager, ClusterLock clusterLock, long scheduleIntervalMilliseconds)
/*     */   {
/*  50 */     this.clusterLock = clusterLock;
/*  51 */     this.leaderElection = leaderElectionFactory.makeLeader(RollingIndexConstants.LEADER_ZK_PATH);
/*  52 */     this.rollingIndexManager = rollingIndexManager;
/*  53 */     this.scheduleIntervalMilliseconds = scheduleIntervalMilliseconds;
/*  54 */     this.executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("rolling-index-leader").build());
/*     */     
/*     */ 
/*  57 */     this.leaderRunning = new RunningState(false);
/*     */   }
/*     */   
/*     */   public void start()
/*     */   {
/*  62 */     if (this.leaderRunning.get()) {
/*  63 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  67 */       this.leaderElection.start();
/*     */       
/*  69 */       this.rollingTask = new LeaderTask();
/*  70 */       this.leaderTaskFuture = this.executorService.submit(this.rollingTask);
/*     */       
/*  72 */       this.leaderRunning.set(true);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  76 */       log.error("Failed to start rolling index leader, please investigate!", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/*  82 */     if (!this.leaderRunning.get()) {
/*  83 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  88 */       synchronized (this.rollingTask.rolloverLock) {
/*  89 */         this.leaderElection.stop();
/*     */       }
/*     */       
/*  92 */       ConcurrencyHelper.cancel(this.leaderTaskFuture, 1000L, false, log);
/*     */     }
/*     */     catch (Exception e) {
/*  95 */       log.error("Failed to stop rolling index leader.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public RunningState getRunningState()
/*     */   {
/* 101 */     return this.leaderRunning;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 106 */   public void forceRollover(final ForcedRolloverRequest forcedRolloverRequest) { this.clusterLock.acquireAndExecute("rolling_index_manager_rollover_lock", this.scheduleIntervalMilliseconds, TimeUnit.MILLISECONDS, new Callable()
/*     */     {
/*     */       public Void call() throws Exception
/*     */       {
/* 110 */         RollingIndexLeader.log.info("Performing a forced roll over.");
/* 111 */         RollingIndexLeader.this.rollingIndexManager.forceRollOver(forcedRolloverRequest);
/* 112 */         RollingIndexLeader.log.info("Completed performing a forced roll over.");
/*     */         
/* 114 */         return null;
/*     */       }
/*     */     }); }
/*     */   
/*     */   public class LeaderTask implements Runnable {
/*     */     public LeaderTask() {}
/*     */     
/* 121 */     final Object rolloverLock = new Object();
/*     */     
/*     */     public void run() {
/* 124 */       boolean keepRunning = true;
/* 125 */       while (keepRunning) {
/*     */         try {
/* 127 */           RollingIndexLeader.this.leaderElection.waitToBeLeader();
/*     */           
/* 129 */           RollingIndexLeader.log.info("Elected leader for rolling index management.");
/*     */           
/* 131 */           while (RollingIndexLeader.this.leaderElection.isLeader())
/*     */           {
/* 133 */             ConcurrencyHelper.sleep(RollingIndexLeader.this.scheduleIntervalMilliseconds);
/*     */             
/* 135 */             RollingIndexLeader.this.clusterLock.acquireAndExecute("rolling_index_manager_rollover_lock", RollingIndexLeader.this.scheduleIntervalMilliseconds, TimeUnit.MILLISECONDS, new Callable()
/*     */             {
/*     */               public Void call()
/*     */                 throws Exception
/*     */               {
/* 140 */                 if (RollingIndexLeader.this.leaderElection.isLeader()) {
/* 141 */                   RollingIndexLeader.this.rollingIndexManager.doRollOver();
/*     */                 }
/* 143 */                 return null;
/*     */               }
/*     */             });
/*     */           }
/*     */           
/*     */ 
/* 149 */           RollingIndexLeader.log.info("No longer elected leader for rolling index management.");
/*     */         }
/*     */         catch (EOFException e) {
/* 152 */           RollingIndexLeader.log.debug("Rolling index leader latch closed, exiting thread.");
/* 153 */           keepRunning = false;
/*     */         } catch (Exception e) {
/* 155 */           RollingIndexLeader.log.error("Failed to roll indexes!!", e);
/* 156 */           if (Thread.currentThread().isInterrupted()) {
/* 157 */             keepRunning = false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/RollingIndexLeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */