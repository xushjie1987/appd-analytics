/*    */ package com.appdynamics.common.util.configuration;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Inject;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
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
/*    */ public class SystemPropertiesModule
/*    */   extends Module<Map<String, String>>
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(SystemPropertiesModule.class);
/*    */   
/*    */   @Inject
/*    */   void onStart()
/*    */   {
/* 28 */     Map<String, String> systemProperties = (Map)getConfiguration();
/* 29 */     for (Map.Entry<String, String> entry : systemProperties.entrySet()) {
/* 30 */       log.info("Setting system property [{}] to [{}]", entry.getKey(), entry.getValue());
/* 31 */       System.setProperty((String)entry.getKey(), (String)entry.getValue());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/SystemPropertiesModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */