/*    */ package com.appdynamics.analytics.processor.event.stage;
/*    */ 
/*    */ import com.appdynamics.analytics.message.api.MessagePack;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.processor.admin.ActionType;
/*    */ import com.appdynamics.analytics.processor.admin.Locator;
/*    */ import com.appdynamics.analytics.processor.event.EventService;
/*    */ import com.appdynamics.analytics.processor.event.dto.IndexingRequest;
/*    */ import com.appdynamics.analytics.processor.event.dto.IndexingRequest.Factory;
/*    */ import com.appdynamics.analytics.processor.event.exception.BulkFailureException;
/*    */ import com.appdynamics.common.util.exception.TransientException;
/*    */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*    */ import com.codahale.metrics.Meter;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class EventIndexingStage
/*    */   extends AbstractIndexingStage
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(EventIndexingStage.class);
/*    */   
/*    */   final IndexingRequest.Factory indexingRequestFactory;
/*    */   
/*    */ 
/*    */   EventIndexingStage(PipelineStageParameters<Void> parameters, EventService eventService, Locator locator, MeteredHealthCheck healthCheck, IndexingRequest.Factory indexingRequestFactory)
/*    */   {
/* 33 */     super(parameters, eventService, locator, healthCheck);
/* 34 */     this.indexingRequestFactory = indexingRequestFactory;
/*    */   }
/*    */   
/*    */ 
/*    */   public void process(MessagePack<String, IndexingEvent> input)
/*    */   {
/*    */     IndexingEvent message;
/*    */     
/* 42 */     while ((message = (IndexingEvent)input.poll()) != null) {
/* 43 */       switch (message.getType()) {
/*    */       case EVENT_PUBLISH: 
/* 45 */         handlePublish(message, input);
/* 46 */         break;
/*    */       default: 
/* 48 */         Exception e = new UnsupportedOperationException("This message type [" + message.getType().name() + "] is not supported");
/*    */         
/* 50 */         handlePermanentException("unknown", "unknown", "unknown", message, input, e);
/*    */       }
/*    */       
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private void handlePublish(IndexingEvent message, MessagePack<String, IndexingEvent> messagePack)
/*    */   {
/*    */     IndexingRequest req;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     try
/*    */     {
/* 70 */       req = this.indexingRequestFactory.fromBytes(message.getBody());
/*    */     } catch (RuntimeException e) {
/* 72 */       this.healthCheck.getMeterError().mark();
/*    */       
/*    */ 
/* 75 */       handlePermanentException("unknown", "unknown", "unknown", message, messagePack, e);
/* 76 */       return;
/*    */     }
/*    */     
/* 79 */     String clusterName = null;
/*    */     try {
/* 81 */       clusterName = this.locator.findActiveClusterName(req.getAccountName());
/* 82 */       this.eventService.publishEvents(2, req.getAccountName(), req.getEventType(), req.getPayload(), req.getOffset(), req.getLength(), message.getId());
/*    */       
/* 84 */       this.healthCheck.getMeterSuccess().mark();
/*    */     } catch (TransientException e) {
/* 86 */       this.healthCheck.getMeterError().mark();
/* 87 */       handleTransientException(req.getAccountName(), req.getEventType(), clusterName, message, messagePack, e);
/*    */     } catch (BulkFailureException e) {
/* 89 */       this.healthCheck.getMeterError().mark();
/* 90 */       handleBulkFailureException(req.getAccountName(), req.getEventType(), clusterName, message, messagePack, e);
/*    */     } catch (Exception e) {
/* 92 */       this.healthCheck.getMeterError().mark();
/* 93 */       handlePermanentException(req.getAccountName(), req.getEventType(), clusterName, message, messagePack, e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/stage/EventIndexingStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */