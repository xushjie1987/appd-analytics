/*     */ package com.appdynamics.analytics.processor.event.resource;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.auth.AccountResolver;
/*     */ import com.appdynamics.analytics.processor.auth.SecureResource;
/*     */ import com.appdynamics.analytics.processor.event.ExtractedFieldDefinition;
/*     */ import com.appdynamics.analytics.processor.event.ExtractedFieldsService;
/*     */ import com.appdynamics.analytics.processor.exception.WebAppExceptionUtil;
/*     */ import com.appdynamics.analytics.processor.rest.RestError;
/*     */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.grok.GrokParser;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*     */ import com.codahale.metrics.Meter;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.google.inject.Inject;
/*     */ import com.sun.jersey.api.core.HttpContext;
/*     */ import com.sun.jersey.api.core.HttpRequestContext;
/*     */ import com.sun.jersey.spi.resource.Singleton;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.DELETE;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.PUT;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.PathParam;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.QueryParam;
/*     */ import javax.ws.rs.WebApplicationException;
/*     */ import javax.ws.rs.core.Context;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ @Singleton
/*     */ @Path("v1/events/{eventType}/extracted-fields")
/*     */ @Produces({"application/json"})
/*     */ @Consumes({"application/json"})
/*     */ @SecureResource
/*     */ public class ExtractedFieldsResource
/*     */   implements RestfulExtractedFields
/*     */ {
/*  68 */   private static final Logger log = LoggerFactory.getLogger(ExtractedFieldsResource.class);
/*     */   
/*     */ 
/*  71 */   static final Pattern EXTRACTED_FIELD_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9]+");
/*     */   
/*     */   final ExtractedFieldsService extractedFieldsService;
/*     */   final EventServiceHealthChecks healthChecks;
/*     */   
/*     */   @Inject
/*     */   public ExtractedFieldsResource(ExtractedFieldsService extractedFieldsService, EventServiceHealthChecks healthChecks)
/*     */   {
/*  79 */     this.extractedFieldsService = extractedFieldsService;
/*  80 */     this.healthChecks = healthChecks;
/*     */   }
/*     */   
/*     */ 
/*     */   @GET
/*     */   public Response getExtractedFields(@Context HttpContext context, @PathParam("eventType") String eventType, @QueryParam("source-types") List<String> sourceTypes)
/*     */   {
/*  87 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.GET_EXTRACTED_FIELDS);
/*     */     try {
/*  89 */       String accountName = findAccountName(context);
/*  90 */       logRequest(context);
/*  91 */       List<ExtractedFieldDefinition> fields = this.extractedFieldsService.getExtractedFields(accountName, eventType, sourceTypes);
/*     */       
/*  93 */       ArrayNode json = (ArrayNode)Reader.DEFAULT_JSON_MAPPER.valueToTree(fields);
/*  94 */       healthCheck.getMeterSuccess().mark();
/*  95 */       return Response.ok(json).build();
/*     */     } catch (WebApplicationException e) {
/*  97 */       healthCheck.getMeterUserError().mark();
/*  98 */       throw e;
/*     */     } catch (Throwable t) {
/* 100 */       throw WebAppExceptionUtil.propagateAsWebAppException(t, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{extractedFieldName}")
/*     */   @GET
/*     */   public Response getExtractedField(@Context HttpContext context, @PathParam("eventType") String eventType, @PathParam("extractedFieldName") String extractedFieldName)
/*     */   {
/* 109 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.GET_EXTRACTED_FIELD);
/*     */     try {
/* 111 */       String accountName = findAccountName(context);
/* 112 */       logRequest(context);
/* 113 */       ExtractedFieldDefinition field = this.extractedFieldsService.getExtractedField(accountName, eventType, extractedFieldName);
/*     */       
/* 115 */       if (field == null) {
/* 116 */         return Response.noContent().build();
/*     */       }
/*     */       
/* 119 */       JsonNode json = Reader.DEFAULT_JSON_MAPPER.valueToTree(field);
/* 120 */       healthCheck.getMeterSuccess().mark();
/* 121 */       return Response.ok(json).build();
/*     */     } catch (WebApplicationException e) {
/* 123 */       healthCheck.getMeterUserError().mark();
/* 124 */       throw e;
/*     */     } catch (Throwable t) {
/* 126 */       throw WebAppExceptionUtil.propagateAsWebAppException(t, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{extractedFieldName}/validate")
/*     */   @POST
/*     */   public Response validateExtractedField(@Context HttpContext context, @PathParam("eventType") String eventType, @PathParam("extractedFieldName") String extractedFieldName, String body)
/*     */   {
/* 135 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.VALIDATE_EXTRACTED_FIELD);
/*     */     try {
/* 137 */       guardBody(body);
/* 138 */       String accountName = findAccountName(context);
/* 139 */       logRequest(context, body);
/*     */       
/*     */ 
/* 142 */       toExtractedFieldDefinition(accountName, eventType, extractedFieldName, body);
/* 143 */       healthCheck.getMeterSuccess().mark();
/* 144 */       return Response.noContent().build();
/*     */     } catch (WebApplicationException e) {
/* 146 */       healthCheck.getMeterUserError().mark();
/* 147 */       throw e;
/*     */     } catch (Throwable t) {
/* 149 */       throw WebAppExceptionUtil.propagateAsWebAppException(t, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{extractedFieldName}")
/*     */   @POST
/*     */   public Response createExtractedField(@Context HttpContext context, @PathParam("eventType") String eventType, @PathParam("extractedFieldName") String extractedFieldName, String body)
/*     */   {
/* 158 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.CREATE_EXTRACTED_FIELD);
/*     */     try {
/* 160 */       guardBody(body);
/* 161 */       String accountName = findAccountName(context);
/* 162 */       logRequest(context, body);
/* 163 */       ExtractedFieldDefinition extractedFieldDefinition = toExtractedFieldDefinition(accountName, eventType, extractedFieldName, body);
/*     */       
/* 165 */       this.extractedFieldsService.createExtractedField(extractedFieldDefinition);
/* 166 */       healthCheck.getMeterSuccess().mark();
/* 167 */       return Response.noContent().build();
/*     */     } catch (WebApplicationException e) {
/* 169 */       healthCheck.getMeterUserError().mark();
/* 170 */       throw e;
/*     */     } catch (Throwable t) {
/* 172 */       throw WebAppExceptionUtil.propagateAsWebAppException(t, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{extractedFieldName}")
/*     */   @PUT
/*     */   public Response updateExtractedField(@Context HttpContext context, @PathParam("eventType") String eventType, @PathParam("extractedFieldName") String extractedFieldName, String body)
/*     */   {
/* 181 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.UPDATE_EXTRACTED_FIELD);
/*     */     try {
/* 183 */       guardBody(body);
/* 184 */       String accountName = findAccountName(context);
/* 185 */       logRequest(context, body);
/* 186 */       ExtractedFieldDefinition extractedFieldDefinition = toExtractedFieldDefinition(accountName, eventType, extractedFieldName, body);
/*     */       
/* 188 */       this.extractedFieldsService.updateExtractedField(extractedFieldDefinition);
/* 189 */       healthCheck.getMeterSuccess().mark();
/* 190 */       return Response.noContent().build();
/*     */     } catch (WebApplicationException e) {
/* 192 */       healthCheck.getMeterUserError().mark();
/* 193 */       throw e;
/*     */     } catch (Throwable t) {
/* 195 */       throw WebAppExceptionUtil.propagateAsWebAppException(t, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{extractedFieldName}")
/*     */   @DELETE
/*     */   public Response deleteExtractedField(@Context HttpContext context, @PathParam("eventType") String eventType, @PathParam("extractedFieldName") String extractedFieldName)
/*     */   {
/* 204 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.DELETE_EXTRACTED_FIELD);
/*     */     try {
/* 206 */       logRequest(context);
/* 207 */       String accountName = findAccountName(context);
/* 208 */       this.extractedFieldsService.deleteExtractedField(accountName, eventType, extractedFieldName);
/* 209 */       healthCheck.getMeterSuccess().mark();
/* 210 */       return Response.noContent().build();
/*     */     } catch (WebApplicationException e) {
/* 212 */       healthCheck.getMeterUserError().mark();
/* 213 */       throw e;
/*     */     } catch (Throwable t) {
/* 215 */       throw WebAppExceptionUtil.propagateAsWebAppException(t, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   String findAccountName(HttpContext context)
/*     */   {
/* 225 */     String accountName = AccountResolver.resolveAccountName(context);
/* 226 */     if (accountName == null) {
/* 227 */       this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.ACCOUNT_LOOKUP).getMeterUserError().mark();
/* 228 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "You've encountered a bug! Please report it back to AppDynamics", "No account info set in security context.");
/*     */     }
/*     */     
/*     */ 
/* 232 */     return accountName;
/*     */   }
/*     */   
/*     */   void guardBody(String body) {
/* 236 */     if (body == null) {
/* 237 */       throw RestError.create(StandardErrorCode.CODE_MISSING_BODY, "The body must not be null or empty.");
/*     */     }
/*     */   }
/*     */   
/*     */   ExtractedFieldDefinition toExtractedFieldDefinition(String accountName, String eventType, String name, String body)
/*     */   {
/*     */     ExtractedFieldDefinition extractedFieldDefinition;
/*     */     try {
/* 245 */       extractedFieldDefinition = (ExtractedFieldDefinition)Reader.DEFAULT_JSON_MAPPER.readValue(body, ExtractedFieldDefinition.class);
/*     */     } catch (Exception e) {
/* 247 */       throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "The request body is invalid: " + e.getMessage());
/*     */     }
/*     */     
/*     */ 
/* 251 */     if ((extractedFieldDefinition.getSourceType() == null) || (extractedFieldDefinition.getPattern() == null)) {
/* 252 */       throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "Extracted fields payload must have both sourceType and pattern");
/*     */     }
/*     */     
/*     */ 
/* 256 */     if (!EXTRACTED_FIELD_NAME_PATTERN.matcher(name).matches()) {
/* 257 */       throw RestError.create(StandardErrorCode.CODE_INVALID_EXTRACTED_FIELD_NAME, "Extracted field name must adhere to the regex [a-zA-Z0-9]+");
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 263 */       Pattern.compile(extractedFieldDefinition.getPattern());
/*     */     } catch (PatternSyntaxException e) {
/* 265 */       throw RestError.create(StandardErrorCode.CODE_INVALID_REGEX_PATTERN, e.getMessage(), e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 270 */     Set<String> aliases = GrokParser.getJavaRegexGroupAliases(extractedFieldDefinition.getPattern());
/* 271 */     if ((aliases.size() == 0) || (!aliases.contains(name))) {
/* 272 */       throw RestError.create(StandardErrorCode.CODE_MISSING_CAPTURING_GROUP_NAME_EXTRACTED_FIELD, "Extracted field pattern doesn't contain a named capturing group with the name " + name);
/*     */     }
/*     */     
/* 275 */     if (aliases.size() > 1) {
/* 276 */       throw RestError.create(StandardErrorCode.CODE_INVALID_ONE_CAPTURING_GROUP_NAME_REQUIRED, "Extracted field pattern must contain one, and only one, capturing group");
/*     */     }
/*     */     
/*     */ 
/* 280 */     extractedFieldDefinition.fill(accountName, eventType, name);
/* 281 */     return extractedFieldDefinition;
/*     */   }
/*     */   
/*     */   private void logRequest(HttpContext context) {
/* 285 */     logRequest(context, null);
/*     */   }
/*     */   
/*     */   private void logRequest(HttpContext context, String request) {
/* 289 */     String accountName = AccountResolver.resolveAccountName(context);
/* 290 */     if (accountName == null) {
/* 291 */       accountName = "unknown";
/*     */     }
/*     */     
/* 294 */     String method = "unknown";
/* 295 */     String path = "unknown";
/* 296 */     if (context.getRequest() != null) {
/* 297 */       method = context.getRequest().getMethod();
/* 298 */       path = context.getRequest().getPath();
/*     */     }
/*     */     
/* 301 */     log.info("Extracted Fields Request: [{} {}] - account [{}], request [{}]", new Object[] { method, path, accountName, StringUtils.chomp(request) });
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/ExtractedFieldsResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */