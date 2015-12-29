/*    */ package com.appdynamics.common.util.var;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.lang.WordUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DottedKeyMapBasedVariableResolver
/*    */   extends AbstractVariableResolver
/*    */ {
/*    */   final Map<String, ?> map;
/* 19 */   private boolean failIfNotResolved = false;
/*    */   
/*    */   public DottedKeyMapBasedVariableResolver(Map<String, ?> map) {
/* 22 */     this.map = map;
/*    */   }
/*    */   
/*    */   public DottedKeyMapBasedVariableResolver(Map<String, ?> map, boolean failIfNotResolved) {
/* 26 */     this.map = map;
/* 27 */     this.failIfNotResolved = failIfNotResolved;
/*    */   }
/*    */   
/*    */   public Object resolve(String variable)
/*    */   {
/* 32 */     Object value = resolveInternal(variable);
/* 33 */     if ((value == null) && (this.failIfNotResolved)) {
/* 34 */       throw new IllegalArgumentException("Unable to resolve variable [" + variable + "]");
/*    */     }
/* 36 */     return value;
/*    */   }
/*    */   
/*    */   private Object resolveInternal(String variable) {
/* 40 */     String[] path = variable.split("\\.");
/* 41 */     if (path.length == 0) {
/* 42 */       return null;
/*    */     }
/* 44 */     Object value = this.map.get(path[0]);
/* 45 */     if (value == null) {
/* 46 */       return null;
/*    */     }
/* 48 */     return resolveFromPath(path, 1, value);
/*    */   }
/*    */   
/*    */   private Object resolveFromPath(String[] path, int offset, Object source) {
/* 52 */     if (offset == path.length) {
/* 53 */       return source;
/*    */     }
/* 55 */     if ((source instanceof Map)) {
/* 56 */       return resolveFromPath(path, offset + 1, ((Map)source).get(path[offset]));
/*    */     }
/* 58 */     String key = WordUtils.capitalize(path[offset]);
/*    */     try {
/* 60 */       Method m = source.getClass().getMethod("get" + key, new Class[0]);
/* 61 */       return m.invoke(source, new Object[0]);
/*    */     } catch (ReflectiveOperationException e) {}
/* 63 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/DottedKeyMapBasedVariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */