/*    */ package com.appdynamics.analytics.processor.tool.executor;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ import java.util.Map;
/*    */ import lombok.NonNull;
/*    */ import org.apache.http.impl.client.CloseableHttpClient;
/*    */ 
/*    */ 
/*    */ public class ExecutionContext
/*    */ {
/*    */   @NonNull
/*    */   private CloseableHttpClient httpClient;
/*    */   private Map<String, Object> properties;
/*    */   
/*    */   @ConstructorProperties({"httpClient", "properties"})
/*    */   public ExecutionContext(@NonNull CloseableHttpClient httpClient, Map<String, Object> properties)
/*    */   {
/* 18 */     if (httpClient == null) throw new NullPointerException("httpClient"); this.httpClient = httpClient;this.properties = properties; }
/*    */   public ExecutionContext() {} @ConstructorProperties({"httpClient"})
/* 20 */   public ExecutionContext(@NonNull CloseableHttpClient httpClient) { if (httpClient == null) throw new NullPointerException("httpClient"); this.httpClient = httpClient;
/*    */   }
/*    */   
/* 23 */   public void setHttpClient(@NonNull CloseableHttpClient httpClient) { if (httpClient == null) throw new NullPointerException("httpClient"); this.httpClient = httpClient; } @NonNull
/* 24 */   public CloseableHttpClient getHttpClient() { return this.httpClient; }
/*    */   
/*    */ 
/* 27 */   public void setProperties(Map<String, Object> properties) { this.properties = properties; }
/* 28 */   public Map<String, Object> getProperties() { return this.properties; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/ExecutionContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */