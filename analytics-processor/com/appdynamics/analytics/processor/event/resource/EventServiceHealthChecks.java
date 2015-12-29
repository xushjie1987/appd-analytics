/*     */ package com.appdynamics.analytics.processor.event.resource;
/*     */ 
/*     */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ @Singleton
/*     */ public class EventServiceHealthChecks
/*     */ {
/*     */   private static final int DEFAULT_VERSION = 1;
/*     */   private static final String EVENTS_PATH = "events";
/*     */   private static final String EVENTS_USAGE_PATH = "events/{eventType}/usage";
/*     */   private static final String EVENTS_EXTRACTED_FIELDS_PATH = "events/{eventType}/extracted-fields";
/*     */   
/*     */   public static enum HealthCheckDefinition
/*     */   {
/*  39 */     ACCOUNT_LOOKUP("events", "account lookup", ImmutableList.of(Integer.valueOf(1))), 
/*  40 */     REGISTER_EVENT("events", "register", ImmutableList.of(Integer.valueOf(1), Integer.valueOf(2))), 
/*  41 */     UPDATE_EVENT("events", "update", ImmutableList.of(Integer.valueOf(1), Integer.valueOf(2))), 
/*  42 */     BULK_UPDATE_EVENT("events", "bulk update", ImmutableList.of(Integer.valueOf(1), Integer.valueOf(2))), 
/*  43 */     GET_EVENT("events", "get", ImmutableList.of(Integer.valueOf(1))), 
/*  44 */     DELETE_EVENT("events", "delete", ImmutableList.of(Integer.valueOf(1))), 
/*  45 */     HIDE_EVENT_FIELDS("events", "hide fields", ImmutableList.of(Integer.valueOf(1))), 
/*  46 */     UNHIDE_EVENT_FIELDS("events", "show (unhide) fields", ImmutableList.of(Integer.valueOf(1))), 
/*  47 */     LIST_HIDDEN_EVENT_FIELDS("events", "list hidden fields", ImmutableList.of(Integer.valueOf(1))), 
/*  48 */     PUBLISH_EVENTS("events", "publish", ImmutableList.of(Integer.valueOf(1))), 
/*  49 */     UPSERT_EVENTS("events", "upsert", ImmutableList.of(Integer.valueOf(1))), 
/*  50 */     MOVE_EVENTS("events", "move", ImmutableList.of(Integer.valueOf(1))), 
/*  51 */     METER_EVENT("events", "meter", ImmutableList.of(Integer.valueOf(1))), 
/*  52 */     EVENT_SEARCH("events", "search", ImmutableList.of(Integer.valueOf(1), Integer.valueOf(2))), 
/*  53 */     EVENT_MULTI_SEARCH("events", "multi search", ImmutableList.of(Integer.valueOf(1))), 
/*  54 */     EVENT_QUERY("events", "query", ImmutableList.of(Integer.valueOf(1))), 
/*  55 */     RELEVANT_FIELDS("events", "relevant fields", ImmutableList.of(Integer.valueOf(1))), 
/*  56 */     GET_USAGE_DOCS("events/{eventType}/usage", "documents", ImmutableList.of(Integer.valueOf(1))), 
/*  57 */     GET_USAGE_DOC_FRAGMENTS("events/{eventType}/usage", "document fragments", ImmutableList.of(Integer.valueOf(1))), 
/*  58 */     GET_USAGE_BYTES("events/{eventType}/usage", "document bytes", ImmutableList.of(Integer.valueOf(1))), 
/*  59 */     GET_EXTRACTED_FIELDS("events/{eventType}/extracted-fields", "bulk get", ImmutableList.of(Integer.valueOf(1))), 
/*  60 */     GET_EXTRACTED_FIELD("events/{eventType}/extracted-fields", "get", ImmutableList.of(Integer.valueOf(1))), 
/*  61 */     CREATE_EXTRACTED_FIELD("events/{eventType}/extracted-fields", "create", ImmutableList.of(Integer.valueOf(1))), 
/*  62 */     UPDATE_EXTRACTED_FIELD("events/{eventType}/extracted-fields", "update", ImmutableList.of(Integer.valueOf(1))), 
/*  63 */     DELETE_EXTRACTED_FIELD("events/{eventType}/extracted-fields", "delete", ImmutableList.of(Integer.valueOf(1))), 
/*  64 */     VALIDATE_EXTRACTED_FIELD("events/{eventType}/extracted-fields", "validate", ImmutableList.of(Integer.valueOf(1)));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private final String path;
/*     */     
/*     */ 
/*     */ 
/*     */     private final String methodName;
/*     */     
/*     */ 
/*     */ 
/*     */     private final List<Integer> versions;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private HealthCheckDefinition(String path, String methodName, List<Integer> versions)
/*     */     {
/*  85 */       this.path = path;
/*  86 */       this.methodName = methodName;
/*  87 */       this.versions = versions;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String getPath()
/*     */     {
/*  94 */       return this.path;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String getMethodName()
/*     */     {
/* 101 */       return this.methodName;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public List<Integer> getVersions()
/*     */     {
/* 108 */       return this.versions;
/*     */     }
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
/* 120 */   private final Map<Pair<HealthCheckDefinition, Integer>, MeteredHealthCheck> healthChecks = new HashMap(HealthCheckDefinition.values().length, 1.0F);
/*     */   @Inject
/* 122 */   public EventServiceHealthChecks(Environment environment, ConsolidatedHealthCheck consolidatedHealthCheck) { HealthCheckDefinition healthCheck; for (healthCheck : HealthCheckDefinition.values()) {
/* 123 */       for (Integer version : healthCheck.getVersions()) {
/* 124 */         Pair<HealthCheckDefinition, Integer> pair = new Pair(healthCheck, version);
/* 125 */         MeteredHealthCheck meteredHealthCheck = new MeteredHealthCheck(String.format("Resource [v%s/%s - %s]", new Object[] { version, healthCheck.getPath(), healthCheck.getMethodName() }), environment);
/*     */         
/*     */ 
/* 128 */         this.healthChecks.put(pair, meteredHealthCheck);
/* 129 */         consolidatedHealthCheck.register(meteredHealthCheck);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MeteredHealthCheck getHealthCheck(HealthCheckDefinition healthCheckDefinition)
/*     */   {
/* 142 */     return getHealthCheck(healthCheckDefinition, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MeteredHealthCheck getHealthCheck(HealthCheckDefinition healthCheckDefinition, int version)
/*     */   {
/* 154 */     Preconditions.checkNotNull(healthCheckDefinition, "healthCheckDefinition can't be null");
/* 155 */     Preconditions.checkArgument(version > 0, "version must be greater than 0");
/*     */     
/* 157 */     return (MeteredHealthCheck)this.healthChecks.get(new Pair(healthCheckDefinition, Integer.valueOf(version)));
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/EventServiceHealthChecks.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */