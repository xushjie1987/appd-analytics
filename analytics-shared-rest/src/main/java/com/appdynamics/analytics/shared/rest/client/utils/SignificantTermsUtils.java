/*     */ package com.appdynamics.analytics.shared.rest.client.utils;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.dto.elasticsearch.SignificantTermsField;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SignificantTermsUtils
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(SignificantTermsUtils.class);
/*     */   
/*     */ 
/*  29 */   private static final Set<String> BT_BG_TERM_FIELDS = ImmutableSet.of("application");
/*  30 */   private static final Set<String> BT_BG_NESTED_FIELDS = ImmutableSet.of("segments.node", "segments.tier");
/*  31 */   private static final Set<String> LOG_BG_TERM_FIELDS = ImmutableSet.of("host", "source", "sourceType", "nodeName", "tierName", "appName", new String[0]);
/*     */   
/*  33 */   private static final String[] BG_TERM_PATH = { "terms" };
/*  34 */   private static final String[] BG_NESTED_PATH = { "nested", "filter", "terms" };
/*  35 */   private static final String[] FILTER_MUST_PATH = { "filtered", "filter", "bool", "must" };
/*  36 */   private static final String[] QUERY_MUST_PATH = { "filtered", "query", "bool", "must" };
/*  37 */   private static final String[] TIMESTAMP_PATH = { "range", "eventTimestamp" };
/*     */   
/*     */ 
/*     */   private static final String INDEX_TYPE_TEMPLATE = "{\"type\":{\"value\":\"%s___%s\"}},";
/*     */   
/*     */ 
/*  43 */   private static final ObjectMapper MAPPER = new ObjectMapper();
/*     */   
/*     */   private static final long BACKGROUND_FILTER_DURATION = 1209600000L;
/*     */   
/*     */   public static Map<String, SignificantTermsField> getFields(String json)
/*     */   {
/*  49 */     Map<String, SignificantTermsField> termMap = new HashMap();
/*     */     try {
/*  51 */       int failureCount = 0;
/*  52 */       JsonNode root = MAPPER.readTree(json);
/*     */       
/*  54 */       if ((root.has("hits")) && (root.has("aggregations"))) {
/*  55 */         root = root.get("aggregations");
/*     */       }
/*  57 */       Iterator<Map.Entry<String, JsonNode>> fieldsIterator = root.fields();
/*  58 */       while (fieldsIterator.hasNext()) {
/*  59 */         Map.Entry<String, JsonNode> field = (Map.Entry)fieldsIterator.next();
/*  60 */         String fieldName = (String)field.getKey();
/*  61 */         if (fieldName == null) {
/*  62 */           log.error("Field name is null in the returned significant values query result field=[{}], response=[{}]", field, json);
/*     */           
/*  64 */           failureCount++;
/*     */         }
/*     */         else {
/*  67 */           JsonNode fieldNode = (JsonNode)field.getValue();
/*  68 */           if (!fieldNode.has("doc_count")) {
/*  69 */             log.error("Element doc_count cannot be found for field [{}] in response [{}]", field, json);
/*  70 */             failureCount++;
/*     */           }
/*     */           else {
/*  73 */             SignificantTermsField terms = getSignificantTermsFromJson(fieldName, fieldNode);
/*  74 */             if (terms == null) {
/*  75 */               log.error("No significant terms found from json [{}] for field [{}]", fieldNode, fieldName);
/*  76 */               failureCount++;
/*     */ 
/*     */ 
/*     */             }
/*  80 */             else if (terms.getTerms().iterator().hasNext()) {
/*  81 */               termMap.put(fieldName, terms);
/*     */             }
/*     */           } } }
/*  84 */       if ((root.size() == failureCount) && (failureCount > 0)) {
/*  85 */         throw new RuntimeException("No valid significant terms exist in response [" + json + "]");
/*     */       }
/*     */     } catch (Exception e) {
/*  88 */       log.error("Error occurred while converting significant terms response [{}]", json, e);
/*  89 */       Throwables.propagate(e);
/*     */     }
/*  91 */     return termMap;
/*     */   }
/*     */   
/*     */   private static SignificantTermsField getSignificantTermsFromJson(String fieldName, JsonNode node) {
/*  95 */     if (node == null) {
/*  96 */       log.error("Significant terms for field [{}] has null json node", fieldName);
/*  97 */       return null;
/*     */     }
/*     */     
/* 100 */     if (!node.has("buckets")) {
/* 101 */       if (node.has(fieldName)) {
/* 102 */         node = node.get(fieldName);
/* 103 */         if (!node.has("buckets")) {
/* 104 */           log.error("Significant terms json request [{}] for field [{}] should contain bucket array", node, fieldName);
/*     */           
/* 106 */           return null;
/*     */         }
/*     */       } else {
/* 109 */         log.error("Cannot infer significant values nested field [{}] from json [{}]", fieldName, node);
/* 110 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 114 */     long docCount = node.get("doc_count").asLong();
/* 115 */     JsonNode bucketNode = node.get("buckets");
/*     */     
/* 117 */     if ((bucketNode != null) && (bucketNode.isArray())) {
/* 118 */       return new SignificantTermsField(docCount, bucketNode);
/*     */     }
/* 120 */     log.error("Response [{}] from significant terms is not in the expected format. buckets element should be an array", node);
/*     */     
/* 122 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getBackgroundSearch(JsonNode query, String eventType, String accountName)
/*     */   {
/* 133 */     JsonNode filterMustNode = getNode(FILTER_MUST_PATH, query);
/* 134 */     JsonNode queryMustNode = getNode(QUERY_MUST_PATH, query);
/*     */     
/* 136 */     StringBuilder sb = new StringBuilder();
/*     */     
/*     */ 
/* 139 */     sb.append(getTypeFilter(accountName, eventType));
/*     */     
/* 141 */     if (eventType.equals("biz_txn_v1")) {
/* 142 */       addBtBackgroundSearch(queryMustNode, filterMustNode, sb);
/* 143 */     } else if (eventType.equals("log_v1")) {
/* 144 */       addLogBackgroundSearch(queryMustNode, filterMustNode, sb);
/*     */     }
/*     */     
/* 147 */     if (sb.length() == 0) {
/* 148 */       return null;
/*     */     }
/* 150 */     addTimestampToBackground(filterMustNode, sb);
/* 151 */     return "{\"bool\":{\"must\":[" + sb.substring(0, sb.length() - 1) + "]}}";
/*     */   }
/*     */   
/*     */   public static String getNestedFilter(String filter)
/*     */   {
/* 156 */     if (filter == null) {
/* 157 */       return null;
/*     */     }
/*     */     try {
/* 160 */       JsonNode tree = MAPPER.readTree(filter);
/* 161 */       StringBuilder sb = new StringBuilder();
/* 162 */       for (JsonNode node : tree.get("bool").get("must")) {
/* 163 */         if ((node.has("nested")) && (node.get("nested").has("filter")))
/*     */         {
/*     */ 
/* 166 */           sb.append(node.get("nested").get("filter")).append(","); }
/*     */       }
/* 168 */       if (sb.length() == 0) {
/* 169 */         return null;
/*     */       }
/* 171 */       return "{\"bool\":{\"must\":[" + sb.substring(0, sb.length() - 1) + "]}}";
/*     */     }
/*     */     catch (Exception e) {
/* 174 */       log.error("Error occurred while extracting nested filters from [{}]", filter, e);
/* 175 */       Throwables.propagate(e);
/*     */     }
/* 177 */     return null;
/*     */   }
/*     */   
/*     */   private static String getTypeFilter(String accountName, String eventType) {
/* 181 */     return String.format("{\"type\":{\"value\":\"%s___%s\"}},", new Object[] { accountName, eventType }).toLowerCase();
/*     */   }
/*     */   
/*     */   private static void addTimestampToBackground(JsonNode filterMustNode, StringBuilder sb) {
/* 185 */     JsonNode timeNode = getNode(TIMESTAMP_PATH, filterMustNode);
/* 186 */     if (timeNode != null) {
/* 187 */       long fromTime = timeNode.get("from").asLong();
/* 188 */       long toTime = timeNode.get("to").asLong();
/* 189 */       long diff = (toTime - fromTime) * 2L;
/* 190 */       if (diff < 1209600000L) {
/* 191 */         diff = 1209600000L;
/*     */       }
/* 193 */       fromTime = toTime - diff;
/* 194 */       sb.append("{\"").append("range").append("\":{\"").append("eventTimestamp").append("\":{\"").append("from").append("\":").append(fromTime).append(",\"").append("to").append("\":").append(toTime).append("}}},");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addBtBackgroundSearch(JsonNode queryMustNode, JsonNode filterMustNode, StringBuilder sb)
/*     */   {
/* 203 */     if (queryMustNode != null) {
/* 204 */       addFiltersFromQuery(queryMustNode, BT_BG_TERM_FIELDS, sb);
/*     */     }
/* 206 */     if (filterMustNode != null) {
/* 207 */       addFiltersFromNestedFilter(filterMustNode, BT_BG_NESTED_FIELDS, sb);
/* 208 */       addFiltersFromTermFilter(filterMustNode, BT_BG_TERM_FIELDS, sb);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addLogBackgroundSearch(JsonNode queryMustNode, JsonNode filterMustNode, StringBuilder sb) {
/* 213 */     if (queryMustNode != null) {
/* 214 */       addFiltersFromQuery(queryMustNode, LOG_BG_TERM_FIELDS, sb);
/*     */     }
/* 216 */     if (filterMustNode != null) {
/* 217 */       addFiltersFromTermFilter(filterMustNode, LOG_BG_TERM_FIELDS, sb);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addFiltersFromNestedFilter(JsonNode mustNode, Set<String> fieldNames, StringBuilder sb) {
/* 222 */     addFiltersFromTermHelper(mustNode, fieldNames, BG_NESTED_PATH, sb);
/*     */   }
/*     */   
/*     */   private static void addFiltersFromTermFilter(JsonNode mustNode, Set<String> fieldNames, StringBuilder sb) {
/* 226 */     addFiltersFromTermHelper(mustNode, fieldNames, BG_TERM_PATH, sb);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void addFiltersFromTermHelper(JsonNode mustNode, Set<String> fieldNames, String[] path, StringBuilder sb)
/*     */   {
/* 232 */     for (JsonNode filter : mustNode) {
/* 233 */       JsonNode innerQuery = getNode(path, filter);
/* 234 */       if ((innerQuery != null) && (fieldNames.contains(innerQuery.fieldNames().next()))) {
/* 235 */         sb.append(filter.toString()).append(",");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void addFiltersFromQuery(JsonNode mustNode, Set<String> fieldNames, StringBuilder sb) {
/* 241 */     for (JsonNode query : mustNode) {
/* 242 */       if (query.has("query_string")) {
/* 243 */         JsonNode innerQuery = query.get("query_string");
/* 244 */         if ((innerQuery.has("query")) && (innerQuery.has("default_field")))
/*     */         {
/* 246 */           if (fieldNames.contains(innerQuery.get("default_field").asText())) {
/* 247 */             sb.append("{\"").append("query").append("\":").append(query.toString()).append("},");
/*     */           }
/*     */         }
/*     */       }
/* 251 */       else if (query.has("match")) {
/* 252 */         JsonNode innerQuery = query.get("match");
/* 253 */         if ((innerQuery != null) && (fieldNames.contains(innerQuery.fieldNames().next()))) {
/* 254 */           sb.append("{\"").append("query").append("\":").append(query.toString()).append("},");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static JsonNode getNode(String[] path, JsonNode root)
/*     */   {
/* 262 */     if (root == null) {
/* 263 */       return null;
/*     */     }
/*     */     
/* 266 */     if (root.isArray()) {
/* 267 */       for (JsonNode childNode : root) {
/* 268 */         JsonNode tmp = getNode(path, childNode);
/* 269 */         if (tmp != null) {
/* 270 */           return tmp;
/*     */         }
/*     */       }
/*     */     } else {
/* 274 */       JsonNode tmpNode = root;
/*     */       
/* 276 */       for (String p : path) {
/* 277 */         tmpNode = tmpNode.get(p);
/* 278 */         if (tmpNode == null) {
/* 279 */           return null;
/*     */         }
/*     */       }
/* 282 */       return tmpNode;
/*     */     }
/* 284 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/SignificantTermsUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */