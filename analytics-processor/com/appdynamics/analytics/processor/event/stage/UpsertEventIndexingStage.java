/*     */ package com.appdynamics.analytics.processor.event.stage;
/*     */ 
/*     */ import com.appdynamics.analytics.message.api.ExplicitCommitable;
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.analytics.processor.admin.ActionType;
/*     */ import com.appdynamics.analytics.processor.admin.Locator;
/*     */ import com.appdynamics.analytics.processor.event.EventService;
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailure;
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailureException;
/*     */ import com.appdynamics.analytics.processor.event.upsert.MutableUpsert;
/*     */ import com.appdynamics.analytics.processor.event.upsert.Upsert;
/*     */ import com.appdynamics.analytics.processor.event.upsert.UpsertCodec;
/*     */ import com.appdynamics.common.io.payload.Bytes;
/*     */ import com.appdynamics.common.util.datetime.Ticker;
/*     */ import com.appdynamics.common.util.exception.PermanentException;
/*     */ import com.appdynamics.common.util.exception.TransientException;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*     */ import com.appdynamics.common.util.misc.Collections;
/*     */ import com.codahale.metrics.Histogram;
/*     */ import com.codahale.metrics.Meter;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
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
/*     */ class UpsertEventIndexingStage
/*     */   extends AbstractIndexingStage
/*     */ {
/*  46 */   private static final Logger log = LoggerFactory.getLogger(UpsertEventIndexingStage.class);
/*     */   
/*     */ 
/*     */   static final int NUM_BYTES_FIXED_OVERHEAD = 80;
/*     */   
/*     */ 
/*     */   private final Histogram mergeCountHistogram;
/*     */   
/*     */ 
/*     */   private final FlushStrategy flushStrategy;
/*     */   
/*     */ 
/*     */   private final HashMap<String, RelatedUpserts> bufferedRelatedUpserts;
/*     */   
/*     */ 
/*     */   private int numAccumulatedUpserts;
/*     */   
/*     */ 
/*     */   private long byteSizeOfAccumulatedUpserts;
/*     */   
/*     */ 
/*     */   UpsertEventIndexingStage(PipelineStageParameters<Void> parameters, EventService eventService, Locator locator, MeteredHealthCheck healthCheck, Histogram mergeCountHistogram, FlushStrategy flushStrategy)
/*     */   {
/*  69 */     super(parameters, eventService, locator, healthCheck);
/*  70 */     this.mergeCountHistogram = mergeCountHistogram;
/*  71 */     this.flushStrategy = flushStrategy;
/*  72 */     this.bufferedRelatedUpserts = new HashMap();
/*     */   }
/*     */   
/*     */ 
/*     */   public void process(MessagePack<String, IndexingEvent> input)
/*     */   {
/*     */     IndexingEvent event;
/*     */     
/*  80 */     while ((event = (IndexingEvent)input.poll()) != null) {
/*  81 */       switch (event.getType()) {
/*     */       case EVENT_UPSERT: 
/*  83 */         MutableUpsert upsertKeyPart = decodeKeyAsUpsert(event, input);
/*     */         
/*  85 */         if (upsertKeyPart != null)
/*     */         {
/*  87 */           RelatedUpserts relatedUpserts = (RelatedUpserts)this.bufferedRelatedUpserts.get(upsertKeyPart.getCorrelationId());
/*     */           
/*     */ 
/*  90 */           if (relatedUpserts == null) {
/*  91 */             relatedUpserts = new RelatedUpserts(upsertKeyPart.getCorrelationId());
/*  92 */             this.bufferedRelatedUpserts.put(relatedUpserts.getCorrelationId(), relatedUpserts);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*  97 */           Bytes keyBytes = event.getKey();
/*  98 */           Bytes bodyBytes = event.getBodyBytes();
/*     */           
/* 100 */           upsertKeyPart.setBytes(bodyBytes);
/* 101 */           if (relatedUpserts.add(event, upsertKeyPart)) {
/* 102 */             this.numAccumulatedUpserts += 1;
/*     */             
/*     */ 
/* 105 */             this.byteSizeOfAccumulatedUpserts += 80L;
/* 106 */             this.byteSizeOfAccumulatedUpserts += keyBytes.getLength() - keyBytes.getOffset();
/* 107 */             this.byteSizeOfAccumulatedUpserts += bodyBytes.getLength() - bodyBytes.getOffset();
/*     */           } }
/* 109 */         break;
/*     */       
/*     */ 
/*     */       default: 
/* 113 */         Exception e = new UnsupportedOperationException("This message type [" + event.getType().name() + "] is not supported");
/*     */         
/* 115 */         handlePermanentException("unknown", "unknown", "unknown", event, input, e);
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 120 */     tryMergeAndFlush(input);
/*     */   }
/*     */   
/*     */   private MutableUpsert decodeKeyAsUpsert(IndexingEvent message, MessagePack<String, IndexingEvent> messagePack)
/*     */   {
/*     */     try {
/* 126 */       return UpsertCodec.decodeKey(message.getKey());
/*     */     } catch (RuntimeException e) {
/* 128 */       this.healthCheck.getMeterError().mark();
/*     */       
/*     */ 
/* 131 */       handlePermanentException("unknown", "unknown", "unknown", message, messagePack, e);
/*     */     }
/* 133 */     return null;
/*     */   }
/*     */   
/*     */   private void tryMergeAndFlush(MessagePack<String, IndexingEvent> messagePack) {
/* 137 */     log.debug("There are now [{}] accumulated upserts that amount to approximately [{}] bytes", Integer.valueOf(this.numAccumulatedUpserts), Long.valueOf(this.byteSizeOfAccumulatedUpserts));
/*     */     
/* 139 */     if (!this.flushStrategy.timeToFlush(this.numAccumulatedUpserts, this.byteSizeOfAccumulatedUpserts)) {
/* 140 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 146 */     RelatedUpserts[] allRelatedUpserts = getAllRelatedUpserts();
/* 147 */     ArrayList<Upsert> mergedUpserts = new ArrayList(allRelatedUpserts.length);
/*     */     
/* 149 */     log.debug("Preparing to merge [{}] groups of related upserts", Integer.valueOf(allRelatedUpserts.length));
/* 150 */     for (RelatedUpserts relatedUpserts : allRelatedUpserts) {
/*     */       try {
/* 152 */         int mergeCount = relatedUpserts.size();
/* 153 */         Upsert mergedUpsert = relatedUpserts.merge();
/* 154 */         mergedUpserts.add(mergedUpsert);
/* 155 */         this.mergeCountHistogram.update(mergeCount);
/*     */       } catch (IOException|RuntimeException e) {
/* 157 */         Exception x = new PermanentException("Error occurred while attempting to merge upserts related to correlation id [" + relatedUpserts.getCorrelationId() + "]", e);
/*     */         
/* 159 */         List<RelatedUpserts> list = collectPermanentUpsertError(null, relatedUpserts.getCorrelationId());
/* 160 */         handleIfPermanentUpsertErrors(list, x, messagePack);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 165 */     List<RelatedUpserts> upsertsWithPermErrors = null;
/* 166 */     Throwable throwable = null;
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 172 */       if (mergedUpserts.size() > 0) {
/* 173 */         this.eventService.upsertEvents(2, mergedUpserts);
/* 174 */         this.healthCheck.getMeterSuccess().mark(mergedUpserts.size());
/*     */         
/* 176 */         log.debug("[{}] successful upserts were made", Integer.valueOf(mergedUpserts.size()));
/*     */       }
/* 178 */       ((ExplicitCommitable)messagePack).commit();
/*     */       
/* 180 */       this.bufferedRelatedUpserts.clear();
/* 181 */       this.numAccumulatedUpserts = 0;
/* 182 */       this.byteSizeOfAccumulatedUpserts = 0L;
/*     */     } catch (TransientException e) {
/* 184 */       throwable = e;
/*     */       
/* 186 */       this.healthCheck.getMeterError().mark(this.bufferedRelatedUpserts.size());
/*     */     } catch (BulkFailureException e) {
/* 188 */       throwable = e;
/*     */       
/* 190 */       HashSet<String> correlationIdsWithTransientErrs = new HashSet(e.getFailures().size());
/* 191 */       int numPermanentErrors = 0;
/* 192 */       for (BulkFailure failure : e.getFailures()) {
/* 193 */         if (failure.isTransient()) {
/* 194 */           this.healthCheck.getMeterError().mark();
/*     */           
/* 196 */           correlationIdsWithTransientErrs.add(failure.getCorrelationId());
/*     */         } else {
/* 198 */           numPermanentErrors++;
/* 199 */           upsertsWithPermErrors = collectPermanentUpsertError(upsertsWithPermErrors, failure.getCorrelationId());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 205 */       int before = this.bufferedRelatedUpserts.size();
/* 206 */       if (this.bufferedRelatedUpserts.keySet().retainAll(correlationIdsWithTransientErrs)) {
/* 207 */         int numSuccess = before - correlationIdsWithTransientErrs.size();
/* 208 */         this.healthCheck.getMeterSuccess().mark(numSuccess);
/* 209 */         log.info("In spite of the error, [" + numSuccess + "] upserts" + " were processed successfully, while there were [" + numPermanentErrors + "] permanent" + " errors and [" + correlationIdsWithTransientErrs.size() + "] transient errors", e);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 214 */       if (correlationIdsWithTransientErrs.isEmpty())
/*     */       {
/*     */ 
/* 217 */         ((ExplicitCommitable)messagePack).commit();
/*     */       }
/*     */     }
/*     */     catch (Throwable t) {
/* 221 */       throwable = t;
/*     */       
/*     */ 
/* 224 */       for (RelatedUpserts ru : allRelatedUpserts) {
/* 225 */         upsertsWithPermErrors = collectPermanentUpsertError(upsertsWithPermErrors, ru.getCorrelationId());
/*     */       }
/*     */       
/*     */ 
/* 229 */       throw t;
/*     */     } finally {
/* 231 */       handleIfPermanentUpsertErrors(upsertsWithPermErrors, throwable, messagePack);
/* 232 */       handleIfTransientUpsertErrors(throwable, messagePack);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private RelatedUpserts[] getAllRelatedUpserts()
/*     */   {
/* 240 */     RelatedUpserts[] allRelatedUpserts = new RelatedUpserts[this.bufferedRelatedUpserts.size()];
/* 241 */     int i = 0;
/*     */     
/* 243 */     for (RelatedUpserts relatedUpserts : this.bufferedRelatedUpserts.values()) {
/* 244 */       allRelatedUpserts[(i++)] = relatedUpserts;
/*     */     }
/* 246 */     return allRelatedUpserts;
/*     */   }
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
/*     */   private List<RelatedUpserts> collectPermanentUpsertError(@Nullable List<RelatedUpserts> upsertsWithPermErrors, String upsertCorrelationId)
/*     */   {
/* 260 */     RelatedUpserts relatedUpserts = (RelatedUpserts)this.bufferedRelatedUpserts.remove(upsertCorrelationId);
/*     */     
/* 262 */     upsertsWithPermErrors = Collections.castOrNewList(upsertsWithPermErrors);
/* 263 */     upsertsWithPermErrors.add(relatedUpserts);
/*     */     
/* 265 */     this.healthCheck.getMeterError().mark();
/*     */     
/* 267 */     return upsertsWithPermErrors;
/*     */   }
/*     */   
/*     */ 
/*     */   private void handleIfPermanentUpsertErrors(@Nullable List<RelatedUpserts> upsertsWithPermErrors, Throwable throwable, MessagePack<String, IndexingEvent> messagePack)
/*     */   {
/* 273 */     if (upsertsWithPermErrors == null) {
/* 274 */       return;
/*     */     }
/*     */     
/*     */ 
/* 278 */     for (RelatedUpserts failedRelatedUpsert : upsertsWithPermErrors) {
/* 279 */       upserts = failedRelatedUpsert.getUpserts();
/* 280 */       i = 0;
/* 281 */       for (IndexingEvent indexingEvent : failedRelatedUpsert.getIndexingEvents().values()) {
/* 282 */         Upsert upsert = (Upsert)upserts.get(i);
/* 283 */         String clusterName = getActiveClusterName(upsert.getAccountName());
/* 284 */         handlePermanentException(upsert.getAccountName(), upsert.getEventType(), clusterName, indexingEvent, messagePack, throwable);
/*     */         
/* 286 */         i++;
/*     */       } }
/*     */     List<? extends Upsert> upserts;
/*     */     int i;
/* 290 */     upsertsWithPermErrors.clear();
/*     */   }
/*     */   
/*     */   private void handleIfTransientUpsertErrors(Throwable throwable, MessagePack<String, IndexingEvent> messagePack) {
/* 294 */     if (this.bufferedRelatedUpserts.size() == 0) {
/* 295 */       return;
/*     */     }
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
/*     */ 
/* 312 */     for (RelatedUpserts relatedUpserts : this.bufferedRelatedUpserts.values()) {
/* 313 */       upserts = relatedUpserts.getUpserts();
/* 314 */       i = 0;
/* 315 */       for (IndexingEvent indexingEvent : relatedUpserts.getIndexingEvents().values()) {
/* 316 */         Upsert upsert = (Upsert)upserts.get(i);
/* 317 */         String clusterName = getActiveClusterName(upsert.getAccountName());
/* 318 */         handleTransientException(upsert.getAccountName(), upsert.getEventType(), clusterName, indexingEvent, messagePack, throwable);
/*     */       }
/*     */     }
/*     */     List<? extends Upsert> upserts;
/*     */     int i;
/* 323 */     this.bufferedRelatedUpserts.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getActiveClusterName(String accountName)
/*     */   {
/*     */     try
/*     */     {
/* 337 */       return this.locator.findActiveClusterName(accountName);
/*     */     } catch (RuntimeException e) {
/* 339 */       log.warn("Failed to retrieve cluster name for account [{}]", accountName, e); }
/* 340 */     return "unknown";
/*     */   }
/*     */   
/*     */ 
/*     */   static class DefaultFlushStrategy
/*     */     implements UpsertEventIndexingStage.FlushStrategy
/*     */   {
/*     */     final Ticker ticker;
/*     */     final long maxTimeWithoutUpsertFlushMillis;
/*     */     final long maxBytesAccumulatedUpserts;
/*     */     private long lastFlushAtMillis;
/*     */     
/*     */     public String toString()
/*     */     {
/* 354 */       return "UpsertEventIndexingStage.DefaultFlushStrategy(ticker=" + getTicker() + ", maxTimeWithoutUpsertFlushMillis=" + getMaxTimeWithoutUpsertFlushMillis() + ", maxBytesAccumulatedUpserts=" + getMaxBytesAccumulatedUpserts() + ", lastFlushAtMillis=" + getLastFlushAtMillis() + ")";
/*     */     }
/*     */     
/* 357 */     public Ticker getTicker() { return this.ticker; }
/* 358 */     public long getMaxTimeWithoutUpsertFlushMillis() { return this.maxTimeWithoutUpsertFlushMillis; }
/* 359 */     public long getMaxBytesAccumulatedUpserts() { return this.maxBytesAccumulatedUpserts; }
/* 360 */     public long getLastFlushAtMillis() { return this.lastFlushAtMillis; }
/*     */     
/*     */     DefaultFlushStrategy(Ticker ticker, long maxTimeWithoutUpsertFlushMillis, long maxBytesAccumulatedUpserts) {
/* 363 */       this.ticker = ticker;
/* 364 */       this.maxTimeWithoutUpsertFlushMillis = maxTimeWithoutUpsertFlushMillis;
/* 365 */       this.maxBytesAccumulatedUpserts = maxBytesAccumulatedUpserts;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean timeToFlush(int numAccumulatedUpserts, long byteSizeOfAccumulatedUpserts)
/*     */     {
/* 375 */       long now = this.ticker.currentTimeMillis();
/*     */       
/* 377 */       if (byteSizeOfAccumulatedUpserts >= this.maxBytesAccumulatedUpserts) {
/* 378 */         this.lastFlushAtMillis = now;
/* 379 */         return true; }
/* 380 */       if ((numAccumulatedUpserts > 0) && (now - this.lastFlushAtMillis >= this.maxTimeWithoutUpsertFlushMillis)) {
/* 381 */         this.lastFlushAtMillis = now;
/* 382 */         return true;
/*     */       }
/*     */       
/* 385 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract interface FlushStrategy
/*     */   {
/*     */     public abstract boolean timeToFlush(int paramInt, long paramLong);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/stage/UpsertEventIndexingStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */