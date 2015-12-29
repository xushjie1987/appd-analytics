/*    */ package com.appdynamics.common.util.grok;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum GrokValueType
/*    */ {
/*    */   private GrokValueType() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 17 */   NUMBER, 
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
/* 29 */   BOOLEAN, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */   STRING, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 47 */   UNKNOWN;
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
/* 60 */   public static final GrokValueType[] VALID_VALUES = { BOOLEAN, NUMBER, STRING };
/*    */   
/*    */   public abstract GrokValueType and(GrokValueType paramGrokValueType);
/*    */   
/*    */   public static GrokValueType defaultIfNull(GrokValueType gvt) {
/* 65 */     return gvt == null ? UNKNOWN : gvt;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/grok/GrokValueType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */