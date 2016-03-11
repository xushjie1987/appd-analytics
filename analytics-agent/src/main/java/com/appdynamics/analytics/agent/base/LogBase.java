/*    */ package com.appdynamics.analytics.agent.base;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.source.LogComponentFactory;
/*    */ import com.appdynamics.common.util.health.HealthCheckable;
/*    */ import com.appdynamics.common.util.item.SimpleItem;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleAware;
/*    */ import com.appdynamics.common.util.lifecycle.RunningState;
/*    */ import java.util.concurrent.ExecutorService;
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
/*    */ public abstract class LogBase
/*    */   extends SimpleItem<Object>
/*    */   implements LifecycleAware, HealthCheckable
/*    */ {
/* 25 */   private static final Logger log = LoggerFactory.getLogger(LogBase.class);
/*    */   
/* 27 */   protected ExecutorService getExecutorService() { return this.executorService; }
/*    */   
/*    */   protected final ExecutorService executorService;
/* 30 */   protected LogComponentFactory getFactory() { return this.factory; }
/*    */   
/*    */ 
/* 33 */   protected RunningState getRunningState() { return this.runningState; }
/*    */   
/*    */   public LogBase(String name, LogComponentFactory factory, ExecutorService executorService)
/*    */   {
/* 37 */     super.setId(name);
/* 38 */     this.factory = factory;
/* 39 */     this.executorService = executorService;
/* 40 */     this.runningState = factory.createRunningState(false);
/*    */   }
/*    */   
/*    */   public boolean isRunning() {
/* 44 */     return this.runningState.get();
/*    */   }
/*    */   
/*    */   public String getName() {
/* 48 */     return (String)super.getId();
/*    */   }
/*    */   
/*    */   protected final LogComponentFactory factory;
/*    */   protected final RunningState runningState;
/*    */   public synchronized void start() {
/* 54 */     log.info("Starting [{}]", getName());
/* 55 */     this.runningState.set(true);
/*    */   }
/*    */   
/*    */ 
/*    */   public synchronized void stop()
/*    */   {
/* 61 */     log.info("Stopping [{}]", getName());
/* 62 */     this.runningState.set(false);
/*    */   }
/*    */   
/*    */   public void sleepWhileRunning(long milliseconds) {
/*    */     try {
/* 67 */       this.runningState.sleepWhileRunning(milliseconds);
/*    */     } catch (InterruptedException e) {
/* 69 */       log.debug("Thread interrupted while sleeping.", e);
/* 70 */       Thread.currentThread().interrupt();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/base/LogBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */