/*    */ package com.appdynamics.common.util.var;
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
/*    */ public enum SystemPropertyResolver
/*    */   implements VariableResolver
/*    */ {
/* 16 */   INSTANCE;
/*    */   
/*    */   private SystemPropertyResolver() {}
/*    */   
/* 20 */   public String resolve(String variable) { String value = System.getProperty(variable);
/* 21 */     if (value == null) {
/* 22 */       value = System.getenv(variable);
/*    */     }
/*    */     
/* 25 */     return value;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object resolve(String variable, String variableExtension)
/*    */   {
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/SystemPropertyResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */