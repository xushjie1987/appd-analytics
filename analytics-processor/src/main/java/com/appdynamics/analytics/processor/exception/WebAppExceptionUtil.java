/*     */ package com.appdynamics.analytics.processor.exception;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailureException;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeAlreadyExistsException;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeDeletionFailureException;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeMissingException;
/*     */ import com.appdynamics.analytics.processor.event.exception.EventTypeRegistrationFailure;
/*     */ import com.appdynamics.analytics.processor.event.exception.ExtractedFieldAlreadyExistsException;
/*     */ import com.appdynamics.analytics.processor.event.exception.ExtractedFieldMissingException;
/*     */ import com.appdynamics.analytics.processor.event.exception.HiddenFieldNotExistsException;
/*     */ import com.appdynamics.analytics.processor.rest.BadRequestErrorCode;
/*     */ import com.appdynamics.analytics.processor.rest.RestError;
/*     */ import com.appdynamics.analytics.processor.rest.RestError.RestErrorBody;
/*     */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*     */ import com.appdynamics.common.util.exception.Exceptions;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*     */ import com.codahale.metrics.Meter;
/*     */ import com.google.common.base.Throwables;
/*     */ import javax.ws.rs.WebApplicationException;
/*     */ import javax.ws.rs.core.Response;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebAppExceptionUtil
/*     */ {
/*  28 */   private static final Logger log = LoggerFactory.getLogger(WebAppExceptionUtil.class);
/*     */   
/*     */ 
/*     */ 
/*     */   public static WebApplicationException propagateAsWebAppException(Throwable t)
/*     */   {
/*  34 */     return propagateAsWebAppException(t, null);
/*     */   }
/*     */   
/*     */   public static WebApplicationException propagateAsWebAppException(Throwable t, MeteredHealthCheck healthCheck)
/*     */   {
/*  39 */     boolean shouldLogException = true;
/*     */     
/*  41 */     WebApplicationException e = null;
/*  42 */     if ((t instanceof WebApplicationException)) {
/*  43 */       e = (WebApplicationException)t;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */       if ((e.getResponse().getEntity() != null) && ((e.getResponse().getEntity() instanceof RestError.RestErrorBody))) {
/*  51 */         RestError.RestErrorBody body = (RestError.RestErrorBody)e.getResponse().getEntity();
/*  52 */         String code = body.getCode();
/*  53 */         if ((code != null) && ((code.equals(StandardErrorCode.CODE_EVENT_TYPE_BYTES_LIMIT_REACHED.getSubStatus())) || (code.equals(StandardErrorCode.CODE_EVENT_TYPE_DOC_LIMIT_REACHED.getSubStatus())) || (code.equals(StandardErrorCode.CODE_EVENT_TYPE_EXPIRED.getSubStatus()))))
/*     */         {
/*     */ 
/*     */ 
/*  57 */           shouldLogException = false;
/*     */         }
/*     */       }
/*     */     } else {
/*  61 */       if ((t instanceof ServiceRequestException)) {
/*  62 */         String code = ((ServiceRequestException)t).getCode();
/*  63 */         e = RestError.create(new BadRequestErrorCode(code), "Service request error.", t);
/*  64 */       } else if ((t instanceof EventTypeAlreadyExistsException)) {
/*  65 */         e = RestError.create(StandardErrorCode.CODE_CONFLICT_EVENT_TYPE, "An event type with the same name already exists.", t);
/*     */         
/*  67 */         shouldLogException = false;
/*  68 */       } else if ((t instanceof EventTypeRegistrationFailure)) {
/*  69 */         e = RestError.create(StandardErrorCode.CODE_RETRY_EVENT_TYPE, "Unable to register the event, this might be transient so suggest trying again later.", t);
/*     */       }
/*  71 */       else if ((t instanceof EventTypeMissingException)) {
/*  72 */         e = RestError.create(StandardErrorCode.CODE_MISSING_EVENT_TYPE, "Expected event type was not found.", t);
/*     */         
/*  74 */         shouldLogException = false;
/*  75 */       } else if ((t instanceof EventTypeDeletionFailureException)) {
/*  76 */         e = RestError.create(StandardErrorCode.CODE_INVALID_EVENT_TYPE, "Event type has was not deleted successfully.", t);
/*     */       }
/*  78 */       else if ((t instanceof HiddenFieldNotExistsException)) {
/*  79 */         e = RestError.create(StandardErrorCode.CODE_HIDDEN_FIELD_NOT_EXISTS, String.format("Hidden field [%s] does not exist.", new Object[] { ((HiddenFieldNotExistsException)t).getHiddenFieldName() }), t);
/*     */ 
/*     */       }
/*  82 */       else if ((t instanceof RequestParsingException)) {
/*  83 */         e = RestError.create(StandardErrorCode.CODE_UNPARSEABLE_REQUEST, "Request could not be parsed.", t);
/*  84 */       } else if ((t instanceof ExtractedFieldAlreadyExistsException)) {
/*  85 */         e = RestError.create(StandardErrorCode.CODE_ALREADY_EXISTS_EXTRACTED_FIELD, "An extracted field already exists.", t);
/*     */         
/*  87 */         shouldLogException = false;
/*  88 */       } else if ((t instanceof ExtractedFieldMissingException)) {
/*  89 */         e = RestError.create(StandardErrorCode.CODE_MISSING_EXTRACTED_FIELD, "Expected extracted field to already exist.", t);
/*     */         
/*  91 */         shouldLogException = false;
/*  92 */       } else if ((t instanceof IllegalArgumentException)) {
/*  93 */         e = RestError.create(StandardErrorCode.CODE_INVALID_REQUEST_BODY, "Request contains invalid data or illegal arguments.", t);
/*     */       }
/*     */       
/*     */ 
/*  97 */       if (e != null) {
/*  98 */         if (healthCheck != null) {
/*  99 */           healthCheck.getMeterUserError().mark();
/*     */         }
/*     */       } else {
/* 102 */         if ((t instanceof UnavailableException)) {
/* 103 */           e = RestError.create(StandardErrorCode.CODE_SERVICE_UNAVAILABLE, String.format("Service unavailable: [%s]", new Object[] { Throwables.getRootCause(t).getMessage() }), t);
/*     */ 
/*     */         }
/* 106 */         else if ((t instanceof InternalServerException)) {
/* 107 */           e = RestError.create(StandardErrorCode.CODE_INTERNAL_TRANSIENT_FAILURE, String.format("Transient server error: [%s]", new Object[] { Throwables.getRootCause(t).getMessage() }), t);
/*     */ 
/*     */         }
/* 110 */         else if ((t instanceof BulkFailureException)) {
/* 111 */           e = RestError.create(StandardErrorCode.CODE_BULK_ERROR, t.getMessage(), ((BulkFailureException)t).toJson());
/*     */         }
/*     */         else {
/* 114 */           e = RestError.create(StandardErrorCode.CODE_UNKNOWN_FAILURE, String.format("Unknown server error: [%s]", new Object[] { Throwables.getRootCause(t).getMessage() }), t);
/*     */         }
/*     */         
/*     */ 
/* 118 */         if (healthCheck != null) {
/* 119 */           healthCheck.getMeterError().mark();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 126 */     if (shouldLogException) {
/* 127 */       log.error("Error occurred", t);
/*     */     } else {
/* 129 */       log.warn(Exceptions.collectMessages(t));
/*     */     }
/* 131 */     throw e;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/exception/WebAppExceptionUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */