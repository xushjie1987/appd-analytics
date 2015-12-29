/*    */ package com.appdynamics.common.util.exception;
/*    */ 
/*    */ import com.google.common.base.Throwables;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ExceptionHandler
/*    */ {
/*    */   final Logger log;
/*    */   final String interruptedLogMessage;
/*    */   final String transientLogMessage;
/*    */   final String permanentLogMessage;
/*    */   final String unknownLogMessage;
/*    */   final boolean rethrowUnknown;
/*    */   
/* 33 */   public Logger getLog() { return this.log; }
/* 34 */   public String getInterruptedLogMessage() { return this.interruptedLogMessage; }
/* 35 */   public String getTransientLogMessage() { return this.transientLogMessage; }
/* 36 */   public String getPermanentLogMessage() { return this.permanentLogMessage; }
/* 37 */   public String getUnknownLogMessage() { return this.unknownLogMessage; }
/* 38 */   public boolean isRethrowUnknown() { return this.rethrowUnknown; }
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
/*    */   public ExceptionHandler(Logger log, String interruptedLogMessage, String transientLogMessage, String permanentLogMessage, String unknownLogMessage, boolean rethrowUnknown)
/*    */   {
/* 53 */     this.log = log;
/* 54 */     this.interruptedLogMessage = interruptedLogMessage;
/* 55 */     this.transientLogMessage = transientLogMessage;
/* 56 */     this.permanentLogMessage = permanentLogMessage;
/* 57 */     this.unknownLogMessage = ((rethrowUnknown ? "" : "(Suppressed) ") + unknownLogMessage);
/* 58 */     this.rethrowUnknown = rethrowUnknown;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ExceptionHandler(Logger log)
/*    */   {
/* 67 */     this(log, "Thread was interrupted", "Transient error", "Permanent error", "Unexpected error", true);
/*    */   }
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
/*    */   public void handle(Throwable throwable)
/*    */     throws InterruptedException
/*    */   {
/* 86 */     if ((throwable instanceof InterruptedException)) {
/* 87 */       this.log.debug(this.interruptedLogMessage, throwable);
/* 88 */       Thread.currentThread().interrupt();
/* 89 */       throw ((InterruptedException)throwable); }
/* 90 */     if ((throwable instanceof TransientException)) {
/* 91 */       this.log.warn(this.transientLogMessage, throwable);
/* 92 */     } else { if (((throwable instanceof PermanentException)) || ((throwable instanceof Error))) {
/* 93 */         this.log.error(this.permanentLogMessage, throwable);
/* 94 */         throw Throwables.propagate(throwable);
/*    */       }
/* 96 */       this.log.error(this.unknownLogMessage, throwable);
/* 97 */       if (this.rethrowUnknown) {
/* 98 */         throw Throwables.propagate(throwable);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/exception/ExceptionHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */