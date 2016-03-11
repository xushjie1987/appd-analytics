/*     */ package com.appdynamics.common.util.concurrent;
/*     */ 
/*     */ import com.appdynamics.common.util.exception.Exceptions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ public abstract class ConcurrencyHelper
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(ConcurrencyHelper.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int DEFAULT_EXECUTOR_STOP_WAIT_SECS = 10;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean stop(ExecutorService executorService, Logger logger)
/*     */   {
/*  40 */     return stop(executorService, 10, logger);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean stop(ExecutorService executorService, int waitBeforeTerminateSecs, Logger logger)
/*     */   {
/*  53 */     int waitMillis = Math.max(1000, 1000 * waitBeforeTerminateSecs);
/*     */     
/*     */ 
/*  56 */     executorService.shutdown();
/*     */     
/*     */ 
/*  59 */     boolean stopped = false;
/*  60 */     while ((waitMillis > 0) && (!stopped)) {
/*  61 */       long startMillis = System.currentTimeMillis();
/*     */       try {
/*  63 */         logger.debug("Waiting for thread pool to stop");
/*  64 */         stopped = executorService.awaitTermination(waitMillis, TimeUnit.MILLISECONDS);
/*     */       } catch (InterruptedException e) {
/*  66 */         logger.debug("Thread was interrupted while it was waiting for thread pool to stop", e);
/*  67 */         Thread.currentThread().interrupt();
/*  68 */         break;
/*     */       }
/*  70 */       waitMillis = (int)(waitMillis - (System.currentTimeMillis() - startMillis));
/*     */     }
/*     */     
/*  73 */     if (!executorService.isTerminated()) {
/*  74 */       logger.warn("Thread pool will be forcibly stopped now if it has not already stopped");
/*  75 */       executorService.shutdownNow();
/*     */       try {
/*  77 */         stopped = executorService.awaitTermination(waitBeforeTerminateSecs, TimeUnit.SECONDS);
/*     */       }
/*     */       catch (InterruptedException e) {}
/*     */       
/*  81 */       if (!executorService.isTerminated()) {
/*  82 */         logger.warn("Could not shutdown thread pool in [{}] seconds", Integer.valueOf(waitBeforeTerminateSecs));
/*     */       }
/*     */     }
/*     */     
/*  86 */     return stopped;
/*     */   }
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
/*     */   public static <T> T getOrCancel(Future<T> future, int waitBeforeCancelSecs, Logger logger)
/*     */   {
/* 100 */     int waitMillis = Math.max(1000, 1000 * waitBeforeCancelSecs);
/*     */     for (;;)
/*     */     {
/* 103 */       long startMillis = System.currentTimeMillis();
/*     */       try {
/* 105 */         if (waitMillis <= 0)
/*     */         {
/* 107 */           return (T)future.get(1L, TimeUnit.NANOSECONDS);
/*     */         }
/* 109 */         logger.debug("Waiting for task to complete");
/* 110 */         return (T)future.get(waitMillis, TimeUnit.MILLISECONDS);
/*     */       }
/*     */       catch (InterruptedException e) {
/* 113 */         Exceptions.rethrowAsRuntimeException(e);
/*     */       } catch (ExecutionException e) {
/* 115 */         Throwables.propagate(e);
/*     */       } catch (TimeoutException e) {
/* 117 */         logger.debug("Thread timed out while it was waiting for task to complete", e);
/*     */       }
/* 119 */       waitMillis = (int)(waitMillis - (System.currentTimeMillis() - startMillis));
/* 120 */       if (waitMillis <= 0) {
/* 121 */         logger.warn("Task will be forcibly stopped now if it has not already stopped");
/* 122 */         future.cancel(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void cancel(Future<?> future, long timeoutMillis, boolean hardCancelIfNecessary, Logger logger)
/*     */   {
/* 136 */     future.cancel(false);
/*     */     try {
/* 138 */       future.get(timeoutMillis, TimeUnit.MILLISECONDS);
/*     */     } catch (InterruptedException e) {
/* 140 */       Exceptions.rethrowAsRuntimeException(e);
/*     */     } catch (ExecutionException e) {
/* 142 */       Throwables.propagate(e);
/*     */     }
/*     */     catch (CancellationException e) {}catch (TimeoutException e)
/*     */     {
/* 146 */       logger.debug("Thread timed out while it was waiting for task to complete", e);
/* 147 */       if (hardCancelIfNecessary) {
/* 148 */         future.cancel(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void sleep(long millis)
/*     */   {
/*     */     try
/*     */     {
/* 160 */       Thread.sleep(millis);
/*     */     } catch (InterruptedException e) {
/* 162 */       Exceptions.rethrowAsRuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ThreadFactory newDaemonThreadFactory(String threadFamilyNameFormat)
/*     */   {
/* 171 */     new ThreadFactoryBuilder().setNameFormat(threadFamilyNameFormat).setDaemon(true).setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
/*     */     {
/*     */ 
/*     */ 
/*     */       public void uncaughtException(Thread t, Throwable e)
/*     */       {
/*     */ 
/* 178 */         ConcurrencyHelper.log.error("Unexpected error occurred on thread [" + t.getName() + "]", e);
/*     */       }
/*     */     }).build();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/concurrent/ConcurrencyHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */