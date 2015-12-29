/*    */ package com.appdynamics.common.util.var;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.joda.time.DateTime;
/*    */ import org.joda.time.format.DateTimeFormat;
/*    */ import org.joda.time.format.DateTimeFormatter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CalendarVariableResolver
/*    */   implements VariableResolver
/*    */ {
/* 27 */   INSTANCE;
/*    */   
/* 25 */   private static final Logger log = LoggerFactory.getLogger(CalendarVariableResolver.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String VAR_NOW = "calendar.now";
/*    */   
/*    */ 
/*    */ 
/* 34 */   static final ConcurrentHashMap<String, DateTimeFormatter> DATE_TIME_FORMATS = new ConcurrentHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */   private CalendarVariableResolver() {}
/*    */   
/*    */ 
/*    */   public Object resolve(String variable)
/*    */   {
/* 43 */     return null;
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
/* 54 */     if (!variable.equals("calendar.now")) {
/* 55 */       return null;
/*    */     }
/*    */     
/* 58 */     DateTimeFormatter fmt = (DateTimeFormatter)DATE_TIME_FORMATS.get(variableExtension);
/* 59 */     if (fmt == null) {
/*    */       try {
/* 61 */         fmt = DateTimeFormat.forPattern(variableExtension);
/*    */       } catch (IllegalArgumentException e) {
/* 63 */         log.error("Error occurred while parsing date time format [" + variableExtension + "]", e);
/*    */         
/* 65 */         return null;
/*    */       }
/*    */     }
/* 68 */     DATE_TIME_FORMATS.putIfAbsent(variableExtension, fmt);
/*    */     
/* 70 */     return DateTime.now().toString(fmt);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/CalendarVariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */