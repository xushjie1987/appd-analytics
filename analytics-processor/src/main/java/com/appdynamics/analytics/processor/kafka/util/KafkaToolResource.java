/*     */ package com.appdynamics.analytics.processor.kafka.util;
/*     */ 
/*     */ import com.google.common.base.Throwables;
/*     */ import com.sun.jersey.spi.resource.Singleton;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.WebApplicationException;
/*     */ import javax.ws.rs.core.MediaType;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import javax.ws.rs.core.Response.Status;
/*     */ import org.I0Itec.zkclient.ZkClient;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ @Path("v1/kafka")
/*     */ @Produces({"text/plain"})
/*     */ @Consumes({"text/plain"})
/*     */ public class KafkaToolResource
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(KafkaToolResource.class);
/*     */   final String zkCsvList;
/*     */   final ZkClient zkClient;
/*     */   
/*     */   public KafkaToolResource(String zkCsvList)
/*     */   {
/*  44 */     this.zkCsvList = zkCsvList;
/*  45 */     this.zkClient = KafkaTool.newZkClient(zkCsvList);
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
/*     */   @GET
/*     */   @Path("report")
/*     */   public String report()
/*     */   {
/*     */     try
/*     */     {
/*  68 */       KafkaTool.Report report = KafkaTool.runReport(this.zkCsvList, this.zkClient);
/*  69 */       String text = report.toString();
/*  70 */       log.info(text);
/*  71 */       return text;
/*     */     } catch (RuntimeException e) {
/*  73 */       log.error("Error occurred while retrieving status report", e);
/*     */       
/*  75 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Throwables.getStackTraceAsString(e)).type(MediaType.TEXT_PLAIN_TYPE).build());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @POST
/*     */   @Path("elect")
/*     */   public void electLeaders()
/*     */   {
/*     */     try
/*     */     {
/*  87 */       KafkaTool.runLeaderElection(this.zkCsvList);
/*     */     } catch (RuntimeException e) {
/*  89 */       log.error("Error occurred while electing leaders", e);
/*     */       
/*  91 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Throwables.getStackTraceAsString(e)).type(MediaType.TEXT_PLAIN_TYPE).build());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @PreDestroy
/*     */   void stop()
/*     */   {
/* 101 */     KafkaTool.closeQuietly(this.zkClient, log);
/* 102 */     KafkaTool.stop();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/util/KafkaToolResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */