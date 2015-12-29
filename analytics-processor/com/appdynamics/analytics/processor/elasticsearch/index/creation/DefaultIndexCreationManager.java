/*     */ package com.appdynamics.analytics.processor.elasticsearch.index.creation;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.EventIndexDefaults;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.MetaDataIndexConfiguration;
/*     */ import com.appdynamics.analytics.processor.event.ClusterLock;
/*     */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.lucene.util.BytesRef;
/*     */ import org.elasticsearch.ElasticsearchTimeoutException;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.indices.alias.Alias;
/*     */ import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.common.bytes.BytesReference;
/*     */ import org.elasticsearch.common.io.stream.BytesStreamOutput;
/*     */ import org.elasticsearch.common.settings.Settings;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.indices.IndexAlreadyExistsException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultIndexCreationManager
/*     */   implements IndexCreationManager
/*     */ {
/*  40 */   private static final Logger log = LoggerFactory.getLogger(DefaultIndexCreationManager.class);
/*     */   
/*     */   static final String ACTIVE_INDEX_LOCK_ID = "active_index_lock_%s_%s";
/*     */   
/*     */   public static final String GLOBAL = "global";
/*     */   
/*     */   private final TimeValue esCallTimeout;
/*     */   private final ClusterLock lock;
/*     */   private final EventIndexDefaults indexCreationDefaults;
/*     */   private final IndexNameResolver indexNameResolver;
/*     */   
/*     */   public DefaultIndexCreationManager(TimeValue esCallTimeout, ClusterLock lock, EventIndexDefaults configuration, IndexNameResolver indexNameResolver)
/*     */   {
/*  53 */     this.esCallTimeout = esCallTimeout;
/*  54 */     this.lock = lock;
/*  55 */     this.indexCreationDefaults = configuration;
/*  56 */     this.indexNameResolver = indexNameResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean createIndexLocking(Client client, String indexName, Map<String, Object> settings)
/*     */   {
/*  64 */     return createIndexOptionalLocking(client, indexName, settings, null, null, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean createIndexLocking(Client client, String indexName, MetaDataIndexConfiguration configuration)
/*     */   {
/*  72 */     List<String> aliases = Lists.newArrayList(new String[] { configuration.getAlias() });
/*  73 */     Map<String, Object> settings = configuration.getSettings();
/*  74 */     Map<String, Map<String, Object>> mappings = configuration.getMappings();
/*     */     
/*  76 */     return createIndexOptionalLocking(client, indexName, settings, aliases, mappings, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean createIndexLocking(Client client, String indexName, Map<String, Object> settings, List<String> aliases, Map<String, Map<String, Object>> mappings)
/*     */   {
/*  85 */     return createIndexOptionalLocking(client, indexName, settings, aliases, mappings, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean createIndexNotLocking(Client client, String indexName, Map<String, Object> settings, List<String> aliases, Map<String, Map<String, Object>> mappings)
/*     */   {
/*  94 */     return createIndexOptionalLocking(client, indexName, settings, aliases, mappings, false);
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean createIndexOptionalLocking(final Client client, final String indexName, Map<String, Object> settings, List<String> aliases, Map<String, Map<String, Object>> mappings, boolean shouldLock)
/*     */   {
/* 100 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(indexName), "Invalid index name");
/* 101 */     final CreateIndexRequestBuilder builder = new CreateIndexRequestBuilder(client.admin().indices());
/* 102 */     addAliases(builder, aliases);
/* 103 */     addSettings(builder, settings);
/* 104 */     addMappings(builder, mappings);
/*     */     
/* 106 */     if (ESUtils.indexExists(client, indexName)) {
/* 107 */       return false;
/*     */     }
/* 109 */     builder.setIndex(indexName);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 114 */     if (!shouldLock) {
/* 115 */       return createIndexInternal(client, indexName, builder).booleanValue();
/*     */     }
/*     */     
/*     */ 
/* 119 */     String accountName = "global";
/*     */     try {
/* 121 */       accountName = this.indexNameResolver.accountNameFromIndex(indexName);
/*     */     }
/*     */     catch (IllegalArgumentException ignore) {}
/*     */     
/* 125 */     ((Boolean)this.lock.acquireAndExecute(String.format("active_index_lock_%s_%s", new Object[] { client.settings().get("cluster.name"), accountName }), 30L, TimeUnit.SECONDS, new Callable()
/*     */     {
/*     */       public Boolean call() throws Exception
/*     */       {
/* 129 */         return DefaultIndexCreationManager.this.createIndexInternal(client, indexName, builder);
/*     */       }
/*     */     })).booleanValue();
/*     */   }
/*     */   
/*     */   private Boolean createIndexInternal(Client client, String indexName, CreateIndexRequestBuilder finalBuilder)
/*     */   {
/*     */     try {
/* 137 */       if (ESUtils.indexExists(client, indexName)) {
/* 138 */         return Boolean.valueOf(false);
/*     */       }
/* 140 */       CreateIndexResponse response = (CreateIndexResponse)finalBuilder.execute().actionGet(this.esCallTimeout);
/* 141 */       if (!response.isAcknowledged())
/*     */       {
/* 143 */         if (checkIndexExists(client, indexName)) {
/* 144 */           return Boolean.valueOf(true);
/*     */         }
/* 146 */         reportIndexCreationError(response, indexName);
/* 147 */         return Boolean.valueOf(false);
/*     */       }
/* 149 */       log.debug("Index [{}] created.", indexName);
/* 150 */       return Boolean.valueOf(true);
/*     */     }
/*     */     catch (IndexAlreadyExistsException e) {
/* 153 */       log.warn("Index [{}] was not created as it appears that it already exists.", indexName, e);
/*     */     } catch (ElasticsearchTimeoutException e) {
/* 155 */       if (checkIndexExists(client, indexName)) {
/* 156 */         return Boolean.valueOf(true);
/*     */       }
/*     */       
/* 159 */       log.error("ElasticSearch timed out while creating index [{}] - it may exist, but could not be confirmed.", indexName, e);
/*     */     }
/*     */     
/* 162 */     return Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean checkIndexExists(Client client, String indexName)
/*     */   {
/* 168 */     long sleptForMillis = 0L;
/* 169 */     while (sleptForMillis < this.esCallTimeout.getMillis()) {
/* 170 */       ConcurrencyHelper.sleep(1000L);
/* 171 */       if (ESUtils.indexExists(client, indexName)) {
/* 172 */         return true;
/*     */       }
/* 174 */       sleptForMillis += 1000L;
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addSettings(CreateIndexRequestBuilder builder, Map<String, Object> newSettings)
/*     */   {
/* 184 */     Map<String, Object> defaultSettings = convertIndexDefaultSettings(this.indexCreationDefaults);
/*     */     
/* 186 */     if (newSettings != null) {
/* 187 */       for (Map.Entry<String, Object> entry : newSettings.entrySet()) {
/* 188 */         defaultSettings.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/* 191 */     builder.setSettings(defaultSettings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void addAliases(CreateIndexRequestBuilder builder, List<String> aliases)
/*     */   {
/* 198 */     if (aliases != null) {
/* 199 */       for (String alias : aliases) {
/* 200 */         if (!Strings.isNullOrEmpty(alias)) {
/* 201 */           builder.addAlias(new Alias(alias));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addMappings(CreateIndexRequestBuilder builder, Map<String, Map<String, Object>> mappings)
/*     */   {
/* 212 */     if (mappings != null) {
/* 213 */       for (Map.Entry<String, Map<String, Object>> entry : mappings.entrySet()) {
/* 214 */         builder.addMapping((String)entry.getKey(), (Map)entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, Object> convertIndexDefaultSettings(EventIndexDefaults defaults)
/*     */   {
/* 226 */     IndexCreationSettings settings = new IndexCreationSettings().setNumShards(defaults.getShards().intValue()).setNumReplicas(defaults.getReplicas().intValue());
/*     */     
/*     */ 
/* 229 */     return settings.getSettings();
/*     */   }
/*     */   
/*     */   private void reportIndexCreationError(CreateIndexResponse createIndexResponse, String indexName) {
/* 233 */     BytesStreamOutput responseData = new BytesStreamOutput();
/*     */     try {
/* 235 */       createIndexResponse.writeTo(responseData);
/*     */     } catch (IOException e) {
/* 237 */       throw Throwables.propagate(e);
/*     */     }
/* 239 */     log.error("Failed to create index [{}]. Response=[{}]", indexName, responseData.bytes().copyBytesRef().utf8ToString());
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/creation/DefaultIndexCreationManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */