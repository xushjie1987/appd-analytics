/*    */ package com.appdynamics.analytics.agent.pipeline.xform.grok;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.util.PipelineHelper;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Strings;
/*    */ import com.sun.jersey.spi.resource.Singleton;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ @Singleton
/*    */ @Path("debug/grok")
/*    */ @Produces({"text/plain"})
/*    */ public class GrokStageResource
/*    */ {
/* 29 */   private static final Logger log = LoggerFactory.getLogger(GrokStageResource.class);
/*    */   
/*    */   private GrokStageFactory grokStageFactory;
/*    */   
/*    */   public GrokStageResource(GrokStageFactory factory)
/*    */   {
/* 35 */     this.grokStageFactory = factory;
/*    */   }
/*    */   
/*    */   @GET
/*    */   public String usage() {
/* 40 */     StringBuilder sb = new StringBuilder();
/* 41 */     sb.append("Usage:\n").append("Single line log: ").append("curl -v -XPOST http://localhost:9090/debug/grok --data-urlencode \"logLine=LOG_LINE\"").append(" --data-urlencode \"pattern=PATTERN\"\n").append("Multi line log: ").append("curl -v -XPOST http://localhost:9090/debug/grok --data-urlencode \"logLine=`cat FILE_NAME`\"").append(" --data-urlencode \"pattern=PATTERN\"\n");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 47 */     return sb.toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @POST
/*    */   @Consumes({"application/x-www-form-urlencoded"})
/*    */   public String postGrokParsing(@FormParam("logLine") String logLine, @FormParam("pattern") String pattern)
/*    */   {
/*    */     try
/*    */     {
/* 59 */       Preconditions.checkArgument(!Strings.isNullOrEmpty(logLine), "'logLine' should be provided");
/* 60 */       Preconditions.checkArgument(!Strings.isNullOrEmpty(pattern), "'pattern' should be provided");
/*    */       
/* 62 */       GrokStageConfiguration stageConfiguration = new GrokStageConfiguration();
/* 63 */       stageConfiguration.setPattern(pattern);
/* 64 */       GrokStage stage = this.grokStageFactory.create(PipelineHelper.parameters(null, null, stageConfiguration));
/*    */       
/* 66 */       log.info("grok stage created successfully for pattern [{}]", pattern);
/* 67 */       Map<String, Object> input = new HashMap();
/* 68 */       input.put("message", logLine);
/* 69 */       stage.process(input);
/* 70 */       StringBuffer output = new StringBuffer();
/* 71 */       output.append("{\n");
/* 72 */       for (Map.Entry<String, Object> entry : input.entrySet()) {
/* 73 */         if (!((String)entry.getKey()).equals("message")) {
/* 74 */           output.append(" ").append((String)entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
/*    */         }
/*    */       }
/* 77 */       output.append("}\n");
/* 78 */       return output.toString();
/*    */     } catch (IllegalArgumentException e) {
/* 80 */       throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
/*    */     }
/*    */     catch (RuntimeException e)
/*    */     {
/* 84 */       log.error("Error occurred while parsing the grok pattern", e);
/* 85 */       throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type(MediaType.TEXT_PLAIN_TYPE).build());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/grok/GrokStageResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */