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
/*    */ 
/*    */ public enum EchoVariableResolver
/*    */   implements VariableResolver
/*    */ {
/* 17 */   INSTANCE;
/*    */   
/*    */   private EchoVariableResolver() {}
/*    */   
/* 21 */   public String resolve(String variable) { return "${" + variable + "}"; }
/*    */   
/*    */ 
/*    */   public String resolve(String variable, String variableExtension)
/*    */   {
/* 26 */     return "${" + variable + "(" + variableExtension + ")}";
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/EchoVariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */