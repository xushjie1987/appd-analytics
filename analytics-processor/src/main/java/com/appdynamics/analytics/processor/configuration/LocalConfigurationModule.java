/*    */ package com.appdynamics.analytics.processor.configuration;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.FrameworkHelper;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import com.netflix.config.ConcurrentCompositeConfiguration;
/*    */ import org.apache.commons.configuration.AbstractConfiguration;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocalConfigurationModule
/*    */   extends ConfigurationModuleBase
/*    */ {
/* 18 */   private static final Logger log = LoggerFactory.getLogger(LocalConfigurationModule.class);
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   public AbstractConfiguration makeLocalConfiguration()
/*    */   {
/* 24 */     ConcurrentCompositeConfiguration configuration = new ConcurrentCompositeConfiguration();
/*    */     
/* 26 */     String propertiesFile = FrameworkHelper.getPropertiesFileLocation();
/* 27 */     ConfigurationHelper.addSystemConfiguration(configuration);
/* 28 */     ConfigurationHelper.addLocalConfiguration(configuration, propertiesFile, log);
/*    */     
/* 30 */     return configuration;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/configuration/LocalConfigurationModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */