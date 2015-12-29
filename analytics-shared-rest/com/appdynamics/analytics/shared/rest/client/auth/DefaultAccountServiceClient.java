/*     */ package com.appdynamics.analytics.shared.rest.client.auth;
/*     */ 
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
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.io.BaseEncoding;
/*     */ import java.io.IOException;
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
/*     */ public class DefaultAccountServiceClient
/*     */   extends AbstractAnalyticsClient
/*     */   implements AccountServiceClient
/*     */ {
/*     */   final ObjectMapper mapper;
/*     */   
/*     */   private DefaultAccountServiceClient(ObjectMapper mapper, CloseableHttpClient client, URI baseUri)
/*     */   {
/*  34 */     super(mapper, client, baseUri);
/*  35 */     this.mapper = mapper;
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
/*     */   public static AbstractAnalyticsClient.Builder<DefaultAccountServiceClient> builder(String hostName, int port)
/*     */   {
/*  58 */     new AbstractAnalyticsClient.Builder(hostName, port)
/*     */     {
/*     */       protected DefaultAccountServiceClient buildInternal() {
/*  61 */         URI baseUri = buildBaseUri("/v1/account");
/*     */         
/*  63 */         return new DefaultAccountServiceClient(this.mapper, this.client, baseUri, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AbstractAnalyticsClient.Builder<DefaultAccountServiceClient> builder(String scheme, String hostName, int port)
/*     */   {
/*  89 */     new AbstractAnalyticsClient.Builder(scheme, hostName, port)
/*     */     {
/*     */       protected DefaultAccountServiceClient buildInternal() {
/*  92 */         URI baseUri = buildBaseUri("/v1/account");
/*     */         
/*  94 */         return new DefaultAccountServiceClient(this.mapper, this.client, baseUri, null);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public void addOrUpdateAccounts(String symmetricKey, List<AccountServiceClient.Account> accounts) throws RestException
/*     */   {
/* 101 */     ArrayNode body = this.mapper.createArrayNode();
/* 102 */     for (AccountServiceClient.Account account : accounts) {
/* 103 */       body.add((JsonNode)this.mapper.convertValue(account, JsonNode.class));
/*     */     }
/*     */     
/* 106 */     ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().addHeader("Authorization", buildAuthHeader(symmetricKey))).setRequestEntity(body).expecting(204)).executeAndReturnRawResponseString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAccountConfiguration(String symmetricKey, String accountName)
/*     */     throws RestException
/*     */   {
/* 115 */     return ((HttpRequestBuilder)((HttpRequestBuilder)((HttpRequestBuilder)getRequestFactory().get().appendPath(accountName, new String[0])).addHeader("Authorization", buildAuthHeader(symmetricKey))).expecting(200)).executeAndReturnRawResponseString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<AccountServiceClient.Account> getAccountConfigurations(String symmetricKey, List<String> accountNames)
/*     */     throws IOException, RestException
/*     */   {
/* 124 */     ArrayNode payload = (ArrayNode)this.mapper.convertValue(accountNames, ArrayNode.class);
/* 125 */     String response = ((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)getRequestFactory().post().appendPath("search", new String[0])).addHeader("Authorization", buildAuthHeader(symmetricKey))).setRequestEntity(payload).expecting(200)).executeAndReturnRawResponseString();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 130 */     return (List)this.mapper.readValue(response, this.mapper.getTypeFactory().constructCollectionType(List.class, AccountServiceClient.Account.class));
/*     */   }
/*     */   
/*     */   private String buildAuthHeader(String symmetricKey) {
/* 134 */     return "Basic " + BaseEncoding.base64().encode(symmetricKey.getBytes(Charsets.UTF_8));
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/auth/DefaultAccountServiceClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */