/*    */ package com.appdynamics.analytics.message;
/*    */ 
/*    */ import com.appdynamics.analytics.message.api.MessageSource;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.appdynamics.common.util.misc.ParameterAwareSupplier;
/*    */ import com.appdynamics.common.util.misc.ParameterAwareSupplier.Helper;
/*    */ import com.google.common.base.Supplier;
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.google.inject.Singleton;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import javax.annotation.PreDestroy;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Singleton
/*    */ public class MessageSources
/*    */ {
/*    */   private final Map<Object, Supplier<? extends MessageSource<?, ?>>> registry;
/*    */   
/*    */   MessageSources()
/*    */   {
/* 29 */     this.registry = new ConcurrentHashMap();
/*    */   }
/*    */   
/*    */   public void register(Object srcId, Supplier<? extends MessageSource<?, ?>> messageSourceSupplier) {
/* 33 */     this.registry.put(srcId, messageSourceSupplier);
/*    */   }
/*    */   
/*    */   public void register(Object srcId, ParameterAwareSupplier<?, ? extends MessageSource<?, ?>> messageSourceSupplier) {
/* 37 */     this.registry.put(srcId, messageSourceSupplier);
/*    */   }
/*    */   
/*    */   public Supplier<? extends MessageSource<?, ?>> getSupplier(Object srcId) {
/* 41 */     return (Supplier)this.registry.get(srcId);
/*    */   }
/*    */   
/*    */   public MessageSource<?, ?> get(Object srcId) {
/* 45 */     Supplier<? extends MessageSource<?, ?>> supplier = getSupplier(srcId);
/* 46 */     return supplier != null ? (MessageSource)supplier.get() : null;
/*    */   }
/*    */   
/*    */   public MessageSource<?, ?> get(Object srcId, Object additionalParam)
/*    */   {
/* 51 */     Supplier<? extends MessageSource<?, ?>> supplier = getSupplier(srcId);
/* 52 */     if (supplier == null) {
/* 53 */       return null;
/*    */     }
/* 55 */     if ((supplier instanceof ParameterAwareSupplier)) {
/* 56 */       ParameterAwareSupplier<Object, ? extends MessageSource<?, ?>> pas = (ParameterAwareSupplier)supplier;
/* 57 */       Object convertedParam = null;
/*    */       try {
/* 59 */         TypeToken parameterTypeToken = ParameterAwareSupplier.Helper.findParameterType(pas);
/*    */         
/* 61 */         convertedParam = Reader.readFrom(parameterTypeToken, additionalParam);
/*    */       } catch (RuntimeException e) {
/* 63 */         throw new RuntimeException("An error occurred while attempting to transform the parameter [" + additionalParam + "] of type [" + additionalParam.getClass().getName() + "] to the type required by [" + pas.getClass().getName() + "]");
/*    */       }
/*    */       
/*    */ 
/*    */ 
/* 68 */       return (MessageSource)pas.get(convertedParam);
/*    */     }
/* 70 */     throw new UnsupportedOperationException("The supplier [" + supplier.getClass().getName() + "] is not an instance of [" + ParameterAwareSupplier.class.getName() + "]");
/*    */   }
/*    */   
/*    */ 
/*    */   public Supplier<? extends MessageSource<?, ?>> unregister(Object srcId)
/*    */   {
/* 76 */     return (Supplier)this.registry.remove(srcId);
/*    */   }
/*    */   
/*    */   @PreDestroy
/*    */   public void removeAll() {
/* 81 */     this.registry.clear();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/message/MessageSources.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */