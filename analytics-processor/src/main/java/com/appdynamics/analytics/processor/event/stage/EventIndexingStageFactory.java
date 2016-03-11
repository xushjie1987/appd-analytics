/*    */ package com.appdynamics.analytics.processor.event.stage;
/*    */ 
/*    */ import com.appdynamics.analytics.message.api.MessagePack;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.appdynamics.analytics.processor.admin.ActionType;
/*    */ import com.appdynamics.analytics.processor.admin.Locator;
/*    */ import com.appdynamics.analytics.processor.event.EventService;
/*    */ import com.appdynamics.analytics.processor.event.dto.IndexingRequest.Factory;
/*    */ import com.appdynamics.common.util.annotation.Raw;
/*    */ import com.appdynamics.common.util.datetime.Ticker;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*    */ import com.codahale.metrics.Histogram;
/*    */ import com.codahale.metrics.MetricRegistry;
/*    */ import com.google.inject.Inject;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import java.util.EnumMap;
/*    */ import java.util.EnumSet;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventIndexingStageFactory
/*    */   extends AbstractPipelineStageFactory<MessagePack<String, IndexingEvent>, Void, Void, EventIndexingStageConfiguration>
/*    */ {
/*    */   private volatile MetricRegistry metricRegistry;
/*    */   private volatile EventService eventService;
/*    */   private volatile Locator locator;
/*    */   private volatile IndexingRequest.Factory indexingRequestFactory;
/*    */   private volatile EnumMap<ActionType, MeteredHealthCheck> healthChecks;
/*    */   
/*    */   @Inject
/*    */   void postConstruct(Environment environment, ConsolidatedHealthCheck healthCheck, MetricRegistry metricRegistry, @Raw EventService eventService, Locator locator, IndexingRequest.Factory indexingRequestFactory)
/*    */   {
/* 45 */     this.metricRegistry = metricRegistry;
/* 46 */     this.eventService = eventService;
/* 47 */     this.locator = locator;
/* 48 */     this.indexingRequestFactory = indexingRequestFactory;
/*    */     
/* 50 */     this.healthChecks = new EnumMap(ActionType.class);
/* 51 */     for (ActionType actionType : ActionType.values()) {
/* 52 */       MeteredHealthCheck check = new MeteredHealthCheck(getUri() + ":" + actionType.name().toLowerCase(), environment);
/*    */       
/* 54 */       healthCheck.register(check);
/* 55 */       this.healthChecks.put(actionType, check);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public AbstractIndexingStage create(PipelineStageParameters<Void> parameters)
/*    */   {
/* 62 */     EventIndexingStageConfiguration cfg = (EventIndexingStageConfiguration)extract(parameters);
/* 63 */     ActionType actionType = (ActionType)cfg.getTopicTypes().iterator().next();
/*    */     
/* 65 */     MeteredHealthCheck healthCheck = (MeteredHealthCheck)this.healthChecks.get(actionType);
/* 66 */     switch (actionType) {
/*    */     case EVENT_PUBLISH: 
/* 68 */       return new EventIndexingStage(parameters, this.eventService, this.locator, healthCheck, this.indexingRequestFactory);
/*    */     case EVENT_UPSERT: 
/* 70 */       UpsertEventIndexingStage.FlushStrategy fs = new UpsertEventIndexingStage.DefaultFlushStrategy(new Ticker(), cfg.getMaxTimeWithoutUpsertFlush().toMilliseconds(), cfg.getMaxBytesAccumulatedUpserts());
/*    */       
/*    */ 
/* 73 */       Histogram histogram = this.metricRegistry.histogram(cfg.getName() + "-merge-count");
/* 74 */       return new UpsertEventIndexingStage(parameters, this.eventService, this.locator, healthCheck, histogram, fs);
/*    */     }
/* 76 */     throw new IllegalArgumentException("The topic type [" + actionType.name() + "] is not supported");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/stage/EventIndexingStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */