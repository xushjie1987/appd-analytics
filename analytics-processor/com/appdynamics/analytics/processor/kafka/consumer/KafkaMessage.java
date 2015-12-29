/*    */ package com.appdynamics.analytics.processor.kafka.consumer;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.dto.GenericMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KafkaMessage
/*    */   implements GenericMessage
/*    */ {
/*    */   final String source;
/*    */   final String id;
/*    */   final byte[] body;
/*    */   
/*    */   public KafkaMessage(String source, String id, byte[] body)
/*    */   {
/* 21 */     this.source = source;
/* 22 */     this.id = id;
/* 23 */     this.body = body;
/*    */   }
/*    */   
/*    */   public String getSource()
/*    */   {
/* 28 */     return this.source;
/*    */   }
/*    */   
/*    */   public String getId()
/*    */   {
/* 33 */     return this.id;
/*    */   }
/*    */   
/*    */   public byte[] getBody()
/*    */   {
/* 38 */     return this.body;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/consumer/KafkaMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */