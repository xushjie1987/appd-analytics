/*    */ package com.appdynamics.analytics.processor.event.dto;
/*    */ 
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SimpleSerializableDto
/*    */ {
/*    */   private byte[] payload;
/*    */   private int offset;
/*    */   private int length;
/*    */   
/*    */   @ConstructorProperties({"payload", "offset", "length"})
/*    */   protected SimpleSerializableDto(byte[] payload, int offset, int length)
/*    */   {
/* 19 */     this.payload = payload;this.offset = offset;this.length = length;
/*    */   }
/*    */   
/*    */   public byte[] getPayload() {
/* 23 */     return this.payload;
/*    */   }
/*    */   
/* 26 */   public int getOffset() { return this.offset; }
/*    */   
/*    */   public int getLength() {
/* 29 */     return this.length;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public byte[] asBytes()
/*    */   {
/*    */     try
/*    */     {
/* 38 */       return Reader.DEFAULT_JSON_MAPPER.writeValueAsBytes(this);
/*    */     } catch (JsonProcessingException e) {
/* 40 */       throw new RuntimeException("Unexpected error", e);
/*    */     }
/*    */   }
/*    */   
/*    */   protected SimpleSerializableDto() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/dto/SimpleSerializableDto.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */