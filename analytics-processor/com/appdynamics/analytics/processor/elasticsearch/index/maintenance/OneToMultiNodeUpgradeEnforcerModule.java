/*     */ package com.appdynamics.analytics.processor.elasticsearch.index.maintenance;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.EventIndexDefaults;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.IndexCreationModuleConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.IndexManagementModuleConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.MetaDataIndexConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.single.ElasticSearchSingleNodeConfiguration;
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.inject.Inject;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.validation.constraints.Min;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.elasticsearch.action.ActionFuture;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
/*     */ import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
/*     */ import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequestBuilder;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.common.collect.ImmutableOpenMap;
/*     */ import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
/*     */ import org.elasticsearch.common.settings.Settings;
/*     */ import org.elasticsearch.common.unit.TimeValue;
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
/*     */ public class OneToMultiNodeUpgradeEnforcerModule
/*     */   extends Module<Configuration>
/*     */ {
/*  50 */   private static final Logger log = LoggerFactory.getLogger(OneToMultiNodeUpgradeEnforcerModule.class);
/*     */   
/*     */ 
/*     */ 
/*     */   @Inject
/*     */   void start(ClientProvider clientProvider, ElasticSearchSingleNodeConfiguration nodeConfiguration, IndexManagementModuleConfiguration indexConfiguration, IndexCreationModuleConfiguration indexCreationConfiguration)
/*     */   {
/*  57 */     TimeUnitConfiguration timeUnitConfiguration = nodeConfiguration.getCallTimeout();
/*  58 */     TimeValue timeValue = new TimeValue(timeUnitConfiguration.getTime(), timeUnitConfiguration.getTimeUnit());
/*     */     
/*     */ 
/*  61 */     Thread thread = ConcurrencyHelper.newDaemonThreadFactory("elasticsearch-config-enforcer-%d").newThread(newTask(clientProvider, indexConfiguration, timeValue, indexCreationConfiguration));
/*     */     
/*     */ 
/*  64 */     thread.start();
/*  65 */     log.info("A background thread has been spawned to wake up after [{}] and force cluster settings", ((Configuration)getConfiguration()).getStartDelay());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   EnforcerTask newTask(ClientProvider clientProvider, IndexManagementModuleConfiguration indexConfiguration, TimeValue timeValue, IndexCreationModuleConfiguration indexCreationConfiguration)
/*     */   {
/*  72 */     return new EnforcerTask(clientProvider, timeValue, indexConfiguration, indexCreationConfiguration);
/*     */   }
/*     */   
/*     */   static ImmutableOpenMap<String, Settings> getIndexSettings(ClientProvider clientProvider, TimeValue callTimeOutValue)
/*     */   {
/*  77 */     log.debug("About to fetch index settings");
/*     */     
/*  79 */     return ((GetSettingsResponse)clientProvider.getAdminClient().admin().indices().getSettings(new GetSettingsRequest()).actionGet(callTimeOutValue)).getIndexToSettings();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Set<String> tryEnforceMetadataIndexSettings(ClientProvider clientProvider, TimeValue timeValue, ImmutableOpenMap<String, Settings> indexSettings, Map<String, MetaDataIndexConfiguration> metaDataConfigurations)
/*     */   {
/* 100 */     Object numMetadataReplicasInConfig = ((MetaDataIndexConfiguration)Iterables.get(metaDataConfigurations.values(), 0)).getSettings().get("number_of_replicas");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */     HashSet<String> metadataIndices = new HashSet();
/* 107 */     metadataIndices.addAll(metaDataConfigurations.keySet());
/*     */     
/*     */ 
/* 110 */     HashSet<String> metadataIndicesToEnforce = new HashSet();
/* 111 */     String key = "number_of_replicas";
/* 112 */     for (String index : metadataIndices)
/*     */     {
/* 114 */       Settings settings = (Settings)indexSettings.get(index);
/* 115 */       if (settings != null) {
/* 116 */         Object value = settings.get(key);
/* 117 */         log.debug("Index [{}] has [{} {}]", new Object[] { index, key, value });
/* 118 */         if ((value == null) || (!numMetadataReplicasInConfig.equals(value))) {
/* 119 */           metadataIndicesToEnforce.add(index);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 124 */     if (metadataIndicesToEnforce.size() > 0) {
/* 125 */       enforceIndexSettings(clientProvider, timeValue, metadataIndicesToEnforce, numMetadataReplicasInConfig);
/*     */     } else {
/* 127 */       log.info("No metadata indices were identified that needed settings enforcement");
/*     */     }
/*     */     
/*     */ 
/* 131 */     metadataIndices.add("appdynamics_version");
/* 132 */     return metadataIndices;
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
/*     */   void tryEnforceIndexSettings(ClientProvider clientProvider, TimeValue timeValue, ImmutableOpenMap<String, Settings> indexSettings, IndexCreationModuleConfiguration indexCreationModuleConfiguration, Set<String> exceptTheseIndices)
/*     */   {
/* 148 */     Integer numReplicas = indexCreationModuleConfiguration.getEventIndexDefaults().getReplicas();
/*     */     
/* 150 */     HashSet<String> indicesToEnforce = new HashSet();
/* 151 */     String key = "number_of_replicas";
/* 152 */     for (ObjectObjectCursor<String, Settings> pair : indexSettings)
/*     */     {
/* 154 */       if (!exceptTheseIndices.contains(pair.key)) {
/* 155 */         Settings settings = (Settings)indexSettings.get(pair.key);
/* 156 */         if (settings != null) {
/* 157 */           Object value = settings.get(key);
/* 158 */           Integer valueInt = value == null ? null : Integer.valueOf(Integer.parseInt(value.toString()));
/* 159 */           log.debug("Index [{}] has [{} {}]", new Object[] { pair.key, key, valueInt });
/* 160 */           if ((value == null) || (!valueInt.equals(numReplicas))) {
/* 161 */             indicesToEnforce.add(pair.key);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 167 */     if (indicesToEnforce.size() > 0) {
/* 168 */       enforceIndexSettings(clientProvider, timeValue, indicesToEnforce, numReplicas);
/*     */     } else {
/* 170 */       log.info("No indices were identified that needed settings enforcement");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void enforceIndexSettings(ClientProvider clientProvider, TimeValue callTimeOutValue, Set<String> indices, Object numReplicas)
/*     */   {
/* 182 */     if (numReplicas != null) {
/* 183 */       Map<String, Object> settings = ImmutableMap.of("number_of_replicas", numReplicas);
/*     */       
/*     */ 
/* 186 */       log.info("About to force settings \n{}\non indices\n{}\n", settings, indices);
/*     */       
/* 188 */       ((UpdateSettingsRequestBuilder)clientProvider.getAdminClient().admin().indices().prepareUpdateSettings((String[])indices.toArray(new String[indices.size()])).setSettings(settings).setTimeout(callTimeOutValue)).execute().actionGet(callTimeOutValue);
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/* 196 */       log.warn("No value has been set for the property [{}]. So, no action will be performed", "number_of_replicas");
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Configuration {
/*     */     public boolean equals(Object o) {
/* 202 */       if (o == this) return true; if (!(o instanceof Configuration)) return false; Configuration other = (Configuration)o; if (!other.canEqual(this)) return false; Object this$startDelay = getStartDelay();Object other$startDelay = other.getStartDelay(); if (this$startDelay == null ? other$startDelay != null : !this$startDelay.equals(other$startDelay)) return false; return getRetries() == other.getRetries(); } public boolean canEqual(Object other) { return other instanceof Configuration; } public int hashCode() { int PRIME = 31;int result = 1;Object $startDelay = getStartDelay();result = result * 31 + ($startDelay == null ? 0 : $startDelay.hashCode());result = result * 31 + getRetries();return result; } public String toString() { return "OneToMultiNodeUpgradeEnforcerModule.Configuration(startDelay=" + getStartDelay() + ", retries=" + getRetries() + ")"; }
/*     */     @NotNull
/* 204 */     TimeUnitConfiguration startDelay = new TimeUnitConfiguration(1L, TimeUnit.MINUTES);
/* 205 */     public TimeUnitConfiguration getStartDelay() { return this.startDelay; } public void setStartDelay(TimeUnitConfiguration startDelay) { this.startDelay = startDelay; }
/*     */     @Min(1L)
/* 207 */     int retries = 3;
/* 208 */     public int getRetries() { return this.retries; } public void setRetries(int retries) { this.retries = retries; }
/*     */   }
/*     */   
/*     */   class EnforcerTask
/*     */     implements Runnable
/*     */   {
/*     */     private final ClientProvider clientProvider;
/*     */     private final TimeValue timeValue;
/*     */     private final IndexManagementModuleConfiguration indexConfiguration;
/*     */     private final IndexCreationModuleConfiguration indexCreationConfiguration;
/*     */     
/*     */     EnforcerTask(ClientProvider clientProvider, TimeValue timeValue, IndexManagementModuleConfiguration indexConfiguration, IndexCreationModuleConfiguration indexCreationConfiguration)
/*     */     {
/* 221 */       this.clientProvider = clientProvider;
/* 222 */       this.timeValue = timeValue;
/* 223 */       this.indexConfiguration = indexConfiguration;
/* 224 */       this.indexCreationConfiguration = indexCreationConfiguration;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/*     */       try {
/* 230 */         long delayMillis = calculateDelayMillis();
/* 231 */         OneToMultiNodeUpgradeEnforcerModule.log.debug("Settings will be enforced after sleeping for [{}] millis", Long.valueOf(delayMillis));
/* 232 */         ConcurrencyHelper.sleep(delayMillis);
/* 233 */         OneToMultiNodeUpgradeEnforcerModule.log.debug("Woke up now and preparing to enforce settings");
/*     */         
/* 235 */         for (int i = 1; i <= ((OneToMultiNodeUpgradeEnforcerModule.Configuration)OneToMultiNodeUpgradeEnforcerModule.this.getConfiguration()).getRetries(); i++) {
/*     */           try {
/* 237 */             OneToMultiNodeUpgradeEnforcerModule.log.info("Attempt [{}]", Integer.valueOf(i));
/*     */             
/* 239 */             ImmutableOpenMap<String, Settings> indexSettings = OneToMultiNodeUpgradeEnforcerModule.getIndexSettings(this.clientProvider, this.timeValue);
/*     */             
/* 241 */             Set<String> metadataIndicesEnforced = OneToMultiNodeUpgradeEnforcerModule.this.tryEnforceMetadataIndexSettings(this.clientProvider, this.timeValue, indexSettings, this.indexConfiguration.getIndexConfigurations());
/*     */             
/*     */ 
/*     */ 
/* 245 */             OneToMultiNodeUpgradeEnforcerModule.this.tryEnforceIndexSettings(this.clientProvider, this.timeValue, indexSettings, this.indexCreationConfiguration, metadataIndicesEnforced);
/*     */             
/*     */ 
/* 248 */             onDone();
/*     */           }
/*     */           catch (Exception e) {
/* 251 */             int remaining = ((OneToMultiNodeUpgradeEnforcerModule.Configuration)OneToMultiNodeUpgradeEnforcerModule.this.getConfiguration()).getRetries() - i;
/* 252 */             if (remaining > 0) {
/* 253 */               OneToMultiNodeUpgradeEnforcerModule.log.warn("Error occurred while attempting to force cluster settings. Retry attempts remaining [" + remaining + "]", e);
/*     */             }
/*     */             else {
/* 256 */               throw e;
/*     */             }
/*     */           }
/*     */         }
/*     */       } catch (Throwable t) {
/* 261 */         onError(t);
/*     */       }
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     long calculateDelayMillis()
/*     */     {
/* 268 */       return ((OneToMultiNodeUpgradeEnforcerModule.Configuration)OneToMultiNodeUpgradeEnforcerModule.this.getConfiguration()).getStartDelay().toMilliseconds() + 1000 * new Random().nextInt(20);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     void onDone() {
/* 273 */       OneToMultiNodeUpgradeEnforcerModule.log.info("Done");
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     void onError(Throwable t) {
/* 278 */       OneToMultiNodeUpgradeEnforcerModule.log.error("Error occurred while attempting to force cluster settings", t);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/maintenance/OneToMultiNodeUpgradeEnforcerModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */