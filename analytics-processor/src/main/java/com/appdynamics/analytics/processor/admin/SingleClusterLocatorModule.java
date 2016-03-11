/*    */ package com.appdynamics.analytics.processor.admin;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.FrameworkHelper;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import java.util.Properties;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SingleClusterLocatorModule
/*    */   extends Module<Object>
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(SingleClusterLocatorModule.class);
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   Locator makeLocator() {
/* 32 */     return new DummyLocator();
/*    */   }
/*    */   
/*    */   static class DummyLocator implements Locator {
/*    */     final String clusterName;
/*    */     
/*    */     DummyLocator() {
/* 39 */       String propertyName = "ad.es.cluster.name";
/* 40 */       this.clusterName = FrameworkHelper.getProperties().getProperty(propertyName);
/*    */     }
/*    */     
/*    */     public String findActiveClusterName(String account)
/*    */     {
/* 45 */       return this.clusterName;
/*    */     }
/*    */     
/*    */     public String findTopicName(String clusterName, ActionType actionType)
/*    */     {
/* 50 */       throw new UnsupportedOperationException();
/*    */     }
/*    */     
/*    */     public String findTopicName(String account, String eventType, ActionType actionType)
/*    */     {
/* 55 */       throw new UnsupportedOperationException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/SingleClusterLocatorModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */