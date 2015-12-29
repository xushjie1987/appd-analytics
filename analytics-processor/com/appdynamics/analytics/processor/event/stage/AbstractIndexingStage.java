/*     */ package com.appdynamics.analytics.processor.event.stage;
/*     */ 
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*     */ import com.appdynamics.analytics.processor.admin.Locator;
/*     */ import com.appdynamics.analytics.processor.event.EventService;
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailure;
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailureException;
/*     */ import com.appdynamics.common.util.exception.PermanentException;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
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
/*     */ abstract class AbstractIndexingStage
/*     */   extends AbstractPipelineStage<MessagePack<String, IndexingEvent>, Void>
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(AbstractIndexingStage.class);
/*     */   
/*     */   static final String UNKNOWN_VALUE = "unknown";
/*     */   
/*     */   final EventService eventService;
/*     */   final MeteredHealthCheck healthCheck;
/*     */   final Locator locator;
/*     */   
/*     */   AbstractIndexingStage(PipelineStageParameters<Void> parameters, EventService eventService, Locator locator, MeteredHealthCheck healthCheck)
/*     */   {
/*  36 */     super(parameters);
/*  37 */     this.eventService = eventService;
/*  38 */     this.locator = locator;
/*  39 */     this.healthCheck = healthCheck;
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
/*     */   final void handleBulkFailureException(String accountName, String eventType, String clusterName, IndexingEvent message, MessagePack<String, IndexingEvent> messagePack, BulkFailureException exception)
/*     */   {
/*  65 */     log.warn("Encountered BulkFailureException while processing message with Id [{}]. The list of errors is: {}", message.getId(), exception.getFailures());
/*     */     
/*  67 */     boolean transientFailuresExist = false;
/*  68 */     for (BulkFailure failure : exception.getFailures()) {
/*  69 */       if (failure.isTransient()) {
/*  70 */         transientFailuresExist = true;
/*  71 */         break;
/*     */       }
/*     */     }
/*     */     
/*  75 */     if (transientFailuresExist) {
/*  76 */       handleTransientException(accountName, eventType, clusterName, message, messagePack, exception);
/*     */     } else {
/*  78 */       handlePermanentException(accountName, eventType, clusterName, message, messagePack, exception);
/*     */     }
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
/*     */   final void handleTransientException(String accountName, String eventType, String clusterName, IndexingEvent message, MessagePack<String, IndexingEvent> messagePack, Throwable throwable)
/*     */   {
/* 103 */     messagePack.returnUndelivered(message);
/* 104 */     log.warn("Could not perform [{}] for message with Id [{}] to cluster [{}], for account [{}] and eventType [{}] due to a transient exception. The message has been placed back onto the message pack queue so that it can be retried.", new Object[] { message.getType(), message.getId(), clusterName, accountName, eventType, throwable });
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
/*     */ 
/*     */   final void handlePermanentException(String accountName, String eventType, String clusterName, IndexingEvent message, MessagePack<String, IndexingEvent> messagePack, Throwable throwable)
/*     */   {
/* 134 */     messagePack.returnUndelivered(message, new PermanentException(throwable));
/* 135 */     log.warn("Could not perform [{}] for message with Id [{}] to cluster [{}], for account [{}] and eventType [{}] due to a permanent exception. The message will not be retried.", new Object[] { message.getType(), message.getId(), clusterName, accountName, eventType, throwable });
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/stage/AbstractIndexingStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */