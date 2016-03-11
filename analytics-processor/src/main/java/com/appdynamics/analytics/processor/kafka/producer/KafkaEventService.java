/*     */ package com.appdynamics.analytics.processor.kafka.producer;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.admin.ActionType;
/*     */ import com.appdynamics.analytics.processor.admin.Locator;
/*     */ import com.appdynamics.analytics.processor.event.EventService;
/*     */ import com.appdynamics.analytics.processor.event.EventTypeMetaData;
/*     */ import com.appdynamics.analytics.processor.event.dto.IndexingRequest;
/*     */ import com.appdynamics.analytics.processor.event.dto.IndexingRequest.Factory;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeMissingException;
/*     */ import com.appdynamics.analytics.processor.event.parsers.ObjectListParser;
/*     */ import com.appdynamics.analytics.processor.event.parsers.ObjectListParser.Factory;
/*     */ import com.appdynamics.analytics.processor.event.resource.MoveEventTypeRequest;
/*     */ import com.appdynamics.analytics.processor.event.upsert.Upsert;
/*     */ import com.appdynamics.analytics.processor.exception.UnavailableException;
/*     */ import com.appdynamics.analytics.processor.kafka.EventServiceKafkaConstants;
/*     */ import com.appdynamics.common.util.annotation.Raw;
/*     */ import com.appdynamics.common.util.misc.ThreadLocalObjects;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.google.inject.Inject;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import kafka.common.FailedToSendMessageException;
/*     */ import kafka.javaapi.producer.Producer;
/*     */ import kafka.producer.KeyedMessage;
/*     */ import org.joda.time.DateTime;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public class KafkaEventService
/*     */   implements EventService
/*     */ {
/*  37 */   private static final Logger log = LoggerFactory.getLogger(KafkaEventService.class);
/*     */   private final EventService delegate;
/*     */   
/*  40 */   public void registerEventType(int requestVersion, String accountName, String eventType, String body) { this.delegate.registerEventType(requestVersion, accountName, eventType, body); } public void updateEventType(int requestVersion, String accountName, String eventType, String body) { this.delegate.updateEventType(requestVersion, accountName, eventType, body); } public void bulkUpdateEventType(int requestVersion, String eventType, String body) { this.delegate.bulkUpdateEventType(requestVersion, eventType, body); } public JsonNode getEventType(int requestVersion, String accountName, String eventType) { return this.delegate.getEventType(requestVersion, accountName, eventType); } public boolean eventTypeExists(int requestVersion, String accountName, String eventType) { return this.delegate.eventTypeExists(requestVersion, accountName, eventType); } public void deleteEventType(String accountName, String eventType) { this.delegate.deleteEventType(accountName, eventType); } public void moveEvents(int requestVersion, String accountName, String sourceEventType, MoveEventTypeRequest request) { this.delegate.moveEvents(requestVersion, accountName, sourceEventType, request); } public EventTypeMetaData getEventTypeMetaData(String accountName, String eventType) { return this.delegate.getEventTypeMetaData(accountName, eventType); } public void searchEvents(int requestVersion, String accountName, String eventType, String searchRequest, OutputStream out) { this.delegate.searchEvents(requestVersion, accountName, eventType, searchRequest, out); } public void multiSearchEvents(int requestVersion, String accountName, String searchRequest, OutputStream out) { this.delegate.multiSearchEvents(requestVersion, accountName, searchRequest, out); } public void queryEvents(int requestVersion, String accountName, String queryString, String startTime, String endTime, int limitResults, boolean returnEsJson, OutputStream out) { this.delegate.queryEvents(requestVersion, accountName, queryString, startTime, endTime, limitResults, returnEsJson, out); } public Map<String, Set<String>> eventTypesByAccount() { return this.delegate.eventTypesByAccount(); } public long getCount(int requestVersion, String accountName, String eventType, DateTime from, DateTime to) { return this.delegate.getCount(requestVersion, accountName, eventType, from, to); } public JsonNode relevantFields(int requestVersion, String accountName, String eventType, String queryString) { return this.delegate.relevantFields(requestVersion, accountName, eventType, queryString); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final KafkaProducerSupplier producerSupplier;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final IndexingRequest.Factory indexingRequestDtoFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ObjectListParser.Factory publishReqParserFactory;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Locator locator;
/*     */   
/*     */ 
/*     */ 
/*     */   @Inject
/*     */   KafkaEventService(KafkaProducerSupplier producerSupplier, IndexingRequest.Factory indexingRequestDtoFactory, ObjectListParser.Factory publishReqParserFactory, @Raw EventService delegate, Locator locator)
/*     */   {
/*  67 */     this.producerSupplier = producerSupplier;
/*  68 */     this.indexingRequestDtoFactory = indexingRequestDtoFactory;
/*  69 */     this.publishReqParserFactory = publishReqParserFactory;
/*  70 */     this.delegate = delegate;
/*  71 */     this.locator = locator;
/*     */   }
/*     */   
/*     */ 
/*     */   public void publishEvents(int requestVersion, String accountName, String eventType, ObjectListParser parser, String requestId)
/*     */   {
/*  77 */     verifyEventType(requestVersion, accountName, eventType);
/*  78 */     if (!parser.hasParsed()) {
/*  79 */       parser.parseAndCheckFormat();
/*     */     }
/*     */     
/*  82 */     IndexingRequest dto = this.indexingRequestDtoFactory.fromParams(accountName, eventType, parser.getPayloads(), parser.getPayloadOffset(), parser.getPayloadLength());
/*     */     
/*  84 */     String topic = this.locator.findTopicName(accountName, eventType, ActionType.EVENT_PUBLISH);
/*  85 */     Integer randomPartitionHash = Integer.valueOf(ThreadLocalObjects.random().nextInt(Integer.MAX_VALUE));
/*  86 */     KeyedMessage<byte[], byte[]> keyedMessage = new KeyedMessage(topic, EventServiceKafkaConstants.MESSAGE_KEY, randomPartitionHash, dto.asBytes());
/*     */     
/*  88 */     log.trace("Sending indexing request for account name [{}] and eventType [{}] to kafka", accountName, eventType);
/*     */     try {
/*  90 */       this.producerSupplier.get().send(keyedMessage);
/*     */     } catch (FailedToSendMessageException e) {
/*  92 */       throw new UnavailableException("Could not send publish request", e);
/*     */     }
/*  94 */     log.trace("Indexing request for account name [{}] and eventType [{}] sent to kafka", accountName, eventType);
/*     */   }
/*     */   
/*     */ 
/*     */   public void publishEvents(int requestVersion, String accountName, String eventType, byte[] payloads, int payloadOffset, int payloadLength, String requestId)
/*     */   {
/* 100 */     ObjectListParser parser = this.publishReqParserFactory.make(payloads, payloadOffset, payloadLength);
/* 101 */     publishEvents(requestVersion, accountName, eventType, parser, requestId);
/*     */   }
/*     */   
/*     */   public void upsertEvents(int requestVersion, List<? extends Upsert> upserts)
/*     */   {
/* 106 */     int size = upserts.size();
/* 107 */     List<String> topics = new ArrayList(size);
/* 108 */     boolean debug = log.isDebugEnabled();
/*     */     
/*     */ 
/* 111 */     for (int i = 0; i < size; i++) {
/* 112 */       Upsert upsert = (Upsert)upserts.get(i);
/*     */       
/*     */ 
/* 115 */       String accountName = upsert.getAccountName();
/* 116 */       String eventType = upsert.getEventType();
/*     */       try {
/* 118 */         verifyEventType(requestVersion, accountName, eventType);
/*     */       } catch (EventTypeMissingException e) {
/* 120 */         e.addSuppressed(new IllegalArgumentException("This error occurred at upsert item [" + upsert.getBatchPosition() + "]"));
/*     */         
/* 122 */         throw e;
/*     */       }
/*     */       
/*     */ 
/* 126 */       String topic = this.locator.findTopicName(accountName, eventType, ActionType.EVENT_UPSERT);
/* 127 */       topics.add(topic);
/*     */       
/* 129 */       if (debug) {
/* 130 */         log.debug("Message with batch id [{}] will be sent to account [{}] and event type [{}] over topic [{}]", new Object[] { upsert.getBatchId(), accountName, eventType, topic });
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 136 */       KafkaUpsertProcessor.sendUpsertsToKafka(upserts, topics, this.producerSupplier.get());
/*     */     } catch (RuntimeException e) {
/* 138 */       throw new UnavailableException("Error occurred while sending upserts to Kafka", e);
/*     */     }
/*     */   }
/*     */   
/*     */   void verifyEventType(int requestVersion, String accountName, String eventType) {
/* 143 */     if (!this.delegate.eventTypeExists(requestVersion, accountName, eventType)) {
/* 144 */       throw new EventTypeMissingException(accountName, eventType);
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract interface NonDelegatedEventService
/*     */   {
/*     */     public abstract void publishEvents(int paramInt, String paramString1, String paramString2, ObjectListParser paramObjectListParser, String paramString3);
/*     */     
/*     */     public abstract void publishEvents(int paramInt1, String paramString1, String paramString2, byte[] paramArrayOfByte, int paramInt2, int paramInt3, String paramString3);
/*     */     
/*     */     public abstract void upsertEvents(int paramInt, List<? extends Upsert> paramList);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/producer/KafkaEventService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */