/*     */ package com.appdynamics.analytics.queue;
/*     */ 
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.appdynamics.analytics.message.api.MessageSource;
/*     */ import com.appdynamics.analytics.message.util.RetriableMessagePack;
/*     */ import com.appdynamics.analytics.message.util.RetriableMessagePack.ReturnedMessage;
/*     */ import com.appdynamics.common.util.execution.Retriable;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class QueueSource<ID, MSG>
/*     */   implements MessageSource<ID, MSG>
/*     */ {
/*  29 */   private static final Logger log = LoggerFactory.getLogger(QueueSource.class);
/*     */   
/*     */   final ID id;
/*     */   
/*     */   final BlockingQueue<MSG> queue;
/*     */   final ThreadLocal<OurRetriableMessagePack<ID, MSG>> tlsMessagePacks;
/*     */   
/*     */   public QueueSource(final ID id, BlockingQueue<MSG> queue)
/*     */   {
/*  38 */     Preconditions.checkArgument(Retriable.class.isAssignableFrom(OurRetriableMessagePack.class), "[" + OurRetriableMessagePack.class.getName() + "] is not [" + Retriable.class.getName() + "]");
/*     */     
/*  40 */     this.id = id;
/*  41 */     this.queue = queue;
/*  42 */     this.tlsMessagePacks = new ThreadLocal()
/*     */     {
/*     */       protected QueueSource.OurRetriableMessagePack<ID, MSG> initialValue() {
/*  45 */         return new QueueSource.OurRetriableMessagePack(id, new ArrayDeque());
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public BlockingQueue<MSG> getQueue() {
/*  51 */     return this.queue;
/*     */   }
/*     */   
/*     */   public ID getId()
/*     */   {
/*  56 */     return (ID)this.id;
/*     */   }
/*     */   
/*     */   public boolean supportsRetriable()
/*     */   {
/*  61 */     return true;
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
/*     */   public MessagePack<ID, MSG> poll(int maxMessages, long callTimeOut, TimeUnit callTimeOutUnit)
/*     */     throws InterruptedException
/*     */   {
/*  76 */     OurRetriableMessagePack<ID, MSG> messagePack = (OurRetriableMessagePack)this.tlsMessagePacks.get();
/*     */     
/*     */ 
/*  79 */     Deque<RetriableMessagePack.ReturnedMessage<MSG>> returnedMessages = messagePack.getUndeliveredErrorMessages();
/*  80 */     RetriableMessagePack.ReturnedMessage<MSG> rm; while ((rm = (RetriableMessagePack.ReturnedMessage)returnedMessages.poll()) != null) {
/*  81 */       log.error("Dropping message [{}] that encountered permanent error: [{}]", rm.getMessage(), rm.getCause());
/*     */     }
/*     */     
/*     */ 
/*     */     MSG message;
/*     */     
/*  87 */     while ((message = messagePack.poll()) != null) {
/*  88 */       log.error("Dropping message [{}] due to the fact that the number of transient errors it encountered exceeded the maximum number of configured retries", message);
/*     */     }
/*     */     
/*  91 */     messagePack.resetCounts();
/*     */     
/*     */ 
/*  94 */     Deque<MSG> messages = messagePack.anotherRefToMessages;
/*     */     
/*     */ 
/*  97 */     while (maxMessages > 0) {
/*  98 */       MSG message = this.queue.poll(callTimeOut, callTimeOutUnit);
/*  99 */       if (message == null) {
/*     */         break;
/*     */       }
/* 102 */       boolean ok = messages.offer(message);
/* 103 */       if (ok) {
/* 104 */         maxMessages--;
/*     */       }
/* 106 */       if (maxMessages > 0) {
/* 107 */         maxMessages -= this.queue.drainTo(messages, maxMessages);
/*     */       }
/*     */     }
/*     */     
/* 111 */     return messages.isEmpty() ? null : messagePack;
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {}
/*     */   
/*     */   private static class OurRetriableMessagePack<ID, MSG> extends RetriableMessagePack<ID, MSG>
/*     */   {
/*     */     final Deque<MSG> anotherRefToMessages;
/*     */     
/*     */     OurRetriableMessagePack(ID id, Deque<MSG> messages)
/*     */     {
/* 123 */       super(messages);
/* 124 */       this.anotherRefToMessages = messages;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/queue/QueueSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */