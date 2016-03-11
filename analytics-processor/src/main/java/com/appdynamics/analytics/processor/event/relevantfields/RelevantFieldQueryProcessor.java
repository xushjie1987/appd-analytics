/*     */ package com.appdynamics.analytics.processor.event.relevantfields;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.event.EventService;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.SignificantTermsUtils;
/*     */ import com.appdynamics.analytics.shared.rest.dto.elasticsearch.SignificantTermsField;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ public class RelevantFieldQueryProcessor
/*     */ {
/*  29 */   private static final Logger log = LoggerFactory.getLogger(RelevantFieldQueryProcessor.class);
/*     */   final ObjectMapper mapper;
/*     */   final AbstractScorer scorer;
/*     */   
/*     */   public RelevantFieldQueryProcessor()
/*     */   {
/*  35 */     this.mapper = Reader.DEFAULT_JSON_MAPPER;
/*  36 */     this.scorer = new LogisticScorer();
/*     */   }
/*     */   
/*     */   public JsonNode query(EventService eventService, int requestVersion, String accountName, String eventType, String query)
/*     */   {
/*  41 */     validateQuery(query);
/*     */     
/*  43 */     JsonNode result = null;
/*     */     try {
/*  45 */       ByteArrayOutputStream os = new ByteArrayOutputStream();
/*  46 */       eventService.searchEvents(requestVersion, accountName, eventType, query, os);
/*  47 */       String response = new String(os.toByteArray(), Charsets.UTF_8);
/*  48 */       os.close();
/*     */       
/*  50 */       Map<String, SignificantTermsField> fields = SignificantTermsUtils.getFields(response);
/*  51 */       updateMetrics(fields);
/*     */       
/*  53 */       result = this.mapper.valueToTree(fields);
/*     */     } catch (Exception e) {
/*  55 */       Throwables.propagate(e);
/*     */     }
/*  57 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void validateQuery(String query)
/*     */   {
/*  65 */     if (!query.toLowerCase().contains("significant_terms")) {
/*  66 */       throw new RuntimeException("Query [" + query + "] is not a valid significant terms aggregation query.");
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateMetrics(Map<String, SignificantTermsField> fields) {
/*  71 */     for (SignificantTermsField field : fields.values()) {
/*  72 */       subsetSize = field.getDocCount();
/*  73 */       for (JsonNode term : field.getTerms()) {
/*  74 */         double rawScore = term.get("score").asDouble();
/*  75 */         long subsetFreq = term.get("doc_count").asLong();
/*     */         
/*  77 */         double foregroundProbability = getForegroundProbability(subsetFreq, subsetSize);
/*  78 */         double backgroundProbability = getBackgroundProbability(rawScore, foregroundProbability);
/*     */         
/*  80 */         double normalizedScore = this.scorer.normalize(rawScore);
/*  81 */         ((ObjectNode)term).put("score", roundScore(normalizedScore));
/*     */         
/*     */ 
/*  84 */         ((ObjectNode)term).put("background_percent", backgroundProbability);
/*  85 */         ((ObjectNode)term).put("foreground_percent", foregroundProbability);
/*     */       }
/*     */     }
/*     */     long subsetSize;
/*     */   }
/*     */   
/*     */   private int roundScore(double score) {
/*  92 */     return (int)(score + 0.5D);
/*     */   }
/*     */   
/*     */   private double getForegroundProbability(long subsetFreq, long subsetSize) {
/*  96 */     if (subsetSize == 0L) {
/*  97 */       return 0.0D;
/*     */     }
/*  99 */     return subsetFreq / subsetSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private double getBackgroundProbability(double score, double foregroundProbability)
/*     */   {
/* 106 */     double denominator = score + foregroundProbability;
/* 107 */     if (denominator == 0.0D) {
/* 108 */       return 0.0D;
/*     */     }
/* 110 */     return foregroundProbability * foregroundProbability / denominator;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/relevantfields/RelevantFieldQueryProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */