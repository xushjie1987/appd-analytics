/*     */ package com.appdynamics.analytics.processor.elasticsearch.index.compaction;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ZookeeperConstants;
/*     */ import com.appdynamics.analytics.processor.leader.LeaderElection;
/*     */ import com.appdynamics.analytics.processor.leader.LeaderElectionFactory;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import java.io.EOFException;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.curator.utils.ZKPaths;
/*     */ import org.joda.time.DateTime;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndexCompactionLeader
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(IndexCompactionLeader.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   static final String LEADER_ZK_PATH = ZKPaths.makePath(ZookeeperConstants.ZK_BASE_PATH, "index-compaction");
/*     */   
/*     */   private IndexCompactionTask indexCompactionTask;
/*     */   
/*     */   private Future<?> indexCompactionTaskFuture;
/*     */   private final LeaderElection leaderElection;
/*     */   private final IndexCompactionManager indexCompactionManager;
/*     */   private final DateTime startTime;
/*     */   private final boolean autoRun;
/*  52 */   private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
/*     */   private final AtomicBoolean leaderRunning;
/*     */   private final AtomicBoolean lastExecutionSuccessful;
/*     */   
/*     */   public AtomicBoolean getLeaderRunning() {
/*  57 */     return this.leaderRunning;
/*     */   }
/*     */   
/*     */ 
/*     */   public AtomicBoolean getLastExecutionSuccessful()
/*     */   {
/*  63 */     return this.lastExecutionSuccessful;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IndexCompactionLeader(LeaderElectionFactory leaderElectionFactory, IndexCompactionManager indexCompactionManager, boolean autoRun, String timeToRun, String timeFormat)
/*     */   {
/*  71 */     this.leaderElection = leaderElectionFactory.makeLeader(LEADER_ZK_PATH);
/*  72 */     this.indexCompactionManager = indexCompactionManager;
/*  73 */     this.startTime = DateTimes.getNextDateTimeOccurrence(timeToRun, timeFormat);
/*  74 */     this.leaderRunning = new AtomicBoolean(false);
/*  75 */     this.lastExecutionSuccessful = new AtomicBoolean(true);
/*  76 */     this.autoRun = autoRun;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  83 */     if (this.autoRun)
/*     */     {
/*  85 */       log.info("Auto-run set to true - starting index compaction leader");
/*  86 */       if (this.leaderRunning.compareAndSet(false, true)) {
/*     */         try {
/*  88 */           this.leaderElection.start();
/*     */           
/*  90 */           this.indexCompactionTask = new IndexCompactionTask();
/*  91 */           sheduleIndexCompaction(this.startTime);
/*  92 */           log.debug("Done starting index compaction leader");
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/*  96 */           log.error("Failed to start index compaction leader, please investigate!", e);
/*  97 */           this.leaderRunning.set(false);
/*     */         }
/*     */       } else {
/* 100 */         log.debug("Index compaction leader already started");
/*     */       }
/*     */     } else {
/* 103 */       log.info("Auto-run set to false, this node will not participate in index compaction");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void sheduleIndexCompaction(DateTime time)
/*     */   {
/* 113 */     long delayInMillis = time.getMillis() - TimeKeeper.currentUtcTime().getMillis();
/* 114 */     long periodInMillis = 86400000L;
/* 115 */     log.info("Scheduling index compaction routine to happen at [{}:{}], every day. First run will be at [{}], which is in [{}] seconds", new Object[] { Integer.valueOf(time.getHourOfDay()), Integer.valueOf(time.getMinuteOfHour()), TimeKeeper.currentUtcTime().plusMillis((int)delayInMillis), Double.valueOf(delayInMillis / 1000.0D) });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */     this.indexCompactionTaskFuture = this.executorService.scheduleAtFixedRate(this.indexCompactionTask, delayInMillis, periodInMillis, TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 130 */     if (this.leaderRunning.compareAndSet(true, false)) {
/* 131 */       if (this.autoRun) {
/*     */         try {
/* 133 */           ConcurrencyHelper.getOrCancel(this.indexCompactionTaskFuture, 1, log);
/*     */         } catch (CancellationException e) {
/* 135 */           log.debug("Index compaction task did not close in time, had to be interrupted.", e);
/*     */         }
/*     */       }
/* 138 */       this.leaderElection.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   public class IndexCompactionTask implements Runnable
/*     */   {
/*     */     public IndexCompactionTask() {}
/*     */     
/*     */     public void run() {
/* 147 */       IndexCompactionLeader.log.info("Checking for index compaction kickoff: currently leader status is [{}].", Boolean.valueOf(IndexCompactionLeader.this.leaderElection.isLeader()));
/*     */       try
/*     */       {
/* 150 */         boolean isLeader = IndexCompactionLeader.this.leaderElection.waitToBeLeader(10L, TimeUnit.SECONDS);
/* 151 */         if (isLeader) {
/* 152 */           executeIndexCompactionManager();
/*     */         }
/*     */       }
/*     */       catch (EOFException e) {
/* 156 */         IndexCompactionLeader.log.debug("Index compaction leader latch closed, likely failed to run compaction.");
/*     */       } catch (Exception e) {
/* 158 */         IndexCompactionLeader.log.error("Failed to run compaction!", e);
/*     */       }
/*     */     }
/*     */     
/*     */     private void executeIndexCompactionManager() {
/* 163 */       IndexCompactionLeader.log.info("This node is leader for compacting old data, starting the routine!");
/* 164 */       IndexCompactionLeader.this.lastExecutionSuccessful.set(false);
/* 165 */       IndexCompactionLeader.this.lastExecutionSuccessful.set(IndexCompactionLeader.this.indexCompactionManager.compactData());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionLeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */