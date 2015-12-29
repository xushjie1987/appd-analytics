/*     */ package com.appdynamics.analytics.shared.rest.client.utils;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.ClientException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpRequestBase;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.utils.URIBuilder;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenericHttpRequestBuilder<T extends GenericHttpRequestBuilder<?>>
/*     */ {
/*     */   protected final HttpRequestBase request;
/*     */   protected URIBuilder uriBuilder;
/*     */   private ObjectMapper mapper;
/*     */   private int expectedStatusCode;
/*     */   private CloseableHttpClient client;
/*     */   
/*     */   protected HttpRequestBase getRequest()
/*     */   {
/*  33 */     return this.request;
/*     */   }
/*     */   
/*     */   protected ObjectMapper getMapper()
/*     */   {
/*  38 */     return this.mapper;
/*     */   }
/*     */   
/*  41 */   protected int getExpectedStatusCode() { return this.expectedStatusCode; }
/*     */   
/*     */   protected CloseableHttpClient getClient() {
/*  44 */     return this.client;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected GenericHttpRequestBuilder(HttpRequestBase request)
/*     */   {
/*  52 */     this.request = request;
/*  53 */     request.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
/*     */     try {
/*  55 */       this.uriBuilder = new URIBuilder("/");
/*     */     }
/*     */     catch (URISyntaxException e) {
/*  58 */       throw new RequestBuilderException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T setUri(String uri)
/*     */   {
/*     */     try
/*     */     {
/*  70 */       this.uriBuilder = new URIBuilder(uri);
/*     */     } catch (URISyntaxException e) {
/*  72 */       throw new RequestBuilderException(e);
/*     */     }
/*  74 */     return getThis();
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
/*     */   public T setUri(URI uri)
/*     */   {
/*  88 */     this.uriBuilder = new URIBuilder(uri);
/*  89 */     return getThis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T appendPath(String pathSegment, String... otherSegments)
/*     */   {
/*  99 */     String currentPath = this.uriBuilder.getPath();
/* 100 */     String completePath = RestClientUtils.buildPath(currentPath, pathSegment, otherSegments);
/* 101 */     if (completePath.charAt(0) != '/') {
/* 102 */       completePath = '/' + completePath;
/*     */     }
/* 104 */     this.uriBuilder.setPath(completePath);
/* 105 */     return getThis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T addQueryParam(String name, String value)
/*     */   {
/* 115 */     this.uriBuilder.addParameter(name, value);
/* 116 */     return getThis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T addHeader(String name, String value)
/*     */   {
/* 126 */     this.request.addHeader(name, value);
/* 127 */     return getThis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T using(ObjectMapper mapper)
/*     */   {
/* 136 */     this.mapper = mapper;
/* 137 */     return getThis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T using(CloseableHttpClient client)
/*     */   {
/* 146 */     this.client = client;
/* 147 */     return getThis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T expecting(int expectedStatusCode)
/*     */   {
/* 156 */     this.expectedStatusCode = expectedStatusCode;
/* 157 */     return getThis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <U> U execute(Class<U> responseEntityClass)
/*     */   {
/* 169 */     checkNotNull(responseEntityClass, "Response entity class");
/* 170 */     CloseableHttpResponse response = getResponse();
/*     */     
/* 172 */     return (U)RestClientUtils.resolve(response, this.expectedStatusCode, this.mapper, responseEntityClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpUriRequest getHttpRequest()
/*     */     throws URISyntaxException
/*     */   {
/* 181 */     this.request.setURI(this.uriBuilder.build());
/* 182 */     return this.request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void executeExpectingNoContent()
/*     */   {
/* 192 */     CloseableHttpResponse response = getResponse();
/* 193 */     RestClientUtils.resolve(response, this.expectedStatusCode, this.mapper, null);
/*     */   }
/*     */   
/*     */   private CloseableHttpResponse getResponse()
/*     */   {
/* 198 */     checkNotNull(this.client, "Client");
/* 199 */     checkNotNull(this.mapper, "ObjectMapper");
/*     */     URI uri;
/*     */     try
/*     */     {
/* 203 */       uri = this.uriBuilder.build();
/*     */     } catch (URISyntaxException e1) {
/* 205 */       throw new RequestBuilderException("The URI for the request is incorrect. Fix before reattempting.", e1);
/*     */     }
/* 207 */     getRequest().setURI(uri);
/*     */     CloseableHttpResponse response;
/*     */     try
/*     */     {
/* 211 */       response = this.client.execute(getRequest());
/*     */     } catch (IOException e) {
/* 213 */       throw new ClientException("Could not execute request to " + uri, e);
/*     */     }
/* 215 */     return response;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String executeAndReturnRawResponseString()
/*     */   {
/* 227 */     CloseableHttpResponse response = getResponse();
/* 228 */     return (String)RestClientUtils.resolve(response, this.expectedStatusCode, this.mapper, String.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void checkNotNull(Object param, String name)
/*     */   {
/* 237 */     if (param == null) {
/* 238 */       throw new RequestBuilderException(name + " has not been set");
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract T getThis();
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/GenericHttpRequestBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */