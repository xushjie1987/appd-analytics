/*    */ package com.appdynamics.analytics.agent.pipeline.xform.time;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.util.PipelineHelper;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Strings;
/*    */ import com.sun.jersey.spi.resource.Singleton;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.ws.rs.Consumes;
/*    */ import javax.ws.rs.FormParam;
/*    */ import javax.ws.rs.GET;
/*    */ import javax.ws.rs.POST;
/*    */ import javax.ws.rs.Path;
/*    */ import javax.ws.rs.Produces;
/*    */ import javax.ws.rs.WebApplicationException;
/*    */ import javax.ws.rs.core.MediaType;
/*    */ import javax.ws.rs.core.Response;
/*    */ import javax.ws.rs.core.Response.ResponseBuilder;
/*    */ import javax.ws.rs.core.Response.Status;
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
/*    */ @Singleton
/*    */ @Path("debug/timestamp")
/*    */ @Produces({"text/plain"})
/*    */ public class DateTimeExtractorStageResource
/*    */ {
/* 35 */   private static final Logger log = LoggerFactory.getLogger(DateTimeExtractorStageResource.class);
/*    */   
/*    */   private DateTimeExtractorStageFactory dateTimeExtractorStageFactory;
/*    */   
/*    */   public DateTimeExtractorStageResource(DateTimeExtractorStageFactory factory)
/*    */   {
/* 41 */     this.dateTimeExtractorStageFactory = factory;
/*    */   }
/*    */   
/*    */   @GET
/*    */   public String usage() {
/* 46 */     StringBuilder sb = new StringBuilder();
/* 47 */     sb.append("Usage:\n").append("curl -XPOST http://localhost:9090/debug/timestamp --data-urlencode \"logLine=LOG_LINE\"").append(" --data-urlencode \"pattern=PATTERN\"\n");
/*    */     
/*    */ 
/* 50 */     return sb.toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @POST
/*    */   @Consumes({"application/x-www-form-urlencoded"})
/*    */   public String postTimestampParsing(@FormParam("logLine") String logLine, @FormParam("pattern") String pattern)
/*    */   {
/*    */     try
/*    */     {
/* 62 */       Preconditions.checkArgument(!Strings.isNullOrEmpty(logLine), "'logLine' should be provided");
/* 63 */       Preconditions.checkArgument(!Strings.isNullOrEmpty(pattern), "'pattern' should be provided");
/*    */       
/* 65 */       DateTimeExtractorConfiguration stageConfiguration = new DateTimeExtractorConfiguration();
/* 66 */       stageConfiguration.setPattern(pattern);
/* 67 */       DateTimeExtractorStage stage = this.dateTimeExtractorStageFactory.create(PipelineHelper.parameters(null, null, stageConfiguration));
/*    */       
/* 69 */       log.info("DateTimeExtractor stage created successfully for pattern [{}]", pattern);
/* 70 */       Map<String, Object> input = new HashMap();
/* 71 */       input.put("message", logLine);
/* 72 */       stage.process(input);
/* 73 */       StringBuffer output = new StringBuffer();
/* 74 */       output.append("{\n");
/* 75 */       output.append(" ").append("eventTimestamp").append(" => ").append(input.get("eventTimestamp")).append("\n");
/*    */       
/* 77 */       output.append("}\n");
/* 78 */       return output.toString();
/*    */     } catch (IllegalArgumentException e) {
/* 80 */       throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
/*    */     }
/*    */     catch (RuntimeException e)
/*    */     {
/* 84 */       log.error("Error occurred while parsing the timestamp pattern", e);
/* 85 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/time/DateTimeExtractorStageResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */