/*     */ package com.appdynamics.analytics.processor.event;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElasticSearchMappingHelper
/*     */ {
/*     */   public static void ensureDocValues(JsonNode mapping)
/*     */   {
/*  27 */     JsonNode properties = mapping.get("properties");
/*  28 */     if (properties != null) {
/*  29 */       for (JsonNode property : properties) {
/*  30 */         ensureDocValueInMapping(property, "properties");
/*     */       }
/*     */     }
/*     */     
/*  34 */     JsonNode templates = mapping.get("dynamic_templates");
/*  35 */     if (templates != null) {
/*  36 */       for (JsonNode template : templates) {
/*  37 */         for (JsonNode fieldMapping : template) {
/*  38 */           if (fieldMapping != null) {
/*  39 */             JsonNode mappingNode = fieldMapping.get("mapping");
/*  40 */             if (mappingNode != null) {
/*  41 */               ensureDocValueInMapping(mappingNode, "mapping");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void ensureDocValueInMapping(JsonNode property, String propertyType) {
/*  50 */     if (isFieldAnalyzed(property, false)) {
/*  51 */       return;
/*     */     }
/*     */     
/*  54 */     JsonNode propertiesNode = property.get(propertyType);
/*  55 */     if (propertiesNode != null) {
/*  56 */       Iterator<String> fields = propertiesNode.fieldNames();
/*  57 */       while (fields.hasNext()) {
/*  58 */         String field = (String)fields.next();
/*  59 */         JsonNode fieldNode = propertiesNode.get(field);
/*  60 */         if (fieldNode != null) {
/*  61 */           ensureDocValueInMapping(fieldNode, propertyType);
/*     */         }
/*     */       }
/*     */     } else {
/*  65 */       JsonNode propertyNode = property.get("doc_values");
/*  66 */       if ((propertyNode == null) && (canApplyDocValues(property))) {
/*  67 */         ((ObjectNode)property).put("doc_values", true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean canApplyDocValues(JsonNode property) {
/*  73 */     JsonNode type = property.get("type");
/*  74 */     return (type != null) && (type.isTextual()) && (ElasticSearchConstants.VALID_DOC_VALUE_TYPES.contains(type.asText().toLowerCase()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isFieldAnalyzed(JsonNode property, boolean strict)
/*     */   {
/*  85 */     JsonNode indexNode = property.get("index");
/*  86 */     if (indexNode != null) {
/*  87 */       String value = indexNode.textValue();
/*  88 */       if (value.equals("analyzed")) {
/*  89 */         return true;
/*     */       }
/*     */     } else {
/*  92 */       JsonNode nodeType = property.get("type");
/*  93 */       if (nodeType != null)
/*     */       {
/*     */ 
/*  96 */         String value = nodeType.textValue();
/*  97 */         if ((value.toLowerCase().equals("string")) && (!strict)) {
/*  98 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 105 */     return false;
/*     */   }
/*     */   
/*     */   public static JsonNode ensureDocValueInMappingMetadata(JsonNode jsonNode) {
/* 109 */     Iterator<String> fields = jsonNode.fieldNames();
/* 110 */     while (fields.hasNext()) {
/* 111 */       String field = (String)fields.next();
/* 112 */       JsonNode fieldNode = jsonNode.get(field);
/* 113 */       if (fieldNode != null) {
/* 114 */         ensureDocValues(fieldNode);
/*     */       }
/*     */     }
/* 117 */     return jsonNode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void stripDocValues(JsonNode mapping)
/*     */   {
/* 125 */     JsonNode properties = (JsonNode)Preconditions.checkNotNull(mapping.get("properties"), "Expected to find properties node");
/*     */     
/* 127 */     for (JsonNode property : properties) {
/* 128 */       stripDocValueFromMapping(property, "properties");
/*     */     }
/*     */     
/* 131 */     JsonNode templates = mapping.get("dynamic_templates");
/* 132 */     if (templates != null) {
/* 133 */       for (JsonNode template : templates) {
/* 134 */         for (JsonNode fieldMapping : template) {
/* 135 */           if (fieldMapping != null) {
/* 136 */             JsonNode mappingNode = fieldMapping.get("mapping");
/* 137 */             if (mappingNode != null) {
/* 138 */               stripDocValueFromMapping(mappingNode, "mapping");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void stripDocValueFromMapping(JsonNode property, String propertyType)
/*     */   {
/* 148 */     if (isFieldAnalyzed(property, false)) {
/* 149 */       return;
/*     */     }
/*     */     
/* 152 */     JsonNode propertiesNode = property.get(propertyType);
/* 153 */     if (propertiesNode != null) {
/* 154 */       Iterator<String> fields = propertiesNode.fieldNames();
/* 155 */       while (fields.hasNext()) {
/* 156 */         String field = (String)fields.next();
/* 157 */         JsonNode fieldNode = propertiesNode.get(field);
/* 158 */         if (fieldNode != null) {
/* 159 */           stripDocValueFromMapping(fieldNode, propertyType);
/*     */         }
/*     */       }
/*     */     } else {
/* 163 */       JsonNode propertyNode = property.get("doc_values");
/* 164 */       if (propertyNode != null) {
/* 165 */         ((ObjectNode)property).remove("doc_values");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static JsonNode stripDocValueFromMappingMetadata(JsonNode jsonNode) {
/* 171 */     Iterator<String> fields = jsonNode.fieldNames();
/* 172 */     while (fields.hasNext()) {
/* 173 */       String field = (String)fields.next();
/* 174 */       JsonNode fieldNode = jsonNode.get(field);
/* 175 */       if (fieldNode != null) {
/* 176 */         stripDocValues(fieldNode);
/*     */       }
/*     */     }
/* 179 */     return jsonNode;
/*     */   }
/*     */   
/*     */   public static JsonNode ensureFieldDataOptimizedMapping(JsonNode jsonNode) {
/* 183 */     Iterator<String> fields = jsonNode.fieldNames();
/* 184 */     while (fields.hasNext()) {
/* 185 */       String field = (String)fields.next();
/* 186 */       JsonNode fieldNode = jsonNode.get(field);
/* 187 */       if (fieldNode != null) {
/* 188 */         ensureFieldDataOptimized(fieldNode);
/*     */       }
/*     */     }
/* 191 */     return jsonNode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void ensureFieldDataOptimized(JsonNode mapping)
/*     */   {
/* 199 */     JsonNode properties = mapping.get("properties");
/* 200 */     if (properties != null) {
/* 201 */       for (JsonNode property : properties) {
/* 202 */         ensureFieldDataOptimized(property, "properties");
/*     */       }
/*     */     }
/*     */     
/* 206 */     JsonNode templates = mapping.get("dynamic_templates");
/* 207 */     if (templates != null) {
/* 208 */       for (JsonNode template : templates) {
/* 209 */         for (JsonNode fieldMapping : template) {
/* 210 */           if (fieldMapping != null) {
/* 211 */             JsonNode mappingNode = fieldMapping.get("mapping");
/* 212 */             if (mappingNode != null) {
/* 213 */               ensureFieldDataOptimized(mappingNode, "mapping");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void ensureFieldDataOptimized(JsonNode property, String propertyType) {
/* 222 */     JsonNode propertiesNode = property.get(propertyType);
/* 223 */     if ((propertiesNode == null) && (!isFieldAnalyzed(property, false))) {
/* 224 */       return;
/*     */     }
/*     */     
/* 227 */     if (propertiesNode != null) {
/* 228 */       Iterator<String> fields = propertiesNode.fieldNames();
/* 229 */       while (fields.hasNext()) {
/* 230 */         String field = (String)fields.next();
/* 231 */         JsonNode fieldNode = propertiesNode.get(field);
/* 232 */         if (fieldNode != null) {
/* 233 */           ensureFieldDataOptimized(fieldNode, propertyType);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 238 */       JsonNode typeNode = property.get("type");
/* 239 */       if ((typeNode != null) && (typeNode.asText().equals("string"))) {
/* 240 */         ObjectNode fieldDataProperties = Reader.DEFAULT_JSON_MAPPER.createObjectNode();
/* 241 */         fieldDataProperties.put("format", "disabled");
/* 242 */         ((ObjectNode)property).put("fielddata", fieldDataProperties);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void ensureStringNotAnalyzedDefault(JsonNode mapping)
/*     */   {
/* 252 */     JsonNode properties = mapping.get("properties");
/* 253 */     if (properties != null) {
/* 254 */       for (JsonNode property : properties) {
/* 255 */         defaultStringFieldNotAnalyzed(property, "properties");
/*     */       }
/*     */     }
/*     */     
/* 259 */     defaultDynamicTemplateStringFieldNotAnalyzed(mapping);
/*     */   }
/*     */   
/*     */   private static void defaultStringFieldNotAnalyzed(JsonNode property, String propertyType) {
/* 263 */     if (isFieldAnalyzed(property, true)) {
/* 264 */       return;
/*     */     }
/*     */     
/* 267 */     JsonNode propertiesNode = property.get(propertyType);
/* 268 */     if (propertiesNode != null) {
/* 269 */       Iterator<String> fields = propertiesNode.fieldNames();
/* 270 */       while (fields.hasNext()) {
/* 271 */         String field = (String)fields.next();
/* 272 */         JsonNode fieldNode = propertiesNode.get(field);
/* 273 */         if (fieldNode != null) {
/* 274 */           defaultStringFieldNotAnalyzed(fieldNode, propertyType);
/*     */         }
/*     */       }
/*     */     } else {
/* 278 */       JsonNode indexNode = property.get("index");
/* 279 */       JsonNode typeNode = property.get("type");
/* 280 */       if ((indexNode == null) && (typeNode != null) && (typeNode.asText().equals("string"))) {
/* 281 */         ((ObjectNode)property).put("index", "not_analyzed");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void defaultDynamicTemplateStringFieldNotAnalyzed(JsonNode mapping) {
/* 287 */     JsonNode templates = mapping.get("dynamic_templates");
/* 288 */     boolean hadDynamicStringMatchAllTemplate = false;
/* 289 */     if (templates != null) {
/* 290 */       for (JsonNode template : templates) {
/* 291 */         for (JsonNode fieldMapping : template) {
/* 292 */           if (fieldMapping != null) {
/* 293 */             JsonNode matchType = fieldMapping.get("match_mapping_type");
/* 294 */             if (matchType != null) {
/* 295 */               JsonNode mappingNode = fieldMapping.get("mapping");
/* 296 */               if (mappingNode != null) {
/* 297 */                 defaultStringFieldNotAnalyzed(mappingNode, "mapping");
/*     */               }
/*     */               
/* 300 */               JsonNode matchNode = fieldMapping.get("match");
/* 301 */               if ((matchNode != null) && (matchNode.asText().equals("*")) && (matchType.asText().equals("string")))
/*     */               {
/*     */ 
/* 304 */                 hadDynamicStringMatchAllTemplate = true;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 312 */     if (!hadDynamicStringMatchAllTemplate) {
/* 313 */       addDefaultStringFieldsDynamicTemplate((ObjectNode)mapping, templates);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addDefaultStringFieldsDynamicTemplate(ObjectNode mapping, JsonNode templates) {
/* 318 */     JsonNodeFactory factory = JsonNodeFactory.instance;
/*     */     ArrayNode templatesArr;
/* 320 */     if (templates == null) {
/* 321 */       ArrayNode templatesArr = factory.arrayNode();
/* 322 */       mapping.put("dynamic_templates", templatesArr);
/*     */     } else {
/* 324 */       templatesArr = (ArrayNode)templates;
/*     */     }
/*     */     
/*     */ 
/* 328 */     String dynamicTemplate = String.format("{\"%s\":{\"mapping\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"match\":\"*\",\"match_mapping_type\":\"string\"}}", new Object[] { getDynamicTemplateFieldName(templatesArr) });
/*     */     JsonNode template;
/*     */     try
/*     */     {
/* 332 */       template = Reader.DEFAULT_JSON_MAPPER.readTree(dynamicTemplate);
/*     */     } catch (IOException e) {
/* 334 */       throw Throwables.propagate(e);
/*     */     }
/* 336 */     templatesArr.add(template);
/*     */   }
/*     */   
/*     */   private static String getDynamicTemplateFieldName(ArrayNode templatesArr) {
/* 340 */     String dynamicTemplateFieldName = "string_fields";
/*     */     
/*     */ 
/*     */ 
/* 344 */     while (templatesArr.findValues(dynamicTemplateFieldName).size() != 0) {
/* 345 */       int randomSixDigitNumber = 100000 + new Random().nextInt(900000);
/* 346 */       dynamicTemplateFieldName = "string_fields" + randomSixDigitNumber;
/*     */     }
/*     */     
/*     */ 
/* 350 */     return dynamicTemplateFieldName;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ElasticSearchMappingHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */