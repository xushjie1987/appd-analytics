/*    */ package com.appdynamics.common.util.var;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapBasedVariableResolver
/*    */   extends AbstractVariableResolver
/*    */ {
/*    */   final Map<String, ?> map;
/*    */   
/*    */   public MapBasedVariableResolver(Map<String, ?> map)
/*    */   {
/* 19 */     this.map = map;
/*    */   }
/*    */   
/*    */   public Object resolve(String variable)
/*    */   {
/* 24 */     return this.map.get(variable);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/MapBasedVariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */