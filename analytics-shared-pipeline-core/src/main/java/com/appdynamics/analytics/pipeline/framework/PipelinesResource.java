/*     */ package com.appdynamics.analytics.pipeline.framework;
/*     */ 
/*     */ import com.sun.jersey.spi.resource.Singleton;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import javax.validation.Valid;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.PathParam;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.WebApplicationException;
/*     */ import javax.ws.rs.core.MediaType;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import javax.ws.rs.core.Response.Status;
/*     */ import javax.ws.rs.core.UriBuilder;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ @Singleton
/*     */ @Path("pipelines")
/*     */ @Produces({"application/json"})
/*     */ @Consumes({"application/json"})
/*     */ public class PipelinesResource
/*     */ {
/*  29 */   private static final Logger log = LoggerFactory.getLogger(PipelinesResource.class);
/*     */   public static final String KEYWORD_START = "_start";
/*     */   public static final String KEYWORD_STOP = "_stop";
/*     */   final Pipelines pipelines;
/*     */   
/*     */   public PipelinesResource(Pipelines pipelines)
/*     */   {
/*  36 */     this.pipelines = pipelines;
/*     */   }
/*     */   
/*     */   @GET
/*     */   public Collection<Object> getPipelineIds() {
/*  41 */     Collection<? extends Pipeline> pipelines = this.pipelines.getAll();
/*  42 */     Collection<Object> pipelineIds = new ArrayList(pipelines.size());
/*  43 */     for (Pipeline pipeline : pipelines) {
/*  44 */       pipelineIds.add(pipeline.getId());
/*     */     }
/*     */     
/*  47 */     return pipelineIds;
/*     */   }
/*     */   
/*     */   @POST
/*     */   public Response addPipeline(@Valid PipelineConfiguration pipelineConfiguration) {
/*  52 */     Object pipelineId = pipelineConfiguration.getId();
/*  53 */     validateId(pipelineId);
/*     */     try
/*     */     {
/*  56 */       this.pipelines.createAddAndStart(pipelineConfiguration);
/*     */     } catch (RuntimeException e) {
/*  58 */       log.error("Error occurred while adding pipeline", e);
/*     */       
/*  60 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  66 */     URI uri = UriBuilder.fromResource(PipelinesResource.class).build(new Object[] { pipelineId });
/*     */     
/*  68 */     return Response.created(uri).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{pipelineId}/_start")
/*     */   @POST
/*     */   public Response startPipeline(@Valid @PathParam("pipelineId") String pipelineId)
/*     */   {
/*     */     try
/*     */     {
/*  80 */       Pipeline pipeline = getPipeline(pipelineId);
/*  81 */       pipeline.start();
/*  82 */       this.pipelines.executePipeline(pipeline);
/*     */       
/*  84 */       return Response.status(Response.Status.OK).build();
/*     */     } catch (WebApplicationException we) {
/*  86 */       log.error("Error occurred while starting pipeline", we);
/*     */       
/*  88 */       throw we;
/*     */     } catch (Exception e) {
/*  90 */       log.error("Error occurred while starting pipeline", e);
/*     */       
/*  92 */       throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{pipelineId}")
/*     */   @GET
/*     */   public String getPipelineStatus(@Valid @PathParam("pipelineId") String pipelineId)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       Pipeline pipeline = getPipeline(pipelineId);
/*     */       
/* 109 */       return pipeline.getState().name();
/*     */     } catch (WebApplicationException we) {
/* 111 */       log.error("Error occurred while retrieving pipeline status", we);
/*     */       
/* 113 */       throw we;
/*     */     } catch (Exception e) {
/* 115 */       log.error("Error occurred while retrieving pipeline status", e);
/*     */       
/* 117 */       throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
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
/*     */   static void validateId(Object id)
/*     */   {
/* 130 */     if ((id.equals("_start")) || (id.equals("_stop"))) {
/* 131 */       throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("The id [" + id + "] is a reserved word").type(MediaType.TEXT_PLAIN_TYPE).build());
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
/*     */   Pipeline getPipeline(Object pipelineId)
/*     */   {
/* 144 */     Pipeline pipeline = (Pipeline)this.pipelines.get(pipelineId);
/* 145 */     if (pipeline == null) {
/* 146 */       throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build());
/*     */     }
/*     */     
/* 149 */     return pipeline;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{pipelineId}/_stop")
/*     */   @POST
/*     */   public Response stopPipeline(@Valid @PathParam("pipelineId") String pipelineId)
/*     */   {
/*     */     try
/*     */     {
/* 161 */       Pipeline pipeline = getPipeline(pipelineId);
/* 162 */       pipeline.stop();
/*     */       
/* 164 */       return Response.status(Response.Status.OK).build();
/*     */     } catch (WebApplicationException we) {
/* 166 */       log.error("Error occurred while stopping pipeline", we);
/*     */       
/* 168 */       throw we;
/*     */     } catch (Exception e) {
/* 170 */       log.error("Error occurred while stopping pipeline", e);
/*     */       
/* 172 */       throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{pipelineId}")
/*     */   @javax.ws.rs.DELETE
/*     */   public Response removePipeline(@Valid @PathParam("pipelineId") String pipelineId)
/*     */   {
/*     */     try
/*     */     {
/* 183 */       Pipeline pipeline = getPipeline(pipelineId);
/* 184 */       this.pipelines.remove(pipelineId);
/* 185 */       if ((pipeline.getState() == Pipeline.State.RUNNING) || (pipeline.getState() == Pipeline.State.IDLE)) {
/* 186 */         pipeline.stop();
/*     */       }
/*     */       
/* 189 */       return Response.status(Response.Status.OK).build();
/*     */     } catch (WebApplicationException we) {
/* 191 */       log.error("Error occurred while removing pipeline", we);
/*     */       
/* 193 */       throw we;
/*     */     } catch (Exception e) {
/* 195 */       log.error("Error occurred while removing pipeline", e);
/*     */       
/* 197 */       throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/PipelinesResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */