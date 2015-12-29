/*     */ package com.appdynamics.analytics.processor.route;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.IndexManagementModuleConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ClusterRouter;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.ElasticSearchEventService;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.inject.Singleton;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.indices.get.GetIndexRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
/*     */ import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.search.SearchType;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.index.query.BoolFilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilders;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.index.query.RangeFilterBuilder;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.elasticsearch.search.SearchHitField;
/*     */ import org.elasticsearch.search.SearchHits;
/*     */ import org.elasticsearch.search.aggregations.AggregationBuilders;
/*     */ import org.elasticsearch.search.aggregations.Aggregations;
/*     */ import org.elasticsearch.search.aggregations.bucket.terms.Terms;
/*     */ import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
/*     */ import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
/*     */ import org.joda.time.DateTime;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ @Singleton
/*     */ public class RouteCleanupManager
/*     */ {
/*  56 */   private static final Logger log = LoggerFactory.getLogger(RouteCleanupManager.class);
/*     */   
/*     */   private final ElasticSearchEventService elasticSearchEventService;
/*     */   
/*     */   private final ClusterRouter clusterRouter;
/*     */   
/*     */   private final ClientProvider clientProvider;
/*     */   
/*     */   private final String[] metaDataIndices;
/*     */   
/*     */   private final long leewayTimeInMillis;
/*     */   
/*     */   private static final int numRetries = 5;
/*     */   private RouteCleanupParameters defaultRouteCleanupParameters;
/*     */   
/*     */   public RouteCleanupManager(ElasticSearchEventService elasticSearchEventService, ClusterRouter clusterRouter, ClientProvider clientProvider, IndexManagementModuleConfiguration indexManagementModuleConfiguration, long leewayTimeInMillis)
/*     */   {
/*  73 */     this.elasticSearchEventService = elasticSearchEventService;
/*  74 */     this.clusterRouter = clusterRouter;
/*  75 */     this.clientProvider = clientProvider;
/*  76 */     Set<String> adminMetaDataIndices = indexManagementModuleConfiguration.getIndexConfigurations().keySet();
/*  77 */     this.metaDataIndices = ((String[])adminMetaDataIndices.toArray(new String[adminMetaDataIndices.size()]));
/*  78 */     this.leewayTimeInMillis = leewayTimeInMillis;
/*     */     
/*  80 */     this.defaultRouteCleanupParameters = new RouteCleanupParameters();
/*  81 */     this.defaultRouteCleanupParameters.debugMode = Boolean.valueOf(true);
/*  82 */     this.defaultRouteCleanupParameters.batchDeleteSize = Integer.valueOf(100);
/*  83 */     this.defaultRouteCleanupParameters.numBatchesToProcess = Integer.valueOf(Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void cleanupRoutes(RouteCleanupParameters routeCleanupParameters)
/*     */   {
/*  90 */     routeCleanupParameters.mergeRouteCleanupParametersWithDefaults(this.defaultRouteCleanupParameters);
/*  91 */     log.info("Cleaning up routes, debug mode flag is [{}]...", routeCleanupParameters.getDebugMode());
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
/*     */   private AccountWithClusterList getActiveAccounts(String alias, String term)
/*     */   {
/* 127 */     log.info("Getting all accounts with data in [{}]...", alias);
/* 128 */     Set<String> accounts = new HashSet();
/* 129 */     Set<String> unavailableClusters = new HashSet();
/* 130 */     for (Client client : this.clientProvider.getAllInsertClients()) {
/* 131 */       String clusterName = ESUtils.getClusterName(client);
/*     */       try {
/*     */         String[] indices;
/* 134 */         if (ESUtils.indexExists(client, alias)) {
/* 135 */           indices = ((GetIndexResponse)((GetIndexRequestBuilder)client.admin().indices().prepareGetIndex().setIndices(new String[] { alias + "*" })).execute().actionGet()).getIndices();
/*     */         }
/*     */         else {
/* 138 */           log.info("No [{}] found on cluster [{}].", alias, clusterName);
/* 139 */           continue; }
/*     */         String[] indices;
/* 141 */         for (String index : indices) {
/* 142 */           log.debug("Getting all accounts with data in cluster [{}], index [{}]...", clusterName, index);
/* 143 */           long startTime = System.currentTimeMillis();
/* 144 */           Set<String> accountsInIndex = getActiveAccountsFromIndex(client, index, term, 0);
/* 145 */           log.info("Got all accounts with data in cluster [{}], index [{}]. This took [{}] ms.", new Object[] { clusterName, index, Long.valueOf(System.currentTimeMillis() - startTime) });
/*     */           
/* 147 */           accounts.addAll(accountsInIndex);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */ 
/* 155 */         unavailableClusters.add(clusterName);
/* 156 */         log.error("Could not connect to cluster [{}], not touching accounts that are routed there: ", clusterName, e);
/*     */       }
/*     */     }
/*     */     
/* 160 */     return new AccountWithClusterList(accounts, unavailableClusters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Set<String> trimEventFromAccount(Set<String> accountsWithTypeInIndex)
/*     */   {
/* 167 */     Set<String> accountsWithoutType = new HashSet();
/* 168 */     for (String accountWithType : accountsWithTypeInIndex) {
/* 169 */       accountsWithoutType.add(accountWithType.split("___")[0]);
/*     */     }
/* 171 */     return accountsWithoutType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Set<String> getActiveAccountsFromIndex(Client client, String index, String term, int currentRetryCount)
/*     */   {
/* 180 */     String aggName = "accounts";
/*     */     Aggregations aggs;
/* 182 */     try { aggs = ((SearchResponse)client.prepareSearch(new String[] { index }).addAggregation(((TermsBuilder)AggregationBuilders.terms(aggName).field(term)).size(0).executionHint("global_ordinals_low_cardinality")).setSearchType(SearchType.COUNT).setTimeout(new TimeValue(60L, TimeUnit.SECONDS)).execute().actionGet()).getAggregations();
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/*     */ 
/* 191 */       if (currentRetryCount < 5) {
/* 192 */         log.error("Hit timeout time limit on retry #[{}]: ", Integer.valueOf(currentRetryCount), e);
/* 193 */         return getActiveAccountsFromIndex(client, index, term, ++currentRetryCount);
/*     */       }
/* 195 */       log.error("Number of retries limit hit for getting active accounts in route cleanup. Exiting without cleaning anything.");
/*     */       
/* 197 */       throw Throwables.propagate(e);
/*     */     }
/*     */     
/*     */ 
/* 201 */     Terms terms = (Terms)aggs.get(aggName);
/* 202 */     Collection<Terms.Bucket> buckets = terms.getBuckets();
/* 203 */     Set<String> returnSet = new HashSet();
/* 204 */     for (Terms.Bucket bucket : buckets) {
/* 205 */       returnSet.add(bucket.getKey());
/*     */     }
/* 207 */     return returnSet;
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
/*     */   private List<Map<String, List<String>>> getAccountsToDeleteInBatches(ImmutableMap<String, ImmutableList<String>> allAccounts, Set<String> accountsWithData, Set<String> unreachableClusters, int batchDeleteSize, int numBatchesToProcess)
/*     */   {
/* 227 */     int numAccountsToDelete = allAccounts.size() - accountsWithData.size();
/* 228 */     int numBatches = (int)Math.ceil(numAccountsToDelete / batchDeleteSize);
/* 229 */     if (numBatchesToProcess < numBatches) {
/* 230 */       log.info("Found [{}] accounts to delete. This corresponds to [{}] batches, since we are using a batch size of [{}], but numBatchesToProcess is [{}] and so only this many will be deleted.", new Object[] { Integer.valueOf(numAccountsToDelete), Integer.valueOf(numBatches), Integer.valueOf(batchDeleteSize), Integer.valueOf(numBatchesToProcess) });
/*     */       
/*     */ 
/* 233 */       numBatches = numBatchesToProcess;
/*     */     } else {
/* 235 */       log.info("Deleting [{}] accounts. This corresponds to [{}] batches, since we are using a batch size of [{}].", new Object[] { Integer.valueOf(numAccountsToDelete), Integer.valueOf(numBatches), Integer.valueOf(batchDeleteSize) });
/*     */     }
/*     */     
/*     */ 
/* 239 */     List<Map<String, List<String>>> batches = new ArrayList(numBatches);
/* 240 */     for (int i = 0; i < numBatches; i++) {
/* 241 */       batches.add(i, new HashMap());
/*     */     }
/* 243 */     int currentIndex = 0;
/* 244 */     for (Map.Entry<String, ImmutableList<String>> entry : allAccounts.entrySet()) {
/* 245 */       if (((Map)batches.get(currentIndex)).size() == batchDeleteSize) {
/* 246 */         currentIndex++;
/*     */       }
/*     */       
/*     */ 
/* 250 */       if ((!accountsWithData.contains(entry.getKey())) && (Collections.disjoint((Collection)entry.getValue(), unreachableClusters)))
/*     */       {
/* 252 */         ((Map)batches.get(currentIndex)).put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/* 255 */     return batches;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void cleanupAllAccounts(List<Map<String, List<String>>> allAccountsToDelete, boolean debugMode, int batchDeleteSize)
/*     */   {
/* 263 */     for (Map<String, List<String>> accountBatch : allAccountsToDelete)
/*     */     {
/* 265 */       List<String> recentAccounts = determineRecentAccounts((String[])accountBatch.keySet().toArray(new String[accountBatch.keySet().size()]));
/*     */       
/*     */ 
/* 268 */       for (String recentAccount : recentAccounts) {
/* 269 */         accountBatch.remove(recentAccount);
/*     */       }
/* 271 */       cleanupAccountBatch(accountBatch, debugMode, batchDeleteSize);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<String> determineRecentAccounts(String[] accounts)
/*     */   {
/* 280 */     long now = TimeKeeper.currentUtcTime().getMillis();
/* 281 */     long cutoffTime = now - this.leewayTimeInMillis;
/*     */     
/*     */ 
/*     */ 
/* 285 */     SearchHit[] hits = ((SearchResponse)this.clientProvider.getAdminClient().prepareSearch(new String[] { "event_type_metadata" }).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.boolFilter().must(new FilterBuilder[] { FilterBuilders.rangeFilter("creationDate").from(cutoffTime).to(now), FilterBuilders.termsFilter("accountName", accounts) }))).addField("accountName").execute().actionGet()).getHits().getHits();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 290 */     List<String> recentAccounts = new ArrayList();
/* 291 */     for (SearchHit hit : hits) {
/* 292 */       recentAccounts.add((String)hit.field("accountName").getValue());
/*     */     }
/* 294 */     return recentAccounts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void cleanupAccountBatch(Map<String, List<String>> accountBatch, boolean debugMode, int batchDeleteSize)
/*     */   {
/* 304 */     if (accountBatch.size() == 0) {
/* 305 */       return;
/*     */     }
/* 307 */     Set<String> accountNames = accountBatch.keySet();
/* 308 */     log.info("Cleaning up account batch containing: [{}]", accountNames);
/* 309 */     if (debugMode) {
/* 310 */       log.info("Since route cleanup is running in debug mode, not deleting these accounts.");
/* 311 */       return;
/*     */     }
/*     */     try {
/* 314 */       this.clusterRouter.bulkRemoveDynamicRoutes(accountNames);
/*     */     } catch (Exception e) {
/* 316 */       log.error("Caught exception while bulk removing dynamic routes. Not continuing with this batch since no data has been modified yet. Exception was: ", e);
/*     */     }
/*     */     
/*     */ 
/* 320 */     bulkRemoveAccountMappings(accountBatch, batchDeleteSize);
/* 321 */     bulkRemoveAccountMetadata(accountNames, this.metaDataIndices);
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
/*     */   private void bulkRemoveAccountMappings(Map<String, List<String>> accountsToClustersMap, int batchDeleteSize) {}
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
/*     */   private Map<String, List<String>> groupAccountsByCluster(Map<String, List<String>> accountsToClustersMap, int batchDeleteSize)
/*     */   {
/* 351 */     Map<String, List<String>> clustersToAccountsMap = new HashMap();
/*     */     
/* 353 */     for (String clusterName : this.clusterRouter.getAllDynamicClusters()) {
/* 354 */       clustersToAccountsMap.put(clusterName, new ArrayList(batchDeleteSize));
/*     */     }
/* 356 */     for (Map.Entry<String, List<String>> entry : accountsToClustersMap.entrySet()) {
/* 357 */       accountName = (String)entry.getKey();
/* 358 */       List<String> clusters = (List)entry.getValue();
/* 359 */       for (String cluster : clusters)
/* 360 */         ((List)clustersToAccountsMap.get(cluster)).add(accountName);
/*     */     }
/*     */     String accountName;
/* 363 */     return clustersToAccountsMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeAccountMappings(Client client, List<String> accountNames)
/*     */   {
/* 371 */     SearchResponse response = (SearchResponse)this.clientProvider.getAdminClient().prepareSearch(new String[] { "event_type_metadata" }).setQuery(QueryBuilders.filteredQuery(QueryBuilders.termsQuery("accountName", accountNames), FilterBuilders.termFilter("dynamic", Boolean.valueOf(true)))).addFields(new String[] { "accountName", "eventType" }).setSize(Integer.MAX_VALUE).execute().actionGet();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 378 */     List<String> mappings = new ArrayList();
/* 379 */     for (SearchHit hit : response.getHits().getHits()) {
/* 380 */       mappings.add(hit.field("accountName").getValue() + "___" + hit.field("eventType").getValue());
/*     */     }
/*     */     
/* 383 */     if (mappings.size() == 0) {
/* 384 */       log.info("No mappings to remove for this batch, skipping this set.");
/* 385 */       return;
/*     */     }
/* 387 */     log.info("Removing the following mapping types: [{}]", mappings);
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
/*     */   private void bulkRemoveAccountMetadata(Collection<String> accountNames, String[] metaDataIndices)
/*     */   {
/* 402 */     log.info("Removing accounts from the following metadata indices: [{}]", Arrays.toString(metaDataIndices));
/*     */     try {
/* 404 */       this.clientProvider.getAdminClient().prepareDeleteByQuery(metaDataIndices).setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), FilterBuilders.termsFilter("accountName", accountNames))).execute().actionGet();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 408 */       log.error("Caught exception while removing metadata for batch containing accounts [{}]: ", accountNames, e);
/*     */       
/* 410 */       return;
/*     */     }
/* 412 */     log.info("Successfully removed accounts from metadata indices. Invalidating event type caches in ElasticSearchEventService now.");
/*     */   }
/*     */   
/*     */ 
/*     */   private static class AccountWithClusterList
/*     */   {
/*     */     Set<String> accounts;
/*     */     Set<String> clusters;
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 423 */       if (o == this) return true; if (!(o instanceof AccountWithClusterList)) return false; AccountWithClusterList other = (AccountWithClusterList)o; if (!other.canEqual(this)) return false; Object this$accounts = getAccounts();Object other$accounts = other.getAccounts(); if (this$accounts == null ? other$accounts != null : !this$accounts.equals(other$accounts)) return false; Object this$clusters = getClusters();Object other$clusters = other.getClusters();return this$clusters == null ? other$clusters == null : this$clusters.equals(other$clusters); } public boolean canEqual(Object other) { return other instanceof AccountWithClusterList; } public int hashCode() { int PRIME = 31;int result = 1;Object $accounts = getAccounts();result = result * 31 + ($accounts == null ? 0 : $accounts.hashCode());Object $clusters = getClusters();result = result * 31 + ($clusters == null ? 0 : $clusters.hashCode());return result; } public String toString() { return "RouteCleanupManager.AccountWithClusterList(accounts=" + getAccounts() + ", clusters=" + getClusters() + ")"; } @ConstructorProperties({"accounts", "clusters"})
/* 424 */     public AccountWithClusterList(Set<String> accounts, Set<String> clusters) { this.accounts = accounts;this.clusters = clusters; }
/*     */     
/* 426 */     public Set<String> getAccounts() { return this.accounts; } public void setAccounts(Set<String> accounts) { this.accounts = accounts; }
/* 427 */     public Set<String> getClusters() { return this.clusters; } public void setClusters(Set<String> clusters) { this.clusters = clusters; }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/route/RouteCleanupManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */