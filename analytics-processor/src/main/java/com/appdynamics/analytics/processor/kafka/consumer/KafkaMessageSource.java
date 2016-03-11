/*     */ package com.appdynamics.analytics.processor.kafka.consumer;
/*     */ 
/*     */ import com.appdynamics.analytics.message.api.ExplicitCommitable;
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.appdynamics.analytics.message.api.MessageSource;
/*     */ import com.appdynamics.analytics.message.util.RetriableMessagePack;
/*     */ import com.appdynamics.analytics.processor.event.dto.GenericMessage;
/*     */ import com.appdynamics.common.util.datetime.Ticker;
/*     */ import com.google.common.base.Joiner;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import javax.annotation.concurrent.NotThreadSafe;
/*     */ import kafka.consumer.ConsumerConfig;
/*     */ import kafka.consumer.ConsumerIterator;
/*     */ import kafka.consumer.ConsumerTimeoutException;
/*     */ import kafka.consumer.KafkaStream;
/*     */ import kafka.consumer.Whitelist;
/*     */ import kafka.javaapi.consumer.ConsumerConnector;
/*     */ import kafka.message.MessageAndMetadata;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class KafkaMessageSource
/*     */   implements MessageSource<String, GenericMessage>
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(KafkaMessageSource.class);
/*     */   
/*     */ 
/*     */   private final String srcId;
/*     */   
/*     */ 
/*     */   private final ConsumerConnector consumer;
/*     */   
/*     */ 
/*     */   private final KafkaMessageSourceHook messageSourceHook;
/*     */   
/*     */ 
/*     */   private final ConsumerIterator<byte[], byte[]> msgIterator;
/*     */   
/*     */   private final Ticker ticker;
/*     */   
/*     */   private final RetriableMessagePack<String, GenericMessage> reusedMessagePack;
/*     */   
/*     */   private final AtomicBoolean shutdownRequested;
/*     */   
/*     */   private final long implicitCommitOffsetIntervalMillis;
/*     */   
/*     */   private final boolean explicitCommitOffset;
/*     */   
/*     */   private long lastCommitOffsetAtMillis;
/*     */   
/*     */   private int numUncommittedMessages;
/*     */   
/*     */ 
/*     */   public KafkaMessageSource(String srcId, ConsumerConfig consumerConfig, ConsumerConnector consumer, Set<String> topics, KafkaMessageSourceHook messageSourceHook, String transientDeadLetterTopic, String permanentDeadLetterTopic, boolean explicitCommitOffset)
/*     */   {
/*  69 */     this.srcId = srcId;
/*  70 */     this.implicitCommitOffsetIntervalMillis = consumerConfig.autoCommitIntervalMs();
/*  71 */     this.consumer = consumer;
/*  72 */     this.messageSourceHook = messageSourceHook;
/*     */     
/*  74 */     StringBuilder sb = new StringBuilder("(");
/*  75 */     Joiner.on('|').appendTo(sb, topics).append(")");
/*  76 */     Whitelist topicWhiteList = new Whitelist(sb.toString());
/*     */     
/*     */ 
/*  79 */     this.msgIterator = ((KafkaStream)consumer.createMessageStreamsByFilter(topicWhiteList, 1).get(0)).iterator();
/*  80 */     this.ticker = new Ticker();
/*  81 */     this.lastCommitOffsetAtMillis = this.ticker.currentTimeMillis();
/*  82 */     this.reusedMessagePack = (explicitCommitOffset ? new ExplicitCommitableKafkaMessagePack(srcId, messageSourceHook, transientDeadLetterTopic, permanentDeadLetterTopic) : new KafkaMessagePack(srcId, messageSourceHook, transientDeadLetterTopic, permanentDeadLetterTopic));
/*     */     
/*     */ 
/*     */ 
/*  86 */     this.shutdownRequested = new AtomicBoolean();
/*  87 */     this.explicitCommitOffset = explicitCommitOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean tryCommitOffsets()
/*     */   {
/*  97 */     long now = this.ticker.currentTimeMillis();
/*  98 */     if (now - this.lastCommitOffsetAtMillis >= this.implicitCommitOffsetIntervalMillis) {
/*  99 */       return forceCommitOffsets(now);
/*     */     }
/*     */     
/* 102 */     log.trace("Skipped committing offsets due to the fact that the elapsed time [{}] ms was less than the offset commit interval [{}] ms", Long.valueOf(now - this.lastCommitOffsetAtMillis), Long.valueOf(this.implicitCommitOffsetIntervalMillis));
/*     */     
/*     */ 
/* 105 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean forceCommitOffsets(long currentTimeInMillis)
/*     */   {
/* 113 */     if (this.numUncommittedMessages > 0) {
/* 114 */       log.debug("Attempting to commit offsets as there are [{}] uncommitted messages", Integer.valueOf(this.numUncommittedMessages));
/* 115 */       this.consumer.commitOffsets();
/* 116 */       this.lastCommitOffsetAtMillis = currentTimeInMillis;
/* 117 */       this.numUncommittedMessages = 0;
/* 118 */       return true;
/*     */     }
/*     */     
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public String getId()
/*     */   {
/* 126 */     return this.srcId;
/*     */   }
/*     */   
/*     */   public boolean supportsRetriable()
/*     */   {
/* 131 */     return true;
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
/*     */   public MessagePack<String, GenericMessage> poll(int maxMessages, long callTimeOut, TimeUnit callTimeOutUnit)
/*     */     throws InterruptedException
/*     */   {
/* 158 */     log.trace("Polling for [{}] messages from kafka message source", Integer.valueOf(maxMessages));
/* 159 */     this.reusedMessagePack.resetCounts();
/* 160 */     int numReads = 0;
/* 161 */     long timeLeft = callTimeOutUnit.toMillis(callTimeOut);
/* 162 */     while ((!this.shutdownRequested.get()) && (timeLeft > 0L) && (numReads < maxMessages)) {
/* 163 */       long start = this.ticker.currentTimeMillis();
/*     */       try {
/* 165 */         log.trace("Checking if kafka stream has messages");
/* 166 */         if (this.msgIterator.hasNext()) {
/* 167 */           log.trace("Reading kafka message [{}] in current batch", Integer.valueOf(numReads));
/* 168 */           MessageAndMetadata<byte[], byte[]> msgAndMetadata = this.msgIterator.next();
/* 169 */           log.trace("Got kafka message");
/* 170 */           GenericMessage message = this.messageSourceHook.convertToMessage(msgAndMetadata);
/* 171 */           this.reusedMessagePack.offer(message);
/* 172 */           numReads++;
/*     */         }
/*     */       } catch (ConsumerTimeoutException e) { long end;
/* 175 */         log.trace("Consumer timed out");
/*     */       } finally {
/*     */         long end;
/* 178 */         if (numReads < maxMessages) {
/* 179 */           long end = this.ticker.currentTimeMillis();
/* 180 */           timeLeft -= end - start;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 185 */     this.numUncommittedMessages += numReads;
/*     */     
/*     */ 
/* 188 */     if ((numReads == 0) && (!this.explicitCommitOffset)) {
/* 189 */       tryCommitOffsets();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 194 */     return this.reusedMessagePack;
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 199 */     this.shutdownRequested.set(true);
/* 200 */     this.consumer.shutdown();
/*     */   }
/*     */   
/*     */   private class KafkaMessagePack
/*     */     extends RetriableMessagePack<String, GenericMessage>
/*     */   {
/*     */     final KafkaMessageSourceHook messageSourceHook;
/*     */     final String transientDeadLetterTopic;
/*     */     final String permanentDeadLetterTopic;
/*     */     
/*     */     KafkaMessagePack(String srcId, KafkaMessageSourceHook messageSourceHook, String transientDeadLetterTopic, String permanentDeadLetterTopic)
/*     */     {
/* 212 */       super(new ArrayDeque());
/* 213 */       this.messageSourceHook = messageSourceHook;
/* 214 */       this.transientDeadLetterTopic = transientDeadLetterTopic;
/* 215 */       this.permanentDeadLetterTopic = permanentDeadLetterTopic;
/*     */     }
/*     */     
/*     */     void completed() {
/* 219 */       KafkaMessageSource.this.tryCommitOffsets();
/* 220 */       getUndeliveredErrorMessages().clear();
/* 221 */       getMessages().clear();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final boolean onAttemptEnd()
/*     */     {
/* 230 */       if (size() > 0) {
/* 231 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 240 */       this.messageSourceHook.handleUndeliveredMessages(null, getUndeliveredErrorMessages(), this.transientDeadLetterTopic, this.permanentDeadLetterTopic);
/*     */       
/*     */ 
/* 243 */       completed();
/* 244 */       return false;
/*     */     }
/*     */     
/*     */     protected final void onAttemptEnd(Throwable cause)
/*     */     {
/* 249 */       KafkaMessageSource.log.warn("Message pack could not be delivered", cause);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 258 */       this.messageSourceHook.handleUndeliveredMessages(getMessages(), getUndeliveredErrorMessages(), this.transientDeadLetterTopic, this.permanentDeadLetterTopic);
/*     */       
/*     */ 
/* 261 */       completed();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class ExplicitCommitableKafkaMessagePack
/*     */     extends KafkaMessageSource.KafkaMessagePack
/*     */     implements ExplicitCommitable
/*     */   {
/*     */     private boolean commitRequested;
/*     */     
/*     */ 
/*     */ 
/*     */     ExplicitCommitableKafkaMessagePack(String srcId, KafkaMessageSourceHook messageSourceHook, String transientDeadLetterTopic, String permanentDeadLetterTopic)
/*     */     {
/* 277 */       super(srcId, messageSourceHook, transientDeadLetterTopic, permanentDeadLetterTopic);
/*     */     }
/*     */     
/*     */     public void commit()
/*     */     {
/* 282 */       this.commitRequested = true;
/*     */     }
/*     */     
/*     */     void completed()
/*     */     {
/* 287 */       if (this.commitRequested) {
/* 288 */         KafkaMessageSource.this.forceCommitOffsets(KafkaMessageSource.this.ticker.currentTimeMillis());
/* 289 */         this.commitRequested = false;
/*     */       }
/* 291 */       getUndeliveredErrorMessages().clear();
/* 292 */       getMessages().clear();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/KafkaMessageSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */