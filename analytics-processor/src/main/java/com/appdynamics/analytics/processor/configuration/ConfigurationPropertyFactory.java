/*     */ package com.appdynamics.analytics.processor.configuration;
/*     */ 
/*     */ import com.netflix.config.DynamicBooleanProperty;
/*     */ import com.netflix.config.DynamicDoubleProperty;
/*     */ import com.netflix.config.DynamicFloatProperty;
/*     */ import com.netflix.config.DynamicIntProperty;
/*     */ import com.netflix.config.DynamicLongProperty;
/*     */ import com.netflix.config.DynamicPropertyFactory;
/*     */ import com.netflix.config.DynamicStringProperty;
/*     */ 
/*     */ public class ConfigurationPropertyFactory
/*     */ {
/*     */   private final DynamicPropertyFactory dynamicPropertyFactory;
/*     */   
/*     */   public ConfigurationPropertyFactory(DynamicPropertyFactory dynamicPropertyFactory)
/*     */   {
/*  17 */     this.dynamicPropertyFactory = dynamicPropertyFactory;
/*     */   }
/*     */   
/*     */   private String customerPropertyName(String customer, String propName) {
/*  21 */     return String.format("%s.%s", new Object[] { propName, customer });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicStringProperty getStringProperty(String propName, String defaultValue)
/*     */   {
/*  30 */     return this.dynamicPropertyFactory.getStringProperty(propName, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicStringProperty getStringProperty(String propName, String defaultValue, Runnable propertyChangeCallback)
/*     */   {
/*  41 */     return this.dynamicPropertyFactory.getStringProperty(propName, defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicIntProperty getIntProperty(String propName, int defaultValue)
/*     */   {
/*  50 */     return this.dynamicPropertyFactory.getIntProperty(propName, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicIntProperty getIntProperty(String propName, int defaultValue, Runnable propertyChangeCallback)
/*     */   {
/*  60 */     return this.dynamicPropertyFactory.getIntProperty(propName, defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicLongProperty getLongProperty(String propName, long defaultValue)
/*     */   {
/*  69 */     return this.dynamicPropertyFactory.getLongProperty(propName, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicLongProperty getLongProperty(String propName, long defaultValue, Runnable propertyChangeCallback)
/*     */   {
/*  80 */     return this.dynamicPropertyFactory.getLongProperty(propName, defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicBooleanProperty getBooleanProperty(String propName, boolean defaultValue)
/*     */   {
/*  89 */     return this.dynamicPropertyFactory.getBooleanProperty(propName, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicBooleanProperty getBooleanProperty(String propName, boolean defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 100 */     return this.dynamicPropertyFactory.getBooleanProperty(propName, defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicFloatProperty getFloatProperty(String propName, float defaultValue)
/*     */   {
/* 109 */     return this.dynamicPropertyFactory.getFloatProperty(propName, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicFloatProperty getFloatProperty(String propName, float defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 120 */     return this.dynamicPropertyFactory.getFloatProperty(propName, defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicDoubleProperty getDoubleProperty(String propName, double defaultValue)
/*     */   {
/* 129 */     return this.dynamicPropertyFactory.getDoubleProperty(propName, defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicDoubleProperty getDoubleProperty(String propName, double defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 140 */     return this.dynamicPropertyFactory.getDoubleProperty(propName, defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicStringProperty getStringProperty(String customer, String propName, String defaultValue)
/*     */   {
/* 150 */     return this.dynamicPropertyFactory.getStringProperty(customerPropertyName(customer, propName), defaultValue, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicStringProperty getStringProperty(String customer, String propName, String defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 162 */     return this.dynamicPropertyFactory.getStringProperty(customerPropertyName(customer, propName), defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicIntProperty getIntProperty(String customer, String propName, int defaultValue)
/*     */   {
/* 173 */     return this.dynamicPropertyFactory.getIntProperty(customerPropertyName(customer, propName), defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicIntProperty getIntProperty(String customer, String propName, int defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 185 */     return this.dynamicPropertyFactory.getIntProperty(customerPropertyName(customer, propName), defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicLongProperty getLongProperty(String customer, String propName, long defaultValue)
/*     */   {
/* 196 */     return this.dynamicPropertyFactory.getLongProperty(customerPropertyName(customer, propName), defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicLongProperty getLongProperty(String customer, String propName, long defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 208 */     return this.dynamicPropertyFactory.getLongProperty(customerPropertyName(customer, propName), defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicBooleanProperty getBooleanProperty(String customer, String propName, boolean defaultValue)
/*     */   {
/* 219 */     return this.dynamicPropertyFactory.getBooleanProperty(customerPropertyName(customer, propName), defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicBooleanProperty getBooleanProperty(String customer, String propName, boolean defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 231 */     return this.dynamicPropertyFactory.getBooleanProperty(customerPropertyName(customer, propName), defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicFloatProperty getFloatProperty(String customer, String propName, float defaultValue)
/*     */   {
/* 242 */     return this.dynamicPropertyFactory.getFloatProperty(customerPropertyName(customer, propName), defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicFloatProperty getFloatProperty(String customer, String propName, float defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 254 */     return this.dynamicPropertyFactory.getFloatProperty(customerPropertyName(customer, propName), defaultValue, propertyChangeCallback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicDoubleProperty getDoubleProperty(String customer, String propName, double defaultValue)
/*     */   {
/* 265 */     return this.dynamicPropertyFactory.getDoubleProperty(customerPropertyName(customer, propName), defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicDoubleProperty getDoubleProperty(String customer, String propName, double defaultValue, Runnable propertyChangeCallback)
/*     */   {
/* 277 */     return this.dynamicPropertyFactory.getDoubleProperty(customerPropertyName(customer, propName), defaultValue, propertyChangeCallback);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/configuration/ConfigurationPropertyFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */