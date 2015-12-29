/*    */ package com.appdynamics.analytics.agent.source;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.base.LogBase;
/*    */ import java.util.Set;
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
/*    */ public abstract class LogSource
/*    */   extends LogBase
/*    */ {
/* 20 */   private static final Logger log = LoggerFactory.getLogger(LogSource.class);
/*    */   final LogSourceConfiguration configuration;
/*    */   
/* 23 */   public LogSourceConfiguration getConfiguration() { return this.configuration; }
/*    */   
/*    */ 
/*    */   public LogSource(String name, LogSourceConfiguration configuration, LogComponentFactory factory, ExecutorService executorService)
/*    */   {
/* 28 */     super(name, factory, executorService);
/* 29 */     this.configuration = configuration;
/*    */   }
/*    */   
/*    */   public Object getId()
/*    */   {
/* 34 */     return this.configuration.getId();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 41 */     return this.configuration.getName();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getSourceType()
/*    */   {
/* 48 */     return this.configuration.getSourceType();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Set<String> getExtractedFieldPatterns()
/*    */   {
/* 55 */     return this.configuration.getExtractedFieldPatterns();
/*    */   }
/*    */   
/*    */   public abstract LogWatermarkState getWatermarkState();
/*    */   
/*    */   public abstract void setWatermarkState(LogWatermarkState paramLogWatermarkState);
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/LogSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */