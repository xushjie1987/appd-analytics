/*    */ package com.appdynamics.analytics.processor.event.resource;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.EventService;
/*    */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*    */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*    */ import com.codahale.metrics.Meter;
/*    */ import java.io.OutputStream;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public final class EventServiceStreamingSearchFacade
/*    */   extends EventServiceStreamingFacade
/*    */ {
/* 24 */   private static final Logger log = LoggerFactory.getLogger(EventServiceStreamingSearchFacade.class);
/*    */   
/*    */ 
/*    */   final String eventType;
/*    */   
/*    */   final String searchRequest;
/*    */   
/*    */   private final int requestVersion;
/*    */   
/*    */ 
/*    */   public EventServiceStreamingSearchFacade(IndexNameResolver indexNameResolver, EventService eventService, String accountName, String eventType, String searchRequest, MeteredHealthCheck healthCheck, int requestVersion)
/*    */   {
/* 36 */     super(indexNameResolver, eventService, accountName, healthCheck);
/* 37 */     this.eventType = eventType;
/* 38 */     this.searchRequest = searchRequest;
/* 39 */     this.requestVersion = requestVersion;
/*    */   }
/*    */   
/*    */   protected void invoke(OutputStream os)
/*    */   {
/* 44 */     this.eventService.searchEvents(this.requestVersion, this.accountName, this.eventType, this.searchRequest, os);
/* 45 */     this.healthCheck.getMeterSuccess().mark();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/EventServiceStreamingSearchFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */