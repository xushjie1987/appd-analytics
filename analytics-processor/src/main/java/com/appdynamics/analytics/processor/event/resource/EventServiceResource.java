/*     */ package com.appdynamics.analytics.processor.event.resource;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import com.appdynamics.analytics.processor.admin.ActionType;
/*     */ import com.appdynamics.analytics.processor.admin.Locator;
/*     */ import com.appdynamics.analytics.processor.auth.AccountResolver;
/*     */ import com.appdynamics.analytics.processor.auth.SecureResource;
/*     */ import com.appdynamics.analytics.processor.event.ElasticSearchQueryHelper;
/*     */ import com.appdynamics.analytics.processor.event.EventService;
/*     */ import com.appdynamics.analytics.processor.event.EventTypeMetaData;
/*     */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeMissingException;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeRegistrationFailure;
/*     */ import com.appdynamics.analytics.processor.event.hiddenfields.HiddenField;
/*     */ import com.appdynamics.analytics.processor.event.hiddenfields.HiddenFieldsService;
/*     */ import com.appdynamics.analytics.processor.event.meter.Meters;
/*     */ import com.appdynamics.analytics.processor.event.meter.MetersStore;
/*     */ import com.appdynamics.analytics.processor.event.parsers.ObjectListParser;
/*     */ import com.appdynamics.analytics.processor.event.parsers.ObjectListParser.Factory;
/*     */ import com.appdynamics.analytics.processor.event.upsert.Upsert;
/*     */ import com.appdynamics.analytics.processor.event.upsert.UpsertCodec;
/*     */ import com.appdynamics.analytics.processor.exception.ServiceRequestException;
/*     */ import com.appdynamics.analytics.processor.exception.WebAppExceptionUtil;
/*     */ import com.appdynamics.analytics.processor.rest.BadRequestErrorCode;
/*     */ import com.appdynamics.analytics.processor.rest.RestError;
/*     */ import com.appdynamics.analytics.processor.rest.RestErrorCode;
/*     */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*     */ import com.codahale.metrics.Meter;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.inject.Inject;
/*     */ import com.sun.jersey.api.core.HttpContext;
/*     */ import com.sun.jersey.api.core.HttpRequestContext;
/*     */ import com.sun.jersey.spi.resource.Singleton;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.ws.rs.Consumes;
/*     */ import javax.ws.rs.DELETE;
/*     */ import javax.ws.rs.DefaultValue;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.PUT;
/*     */ import javax.ws.rs.Path;
/*     */ import javax.ws.rs.PathParam;
/*     */ import javax.ws.rs.Produces;
/*     */ import javax.ws.rs.QueryParam;
/*     */ import javax.ws.rs.core.Context;
/*     */ import javax.ws.rs.core.MultivaluedMap;
/*     */ import javax.ws.rs.core.Response;
/*     */ import javax.ws.rs.core.Response.ResponseBuilder;
/*     */ import javax.ws.rs.core.Response.Status;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.elasticsearch.indices.IndexMissingException;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.DateTimeZone;
/*     */ import org.joda.time.format.DateTimeFormat;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.joda.time.format.ISODateTimeFormat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ @Path("v{version: [1|2]}/events")
/*     */ @Produces({"application/json"})
/*     */ @Consumes({"application/json"})
/*     */ @SecureResource
/*     */ public class EventServiceResource
/*     */   implements RestfulEventService
/*     */ {
/* 107 */   private static final Logger log = LoggerFactory.getLogger(EventServiceResource.class);
/*     */   
/*     */   public static final int PUBLISH_DEBUG_LOG_BODY_LIMIT = 50;
/*     */   
/* 111 */   private static final Joiner COMMA_JOINER = Joiner.on(", ").skipNulls();
/* 112 */   private static final DateTimeFormatter isoDateTimeFormatter = ISODateTimeFormat.dateTime();
/* 113 */   private static final DateTimeFormatter simpleDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
/*     */   
/*     */   final EventService eventService;
/*     */   
/*     */   final HiddenFieldsService hiddenFieldsService;
/*     */   
/*     */   final IndexNameResolver indexNameResolver;
/*     */   final Meters meters;
/*     */   final MetersStore metersStore;
/*     */   final ObjectListParser.Factory publishReqParserFactory;
/*     */   final EventServiceHealthChecks healthChecks;
/*     */   final ObjectMapper mapper;
/*     */   final Locator locator;
/*     */   
/*     */   @Inject
/*     */   public EventServiceResource(EventService eventService, HiddenFieldsService hiddenFieldsService, IndexNameResolver indexNameResolver, Meters meters, MetersStore metersStore, ObjectListParser.Factory publishReqParserFactory, EventServiceHealthChecks healthChecks, Locator locator)
/*     */   {
/* 130 */     this.eventService = eventService;
/* 131 */     this.hiddenFieldsService = hiddenFieldsService;
/* 132 */     this.indexNameResolver = indexNameResolver;
/* 133 */     this.meters = meters;
/* 134 */     this.metersStore = metersStore;
/* 135 */     this.publishReqParserFactory = publishReqParserFactory;
/* 136 */     this.mapper = Reader.DEFAULT_JSON_MAPPER;
/* 137 */     this.healthChecks = healthChecks;
/* 138 */     this.locator = locator;
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{eventType}")
/*     */   @POST
/*     */   public Response registerEventType(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, String body)
/*     */   {
/* 146 */     logEventTypeRequest(Level.INFO, context, eventType, body);
/* 147 */     guardParameter("EventType", eventType);
/* 148 */     guardParameter("Body", body);
/* 149 */     String accountName = findAccountName(context);
/* 150 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.REGISTER_EVENT, version);
/*     */     try {
/* 152 */       this.eventService.registerEventType(version, accountName, eventType, body);
/*     */     } catch (Exception e) {
/* 154 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 157 */     healthCheck.getMeterSuccess().mark();
/* 158 */     return Response.ok().build();
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{eventType}")
/*     */   @PUT
/*     */   public Response updateEventType(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, String body)
/*     */   {
/* 166 */     logEventTypeRequest(Level.INFO, context, eventType, body);
/* 167 */     guardParameter("EventType", eventType);
/* 168 */     guardParameter("Body", body);
/* 169 */     String accountName = findAccountName(context);
/* 170 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.UPDATE_EVENT, version);
/*     */     try {
/* 172 */       this.eventService.updateEventType(version, accountName, eventType, body);
/*     */     } catch (Exception e) {
/* 174 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 177 */     healthCheck.getMeterSuccess().mark();
/* 178 */     return Response.ok().build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/_bulk")
/*     */   @PUT
/*     */   @SecureResource(roles={"OPS", "EUM"})
/*     */   public Response bulkUpdateEventType(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, String body)
/*     */   {
/* 188 */     logEventTypeRequest(Level.INFO, context, eventType, body);
/* 189 */     guardParameter("EventType", eventType);
/* 190 */     guardParameter("Body", body);
/* 191 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.BULK_UPDATE_EVENT, version);
/*     */     try {
/* 193 */       this.eventService.bulkUpdateEventType(version, eventType, body);
/* 194 */       healthCheck.getMeterSuccess().mark();
/*     */     } catch (ServiceRequestException e) {
/* 196 */       healthCheck.getMeterUserError().mark();
/* 197 */       throw RestError.create(new BadRequestErrorCode(e.getCode()), e.getMessage());
/*     */     } catch (IndexMissingException e) {
/* 199 */       healthCheck.getMeterUserError().mark();
/* 200 */       throw RestError.create(StandardErrorCode.CODE_MISSING_EVENT_TYPE, "An event type with the provided name doesn't exist. Choose a different name and try again.");
/*     */     }
/*     */     catch (EventTypeRegistrationFailure e) {
/* 203 */       healthCheck.getMeterError().mark();
/* 204 */       throw RestError.create(StandardErrorCode.CODE_RETRY_EVENT_TYPE, "Service was unable to process request, but this could be a temporary problem. Check request and try again");
/*     */     }
/*     */     catch (Exception e) {
/* 207 */       healthCheck.getMeterError().mark();
/* 208 */       throw Throwables.propagate(e);
/*     */     }
/* 210 */     return Response.ok().build();
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{eventType}")
/*     */   @GET
/*     */   public Response getEventType(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType)
/*     */   {
/* 218 */     logEventTypeRequest(Level.INFO, context, eventType);
/* 219 */     guardParameter("EventType", eventType);
/* 220 */     String accountName = findAccountName(context);
/*     */     
/* 222 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.GET_EVENT);
/*     */     JsonNode eventTypeDefinition;
/* 224 */     try { eventTypeDefinition = this.eventService.getEventType(version, accountName, eventType);
/*     */     } catch (Exception e) {
/* 226 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 229 */     healthCheck.getMeterSuccess().mark();
/* 230 */     return Response.ok(eventTypeDefinition).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/usage/documents")
/*     */   @GET
/*     */   public Response getEventTypeDocumentsUsage(@Context HttpContext context, @PathParam("eventType") String eventType, @QueryParam("startDateTime") String startDateStr, @QueryParam("endDateTime") String endDateStr)
/*     */   {
/* 240 */     logEventTypeRequest(Level.INFO, context, eventType);
/* 241 */     guardParameter("EventType", eventType);
/* 242 */     guardParameter("StartDateTime", startDateStr);
/* 243 */     guardParameter("EndDateTime", endDateStr);
/*     */     
/* 245 */     String accountName = findAccountName(context);
/* 246 */     DateTime startDate = toDateTime(startDateStr);
/* 247 */     DateTime endDate = toDateTime(endDateStr);
/*     */     
/* 249 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.GET_USAGE_DOCS);
/*     */     ObjectNode eventUsage;
/* 251 */     try { long usage = this.eventService.getCount(2, accountName, eventType, startDate, endDate);
/*     */       
/* 253 */       eventUsage = this.mapper.createObjectNode();
/* 254 */       eventUsage.put("startDateTime", isoDateTimeFormatter.print(startDate));
/* 255 */       eventUsage.put("endDateTime", isoDateTimeFormatter.print(endDate));
/* 256 */       eventUsage.put("usage", usage);
/*     */     } catch (Exception e) {
/* 258 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 261 */     healthCheck.getMeterSuccess().mark();
/* 262 */     return Response.ok(eventUsage).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/usage/documentFragments")
/*     */   @GET
/*     */   public Response getEventTypeDocFragmentsUsage(@Context HttpContext context, @PathParam("eventType") String eventType, @QueryParam("date") String dateStr, @QueryParam("hourOfDay") int hourOfDay)
/*     */   {
/* 272 */     logEventTypeRequest(Level.INFO, context, eventType);
/* 273 */     guardParameter("EventType", eventType);
/* 274 */     guardParameter("Dare", dateStr);
/* 275 */     guardParameter("HourOfDay", Integer.valueOf(hourOfDay));
/*     */     
/* 277 */     String accountName = findAccountName(context);
/* 278 */     DateTime utcDateAtStartOfDay = parseEventTypeUsageDate(dateStr);
/*     */     
/* 280 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.GET_USAGE_DOC_FRAGMENTS);
/*     */     try {
/* 282 */       long usage = this.metersStore.getUsageDocumentFragments(accountName, eventType, utcDateAtStartOfDay, hourOfDay);
/* 283 */       ObjectNode eventUsage = createEventTypeUsageResponse(utcDateAtStartOfDay, hourOfDay, usage);
/* 284 */       healthCheck.getMeterSuccess().mark();
/* 285 */       return Response.ok(eventUsage).build();
/*     */     } catch (Exception e) {
/* 287 */       healthCheck.getMeterError().mark();
/* 288 */       String clusterName = this.locator.findActiveClusterName(accountName);
/* 289 */       log.error("Could not get event type usage for [" + accountName + " : " + eventType + "] in cluster " + "[" + clusterName + "]", Throwables.getRootCause(e));
/*     */       
/* 291 */       throw Throwables.propagate(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/usage/bytes")
/*     */   @GET
/*     */   public Response getEventTypeBytesUsage(@Context HttpContext context, @PathParam("eventType") String eventType, @QueryParam("date") String dateStr, @QueryParam("hourOfDay") int hourOfDay)
/*     */   {
/* 302 */     logEventTypeRequest(Level.INFO, context, eventType);
/* 303 */     guardParameter("EventType", eventType);
/* 304 */     guardParameter("Dare", dateStr);
/* 305 */     guardParameter("HourOfDay", Integer.valueOf(hourOfDay));
/*     */     
/* 307 */     String accountName = findAccountName(context);
/* 308 */     DateTime utcDateAtStartOfDay = parseEventTypeUsageDate(dateStr);
/*     */     
/* 310 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.GET_USAGE_BYTES);
/*     */     try {
/* 312 */       long usage = this.metersStore.getUsageBytes(accountName, eventType, utcDateAtStartOfDay, hourOfDay);
/* 313 */       ObjectNode eventUsage = createEventTypeUsageResponse(utcDateAtStartOfDay, hourOfDay, usage);
/* 314 */       healthCheck.getMeterSuccess().mark();
/* 315 */       return Response.ok(eventUsage).build();
/*     */     } catch (Exception e) {
/* 317 */       healthCheck.getMeterError().mark();
/* 318 */       String clusterName = this.locator.findActiveClusterName(accountName);
/* 319 */       log.error("Could not get event type usage for [" + accountName + " : " + eventType + "] in cluster " + "[" + clusterName + "]", Throwables.getRootCause(e));
/*     */       
/* 321 */       throw Throwables.propagate(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private DateTime parseEventTypeUsageDate(String dateStr) {
/*     */     try {
/* 327 */       return simpleDateFormatter.parseDateTime(dateStr).withZoneRetainFields(DateTimeZone.UTC).withTimeAtStartOfDay();
/*     */     }
/*     */     catch (IllegalArgumentException e) {
/* 330 */       throw RestError.create(StandardErrorCode.CODE_INVALID_DATE_TIME, "Could not parse date time [" + dateStr + "] in format yyyy-MM-dd", e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ObjectNode createEventTypeUsageResponse(DateTime utcDateAtStartOfDay, int hourOfDay, long usage)
/*     */   {
/* 337 */     ObjectNode eventUsage = this.mapper.createObjectNode();
/* 338 */     eventUsage.put("date", isoDateTimeFormatter.print(utcDateAtStartOfDay));
/* 339 */     eventUsage.put("hourOfDay", hourOfDay);
/* 340 */     eventUsage.put("usage", usage);
/* 341 */     return eventUsage;
/*     */   }
/*     */   
/*     */   private DateTime toDateTime(String startDateStr) {
/*     */     try {
/* 346 */       return isoDateTimeFormatter.parseDateTime(startDateStr);
/*     */     } catch (IllegalArgumentException e) {
/* 348 */       throw RestError.create(StandardErrorCode.CODE_INVALID_DATE_TIME, "Could not parse date time [" + startDateStr + "]", e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Path("{eventType}")
/*     */   @DELETE
/*     */   public Response deleteEventType(@Context HttpContext context, @PathParam("eventType") String eventType)
/*     */   {
/* 358 */     logEventTypeRequest(Level.INFO, context, eventType);
/* 359 */     guardParameter("EventType", eventType);
/* 360 */     String accountName = findAccountName(context);
/* 361 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.DELETE_EVENT);
/*     */     try {
/* 363 */       this.eventService.deleteEventType(accountName, eventType);
/*     */     } catch (Exception e) {
/* 365 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 368 */     healthCheck.getMeterSuccess().mark();
/* 369 */     return Response.noContent().build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/event")
/*     */   @POST
/*     */   public Response publishEvents(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, byte[] body)
/*     */   {
/* 379 */     logPublishRequest(context, eventType, body);
/* 380 */     precondition((body != null) && (body.length > 0), StandardErrorCode.CODE_MISSING_BODY, "The request is missing a request body");
/*     */     
/* 382 */     checkForEmptyPayload(body);
/*     */     
/* 384 */     String accountName = findAccountName(context);
/*     */     
/*     */ 
/* 387 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.PUBLISH_EVENTS);
/*     */     try {
/* 389 */       ObjectListParser parser = this.publishReqParserFactory.make(body, 0, body.length);
/* 390 */       parser.parseAndCheckFormat();
/*     */       
/* 392 */       checkMeter(accountName, eventType, body.length, parser.getCurrentNumberOfPayloads(), ActionType.EVENT_PUBLISH);
/*     */       
/*     */ 
/* 395 */       this.eventService.publishEvents(version, accountName, eventType, parser, null);
/*     */     } catch (Exception e) {
/* 397 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 400 */     healthCheck.getMeterSuccess().mark();
/* 401 */     return Response.status(Response.Status.ACCEPTED).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/move")
/*     */   @POST
/*     */   public Response moveEvents(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, MoveEventTypeRequest request)
/*     */   {
/* 410 */     logPublishRequest(context, eventType, request.toString().getBytes(Charsets.UTF_8));
/*     */     
/* 412 */     String accountName = findAccountName(context);
/*     */     
/* 414 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.MOVE_EVENTS);
/*     */     try {
/* 416 */       this.eventService.moveEvents(version, accountName, eventType, request);
/*     */     } catch (Exception e) {
/* 418 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 421 */     healthCheck.getMeterSuccess().mark();
/* 422 */     return Response.status(Response.Status.NO_CONTENT).build();
/*     */   }
/*     */   
/*     */ 
/*     */   private void checkMeter(String accountName, String eventType, long numBytes, long numDocuments, ActionType actionType)
/*     */   {
/* 428 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.METER_EVENT);
/*     */     long bytesRemaining;
/* 430 */     try { bytesRemaining = this.meters.updateAndCheckBytes(accountName, eventType, numBytes);
/*     */     } catch (Exception e) {
/* 432 */       String clusterName = this.locator.findActiveClusterName(accountName);
/* 433 */       log.error("For action [" + actionType + "] meter bytes update and check failed for [" + accountName + " : " + " + " + eventType + "] in cluster " + "[" + clusterName + "], so this call will be allowed to go through (until the metering module " + "heals)", Throwables.getRootCause(e));
/*     */       
/*     */ 
/*     */ 
/* 437 */       healthCheck.getMeterError().mark();
/*     */       
/* 439 */       bytesRemaining = Long.MAX_VALUE;
/*     */     }
/*     */     long docsRemaining;
/*     */     try
/*     */     {
/* 444 */       docsRemaining = this.meters.updateAndCheckDocumentFragments(accountName, eventType, numDocuments);
/*     */     } catch (Exception e) {
/* 446 */       String clusterName = this.locator.findActiveClusterName(accountName);
/* 447 */       log.error("For action [" + actionType + "] meter document check failed for [" + accountName + " : " + eventType + "] in cluster [" + clusterName + "], " + "so this call will be allowed to go through (until the metering module heals)", Throwables.getRootCause(e));
/*     */       
/*     */ 
/*     */ 
/* 451 */       healthCheck.getMeterError().mark();
/* 452 */       docsRemaining = Long.MAX_VALUE;
/*     */     }
/*     */     
/* 455 */     if (bytesRemaining < 0L) {
/* 456 */       healthCheck.getMeterUserError().mark();
/* 457 */       long dailyBytesLimit = this.meters.getDailyBytesLimit(accountName, eventType);
/* 458 */       String message = "For action [" + actionType + "], you have reached the data volume limit of [" + dailyBytesLimit + "] bytes for account [" + accountName + "] and event type [" + eventType + "]";
/*     */       
/* 460 */       log.info(message);
/* 461 */       throw RestError.create(StandardErrorCode.CODE_EVENT_TYPE_BYTES_LIMIT_REACHED, message, message); }
/* 462 */     if (log.isDebugEnabled()) {
/* 463 */       log.debug("Event type [{}] has [{}] bytes remaining before its limit is reached.", eventType, Long.valueOf(bytesRemaining));
/*     */     }
/*     */     
/*     */ 
/* 467 */     if (docsRemaining < 0L) {
/* 468 */       healthCheck.getMeterUserError().mark();
/* 469 */       long dailyNumDocsLimit = this.meters.getDailyDocumentsLimit(accountName, eventType);
/* 470 */       String message = "For action [" + actionType + "], you have reached the document volume limit of [" + dailyNumDocsLimit + "] for account [" + accountName + "] and event type [" + eventType + "]";
/*     */       
/* 472 */       log.info(message);
/* 473 */       throw RestError.create(StandardErrorCode.CODE_EVENT_TYPE_DOC_LIMIT_REACHED, message, message); }
/* 474 */     if (log.isDebugEnabled()) {
/* 475 */       log.debug("Event type [{}] has [{}] documents remaining before its limit is reached.", eventType, Long.valueOf(bytesRemaining));
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 480 */       EventTypeMetaData eventTypeMetaData = this.eventService.getEventTypeMetaData(accountName, eventType);
/* 481 */       if (eventTypeMetaData != null) {
/* 482 */         DateTime expirationDate = eventTypeMetaData.getExpirationDate();
/* 483 */         if ((expirationDate != null) && (expirationDate.isBeforeNow())) {
/* 484 */           healthCheck.getMeterUserError().mark();
/* 485 */           String message = "Event type [" + eventType + "] for account [" + accountName + "] expired on [" + expirationDate.toString() + "]";
/*     */           
/* 487 */           log.info(message);
/* 488 */           throw RestError.create(StandardErrorCode.CODE_EVENT_TYPE_EXPIRED, message, message);
/*     */         }
/*     */       } else {
/* 491 */         throw new EventTypeMissingException(accountName, eventType);
/*     */       }
/*     */     } catch (Exception e) {
/* 494 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/event")
/*     */   @PATCH
/*     */   public Response upsertEvents(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, @QueryParam("_idPath") String idPath, @QueryParam("_mergeFields") String csvMergeFields, byte[] body)
/*     */   {
/* 506 */     logUpsertRequest(context, eventType, idPath, csvMergeFields, body);
/*     */     
/* 508 */     precondition((body != null) && (body.length > 0), StandardErrorCode.CODE_MISSING_BODY, "The request is missing a request body");
/*     */     
/* 510 */     precondition(!Strings.isNullOrEmpty(idPath), StandardErrorCode.CODE_MISSING_ID_PATH, "The request is missing the idPath query parameter");
/*     */     
/* 512 */     String accountName = findAccountName(context);
/*     */     
/* 514 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.UPSERT_EVENTS);
/*     */     try {
/* 516 */       List<? extends Upsert> upserts = UpsertCodec.decode(accountName, eventType, idPath, csvMergeFields, body, 0, body.length);
/*     */       
/* 518 */       if (upserts.size() == 0) {
/* 519 */         throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "A non-empty body must be supplied. The body is not a valid payload.");
/*     */       }
/*     */       
/* 522 */       checkMeter(accountName, eventType, body.length, upserts.size(), ActionType.EVENT_UPSERT);
/*     */       
/* 524 */       this.eventService.upsertEvents(version, upserts);
/*     */     } catch (Exception e) {
/* 526 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 529 */     healthCheck.getMeterSuccess().mark();
/* 530 */     return Response.status(Response.Status.ACCEPTED).build();
/*     */   }
/*     */   
/*     */ 
/*     */   @Path("{eventType}/search")
/*     */   @POST
/*     */   public Response searchEvents(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, String searchRequest)
/*     */   {
/* 538 */     logEventTypeRequest(Level.INFO, context, eventType, searchRequest);
/* 539 */     guardParameter("Body", searchRequest);
/* 540 */     String accountName = findAccountName(context);
/* 541 */     checkSearchMeter(accountName, eventType);
/*     */     try
/*     */     {
/* 544 */       if (version < 2) {
/* 545 */         searchRequest = ElasticSearchQueryHelper.stripWildcardQueryStringFromSearchRequest(searchRequest);
/*     */       }
/*     */       else {
/* 548 */         ElasticSearchQueryHelper.parseJsonQueryToObjectModel(searchRequest);
/*     */       }
/*     */       
/* 551 */       EventServiceStreamingSearchFacade facade = new EventServiceStreamingSearchFacade(this.indexNameResolver, this.eventService, accountName, eventType, searchRequest, this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.EVENT_SEARCH), version);
/*     */       
/*     */ 
/* 554 */       return Response.ok(facade).build();
/*     */     } catch (ServiceRequestException e) {
/* 556 */       throw RestError.create(new BadRequestErrorCode(e.getCode()), e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkSearchMeter(String accountName, @Nullable String eventType)
/*     */   {
/* 562 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.METER_EVENT);
/*     */     long searchesRemaining;
/* 564 */     try { searchesRemaining = this.meters.updateAndCheckSearchThreshold(accountName, eventType);
/*     */     } catch (Exception e) {
/* 566 */       log.error("Search update and check failed for [" + accountName + " : " + eventType + "]," + " so this call will be allowed to go through (until the metering module heals)", e);
/*     */       
/* 568 */       healthCheck.getMeterError().mark();
/*     */       
/* 570 */       searchesRemaining = Long.MAX_VALUE;
/*     */     }
/*     */     
/* 573 */     if (searchesRemaining < 0L) {
/* 574 */       healthCheck.getMeterUserError().mark();
/* 575 */       log.warn("Search limit reached on this API node for accountName [" + accountName + "] eventType [" + eventType + "]");
/*     */       
/* 577 */       String message = "You have reached the search threshold limit for event type [" + eventType + "]";
/* 578 */       throw RestError.create(StandardErrorCode.CODE_EVENT_TYPE_DOC_LIMIT_REACHED, message, message); }
/* 579 */     if (log.isDebugEnabled()) {
/* 580 */       log.debug("Event type [{}] has [{}] searches remaining before its limit is reached.", eventType, Long.valueOf(searchesRemaining));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Path("msearch")
/*     */   @POST
/*     */   public Response multiSearchEvents(@Context HttpContext context, @PathParam("version") int version, String body)
/*     */   {
/* 590 */     logEventTypeRequest(Level.INFO, context, "multisearch", body);
/* 591 */     guardParameter("Body", body);
/* 592 */     String accountName = findAccountName(context);
/* 593 */     checkSearchMeter(accountName, null);
/*     */     
/* 595 */     EventServiceStreamingMultiSearchFacade facade = new EventServiceStreamingMultiSearchFacade(this.indexNameResolver, this.eventService, accountName, body, this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.EVENT_MULTI_SEARCH), version);
/*     */     
/*     */ 
/*     */ 
/* 599 */     return Response.ok(facade).build();
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
/*     */   @Path("query")
/*     */   @POST
/*     */   public Response queryEvents(@Context HttpContext context, @PathParam("version") int version, @QueryParam("start") @DefaultValue("") String startTime, @QueryParam("end") @DefaultValue("") String endTime, @QueryParam("limit") @DefaultValue("100") int limitResults, @QueryParam("returnEsJson") @DefaultValue("false") boolean returnEsJson, String query)
/*     */   {
/* 614 */     logEventTypeRequest(Level.INFO, context, "query", query);
/* 615 */     guardParameter("Query", query);
/*     */     
/* 617 */     String accountName = findAccountName(context);
/* 618 */     checkSearchMeter(accountName, null);
/*     */     
/* 620 */     EventServiceStreamingQueryFacade facade = new EventServiceStreamingQueryFacade(this.indexNameResolver, this.eventService, accountName, query, startTime, endTime, limitResults, returnEsJson, this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.EVENT_QUERY), version);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 625 */     return Response.ok(facade).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/event/hiddenFields")
/*     */   @POST
/*     */   public Response hideFields(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, String body)
/*     */   {
/* 634 */     precondition((body != null) && (body.length() > 0), StandardErrorCode.CODE_MISSING_BODY, "The request is missing a request body");
/*     */     
/*     */ 
/* 637 */     logEventTypeRequest(Level.INFO, context, eventType, body);
/* 638 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.HIDE_EVENT_FIELDS);
/*     */     try
/*     */     {
/* 641 */       String accountName = findAccountName(context);
/* 642 */       List<HiddenField> hiddenFields = (List)this.mapper.readValue(body, this.mapper.getTypeFactory().constructCollectionType(List.class, HiddenField.class));
/*     */       
/*     */ 
/* 645 */       DateTime now = new DateTime();
/* 646 */       for (HiddenField field : hiddenFields) {
/* 647 */         field.setHiddenOnDateTime(now);
/*     */       }
/* 649 */       this.hiddenFieldsService.hideFields(accountName, eventType, hiddenFields);
/* 650 */       healthCheck.getMeterSuccess().mark();
/* 651 */       return Response.status(Response.Status.OK).build();
/*     */     } catch (Exception e) {
/* 653 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/event/hiddenFields/{fieldName}")
/*     */   @DELETE
/*     */   public Response showHiddenField(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, @PathParam("fieldName") String fieldName)
/*     */   {
/* 663 */     logEventTypeRequest(Level.INFO, context, eventType);
/* 664 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.UNHIDE_EVENT_FIELDS);
/*     */     try {
/* 666 */       String accountName = findAccountName(context);
/* 667 */       this.hiddenFieldsService.unhideField(accountName, eventType, fieldName);
/* 668 */       healthCheck.getMeterSuccess().mark();
/* 669 */       return Response.status(Response.Status.OK).build();
/*     */     } catch (Exception e) {
/* 671 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/event/hiddenFields")
/*     */   @GET
/*     */   public List<HiddenField> listHiddenFields(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType)
/*     */   {
/* 681 */     logEventTypeRequest(Level.INFO, context, eventType);
/* 682 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.LIST_HIDDEN_EVENT_FIELDS);
/*     */     try {
/* 684 */       String accountName = findAccountName(context);
/* 685 */       List<HiddenField> hiddenFields = this.hiddenFieldsService.getHiddenFields(accountName, eventType);
/* 686 */       healthCheck.getMeterSuccess().mark();
/* 687 */       return hiddenFields;
/*     */     } catch (Exception e) {
/* 689 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */   }
/*     */   
/*     */   @Path("ping")
/*     */   @GET
/*     */   public Response ping(@Context HttpContext context)
/*     */   {
/* 697 */     logEventTypeRequest(Level.INFO, context, "ping");
/*     */     
/* 699 */     String accountName = findAccountName(context);
/*     */     
/* 701 */     if (accountName == null) {
/* 702 */       throw RestError.create(StandardErrorCode.CODE_ACCOUNT_NOT_FOUND, "Account not found.");
/*     */     }
/*     */     
/* 705 */     return Response.ok().build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Path("{eventType}/relevantFields")
/*     */   @POST
/*     */   public Response relevantFields(@Context HttpContext context, @PathParam("version") int version, @PathParam("eventType") String eventType, String query)
/*     */   {
/* 717 */     logEventTypeRequest(Level.INFO, context, "relevantFields", query);
/* 718 */     guardParameter("EventType", eventType);
/* 719 */     guardParameter("Query", query);
/*     */     
/* 721 */     String accountName = findAccountName(context);
/* 722 */     checkSearchMeter(accountName, eventType);
/*     */     
/*     */ 
/* 725 */     MeteredHealthCheck healthCheck = this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.RELEVANT_FIELDS);
/*     */     JsonNode relevantValues;
/* 727 */     try { relevantValues = this.eventService.relevantFields(version, accountName, eventType, query);
/*     */     } catch (Exception e) {
/* 729 */       throw WebAppExceptionUtil.propagateAsWebAppException(e, healthCheck);
/*     */     }
/*     */     
/* 732 */     healthCheck.getMeterSuccess().mark();
/* 733 */     return Response.ok(relevantValues).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   String findAccountName(HttpContext context)
/*     */   {
/* 742 */     String accountName = AccountResolver.resolveAccountName(context);
/* 743 */     if (accountName == null) {
/* 744 */       this.healthChecks.getHealthCheck(EventServiceHealthChecks.HealthCheckDefinition.ACCOUNT_LOOKUP).getMeterUserError().mark();
/* 745 */       throw RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, "You've encountered a bug! Please report it back to AppDynamics", "No account info set in security context.");
/*     */     }
/*     */     
/*     */ 
/* 749 */     return accountName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkForEmptyPayload(byte[] body)
/*     */   {
/* 757 */     if (body.length == 2) {
/* 758 */       String payload = new String(body, Charsets.UTF_8);
/* 759 */       if (payload.equals("[]")) {
/* 760 */         throw RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "A non-empty body must be supplied.", "The body " + payload + " is not a valid payload.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void logUpsertRequest(HttpContext context, String eventType, String idPath, String csvMergeFields, byte[] body)
/*     */   {
/* 769 */     if (!log.isDebugEnabled()) {
/* 770 */       return;
/*     */     }
/* 772 */     logEventTypeRequest(Level.DEBUG, context, eventType, "upsert(" + idPath + "," + csvMergeFields + ") " + new String(body, 0, Math.min(body.length, 50), Charsets.UTF_8));
/*     */   }
/*     */   
/*     */   private void logPublishRequest(HttpContext context, String eventType, byte[] body)
/*     */   {
/* 777 */     if (!log.isDebugEnabled()) {
/* 778 */       return;
/*     */     }
/* 780 */     logEventTypeRequest(Level.DEBUG, context, eventType, new String(body, 0, Math.min(body.length, 50), Charsets.UTF_8));
/*     */   }
/*     */   
/*     */   private void logEventTypeRequest(Level level, HttpContext context, String eventType)
/*     */   {
/* 785 */     logEventTypeRequest(level, context, eventType, null);
/*     */   }
/*     */   
/*     */   private void logEventTypeRequest(Level level, HttpContext context, String eventType, String request) {
/* 789 */     if ((level == Level.DEBUG) && (!log.isDebugEnabled())) {
/* 790 */       return;
/*     */     }
/* 792 */     String accountName = AccountResolver.resolveAccountName(context);
/* 793 */     if (accountName == null) {
/* 794 */       accountName = "unknown account";
/*     */     }
/*     */     
/* 797 */     String method = "unknown";
/* 798 */     String path = "unknown";
/* 799 */     if (context.getRequest() != null) {
/* 800 */       method = context.getRequest().getMethod();
/* 801 */       path = context.getRequest().getPath();
/*     */     }
/*     */     
/* 804 */     logByLevel(level, "Event Service Request: [{} {}] - account [{}], event-type [{}], headers [{}], request [{}]", new Object[] { method, path, accountName, eventType, serializeHeaders(context), StringUtils.chomp(request) });
/*     */   }
/*     */   
/*     */   private String serializeHeaders(HttpContext context)
/*     */   {
/* 809 */     HttpRequestContext requestContext = context.getRequest();
/* 810 */     if (requestContext == null) {
/* 811 */       return "";
/*     */     }
/* 813 */     MultivaluedMap<String, String> headers = requestContext.getRequestHeaders();
/* 814 */     StringBuilder sb = new StringBuilder();
/* 815 */     for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
/* 816 */       if (sb.length() != 0) {
/* 817 */         sb.append(", ");
/*     */       }
/* 819 */       sb.append((String)entry.getKey()).append(": ").append(COMMA_JOINER.join((Iterable)entry.getValue()));
/*     */     }
/* 821 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void logByLevel(Level level, String message, Object... args) {
/* 825 */     if (level == Level.DEBUG) {
/* 826 */       log.debug(message, args);
/*     */     } else {
/* 828 */       log.info(message, args);
/*     */     }
/*     */   }
/*     */   
/*     */   void guardParameter(String parameterName, Object parameterValue) {
/* 833 */     String paramStr = parameterValue == null ? null : parameterValue.toString();
/* 834 */     guardParameter(parameterName, paramStr);
/*     */   }
/*     */   
/*     */   void guardParameter(String parameterName, String parameterValue) {
/* 838 */     precondition(!Strings.isNullOrEmpty(parameterValue), new BadRequestErrorCode("Missing." + parameterName), parameterName + " must not be null or empty.");
/*     */   }
/*     */   
/*     */   private void precondition(boolean condition, RestErrorCode error, String errorMessage)
/*     */   {
/* 843 */     if (!condition) {
/* 844 */       throw RestError.create(error, errorMessage);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/EventServiceResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */