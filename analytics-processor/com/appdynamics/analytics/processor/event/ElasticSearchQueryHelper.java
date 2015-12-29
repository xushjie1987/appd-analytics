/*     */ package com.appdynamics.analytics.processor.event;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.exception.ServiceRequestException;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElasticSearchQueryHelper
/*     */ {
/*  25 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchQueryHelper.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  30 */   private static String[] jsonQueryNestingFields = { "query", "filtered", "query", "bool", "must", "query_string", "query" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ObjectNode parseJsonQueryToObjectModel(String body)
/*     */   {
/*  40 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(body));
/*     */     try {
/*  42 */       return (ObjectNode)Reader.DEFAULT_JSON_MAPPER.readTree(body);
/*     */     } catch (IOException e) {
/*  44 */       log.warn("Could not parse request body [" + body + "] as json: " + e.getMessage());
/*  45 */       throw new ServiceRequestException("Invalid.RequestBody", "The supplied request body [" + body + "] could not be parsed as json. " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  51 */   private static String[] escapeCharacters = { "\\", ".", "?", "+", "*", "|", "{", "}", "[", "]", "(", ")", "\"", "#", "@", "&", "<", ">", "~", "-", "=", "!", "/", ":" };
/*     */   
/*     */ 
/*     */   private static void escapeReservedAndStripWildcardQueryStringCharacters(int index, JsonNode node)
/*     */   {
/*  56 */     if (node == null) {
/*  57 */       return;
/*     */     }
/*  59 */     if (node.isArray()) {
/*  60 */       Iterator<JsonNode> childrenIterator = node.iterator();
/*  61 */       while (childrenIterator.hasNext()) {
/*  62 */         JsonNode child = (JsonNode)childrenIterator.next();
/*  63 */         escapeReservedAndStripWildcardQueryStringCharacters(index, child);
/*     */       }
/*     */     } else {
/*  66 */       String field = jsonQueryNestingFields[index];
/*  67 */       JsonNode fieldNode = node.get(field);
/*  68 */       if (fieldNode != null) {
/*  69 */         if (index == jsonQueryNestingFields.length - 1) {
/*  70 */           String value = fieldNode.asText();
/*     */           
/*  72 */           value = escapeReservedAndStripWildcardCharacters(value);
/*  73 */           ((ObjectNode)node).put("query", value);
/*     */         } else {
/*  75 */           escapeReservedAndStripWildcardQueryStringCharacters(index + 1, fieldNode);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String escapeReservedAndStripWildcardCharacters(String value) {
/*  82 */     value = StringUtils.strip(value, "*");
/*  83 */     String[] parts = StringUtils.splitPreserveAllTokens(value, '"');
/*  84 */     for (int i = 0; i < parts.length; i++)
/*     */     {
/*  86 */       if (i % 2 == 0) {
/*  87 */         for (String escapeStr : escapeCharacters) {
/*  88 */           parts[i] = StringUtils.replace(parts[i], escapeStr, "\\" + escapeStr);
/*     */         }
/*     */       }
/*     */     }
/*  92 */     value = StringUtils.join(parts, '"');
/*  93 */     return value;
/*     */   }
/*     */   
/*     */   public static String stripWildcardQueryStringFromSearchRequest(String searchRequest) {
/*  97 */     JsonNode jsonRequest = parseJsonQueryToObjectModel(searchRequest);
/*  98 */     escapeReservedAndStripWildcardQueryStringCharacters(0, jsonRequest);
/*     */     try {
/* 100 */       return Reader.DEFAULT_JSON_MAPPER.writeValueAsString(jsonRequest);
/*     */     } catch (JsonProcessingException e) {
/* 102 */       log.warn("Could not parse request body [" + jsonRequest + "] as json: " + e.getMessage());
/* 103 */       throw new ServiceRequestException("Invalid.RequestBody", "The supplied request body [" + jsonRequest + "] could not be parsed as json. " + e.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ElasticSearchQueryHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */