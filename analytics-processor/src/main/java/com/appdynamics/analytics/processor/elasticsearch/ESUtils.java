/*     */ package com.appdynamics.analytics.processor.elasticsearch;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.elasticsearch.action.ActionFuture;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
/*     */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
/*     */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
/*     */ import org.elasticsearch.action.admin.cluster.stats.ClusterStatsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
/*     */ import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
/*     */ import org.elasticsearch.action.admin.indices.refresh.RefreshRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndexStats;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.ClusterAdminClient;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.common.settings.Settings;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.index.shard.DocsStats;
/*     */ import org.elasticsearch.indices.IndexMissingException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public final class ESUtils
/*     */ {
/*  37 */   private static final Logger log = LoggerFactory.getLogger(ESUtils.class);
/*     */   
/*     */ 
/*     */ 
/*  41 */   public static TimeValue getTimeoutInMillis() { return timeoutInMillis; }
/*  42 */   public static void setTimeoutInMillis(TimeValue timeoutInMillis) { timeoutInMillis = timeoutInMillis; }
/*  43 */   private static TimeValue timeoutInMillis = new TimeValue(30000L);
/*     */   
/*     */   public static boolean refreshIndices(Client client, String... indices) {
/*     */     try {
/*  47 */       client.admin().indices().prepareRefresh(indices).execute().actionGet();
/*  48 */       return true;
/*     */     } catch (IndexMissingException e) {
/*  50 */       log.info("Could not refresh index: " + e.getMessage());
/*     */     }
/*  52 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean waitForClusterHealthiness(Client client, String... index)
/*     */   {
/*     */     try
/*     */     {
/*  61 */       ClusterHealthResponse response = (ClusterHealthResponse)client.admin().cluster().prepareHealth(index).setWaitForYellowStatus().setTimeout(timeoutInMillis).execute().actionGet();
/*     */       
/*  63 */       if (response.isTimedOut()) {
/*  64 */         log.error("Waiting for cluster healthiness timed out on cluster [{}] ! Cluster state is not in-sync.", getClusterName(client));
/*     */       }
/*     */       else {
/*  67 */         return true;
/*     */       }
/*     */     } catch (IndexMissingException e) {
/*  70 */       log.info("Could not wait for cluster healthiness on index: " + e.getMessage());
/*     */     }
/*  72 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Collection<Client> getClusterByIndexName(String indexName, ClientProvider clientProvider)
/*     */   {
/*  80 */     ArrayList<Client> clusters = new ArrayList();
/*  81 */     for (Client cluster : clientProvider.getAllInsertClients()) {
/*  82 */       if (isIndexInCluster(cluster, indexName)) {
/*  83 */         clusters.add(cluster);
/*     */       }
/*     */     }
/*  86 */     return clusters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isIndexInCluster(Client cluster, String indexName)
/*     */   {
/*  93 */     return ((IndicesExistsResponse)cluster.admin().indices().prepareExists(new String[] { indexName }).execute().actionGet()).isExists();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ClusterStatsResponse getClusterStats(Client cluster)
/*     */   {
/* 100 */     return (ClusterStatsResponse)cluster.admin().cluster().prepareClusterStats().execute().actionGet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ClusterHealthResponse getClusterHealth(Client cluster)
/*     */   {
/* 107 */     return (ClusterHealthResponse)cluster.admin().cluster().health(new ClusterHealthRequest(new String[0])).actionGet(timeoutInMillis.getMillis(), TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DocsStats getDocStats(String index, Client cluster)
/*     */   {
/* 119 */     IndicesStatsResponse indicesStatsResponse = (IndicesStatsResponse)((IndicesStatsRequestBuilder)cluster.admin().indices().prepareStats(new String[0]).setIndices(new String[] { index })).execute().actionGet();
/*     */     
/* 121 */     if (indicesStatsResponse.getIndices().size() != 1) {
/* 122 */       throw new IllegalArgumentException("Expected index [" + index + "] to result in one doc stats object, but" + " [" + indicesStatsResponse.getIndices().size() + "] were found");
/*     */     }
/*     */     
/* 125 */     return ((IndexStats)indicesStatsResponse.getIndices().values().iterator().next()).getPrimaries().getDocs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getClusterName(Client cluster)
/*     */   {
/* 132 */     return cluster.settings().get("cluster.name");
/*     */   }
/*     */   
/*     */   public static boolean indexExists(Client client, String indexName) {
/* 136 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(indexName), "Must provide a valid index name string");
/* 137 */     return ((IndicesExistsResponse)client.admin().indices().prepareExists(new String[] { indexName }).execute().actionGet()).isExists();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/ESUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */