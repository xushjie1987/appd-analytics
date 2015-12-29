/*    */ package com.appdynamics.common.util.configuration;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TemplatesModule
/*    */   extends Module<Map<String, String>>
/*    */ {
/* 18 */   private static final Logger log = LoggerFactory.getLogger(TemplatesModule.class);
/*    */   
/*    */   public void configure()
/*    */   {
/* 22 */     bind(Templates.class).toInstance(new Templates((Map)getConfiguration()));
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/TemplatesModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */