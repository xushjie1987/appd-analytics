/*     */ package com.appdynamics.analytics.processor.event.stage;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.scripts.MergeUpsertScript;
/*     */ import com.appdynamics.analytics.processor.event.upsert.MutableUpsert;
/*     */ import com.appdynamics.analytics.processor.event.upsert.Upsert;
/*     */ import com.appdynamics.analytics.processor.event.upsert.UpsertCodec;
/*     */ import com.appdynamics.common.io.payload.Bytes;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.exception.PermanentException;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class RelatedUpserts
/*     */ {
/*  40 */   private static final Logger log = LoggerFactory.getLogger(RelatedUpserts.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private final String correlationId;
/*     */   
/*     */ 
/*     */   private final LinkedHashMap<Pair<String, Integer>, IndexingEvent> indexingEvents;
/*     */   
/*     */ 
/*     */   private final ArrayList<MutableUpsert> upserts;
/*     */   
/*     */ 
/*     */ 
/*     */   RelatedUpserts(String correlationId)
/*     */   {
/*  56 */     this.correlationId = correlationId;
/*  57 */     this.indexingEvents = new LinkedHashMap(4);
/*  58 */     this.upserts = new ArrayList();
/*     */   }
/*     */   
/*     */   public String getCorrelationId() {
/*  62 */     return this.correlationId;
/*     */   }
/*     */   
/*     */   public Map<Pair<String, Integer>, IndexingEvent> getIndexingEvents() {
/*  66 */     return this.indexingEvents;
/*     */   }
/*     */   
/*     */   public List<? extends Upsert> getUpserts() {
/*  70 */     return this.upserts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean add(IndexingEvent indexingEvent, MutableUpsert upsertKey)
/*     */   {
/*  81 */     Pair<String, Integer> batchIdAndPosition = new Pair(upsertKey.getBatchId(), Integer.valueOf(upsertKey.getBatchPosition()));
/*  82 */     if (this.indexingEvents.get(batchIdAndPosition) == null) {
/*  83 */       this.indexingEvents.put(batchIdAndPosition, indexingEvent);
/*  84 */       this.upserts.add(upsertKey);
/*  85 */       return true;
/*     */     }
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   int size() {
/*  91 */     int size = this.indexingEvents.size();
/*  92 */     Preconditions.checkArgument(size == this.upserts.size());
/*  93 */     return size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Upsert merge()
/*     */     throws IOException
/*     */   {
/* 103 */     int size = size();
/* 104 */     log.debug("Coalescing [{}] upserts with correlation id [{}]", Integer.valueOf(size), this.correlationId);
/*     */     
/* 106 */     String accountName = null;
/* 107 */     String eventType = null;
/* 108 */     String idPath = null;
/* 109 */     String csvMergeFields = null;
/* 110 */     LinkedHashSet<Object> mergeFields = null;
/* 111 */     HashMap<Object, Object> mergedSource = new HashMap();
/* 112 */     LinkedHashSet<String> appliedRequestIds = new LinkedHashSet(size);
/*     */     
/* 114 */     for (int i = 0; i < size; i++) {
/* 115 */       MutableUpsert upsert = (MutableUpsert)this.upserts.get(i);
/* 116 */       String batchRequestId = UpsertCodec.makeBatchRequestId(upsert);
/* 117 */       if (!appliedRequestIds.add(batchRequestId))
/*     */       {
/* 119 */         log.debug("Ignored upsert with batch request id [{}] and correlation id [{}] as it appears to be a duplicate within the current in-memory buffer", batchRequestId, this.correlationId);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */         try
/*     */         {
/*     */ 
/* 127 */           if (i == 0) {
/* 128 */             accountName = upsert.getAccountName();
/* 129 */             eventType = upsert.getEventType();
/* 130 */             idPath = upsert.getCorrelationIdField();
/* 131 */             csvMergeFields = upsert.getCsvMergeFields();
/* 132 */             List<?> csv = UpsertCodec.splitCsvMergeFields(csvMergeFields);
/* 133 */             mergeFields = new LinkedHashSet(csv);
/*     */           }
/*     */           
/* 136 */           Map<String, Object> sourceAsMap = readJson(upsert.getBytes());
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 141 */           new MergeUpsertScript(Integer.toString(i), mergeFields, sourceAsMap, mergedSource).run();
/*     */         } catch (IOException e) {
/* 143 */           throw new PermanentException("An error occurred while attempting to merge request id [" + batchRequestId + "] which is" + " one of [" + size + "] upserts related to [" + this.correlationId + "]", e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 150 */     List<String> appliedUpdates = (List)mergedSource.get("_appliedUpdates");
/* 151 */     int appliedUpdatesSize = appliedUpdates.size();
/* 152 */     if (appliedUpdatesSize != size)
/*     */     {
/* 154 */       log.info("Coalescing [{}] upserts with correlation id [{}] resulted in a merged document with [{}] unique items", new Object[] { Integer.valueOf(size), this.correlationId, Integer.valueOf(appliedUpdatesSize) });
/*     */     }
/*     */     
/*     */ 
/* 158 */     mergedSource.put("_appliedUpdates", new ArrayList(appliedRequestIds));
/*     */     
/*     */ 
/* 161 */     byte[] mergedUpsertBytes = writeAsJsonArrayLengthOne(mergedSource);
/* 162 */     List<MutableUpsert> singleMergedUpsertList = UpsertCodec.decode(accountName, eventType, idPath, csvMergeFields, mergedUpsertBytes, 0, mergedUpsertBytes.length);
/*     */     
/*     */ 
/* 165 */     MutableUpsert singleMergedUpsert = (MutableUpsert)singleMergedUpsertList.get(0);
/*     */     
/*     */ 
/* 168 */     String summaryBatchRequestId = UpsertCodec.makeSummaryBatchRequestId(appliedRequestIds);
/* 169 */     singleMergedUpsert.setBatchId(summaryBatchRequestId);
/* 170 */     singleMergedUpsert.setBatchPosition(0);
/* 171 */     return singleMergedUpsert;
/*     */   }
/*     */   
/*     */   private static Map<String, Object> readJson(Bytes bytes) throws IOException {
/* 175 */     return (Map)Reader.DEFAULT_JSON_MAPPER.readValue(bytes.getArray(), bytes.getOffset(), bytes.getLength(), JsonMapTypeReference.INSTANCE);
/*     */   }
/*     */   
/*     */   private static byte[] writeAsJsonArrayLengthOne(Map<Object, Object> mergedSource) throws JsonProcessingException
/*     */   {
/* 180 */     ArrayList<Map<Object, Object>> arrayOfUpserts = new ArrayList(1);
/* 181 */     arrayOfUpserts.add(mergedSource);
/* 182 */     return Reader.DEFAULT_JSON_MAPPER.writeValueAsBytes(arrayOfUpserts);
/*     */   }
/*     */   
/*     */   private static class JsonMapTypeReference extends TypeReference<Map<String, Object>> {
/* 186 */     static final JsonMapTypeReference INSTANCE = new JsonMapTypeReference();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/stage/RelatedUpserts.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */