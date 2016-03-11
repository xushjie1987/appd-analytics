/*     */ package com.appdynamics.analytics.shared.rest.client.utils;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.UsernamePasswordCredentials;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.impl.client.BasicCredentialsProvider;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
/*     */ import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
/*     */ import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
/*     */ import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
/*     */ import org.apache.http.impl.nio.reactor.IOReactorConfig;
/*     */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*     */ import org.apache.http.nio.reactor.IOReactorException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import rx.Observable;
/*     */ import rx.Observable.OnSubscribe;
/*     */ import rx.Scheduler;
/*     */ import rx.Subscriber;
/*     */ import rx.schedulers.Schedulers;
/*     */ 
/*     */ public class AbstractAsyncAnalyticsClient
/*     */   extends AbstractAnalyticsClient
/*     */ {
/*  36 */   private static final Logger log = LoggerFactory.getLogger(AbstractAsyncAnalyticsClient.class);
/*     */   private final ExecutorService threadPool;
/*     */   private final CloseableHttpAsyncClient httpClient;
/*     */   
/*     */   public static abstract class Builder<T>
/*     */     extends AbstractAnalyticsClient.Builder<T>
/*     */   {
/*  43 */     private int poolSize = 4;
/*  44 */     private CloseableHttpAsyncClient asyncClient = null;
/*     */     
/*     */     public Builder<T> setWorkerPoolSize(int size) {
/*  47 */       this.poolSize = size;
/*  48 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> client(CloseableHttpClient client) {
/*  52 */       throw new UnsupportedOperationException("Supports CloseableHttpAsyncClient only");
/*     */     }
/*     */     
/*     */     public Builder<T> client(CloseableHttpAsyncClient asyncClient) {
/*  56 */       this.asyncClient = asyncClient;
/*  57 */       return this;
/*     */     }
/*     */     
/*     */     public T build() {
/*  61 */       if (this.mapper == null) {
/*  62 */         this.mapper = new ObjectMapper();
/*     */       }
/*     */       
/*  65 */       return (T)buildInternal();
/*     */     }
/*     */     
/*     */     protected ExecutorService createExectorService() {
/*  69 */       return Executors.newFixedThreadPool(this.poolSize);
/*     */     }
/*     */     
/*     */     protected CloseableHttpAsyncClient getOrCreateAsyncClient() {
/*  73 */       if (this.asyncClient != null) {
/*  74 */         return this.asyncClient;
/*     */       }
/*  76 */       ConnectingIOReactor ioReactor = null;
/*     */       try {
/*  78 */         ioReactor = new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT);
/*     */       } catch (IOReactorException e) {
/*  80 */         throw new IllegalStateException(e);
/*     */       }
/*     */       
/*  83 */       PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
/*  84 */       connectionManager.setMaxTotal(this.maxTotalConnections);
/*  85 */       connectionManager.setDefaultMaxPerRoute(this.maxConnectionsPerRoute);
/*     */       
/*  87 */       HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();
/*  88 */       addProxyConfig(builder);
/*  89 */       return builder.setSSLContext(this.sslContext).setConnectionManager(connectionManager).setDefaultRequestConfig(getRequestConfig()).build();
/*     */     }
/*     */     
/*     */ 
/*     */     private void addProxyConfig(HttpAsyncClientBuilder builder)
/*     */     {
/*  95 */       if ((!Strings.isNullOrEmpty(this.httpProxyHost)) && (this.httpProxyPort != null)) {
/*  96 */         HttpHost proxy = new HttpHost(this.httpProxyHost, this.httpProxyPort.intValue());
/*  97 */         builder.setProxy(proxy);
/*  98 */         if ((!Strings.isNullOrEmpty(this.httpProxyUsername)) && (!Strings.isNullOrEmpty(this.httpProxyPassword))) {
/*  99 */           CredentialsProvider credsProvider = new BasicCredentialsProvider();
/* 100 */           Credentials credentials = new UsernamePasswordCredentials(this.httpProxyUsername, this.httpProxyPassword);
/* 101 */           credsProvider.setCredentials(new AuthScope(this.httpProxyHost, this.httpProxyPort.intValue()), credentials);
/* 102 */           builder.setDefaultCredentialsProvider(credsProvider);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected Builder(String scheme, String hostName, int port) {
/* 108 */       super(hostName, port);
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 114 */     this.httpClient.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected AbstractAsyncAnalyticsClient(ExecutorService es, CloseableHttpAsyncClient asyncClient, ObjectMapper mapper, URI baseUri)
/*     */   {
/* 121 */     super(mapper, null, baseUri);
/* 122 */     this.threadPool = es;
/* 123 */     this.httpClient = asyncClient;
/* 124 */     this.httpClient.start();
/*     */   }
/*     */   
/*     */   protected <T> Observable<T> createObservable(final LazyHttpRequestBuilder lazyBuilder, final Throwable[] err, final Class<T> clz)
/*     */   {
/* 129 */     Scheduler scheduler = Schedulers.from(this.threadPool);
/* 130 */     Observable.create(new Observable.OnSubscribe()
/*     */     {
/*     */       public void call(final Subscriber<? super T> observer) {
/* 133 */         if (observer.isUnsubscribed()) {
/* 134 */           return;
/*     */         }
/*     */         
/* 137 */         if (err[0] != null) {
/* 138 */           observer.onError(err[0]);
/* 139 */           return;
/*     */         }
/*     */         try
/*     */         {
/* 143 */           final GenericHttpRequestBuilder httpRequest = lazyBuilder.create();
/* 144 */           AbstractAsyncAnalyticsClient.this.httpClient.execute(httpRequest.getHttpRequest(), new FutureCallback()
/*     */           {
/*     */             public void completed(HttpResponse result) {
/* 147 */               if (observer.isUnsubscribed()) {
/* 148 */                 return;
/*     */               }
/*     */               try {
/* 151 */                 observer.onNext(RestClientUtils.resolve(result, httpRequest.getExpectedStatusCode(), AbstractAsyncAnalyticsClient.this.getMapper(), AbstractAsyncAnalyticsClient.1.this.val$clz));
/*     */                 
/* 153 */                 observer.onCompleted();
/*     */               } catch (Exception e) {
/* 155 */                 AbstractAsyncAnalyticsClient.1.this.val$err[0] = e;
/* 156 */                 observer.onError(e);
/*     */               }
/*     */             }
/*     */             
/*     */             public void failed(Exception ex)
/*     */             {
/* 162 */               AbstractAsyncAnalyticsClient.1.this.val$err[0] = ex;
/* 163 */               if (!observer.isUnsubscribed()) {
/* 164 */                 observer.onError(ex);
/*     */               }
/*     */             }
/*     */             
/*     */             public void cancelled()
/*     */             {
/* 170 */               if (!observer.isUnsubscribed()) {
/* 171 */                 observer.onCompleted();
/*     */               }
/*     */             }
/*     */           });
/*     */         } catch (Exception e) {
/* 176 */           observer.onError(e); } } }).observeOn(scheduler).subscribeOn(scheduler);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/AbstractAsyncAnalyticsClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */