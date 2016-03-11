/*    */ package com.appdynamics.analytics.shared.rest.client.utils;
/*    */ 
/*    */ import com.appdynamics.analytics.shared.rest.exceptions.ClientException;
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.databind.ObjectWriter;
/*    */ import com.google.common.base.Charsets;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.client.entity.GzipCompressingEntity;
/*    */ import org.apache.http.client.methods.HttpRequestBase;
/*    */ import org.apache.http.entity.ByteArrayEntity;
/*    */ import org.apache.http.entity.ContentType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpEntityEnclosingRequestBuilder
/*    */   extends GenericHttpRequestBuilder<HttpEntityEnclosingRequestBuilder>
/*    */ {
/*    */   private final HttpEntityEnclosingRequest request;
/*    */   private static final int GZIP_THRESHOLD = 200;
/*    */   
/*    */   protected <T extends HttpRequestBase,  extends HttpEntityEnclosingRequest> HttpEntityEnclosingRequestBuilder(T request)
/*    */   {
/* 35 */     super(request);
/* 36 */     this.request = ((HttpEntityEnclosingRequest)request);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HttpEntityEnclosingRequestBuilder setRequestEntity(Object entity)
/*    */   {
/* 48 */     return setRequestEntity(entity, false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public HttpEntityEnclosingRequestBuilder setRequestEntity(Object entity, boolean gzipDisabled)
/*    */   {
/*    */     byte[] bytes;
/*    */     
/*    */ 
/*    */     byte[] bytes;
/*    */     
/*    */ 
/* 62 */     if (entity.getClass().isAssignableFrom(String.class)) {
/* 63 */       bytes = ((String)entity).getBytes(Charsets.UTF_8);
/*    */     } else {
/* 65 */       bytes = getBytesFromObject(entity);
/*    */     }
/*    */     
/* 68 */     HttpEntity httpEntity = new ByteArrayEntity(bytes, ContentType.APPLICATION_JSON);
/*    */     
/* 70 */     if ((!gzipDisabled) && (bytes.length >= 200)) {
/* 71 */       httpEntity = new GzipCompressingEntity(httpEntity);
/*    */     }
/*    */     
/* 74 */     this.request.setEntity(httpEntity);
/* 75 */     this.request.addHeader("Content-type", ContentType.APPLICATION_JSON.getMimeType());
/*    */     
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   private byte[] getBytesFromObject(Object entity)
/*    */   {
/* 82 */     checkNotNull(getMapper(), "ObjectMapper");
/*    */     byte[] bytes;
/*    */     try {
/* 85 */       bytes = getMapper().writer().writeValueAsBytes(entity);
/*    */     }
/*    */     catch (JsonProcessingException e1)
/*    */     {
/* 89 */       throw new ClientException("Could not serialize data of type " + entity.getClass(), e1);
/*    */     }
/* 91 */     return bytes;
/*    */   }
/*    */   
/*    */   protected HttpEntityEnclosingRequestBuilder getThis()
/*    */   {
/* 96 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/HttpEntityEnclosingRequestBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */