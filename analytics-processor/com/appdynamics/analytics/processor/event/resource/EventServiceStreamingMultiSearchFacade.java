/*    */ package com.appdynamics.analytics.processor.event.resource;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.EventService;
/*    */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*    */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*    */ import com.codahale.metrics.Meter;
/*    */ import java.io.OutputStream;
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
/*    */ 
/*    */ 
/*    */ public class EventServiceStreamingMultiSearchFacade
/*    */   extends EventServiceStreamingFacade
/*    */ {
/*    */   final String nodes;
/*    */   private final int requestVersion;
/*    */   
/*    */   public EventServiceStreamingMultiSearchFacade(IndexNameResolver indexNameResolver, EventService eventService, String accountName, String nodes, MeteredHealthCheck healthCheck, int requestVersion)
/*    */   {
/* 29 */     super(indexNameResolver, eventService, accountName, healthCheck);
/* 30 */     this.nodes = nodes;
/* 31 */     this.requestVersion = requestVersion;
/*    */   }
/*    */   
/*    */   protected void invoke(OutputStream os)
/*    */   {
/* 36 */     this.eventService.multiSearchEvents(this.requestVersion, this.accountName, this.nodes, os);
/* 37 */     this.healthCheck.getMeterSuccess().mark();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/EventServiceStreamingMultiSearchFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */