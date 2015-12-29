/*     */ package com.appdynamics.analytics.processor.route.resource;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.auth.SecureResource;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ClusterRouter;
/*     */ import com.appdynamics.analytics.processor.rest.RestError;
/*     */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*     */ import com.appdynamics.analytics.processor.route.RouteCleanupManager;
/*     */ import com.appdynamics.analytics.processor.route.RouteCleanupParameters;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.DELETE;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.PUT;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.PathParam;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.WebApplicationException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Path("v1/route")
/*     */ @Produces({"application/json"})
/*     */ @Consumes({"application/json"})
/*     */ @SecureResource(roles={"OPS"})
/*     */ public class ClusterRouteResource
/*     */ {
/*  51 */   private static final Logger log = LoggerFactory.getLogger(ClusterRouteResource.class);
/*     */   
/*     */   final ClusterRouter clusterRouter;
/*     */   
/*  55 */   final ObjectMapper mapper = Reader.DEFAULT_JSON_MAPPER;
/*     */   final RouteCleanupManager routeCleanupManager;
/*     */   
/*     */   public ClusterRouteResource(ClusterRouter clusterRouter, RouteCleanupManager routeCleanupManager) {
/*  59 */     this.clusterRouter = clusterRouter;
/*  60 */     this.routeCleanupManager = routeCleanupManager;
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
/*     */   @GET
/*     */   public List<Map<String, String>> listAllRoutes()
/*     */   {
/*     */     try
/*     */     {
/*  78 */       List<Map<String, String>> resp = new ArrayList();
/*     */       
/*  80 */       Map<String, String> allRoutingProps = this.clusterRouter.listAllRouteProperties();
/*  81 */       for (Map.Entry<String, String> entry : allRoutingProps.entrySet()) {
/*  82 */         Map<String, String> responseObj = new HashMap();
/*  83 */         responseObj.put("path", entry.getKey());
/*  84 */         responseObj.put("value", entry.getValue());
/*  85 */         resp.add(responseObj);
/*     */       }
/*     */       
/*  88 */       return resp;
/*     */     } catch (Exception e) {
/*  90 */       log.error("Could not list routes", e);
/*  91 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not list routes", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("admin/cluster")
/*     */   @GET
/*     */   public List<String> listAdminCluster()
/*     */   {
/*     */     try
/*     */     {
/* 105 */       return Arrays.asList(new String[] { this.clusterRouter.findAdminCluster() });
/*     */     } catch (Exception e) {
/* 107 */       log.error("Could not list admin cluster", e);
/* 108 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not list admin cluster", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("dynamic/cluster")
/*     */   @GET
/*     */   public List<String> listDynamicClusters()
/*     */   {
/*     */     try
/*     */     {
/* 122 */       return this.clusterRouter.getAllDynamicClusters();
/*     */     } catch (Exception e) {
/* 124 */       log.error("Could not list dynamic clusters", e);
/* 125 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not list dynamic clusters", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("admin/cluster/{clusterName}")
/*     */   @POST
/*     */   public Response setAdminCluster(@PathParam("clusterName") String clusterName)
/*     */   {
/*     */     try
/*     */     {
/* 140 */       boolean wasSet = this.clusterRouter.setAdminCluster(clusterName);
/* 141 */       if (wasSet) {
/* 142 */         return Response.noContent().build();
/*     */       }
/* 144 */       throw RestError.create(StandardErrorCode.CODE_CONFLICT_CLUSTER_NAME, "Could not set admin cluster since it was already set.");
/*     */     }
/*     */     catch (WebApplicationException e)
/*     */     {
/* 148 */       throw e;
/*     */     } catch (Exception e) {
/* 150 */       log.error("Could not set admin cluster", e);
/* 151 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not set admin cluster", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("admin/cluster/{clusterName}")
/*     */   @PUT
/*     */   public Response updateAdminCluster(@PathParam("clusterName") String clusterName)
/*     */   {
/*     */     try
/*     */     {
/* 166 */       boolean wasUpdated = this.clusterRouter.updateAdminCluster(clusterName);
/* 167 */       if (wasUpdated) {
/* 168 */         return Response.noContent().build();
/*     */       }
/* 170 */       throw RestError.create(StandardErrorCode.CODE_CONFLICT_CLUSTER_NAME, "Could not update admin cluster since it has not been set.");
/*     */     }
/*     */     catch (WebApplicationException e)
/*     */     {
/* 174 */       throw e;
/*     */     } catch (Exception e) {
/* 176 */       log.error("Could not update admin cluster", e);
/* 177 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not update admin cluster", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("dynamic/cluster/{clusterName}")
/*     */   @POST
/*     */   public Response addDynamicCluster(@PathParam("clusterName") String clusterName)
/*     */   {
/*     */     try
/*     */     {
/* 193 */       if (this.clusterRouter.addDynamicCluster(clusterName)) {
/* 194 */         return Response.noContent().build();
/*     */       }
/* 196 */       throw RestError.create(StandardErrorCode.CODE_CONFLICT_CLUSTER_NAME, "Could not add dynamic cluster [" + clusterName + "] since it already exists.");
/*     */     }
/*     */     catch (WebApplicationException e)
/*     */     {
/* 200 */       throw e;
/*     */     } catch (Exception e) {
/* 202 */       log.error("Could not add dynamic cluster", e);
/* 203 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not add dynamic cluster", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("dynamic/cluster/{clusterName}")
/*     */   @DELETE
/*     */   public Response removeDynamicCluster(@PathParam("clusterName") String clusterName)
/*     */   {
/*     */     try
/*     */     {
/* 219 */       if (this.clusterRouter.removeDynamicCluster(clusterName)) {
/* 220 */         return Response.noContent().build();
/*     */       }
/* 222 */       throw RestError.create(StandardErrorCode.CODE_NOT_FOUND_CLUSTER_NAME, "Could not remove dynamic cluster [" + clusterName + "] since it didn't exist.");
/*     */     }
/*     */     catch (WebApplicationException e)
/*     */     {
/* 226 */       throw e;
/*     */     } catch (Exception e) {
/* 228 */       log.error("Could not remove dynamic cluster", e);
/* 229 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not remove dynamic cluster", Throwables.getStackTraceAsString(e));
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
/*     */ 
/*     */   @Path("dynamic")
/*     */   @POST
/*     */   public Response createDynamicRoute(String bodyStr)
/*     */   {
/*     */     try
/*     */     {
/* 254 */       JsonNode body = getBody(bodyStr);
/* 255 */       List<String> accountNames = validateAndGetAccountNames(body);
/* 256 */       List<String> clusterNames = validateAndGetClusterNames(body);
/*     */       
/* 258 */       List<String> createdAccountNames = this.clusterRouter.createDynamicRoute(accountNames, clusterNames);
/*     */       
/* 260 */       if (createdAccountNames.size() == accountNames.size()) {
/* 261 */         return Response.noContent().build();
/*     */       }
/* 263 */       accountNames.removeAll(createdAccountNames);
/* 264 */       throw RestError.create(StandardErrorCode.CODE_CONFLICT_ROUTE, "Could not add dynamic routes for accounts [" + Joiner.on(",").join(accountNames) + "] because routes already exist for them.");
/*     */ 
/*     */     }
/*     */     catch (WebApplicationException e)
/*     */     {
/* 269 */       throw e;
/*     */     } catch (Exception e) {
/* 271 */       log.error("Could not add dynamic route", e);
/* 272 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not add dynamic route", Throwables.getStackTraceAsString(e));
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
/*     */ 
/*     */   @Path("dynamic")
/*     */   @PUT
/*     */   public Response modifyDynamicRoute(String bodyStr)
/*     */   {
/*     */     try
/*     */     {
/* 297 */       JsonNode body = getBody(bodyStr);
/* 298 */       List<String> accountNames = validateAndGetAccountNames(body);
/* 299 */       List<String> clusterNames = validateAndGetClusterNames(body);
/*     */       
/* 301 */       List<String> modifiedAccountNames = this.clusterRouter.modifyDynamicRoute(accountNames, clusterNames);
/*     */       
/* 303 */       if (modifiedAccountNames.size() == accountNames.size()) {
/* 304 */         return Response.noContent().build();
/*     */       }
/* 306 */       accountNames.removeAll(modifiedAccountNames);
/* 307 */       throw RestError.create(StandardErrorCode.CODE_ROUTE_NOT_FOUND, "Could not modify dynamic routes for accounts [" + Joiner.on(",").join(accountNames) + "] because routes do not already exist for them.");
/*     */ 
/*     */     }
/*     */     catch (WebApplicationException e)
/*     */     {
/* 312 */       throw e;
/*     */     } catch (Exception e) {
/* 314 */       log.error("Could not modify dynamic route", e);
/* 315 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not modify dynamic cluster", Throwables.getStackTraceAsString(e));
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
/*     */   @Path("dynamic")
/*     */   @DELETE
/*     */   public Response removeDynamicRoute(String bodyStr)
/*     */   {
/*     */     try
/*     */     {
/* 337 */       JsonNode body = getBody(bodyStr);
/* 338 */       List<String> accountNames = validateAndGetAccountNames(body);
/*     */       
/* 340 */       this.clusterRouter.removeDynamicRoute(accountNames);
/* 341 */       return Response.noContent().build();
/*     */     } catch (WebApplicationException e) {
/* 343 */       throw e;
/*     */     } catch (Exception e) {
/* 345 */       log.error("Could not remove dynamic route", e);
/* 346 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not remove dynamic cluster", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("cleanup")
/*     */   @POST
/*     */   public Response cleanupRoutes(RouteCleanupParameters routeCleanupParameters)
/*     */   {
/*     */     try
/*     */     {
/* 361 */       this.routeCleanupManager.cleanupRoutes(routeCleanupParameters);
/* 362 */       return Response.noContent().build();
/*     */     } catch (WebApplicationException e) {
/* 364 */       throw e;
/*     */     } catch (Exception e) {
/* 366 */       log.error("Could not clean up ZK routes", e);
/* 367 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "Could not clean up ZK routes", Throwables.getStackTraceAsString(e));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   JsonNode getBody(String bodyStr)
/*     */   {
/* 374 */     if (Strings.isNullOrEmpty(bodyStr)) {
/* 375 */       throw RestError.create(StandardErrorCode.CODE_MISSING_BODY, "Body must not be null or empty.");
/*     */     }
/*     */     try
/*     */     {
/* 379 */       return this.mapper.readTree(bodyStr);
/*     */     } catch (IOException e) {
/* 381 */       log.error("Could not parse request body as json", e);
/* 382 */       throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "The supplied request body could not be parsed as json.", e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   List<String> validateAndGetAccountNames(JsonNode body)
/*     */   {
/* 389 */     List<String> accountNames = new ArrayList();
/* 390 */     ArrayNode arrayNode = (ArrayNode)body.get("accountNames");
/* 391 */     if ((arrayNode == null) || (arrayNode.size() == 0)) {
/* 392 */       throw RestError.create(StandardErrorCode.CODE_MISSING_ACCOUNT_NAMES, "No account names were supplied in the request. ");
/*     */     }
/*     */     
/*     */ 
/* 396 */     for (JsonNode node : arrayNode) {
/* 397 */       accountNames.add(node.textValue());
/*     */     }
/* 399 */     return accountNames;
/*     */   }
/*     */   
/*     */   List<String> validateAndGetClusterNames(JsonNode body) {
/* 403 */     List<String> clusterNames = new ArrayList();
/* 404 */     ArrayNode arrayNode = (ArrayNode)body.get("clusterNames");
/* 405 */     if ((arrayNode == null) || (arrayNode.size() == 0)) {
/* 406 */       throw RestError.create(StandardErrorCode.CODE_MISSING_CLUSTER_NAMES, "No cluster names were supplied in the request.");
/*     */     }
/*     */     
/*     */ 
/* 410 */     for (JsonNode node : arrayNode) {
/* 411 */       clusterNames.add(node.textValue());
/*     */     }
/* 413 */     return clusterNames;
/*     */   }
/*     */   
/*     */   String getTextField(JsonNode body, String field) {
/* 417 */     return body.get(field) != null ? body.get(field).textValue() : null;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/route/resource/ClusterRouteResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */