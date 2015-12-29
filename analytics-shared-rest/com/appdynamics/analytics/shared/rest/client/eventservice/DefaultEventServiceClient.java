/*     */ package com.appdynamics.analytics.shared.rest.client.eventservice;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.client.eventservice.creator.EventTypeCreator;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.AbstractAnalyticsClient;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.AbstractAnalyticsClient.Builder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpEntityEnclosingRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpRequestFactory;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Joiner;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
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
/*     */ public class DefaultEventServiceClient
/*     */   extends AbstractAnalyticsClient
/*     */   implements EventServiceClient
/*     */ {
/*  45 */   private static final Logger log = LoggerFactory.getLogger(DefaultEventServiceClient.class);
/*     */   
/*     */   private static final String CURRENT_REST_VERSION = "v2";
/*     */   
/*  49 */   private static final Joiner COMMA_JOINER = Joiner.on(",");
/*     */   
/*     */   private final Set<EventTypeCreator> eventTypeCreators;
/*     */   
/*     */   private final Set<AccountNameEventTypeKey> createdOrUpdatedEventTypes;
/*     */   
/*     */   private final Set<AccountNameEventTypeKey> eventTypesWithoutCreators;
/*     */   
/*     */   private final ThreadLocal<AccountNameEventTypeKey> tlsAccountNameEventTypeKey;
/*     */   
/*     */   private DefaultEventServiceClient(ObjectMapper mapper, CloseableHttpClient client, URI baseUri)
/*     */   {
/*  61 */     super(mapper, client, baseUri);
/*  62 */     this.eventTypeCreators = Collections.newSetFromMap(new ConcurrentHashMap());
/*  63 */     this.createdOrUpdatedEventTypes = Collections.newSetFromMap(new ConcurrentHashMap());
/*     */     
/*  65 */     this.eventTypesWithoutCreators = Collections.newSetFromMap(new ConcurrentHashMap());
/*     */     
/*  67 */     this.tlsAccountNameEventTypeKey = new ThreadLocal()
/*     */     {
/*     */       protected DefaultEventServiceClient.AccountNameEventTypeKey initialValue() {
/*  70 */         return new DefaultEventServiceClient.AccountNameEventTypeKey("", "");
/*     */       }
/*     */     };
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
/*     */   public static AbstractAnalyticsClient.Builder<DefaultEventServiceClient> builder(String hostName, int port)
/*     */     throws RestException
/*     */   {
/*  90 */     new AbstractAnalyticsClient.Builder(hostName, port)
/*     */     {
/*     */       protected DefaultEventServiceClient buildInternal() {
/*  93 */         URI baseUri = buildBaseUri(DefaultEventServiceClient.access$000());
/*  94 */         return new DefaultEventServiceClient(this.mapper, this.client, baseUri, null);
/*     */       }
/*     */     };
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
/*     */   public static AbstractAnalyticsClient.Builder<DefaultEventServiceClient> builder(String scheme, String hostName, int port)
/*     */     throws RestException
/*     */   {
/* 116 */     new AbstractAnalyticsClient.Builder(scheme, hostName, port)
/*     */     {
/*     */       protected DefaultEventServiceClient buildInternal() {
/* 119 */         URI baseUri = buildBaseUri(DefaultEventServiceClient.access$000());
/* 120 */         return new DefaultEventServiceClient(this.mapper, this.client, baseUri, null);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static String getBaseUri() {
/* 126 */     return "/v2/events";
/*     */   }
/*     */   
/*     */   public void registerEventType(String accountName, String accessKey, String eventType, String body)
/*     */     throws RestException
/*     */   {
/* 132 */     ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath(eventType, new String[0])).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(body).expecting(200)).executeAndReturnRawResponseString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateEventType(String accountName, String accessKey, String eventType, String body)
/*     */     throws RestException
/*     */   {
/*     */     try
/*     */     {
/* 143 */       ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().put().appendPath(eventType, new String[0])).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(body).expecting(200)).executeAndReturnRawResponseString();
/*     */ 
/*     */     }
/*     */     catch (RestException e)
/*     */     {
/*     */ 
/* 149 */       invalidateLocalCacheIfEventTypeNotFound(e, accountName, eventType);
/* 150 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getEventType(String accountName, String accessKey, String eventType) throws RestException
/*     */   {
/* 156 */     satisfyCreators(accountName, accessKey, eventType);
/*     */     try {
/* 158 */       return ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(eventType, new String[0])).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).executeAndReturnRawResponseString();
/*     */ 
/*     */     }
/*     */     catch (RestException e)
/*     */     {
/* 163 */       if (e.getStatusCode() == 404) {
/* 164 */         invalidateLocalCacheIfEventTypeNotFound(e, accountName, eventType);
/* 165 */         return null;
/*     */       }
/* 167 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getEventTypeUsageNumDocuments(String accountName, String accessKey, String eventType, String startDateTime, String endDateTime)
/*     */     throws RestException
/*     */   {
/* 174 */     satisfyCreators(accountName, accessKey, eventType);
/*     */     try {
/* 176 */       return ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(eventType, new String[] { "usage", "documents" })).addQueryParam("startDateTime", startDateTime)).addQueryParam("endDateTime", endDateTime)).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).executeAndReturnRawResponseString();
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (RestException e)
/*     */     {
/*     */ 
/* 183 */       if (e.getStatusCode() == 404) {
/* 184 */         invalidateLocalCacheIfEventTypeNotFound(e, accountName, eventType);
/* 185 */         return null;
/*     */       }
/* 187 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getEventTypeUsageNumDocumentFragments(String accountName, String accessKey, String eventType, String date, int hourOfDay)
/*     */     throws RestException
/*     */   {
/* 194 */     satisfyCreators(accountName, accessKey, eventType);
/*     */     try {
/* 196 */       return ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(eventType, new String[] { "usage", "documentFragments" })).addQueryParam("date", date)).addQueryParam("hourOfDay", Integer.toString(hourOfDay))).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).executeAndReturnRawResponseString();
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (RestException e)
/*     */     {
/*     */ 
/* 203 */       if (e.getStatusCode() == 404) {
/* 204 */         invalidateLocalCacheIfEventTypeNotFound(e, accountName, eventType);
/* 205 */         return null;
/*     */       }
/* 207 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getEventTypeUsageNumBytes(String accountName, String accessKey, String eventType, String date, int hourOfDay)
/*     */     throws RestException
/*     */   {
/* 214 */     satisfyCreators(accountName, accessKey, eventType);
/*     */     try {
/* 216 */       return ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(eventType, new String[] { "usage", "bytes" })).addQueryParam("date", date)).addQueryParam("hourOfDay", Integer.toString(hourOfDay))).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).executeAndReturnRawResponseString();
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (RestException e)
/*     */     {
/*     */ 
/* 223 */       if (e.getStatusCode() == 404) {
/* 224 */         invalidateLocalCacheIfEventTypeNotFound(e, accountName, eventType);
/* 225 */         return null;
/*     */       }
/* 227 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteEventType(String accountName, String accessKey, String eventType) throws RestException
/*     */   {
/*     */     try {
/* 234 */       ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().delete().appendPath(eventType, new String[0])).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(204)).executeAndReturnRawResponseString();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 240 */       invalidateLocalEventTypeCache(accountName, eventType);
/*     */     } catch (RestException e) {
/* 242 */       if (e.getStatusCode() != 404) {
/* 243 */         throw e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void publishEvents(String accountName, String accessKey, String eventType, String body)
/*     */     throws RestException
/*     */   {
/* 251 */     satisfyCreators(accountName, accessKey, eventType);
/*     */     try {
/* 253 */       ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath(eventType, new String[] { "event" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(body).expecting(202)).executeAndReturnRawResponseString();
/*     */ 
/*     */     }
/*     */     catch (RestException e)
/*     */     {
/*     */ 
/* 259 */       invalidateLocalCacheIfEventTypeNotFound(e, accountName, eventType);
/* 260 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void upsertEvents(String accountName, String accessKey, String eventType, String body, String idPath, List<String> mergeFields)
/*     */     throws RestException
/*     */   {
/* 272 */     satisfyCreators(accountName, accessKey, eventType);
/*     */     try {
/* 274 */       ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().patch().appendPath(eventType, new String[] { "event" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(body).addQueryParam("_idPath", idPath)).addQueryParam("_mergeFields", COMMA_JOINER.join(mergeFields))).expecting(202)).executeAndReturnRawResponseString();
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (RestException e)
/*     */     {
/*     */ 
/*     */ 
/* 283 */       invalidateLocalCacheIfEventTypeNotFound(e, accountName, eventType);
/* 284 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */   public String searchEvents(String accountName, String accessKey, String eventType, String searchRequest)
/*     */     throws RestException
/*     */   {
/* 291 */     satisfyCreators(accountName, accessKey, eventType);
/*     */     try {
/* 293 */       return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath(eventType, new String[] { "search" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(searchRequest).expecting(200)).executeAndReturnRawResponseString();
/*     */ 
/*     */     }
/*     */     catch (RestException e)
/*     */     {
/*     */ 
/* 299 */       invalidateLocalCacheIfEventTypeNotFound(e, accountName, eventType);
/* 300 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String multiSearchEvents(String accountName, String accessKey, String multiSearchRequest)
/*     */     throws RestException
/*     */   {
/* 308 */     return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath("msearch", new String[0])).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(multiSearchRequest).expecting(200)).executeAndReturnRawResponseString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String queryEvents(String accountName, String accessKey, String queryString, String startTime, String endTime, String limitResults, boolean returnEsJson)
/*     */     throws RestException
/*     */   {
/* 319 */     return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath("query", new String[0])).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(queryString).addQueryParam("start", startTime)).addQueryParam("end", endTime)).addQueryParam("limit", limitResults)).addQueryParam("returnEsJson", Boolean.toString(returnEsJson))).expecting(200)).executeAndReturnRawResponseString();
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
/*     */   public void registerEventTypeCreator(EventTypeCreator... creators)
/*     */   {
/* 332 */     this.eventTypeCreators.addAll(Arrays.asList(creators));
/* 333 */     this.eventTypesWithoutCreators.clear();
/*     */   }
/*     */   
/*     */   public String relevantFields(String accountName, String accessKey, String eventType, String significantTermsQuery)
/*     */     throws RestException
/*     */   {
/* 339 */     satisfyCreators(accountName, accessKey, eventType);
/* 340 */     return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath(eventType, new String[] { "relevantFields" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(significantTermsQuery).expecting(200)).executeAndReturnRawResponseString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void ping(String accountName, String accessKey)
/*     */     throws RestException
/*     */   {
/* 349 */     ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath("ping", new String[0])).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).executeAndReturnRawResponseString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void hideFields(String accountName, String accessKey, String eventType, List<EventServiceClient.HiddenField> fields)
/*     */   {
/* 357 */     ArrayNode body = getMapper().createArrayNode();
/* 358 */     for (EventServiceClient.HiddenField hiddenField : fields) {
/* 359 */       body.add((JsonNode)getMapper().convertValue(hiddenField, JsonNode.class));
/*     */     }
/*     */     
/* 362 */     ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath(eventType, new String[] { "event", "hiddenFields" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(body).expecting(200)).executeAndReturnRawResponseString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<EventServiceClient.HiddenField> listHiddenFields(String accountName, String accessKey, String eventType)
/*     */     throws RestException, IOException
/*     */   {
/* 372 */     String response = ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(eventType, new String[] { "event", "hiddenFields" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).executeAndReturnRawResponseString();
/*     */     
/*     */ 
/*     */ 
/* 376 */     return (List)getMapper().readValue(response, getMapper().getTypeFactory().constructCollectionType(List.class, EventServiceClient.HiddenField.class));
/*     */   }
/*     */   
/*     */ 
/*     */   public void unhideField(String accountName, String accessKey, String eventType, String fieldName)
/*     */   {
/* 382 */     ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().delete().appendPath(eventType, new String[] { "event", "hiddenFields", fieldName })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).executeAndReturnRawResponseString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void satisfyCreators(String accountName, String accessKey, String eventType)
/*     */   {
/* 389 */     AccountNameEventTypeKey key = (AccountNameEventTypeKey)this.tlsAccountNameEventTypeKey.get();
/* 390 */     key.reuse(accountName, eventType);
/* 391 */     if ((this.createdOrUpdatedEventTypes.contains(key)) || (this.eventTypesWithoutCreators.contains(key))) {
/* 392 */       return;
/*     */     }
/*     */     
/* 395 */     EventTypeCreator creator = findEventTypeCreator(eventType);
/* 396 */     if (log.isDebugEnabled()) {
/* 397 */       log.debug("Found creator {} for eventType {}", creator, eventType);
/*     */     }
/* 399 */     if (creator == null) {
/* 400 */       this.eventTypesWithoutCreators.add(key.clone());
/* 401 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 405 */       log.info("Registering event type {} for account {}", eventType, accountName);
/* 406 */       registerEventType(accountName, accessKey, eventType, creator.getEventTypeMapping());
/* 407 */       this.createdOrUpdatedEventTypes.add(key.clone());
/*     */     } catch (RestException e) {
/* 409 */       if ((e.getStatusCode() == 409) && ("Conflict.EventType".equals(e.getCode()))) {
/* 410 */         log.info("Event type already registered, updating event type {} for account {}", eventType, accountName);
/*     */         
/* 412 */         updateEventType(accountName, accessKey, eventType, creator.getEventTypeMapping());
/* 413 */         this.createdOrUpdatedEventTypes.add(key.clone());
/*     */       } else {
/* 415 */         throw e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private EventTypeCreator findEventTypeCreator(String eventType) {
/* 421 */     for (EventTypeCreator creator : this.eventTypeCreators) {
/* 422 */       if (creator.getEventTypeName().equals(eventType)) {
/* 423 */         return creator;
/*     */       }
/*     */     }
/* 426 */     return null;
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
/*     */   private void invalidateLocalCacheIfEventTypeNotFound(RestException e, String accountName, String eventType)
/*     */   {
/* 441 */     if ((e.getStatusCode() == 404) && ("Missing.EventType".equals(e.getCode()))) {
/* 442 */       invalidateLocalEventTypeCache(accountName, eventType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void invalidateLocalEventTypeCache(String accountName, String eventType)
/*     */   {
/* 454 */     AccountNameEventTypeKey key = (AccountNameEventTypeKey)this.tlsAccountNameEventTypeKey.get();
/* 455 */     key.reuse(accountName, eventType);
/* 456 */     this.createdOrUpdatedEventTypes.remove(key);
/* 457 */     log.debug("Removed event type [{}] associated to account [{}] from the local cache", eventType, accountName);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class AccountNameEventTypeKey implements Cloneable { private String accountName;
/*     */     private String eventType;
/*     */     
/*     */     @ConstructorProperties({"accountName", "eventType"})
/* 465 */     public AccountNameEventTypeKey(String accountName, String eventType) { this.accountName = accountName;this.eventType = eventType; }
/* 466 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof AccountNameEventTypeKey)) return false; AccountNameEventTypeKey other = (AccountNameEventTypeKey)o; if (!other.canEqual(this)) return false; Object this$accountName = this.accountName;Object other$accountName = other.accountName; if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$eventType = this.eventType;Object other$eventType = other.eventType;return this$eventType == null ? other$eventType == null : this$eventType.equals(other$eventType); } public boolean canEqual(Object other) { return other instanceof AccountNameEventTypeKey; } public int hashCode() { int PRIME = 31;int result = 1;Object $accountName = this.accountName;result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $eventType = this.eventType;result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void reuse(String accountName, String eventType)
/*     */     {
/* 473 */       this.accountName = accountName;
/* 474 */       this.eventType = eventType;
/*     */     }
/*     */     
/*     */     public AccountNameEventTypeKey clone()
/*     */     {
/* 479 */       return new AccountNameEventTypeKey(this.accountName, this.eventType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/DefaultEventServiceClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */