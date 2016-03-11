/*    */ package com.appdynamics.analytics.processor.admin;
/*    */ 
/*    */ import java.util.Arrays;
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
/*    */ 
/*    */ public enum ActionType
/*    */ {
/* 19 */   EVENT_PUBLISH, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 29 */   EVENT_UPSERT, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 38 */   EVENT_TRANSIENT_ERROR, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 47 */   EVENT_PERMANENT_ERROR;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private ActionType() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract String nameSuffix();
/*    */   
/*    */ 
/*    */ 
/*    */   public static ActionType findType(String fullyQualifiedName)
/*    */   {
/* 62 */     for (ActionType type : ) {
/* 63 */       if (fullyQualifiedName.endsWith(type.nameSuffix())) {
/* 64 */         return type;
/*    */       }
/*    */     }
/*    */     
/* 68 */     throw new IllegalArgumentException("The name [" + fullyQualifiedName + "] could not be mapped to any of the known types " + Arrays.asList(values()));
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/ActionType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */