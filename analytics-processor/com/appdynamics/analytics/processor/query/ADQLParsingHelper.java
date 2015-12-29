/*     */ package com.appdynamics.analytics.processor.query;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.event.query.EventFieldType;
/*     */ import com.appdynamics.analytics.processor.event.query.EventSchema;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMap.Builder;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSet.Builder;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.elasticsearch.common.joda.time.DateTime;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ADQLParsingHelper
/*     */ {
/*  25 */   private static final Logger log = LoggerFactory.getLogger(ADQLParsingHelper.class);
/*     */   
/*     */ 
/*     */   static final String ALL_FIELD = "*";
/*     */   
/*  30 */   static final Set NUMERIC_FIELD_TYPES = ImmutableSet.builder().add(new Object[] { EventFieldType.NUMBER_FLOAT, EventFieldType.NUMBER_INT }).build();
/*     */   
/*     */ 
/*  33 */   static final Set NUMERIC_AGGREGATION_FUNCTIONS = ImmutableSet.builder().add(new Object[] { "avg", "min", "max", "sum" }).build();
/*     */   
/*     */ 
/*     */ 
/*  37 */   static final Set ALL_AGGREGATION_FUNCTIONS = ImmutableSet.builder().add(new Object[] { "avg", "min", "max", "sum", "count" }).build();
/*     */   
/*     */ 
/*     */ 
/*  41 */   static final ImmutableMap EVENT_TYPE_NAME_MAPPINGS = ImmutableMap.builder().put("transaction", "biz_txn_v1").put("transactions", "biz_txn_v1").put("bt", "biz_txn_v1").put("log", "log_v1").put("logs", "log_v1").put("biz_txn", "biz_txn_v1").put("browser_record", "browserrecord").build();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   static final ImmutableMap DEFAULT_TIMESTAMP_FIELD_MAPPING = ImmutableMap.builder().put("mobilecrashreport", "appcrashtimestamp").build();
/*     */   
/*     */   private Function<String, EventSchema> getEventSchemaFunction;
/*     */   
/*     */   private HashMap<String, EventSchema> eventSchemas;
/*     */   
/*  56 */   private static HashMap<Type, EventFieldType> mapObjectTypeToFieldType = null;
/*     */   
/*     */   public ADQLParsingHelper(Function<String, EventSchema> getEventSchemaFunction) {
/*  59 */     this.getEventSchemaFunction = getEventSchemaFunction;
/*  60 */     this.eventSchemas = new HashMap();
/*     */   }
/*     */   
/*     */   public void addEventSchema(String eventType) {
/*  64 */     if (this.getEventSchemaFunction != null) {
/*  65 */       EventSchema eventSchema = (EventSchema)this.getEventSchemaFunction.apply(eventType);
/*  66 */       this.eventSchemas.put(eventType, eventSchema);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String normalizeEventType(String eventType) {
/*  71 */     if (EVENT_TYPE_NAME_MAPPINGS.containsKey(eventType.toLowerCase())) {
/*  72 */       eventType = (String)EVENT_TYPE_NAME_MAPPINGS.get(eventType.toLowerCase());
/*     */     }
/*  74 */     return eventType;
/*     */   }
/*     */   
/*     */   public String normalizeIdentifierForEventType(String eventType, String field) {
/*  78 */     if (this.getEventSchemaFunction != null) {
/*  79 */       EventSchema eventSchema = (EventSchema)this.getEventSchemaFunction.apply(eventType);
/*  80 */       return eventSchema.normalizeField(field);
/*     */     }
/*  82 */     return field;
/*     */   }
/*     */   
/*     */   public String normalizeIdentifierForEventType(String field) {
/*  86 */     for (EventSchema eventSchema : this.eventSchemas.values()) {
/*  87 */       String normalizedField = eventSchema.normalizeField(field);
/*  88 */       if (eventSchema.containsField(normalizedField)) {
/*  89 */         return normalizedField;
/*     */       }
/*     */     }
/*  92 */     return field;
/*     */   }
/*     */   
/*     */   public static String getTimestampFieldForEventType(String eventType) {
/*  96 */     String timestamp = "eventTimestamp";
/*  97 */     if (DEFAULT_TIMESTAMP_FIELD_MAPPING.containsKey(eventType)) {
/*  98 */       timestamp = (String)DEFAULT_TIMESTAMP_FIELD_MAPPING.get(eventType);
/*     */     }
/* 100 */     return timestamp;
/*     */   }
/*     */   
/*     */   public double parseDoubleLiteral(String doubleLiteral) {
/*     */     try {
/* 105 */       return Double.parseDouble(doubleLiteral);
/*     */     } catch (NumberFormatException ex) {
/* 107 */       throw new ParsingException(String.format("Failed to parse double: %s", new Object[] { doubleLiteral }));
/*     */     }
/*     */   }
/*     */   
/*     */   public long parseLongLiteral(String longLiteral) {
/*     */     try {
/* 113 */       return Long.parseLong(longLiteral);
/*     */     } catch (NumberFormatException ex) {
/* 115 */       throw new ParsingException(String.format("Failed to parse long: %s", new Object[] { longLiteral }));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean parseBooleanLiteral(String booleanLiteral) {
/* 120 */     if (booleanLiteral.equalsIgnoreCase("true"))
/* 121 */       return true;
/* 122 */     if (booleanLiteral.equalsIgnoreCase("false")) {
/* 123 */       return false;
/*     */     }
/* 125 */     throw new ParsingException(String.format("Failed to parse boolean: %s", new Object[] { booleanLiteral }));
/*     */   }
/*     */   
/*     */   public DateTime parseDateTime(String dateTime)
/*     */   {
/*     */     try {
/* 131 */       return DateTime.parse(dateTime);
/*     */     } catch (Exception e) {
/* 133 */       throw new ParsingException(String.format("Failed to date time: %s", new Object[] { dateTime }));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean parseNullLiteral(String nullLiteral) {
/* 138 */     if (nullLiteral.equalsIgnoreCase("null")) {
/* 139 */       return true;
/*     */     }
/* 141 */     throw new ParsingException(String.format("Failed to parse null: %s", new Object[] { nullLiteral }));
/*     */   }
/*     */   
/*     */   public boolean parseStringLiteral(String stringLiteral, boolean expectsQuote)
/*     */   {
/* 146 */     if ((expectsQuote) && (
/* 147 */       (!stringLiteral.startsWith("'")) || (!stringLiteral.endsWith("'")))) {
/* 148 */       throw new ParsingException(String.format("Failed to parse string: %s", new Object[] { stringLiteral }));
/*     */     }
/*     */     
/*     */ 
/* 152 */     return true;
/*     */   }
/*     */   
/*     */   public EventFieldType getEventFieldType(String fieldName) {
/* 156 */     EventFieldType fieldType = null;
/*     */     
/* 158 */     for (EventSchema eventSchema : this.eventSchemas.values()) {
/* 159 */       if (eventSchema.containsField(fieldName)) {
/* 160 */         if (fieldType != null) {
/* 161 */           throw new ParsingException(String.format("Field name [%s] occurs in multiple events, so field type is ambiguous.", new Object[] { fieldName }));
/*     */         }
/*     */         
/*     */ 
/* 165 */         fieldType = eventSchema.getFieldType(fieldName);
/*     */       }
/*     */     }
/*     */     
/* 169 */     return fieldType;
/*     */   }
/*     */   
/*     */   public void validateEventFieldTypes(String fieldName, Set validFieldTypes) {
/* 173 */     EventFieldType fieldType = getEventFieldType(normalizeIdentifierForEventType(fieldName));
/* 174 */     if (fieldType == null) {
/* 175 */       log.warn("No event schema provided, so not validating type of field [{}] in query string", fieldName);
/* 176 */     } else if (!validFieldTypes.contains(fieldType)) {
/* 177 */       throw new ParsingException(String.format("Expecting field [%s] to be of type(s) [%s]", new Object[] { fieldName, validFieldTypes }));
/*     */     }
/*     */   }
/*     */   
/*     */   public void validateIdentifierTypeMatchesValueTypeAsObject(String identifier, Object value)
/*     */   {
/* 183 */     EventFieldType fieldType = getEventFieldType(identifier);
/* 184 */     if (fieldType != null) {
/* 185 */       EventFieldType expectedFieldType = EventFieldType.NULL;
/* 186 */       if (value != null) {
/* 187 */         expectedFieldType = getFieldTypeFromObjectType(value.getClass());
/*     */       }
/* 189 */       if (fieldType != expectedFieldType) {
/* 190 */         throw new ParsingException(String.format("Expected field [%s] of type [%s] does not match type of value [%s]", new Object[] { identifier, fieldType, expectedFieldType }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void validateIdentifierTypeMatchesValueAsString(String identifier, String value, boolean expectsQuotes)
/*     */   {
/* 198 */     EventFieldType fieldType = getEventFieldType(identifier);
/* 199 */     if (fieldType != null) {
/* 200 */       switch (fieldType) {
/*     */       case NUMBER_INT: 
/* 202 */         parseLongLiteral(value);
/* 203 */         break;
/*     */       
/*     */       case NUMBER_FLOAT: 
/* 206 */         parseDoubleLiteral(value);
/* 207 */         break;
/*     */       
/*     */       case BOOLEAN: 
/* 210 */         parseBooleanLiteral(value);
/* 211 */         break;
/*     */       
/*     */       case NULL: 
/* 214 */         parseNullLiteral(value);
/* 215 */         break;
/*     */       
/*     */       case DATETIME: 
/* 218 */         parseDateTime(value);
/* 219 */         break;
/*     */       
/*     */       case STRING: 
/* 222 */         parseStringLiteral(value, expectsQuotes);
/* 223 */         break;
/*     */       
/*     */       default: 
/* 226 */         throw new ParsingException(String.format("Unrecognized event field type [%s] while parsing [%s] for identifier [%s]", new Object[] { fieldType, value, identifier }));
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   public void validateAggegrateFunctionUsage(String function, String field)
/*     */   {
/* 234 */     if (!isAggregateFunction(function)) {
/* 235 */       throw new ParsingException(String.format("Unknown aggregate function [%s] on field [%s]", new Object[] { function, field }));
/*     */     }
/*     */     
/*     */ 
/* 239 */     boolean isNumericAgg = isNumericAggregateFunction(function);
/*     */     
/* 241 */     if (isNumericAgg) {
/* 242 */       if (field.equalsIgnoreCase("*")) {
/* 243 */         throw new ParsingException(String.format("Aggregate function only runs on numeric fields [%s]", new Object[] { function }));
/*     */       }
/*     */       
/* 246 */       validateEventFieldTypes(field, NUMERIC_FIELD_TYPES);
/*     */     }
/*     */   }
/*     */   
/*     */   public EventFieldType getFieldTypeFromObjectType(Type objType)
/*     */   {
/* 252 */     if (mapObjectTypeToFieldType == null) {
/* 253 */       HashMap<Type, EventFieldType> newObjectTypeToFieldType = new HashMap();
/* 254 */       newObjectTypeToFieldType.put(String.class, EventFieldType.STRING);
/* 255 */       newObjectTypeToFieldType.put(Integer.class, EventFieldType.NUMBER_INT);
/* 256 */       newObjectTypeToFieldType.put(Long.class, EventFieldType.NUMBER_INT);
/* 257 */       newObjectTypeToFieldType.put(Float.class, EventFieldType.NUMBER_FLOAT);
/* 258 */       newObjectTypeToFieldType.put(Double.class, EventFieldType.NUMBER_FLOAT);
/* 259 */       newObjectTypeToFieldType.put(Boolean.class, EventFieldType.BOOLEAN);
/* 260 */       newObjectTypeToFieldType.put(DateTime.class, EventFieldType.DATETIME);
/* 261 */       newObjectTypeToFieldType.put(null, EventFieldType.NULL);
/* 262 */       mapObjectTypeToFieldType = newObjectTypeToFieldType;
/*     */     }
/* 264 */     return (EventFieldType)mapObjectTypeToFieldType.get(objType);
/*     */   }
/*     */   
/*     */   public String getAllField() {
/* 268 */     if (this.eventSchemas.size() != 1) {
/* 269 */       log.warn("Match string query is only accurate if there is an event schema. Using default all field since not available.");
/*     */       
/*     */ 
/* 272 */       return "_all";
/*     */     }
/*     */     
/* 275 */     return ((EventSchema)this.eventSchemas.values().iterator().next()).getAllField();
/*     */   }
/*     */   
/*     */   public boolean isNumericAggregateFunction(String function) {
/* 279 */     return NUMERIC_AGGREGATION_FUNCTIONS.contains(function.toLowerCase());
/*     */   }
/*     */   
/*     */   public boolean isAggregateFunction(String function) {
/* 283 */     return ALL_AGGREGATION_FUNCTIONS.contains(function.toLowerCase());
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/ADQLParsingHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */