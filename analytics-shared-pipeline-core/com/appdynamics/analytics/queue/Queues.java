/*     */ package com.appdynamics.analytics.queue;
/*     */ 
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.SynchronousQueue;
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
/*     */ 
/*     */ 
/*     */ public class Queues
/*     */ {
/*  37 */   private static final Logger log = LoggerFactory.getLogger(Queues.class);
/*     */   private final ConcurrentMap<Object, BlockingQueue<?>> queues;
/*     */   
/*     */   Queues()
/*     */   {
/*  42 */     this.queues = new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */   ConcurrentMap<Object, BlockingQueue<?>> getQueues() {
/*  46 */     return this.queues;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <I> BlockingQueue<I> findOrSetupQueue(QueueConfiguration qConf)
/*     */   {
/*  55 */     BlockingQueue<Object> queue = null;
/*  56 */     int qSize = qConf.getQueueSize();
/*  57 */     switch (qSize) {
/*     */     case 0: 
/*  59 */       queue = new SynchronousQueue();
/*  60 */       break;
/*     */     case 2147483647: 
/*  62 */       queue = new LinkedBlockingQueue();
/*  63 */       break;
/*     */     default: 
/*  65 */       queue = new ArrayBlockingQueue(qConf.getQueueSize());
/*     */     }
/*     */     
/*     */     
/*  69 */     BlockingQueue<?> oldQueue = (BlockingQueue)this.queues.putIfAbsent(qConf.getQueueName(), queue);
/*     */     
/*  71 */     if (oldQueue != null) {
/*  72 */       log.debug("Queue [" + qConf.getQueueName() + "] already exists and so this configuration will be ignored");
/*     */       
/*  74 */       return oldQueue;
/*     */     }
/*     */     
/*  77 */     log.debug("Queue [" + qConf.getQueueName() + "] created with max size [{}]", Integer.valueOf(qConf.getQueueSize()));
/*     */     
/*  79 */     return queue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <I> BlockingQueue<I> getQueue(Object key)
/*     */   {
/*  90 */     BlockingQueue bq = (BlockingQueue)this.queues.get(key);
/*     */     
/*  92 */     return bq;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <I> BlockingQueue<I> removeQueue(Object key)
/*     */   {
/* 103 */     BlockingQueue bq = (BlockingQueue)this.queues.remove(key);
/*     */     
/* 105 */     return bq;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/queue/Queues.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */