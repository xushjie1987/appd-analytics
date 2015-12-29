/*    */ package com.appdynamics.analytics.processor.admin;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ClusterRouter;
/*    */ import com.appdynamics.analytics.processor.event.configuration.NewClusterEvent;
/*    */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*    */ import com.appdynamics.common.util.event.EventBuses;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class ClusterChangeNotifier
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(ClusterChangeNotifier.class);
/*    */   final ClusterRouter clusterRouter;
/*    */   final EventBuses eventBuses;
/*    */   final long pollIntervalMillis;
/*    */   final ScheduledExecutorService backgroundTpe;
/*    */   
/*    */   ClusterChangeNotifier(ClusterRouter clusterRouter, EventBuses eventBuses, long pollIntervalMillis)
/*    */   {
/* 35 */     this.clusterRouter = clusterRouter;
/* 36 */     this.eventBuses = eventBuses;
/* 37 */     this.pollIntervalMillis = pollIntervalMillis;
/* 38 */     this.backgroundTpe = Executors.newScheduledThreadPool(1, ConcurrencyHelper.newDaemonThreadFactory("cluster-change-notifier-%d"));
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   void onStart() {
/* 43 */     log.debug("Started");
/* 44 */     this.backgroundTpe.scheduleWithFixedDelay(new Runnable()
/*    */     {
/*    */ 
/*    */       public void run() {
/* 48 */         ClusterChangeNotifier.this.onWakeUp(); } }, 0L, this.pollIntervalMillis, TimeUnit.MILLISECONDS);
/*    */   }
/*    */   
/*    */   void onWakeUp()
/*    */   {
/*    */     try
/*    */     {
/* 55 */       Set<String> clusterNames = new HashSet();
/* 56 */       clusterNames.addAll(this.clusterRouter.getAllDynamicClusters());
/* 57 */       log.debug("Notifier has identified the following clusters {}", clusterNames);
/*    */       
/* 59 */       for (String clusterName : clusterNames) {
/* 60 */         this.eventBuses.postEvent("default-event-bus", new NewClusterEvent(clusterName));
/*    */       }
/* 62 */       log.debug("Completed posting [{}] cluster change notifications", Integer.valueOf(clusterNames.size()));
/* 63 */       clusterNames.clear();
/*    */     } catch (Throwable t) {
/* 65 */       log.error("Error occurred while processing cluster changes", t);
/*    */     }
/*    */   }
/*    */   
/*    */   @PreDestroy
/*    */   void onStop() {
/* 71 */     ConcurrencyHelper.stop(this.backgroundTpe, 1, log);
/* 72 */     log.debug("Stopped");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/ClusterChangeNotifier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */