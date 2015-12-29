/*    */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*    */ 
/*    */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.annotation.PostConstruct;
/*    */ import javax.annotation.PreDestroy;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public abstract class ElasticSearchToolScheduler
/*    */ {
/* 30 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchToolScheduler.class);
/*    */   
/*    */   final ScheduledExecutorService scheduler;
/*    */   final int samplePeriodInSeconds;
/*    */   
/*    */   public ElasticSearchToolScheduler(int samplePeriodInSeconds)
/*    */   {
/* 37 */     this.scheduler = Executors.newSingleThreadScheduledExecutor(ConcurrencyHelper.newDaemonThreadFactory("elasticsearch-health-tool-scheduler-%d"));
/*    */     
/*    */ 
/* 40 */     this.samplePeriodInSeconds = samplePeriodInSeconds;
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   public void start() {
/* 45 */     scheduleTasks();
/*    */   }
/*    */   
/*    */   @PreDestroy
/*    */   public void stop() {
/* 50 */     ConcurrencyHelper.stop(this.scheduler, 1, log);
/*    */   }
/*    */   
/*    */ 
/*    */   abstract void process();
/*    */   
/*    */ 
/*    */   protected void scheduleTasks()
/*    */   {
/* 59 */     this.scheduler.scheduleAtFixedRate(new Runnable()
/*    */     {
/*    */       public void run() {
/*    */         try {
/* 63 */           ElasticSearchToolScheduler.this.process();
/*    */         } catch (Exception e) {
/* 65 */           ElasticSearchToolScheduler.log.error("Error running scheduled elastic search tool", e); } } }, 0L, this.samplePeriodInSeconds, TimeUnit.SECONDS);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ElasticSearchToolScheduler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */