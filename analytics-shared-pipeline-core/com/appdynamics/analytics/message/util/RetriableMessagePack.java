/*     */ package com.appdynamics.analytics.message.util;
/*     */ 
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.appdynamics.common.util.execution.Retriable;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
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
/*     */ public class RetriableMessagePack<SRC_ID, MSG>
/*     */   implements MessagePack<SRC_ID, MSG>, Retriable
/*     */ {
/*  20 */   private static final Logger log = LoggerFactory.getLogger(RetriableMessagePack.class);
/*     */   private final SRC_ID srcId;
/*     */   private final Deque<MSG> messages;
/*     */   private final Deque<ReturnedMessage<MSG>> undeliveredErrorMessages;
/*     */   private final Deque<MSG> undeliveredMessages;
/*     */   private int deliveryAttempt;
/*     */   
/*     */   public RetriableMessagePack(SRC_ID srcId)
/*     */   {
/*  29 */     this(srcId, new ArrayDeque(), new ArrayDeque());
/*     */   }
/*     */   
/*     */   public RetriableMessagePack(SRC_ID srcId, Deque<MSG> messages) {
/*  33 */     this(srcId, messages, new ArrayDeque());
/*     */   }
/*     */   
/*     */   public RetriableMessagePack(SRC_ID srcId, Deque<MSG> messages, Deque<ReturnedMessage<MSG>> undeliveredErrorMessages)
/*     */   {
/*  38 */     this.srcId = srcId;
/*  39 */     this.messages = messages;
/*  40 */     this.undeliveredErrorMessages = undeliveredErrorMessages;
/*  41 */     this.undeliveredMessages = new ArrayDeque();
/*     */   }
/*     */   
/*     */   public final SRC_ID getSourceId()
/*     */   {
/*  46 */     return (SRC_ID)this.srcId;
/*     */   }
/*     */   
/*     */   public final int size()
/*     */   {
/*  51 */     return this.messages.size();
/*     */   }
/*     */   
/*     */   public final MSG peek()
/*     */   {
/*  56 */     return (MSG)this.messages.peek();
/*     */   }
/*     */   
/*     */   public final MSG poll()
/*     */   {
/*  61 */     return (MSG)this.messages.poll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void returnUndelivered(MSG undeliverableMsg)
/*     */   {
/*  71 */     if (log.isTraceEnabled()) {
/*  72 */       log.trace("Message in the pack from source [{}] has been returned as undelivered [{}]", this.srcId, undeliverableMsg);
/*     */     }
/*     */     
/*     */ 
/*  76 */     boolean ok = this.undeliveredMessages.offer(undeliverableMsg);
/*  77 */     if (!ok) {
/*  78 */       log.warn("Queue appears to be full. Unable to store undelivered message [{}]", undeliverableMsg);
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
/*     */   public final void returnUndelivered(MSG undeliverableMsg, Throwable cause)
/*     */   {
/*  94 */     if (log.isTraceEnabled()) {
/*  95 */       log.trace("Message in the pack from source [" + this.srcId + "] has been returned as undeliverable" + " due to error [" + undeliverableMsg + "]", cause);
/*     */     }
/*     */     
/*     */ 
/*  99 */     boolean ok = this.undeliveredErrorMessages.offer(new ReturnedMessage(undeliverableMsg, cause));
/* 100 */     if (!ok) {
/* 101 */       log.warn("Queue appears to be full. Unable to store undelivered message [{}]", undeliverableMsg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void resetCounts()
/*     */   {
/* 111 */     this.deliveryAttempt = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean offer(MSG msg)
/*     */   {
/* 119 */     return this.messages.offer(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 126 */     this.messages.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Deque<MSG> getMessages()
/*     */   {
/* 134 */     return this.messages;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Deque<ReturnedMessage<MSG>> getUndeliveredErrorMessages()
/*     */   {
/* 142 */     return this.undeliveredErrorMessages;
/*     */   }
/*     */   
/*     */   public final int getAttempt()
/*     */   {
/* 147 */     return this.deliveryAttempt;
/*     */   }
/*     */   
/*     */   public final int incAttempt()
/*     */   {
/* 152 */     return ++this.deliveryAttempt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void recirculateUndelivered()
/*     */   {
/* 159 */     for (MSG msg = null; (msg = this.undeliveredMessages.poll()) != null;) {
/* 160 */       boolean doIt = beforeRecirculate(msg);
/* 161 */       if (doIt)
/*     */       {
/*     */ 
/* 164 */         if (log.isTraceEnabled()) {
/* 165 */           log.trace("Recirculating message [{}] in pack from source [{}]", msg, this.srcId);
/*     */         }
/* 167 */         this.messages.offer(msg);
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
/*     */   protected boolean beforeRecirculate(MSG msg)
/*     */   {
/* 180 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean done()
/*     */   {
/* 190 */     recirculateUndelivered();
/* 191 */     return onAttemptEnd();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean onAttemptEnd()
/*     */   {
/* 198 */     return size() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void done(Throwable cause)
/*     */   {
/* 209 */     recirculateUndelivered();
/* 210 */     onAttemptEnd(cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onAttemptEnd(Throwable cause)
/*     */   {
/* 218 */     if (log.isTraceEnabled()) {
/* 219 */       log.trace("Delivery attempt of message pack from source [" + this.srcId + "] ended in error", cause);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ReturnedMessage<MSG> {
/*     */     private final MSG message;
/*     */     private final Throwable cause;
/*     */     
/*     */     public ReturnedMessage(MSG message, Throwable cause) {
/* 228 */       this.message = message;
/* 229 */       this.cause = cause;
/*     */     }
/*     */     
/*     */     public MSG getMessage() {
/* 233 */       return (MSG)this.message;
/*     */     }
/*     */     
/*     */     public Throwable getCause() {
/* 237 */       return this.cause;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/message/util/RetriableMessagePack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */