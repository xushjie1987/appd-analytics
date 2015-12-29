/*    */ package com.appdynamics.analytics.processor.kafka.producer;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.upsert.Upsert;
/*    */ import com.appdynamics.analytics.processor.event.upsert.UpsertCodec;
/*    */ import com.appdynamics.common.io.payload.Bytes;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.google.common.base.Charsets;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.hash.HashCode;
/*    */ import com.google.common.hash.HashFunction;
/*    */ import com.google.common.hash.Hasher;
/*    */ import com.google.common.hash.Hashing;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import kafka.javaapi.producer.Producer;
/*    */ import kafka.producer.KeyedMessage;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @VisibleForTesting
/*    */ public abstract class KafkaUpsertProcessor
/*    */ {
/* 32 */   private static final Logger log = LoggerFactory.getLogger(KafkaUpsertProcessor.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static int generatePartitionHash(String accountName, String eventType, String correlationId)
/*    */   {
/* 45 */     return Hashing.murmur3_32().newHasher().putString(accountName, Charsets.UTF_8).putString(eventType, Charsets.UTF_8).putString(correlationId, Charsets.UTF_8).hash().asInt();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void sendUpsertsToKafka(List<? extends Upsert> upserts, List<String> topics, Producer<byte[], byte[]> producer)
/*    */   {
/* 68 */     int size = upserts.size();
/* 69 */     Preconditions.checkArgument(size == topics.size(), "The number of topics do not match the number of upserts");
/*    */     
/* 71 */     List<KeyedMessage<byte[], byte[]>> keyedMessages = new ArrayList(size);
/*    */     
/* 73 */     for (int i = size - 1; i >= 0; i--) {
/* 74 */       Upsert upsert = (Upsert)upserts.remove(i);
/* 75 */       String accountName = upsert.getAccountName();
/* 76 */       String eventType = upsert.getEventType();
/* 77 */       String topic = (String)topics.remove(i);
/*    */       
/* 79 */       Integer partitionHashCode = Integer.valueOf(generatePartitionHash(accountName, eventType, upsert.getCorrelationId()));
/* 80 */       byte[] messageKey = UpsertCodec.encodeKey(upsert);
/* 81 */       Bytes bytes = upsert.getBytes();
/* 82 */       Preconditions.checkNotNull(bytes, "Upsert bytes cannot be null");
/* 83 */       byte[] messageValue = bytes.getOrCopyArray();
/*    */       
/* 85 */       KeyedMessage<byte[], byte[]> keyedMessage = new KeyedMessage(topic, messageKey, partitionHashCode, messageValue);
/*    */       
/* 87 */       keyedMessages.add(keyedMessage);
/*    */     }
/*    */     
/* 90 */     producer.send(keyedMessages);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/producer/KafkaUpsertProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */