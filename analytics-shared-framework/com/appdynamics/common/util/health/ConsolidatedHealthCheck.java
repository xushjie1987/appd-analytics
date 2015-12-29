/*     */ package com.appdynamics.common.util.health;
/*     */ 
/*     */ import com.codahale.metrics.health.HealthCheck;
/*     */ import com.codahale.metrics.health.HealthCheck.Result;
/*     */ import com.codahale.metrics.health.HealthCheckRegistry;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.annotation.concurrent.ThreadSafe;
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
/*     */ @ThreadSafe
/*     */ public class ConsolidatedHealthCheck
/*     */ {
/*  33 */   private static final Logger log = LoggerFactory.getLogger(ConsolidatedHealthCheck.class);
/*     */   final String name;
/*     */   final HealthCheckRegistry registry;
/*     */   final ConcurrentHashMap<String, HealthCheckable> componentHealthChecks;
/*     */   
/*     */   public ConsolidatedHealthCheck(String name, HealthCheckRegistry registry)
/*     */   {
/*  40 */     this.name = name;
/*  41 */     this.registry = registry;
/*  42 */     this.componentHealthChecks = new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */   public final String getName() {
/*  46 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Map<String, HealthCheckable> getComponents()
/*     */   {
/*  53 */     return ImmutableMap.copyOf(this.componentHealthChecks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final String makeQualifiedHealthCheckName(String componentHealthCheckName)
/*     */   {
/*  61 */     return getName() + " / " + componentHealthCheckName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void register(HealthCheckable componentHealthCheck)
/*     */   {
/*  70 */     String qualifiedHealthCheckName = makeQualifiedHealthCheckName(componentHealthCheck.getName());
/*  71 */     this.componentHealthChecks.put(qualifiedHealthCheckName, componentHealthCheck);
/*     */     
/*  73 */     this.registry.unregister(qualifiedHealthCheckName);
/*  74 */     this.registry.register(qualifiedHealthCheckName, asHealthCheck(componentHealthCheck));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static HealthCheck asHealthCheck(final HealthCheckable healthCheckable)
/*     */   {
/*  82 */     (healthCheckable instanceof HealthCheck) ? (HealthCheck)healthCheckable : new SimpleHealthCheck(healthCheckable.getName())
/*     */     {
/*     */ 
/*     */       public HealthCheck.Result check()
/*     */       {
/*  87 */         return ConsolidatedHealthCheck.safeCheck(healthCheckable);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   static HealthCheck.Result safeCheck(HealthCheckable healthCheckable) {
/*     */     try {
/*  94 */       return healthCheckable.check();
/*     */     } catch (Throwable t) {
/*  96 */       return HealthCheck.Result.unhealthy(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void unregister(String componentHealthCheckName)
/*     */   {
/* 106 */     String qualifiedHealthCheckName = makeQualifiedHealthCheckName(componentHealthCheckName);
/* 107 */     internalUnregister(qualifiedHealthCheckName);
/*     */   }
/*     */   
/*     */   private void internalUnregister(String qualifiedHealthCheckName) {
/* 111 */     this.registry.unregister(qualifiedHealthCheckName);
/* 112 */     this.componentHealthChecks.remove(qualifiedHealthCheckName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final HealthCheck.Result checkComponents()
/*     */   {
/* 120 */     for (HealthCheckable child : this.componentHealthChecks.values()) {
/* 121 */       HealthCheck.Result result = safeCheck(child);
/* 122 */       if (!result.isHealthy()) {
/* 123 */         return result;
/*     */       }
/*     */     }
/*     */     
/* 127 */     return HealthCheck.Result.healthy();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void discard()
/*     */   {
/* 135 */     List<String> names = new LinkedList(this.componentHealthChecks.keySet());
/* 136 */     for (String s : names) {
/* 137 */       internalUnregister(s);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/ConsolidatedHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */