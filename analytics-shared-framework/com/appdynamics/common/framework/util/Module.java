/*     */ package com.appdynamics.common.framework.util;
/*     */ 
/*     */ import com.appdynamics.common.framework.Configurable;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.google.inject.AbstractModule;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Module<C>
/*     */   extends AbstractModule
/*     */   implements Configurable<C>
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(Module.class);
/*     */   
/*  40 */   private final TypeToken<C> configurationType = new TypeToken(getClass()) {};
/*     */   
/*  42 */   private final AbstractConfigurable<C> configurable = new AbstractConfigurable(this.configurationType) {};
/*     */   
/*     */ 
/*     */   public final TypeToken<C> getConfigurationType()
/*     */   {
/*  47 */     return this.configurable.getConfigurationType();
/*     */   }
/*     */   
/*     */   public final String getUri()
/*     */   {
/*  52 */     return this.configurable.getUri();
/*     */   }
/*     */   
/*     */   public final C getConfiguration()
/*     */   {
/*  57 */     return (C)this.configurable.getConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setUri(String uri)
/*     */   {
/*  66 */     this.configurable.setUri(uri);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setConfiguration(C configuration)
/*     */   {
/*  76 */     this.configurable.setConfiguration(configuration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configure() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String printableName()
/*     */   {
/*  92 */     String name = getClass().getName();
/*  93 */     String s = getUri();
/*  94 */     name = name + (s != null ? " (" + s + ")" : "");
/*  95 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     return printableName();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/Module.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */