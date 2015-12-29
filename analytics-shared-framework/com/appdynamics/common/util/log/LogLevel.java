/*    */ package com.appdynamics.common.util.log;
/*    */ 
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
/*    */ public enum LogLevel
/*    */ {
/* 16 */   TRACE, 
/* 17 */   DEBUG, 
/* 18 */   INFO, 
/* 19 */   WARN, 
/* 20 */   ERROR;
/*    */   
/*    */   private LogLevel() {}
/* 23 */   public void write(Logger logger, String msg, String... args) { switch (this) {
/*    */     case TRACE: 
/* 25 */       logger.trace(msg, (Object[])args);
/* 26 */       break;
/*    */     case DEBUG: 
/* 28 */       logger.debug(msg, (Object[])args);
/* 29 */       break;
/*    */     case INFO: 
/* 31 */       logger.info(msg, (Object[])args);
/* 32 */       break;
/*    */     case WARN: 
/* 34 */       logger.warn(msg, (Object[])args);
/* 35 */       break;
/*    */     case ERROR: 
/* 37 */       logger.error(msg, (Object[])args);
/* 38 */       break;
/*    */     default: 
/* 40 */       throw new IllegalArgumentException("Unknown log level [" + this + "]");
/*    */     }
/*    */   }
/*    */   
/*    */   public void write(Logger logger, String msg) {
/* 45 */     switch (this) {
/*    */     case TRACE: 
/* 47 */       logger.trace(msg);
/* 48 */       break;
/*    */     case DEBUG: 
/* 50 */       logger.debug(msg);
/* 51 */       break;
/*    */     case INFO: 
/* 53 */       logger.info(msg);
/* 54 */       break;
/*    */     case WARN: 
/* 56 */       logger.warn(msg);
/* 57 */       break;
/*    */     case ERROR: 
/* 59 */       logger.error(msg);
/* 60 */       break;
/*    */     default: 
/* 62 */       throw new IllegalArgumentException("Unknown log level [" + this + "]");
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isEnabled(Logger logger) {
/* 67 */     switch (this) {
/*    */     case TRACE: 
/* 69 */       return logger.isTraceEnabled();
/*    */     case DEBUG: 
/* 71 */       return logger.isDebugEnabled();
/*    */     case INFO: 
/* 73 */       return logger.isInfoEnabled();
/*    */     case WARN: 
/* 75 */       return logger.isWarnEnabled();
/*    */     case ERROR: 
/* 77 */       return logger.isErrorEnabled();
/*    */     }
/* 79 */     throw new IllegalArgumentException("Unknown log level [" + this + "]");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/log/LogLevel.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */