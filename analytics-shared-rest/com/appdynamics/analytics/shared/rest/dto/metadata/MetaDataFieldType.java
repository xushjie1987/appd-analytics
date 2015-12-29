/*    */ package com.appdynamics.analytics.shared.rest.dto.metadata;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
/*    */ import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/*    */ import java.io.IOException;
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
/*    */ @JsonSerialize(using=LowerCaseEnumSerializer.class)
/*    */ @JsonDeserialize(using=LowerCaseEnumDeserializer.class)
/*    */ public enum MetaDataFieldType
/*    */ {
/* 31 */   OBJECT, 
/* 32 */   ARRAY, 
/* 33 */   STRING, 
/* 34 */   DATE, 
/* 35 */   BOOLEAN, 
/* 36 */   FLOAT, 
/* 37 */   DOUBLE, 
/* 38 */   BYTE, 
/* 39 */   SHORT, 
/* 40 */   INTEGER, 
/* 41 */   LONG, 
/* 42 */   NULL, 
/* 43 */   MULTI_FIELD, 
/* 44 */   NESTED;
/*    */   
/*    */   private MetaDataFieldType() {}
/*    */   
/*    */   public static class LowerCaseEnumSerializer extends JsonSerializer<MetaDataFieldType>
/*    */   {
/*    */     public void serialize(MetaDataFieldType value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 51 */       jgen.writeString(value.name().toLowerCase());
/*    */     }
/*    */   }
/*    */   
/*    */   public static class LowerCaseEnumDeserializer
/*    */     extends JsonDeserializer<MetaDataFieldType>
/*    */   {
/*    */     public MetaDataFieldType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
/*    */     {
/* 60 */       return MetaDataFieldType.valueOf(jp.getValueAsString().toUpperCase());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/metadata/MetaDataFieldType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */