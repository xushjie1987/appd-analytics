/*     */ package com.appdynamics.analytics.processor.event.query;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class EventSchema
/*     */ {
/*  26 */   private static final ObjectMapper MAPPER = new ObjectMapper();
/*     */   public static final String DEFAULT_ALL_FIELD = "_all";
/*     */   private String eventType;
/*     */   
/*  30 */   public String getEventType() { return this.eventType; }
/*     */   
/*     */   public JsonNode getElasticSearchMapping() {
/*  33 */     return this.elasticSearchMapping;
/*     */   }
/*     */   
/*  36 */   public HashMap<String, String> getCaseInsensitiveFieldMapping() { return this.caseInsensitiveFieldMapping; }
/*     */   
/*     */   private JsonNode elasticSearchMapping;
/*     */   private HashMap<String, String> caseInsensitiveFieldMapping;
/*  40 */   public EventSchema(String eventType, String elasticSearchMapping) throws IOException { this.eventType = eventType;
/*  41 */     this.elasticSearchMapping = MAPPER.readTree(elasticSearchMapping);
/*     */     
/*  43 */     buildFieldNormalizationMappings();
/*     */   }
/*     */   
/*     */   public EventSchema(String eventType, JsonNode elasticSearchMapping) {
/*  47 */     this.eventType = eventType;
/*  48 */     this.elasticSearchMapping = elasticSearchMapping;
/*     */     
/*  50 */     buildFieldNormalizationMappings();
/*     */   }
/*     */   
/*     */   private void buildFieldNormalizationMappings() {
/*  54 */     this.caseInsensitiveFieldMapping = new HashMap();
/*  55 */     Iterator<String> fields = this.elasticSearchMapping.get("properties").fieldNames();
/*  56 */     while (fields.hasNext()) {
/*  57 */       String field = (String)fields.next();
/*  58 */       this.caseInsensitiveFieldMapping.put(field.toLowerCase(), field);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean containsField(String field) {
/*  63 */     JsonNode fieldNode = getFieldNode(field);
/*  64 */     return fieldNode != null;
/*     */   }
/*     */   
/*     */   public String normalizeField(String field) {
/*  68 */     if (this.caseInsensitiveFieldMapping.containsKey(field.toLowerCase())) {
/*  69 */       return (String)this.caseInsensitiveFieldMapping.get(field.toLowerCase());
/*     */     }
/*  71 */     return field;
/*     */   }
/*     */   
/*     */   public String getAllField() {
/*  75 */     if (containsField("message")) {
/*  76 */       return "message";
/*     */     }
/*  78 */     return "_all";
/*     */   }
/*     */   
/*     */   public List<String> getFields()
/*     */   {
/*  83 */     return new ArrayList(this.caseInsensitiveFieldMapping.values());
/*     */   }
/*     */   
/*     */   public String getFieldTypeAsString(String field) {
/*  87 */     JsonNode fieldNode = getFieldNode(field);
/*  88 */     if (fieldNode != null) {
/*  89 */       JsonNode typeNode = fieldNode.get("type");
/*  90 */       return typeNode.asText().toLowerCase();
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   public EventFieldType getFieldType(String field) {
/*  96 */     switch (getFieldTypeAsString(field)) {
/*     */     case "string": 
/*  98 */       return EventFieldType.STRING;
/*     */     case "integer": 
/*     */     case "long": 
/* 101 */       return EventFieldType.NUMBER_INT;
/*     */     case "float": 
/*     */     case "double": 
/* 104 */       return EventFieldType.NUMBER_FLOAT;
/*     */     case "boolean": 
/* 106 */       return EventFieldType.BOOLEAN;
/*     */     case "date": 
/* 108 */       return EventFieldType.DATETIME;
/*     */     case "null": 
/* 110 */       return EventFieldType.NULL;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 115 */     return EventFieldType.UNKNOWN;
/*     */   }
/*     */   
/*     */   private JsonNode getFieldNode(String field) {
/* 119 */     JsonNode propertiesNode = this.elasticSearchMapping.get("properties");
/* 120 */     return propertiesNode.get(field);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/query/EventSchema.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */