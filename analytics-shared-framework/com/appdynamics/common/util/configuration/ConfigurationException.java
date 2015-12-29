/*    */ package com.appdynamics.common.util.configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigurationException
/*    */   extends IllegalArgumentException
/*    */ {
/*    */   public ConfigurationException(String message)
/*    */   {
/* 16 */     super(message);
/*    */   }
/*    */   
/*    */   public ConfigurationException(String message, Throwable cause) {
/* 20 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ConfigurationException(Throwable cause) {
/* 24 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/ConfigurationException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */