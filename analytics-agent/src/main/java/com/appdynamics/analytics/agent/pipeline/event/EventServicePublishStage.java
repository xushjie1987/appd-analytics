/*     */ package com.appdynamics.analytics.agent.pipeline.event;
/*     */ 
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*     */ import com.appdynamics.analytics.shared.rest.client.eventservice.EventServiceClient;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.ClientException;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*     */ import com.appdynamics.common.util.misc.ThreadLocalObjects;
/*     */ import com.codahale.metrics.Meter;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventServicePublishStage
/*     */   extends AbstractPipelineStage<MessagePack<String, String>, MessagePack<String, String>>
/*     */ {
/*  24 */   private static final Logger log = LoggerFactory.getLogger(EventServicePublishStage.class);
/*     */   
/*     */   final EventServicePublishStageFactoryConfig factoryConfig;
/*     */   
/*     */   final Meter successMeter;
/*     */   
/*     */   final Meter errorMeter;
/*     */   
/*     */   final EventServiceClient client;
/*     */   
/*     */   final String accountName;
/*     */   
/*     */   final String accessKey;
/*     */   final boolean eventBatchingEnabled;
/*     */   
/*     */   public EventServicePublishStage(PipelineStageParameters<MessagePack<String, String>> parameters, EventServicePublishStageFactoryConfig factoryConfig, MeteredHealthCheck healthCheck, EventServiceClient eventServiceClient, String accountName, String accessKey)
/*     */   {
/*  41 */     super(parameters);
/*  42 */     this.factoryConfig = factoryConfig;
/*  43 */     this.successMeter = healthCheck.getMeterSuccess();
/*  44 */     this.errorMeter = healthCheck.getMeterError();
/*  45 */     this.client = eventServiceClient;
/*  46 */     this.accountName = accountName;
/*  47 */     this.accessKey = accessKey;
/*  48 */     this.eventBatchingEnabled = factoryConfig.isEventBatchingEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */   public void process(MessagePack<String, String> messagePack)
/*     */   {
/*  54 */     if (messagePack.size() == 0) {
/*  55 */       invokeNext(messagePack);
/*     */     }
/*     */     
/*  58 */     boolean trace = log.isTraceEnabled();
/*     */     
/*  60 */     if (this.eventBatchingEnabled)
/*     */     {
/*     */ 
/*     */ 
/*  64 */       boolean isFirst = true;
/*     */       
/*  66 */       StringBuilder messageBuilder = ThreadLocalObjects.stringBuilder();
/*     */       try {
/*  68 */         messageBuilder.append("[");
/*  69 */         String message; while ((message = (String)messagePack.poll()) != null) {
/*  70 */           if (!isFirst) {
/*  71 */             messageBuilder.append(",");
/*     */           } else {
/*  73 */             isFirst = false;
/*     */           }
/*     */           
/*  76 */           messageBuilder.append(message);
/*     */         }
/*  78 */         messageBuilder.append("]");
/*     */         
/*  80 */         String batchedMessage = messageBuilder.toString();
/*  81 */         sendMessage(messagePack, batchedMessage, trace);
/*     */       } finally {
/*  83 */         messageBuilder.setLength(0);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       String message;
/*  89 */       while ((message = (String)messagePack.poll()) != null) {
/*  90 */         sendMessage(messagePack, message, trace);
/*     */       }
/*     */     }
/*     */     
/*  94 */     invokeNext(messagePack);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void sendMessage(MessagePack<String, String> messagePack, String message, boolean traceEnabled)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       if (traceEnabled) {
/* 108 */         log.trace("About to publish [\n{}\n]", message);
/*     */       }
/*     */       
/* 111 */       if (this.factoryConfig.isUpsert()) {
/* 112 */         this.client.upsertEvents(this.accountName, this.accessKey, (String)messagePack.getSourceId(), message, this.factoryConfig.getIdPath(), this.factoryConfig.getMergeFields());
/*     */       }
/*     */       else {
/* 115 */         this.client.publishEvents(this.accountName, this.accessKey, (String)messagePack.getSourceId(), message);
/*     */       }
/* 117 */       this.successMeter.mark();
/*     */     } catch (RestException e) {
/* 119 */       this.errorMeter.mark();
/* 120 */       EventServiceHelper.handleRestError(log, messagePack, message, this.eventBatchingEnabled, e);
/*     */     } catch (ClientException e) {
/* 122 */       this.errorMeter.mark();
/* 123 */       EventServiceHelper.handleTransientError(log, messagePack, message, this.eventBatchingEnabled, e, e.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/event/EventServicePublishStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */