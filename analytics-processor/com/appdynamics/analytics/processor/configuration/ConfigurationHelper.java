/*    */ package com.appdynamics.analytics.processor.configuration;
/*    */ 
/*    */ import com.netflix.config.ConcurrentCompositeConfiguration;
/*    */ import com.netflix.config.ConcurrentMapConfiguration;
/*    */ import com.netflix.config.DynamicWatchedConfiguration;
/*    */ import com.netflix.config.source.ZooKeeperConfigurationSource;
/*    */ import org.apache.commons.configuration.PropertiesConfiguration;
/*    */ import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
/*    */ import org.apache.curator.framework.CuratorFramework;
/*    */ import org.slf4j.Logger;
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
/*    */ public final class ConfigurationHelper
/*    */ {
/*    */   public static void addSystemConfiguration(ConcurrentCompositeConfiguration configuration)
/*    */   {
/* 27 */     ConcurrentMapConfiguration systemConfig = new ConcurrentMapConfiguration();
/* 28 */     systemConfig.loadProperties(System.getProperties());
/* 29 */     configuration.addConfiguration(systemConfig);
/*    */   }
/*    */   
/*    */   public static void addLocalConfiguration(ConcurrentCompositeConfiguration configuration, String propertiesFile, Logger log)
/*    */   {
/*    */     try {
/* 35 */       PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(propertiesFile);
/* 36 */       propertiesConfiguration.setReloadingStrategy(new FileChangedReloadingStrategy());
/* 37 */       configuration.addConfigurationAtFront(propertiesConfiguration, propertiesFile);
/* 38 */       log.info("Dynamic configuration established with .properties file [{}]", propertiesFile);
/*    */     } catch (org.apache.commons.configuration.ConfigurationException e) {
/* 40 */       log.error("Failed to initialize dynamic configuration with local properties file [{}]", propertiesFile);
/* 41 */       throw new com.appdynamics.common.util.configuration.ConfigurationException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void addZookeeperConfiguration(ConcurrentCompositeConfiguration configuration, CuratorFramework zkClient, Logger log)
/*    */   {
/* 47 */     String zkConfigurationPath = ConfigurationPropertyConstants.ZK_CONFIGURATION_PATH;
/*    */     try {
/* 49 */       ZooKeeperConfigurationSource zkConfigSource = new ZooKeeperConfigurationSource(zkClient, zkConfigurationPath);
/*    */       
/* 51 */       zkConfigSource.start();
/*    */       
/* 53 */       DynamicWatchedConfiguration zkDynamicOverrideConfig = new DynamicWatchedConfiguration(zkConfigSource);
/* 54 */       configuration.addConfigurationAtFront(zkDynamicOverrideConfig, zkConfigurationPath);
/*    */       
/* 56 */       log.info("Dynamic configuration override established with zookeeper [{}]", zkConfigurationPath);
/*    */     } catch (Exception e) {
/* 58 */       log.error("Failed to initialize dynamic configuration override from zookeeper [{}]", zkConfigurationPath);
/* 59 */       throw new com.appdynamics.common.util.configuration.ConfigurationException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/configuration/ConfigurationHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */