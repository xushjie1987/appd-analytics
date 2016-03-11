/*     */ package com.appdynamics.analytics.processor.zookeeper.util;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.zookeeper.server.ZooKeeperHealthCheck;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.sun.jersey.spi.resource.Singleton;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.DefaultValue;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.QueryParam;
/*     */ import javax.ws.rs.WebApplicationException;
/*     */ import javax.ws.rs.core.MediaType;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import javax.ws.rs.core.Response.Status;
/*     */ import org.apache.curator.framework.CuratorFramework;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ @Path("v1/zookeeper")
/*     */ @Produces({"application/json"})
/*     */ @Consumes({"application/json"})
/*     */ public class ZooKeeperToolResource
/*     */ {
/*  40 */   private static final Logger log = LoggerFactory.getLogger(ZooKeeperToolResource.class);
/*     */   final CuratorFramework framework;
/*     */   final ZooKeeperHealthCheck zooKeeperHealthCheck;
/*     */   
/*     */   public ZooKeeperToolResource(CuratorFramework framework, ZooKeeperHealthCheck zooKeeperHealthCheck)
/*     */   {
/*  46 */     this.framework = framework;
/*  47 */     this.zooKeeperHealthCheck = zooKeeperHealthCheck;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @GET
/*     */   @Path("export")
/*     */   public Object exportTree(@QueryParam("path") @DefaultValue("/") String path, @QueryParam("pretty") @DefaultValue("false") boolean pretty)
/*     */   {
/*     */     try
/*     */     {
/*  61 */       Map<String, Object> map = ZooKeeperTool.exportTree(this.framework, path);
/*  62 */       return pretty ? Reader.DEFAULT_JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(map) : map;
/*     */     } catch (JsonProcessingException|RuntimeException e) {
/*  64 */       log.error("Error occurred while exporting data", e);
/*     */       
/*  66 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Throwables.getStackTraceAsString(e)).type(MediaType.TEXT_PLAIN_TYPE).build());
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
/*     */   @POST
/*     */   @Path("import")
/*     */   public void importTree(@QueryParam("path") @DefaultValue("/") String path, @QueryParam("merge") @DefaultValue("true") boolean merge, byte[] jsonUtf8)
/*     */   {
/*     */     try
/*     */     {
/*  90 */       Map<String, Object> json = (Map)Reader.DEFAULT_JSON_MAPPER.readValue(jsonUtf8, Map.class);
/*  91 */       ZooKeeperTool.importTree(this.framework, path, merge, json);
/*     */     } catch (IOException|RuntimeException e) {
/*  93 */       log.error("Error occurred while importing data", e);
/*     */       
/*  95 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Throwables.getStackTraceAsString(e)).type(MediaType.TEXT_PLAIN_TYPE).build());
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
/*     */   @GET
/*     */   @Path("status")
/*     */   public String ensembleStatus()
/*     */   {
/*     */     try
/*     */     {
/* 112 */       return this.zooKeeperHealthCheck.getEnsembleStatus();
/*     */     } catch (RuntimeException e) {
/* 114 */       log.error("Error occurred while fetching ZooKeeper ensemble status", e);
/*     */       
/* 116 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Throwables.getStackTraceAsString(e)).type(MediaType.TEXT_PLAIN_TYPE).build());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/util/ZooKeeperToolResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */