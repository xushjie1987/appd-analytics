/*     */ package com.appdynamics.analytics.processor.rest.availability.resource;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.auth.SecureResource;
/*     */ import com.appdynamics.analytics.processor.rest.RestError;
/*     */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*     */ import com.appdynamics.analytics.processor.rest.availability.ResourceAvailabilityAdapter;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.inject.Inject;
/*     */ import com.sun.jersey.spi.resource.Singleton;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.DELETE;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.WebApplicationException;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import org.apache.curator.framework.CuratorFramework;
/*     */ import org.apache.curator.framework.api.CreateBuilder;
/*     */ import org.apache.curator.framework.api.ExistsBuilder;
/*     */ import org.apache.curator.framework.api.GetDataBuilder;
/*     */ import org.apache.curator.framework.api.PathAndBytesable;
/*     */ import org.apache.curator.framework.api.SetDataBuilder;
/*     */ import org.apache.zookeeper.KeeperException.NoNodeException;
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
/*     */ @Path("v1/resource/exclusions")
/*     */ @Produces({"application/json"})
/*     */ @Consumes({"application/json"})
/*     */ @SecureResource(roles={"OPS"})
/*     */ public class ResourceAvailabilityResource
/*     */ {
/*  61 */   private static final Logger log = LoggerFactory.getLogger(ResourceAvailabilityResource.class);
/*     */   
/*     */ 
/*  64 */   public static final String ZK_EXCLUSIONS_PATH = ResourceAvailabilityAdapter.ANALYTICS_RESOURCE_EXCLUSIONS_ZK_PATH;
/*     */   
/*     */   public static final String RESOURCE_FIELD = "resource";
/*     */   
/*     */   public static final String HTTP_METHODS_FIELD = "httpMethods";
/*     */   final ObjectMapper mapper;
/*     */   final CuratorFramework zkClient;
/*     */   
/*     */   @Inject
/*     */   public ResourceAvailabilityResource(CuratorFramework zkClient)
/*     */   {
/*  75 */     this.zkClient = zkClient;
/*  76 */     this.mapper = Reader.DEFAULT_JSON_MAPPER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @GET
/*     */   public JsonNode listAllExclusions()
/*     */   {
/*     */     try
/*     */     {
/*  86 */       byte[] data = (byte[])this.zkClient.getData().forPath(ZK_EXCLUSIONS_PATH);
/*  87 */       return this.mapper.readTree(data);
/*     */     }
/*     */     catch (KeeperException.NoNodeException e) {
/*  90 */       log.debug("Returning empty node for exclusions since ZK node [{}] doesn't exit.", ZK_EXCLUSIONS_PATH);
/*  91 */       return this.mapper.createArrayNode();
/*     */     } catch (Exception e) {
/*  93 */       log.error("Could not list exclusions", e);
/*  94 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not list exclusions", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @POST
/*     */   public Response setExclusions(ArrayNode body)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       String bodyStr = Reader.DEFAULT_JSON_MAPPER.writeValueAsString(body);
/* 108 */       log.info("Setting resource exclusions as {}", bodyStr);
/*     */       
/* 110 */       validateExclusionsPayload(body);
/*     */       PathAndBytesable operation;
/*     */       PathAndBytesable operation;
/* 113 */       if (this.zkClient.checkExists().forPath(ZK_EXCLUSIONS_PATH) != null) {
/* 114 */         operation = this.zkClient.setData();
/*     */       } else {
/* 116 */         operation = this.zkClient.create().creatingParentsIfNeeded();
/*     */       }
/* 118 */       operation.forPath(ZK_EXCLUSIONS_PATH, bodyStr.getBytes("UTF-8"));
/* 119 */       return Response.noContent().build();
/*     */     } catch (WebApplicationException e) {
/* 121 */       throw e;
/*     */     } catch (Exception e) {
/* 123 */       log.error("Could not set exclusions [{}] on zk node [{}]", new Object[] { body, ZK_EXCLUSIONS_PATH, e });
/* 124 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not set exclusions", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void validateExclusionsPayload(ArrayNode body)
/*     */   {
/* 131 */     for (JsonNode exclusion : body) {
/* 132 */       if (exclusion.get("resource") == null) {
/* 133 */         throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "Field [resource] was missing from payload item.");
/*     */       }
/*     */       
/*     */ 
/* 137 */       JsonNode httpMethods = exclusion.get("httpMethods");
/* 138 */       if (httpMethods == null) {
/* 139 */         throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "Field [httpMethods] was missing from payload item.");
/*     */       }
/* 141 */       if (!(httpMethods instanceof ArrayNode)) {
/* 142 */         throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "Field [httpMethods] must be an array.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @DELETE
/*     */   public Response removeExclusions()
/*     */   {
/*     */     try
/*     */     {
/* 155 */       log.info("Removing resource exclusions");
/*     */       
/*     */ 
/*     */ 
/* 159 */       if (this.zkClient.checkExists().forPath(ZK_EXCLUSIONS_PATH) != null) {
/* 160 */         this.zkClient.setData().forPath(ZK_EXCLUSIONS_PATH, "[]".getBytes("UTF-8"));
/*     */       }
/*     */       
/* 163 */       return Response.noContent().build();
/*     */     } catch (KeeperException.NoNodeException e) {
/* 165 */       log.debug("Did not delete exclusions since node [{}] doesn't exist in ZK.", ZK_EXCLUSIONS_PATH);
/* 166 */       return Response.noContent().build();
/*     */     } catch (Exception e) {
/* 168 */       log.error("Could not remove all exclusions", e);
/* 169 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not remove all exclusions", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/availability/resource/ResourceAvailabilityResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */