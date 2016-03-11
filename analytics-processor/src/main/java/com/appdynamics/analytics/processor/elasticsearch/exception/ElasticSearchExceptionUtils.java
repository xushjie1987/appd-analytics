/*     */ package com.appdynamics.analytics.processor.elasticsearch.exception;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailure;
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailureException;
/*     */ import com.appdynamics.analytics.processor.exception.InternalServerException;
/*     */ import com.appdynamics.analytics.processor.exception.RequestParsingException;
/*     */ import com.appdynamics.analytics.processor.exception.UnavailableException;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.elasticsearch.ElasticsearchException;
/*     */ import org.elasticsearch.action.bulk.BulkItemResponse;
/*     */ import org.elasticsearch.action.bulk.BulkItemResponse.Failure;
/*     */ import org.elasticsearch.action.bulk.BulkResponse;
/*     */ import org.elasticsearch.action.search.SearchPhaseExecutionException;
/*     */ import org.elasticsearch.rest.RestStatus;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MessageFormatter;
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
/*     */ public final class ElasticSearchExceptionUtils
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchExceptionUtils.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public static final Set<RestStatus> ELASTICSEARCH_UNAVAILABLE_ERROR_CODES = Sets.immutableEnumSet(RestStatus.REQUEST_TIMEOUT, new RestStatus[] { RestStatus.TOO_MANY_REQUESTS, RestStatus.SERVICE_UNAVAILABLE, RestStatus.GATEWAY_TIMEOUT, RestStatus.INSUFFICIENT_STORAGE });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */   public static final Set<RestStatus> ELASTICSEARCH_INTERNAL_ERROR_CODES = Sets.immutableEnumSet(RestStatus.INTERNAL_SERVER_ERROR, new RestStatus[] { RestStatus.BAD_GATEWAY });
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
/*     */   public static boolean isTransientFailure(BulkItemResponse.Failure failure)
/*     */   {
/*  75 */     if (ELASTICSEARCH_UNAVAILABLE_ERROR_CODES.contains(failure.getStatus())) {
/*  76 */       return true;
/*     */     }
/*     */     
/*  79 */     if (ELASTICSEARCH_INTERNAL_ERROR_CODES.contains(failure.getStatus())) {
/*  80 */       String message = failure.getMessage();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */       if ((message != null) && (message.startsWith("IllegalArgumentException"))) {
/*  89 */         return false;
/*     */       }
/*  91 */       return true;
/*     */     }
/*  93 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RuntimeException propagateAsEventException(ElasticsearchException e, String format, Object... args)
/*     */   {
/* 104 */     String message = MessageFormatter.arrayFormat(format, args).getMessage();
/* 105 */     throw propagateAsEventException(e, Optional.of(message));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static RuntimeException propagateAsEventException(ElasticsearchException e)
/*     */   {
/* 114 */     throw propagateAsEventException(e, Optional.absent());
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
/*     */   private static RuntimeException propagateAsEventException(ElasticsearchException e, Optional<String> message)
/*     */   {
/* 133 */     Preconditions.checkNotNull(e, "ElasticsearchException can't be null");
/* 134 */     if (ELASTICSEARCH_UNAVAILABLE_ERROR_CODES.contains(e.status())) {
/* 135 */       throw (message.isPresent() ? new UnavailableException((String)message.get(), e) : new UnavailableException(e));
/*     */     }
/*     */     
/* 138 */     if (ELASTICSEARCH_INTERNAL_ERROR_CODES.contains(e.status())) {
/* 139 */       throw (message.isPresent() ? new InternalServerException((String)message.get(), e) : new InternalServerException(e));
/*     */     }
/*     */     
/* 142 */     if ((e instanceof SearchPhaseExecutionException)) {
/* 143 */       throw (message.isPresent() ? new RequestParsingException((String)message.get(), e) : new RequestParsingException(e));
/*     */     }
/*     */     
/*     */ 
/* 147 */     throw (message.isPresent() ? new RuntimeException((String)message.get(), e) : new RuntimeException(e));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static BulkFailureException getBulkFailureException(BulkResponse response)
/*     */   {
/* 154 */     return getBulkFailureException(response, response.buildFailureMessage(), new Object[0]);
/*     */   }
/*     */   
/*     */   public static BulkFailureException getBulkFailureException(BulkResponse response, String format, Object... args) {
/* 158 */     BulkFailureException e = new BulkFailureException(String.format(format, args));
/*     */     
/* 160 */     for (BulkItemResponse itemResponse : response) {
/* 161 */       BulkFailure failure = getBulkFailure(itemResponse);
/* 162 */       if (failure != null) {
/* 163 */         e.getFailures().add(failure);
/*     */       }
/*     */     }
/*     */     
/* 167 */     return e;
/*     */   }
/*     */   
/*     */   public static BulkFailure getBulkFailure(BulkItemResponse response) {
/* 171 */     BulkFailure f = null;
/*     */     
/* 173 */     BulkItemResponse.Failure failure = response.getFailure();
/* 174 */     if (failure != null) {
/* 175 */       String status = failure.getStatus() == null ? null : failure.getStatus().toString();
/* 176 */       String message = String.format("Failed [%s] with id [%s] index [%s] type [%s]", new Object[] { failure.getMessage(), failure.getId(), failure.getIndex(), failure.getType() });
/*     */       
/* 178 */       f = new BulkFailure(status, isTransientFailure(failure), response.getItemId(), response.getId(), message);
/*     */     }
/*     */     
/* 181 */     return f;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/exception/ElasticSearchExceptionUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */