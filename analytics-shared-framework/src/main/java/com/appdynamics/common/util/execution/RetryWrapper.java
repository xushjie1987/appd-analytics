/*     */ package com.appdynamics.common.util.execution;
/*     */ 
/*     */ import com.appdynamics.common.util.exception.Exceptions;
/*     */ import com.appdynamics.common.util.exception.PermanentException;
/*     */ import com.appdynamics.common.util.exception.TransientException;
/*     */ import com.appdynamics.common.util.health.Countable;
/*     */ import com.appdynamics.common.util.item.Item;
/*     */ import com.google.common.base.Throwables;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RetryWrapper<ID>
/*     */   implements Item<ID>
/*     */ {
/*  36 */   private static final Logger log = LoggerFactory.getLogger(RetryWrapper.class);
/*     */   
/*     */   static final String BEFORE_RESEND_MSG = "A retry attempt has been requested and it will be made after pausing for [%d] milliseconds";
/*     */   
/*     */   static final String NO_MORE_RESEND_MSG = "Further resend attempts will not be made after [%d] failed attempts";
/*     */   
/*     */   static final String TRANSIENT_ERR_MSG = "Error occurred while attempting to process data on [%s]. A retry attempt has been requested and it will be made after pausing for [%d] milliseconds";
/*     */   static final String PERMANENT_ERR_MSG_PART = "error occurred while attempting to send data on [%s]. Further resend attempts will not be made after [%d] failed attempts";
/*     */   final ID id;
/*     */   final RetryConfiguration retryConfiguration;
/*     */   
/*     */   public RetryWrapper(ID id, RetryConfiguration retryConfiguration)
/*     */   {
/*  49 */     this.id = id;
/*  50 */     this.retryConfiguration = retryConfiguration;
/*     */   }
/*     */   
/*     */   public ID getId()
/*     */   {
/*  55 */     return (ID)this.id;
/*     */   }
/*     */   
/*     */   public RetryConfiguration getRetryConfiguration() {
/*  59 */     return this.retryConfiguration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <INPT extends Retriable> void invoke(INPT input, Operation<? super INPT> operation)
/*     */   {
/*  70 */     int maxAttempts = this.retryConfiguration.totalAttempts;
/*     */     
/*  72 */     int attempt = 1;
/*  73 */     TransientException reattemptException = null;
/*  74 */     String reattemptWarningMessage = null;
/*     */     do
/*     */     {
/*  77 */       if (attempt > 1) {
/*  78 */         log.warn(reattemptWarningMessage);
/*  79 */         pause();
/*  80 */         log.warn("Retry attempt [{}] by [{}]", Integer.valueOf(attempt), this.id);
/*     */       }
/*  82 */       input.incAttempt();
/*     */       try {
/*  84 */         if ((operation instanceof Countable)) {
/*  85 */           ((Countable)operation).incrementCount();
/*     */         }
/*  87 */         operation.process(input);
/*     */         
/*     */ 
/*     */ 
/*  91 */         boolean retry = input.done();
/*  92 */         if (!retry) {
/*  93 */           return;
/*     */         }
/*  95 */         reattemptWarningMessage = String.format("A retry attempt has been requested and it will be made after pausing for [%d] milliseconds", new Object[] { Long.valueOf(this.retryConfiguration.retryPauseMillis) });
/*     */       }
/*     */       catch (TransientException te)
/*     */       {
/*  99 */         reattemptException = te;
/* 100 */         reattemptWarningMessage = String.format("Error occurred while attempting to process data on [%s]. A retry attempt has been requested and it will be made after pausing for [%d] milliseconds", new Object[] { this.id, Long.valueOf(this.retryConfiguration.retryPauseMillis) }) + "\n" + Throwables.getStackTraceAsString(te);
/*     */       }
/*     */       catch (PermanentException pe) {
/* 103 */         log.error(String.format("Unrecoverable error occurred while attempting to send data on [%s]. Further resend attempts will not be made after [%d] failed attempts", new Object[] { this.id, Integer.valueOf(attempt) }), pe);
/* 104 */         input.done(pe);
/*     */         
/* 106 */         throw pe;
/*     */       } catch (Throwable t) {
/* 108 */         log.error(String.format("Unrecoverable and unexpected error occurred while attempting to send data on [%s]. Further resend attempts will not be made after [%d] failed attempts", new Object[] { this.id, Integer.valueOf(attempt) }), t);
/*     */         
/* 110 */         input.done(t);
/* 111 */         throw t;
/*     */       }
/* 113 */       attempt++;
/* 114 */     } while (attempt <= maxAttempts);
/*     */     
/*     */ 
/* 117 */     String msg = String.format("Further resend attempts will not be made after [%d] failed attempts", new Object[] { Integer.valueOf(Math.min(attempt, maxAttempts)) });
/*     */     
/*     */ 
/*     */ 
/* 121 */     PermanentException pe = reattemptException == null ? new PermanentException(msg) : new PermanentException(msg, reattemptException);
/*     */     
/* 123 */     input.done(pe);
/* 124 */     log.error(msg, pe);
/*     */   }
/*     */   
/*     */   private void pause() {
/*     */     try {
/* 129 */       Thread.sleep(this.retryConfiguration.retryPauseMillis);
/*     */     } catch (InterruptedException ie) {
/* 131 */       Exceptions.rethrowAsRuntimeException("Pause before retry was interrupted", ie);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/execution/RetryWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */