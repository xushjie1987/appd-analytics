/*     */ package com.appdynamics.common.util.configuration;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Parameters
/*     */ {
/*     */   public static String toStringOrNull(Object obj)
/*     */   {
/*  29 */     return (obj instanceof String) ? (String)obj : obj == null ? null : obj.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toStringOrBlank(Object obj)
/*     */   {
/*  37 */     return (obj instanceof String) ? (String)obj : obj == null ? "" : obj.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String defaultIfBlank(String input, String defaultValue, boolean trim)
/*     */   {
/*  48 */     if ((input == null) || (input.length() == 0)) {
/*  49 */       return defaultValue;
/*     */     }
/*     */     
/*  52 */     if (!trim) {
/*  53 */       return input;
/*     */     }
/*     */     
/*  56 */     String s = input.trim();
/*     */     
/*  58 */     return s.length() == 0 ? defaultValue : s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String defaultIfBlank(String input, String defaultValue)
/*     */   {
/*  67 */     return defaultIfBlank(input, defaultValue, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String asMandatoryString(Object param, String exceptionMessage, boolean trim)
/*     */   {
/*  79 */     String paramStr = defaultIfBlank(toStringOrNull(param), null, trim);
/*  80 */     if (paramStr == null) {
/*  81 */       throw new ConfigurationException(exceptionMessage);
/*     */     }
/*     */     
/*  84 */     return paramStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String asMandatoryString(Object param, String exceptionMessage)
/*     */   {
/*  96 */     return asMandatoryString(param, exceptionMessage, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String asString(Object param, String defaultValue)
/*     */   {
/* 105 */     return defaultIfBlank(toStringOrNull(param), defaultValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T firstNonNull(T a, T b, T c)
/*     */   {
/* 118 */     T x = a == null ? b : a;
/* 119 */     return (T)Objects.firstNonNull(x, c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map<String, Object> consolidate(Map<String, Object> level1, Map<String, Object> level2)
/*     */   {
/* 135 */     Map<String, Object> map = null;
/* 136 */     if (level1 != null) {
/* 137 */       map = new LinkedHashMap(level1);
/*     */     }
/*     */     
/* 140 */     if (map == null) {
/* 141 */       if (level2 == null) {
/* 142 */         throw new IllegalArgumentException("Fields/properties must be specified");
/*     */       }
/* 144 */       map = new LinkedHashMap(8);
/*     */     }
/*     */     
/*     */ 
/* 148 */     if (level2 != null) {
/* 149 */       map.putAll(level2);
/*     */     }
/*     */     
/* 152 */     return map;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/Parameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */