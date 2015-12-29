/*     */ package com.appdynamics.analytics.processor.event.meter;
/*     */ 
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
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
/*     */ class MetersManager
/*     */ {
/*  31 */   private static final Logger log = LoggerFactory.getLogger(MetersManager.class);
/*     */   
/*     */   private final ConcurrentMap<MeterKey, Meter> meters;
/*     */   
/*     */   private final MetersStore metersStore;
/*     */   
/*     */   private final TimeKeeper timeKeeper;
/*     */   
/*     */   private final ConcurrentMap<MeterKey, Meter> previousReapedMeters;
/*     */   
/*     */   private final ScheduledExecutorService executorService;
/*     */   private volatile ScheduledFuture<?> taskRef;
/*     */   private volatile boolean stopRequested;
/*     */   
/*     */   MetersManager(Meters meters, MetersStore metersStore, TimeKeeper timeKeeper)
/*     */   {
/*  47 */     this.meters = meters.getMeters();
/*  48 */     this.metersStore = metersStore;
/*  49 */     this.timeKeeper = timeKeeper;
/*  50 */     this.previousReapedMeters = new ConcurrentHashMap();
/*  51 */     this.executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("analytics-meters-manager-%d").build());
/*     */   }
/*     */   
/*     */ 
/*     */   @PostConstruct
/*     */   void start()
/*     */   {
/*  58 */     this.stopRequested = false;
/*  59 */     Runnable task = new Runnable()
/*     */     {
/*     */       public void run() {
/*     */         try {
/*  63 */           MetersManager.this.manage();
/*     */         } catch (RuntimeException r) {
/*  65 */           MetersManager.log.warn("Error occurred while performing scheduled meter reaping", r);
/*     */         } catch (Throwable t) {
/*  67 */           MetersManager.log.error("Severe error occurred while performing scheduled meter reaping. No further scheduled meter reaping will be performed", t);
/*     */           
/*     */ 
/*  70 */           throw t;
/*     */         }
/*     */       }
/*  73 */     };
/*  74 */     this.taskRef = scheduleTask(this.executorService, task, this.timeKeeper);
/*     */   }
/*     */   
/*     */   ScheduledFuture<?> scheduleTask(ScheduledExecutorService executorService, Runnable task, TimeKeeper timeKeeper) {
/*  78 */     DateTime nextMinuteStart = timeKeeper.getCurrentMinute().plusMinutes(1);
/*     */     
/*  80 */     long startDelayMillis = Math.min(TimeUnit.MINUTES.toMillis(1L), Math.max(0L, System.currentTimeMillis() - nextMinuteStart.getMillis()));
/*     */     
/*     */ 
/*  83 */     return executorService.scheduleAtFixedRate(task, startDelayMillis, TimeUnit.MINUTES.toMillis(1L), TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */   ScheduledFuture<?> getTaskRef()
/*     */   {
/*  89 */     return this.taskRef;
/*     */   }
/*     */   
/*     */   void manage() {
/*  93 */     if (this.stopRequested) {
/*  94 */       return;
/*     */     }
/*  96 */     log.debug("Starting scheduled work");
/*     */     
/*  98 */     purgeOldReapedMeters();
/*  99 */     reapOldMeters();
/*     */     
/* 101 */     log.debug("Completed scheduled work");
/*     */   }
/*     */   
/*     */   private void purgeOldReapedMeters()
/*     */   {
/* 106 */     log.debug("Preparing to purge [{}] old meter readings", Integer.valueOf(this.previousReapedMeters.size()));
/* 107 */     for (Iterator<Map.Entry<MeterKey, Meter>> it = this.previousReapedMeters.entrySet().iterator(); it.hasNext();) {
/* 108 */       Map.Entry<MeterKey, Meter> entry = (Map.Entry)it.next();
/* 109 */       MeterKey key = (MeterKey)entry.getKey();
/* 110 */       this.meters.remove(key);
/* 111 */       it.remove();
/*     */     }
/* 113 */     log.debug("Completed purging of old meter readings");
/*     */   }
/*     */   
/*     */   private void reapOldMeters() {
/* 117 */     DateTime latestReapableTime = this.timeKeeper.getPreviousMinute();
/* 118 */     long latestReapableTimeInMillis = latestReapableTime.getMillis();
/*     */     
/* 120 */     log.debug("Preparing to reap meter readings");
/* 121 */     Map<MeterKey, MeterReapResult> reapedCounts = new HashMap(this.meters.size());
/* 122 */     for (Meter meter : this.meters.values())
/*     */     {
/*     */ 
/* 125 */       if (meter.getTimeWindowStart() <= latestReapableTimeInMillis) {
/* 126 */         MeterReapResult reapResult = meter.reap();
/*     */         
/* 128 */         if ((reapResult.getUsage() > 0L) || (reapResult.getExcess() > 0L)) {
/* 129 */           reapedCounts.put(meter, reapResult);
/*     */         }
/* 131 */         this.previousReapedMeters.put(meter, meter);
/*     */       }
/*     */     }
/* 134 */     log.debug("Completed reaping [{}] meter readings", Integer.valueOf(reapedCounts.size()));
/*     */     
/* 136 */     if (reapedCounts.size() > 0) {
/* 137 */       log.debug("Submitting reaped meters not later than [{}] to store", latestReapableTime);
/* 138 */       this.metersStore.submitMeters(reapedCounts);
/*     */     }
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   void stop() {
/* 144 */     this.stopRequested = true;
/* 145 */     ConcurrencyHelper.stop(this.executorService, 10, log);
/*     */     
/* 147 */     ScheduledFuture<?> sf = this.taskRef;
/* 148 */     if (sf != null) {
/* 149 */       sf.cancel(true);
/*     */     }
/*     */     
/* 152 */     this.meters.clear();
/* 153 */     this.previousReapedMeters.clear();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/MetersManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */