/*    */ package com.appdynamics.analytics.shared.rest.client.utils;
/*    */ 
/*    */ import org.apache.http.client.methods.HttpRequestBase;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpRequestBuilder
/*    */   extends GenericHttpRequestBuilder<HttpRequestBuilder>
/*    */ {
/*    */   protected HttpRequestBuilder(HttpRequestBase request)
/*    */   {
/* 25 */     super(request);
/*    */   }
/*    */   
/*    */   protected HttpRequestBuilder getThis()
/*    */   {
/* 30 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/HttpRequestBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */