/*    */ package com.appdynamics.analytics.processor.event.resource;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.event.EventService;
/*    */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*    */ import com.appdynamics.analytics.processor.exception.WebAppExceptionUtil;
/*    */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import javax.ws.rs.WebApplicationException;
/*    */ import javax.ws.rs.core.StreamingOutput;
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
/*    */ public abstract class EventServiceStreamingFacade
/*    */   implements StreamingOutput
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(EventServiceStreamingFacade.class);
/*    */   
/*    */   final IndexNameResolver indexNameResolver;
/*    */   
/*    */   final EventService eventService;
/*    */   final String accountName;
/*    */   final MeteredHealthCheck healthCheck;
/*    */   
/*    */   public EventServiceStreamingFacade(IndexNameResolver indexNameResolver, EventService eventService, String accountName, MeteredHealthCheck healthCheck)
/*    */   {
/* 36 */     this.indexNameResolver = indexNameResolver;
/* 37 */     this.eventService = eventService;
/* 38 */     this.accountName = accountName;
/* 39 */     this.healthCheck = healthCheck;
/*    */   }
/*    */   
/*    */   public void write(OutputStream os) throws WebApplicationException
/*    */   {
/*    */     try
/*    */     {
/* 46 */       invoke(os);
/*    */     } catch (Exception e) {
/* 48 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, this.healthCheck);
/*    */     }
/*    */   }
/*    */   
/*    */   protected abstract void invoke(OutputStream paramOutputStream)
/*    */     throws IOException, WebApplicationException;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/EventServiceStreamingFacade.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */