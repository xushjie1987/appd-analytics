/*    */ package com.appdynamics.common.util.var;
/*    */ 
/*    */ import com.google.common.base.Objects;
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import javax.annotation.concurrent.ThreadSafe;
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
/*    */ @ThreadSafe
/*    */ public final class LocalContextVariableResolver
/*    */   extends AbstractVariableResolver
/*    */ {
/* 28 */   private static final VariableResolver DEFAULT_RESOLVER = new ChainedVariableResolver(SystemPropertyResolver.INSTANCE, SystemVariableResolver.INSTANCE, new VariableResolver[] { CalendarVariableResolver.INSTANCE });
/*    */   
/*    */ 
/* 31 */   private static final ThreadLocal<LocalContextVariableResolver> PER_THREAD_INSTANCE = new ThreadLocal()
/*    */   {
/*    */     protected LocalContextVariableResolver initialValue()
/*    */     {
/* 35 */       return new LocalContextVariableResolver(null);
/*    */     }
/*    */   };
/*    */   
/*    */   private Map<String, String> localContextMap;
/*    */   
/*    */   public Object resolve(String variable)
/*    */   {
/* 43 */     Object obj = this.localContextMap.get(variable);
/* 44 */     if (obj != null) {
/* 45 */       return obj;
/*    */     }
/* 47 */     return DEFAULT_RESOLVER.resolve(variable);
/*    */   }
/*    */   
/*    */   public Object resolve(String variable, String variableExtension)
/*    */   {
/* 52 */     return DEFAULT_RESOLVER.resolve(variable, variableExtension);
/*    */   }
/*    */   
/*    */   private VariableResolver setContext(Map<String, String> localContextMap) {
/* 56 */     this.localContextMap = ((Map)Objects.firstNonNull(localContextMap, Collections.emptyMap()));
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public static VariableResolver getResolver(Map<String, String> localContextMap) {
/* 61 */     return ((LocalContextVariableResolver)PER_THREAD_INSTANCE.get()).setContext(localContextMap);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/LocalContextVariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */