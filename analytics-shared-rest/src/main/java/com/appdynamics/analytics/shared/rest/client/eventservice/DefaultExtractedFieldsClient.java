/*     */ package com.appdynamics.analytics.shared.rest.client.eventservice;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.AbstractAnalyticsClient;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.AbstractAnalyticsClient.Builder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpEntityEnclosingRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpRequestBuilder;
/*     */ import com.appdynamics.analytics.shared.rest.client.utils.HttpRequestFactory;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.BadRequestRestException;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.ConflictRestException;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.NotFoundRestException;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.net.URI;
/*     */ import java.util.List;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultExtractedFieldsClient
/*     */   extends AbstractAnalyticsClient
/*     */   implements ExtractedFieldsClient
/*     */ {
/*     */   protected DefaultExtractedFieldsClient(ObjectMapper mapper, CloseableHttpClient client, URI baseUri)
/*     */   {
/*  31 */     super(mapper, client, baseUri);
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
/*     */   public static AbstractAnalyticsClient.Builder<DefaultExtractedFieldsClient> builder(String hostName, int port)
/*     */     throws RestException
/*     */   {
/*  49 */     new AbstractAnalyticsClient.Builder(hostName, port)
/*     */     {
/*     */       protected DefaultExtractedFieldsClient buildInternal() {
/*  52 */         URI baseUri = buildBaseUri("/v1/events");
/*     */         
/*  54 */         return new DefaultExtractedFieldsClient(this.mapper, this.client, baseUri);
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
/*     */   public static AbstractAnalyticsClient.Builder<DefaultExtractedFieldsClient> builder(String scheme, String hostName, int port)
/*     */     throws RestException
/*     */   {
/*  76 */     new AbstractAnalyticsClient.Builder(scheme, hostName, port)
/*     */     {
/*     */       protected DefaultExtractedFieldsClient buildInternal() {
/*  79 */         URI baseUri = buildBaseUri("/v1/events");
/*     */         
/*  81 */         return new DefaultExtractedFieldsClient(this.mapper, this.client, baseUri);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   public List<ExtractedFieldsClient.ExtractedFieldDefinition> getExtractedFields(String accountName, String accessKey, String eventType)
/*     */   {
/*  89 */     String response = (String)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(eventType, new String[] { "extracted-fields" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).execute(String.class);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  94 */     return jsonToListExtractedFields(response);
/*     */   }
/*     */   
/*     */   public ExtractedFieldsClient.ExtractedFieldDefinition getExtractedField(String accountName, String accessKey, String eventType, String fieldName)
/*     */   {
/*     */     try
/*     */     {
/* 101 */       String response = (String)((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(eventType, new String[] { "extracted-fields", fieldName })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(200)).execute(String.class);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 106 */       return (ExtractedFieldsClient.ExtractedFieldDefinition)getMapper().readValue(response, ExtractedFieldsClient.ExtractedFieldDefinition.class);
/*     */     } catch (Exception e) {
/* 108 */       if (((e instanceof RestException)) && (((RestException)e).getStatusCode() == 204)) {
/* 109 */         return null;
/*     */       }
/* 111 */       throw Throwables.propagate(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<ExtractedFieldsClient.ExtractedFieldDefinition> getExtractedFields(String accountName, String accessKey, String eventType, List<String> sourceTypes)
/*     */   {
/* 119 */     HttpRequestBuilder builder = (HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(eventType, new String[] { "extracted-fields" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey));
/*     */     
/*     */ 
/*     */ 
/* 123 */     for (String sourceType : sourceTypes) {
/* 124 */       builder.addQueryParam("source-types", sourceType);
/*     */     }
/*     */     
/* 127 */     String response = (String)((HttpRequestBuilder)builder.expecting(200)).execute(String.class);
/* 128 */     return jsonToListExtractedFields(response);
/*     */   }
/*     */   
/*     */   public void validateExtractedField(String accountName, String accessKey, String eventType, ExtractedFieldsClient.ExtractedFieldDefinition extractedField)
/*     */     throws BadRequestRestException
/*     */   {
/* 134 */     ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath(eventType, new String[] { "extracted-fields", extractedField.getName(), "validate" })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(extractedField).expecting(204)).executeExpectingNoContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createExtractedField(String accountName, String accessKey, String eventType, ExtractedFieldsClient.ExtractedFieldDefinition extractedField)
/*     */     throws ConflictRestException
/*     */   {
/* 146 */     ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath(eventType, new String[] { "extracted-fields", extractedField.getName() })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(extractedField).expecting(204)).executeExpectingNoContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateExtractedField(String accountName, String accessKey, String eventType, ExtractedFieldsClient.ExtractedFieldDefinition extractedField)
/*     */     throws NotFoundRestException
/*     */   {
/* 157 */     ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().put().appendPath(eventType, new String[] { "extracted-fields", extractedField.getName() })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).setRequestEntity(extractedField).expecting(204)).executeExpectingNoContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deleteExtractedField(String accountName, String accessKey, String eventType, String name)
/*     */     throws NotFoundRestException
/*     */   {
/* 168 */     ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().delete().appendPath(eventType, new String[] { "extracted-fields", name })).addHeader("Authorization", buildStandardAuthHeader(accountName, accessKey))).expecting(204)).executeExpectingNoContent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<ExtractedFieldsClient.ExtractedFieldDefinition> jsonToListExtractedFields(String json)
/*     */   {
/*     */     try
/*     */     {
/* 177 */       ObjectMapper mapper = getMapper();
/* 178 */       return (List)mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, ExtractedFieldsClient.ExtractedFieldDefinition.class));
/*     */     }
/*     */     catch (Exception e) {
/* 181 */       throw Throwables.propagate(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/DefaultExtractedFieldsClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */