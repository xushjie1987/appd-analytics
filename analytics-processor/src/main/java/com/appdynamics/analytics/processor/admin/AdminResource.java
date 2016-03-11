/*     */ package com.appdynamics.analytics.processor.admin;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.auth.SecureResource;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.compaction.IndexCompactionManager;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.compaction.IndexCompactionParameters;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.RollingIndexController;
/*     */ import com.appdynamics.analytics.processor.exception.WebAppExceptionUtil;
/*     */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*     */ import com.codahale.metrics.Meter;
/*     */ import com.sun.jersey.api.core.HttpContext;
/*     */ import com.sun.jersey.api.core.HttpRequestContext;
/*     */ import com.sun.jersey.spi.resource.Singleton;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.core.Context;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ @Path("v1/admin")
/*     */ @Produces({"application/json"})
/*     */ @Consumes({"application/json"})
/*     */ @SecureResource(roles={"OPS"})
/*     */ public class AdminResource
/*     */ {
/*  37 */   private static final Logger log = LoggerFactory.getLogger(AdminResource.class);
/*     */   
/*     */ 
/*     */   final MeteredHealthCheck rolloverHealthCheck;
/*     */   
/*     */ 
/*     */   final RollingIndexController rollingIndexController;
/*     */   
/*     */   final MeteredHealthCheck indexCompactionHealthCheck;
/*     */   
/*     */   final IndexCompactionManager indexCompactionManager;
/*     */   
/*     */ 
/*     */   public AdminResource(ConsolidatedHealthCheck healthCheck, Environment environment, RollingIndexController rollingIndexController, IndexCompactionManager indexCompactionManager)
/*     */   {
/*  52 */     this.rollingIndexController = rollingIndexController;
/*  53 */     this.rolloverHealthCheck = new MeteredHealthCheck("Resource [v1/admin - rollover]", environment);
/*  54 */     healthCheck.register(this.rolloverHealthCheck);
/*     */     
/*  56 */     this.indexCompactionManager = indexCompactionManager;
/*  57 */     this.indexCompactionHealthCheck = new MeteredHealthCheck("Resource [v1/admin - index compaction]", environment);
/*  58 */     healthCheck.register(this.indexCompactionHealthCheck);
/*     */   }
/*     */   
/*     */   @Path("rollover")
/*     */   @POST
/*     */   public Response forceRollOver(ForcedRolloverRequest forcedRolloverRequest) {
/*  64 */     log.info("Admin Service request forcing roll over []", forcedRolloverRequest);
/*     */     try
/*     */     {
/*  67 */       this.rollingIndexController.forceRollover(forcedRolloverRequest);
/*  68 */       this.rolloverHealthCheck.getMeterSuccess().mark();
/*     */     } catch (Throwable t) {
/*  70 */       throw WebAppExceptionUtil.propagateAsWebAppException(t, this.rolloverHealthCheck);
/*     */     }
/*     */     
/*  73 */     return Response.ok().build();
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
/*     */   @Path("indexcompaction")
/*     */   @POST
/*     */   public Response forceIndexCompaction(@Context HttpContext context, IndexCompactionParameters indexCompactionParameters)
/*     */   {
/*  92 */     log.info("Admin Event Service Request: [{} {}]", context.getRequest().getMethod(), context.getRequest().getPath());
/*     */     
/*     */     try
/*     */     {
/*  96 */       this.indexCompactionManager.compactData(indexCompactionParameters);
/*  97 */       return Response.noContent().build();
/*     */     } catch (Exception e) {
/*  99 */       this.indexCompactionHealthCheck.getMeterError().mark();
/* 100 */       log.error("Could not run index compaction: " + e.getMessage(), e);
/* 101 */       throw e;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/AdminResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */