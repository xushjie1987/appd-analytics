/*     */ package com.appdynamics.analytics.agent.field;
/*     */ 
/*     */ import com.appdynamics.analytics.agent.pipeline.dynamic.LogSourceJobFileParser;
/*     */ import com.appdynamics.analytics.agent.source.LogSource;
/*     */ import com.appdynamics.analytics.agent.source.LogSources;
/*     */ import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.annotation.concurrent.GuardedBy;
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
/*     */ 
/*     */ public class ExtractedFieldsPoller
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(ExtractedFieldsPoller.class);
/*     */   
/*     */ 
/*     */   private final LogSourceJobFileParser jobFileParser;
/*     */   
/*     */ 
/*     */   @GuardedBy("this")
/*     */   private final LogSources logSources;
/*     */   
/*     */ 
/*     */   private final ExtractedFieldsManager extractedFieldsManager;
/*     */   
/*     */ 
/*     */   private final ScheduledExecutorService backgroundTpe;
/*     */   
/*     */   private final long pollIntervalMillis;
/*     */   
/*     */   private final long initialDelayMillis;
/*     */   
/*     */ 
/*     */   public ExtractedFieldsPoller(LogSourceJobFileParser jobFileParser, LogSources logSources, ExtractedFieldsManager extractedFieldsManager, long pollIntervalMillis, long initialDelayMillis)
/*     */   {
/*  60 */     this.jobFileParser = jobFileParser;
/*  61 */     this.logSources = logSources;
/*  62 */     this.extractedFieldsManager = extractedFieldsManager;
/*  63 */     this.initialDelayMillis = initialDelayMillis;
/*  64 */     this.pollIntervalMillis = pollIntervalMillis;
/*  65 */     this.backgroundTpe = Executors.newScheduledThreadPool(1, ConcurrencyHelper.newDaemonThreadFactory("extracted-fields-poller-%d"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @PostConstruct
/*     */   void onStart()
/*     */   {
/*  73 */     this.backgroundTpe.scheduleWithFixedDelay(new Runnable()
/*     */     {
/*     */ 
/*     */       public void run() {
/*  77 */         ExtractedFieldsPoller.this.onWakeUp(); } }, this.initialDelayMillis, this.pollIntervalMillis, TimeUnit.MILLISECONDS);
/*     */     
/*     */ 
/*  80 */     log.info("Extracted fields poller started with poll interval [{}] ms and initial delay [{}] ms", Long.valueOf(this.pollIntervalMillis), Long.valueOf(this.initialDelayMillis));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void onWakeUp()
/*     */   {
/*  92 */     synchronized (this.logSources) {
/*     */       try {
/*  94 */         List<String> sourceTypes = this.logSources.getLogSourceTypes();
/*  95 */         log.debug("Found the following enabled source types {}", sourceTypes);
/*     */         
/*     */ 
/*  98 */         if ((sourceTypes == null) || (sourceTypes.isEmpty())) {
/*  99 */           return;
/*     */         }
/*     */         
/* 102 */         extractedFieldPatterns = this.extractedFieldsManager.getExtractedFieldPatterns(sourceTypes);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */         for (LogSource logSource : this.logSources.getLogSources()) {
/*     */           try {
/* 110 */             String sourceType = logSource.getSourceType();
/* 111 */             String logSourceName = logSource.getName();
/* 112 */             log.debug("Processing extracted field pattern configurations for log source [{}] with source type [{}]", logSourceName, sourceType);
/*     */             
/*     */ 
/* 115 */             Set<String> currentPatterns = logSource.getExtractedFieldPatterns();
/* 116 */             log.debug("The following extracted field patterns {} are currently being used by log source [{}]", currentPatterns, logSourceName);
/*     */             
/*     */ 
/* 119 */             Set<String> newPatterns = extractedFieldPatterns.get(sourceType);
/* 120 */             log.debug("Retrieved new extracted field patterns {} for source type [{}]", newPatterns, sourceType);
/*     */             
/*     */ 
/* 123 */             if (!Objects.equals(normalize(currentPatterns), normalize(newPatterns))) {
/* 124 */               log.info("The current extracted field patterns for log source [{}] with source type [{}] were different from the newly retrieved set of extracted field patterns. The job pipeline for this log source will be updated with the latest configurations.", logSourceName, sourceType);
/*     */               
/*     */ 
/*     */ 
/* 128 */               PipelineConfiguration pipelineConfiguration = this.jobFileParser.parsePipelineConfigurationFromFileName(logSourceName);
/*     */               
/* 130 */               this.logSources.upsertSourceFromPipeline(logSource.getId(), pipelineConfiguration);
/*     */             }
/*     */           } catch (Exception e) {
/* 133 */             log.warn("An exception occurred while attempting to process extracted fields for log source [{}], therefore its associated pipeline configurations were not updated.", logSource.getName(), e);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/*     */         SetMultimap<String, String> extractedFieldPatterns;
/* 139 */         log.warn("An exception occurred while attempting to poll for extracted field patterns, therefore the pipeline configurations were not updated", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @PreDestroy
/*     */   void onStop()
/*     */   {
/* 150 */     ConcurrencyHelper.stop(this.backgroundTpe, 1, log);
/* 151 */     log.info("Extracted fields poller stopped");
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
/*     */   private Set<String> normalize(Set<String> set)
/*     */   {
/* 165 */     return set == null ? Collections.emptySet() : set;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/field/ExtractedFieldsPoller.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */