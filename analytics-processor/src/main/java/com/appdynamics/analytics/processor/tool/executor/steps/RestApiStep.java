/*     */ package com.appdynamics.analytics.processor.tool.executor.steps;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionContext;
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionStep;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.ExecutionResponse;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.FailedResponse;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.SuccessfulResponse;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.GenericHttpRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpEntityEnclosingRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpRequestFactory;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.BaseEncoding;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.validation.constraints.NotNull;
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
/*     */ public class RestApiStep
/*     */   extends AbstractExecutionStep
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(RestApiStep.class);
/*     */   public static class Properties { @NotNull
/*  45 */     String method; public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Properties)) return false; Properties other = (Properties)o; if (!other.canEqual(this)) return false; Object this$method = getMethod();Object other$method = other.getMethod(); if (this$method == null ? other$method != null : !this$method.equals(other$method)) return false; Object this$scheme = getScheme();Object other$scheme = other.getScheme(); if (this$scheme == null ? other$scheme != null : !this$scheme.equals(other$scheme)) return false; Object this$host = getHost();Object other$host = other.getHost(); if (this$host == null ? other$host != null : !this$host.equals(other$host)) return false; Object this$path = getPath();Object other$path = other.getPath(); if (this$path == null ? other$path != null : !this$path.equals(other$path)) return false; Object this$credentials = getCredentials();Object other$credentials = other.getCredentials(); if (this$credentials == null ? other$credentials != null : !this$credentials.equals(other$credentials)) return false; Object this$queryParams = getQueryParams();Object other$queryParams = other.getQueryParams(); if (this$queryParams == null ? other$queryParams != null : !this$queryParams.equals(other$queryParams)) return false; Object this$body = getBody();Object other$body = other.getBody();return this$body == null ? other$body == null : this$body.equals(other$body); } public boolean canEqual(Object other) { return other instanceof Properties; } public int hashCode() { int PRIME = 31;int result = 1;Object $method = getMethod();result = result * 31 + ($method == null ? 0 : $method.hashCode());Object $scheme = getScheme();result = result * 31 + ($scheme == null ? 0 : $scheme.hashCode());Object $host = getHost();result = result * 31 + ($host == null ? 0 : $host.hashCode());Object $path = getPath();result = result * 31 + ($path == null ? 0 : $path.hashCode());Object $credentials = getCredentials();result = result * 31 + ($credentials == null ? 0 : $credentials.hashCode());Object $queryParams = getQueryParams();result = result * 31 + ($queryParams == null ? 0 : $queryParams.hashCode());Object $body = getBody();result = result * 31 + ($body == null ? 0 : $body.hashCode());return result; } public String toString() { return "RestApiStep.Properties(method=" + getMethod() + ", scheme=" + getScheme() + ", host=" + getHost() + ", path=" + getPath() + ", credentials=" + getCredentials() + ", queryParams=" + getQueryParams() + ", body=" + getBody() + ")"; } @ConstructorProperties({"method", "scheme", "host", "path", "credentials", "queryParams", "body"})
/*  46 */     public Properties(String method, String scheme, String host, String path, String credentials, Map<String, String> queryParams, JsonNode body) { this.method = method;this.scheme = scheme;this.host = host;this.path = path;this.credentials = credentials;this.queryParams = queryParams;this.body = body; } String scheme = "http";
/*     */     @NotNull
/*     */     String host;
/*     */     
/*  50 */     public String getMethod() { return this.method; } public void setMethod(String method) { this.method = method; }
/*  51 */     public String getScheme() { return this.scheme; } public void setScheme(String scheme) { this.scheme = scheme; }
/*     */     
/*  53 */     public String getHost() { return this.host; } public void setHost(String host) { this.host = host; }
/*  54 */     public String getPath() { return this.path; } public void setPath(String path) { this.path = path; }
/*  55 */     public String getCredentials() { return this.credentials; } public void setCredentials(String credentials) { this.credentials = credentials; }
/*  56 */     public Map<String, String> getQueryParams() { return this.queryParams; } public void setQueryParams(Map<String, String> queryParams) { this.queryParams = queryParams; }
/*  57 */     public JsonNode getBody() { return this.body; } public void setBody(JsonNode body) { this.body = body; }
/*     */     
/*     */     String path;
/*  60 */     public Properties copy() { return new Properties(this.method, this.scheme, this.host, this.path, this.credentials, Maps.newHashMap(this.queryParams), this.body); }
/*     */     
/*     */     String credentials;
/*     */     Map<String, String> queryParams;
/*     */     JsonNode body;
/*     */     public Properties() {}
/*     */   }
/*     */   
/*  68 */   public RestApiStep(Properties properties) { Preconditions.checkArgument((properties != null) && (!Strings.isNullOrEmpty(properties.host)) && (!Strings.isNullOrEmpty(properties.method)));
/*     */     
/*  70 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public ExecutionStep copy()
/*     */   {
/*  75 */     RestApiStep step = new RestApiStep(this.properties.copy());
/*  76 */     step.setName(getName());
/*  77 */     return step;
/*     */   }
/*     */   
/*     */   public ExecutionResponse executeStep(ExecutionContext executionContext)
/*     */   {
/*  82 */     Preconditions.checkArgument(executionContext.getHttpClient() != null, "Expected execution context to have configured HttpClient");
/*     */     
/*  84 */     HttpRequestFactory requestFactory = buildRequestFactory(executionContext.getHttpClient());
/*     */     
/*  86 */     switch (this.properties.method.toLowerCase()) {
/*     */     case "post": 
/*  88 */       return executeRequest(requestFactory.post());
/*     */     case "put": 
/*  90 */       return executeRequest(requestFactory.put());
/*     */     case "get": 
/*  92 */       return executeRequest(requestFactory.get());
/*     */     case "delete": 
/*  94 */       return executeRequest(requestFactory.delete());
/*     */     case "patch": 
/*  96 */       return executeRequest(requestFactory.patch());
/*     */     }
/*  98 */     throw new IllegalArgumentException("Method [" + this.properties.method + "] not allowed");
/*     */   }
/*     */   
/*     */   private final Properties properties;
/*     */   private ExecutionResponse executeRequest(GenericHttpRequestBuilder<? extends GenericHttpRequestBuilder> requestBuilder)
/*     */   {
/* 104 */     addEntityParts(requestBuilder);
/*     */     JsonNode jsonResponse;
/*     */     try {
/* 107 */       jsonResponse = executeRequest(requestBuilder, this.properties.credentials);
/*     */     } catch (RestException e) {
/* 109 */       return new FailedResponse(e.getStatusCode(), e.getCode() + "[" + e.getMessage() + "]:\n" + e.getDeveloperMessage());
/*     */     }
/*     */     catch (Exception e) {
/* 112 */       return new FailedResponse(1, e.getMessage() + ":\n" + Throwables.getStackTraceAsString(e));
/*     */     }
/*     */     
/* 115 */     return new SuccessfulResponse("REST call successful:\n" + (jsonResponse == null ? "" : jsonResponse.toString()));
/*     */   }
/*     */   
/*     */   private void addEntityParts(GenericHttpRequestBuilder<? extends GenericHttpRequestBuilder> requestBuilder)
/*     */   {
/* 120 */     if (requestBuilder.getClass().isAssignableFrom(HttpEntityEnclosingRequestBuilder.class)) {
/* 121 */       HttpEntityEnclosingRequestBuilder entityEnclosingRequestBuilder = (HttpEntityEnclosingRequestBuilder)requestBuilder;
/*     */       
/* 123 */       if (this.properties.queryParams != null) {
/* 124 */         for (Map.Entry<String, String> entry : this.properties.queryParams.entrySet()) {
/* 125 */           entityEnclosingRequestBuilder.addQueryParam((String)entry.getKey(), (String)entry.getValue());
/* 126 */           log.info("With request parameter: {}={}", entry.getKey(), entry.getValue());
/*     */         }
/*     */       }
/* 129 */       if (this.properties.body != null) {
/* 130 */         String body = this.properties.body.toString();
/* 131 */         entityEnclosingRequestBuilder.setRequestEntity(body, true);
/* 132 */         log.info("With request body: {}", body);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   HttpRequestFactory buildRequestFactory(CloseableHttpClient httpClient)
/*     */   {
/*     */     URI uri;
/*     */     try {
/* 141 */       uri = new URI(this.properties.scheme, this.properties.host, this.properties.path, null, null);
/*     */     } catch (URISyntaxException e) {
/* 143 */       throw Throwables.propagate(e);
/*     */     }
/*     */     
/* 146 */     log.info("Executing a call to REST endpoint: {}", uri);
/* 147 */     return new HttpRequestFactory(Reader.DEFAULT_JSON_MAPPER, httpClient, uri);
/*     */   }
/*     */   
/*     */   JsonNode executeRequest(GenericHttpRequestBuilder<? extends GenericHttpRequestBuilder> requestBuilder, String key)
/*     */   {
/* 152 */     if (!Strings.isNullOrEmpty(this.properties.credentials)) {
/* 153 */       requestBuilder.addHeader("Authorization", "Basic " + BaseEncoding.base64().encode(key.getBytes(Charsets.UTF_8)));
/*     */     }
/*     */     
/* 156 */     return (JsonNode)requestBuilder.execute(JsonNode.class);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/steps/RestApiStep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */