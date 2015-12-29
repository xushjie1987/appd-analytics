/*    */ package com.appdynamics.analytics.processor;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.FrameworkHelper;
/*    */ import com.appdynamics.common.framework.util.SimpleApp;
/*    */ import com.appdynamics.common.framework.util.VersionedAppConfiguration;
/*    */ import com.google.common.base.Optional;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import java.util.Arrays;
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
/*    */ public class AnalyticsService
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);
/*    */   
/*    */ 
/*    */ 
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 34 */     log.info("Starting analytics processor with arguments " + Arrays.toString(args));
/* 35 */     new SimpleVersionedApp().runUsingTemplate(args);
/*    */   }
/*    */   
/*    */   public static class SimpleVersionedApp extends SimpleApp<VersionedAppConfiguration>
/*    */   {
/*    */     protected void beforeModuleLoading(VersionedAppConfiguration conf, Environment env) {
/* 41 */       FrameworkHelper.checkVersionsMatch(1, conf, Optional.of("ad.configuration.version"));
/*    */       
/* 43 */       super.beforeModuleLoading(conf, env);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/AnalyticsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */