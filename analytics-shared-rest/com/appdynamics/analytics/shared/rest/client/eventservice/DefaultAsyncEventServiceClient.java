/*     */ package com.appdynamics.analytics.shared.rest.client.eventservice;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.client.eventservice.creator.EventTypeCreator;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.AbstractAsyncAnalyticsClient;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.AbstractAsyncAnalyticsClient.Builder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.GenericHttpRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpEntityEnclosingRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpRequestFactory;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.LazyHttpRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Joiner;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import rx.Observable;
/*     */ import rx.functions.Action0;
/*     */ import rx.functions.Action1;
/*     */ import rx.observables.ConnectableObservable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultAsyncEventServiceClient
/*     */   extends AbstractAsyncAnalyticsClient
/*     */   implements AsyncEventServiceClient
/*     */ {
/*  39 */   private static final Logger log = LoggerFactory.getLogger(DefaultAsyncEventServiceClient.class);
/*     */   
/*     */   public static class Builder extends AbstractAsyncAnalyticsClient.Builder<AsyncEventServiceClient> {
/*  42 */     private List<EventTypeCreator> creators = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder eventTypeCreators(EventTypeCreator... creators)
/*     */     {
/*  53 */       this.creators.addAll(Arrays.asList(creators));
/*  54 */       return this;
/*     */     }
/*     */     
/*     */     protected AsyncEventServiceClient buildInternal()
/*     */     {
/*  59 */       return new DefaultAsyncEventServiceClient(createExectorService(), getOrCreateAsyncClient(), this.mapper, buildBaseUri("/v2/events"), this.creators);
/*     */     }
/*     */     
/*     */     protected Builder(URI endpoint)
/*     */     {
/*  64 */       super(endpoint.getHost(), endpoint.getPort());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  69 */   private static final Joiner COMMA_JOINER = Joiner.on(",");
/*  70 */   private final CreatorRegisteredEventTypes registeredEventTypes = new CreatorRegisteredEventTypes();
/*     */   
/*     */   protected DefaultAsyncEventServiceClient(ExecutorService es, CloseableHttpAsyncClient client, ObjectMapper mapper, URI baseUri, List<EventTypeCreator> creators)
/*     */   {
/*  74 */     super(es, client, mapper, baseUri);
/*  75 */     this.registeredEventTypes.registerEventTypeCreator(creators);
/*     */   }
/*     */   
/*     */   public static Builder buildFrom(URI endpoint) {
/*  79 */     return new Builder(endpoint);
/*     */   }
/*     */   
/*     */   public Observable<Void> registerEventType(String accountName, String accessKey, String eventType, String body)
/*     */   {
/*  84 */     Throwable[] err = new Throwable[1];
/*  85 */     return createRegisterEventType(accountName, accessKey, eventType, body, err, Void.class);
/*     */   }
/*     */   
/*     */   public Observable<Void> updateEventType(String accountName, String accessKey, String eventType, String body)
/*     */   {
/*  90 */     Throwable[] err = new Throwable[1];
/*  91 */     return createUpdateEventType(accountName, accessKey, eventType, body, err, Void.class);
/*     */   }
/*     */   
/*     */   public Observable<String> getEventType(final String accountName, final String accessKey, final String eventType)
/*     */   {
/*  96 */     Throwable[] err = new Throwable[1];
/*  97 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 101 */         return ((HttpRequestBuilder)((HttpRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().get().appendPath(eventType, new String[0])).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).expecting(200); } }, err, String.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<String> getEventTypeUsageNumDocuments(final String accountName, final String accessKey, final String eventType, final String startDateTime, final String endDateTime)
/*     */   {
/* 111 */     Throwable[] err = new Throwable[1];
/* 112 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 116 */         return ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().get().appendPath(eventType, new String[] { "usage", "documents" })).addQueryParam("startDateTime", startDateTime)).addQueryParam("endDateTime", endDateTime)).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).expecting(200); } }, err, String.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<String> getEventTypeUsageNumDocumentFragments(final String accountName, final String accessKey, final String eventType, final String date, final int hourOfDay)
/*     */   {
/* 128 */     Throwable[] err = new Throwable[1];
/* 129 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 133 */         return ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().get().appendPath(eventType, new String[] { "usage", "documentFragments" })).addQueryParam("date", date)).addQueryParam("hourOfDay", Integer.toString(hourOfDay))).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).expecting(200); } }, err, String.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<String> getEventTypeUsageNumBytes(final String accountName, final String accessKey, final String eventType, final String date, final int hourOfDay)
/*     */   {
/* 145 */     Throwable[] err = new Throwable[1];
/* 146 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 150 */         return ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().get().appendPath(eventType, new String[] { "usage", "bytes" })).addQueryParam("date", date)).addQueryParam("hourOfDay", Integer.toString(hourOfDay))).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).expecting(200); } }, err, String.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<Void> deleteEventType(final String accountName, final String accessKey, final String eventType)
/*     */   {
/* 161 */     Throwable[] err = new Throwable[1];
/* 162 */     createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/* 165 */       public GenericHttpRequestBuilder create() { return ((HttpRequestBuilder)((HttpRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().delete().appendPath(eventType, new String[0])).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).expecting(204); } }, err, Void.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<Void> publishEvents(final String accountName, final String accessKey, final String eventType, final String body)
/*     */   {
/* 175 */     Throwable[] err = new Throwable[1];
/* 176 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 180 */         return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().post().appendPath(eventType, new String[] { "event" })).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(body, true).expecting(202); } }, err, Void.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<Void> upsertEvents(final String accountName, final String accessKey, final String eventType, final String body, final String idPath, final List<String> mergeFields)
/*     */   {
/* 191 */     Throwable[] err = new Throwable[1];
/* 192 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 196 */         return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().patch().appendPath(eventType, new String[] { "event" })).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(body, true).addQueryParam("_idPath", idPath)).addQueryParam("_mergeFields", mergeFields == null ? null : DefaultAsyncEventServiceClient.COMMA_JOINER.join(mergeFields))).expecting(202); } }, err, Void.class));
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
/*     */   public Observable<String> searchEvents(final String accountName, final String accessKey, final String eventType, final String searchRequest)
/*     */   {
/* 211 */     Throwable[] err = new Throwable[1];
/* 212 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 216 */         return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().post().appendPath(eventType, new String[] { "search" })).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(searchRequest, true).expecting(200); } }, err, String.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<String> multiSearchEvents(final String accountName, final String accessKey, final String multiSearchRequest)
/*     */   {
/* 227 */     Throwable[] err = new Throwable[1];
/* 228 */     createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/* 231 */       public GenericHttpRequestBuilder create() { return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().post().appendPath("msearch", new String[0])).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(multiSearchRequest, true).expecting(200); } }, err, String.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<String> queryEvents(final String accountName, final String accessKey, final String queryString, final String startTime, final String endTime, final String limitResults, final boolean returnEsJson)
/*     */   {
/* 242 */     Throwable[] err = new Throwable[1];
/* 243 */     createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/* 246 */       public GenericHttpRequestBuilder create() { return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().post().appendPath("query", new String[0])).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(queryString, true).addQueryParam("start", startTime)).addQueryParam("end", endTime)).addQueryParam("limit", limitResults)).addQueryParam("returnEsJson", Boolean.toString(returnEsJson))).expecting(200); } }, err, String.class);
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
/*     */   public Observable<String> searchErrors(final String accountName, final String accessKey, final String eventType, final String searchRequest)
/*     */   {
/* 261 */     Throwable[] err = new Throwable[1];
/* 262 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 266 */         return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().post().appendPath(eventType, new String[] { "errors" })).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(searchRequest, true).expecting(200); } }, err, String.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<Void> ping(final String accountName, final String accessKey)
/*     */   {
/* 276 */     Throwable[] err = new Throwable[1];
/* 277 */     createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/* 280 */       public GenericHttpRequestBuilder create() { return ((HttpRequestBuilder)((HttpRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().get().appendPath("ping", new String[0])).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).expecting(200); } }, err, Void.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Observable<String> relevantFields(final String accountName, final String accessKey, final String eventType, final String significantTermsQuery)
/*     */   {
/* 290 */     Throwable[] err = new Throwable[1];
/* 291 */     verifyEventType(accountName, accessKey, eventType, err, createObservable(new LazyHttpRequestBuilder()
/*     */     {
/*     */ 
/*     */       public GenericHttpRequestBuilder create() {
/* 295 */         return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)DefaultAsyncEventServiceClient.this.getRequestFactory().post().appendPath(eventType, new String[] { "relevantFields" })).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(significantTermsQuery, true).expecting(200); } }, err, String.class));
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
/*     */   private <T> Observable<T> createRegisterEventType(String accountName, String accessKey, String eventType, String body, Throwable[] err, Class<T> clz)
/*     */   {
/* 317 */     return createObservable(buildEventTypeRequest(getRequestFactory().post(), accountName, accessKey, eventType, body), err, clz);
/*     */   }
/*     */   
/*     */ 
/*     */   private <T> Observable<T> createUpdateEventType(String accountName, String accessKey, String eventType, String body, Throwable[] err, Class<T> clz)
/*     */   {
/* 323 */     return createObservable(buildEventTypeRequest(getRequestFactory().put(), accountName, accessKey, eventType, body), err, clz);
/*     */   }
/*     */   
/*     */ 
/*     */   private LazyHttpRequestBuilder buildEventTypeRequest(final HttpEntityEnclosingRequestBuilder builder, final String accountName, final String accessKey, final String eventType, final String body)
/*     */   {
/* 329 */     new LazyHttpRequestBuilder()
/*     */     {
/*     */       public GenericHttpRequestBuilder create() {
/* 332 */         return ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)builder.appendPath(eventType, new String[0])).addHeader("Authorization", DefaultAsyncEventServiceClient.this.buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(body != null ? body : DefaultAsyncEventServiceClient.this.getEventTypeBody(eventType), true).expecting(200);
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
/*     */   private <T> Observable<T> verifyEventType(final String accountName, final String accessKey, final String eventType, final Throwable[] err, Observable<T> observable)
/*     */   {
/* 353 */     if (this.registeredEventTypes.isEventTypeRegisteredOrNoCreator(accountName, eventType)) {
/* 354 */       return observable;
/*     */     }
/*     */     
/* 357 */     final ConnectableObservable<T> connector = observable.publish();
/*     */     
/* 359 */     final Observable<Void> validator = createRegisterEventType(accountName, accessKey, eventType, null, err, Void.class).doOnError(new Action1()
/*     */     {
/*     */ 
/*     */       public void call(Throwable throwable)
/*     */       {
/* 364 */         if (((throwable instanceof RestException)) && (((RestException)throwable).getStatusCode() == 409) && ("Conflict.EventType".equals(((RestException)throwable).getCode())))
/*     */         {
/*     */ 
/* 367 */           DefaultAsyncEventServiceClient.log.debug("Event type already registered, updating event type {} for account {}", eventType, accountName);
/*     */           
/* 369 */           err[0] = null;
/*     */           
/*     */ 
/* 372 */           DefaultAsyncEventServiceClient.this.createUpdateEventType(accountName, accessKey, eventType, DefaultAsyncEventServiceClient.this.registeredEventTypes.findEventTypeCreator(eventType).getEventTypeMapping(), err, Void.class).doOnCompleted(new Action0()
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             public void call() {
/* 379 */               DefaultAsyncEventServiceClient.this.registeredEventTypes.addRegisteredEventType(DefaultAsyncEventServiceClient.16.this.val$accountName, DefaultAsyncEventServiceClient.16.this.val$eventType); } }).doOnTerminate(new Action0()
/*     */           {
/*     */ 
/*     */ 
/*     */             public void call()
/*     */             {
/*     */ 
/* 386 */               DefaultAsyncEventServiceClient.16.this.val$connector.connect();
/*     */             }
/*     */           }).subscribe();
/*     */         } else {
/* 390 */           connector.connect(); } } }).doOnCompleted(new Action0()
/*     */     {
/*     */ 
/*     */ 
/*     */       public void call()
/*     */       {
/*     */ 
/* 397 */         DefaultAsyncEventServiceClient.this.registeredEventTypes.addRegisteredEventType(accountName, eventType);
/*     */         
/*     */ 
/* 400 */         connector.connect();
/*     */       }
/* 402 */     });
/* 403 */     Observable.merge(Observable.empty(), connector).doOnSubscribe(new Action0()
/*     */     {
/*     */       public void call()
/*     */       {
/* 407 */         validator.subscribe();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private String getEventTypeBody(String eventType) {
/* 413 */     EventTypeCreator creator = this.registeredEventTypes.findEventTypeCreator(eventType);
/* 414 */     if (creator != null) {
/* 415 */       return creator.getEventTypeMapping();
/*     */     }
/* 417 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/DefaultAsyncEventServiceClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */