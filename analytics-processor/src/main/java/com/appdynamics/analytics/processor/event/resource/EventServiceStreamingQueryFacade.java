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
/*    */ public final class EventServiceStreamingQueryFacade
/*    */   extends EventServiceStreamingFacade
/*    */ {
/* 24 */   private static final Logger log = LoggerFactory.getLogger(EventServiceStreamingQueryFacade.class);
/*    */   
/*    */ 
/*    */   final String query;
/*    */   
/*    */ 
/*    */   final String startTime;
/*    */   
/*    */   final String endTime;
/*    */   
/*    */   final int limitResults;
/*    */   
/*    */   final boolean returnAsEsJson;
/*    */   
/*    */   private final int requestVersion;
/*    */   
/*    */ 
/*    */   public EventServiceStreamingQueryFacade(IndexNameResolver indexNameResolver, EventService eventService, String accountName, String query, String startTime, String endTime, int limitResults, boolean returnAsEsJson, MeteredHealthCheck healthCheck, int requestVersion)
/*    */   {
/* 43 */     super(indexNameResolver, eventService, accountName, healthCheck);
/*    */     
/* 45 */     this.query = query;
/* 46 */     this.startTime = startTime;
/* 47 */     this.endTime = endTime;
/* 48 */     this.limitResults = limitResults;
/* 49 */     this.returnAsEsJson = returnAsEsJson;
/* 50 */     this.requestVersion = requestVersion;
/*    */   }
/*    */   
/*    */   protected void invoke(OutputStream os)
/*    */   {
/* 55 */     this.eventService.queryEvents(this.requestVersion, this.accountName, this.query, this.startTime, this.endTime, this.limitResults, this.returnAsEsJson, os);
/* 56 */     this.healthCheck.getMeterSuccess().mark();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/EventServiceStreamingQueryFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */