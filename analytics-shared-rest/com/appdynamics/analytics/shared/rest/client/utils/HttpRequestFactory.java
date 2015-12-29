/*     */ package com.appdynamics.analytics.shared.rest.client.utils;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.ClientException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.net.SocketException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.http.NoHttpResponseException;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.config.RequestConfig.Builder;
/*     */ import org.apache.http.client.methods.HttpDelete;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpPatch;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*     */ 
/*     */ 
/*     */ public class HttpRequestFactory
/*     */ {
/*     */   @ConstructorProperties({"mapper", "client", "baseUri"})
/*     */   public HttpRequestFactory(ObjectMapper mapper, CloseableHttpClient client, URI baseUri)
/*     */   {
/*  31 */     this.mapper = mapper;this.client = client;this.baseUri = baseUri;
/*     */   }
/*     */   
/*  34 */   private static final ObjectMapper MAPPER = new ObjectMapper();
/*     */   
/*     */ 
/*     */   private final ObjectMapper mapper;
/*     */   
/*     */ 
/*     */   private final CloseableHttpClient client;
/*     */   
/*     */   private final URI baseUri;
/*     */   
/*     */ 
/*     */   public static <T> T retryWithRequestFactory(HttpRequestExecutor<T> executor, CloseableHttpClient httpClient, String url, int maxRetries)
/*     */   {
/*  47 */     int attempt = 1;
/*     */     
/*  49 */     Exception clientException = null;
/*  50 */     for (; attempt <= maxRetries; attempt++) {
/*  51 */       HttpRequestFactory requestFactory = buildRequestFactory(url, httpClient);
/*     */       try {
/*  53 */         return (T)executor.execute(requestFactory);
/*     */       } catch (ClientException e) {
/*  55 */         clientException = e;
/*  56 */         if (((e.getCause() instanceof SocketException)) || (!(e.getCause() instanceof NoHttpResponseException)))
/*     */         {
/*     */ 
/*  59 */           throw Throwables.propagate(e); }
/*     */       }
/*     */     }
/*  62 */     String msg = "Failed after [" + maxRetries + "] retries" + (clientException != null ? " with exception " + clientException.getMessage() : "");
/*     */     
/*  64 */     if (clientException != null) {
/*  65 */       throw new IllegalStateException(msg, clientException);
/*     */     }
/*  67 */     throw new IllegalStateException(msg);
/*     */   }
/*     */   
/*     */   public static HttpRequestFactory buildRequestFactory(String uriString, CloseableHttpClient httpClient)
/*     */   {
/*     */     URI uri;
/*     */     try {
/*  74 */       uri = new URI(uriString);
/*     */     } catch (URISyntaxException e) {
/*  76 */       throw Throwables.propagate(e);
/*     */     }
/*  78 */     if (((!uri.getScheme().equalsIgnoreCase("http")) && (!uri.getScheme().equalsIgnoreCase("https"))) || (Strings.isNullOrEmpty(uri.getHost())))
/*     */     {
/*  80 */       throw new IllegalArgumentException("Invalid uri: " + uri);
/*     */     }
/*  82 */     return new HttpRequestFactory(MAPPER, httpClient, uri);
/*     */   }
/*     */   
/*     */   public static CloseableHttpClient buildHttpClient(long timeout, TimeUnit timeoutUnit) {
/*  86 */     RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout((int)TimeUnit.MILLISECONDS.convert(timeout, timeoutUnit)).build();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  91 */     PoolingHttpClientConnectionManager cManager = new PoolingHttpClientConnectionManager();
/*  92 */     cManager.setDefaultMaxPerRoute(20);
/*  93 */     cManager.setMaxTotal(200);
/*     */     
/*  95 */     return HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(cManager).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpEntityEnclosingRequestBuilder post()
/*     */   {
/* 106 */     return (HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)new HttpEntityEnclosingRequestBuilder(new HttpPost()).setUri(this.baseUri)).using(this.mapper)).using(this.client);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpEntityEnclosingRequestBuilder patch()
/*     */   {
/* 117 */     return (HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)new HttpEntityEnclosingRequestBuilder(new HttpPatch()).setUri(this.baseUri)).using(this.mapper)).using(this.client);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpEntityEnclosingRequestBuilder put()
/*     */   {
/* 128 */     return (HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)new HttpEntityEnclosingRequestBuilder(new HttpPut()).setUri(this.baseUri)).using(this.mapper)).using(this.client);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequestBuilder get()
/*     */   {
/* 139 */     return (HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)new HttpRequestBuilder(new HttpGet()).setUri(this.baseUri)).using(this.mapper)).using(this.client);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpRequestBuilder delete()
/*     */   {
/* 150 */     return (HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)new HttpRequestBuilder(new HttpDelete()).setUri(this.baseUri)).using(this.mapper)).using(this.client);
/*     */   }
/*     */   
/*     */   public static abstract interface HttpRequestExecutor<T>
/*     */   {
/*     */     public abstract T execute(HttpRequestFactory paramHttpRequestFactory);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/HttpRequestFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */