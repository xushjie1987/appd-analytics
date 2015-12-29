/*     */ package com.appdynamics.analytics.message.util;
/*     */ 
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public class MpscMessagePacks<SRC_ID, MSG>
/*     */ {
/*  81 */   private static final Logger log = LoggerFactory.getLogger(MpscMessagePacks.class);
/*     */   
/*     */   private final int maximumExpectedProducers;
/*     */   
/*     */   private final ThreadLocal<Container<SRC_ID, MSG>> tlsMsgPacks;
/*     */   
/*     */   private final BlockingQueue<Container<SRC_ID, MSG>> pickUpQueue;
/*     */   
/*     */ 
/*     */   public MpscMessagePacks(int maximumExpectedProducers)
/*     */   {
/*  92 */     this.maximumExpectedProducers = maximumExpectedProducers;
/*  93 */     this.tlsMsgPacks = new ThreadLocal();
/*  94 */     this.pickUpQueue = new ArrayBlockingQueue(maximumExpectedProducers * 2);
/*     */   }
/*     */   
/*     */   BlockingQueue<Container<SRC_ID, MSG>> getPickUpQueue() {
/*  98 */     return this.pickUpQueue;
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
/*     */   public void offer(MessagePack<SRC_ID, MSG> messagePack)
/*     */   {
/* 111 */     Container<SRC_ID, MSG> container = (Container)this.tlsMsgPacks.get();
/* 112 */     if (container == null) {
/* 113 */       container = new Container(messagePack, null);
/* 114 */       this.tlsMsgPacks.set(container);
/* 115 */     } else if (container.messagePack != messagePack) {
/* 116 */       log.warn("It appears that another message pack was used on this thread and it was not retired before this new message pack was offered");
/*     */     }
/*     */     
/* 119 */     offerNonEmptyPack(container);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void retireProducer()
/*     */   {
/* 130 */     internalRetireProducer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   MessagePack<SRC_ID, MSG> internalRetireProducer()
/*     */   {
/* 137 */     Container<SRC_ID, MSG> container = (Container)this.tlsMsgPacks.get();
/* 138 */     this.tlsMsgPacks.remove();
/* 139 */     return container == null ? null : container.messagePack;
/*     */   }
/*     */   
/*     */   private void offerNonEmptyPack(Container<SRC_ID, MSG> containerWithNonEmptyPack) {
/* 143 */     if (containerWithNonEmptyPack.waitingForPickup.compareAndSet(false, true)) {
/* 144 */       for (int i = 0; !this.pickUpQueue.offer(containerWithNonEmptyPack); i++)
/*     */       {
/* 146 */         if (i % 10 == 0) {
/* 147 */           log.warn("Internal queue appears to be full [{}]. There appear to be moreproducers than the initial, expected number of producers [{}]", Integer.valueOf(this.pickUpQueue.size()), Integer.valueOf(this.maximumExpectedProducers));
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessagePack<SRC_ID, MSG> consume()
/*     */     throws InterruptedException
/*     */   {
/* 167 */     Container<SRC_ID, MSG> container = internalPickUp();
/* 168 */     return container.messagePack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   Container<SRC_ID, MSG> internalPickUp()
/*     */     throws InterruptedException
/*     */   {
/*     */     Container<SRC_ID, MSG> container;
/*     */     
/*     */ 
/* 179 */     while (((container = (Container)this.pickUpQueue.poll(1L, TimeUnit.SECONDS)) == null) && (!Thread.currentThread().isInterrupted())) {}
/*     */     
/*     */ 
/* 182 */     if (container != null) {
/* 183 */       container.waitingForPickup.set(false);
/* 184 */       this.tlsMsgPacks.set(container);
/*     */     }
/* 186 */     return container;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterConsume()
/*     */   {
/* 194 */     Container<SRC_ID, MSG> container = (Container)this.tlsMsgPacks.get();
/* 195 */     Preconditions.checkArgument(container != null, "This thread has not consumed a message pack recently");
/* 196 */     this.tlsMsgPacks.remove();
/* 197 */     if (container.messagePack.size() > 0) {
/* 198 */       offerNonEmptyPack(container);
/*     */     }
/*     */   }
/*     */   
/*     */   static class Container<SRC_ID, MSG> {
/*     */     private final MessagePack<SRC_ID, MSG> messagePack;
/* 204 */     private final AtomicBoolean waitingForPickup = new AtomicBoolean();
/*     */     
/*     */     private Container(MessagePack<SRC_ID, MSG> messagePack) {
/* 207 */       this.messagePack = messagePack;
/*     */     }
/*     */     
/*     */     MessagePack<SRC_ID, MSG> getMessagePack() {
/* 211 */       return this.messagePack;
/*     */     }
/*     */     
/*     */     boolean isWaitingForPickup() {
/* 215 */       return this.waitingForPickup.get();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/message/util/MpscMessagePacks.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */