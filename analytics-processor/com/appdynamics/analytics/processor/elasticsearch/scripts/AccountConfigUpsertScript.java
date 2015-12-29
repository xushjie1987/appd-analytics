/*     */ package com.appdynamics.analytics.processor.elasticsearch.scripts;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.elasticsearch.script.AbstractExecutableScript;
/*     */ import org.elasticsearch.script.ScriptService.ScriptType;
/*     */ import org.joda.time.DateTime;
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
/*     */ public class AccountConfigUpsertScript
/*     */   extends AbstractExecutableScript
/*     */ {
/*  29 */   private static final Logger log = LoggerFactory.getLogger(AccountConfigUpsertScript.class);
/*     */   
/*     */ 
/*     */ 
/*  33 */   public static final ScriptService.ScriptType SCRIPT_TYPE = ScriptService.ScriptType.INLINE;
/*     */   
/*     */   public static final String PARAM_DOC = "doc";
/*     */   
/*     */   public static final String SCRIPT_LANG = "native";
/*     */   
/*     */   private final Map<String, Object> doc;
/*     */   private Map ctx;
/*     */   
/*     */   AccountConfigUpsertScript(Map<String, Object> params)
/*     */   {
/*  44 */     this.doc = getFieldsToUpdate(params, "doc");
/*     */   }
/*     */   
/*     */   private static Map<String, Object> getFieldsToUpdate(Map<String, Object> params, String paramName) {
/*  48 */     Object obj = params.get(paramName);
/*     */     Map<String, Object> fieldsToUpdate;
/*  50 */     if (obj == null) {
/*  51 */       fieldsToUpdate = new LinkedHashMap();
/*     */     } else { Map<String, Object> fieldsToUpdate;
/*  53 */       if ((obj instanceof Map)) {
/*  54 */         fieldsToUpdate = (Map)obj;
/*     */       } else {
/*  56 */         throw new IllegalArgumentException("Script parameter " + paramName + " must be a Map, instead got " + obj.getClass());
/*     */       }
/*     */     }
/*     */     
/*     */     Map<String, Object> fieldsToUpdate;
/*  61 */     return fieldsToUpdate;
/*     */   }
/*     */   
/*     */   private static Map<String, Object> updateAccountConfiguration(Map existingConfig, Map<String, Object> fieldsToUpdate)
/*     */   {
/*  66 */     Map<String, Object> result = new LinkedHashMap();
/*     */     
/*  68 */     DateTime existingExpiryDate = null;
/*  69 */     if (existingConfig.get("expirationDate") != null) {
/*  70 */       existingExpiryDate = DateTime.parse(String.valueOf(existingConfig.get("expirationDate")));
/*     */     }
/*     */     
/*  73 */     DateTime resolvedExpiryDate = existingExpiryDate;
/*  74 */     if (fieldsToUpdate.get("expirationDate") != null) {
/*  75 */       resolvedExpiryDate = determineAccountExpiryDate(existingExpiryDate, DateTime.parse((String)fieldsToUpdate.get("expirationDate")));
/*     */     }
/*     */     
/*     */ 
/*  79 */     result.put("expirationDate", resolvedExpiryDate);
/*  80 */     result.put("accountName", existingConfig.get("accountName"));
/*  81 */     result.put("accessKey", fieldsToUpdate.get("accessKey"));
/*  82 */     result.put("eumAccountName", fieldsToUpdate.get("eumAccountName"));
/*  83 */     result.put("licensingConfigurations", getMergedLicenceConfig((List)existingConfig.get("licensingConfigurations"), (List)fieldsToUpdate.get("licensingConfigurations")));
/*     */     
/*     */ 
/*  86 */     return result;
/*     */   }
/*     */   
/*     */   static DateTime determineAccountExpiryDate(DateTime oldExpiryDate, DateTime newExpiryDate) {
/*  90 */     if ((oldExpiryDate != null) && (
/*  91 */       (newExpiryDate == null) || (oldExpiryDate.isAfter(newExpiryDate)))) {
/*  92 */       return oldExpiryDate;
/*     */     }
/*     */     
/*  95 */     return newExpiryDate;
/*     */   }
/*     */   
/*     */   static List<Map<String, Object>> getMergedLicenceConfig(List<Map<String, Object>> existingLicenseConfig, List<Map<String, Object>> newLicenseConfig)
/*     */   {
/* 100 */     List<Map<String, Object>> licensesList = new ArrayList();
/*     */     
/* 102 */     if (existingLicenseConfig != null) {
/* 103 */       for (Map<String, Object> m : existingLicenseConfig) {
/* 104 */         String currentEventType = (String)m.get("eventType");
/* 105 */         if (!isEventTypeInNewList(currentEventType, newLicenseConfig)) {
/* 106 */           licensesList.add(m);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 112 */     if (newLicenseConfig != null) {
/* 113 */       for (Map<String, Object> configuration : newLicenseConfig) {
/* 114 */         licensesList.add(configuration);
/*     */       }
/*     */     }
/* 117 */     return licensesList;
/*     */   }
/*     */   
/*     */   static boolean isEventTypeInNewList(String oldEventType, List<Map<String, Object>> licenseConfig) {
/* 121 */     if (licenseConfig != null) {
/* 122 */       for (Map<String, Object> lc : licenseConfig) {
/* 123 */         String newEventType = (String)lc.get("eventType");
/* 124 */         if (newEventType.equals(oldEventType)) {
/* 125 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   public Object run()
/*     */   {
/* 134 */     if (!this.ctx.containsKey("_source")) {
/* 135 */       throw new IllegalStateException("AccountManagerUpsertScript does not have access to the _source field. Make sure it is enabled.");
/*     */     }
/*     */     
/*     */ 
/* 139 */     Map<String, Object> source = (Map)this.ctx.get("_source");
/* 140 */     log.trace("Updating existing account config data {} with updated {}", source, this.doc);
/*     */     
/* 142 */     Map<String, Object> result = updateAccountConfiguration(source, this.doc);
/* 143 */     for (Map.Entry<String, Object> entry : result.entrySet()) {
/* 144 */       source.put(entry.getKey(), entry.getValue());
/*     */     }
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   public void setNextVar(String name, Object value)
/*     */   {
/* 151 */     if (("ctx".equals(name)) && (value != null) && ((value instanceof Map))) {
/* 152 */       this.ctx = ((Map)value);
/*     */     } else {
/* 154 */       log.debug("Unknown request from ES for setting script var {} to {}", name, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/scripts/AccountConfigUpsertScript.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */