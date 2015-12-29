/*     */ package com.appdynamics.analytics.processor.account;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountLicensingConfiguration;
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountManagerConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.exception.ElasticSearchExceptionUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.scripts.AccountConfigUpsertScript;
/*     */ import com.appdynamics.common.util.exception.Exceptions;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.datatype.joda.JodaModule;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.collect.ArrayListMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.inject.Inject;
/*     */ import java.io.IOException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.elasticsearch.ElasticsearchException;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
/*     */ import org.elasticsearch.action.bulk.BulkRequestBuilder;
/*     */ import org.elasticsearch.action.bulk.BulkResponse;
/*     */ import org.elasticsearch.action.get.GetRequestBuilder;
/*     */ import org.elasticsearch.action.get.GetResponse;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.update.UpdateRequest;
/*     */ import org.elasticsearch.action.update.UpdateRequestBuilder;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.index.query.BaseQueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.elasticsearch.search.SearchHits;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ElasticsearchAccountManager
/*     */   implements AccountManager
/*     */ {
/*  53 */   private static final Logger log = LoggerFactory.getLogger(ElasticsearchAccountManager.class);
/*     */   
/*     */ 
/*  56 */   private static final ObjectMapper MAPPER = new ObjectMapper();
/*     */   private static final int NUM_OF_RETRIES = 10;
/*     */   private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
/*  59 */   private static final Joiner COMMA_JOINER = Joiner.on(", ");
/*     */   final ClientProvider clientProvider;
/*     */   
/*  62 */   static { MAPPER.registerModule(new JodaModule());
/*  63 */     MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   List<AccountConfiguration> systemAccounts;
/*     */   
/*     */   @Inject
/*     */   public ElasticsearchAccountManager(ClientProvider clientProvider, AccountManagerConfiguration authConfig)
/*     */   {
/*  73 */     this.clientProvider = clientProvider;
/*  74 */     this.systemAccounts = authConfig.getSystemAccounts();
/*  75 */     this.systemAccountsAsMap = ArrayListMultimap.create();
/*     */     
/*  77 */     if (this.systemAccounts != null) {
/*  78 */       for (AccountConfiguration keyConfig : findSystemAccountConfigurations()) {
/*  79 */         this.systemAccountsAsMap.put(keyConfig.getAccessKey(), keyConfig);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void upsertAccountConfigurations(List<AccountConfiguration> newConfigs)
/*     */   {
/*  86 */     List<String> accountNames = new ArrayList();
/*  87 */     if (newConfigs == null) {
/*  88 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  92 */       Client client = this.clientProvider.getAdminClient();
/*  93 */       for (AccountConfiguration newConfig : newConfigs) {
/*  94 */         accountNames.add(newConfig.getAccountName());
/*     */       }
/*  96 */       List<AccountConfiguration> existingConfigs = findAccountConfigurations(accountNames);
/*     */       
/*  98 */       Set<AccountConfiguration> existingConfigsSet = new HashSet(existingConfigs);
/*  99 */       BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
/* 100 */       List<UpdateRequest> requests = new ArrayList();
/*     */       
/* 102 */       for (AccountConfiguration newConfig : newConfigs) {
/* 103 */         if (!existingConfigsSet.contains(newConfig)) {
/* 104 */           UpdateRequest request = (UpdateRequest)((UpdateRequestBuilder)client.prepareUpdate().setIndex("appdynamics_accounts")).setType("appdynamics_accounts").setScript(AccountConfigUpsertScript.class.getSimpleName(), AccountConfigUpsertScript.SCRIPT_TYPE).setId(newConfig.getAccountName()).setScriptLang("native").addScriptParam("doc", MAPPER.convertValue(newConfig, Map.class)).setUpsert(MAPPER.writeValueAsString(newConfig)).setRetryOnConflict(10).request();
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
/* 115 */           requests.add(request);
/*     */         }
/*     */       }
/*     */       
/* 119 */       log.debug("Total [{}] account configs are passed for upsert and [{}] account configs are new/changed.", Integer.valueOf(newConfigs.size()), Integer.valueOf(requests.size()));
/*     */       
/* 121 */       if (requests.size() > 0) {
/* 122 */         for (UpdateRequest request : requests) {
/* 123 */           bulkRequestBuilder.add(request);
/*     */         }
/* 125 */         BulkResponse bulkResponse = (BulkResponse)bulkRequestBuilder.execute().actionGet();
/* 126 */         ESUtils.refreshIndices(client, new String[] { "appdynamics_accounts" });
/* 127 */         if (bulkResponse.hasFailures()) {
/* 128 */           log.error("Failed in bulk upserting account config requests: " + bulkResponse.buildFailureMessage());
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (JsonProcessingException|RuntimeException e) {
/* 133 */       log.error("Error occurred while upserting account configuration for accounts [{}]", COMMA_JOINER.join(accountNames), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public List<AccountConfiguration> findAccountConfigurations()
/*     */   {
/* 140 */     List<AccountConfiguration> configs = new ArrayList();
/* 141 */     configs.addAll(findAccountConfigurationsInElasticsearch(null));
/* 142 */     return configs;
/*     */   }
/*     */   
/*     */   public List<AccountConfiguration> findAccountConfigurations(List<String> accountNames)
/*     */   {
/* 147 */     return findAccountConfigurationsInElasticsearch(accountNames);
/*     */   }
/*     */   
/*     */   List<AccountConfiguration> findAccountConfigurationsInElasticsearch(List<String> accountNames) {
/* 151 */     List<AccountConfiguration> configs = new ArrayList();
/*     */     try {
/* 153 */       Client client = this.clientProvider.getAdminClient();
/* 154 */       if (((IndicesExistsResponse)client.admin().indices().prepareExists(new String[] { "appdynamics_accounts" }).execute().actionGet()).isExists()) { BaseQueryBuilder queryBuilder;
/*     */         BaseQueryBuilder queryBuilder;
/* 156 */         if (accountNames != null) {
/* 157 */           queryBuilder = QueryBuilders.termsQuery("accountName", accountNames);
/*     */         } else {
/* 159 */           queryBuilder = QueryBuilders.matchAllQuery();
/*     */         }
/*     */         
/* 162 */         List<SearchHit> hits = getAllQueryMatches(client, "appdynamics_accounts", queryBuilder);
/* 163 */         for (SearchHit hit : hits) {
/* 164 */           AccountConfiguration config = (AccountConfiguration)MAPPER.readValue(hit.getSourceAsString(), AccountConfiguration.class);
/* 165 */           configs.add(config);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 169 */       String accountNamesJoined = "";
/* 170 */       if (accountNames != null) {
/* 171 */         accountNamesJoined = COMMA_JOINER.join(accountNames);
/*     */       }
/* 173 */       throw propagateAsException(e, "Failed to find accounts [{}]", new Object[] { accountNamesJoined });
/*     */     }
/*     */     
/* 176 */     return configs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   Multimap<String, AccountConfiguration> systemAccountsAsMap;
/*     */   
/*     */ 
/*     */   public static final int SCROLL_SIZE = 1000;
/*     */   
/*     */   private List<SearchHit> getAllQueryMatches(Client client, String indexName, BaseQueryBuilder queryBuilder)
/*     */   {
/* 188 */     List<SearchHit> results = new ArrayList();
/* 189 */     SearchResponse response = null;
/* 190 */     int i = 0;
/* 191 */     while ((response == null) || (response.getHits().hits().length != 0)) {
/* 192 */       response = (SearchResponse)client.prepareSearch(new String[] { indexName }).setQuery(queryBuilder).setSize(1000).setFrom(i * 1000).execute().actionGet();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */       for (SearchHit hit : response.getHits().getHits()) {
/* 199 */         results.add(hit);
/*     */       }
/* 201 */       i++;
/*     */     }
/* 203 */     return results;
/*     */   }
/*     */   
/*     */   public Optional<AccountConfiguration> findAccountConfiguration(String accountName)
/*     */   {
/*     */     try {
/* 209 */       accountName = AccountConfiguration.normalizeAccountName(accountName);
/* 210 */       return findAccountInElasticsearch(accountName);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 214 */       throw propagateAsException(e, "Failed to find account configuration [{}]", new Object[] { accountName });
/*     */     }
/*     */   }
/*     */   
/*     */   Optional<AccountConfiguration> findAccountInElasticsearch(String accountName) throws IOException {
/* 219 */     Client client = this.clientProvider.getAdminClient();
/* 220 */     if (!((IndicesExistsResponse)client.admin().indices().prepareExists(new String[] { "appdynamics_accounts" }).execute().actionGet()).isExists()) {
/* 221 */       return Optional.absent();
/*     */     }
/* 223 */     GetResponse response = (GetResponse)client.prepareGet("appdynamics_accounts", "appdynamics_accounts", accountName).execute().actionGet();
/*     */     
/*     */ 
/*     */ 
/* 227 */     if ((response == null) || (response.getSourceAsString() == null)) {
/* 228 */       return Optional.absent();
/*     */     }
/* 230 */     return Optional.of(MAPPER.readValue(response.getSourceAsString(), AccountConfiguration.class));
/*     */   }
/*     */   
/*     */   public List<AccountConfiguration> findSystemAccountConfigurations() {
/* 234 */     return this.systemAccounts;
/*     */   }
/*     */   
/*     */   public Multimap<String, AccountConfiguration> findSystemAccountConfigurationsAsMap()
/*     */   {
/* 239 */     return this.systemAccountsAsMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public Optional<AccountLicensingConfiguration> findAccountLicensingConfiguration(String accountName, String eventType)
/*     */   {
/* 245 */     Optional<AccountConfiguration> accountConfig = findAccountConfiguration(accountName);
/* 246 */     if (!accountConfig.isPresent()) {
/* 247 */       return Optional.absent();
/*     */     }
/*     */     
/* 250 */     for (AccountLicensingConfiguration licenseConfig : ((AccountConfiguration)accountConfig.get()).getLicensingConfigurations()) {
/* 251 */       if (licenseConfig.getEventType() != null)
/*     */       {
/*     */ 
/*     */ 
/* 255 */         String licenseEventType = licenseConfig.getEventType().toLowerCase();
/* 256 */         if (eventType.equals(licenseEventType)) {
/* 257 */           return Optional.of(licenseConfig);
/*     */         }
/*     */         
/* 260 */         Pattern regexPattern = Pattern.compile(licenseEventType);
/* 261 */         Matcher regexMatcher = regexPattern.matcher(eventType);
/* 262 */         if (regexMatcher.matches()) {
/* 263 */           return Optional.of(licenseConfig);
/*     */         }
/*     */       }
/*     */     }
/* 267 */     return Optional.absent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   RuntimeException propagateAsException(Exception e, String format, Object... args)
/*     */   {
/* 277 */     if ((e instanceof ElasticsearchException)) {
/* 278 */       return ElasticSearchExceptionUtils.propagateAsEventException((ElasticsearchException)e, format, args);
/*     */     }
/* 280 */     return Exceptions.addSuppressedMsgAndPropagate(e, format, args);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/account/ElasticsearchAccountManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */