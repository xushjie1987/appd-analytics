/*    */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*    */ import com.appdynamics.analytics.processor.rest.RestError;
/*    */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.databind.ObjectWriter;
/*    */ import com.google.common.base.Optional;
/*    */ import com.google.common.base.Throwables;
/*    */ import com.sun.jersey.spi.resource.Singleton;
/*    */ import javax.ws.rs.DefaultValue;
/*    */ import javax.ws.rs.GET;
/*    */ import javax.ws.rs.Path;
/*    */ import javax.ws.rs.Produces;
/*    */ import javax.ws.rs.QueryParam;
/*    */ import javax.ws.rs.WebApplicationException;
/*    */ import javax.ws.rs.core.MediaType;
/*    */ import javax.ws.rs.core.Response;
/*    */ import javax.ws.rs.core.Response.ResponseBuilder;
/*    */ import javax.ws.rs.core.Response.Status;
/*    */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Singleton
/*    */ @Path("v1")
/*    */ @Produces({"application/json"})
/*    */ public class ElasticSearchToolResource
/*    */ {
/* 39 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchToolResource.class);
/*    */   private Optional<ElasticSearchStoreTool> esStoreTool;
/*    */   
/* 42 */   public void setEsStoreTool(Optional<ElasticSearchStoreTool> esStoreTool) { this.esStoreTool = esStoreTool; }
/*    */   
/*    */ 
/*    */   public ElasticSearchToolResource(ClientProvider clientProvider)
/*    */   {
/* 47 */     this.esStoreTool = Optional.absent();
/* 48 */     this.clientProvider = clientProvider;
/*    */   }
/*    */   
/*    */ 
/*    */   private final ClientProvider clientProvider;
/*    */   @GET
/*    */   @Path("store/report")
/*    */   public String healthReport(@QueryParam("pretty") @DefaultValue("false") boolean pretty)
/*    */   {
/*    */     try
/*    */     {
/* 59 */       if (!this.esStoreTool.isPresent()) {
/* 60 */         throw RestError.create(StandardErrorCode.CODE_SERVICE_UNAVAILABLE, "The ElasticSearchStoreTool is not available");
/*    */       }
/*    */       
/*    */ 
/* 64 */       ElasticSearchStoreHealth elasticSearchHealth = ((ElasticSearchStoreTool)this.esStoreTool.get()).getCurrentElasticSearchHealth();
/* 65 */       String healthDataString = toJson(elasticSearchHealth, pretty);
/* 66 */       log.debug("Elasticsearch health report: {}", healthDataString);
/* 67 */       return healthDataString;
/*    */     } catch (Exception e) {
/* 69 */       log.error("Error occurred while retrieving ElasticSearch health report", e);
/* 70 */       throw createServerError(e);
/*    */     }
/*    */   }
/*    */   
/*    */   @GET
/*    */   @Path("numberOfDataNodes")
/*    */   public int getNumDataNodes() {
/*    */     try {
/* 78 */       return ESUtils.getClusterHealth(this.clientProvider.getAdminClient()).getNumberOfDataNodes();
/*    */     } catch (RuntimeException e) {
/* 80 */       log.error("Error occurred while retrieving number of ES data nodes: ", e);
/* 81 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Throwables.getStackTraceAsString(e)).type(MediaType.TEXT_PLAIN_TYPE).build());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private String toJson(Object obj, boolean pretty)
/*    */     throws JsonProcessingException
/*    */   {
/* 90 */     return pretty ? Reader.DEFAULT_JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj) : Reader.DEFAULT_JSON_MAPPER.writeValueAsString(obj);
/*    */   }
/*    */   
/*    */ 
/*    */   private WebApplicationException createServerError(Exception e)
/*    */   {
/* 96 */     throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Throwables.getStackTraceAsString(e)).type(MediaType.TEXT_PLAIN_TYPE).build());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ElasticSearchToolResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */