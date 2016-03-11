/*     */ package com.appdynamics.analytics.processor.account.resource;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.AccountManager;
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*     */ import com.appdynamics.analytics.processor.account.exception.IllegalAccountNameException;
/*     */ import com.appdynamics.analytics.processor.auth.SecureResource;
/*     */ import com.appdynamics.analytics.processor.event.ElasticSearchMetaService;
/*     */ import com.appdynamics.analytics.processor.event.metadata.EventTypeMetaDataService;
/*     */ import com.appdynamics.analytics.processor.exception.WebAppExceptionUtil;
/*     */ import com.appdynamics.analytics.processor.rest.RestError;
/*     */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.datatype.joda.JodaModule;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.inject.Inject;
/*     */ import com.sun.jersey.api.core.HttpContext;
/*     */ import com.sun.jersey.api.core.HttpRequestContext;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.PathParam;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.core.Context;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Path("v1/account")
/*     */ @Produces({"application/json"})
/*     */ @Consumes({"application/json"})
/*     */ @SecureResource(roles={"EUM", "CONTROLLER"})
/*     */ public class AccountResource
/*     */ {
/*  44 */   private static final Logger log = LoggerFactory.getLogger(AccountResource.class);
/*     */   
/*     */ 
/*     */ 
/*  48 */   private static final ObjectMapper MAPPER = new ObjectMapper();
/*     */   final AccountManager accountManager;
/*     */   
/*  51 */   static { MAPPER.registerModule(new JodaModule()); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Inject
/*     */   public AccountResource(AccountManager accountManager, ElasticSearchMetaService eventMetaService, EventTypeMetaDataService eventTypeMetaDataService)
/*     */   {
/*  61 */     this.accountManager = accountManager;
/*  62 */     this.eventMetaService = eventMetaService;
/*  63 */     this.eventTypeMetaDataService = eventTypeMetaDataService;
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
/*     */   final ElasticSearchMetaService eventMetaService;
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
/*     */   final EventTypeMetaDataService eventTypeMetaDataService;
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
/*     */   public Response addOrUpdateAccounts(@Context HttpContext context, String bodyStr)
/*     */   {
/* 110 */     logAccountRequest(context, bodyStr);
/* 111 */     if (bodyStr == null) {
/* 112 */       throw RestError.create(StandardErrorCode.CODE_MISSING_BODY, "The body must not be null or empty.");
/*     */     }
/*     */     ArrayNode body;
/*     */     try
/*     */     {
/* 117 */       body = (ArrayNode)MAPPER.readTree(bodyStr);
/*     */     } catch (IOException e) {
/* 119 */       log.error("Could not parse request body as json", e);
/* 120 */       throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "The supplied request body could not be parsed as a json array. " + e.getMessage());
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 125 */       List<AccountConfiguration> accountConfigs = new ArrayList(body.size());
/* 126 */       for (JsonNode node : body) {
/* 127 */         AccountConfiguration accountConfiguration = toAccountConfiguration(node);
/* 128 */         accountConfigs.add(accountConfiguration);
/*     */       }
/* 130 */       this.accountManager.upsertAccountConfigurations(accountConfigs);
/* 131 */       this.eventMetaService.updateEventTypeMetaDataAndAliasFilter(accountConfigs);
/*     */     } catch (Throwable t) {
/* 133 */       throw WebAppExceptionUtil.propagateAsWebAppException(t);
/*     */     }
/*     */     
/* 136 */     return Response.noContent().build();
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
/*     */   @Path("{accountName}")
/*     */   @GET
/*     */   public Response getAccountConfiguration(@Context HttpContext context, @PathParam("accountName") String accountName)
/*     */   {
/* 151 */     logAccountRequest(context, null);
/*     */     try
/*     */     {
/* 154 */       Optional<AccountConfiguration> accountConfiguration = this.accountManager.findAccountConfiguration(accountName);
/* 155 */       if (accountConfiguration.isPresent()) {
/* 156 */         return Response.ok(accountConfiguration.get()).build();
/*     */       }
/* 158 */       throw RestError.create(StandardErrorCode.CODE_ACCOUNT_NOT_FOUND, "Account [" + accountName + "] not found");
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 162 */       throw WebAppExceptionUtil.propagateAsWebAppException(t);
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
/*     */   @Path("search")
/*     */   @POST
/*     */   public List<AccountConfiguration> getAccountConfigurations(@Context HttpContext context, ArrayNode body)
/*     */   {
/* 182 */     logAccountRequest(context, null);
/* 183 */     if (body == null) {
/* 184 */       throw RestError.create(StandardErrorCode.CODE_MISSING_BODY, "No body was supplied with the request.");
/*     */     }
/*     */     try
/*     */     {
/* 188 */       List<String> accountNames = (List)MAPPER.convertValue(body, List.class);
/* 189 */       return this.accountManager.findAccountConfigurations(accountNames);
/*     */     } catch (Throwable t) {
/* 191 */       throw WebAppExceptionUtil.propagateAsWebAppException(t);
/*     */     }
/*     */   }
/*     */   
/*     */   AccountConfiguration toAccountConfiguration(JsonNode node)
/*     */   {
/*     */     AccountConfiguration accountConfiguration;
/*     */     try {
/* 199 */       accountConfiguration = (AccountConfiguration)MAPPER.convertValue(node, AccountConfiguration.class);
/*     */     } catch (IllegalArgumentException e) {
/* 201 */       if (isIllegalAccountNameException(e)) {
/* 202 */         throw RestError.create(StandardErrorCode.CODE_INVALID_ACCOUNT_NAME, e.getMessage());
/*     */       }
/* 204 */       throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "The request body is invalid: " + e.getMessage());
/*     */     }
/*     */     
/*     */ 
/* 208 */     if ((accountConfiguration.getAccountName() == null) || (accountConfiguration.getAccessKey() == null)) {
/* 209 */       throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "Account configuration node must have both accountName and accessKey");
/*     */     }
/*     */     
/*     */ 
/* 213 */     return accountConfiguration;
/*     */   }
/*     */   
/*     */   private boolean isIllegalAccountNameException(IllegalArgumentException e) {
/* 217 */     Throwable cause = e;
/* 218 */     while ((cause = cause.getCause()) != null) {
/* 219 */       if ((cause instanceof IllegalAccountNameException)) {
/* 220 */         return true;
/*     */       }
/*     */     }
/* 223 */     return false;
/*     */   }
/*     */   
/*     */   private void logAccountRequest(HttpContext context, String request) {
/* 227 */     String method = "unknown";
/* 228 */     String path = "unknown";
/* 229 */     if (context.getRequest() != null) {
/* 230 */       method = context.getRequest().getMethod();
/* 231 */       path = context.getRequest().getPath();
/*     */     }
/* 233 */     if (Strings.isNullOrEmpty(request)) {
/* 234 */       log.info("Account Service Request: [{} {}] ", method, path);
/*     */     } else {
/* 236 */       log.info("Account Service Request: [{} {}] - request [{}]", new Object[] { method, path, request });
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/account/resource/AccountResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */