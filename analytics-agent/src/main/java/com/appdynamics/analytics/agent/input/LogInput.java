/*    */ package com.appdynamics.analytics.agent.input;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.base.LogBase;
/*    */ import com.appdynamics.analytics.agent.source.LogComponentFactory;
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
/*    */ public abstract class LogInput
/*    */   extends LogBase
/*    */ {
/* 20 */   private static final Logger log = LoggerFactory.getLogger(LogInput.class);
/*    */   
/*    */   public LogInput(String name, LogComponentFactory factory, ExecutorService executorService)
/*    */   {
/* 24 */     super(name, factory, executorService);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/input/LogInput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */