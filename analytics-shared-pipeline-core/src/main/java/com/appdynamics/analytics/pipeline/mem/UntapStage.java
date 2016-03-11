/*     */ package com.appdynamics.analytics.pipeline.mem;
/*     */ 
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*     */ import com.appdynamics.common.util.exception.Exceptions;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public class UntapStage<I>
/*     */   extends AbstractPipelineStage<Void, I>
/*     */ {
/*  28 */   private static final Logger log = LoggerFactory.getLogger(UntapStage.class);
/*     */   private final BlockingQueue<I> queue;
/*     */   private final CountDownLatch stopWaitLatch;
/*     */   private final AtomicReference<Thread> processingThread;
/*     */   private volatile boolean stop;
/*     */   
/*     */   public UntapStage(PipelineStageParameters<I> parameters, BlockingQueue<I> queue)
/*     */   {
/*  36 */     super(parameters);
/*  37 */     this.queue = queue;
/*  38 */     this.stopWaitLatch = new CountDownLatch(1);
/*  39 */     this.processingThread = new AtomicReference();
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
/*     */   public final void process(Void input)
/*     */   {
/*  53 */     if (!this.processingThread.compareAndSet(null, Thread.currentThread())) {
/*  54 */       String msg = "Pipeline stage is already being processed by another thread";
/*  55 */       Thread otherThread = (Thread)this.processingThread.get();
/*  56 */       if (otherThread != null) {
/*  57 */         msg = msg + " [" + otherThread.getName() + "]";
/*     */       }
/*  59 */       throw new ConcurrentModificationException(msg);
/*     */     }
/*     */     try
/*     */     {
/*  63 */       while (!this.stop) {
/*  64 */         I i = this.queue.take();
/*     */         try {
/*  66 */           invokeNext(i);
/*     */         } catch (Exception e) {
/*  68 */           onError(i, e);
/*     */         } catch (Throwable t) {
/*  70 */           onUnrecoverableError(i, t);
/*  71 */           throw t;
/*     */         }
/*     */       }
/*     */     } catch (InterruptedException e) {
/*  75 */       Exceptions.rethrowAsRuntimeException(e);
/*     */     } catch (Exception e) {
/*  77 */       Throwables.propagate(e);
/*     */     } finally {
/*     */       try {
/*  80 */         this.stopWaitLatch.countDown();
/*     */       } finally {
/*  82 */         this.processingThread.set(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onError(I i, Exception e)
/*     */     throws Exception
/*     */   {
/*  95 */     log.error("Error occurred while processing input on pipeline [{}]", printableName(), e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onUnrecoverableError(I i, Throwable t)
/*     */   {
/* 105 */     log.error("Unrecoverable error occurred while processing input on pipeline [{}]", printableName(), t);
/*     */   }
/*     */   
/*     */   public final void stop()
/*     */   {
/* 110 */     this.stop = true;
/* 111 */     Thread otherThread = (Thread)this.processingThread.get();
/* 112 */     if (otherThread != null) {
/* 113 */       otherThread.interrupt();
/*     */     }
/*     */     try {
/* 116 */       this.stopWaitLatch.await();
/*     */     } catch (InterruptedException e) {
/* 118 */       Exceptions.rethrowAsRuntimeException(e);
/*     */     }
/* 120 */     super.stop();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/mem/UntapStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */