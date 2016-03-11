/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.exception.ElasticSearchExceptionUtils;
/*    */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*    */ import com.appdynamics.common.util.exception.Exceptions;
/*    */ import org.elasticsearch.ElasticsearchException;
/*    */ import org.elasticsearch.client.Client;
/*    */ import org.elasticsearch.common.base.Throwables;
/*    */ import org.elasticsearch.common.settings.Settings;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElasticSearchServiceHelper
/*    */ {
/* 24 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchServiceHelper.class);
/*    */   
/*    */   static final int INDEX_REFRESH_RETRY_THRESHOLD = 5;
/*    */   static final long INDEX_REFRESH_RETRY_PAUSE = 1000L;
/*    */   
/*    */   public static RuntimeException propagateAsException(Exception e, String format, Object... args)
/*    */   {
/* 31 */     if ((e instanceof ElasticsearchException)) {
/* 32 */       return ElasticSearchExceptionUtils.propagateAsEventException((ElasticsearchException)e, format, args);
/*    */     }
/* 34 */     return Exceptions.addSuppressedMsgAndPropagate(e, format, args);
/*    */   }
/*    */   
/*    */   public static RuntimeException propagateAsException(Exception e)
/*    */   {
/* 39 */     if ((e instanceof ElasticsearchException)) {
/* 40 */       return ElasticSearchExceptionUtils.propagateAsEventException((ElasticsearchException)e);
/*    */     }
/* 42 */     return Throwables.propagate(e);
/*    */   }
/*    */   
/*    */   public static boolean refreshAndWaitForIndexReadiness(Client client, String... indexNames)
/*    */   {
/* 47 */     return (ESUtils.refreshIndices(client, indexNames)) && (ESUtils.waitForClusterHealthiness(client, indexNames));
/*    */   }
/*    */   
/*    */   public static void refreshAndWaitForIndexReadinessRetrying(Client client, String... indexNames) {
/* 51 */     int retryCount = 0;
/* 52 */     while ((retryCount < 5) && 
/* 53 */       (!refreshAndWaitForIndexReadiness(client, indexNames)))
/*    */     {
/*    */ 
/* 56 */       ConcurrencyHelper.sleep(1000L);
/* 57 */       retryCount++;
/*    */     }
/* 59 */     if (retryCount == 5) {
/* 60 */       log.error("Could not refresh indices [{}] in node [{}].", indexNames, client.settings().get("cluster.name"));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ElasticSearchServiceHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */