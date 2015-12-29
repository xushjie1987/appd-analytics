/*    */ package com.appdynamics.common.util.grok;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSortedMap;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
/*    */ import java.util.regex.Pattern;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RegexHelper
/*    */ {
/*    */   private static Method cachedNamedGroupsGetter;
/*    */   
/*    */   private static Method namedGroupsGetter()
/*    */     throws NoSuchMethodException
/*    */   {
/* 34 */     if (cachedNamedGroupsGetter != null) {
/* 35 */       return cachedNamedGroupsGetter;
/*    */     }
/*    */     
/*    */ 
/* 39 */     Method method = Pattern.class.getDeclaredMethod("namedGroups", new Class[0]);
/*    */     
/* 41 */     method.setAccessible(true);
/*    */     
/* 43 */     cachedNamedGroupsGetter = method;return method;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static Map<String, Integer> namedGroups(Pattern pattern)
/*    */   {
/*    */     try
/*    */     {
/* 55 */       Method method = namedGroupsGetter();
/* 56 */       Object o = method.invoke(pattern, new Object[0]);
/* 57 */       Map<String, Integer> mapOfGroupNames = (Map)o;
/*    */       
/* 59 */       return ImmutableSortedMap.copyOf(mapOfGroupNames);
/*    */     } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
/* 61 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String removePunctuations(String nonCompliantName)
/*    */   {
/* 71 */     return nonCompliantName.replaceAll("\\p{Punct}", "");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/grok/RegexHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */