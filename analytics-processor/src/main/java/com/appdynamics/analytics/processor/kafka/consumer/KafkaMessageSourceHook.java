/*    */ package com.appdynamics.analytics.processor.kafka.consumer;
/*    */ 
/*    */ import com.appdynamics.analytics.message.util.RetriableMessagePack.ReturnedMessage;
/*    */ import com.appdynamics.analytics.processor.event.dto.GenericMessage;
/*    */ import com.appdynamics.common.util.misc.ThreadLocalObjects;
/*    */ import com.google.common.hash.HashCode;
/*    */ import com.google.common.hash.HashFunction;
/*    */ import com.google.common.hash.Hasher;
/*    */ import com.google.common.hash.Hashing;
/*    */ import java.util.Collection;
/*    */ import kafka.message.MessageAndMetadata;
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
/*    */ public class KafkaMessageSourceHook
/*    */ {
/* 30 */   private static final HashFunction HASH_FUNCTION = Hashing.murmur3_128(0);
/*    */   public static final String KEY_SEPARATOR = "-";
/*    */   
/*    */   protected GenericMessage convertToMessage(MessageAndMetadata<byte[], byte[]> msgAndMetadata) {
/* 34 */     String topic = msgAndMetadata.topic();
/* 35 */     String id = generateUniqueId((byte[])msgAndMetadata.message(), msgAndMetadata.partition(), msgAndMetadata.offset());
/* 36 */     return new KafkaMessage(topic, id, (byte[])msgAndMetadata.message());
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
/*    */   protected void handleUndeliveredMessages(Collection<GenericMessage> undeliveredMessages, Collection<RetriableMessagePack.ReturnedMessage<GenericMessage>> undeliveredErrorMessages, String transientDeadLetterTopic, String permanentDeadLetterTopic) {}
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
/*    */   protected String generateUniqueId(byte[] message, int kafkaPartition, long kafkaOffset)
/*    */   {
/* 64 */     HashCode hashCode = HASH_FUNCTION.newHasher(message.length).putBytes(message).hash();
/*    */     
/* 66 */     StringBuilder sb = ThreadLocalObjects.stringBuilder();
/*    */     try {
/* 68 */       return hashCode.asLong() + "-" + kafkaPartition + "-" + kafkaOffset;
/*    */     }
/*    */     finally
/*    */     {
/* 72 */       sb.setLength(0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/KafkaMessageSourceHook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */