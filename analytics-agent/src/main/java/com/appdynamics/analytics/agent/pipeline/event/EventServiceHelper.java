/*     */ package com.appdynamics.analytics.agent.pipeline.event;
/*     */ 
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.appdynamics.common.io.codec.Json;
/*     */ import com.appdynamics.common.io.codec.Json.ScanResult;
/*     */ import com.appdynamics.common.io.codec.Json.Walker;
/*     */ import com.appdynamics.common.util.exception.PermanentException;
/*     */ import com.appdynamics.common.util.misc.ThreadLocalObjects;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.ws.rs.core.Response.Status;
/*     */ import org.slf4j.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EventServiceHelper
/*     */ {
/*     */   private static final String FAILURES_FIELD = "failures";
/*     */   private static final String BULK_REQUEST_INDEX_FIELD = "bulkRequestIndex";
/*     */   private static final String TRANSIENT_FIELD = "transient";
/*     */   private static final String FAILURE_MESSAGE_FIELD = "message";
/*     */   
/*     */   public static void handleRestError(Logger log, MessagePack<String, String> inputSource, String input, boolean isBatchingEnabled, RestException ex)
/*     */   {
/*  69 */     Preconditions.checkNotNull(log, "log can't be null");
/*  70 */     Preconditions.checkNotNull(inputSource, "inputSource can't be null");
/*  71 */     Preconditions.checkNotNull(ex, "ex can't be null");
/*     */     
/*  73 */     Response.Status status = Response.Status.fromStatusCode(ex.getStatusCode());
/*  74 */     String code = ex.getCode();
/*     */     
/*  76 */     if (status == null) {
/*  77 */       handleTransientError(log, inputSource, input, isBatchingEnabled, ex, String.format("The received status code [%d] was not a known value", new Object[] { Integer.valueOf(ex.getStatusCode()) }));
/*     */       
/*  79 */       return;
/*     */     }
/*     */     
/*  82 */     switch (status.getFamily())
/*     */     {
/*     */     case INFORMATIONAL: 
/*     */     case SUCCESSFUL: 
/*  86 */       handlePermanentError(log, inputSource, input, ex, "The exception seems to contradict the status code");
/*  87 */       return;
/*     */     
/*     */     case CLIENT_ERROR: 
/*  90 */       switch (status) {
/*     */       case BAD_REQUEST: 
/*  92 */         if ("BulkError.BadRequest".equals(code)) {
/*  93 */           handleBulkErrorResponse(log, inputSource, input, isBatchingEnabled, ex);
/*  94 */           return;
/*     */         }
/*     */         
/*  97 */         handlePermanentError(log, inputSource, input, ex, "Message could not be delivered since the request was invalid");
/*     */         
/*  99 */         return;
/*     */       case PRECONDITION_FAILED: 
/* 101 */         handlePermanentError(log, inputSource, input, ex, "Message could not be delivered because it appears to be corrupt or structured badly");
/*     */         
/* 103 */         return;
/*     */       case NOT_ACCEPTABLE: 
/* 105 */         handlePermanentError(log, inputSource, input, ex, "Message could not be delivered because the REST resource rejected it");
/*     */         
/* 107 */         return;
/*     */       case NOT_FOUND: 
/* 109 */         if ("Missing.EventType".equals(code))
/*     */         {
/*     */ 
/* 112 */           handleTransientError(log, inputSource, input, isBatchingEnabled, ex, "Message could not be delivered because the event type is not registered, retrying in some time");
/*     */           
/* 114 */           return;
/*     */         }
/*     */         
/* 117 */         handlePermanentError(log, inputSource, input, ex, "Message could not be delivered because the REST resource was not found");
/*     */         
/* 119 */         return;
/*     */       }
/* 121 */       handlePermanentError(log, inputSource, input, ex, "Message could not be delivered due to a serious error");
/*     */       
/* 123 */       return;
/*     */     
/*     */ 
/*     */     case REDIRECTION: 
/* 127 */       handlePermanentError(log, inputSource, input, ex, "Message could not be delivered because the REST resource appears to have moved");
/*     */       
/* 129 */       return;
/*     */     case SERVER_ERROR: 
/* 131 */       handleTransientError(log, inputSource, input, isBatchingEnabled, ex, "Message could not be delivered due to a server error");
/*     */       
/* 133 */       return;
/*     */     }
/* 135 */     handlePermanentError(log, inputSource, input, ex, "Message could not be delivered due to an unexpected error");
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
/*     */ 
/*     */   public static void handlePermanentError(Logger log, MessagePack<String, String> inputSource, String input, Throwable ex, String errorMessage)
/*     */   {
/* 156 */     Preconditions.checkNotNull(log, "log can't be null");
/* 157 */     Preconditions.checkNotNull(inputSource, "inputSource can't be null");
/*     */     
/* 159 */     log.warn("Permanent error encountered due to the following cause: [{}]", errorMessage, ex);
/*     */     
/* 161 */     if (input == null) {
/* 162 */       log.warn("The original input message corresponding to the permanent error was null so it will not be placed back into the message pack");
/*     */       
/* 164 */       return;
/*     */     }
/*     */     
/* 167 */     inputSource.returnUndelivered(input, new PermanentException(ex));
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
/*     */ 
/*     */ 
/*     */   public static void handleTransientError(Logger log, MessagePack<String, String> inputSource, String input, boolean isBatchingEnabled, Throwable ex, String errorMessage)
/*     */   {
/* 189 */     Preconditions.checkNotNull(log, "log can't be null");
/* 190 */     Preconditions.checkNotNull(inputSource, "inputSource can't be null");
/*     */     
/* 192 */     log.warn("Transient error encountered due to the following cause: [{}]", errorMessage, ex);
/*     */     
/* 194 */     if (input == null) {
/* 195 */       log.warn("The original input message corresponding to the transient error was null so it will not be placed back into the message pack");
/*     */       
/* 197 */       return;
/*     */     }
/*     */     
/* 200 */     if (isBatchingEnabled)
/*     */     {
/*     */ 
/*     */ 
/* 204 */       if ((input.length() >= 2) && (input.charAt(0) == '[') && (input.charAt(input.length() - 1) == ']')) {
/* 205 */         input = input.substring(1, input.length() - 1);
/* 206 */         inputSource.returnUndelivered(input);
/*     */       } else {
/* 208 */         inputSource.returnUndelivered(input, new IllegalArgumentException(String.format("Batch enabled message [%s] was not properly enclosed within square brackets.  Since the message is not formatted properly it will not be re-circulated back onto the message pack queue.", new Object[] { input })));
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else {
/* 214 */       inputSource.returnUndelivered(input);
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
/*     */   public static void handleBulkErrorResponse(Logger log, MessagePack<String, String> inputSource, String input, boolean isBatchingEnabled, RestException ex)
/*     */   {
/* 259 */     Preconditions.checkNotNull(log, "log can't be null");
/* 260 */     Preconditions.checkNotNull(inputSource, "inputSource can't be null");
/* 261 */     Preconditions.checkNotNull(ex, "ex can't be null");
/*     */     
/* 263 */     if (input == null) {
/* 264 */       log.warn("The original input message corresponding to the bulk error response was null so it will not be placed back into the message pack", ex);
/*     */       
/* 266 */       return;
/*     */     }
/*     */     
/* 269 */     String developerMessage = ex.getDeveloperMessage();
/*     */     
/*     */ 
/*     */ 
/* 273 */     if (Strings.isNullOrEmpty(developerMessage)) {
/* 274 */       handlePermanentError(log, inputSource, input, ex, "Failed to parse bulk error response since the developerMessage field was blank");
/*     */       
/* 276 */       return;
/*     */     }
/*     */     
/* 279 */     Json.Walker walker = ThreadLocalObjects.jsonWalker();
/*     */     try {
/* 281 */       JsonNode failures = getFailures(walker, developerMessage);
/* 282 */       if ((failures == null) || (!failures.isArray())) {
/* 283 */         handlePermanentError(log, inputSource, input, ex, "Failed to parse bulk error response since the failures field was not an array type");
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 288 */         Map<Integer, String> transientErrorIndexAndMessagePairs = new HashMap();
/* 289 */         Map<Integer, String> permanentErrorIndexAndMessagePairs = new HashMap();
/* 290 */         populateErrorIndexAndMessagePairs(log, walker, failures, transientErrorIndexAndMessagePairs, permanentErrorIndexAndMessagePairs);
/*     */         
/* 292 */         extractMessagesFromInput(log, walker, inputSource, input, isBatchingEnabled, transientErrorIndexAndMessagePairs, permanentErrorIndexAndMessagePairs);
/*     */       }
/*     */       
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 298 */       handlePermanentError(log, inputSource, input, e, "Failed to parse the bulk error response");
/*     */     } finally {
/* 300 */       walker.endWalk();
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
/*     */   @Nullable
/*     */   private static JsonNode getFailures(Json.Walker walker, String developerMessage)
/*     */     throws IOException
/*     */   {
/* 316 */     byte[] developerMessageBytes = developerMessage.getBytes(Charsets.UTF_8);
/*     */     
/* 318 */     HashMap<String, JsonNode> fieldNamesToFind = new HashMap(2);
/* 319 */     fieldNamesToFind.put("failures", null);
/* 320 */     Json.readTopLevelFields(walker, developerMessageBytes, 0, developerMessageBytes.length, fieldNamesToFind);
/*     */     
/*     */ 
/* 323 */     return (JsonNode)fieldNamesToFind.get("failures");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void populateErrorIndexAndMessagePairs(Logger log, Json.Walker walker, JsonNode failures, Map<Integer, String> transientErrorIndexAndMessagePairs, Map<Integer, String> permanentErrorIndexAndMessagePairs)
/*     */     throws IOException
/*     */   {
/* 347 */     byte[] failureBytes = failures.toString().getBytes(Charsets.UTF_8);
/* 348 */     List<Json.ScanResult> scanResults = Json.scanChildrenOfArray(walker, failureBytes, 0, failureBytes.length);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 353 */     for (Json.ScanResult result : scanResults) {
/* 354 */       int rangeOffset = ((Integer)result.getLeft()).intValue();
/* 355 */       int rangeLength = ((Integer)result.getRight()).intValue();
/*     */       
/* 357 */       Map<String, JsonNode> fieldNamesToFind = new HashMap(4);
/* 358 */       fieldNamesToFind.put("bulkRequestIndex", null);
/* 359 */       fieldNamesToFind.put("transient", null);
/* 360 */       fieldNamesToFind.put("message", null);
/* 361 */       Json.readTopLevelFields(walker, failureBytes, rangeOffset, rangeLength, fieldNamesToFind);
/*     */       
/* 363 */       JsonNode isTransientNode = (JsonNode)fieldNamesToFind.get("transient");
/* 364 */       JsonNode bulkRequestIndexNode = (JsonNode)fieldNamesToFind.get("bulkRequestIndex");
/* 365 */       JsonNode failureMessageNode = (JsonNode)fieldNamesToFind.get("message");
/*     */       
/* 367 */       boolean isTransient = (isTransientNode != null) && (isTransientNode.asBoolean());
/* 368 */       int bulkRequestIndex = bulkRequestIndexNode == null ? -1 : bulkRequestIndexNode.asInt(-1);
/* 369 */       String failureMessage = failureMessageNode == null ? null : failureMessageNode.asText();
/*     */       
/* 371 */       if ((isTransient) && (bulkRequestIndex >= 0)) {
/* 372 */         transientErrorIndexAndMessagePairs.put(Integer.valueOf(bulkRequestIndex), failureMessage);
/* 373 */       } else if (bulkRequestIndex >= 0) {
/* 374 */         permanentErrorIndexAndMessagePairs.put(Integer.valueOf(bulkRequestIndex), failureMessage);
/*     */       } else {
/* 376 */         log.warn("Failure message [{}] from bulk error response contained invalid bulk request index [{}]", failureMessage, Integer.valueOf(bulkRequestIndex));
/*     */       }
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
/*     */   private static void extractMessagesFromInput(Logger log, Json.Walker walker, MessagePack<String, String> inputSource, String input, boolean isBatchingEnabled, Map<Integer, String> transientErrorIndexAndMessagePairs, Map<Integer, String> permanentErrorIndexAndMessagePairs)
/*     */     throws IOException
/*     */   {
/* 409 */     StringBuilder transientErrorMessages = ThreadLocalObjects.stringBuilder();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 417 */       if ((!transientErrorIndexAndMessagePairs.isEmpty()) && (!isBatchingEnabled)) {
/* 418 */         transientErrorMessages.append('[');
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 423 */       byte[] inputBytes = input.getBytes(Charsets.UTF_8);
/* 424 */       List<Json.ScanResult> scanResults = Json.scanChildrenOfArray(walker, inputBytes, 0, inputBytes.length);
/*     */       
/* 426 */       int arrayIndex = 0;
/* 427 */       boolean isFirst = true;
/* 428 */       for (Json.ScanResult result : scanResults) {
/* 429 */         int rangeOffset = ((Integer)result.getLeft()).intValue();
/* 430 */         int rangeLength = ((Integer)result.getRight()).intValue();
/*     */         
/* 432 */         if (transientErrorIndexAndMessagePairs.containsKey(Integer.valueOf(arrayIndex))) {
/* 433 */           if (isFirst) {
/* 434 */             isFirst = false;
/*     */           } else {
/* 436 */             transientErrorMessages.append(',');
/*     */           }
/*     */           
/* 439 */           String transientErrorMessage = new String(Arrays.copyOfRange(inputBytes, rangeOffset, rangeOffset + rangeLength), Charsets.UTF_8);
/*     */           
/* 441 */           log.warn("The message at index [{}] failed due to the following transient error: [{}]", Integer.valueOf(arrayIndex), transientErrorIndexAndMessagePairs.get(Integer.valueOf(arrayIndex)));
/*     */           
/* 443 */           transientErrorMessages.append(transientErrorMessage);
/* 444 */         } else if (permanentErrorIndexAndMessagePairs.containsKey(Integer.valueOf(arrayIndex))) {
/* 445 */           String permanentErrorMessage = new String(Arrays.copyOfRange(inputBytes, rangeOffset, rangeOffset + rangeLength), Charsets.UTF_8);
/*     */           
/* 447 */           log.warn("The message at index [{}] failed due to the following permanent error: [{}]", Integer.valueOf(arrayIndex), permanentErrorIndexAndMessagePairs.get(Integer.valueOf(arrayIndex)));
/*     */           
/* 449 */           inputSource.returnUndelivered(permanentErrorMessage, new PermanentException((String)permanentErrorIndexAndMessagePairs.get(Integer.valueOf(arrayIndex))));
/*     */         }
/*     */         
/*     */ 
/* 453 */         arrayIndex++;
/*     */       }
/*     */       
/* 456 */       if (transientErrorMessages.length() > 0) {
/* 457 */         if (!isBatchingEnabled) {
/* 458 */           transientErrorMessages.append(']');
/*     */         }
/*     */         
/* 461 */         inputSource.returnUndelivered(transientErrorMessages.toString());
/*     */       }
/*     */     } finally {
/* 464 */       transientErrorMessages.setLength(0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/event/EventServiceHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */