/*    */ package com.appdynamics.common.framework.util;
/*    */ 
/*    */ import org.eclipse.jetty.util.component.AbstractLifeCycle.AbstractLifeCycleListener;
/*    */ import org.eclipse.jetty.util.component.LifeCycle;
/*    */ import org.slf4j.Logger;
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
/*    */ class AppLifecycleLogger
/*    */   extends AbstractLifeCycle.AbstractLifeCycleListener
/*    */ {
/*    */   final Logger logger;
/*    */   final String name;
/*    */   
/*    */   AppLifecycleLogger(Logger logger, String name)
/*    */   {
/* 26 */     this.logger = logger;
/* 27 */     this.name = name;
/* 28 */     logger.info("Starting [{}]", name);
/*    */   }
/*    */   
/*    */   public void lifeCycleStarted(LifeCycle event)
/*    */   {
/* 33 */     this.logger.info("Started [{}]", this.name);
/*    */   }
/*    */   
/*    */   public void lifeCycleStopping(LifeCycle event)
/*    */   {
/* 38 */     this.logger.info("Stopping [{}]", this.name);
/*    */   }
/*    */   
/*    */   public void lifeCycleStopped(LifeCycle event)
/*    */   {
/* 43 */     this.logger.info("Stopped [{}]", this.name);
/*    */   }
/*    */   
/*    */   public void lifeCycleFailure(LifeCycle event, Throwable cause)
/*    */   {
/* 48 */     this.logger.error("[" + this.name + "] failed", cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/AppLifecycleLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */