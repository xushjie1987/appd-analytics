/*    */ package com.appdynamics.analytics.processor.configuration;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import com.netflix.config.ConfigurationManager;
/*    */ import com.netflix.config.DynamicPropertyFactory;
/*    */ import org.apache.commons.configuration.AbstractConfiguration;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigurationModuleBase
/*    */   extends Module<Object>
/*    */ {
/* 18 */   private static final Logger log = LoggerFactory.getLogger(ConfigurationModuleBase.class);
/*    */   
/*    */   volatile ConfigurationPropertyFactory configurationPropertyFactory;
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   public ConfigurationPropertyFactory makeConfigurationPropertyFactory(AbstractConfiguration configuration)
/*    */   {
/* 26 */     if (this.configurationPropertyFactory == null) {
/* 27 */       if (!ConfigurationManager.isConfigurationInstalled()) {
/* 28 */         ConfigurationManager.install(configuration);
/*    */       }
/* 30 */       DynamicPropertyFactory dynamicPropertyFactory = DynamicPropertyFactory.getInstance();
/*    */       
/* 32 */       this.configurationPropertyFactory = new ConfigurationPropertyFactory(dynamicPropertyFactory);
/*    */     }
/*    */     
/* 35 */     return this.configurationPropertyFactory;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/configuration/ConfigurationModuleBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */