/*    */ package com.appdynamics.common.framework.util;
/*    */ 
/*    */ import com.appdynamics.common.framework.Configurable;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.google.common.reflect.TypeToken;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractConfigurable<C>
/*    */   implements Configurable<C>
/*    */ {
/*    */   private final TypeToken<C> configurationType;
/*    */   
/*    */   public String toString()
/*    */   {
/* 19 */     return "AbstractConfigurable(uri=" + getUri() + ", configuration=" + getConfiguration() + ")";
/*    */   }
/*    */   
/* 22 */   private volatile String uri = getClass().getSimpleName().toLowerCase();
/*    */   private volatile C configuration;
/*    */   
/*    */   protected AbstractConfigurable() {
/* 26 */     this.configurationType = new TypeToken(getClass()) {};
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected AbstractConfigurable(TypeToken<C> configurationType)
/*    */   {
/* 35 */     this.configurationType = configurationType;
/*    */   }
/*    */   
/*    */   public TypeToken<C> getConfigurationType()
/*    */   {
/* 40 */     return this.configurationType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void setConfiguration(C configuration)
/*    */   {
/* 49 */     this.configuration = configuration;
/* 50 */     Reader.validate(configuration);
/*    */   }
/*    */   
/*    */   public final C getConfiguration()
/*    */   {
/* 55 */     return (C)this.configuration;
/*    */   }
/*    */   
/*    */   public final String getUri()
/*    */   {
/* 60 */     return this.uri;
/*    */   }
/*    */   
/*    */   public final void setUri(String uri)
/*    */   {
/* 65 */     if (uri != null) {
/* 66 */       this.uri = uri;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/AbstractConfigurable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */