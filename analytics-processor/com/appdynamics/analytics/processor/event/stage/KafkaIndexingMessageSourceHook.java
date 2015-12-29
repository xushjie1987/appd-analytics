/*     */ package com.appdynamics.analytics.processor.event.stage;
/*     */ 
/*     */ import com.appdynamics.analytics.message.util.RetriableMessagePack.ReturnedMessage;
/*     */ import com.appdynamics.analytics.processor.admin.ActionType;
/*     */ import com.appdynamics.analytics.processor.event.dto.GenericMessage;
/*     */ import com.appdynamics.analytics.processor.kafka.consumer.KafkaMessageSourceHook;
/*     */ import com.appdynamics.analytics.processor.kafka.producer.KafkaProducerSupplier;
/*     */ import com.appdynamics.common.io.payload.Bytes;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.inject.Inject;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import kafka.common.FailedToSendMessageException;
/*     */ import kafka.javaapi.producer.Producer;
/*     */ import kafka.message.MessageAndMetadata;
/*     */ import kafka.producer.KeyedMessage;
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
/*     */ public class KafkaIndexingMessageSourceHook
/*     */   extends KafkaMessageSourceHook
/*     */ {
/*  35 */   private static final Logger log = LoggerFactory.getLogger(KafkaIndexingMessageSourceHook.class);
/*     */   
/*  37 */   private static final Splitter KEY_SPLITTER = Splitter.on("-");
/*  38 */   private static final Joiner KEY_JOINER = Joiner.on("-").useForNull("unknown");
/*     */   private final KafkaProducerSupplier kafkaProducerSupplier;
/*     */   
/*     */   @Inject
/*     */   public KafkaIndexingMessageSourceHook(KafkaProducerSupplier kafkaProducerSupplier) {
/*  43 */     this.kafkaProducerSupplier = kafkaProducerSupplier;
/*     */   }
/*     */   
/*     */   protected GenericMessage convertToMessage(MessageAndMetadata<byte[], byte[]> msgAndMetadata)
/*     */   {
/*  48 */     String topic = msgAndMetadata.topic();
/*  49 */     String id = generateUniqueId((byte[])msgAndMetadata.message(), msgAndMetadata.partition(), msgAndMetadata.offset());
/*  50 */     ActionType type = ActionType.findType(msgAndMetadata.topic());
/*  51 */     byte[] key = (byte[])msgAndMetadata.key();
/*  52 */     Bytes keyBytes = key == null ? null : new Bytes(key);
/*  53 */     return new IndexingEvent(topic, id, type, keyBytes, new Bytes((byte[])msgAndMetadata.message()));
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
/*     */   protected void handleUndeliveredMessages(Collection<GenericMessage> undeliveredMessages, Collection<RetriableMessagePack.ReturnedMessage<GenericMessage>> undeliveredErrorMessages, String transientDeadLetterTopic, String permanentDeadLetterTopic)
/*     */   {
/*  71 */     boolean isUndeliveredMsgsEmpty = (undeliveredMessages == null) || (undeliveredMessages.isEmpty());
/*  72 */     boolean isUndeliveredErrorMsgsEmpty = (undeliveredErrorMessages == null) || (undeliveredErrorMessages.isEmpty());
/*     */     
/*  74 */     if ((isUndeliveredMsgsEmpty) && (isUndeliveredErrorMsgsEmpty)) {
/*  75 */       return;
/*     */     }
/*     */     
/*  78 */     int transientErrorMessages = isUndeliveredMsgsEmpty ? 0 : undeliveredMessages.size();
/*  79 */     int permanentErrorMessages = isUndeliveredErrorMsgsEmpty ? 0 : undeliveredErrorMessages.size();
/*  80 */     int totalMessages = transientErrorMessages + permanentErrorMessages;
/*  81 */     List<KeyedMessage<byte[], byte[]>> keyedMessages = new ArrayList(totalMessages);
/*     */     
/*  83 */     if (!isUndeliveredMsgsEmpty) {
/*  84 */       for (GenericMessage undeliveredMessage : undeliveredMessages) {
/*  85 */         byte[] messageKey = createMessageKey(undeliveredMessage);
/*     */         
/*     */ 
/*  88 */         KeyedMessage<byte[], byte[]> keyedMessage = new KeyedMessage(transientDeadLetterTopic, messageKey, undeliveredMessage.getBody());
/*     */         
/*  90 */         keyedMessages.add(keyedMessage);
/*     */       }
/*     */     }
/*     */     
/*  94 */     if (!isUndeliveredErrorMsgsEmpty) {
/*  95 */       for (RetriableMessagePack.ReturnedMessage<GenericMessage> undeliveredErrorMessage : undeliveredErrorMessages) {
/*  96 */         GenericMessage message = (GenericMessage)undeliveredErrorMessage.getMessage();
/*  97 */         byte[] messageKey = createMessageKey(message);
/*     */         
/*     */ 
/* 100 */         KeyedMessage<byte[], byte[]> keyedMessage = new KeyedMessage(permanentDeadLetterTopic, messageKey, message.getBody());
/*     */         
/* 102 */         keyedMessages.add(keyedMessage);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 107 */       log.info("Sending [{}] transient error messages to transient dead letter topic [{}].  Sending [{}] permanent error messages to permanent dead letter topic [{}]", new Object[] { Integer.valueOf(transientErrorMessages), transientDeadLetterTopic, Integer.valueOf(permanentErrorMessages), permanentDeadLetterTopic });
/*     */       
/*     */ 
/* 110 */       this.kafkaProducerSupplier.get().send(keyedMessages);
/*     */     } catch (FailedToSendMessageException e) {
/* 112 */       log.warn("Failed to send messages to the Kafka dead letter topics [{}, {}]", new Object[] { transientDeadLetterTopic, permanentDeadLetterTopic, e });
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
/*     */   private static byte[] createMessageKey(GenericMessage message)
/*     */   {
/* 127 */     Iterable<String> idParts = KEY_SPLITTER.split(message.getId());
/* 128 */     String partition = (String)Iterables.get(idParts, 1, null);
/* 129 */     String offset = (String)Iterables.get(idParts, 2, null);
/* 130 */     String topic = message.getSource();
/* 131 */     return KEY_JOINER.join(topic, partition, new Object[] { offset }).getBytes(StandardCharsets.UTF_8);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/stage/KafkaIndexingMessageSourceHook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */