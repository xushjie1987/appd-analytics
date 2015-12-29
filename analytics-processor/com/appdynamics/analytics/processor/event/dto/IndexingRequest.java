/*    */ package com.appdynamics.analytics.processor.event.dto;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.exception.RequestParsingException;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IndexingRequest
/*    */   extends SimpleSerializableDto
/*    */ {
/*    */   private String accountName;
/*    */   private String eventType;
/*    */   
/*    */   public String getAccountName()
/*    */   {
/* 20 */     return this.accountName;
/*    */   }
/*    */   
/* 23 */   public String getEventType() { return this.eventType; }
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
/*    */   private IndexingRequest(String accountName, String eventType, byte[] payload, int payloadOffset, int payloadLength)
/*    */   {
/* 42 */     super(payload, payloadOffset, payloadLength);
/* 43 */     this.accountName = accountName;
/* 44 */     this.eventType = eventType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private IndexingRequest() {}
/*    */   
/*    */ 
/*    */   public static class Factory
/*    */   {
/*    */     public IndexingRequest fromBytes(byte[] bytes)
/*    */     {
/*    */       try
/*    */       {
/* 58 */         return (IndexingRequest)Reader.DEFAULT_JSON_MAPPER.readValue(bytes, IndexingRequest.class);
/*    */       } catch (IOException e) {
/* 60 */         throw new RequestParsingException("Could not parse data", e);
/*    */       }
/*    */     }
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
/*    */     public IndexingRequest fromParams(String accountName, String eventType, byte[] payload, int payloadOffset, int payloadLength)
/*    */     {
/* 75 */       return new IndexingRequest(accountName, eventType, payload, payloadOffset, payloadLength, null);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/dto/IndexingRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */