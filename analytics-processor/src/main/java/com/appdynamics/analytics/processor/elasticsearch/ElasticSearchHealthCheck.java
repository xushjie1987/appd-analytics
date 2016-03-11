/*    */ package com.appdynamics.analytics.processor.elasticsearch;
/*    */ 
/*    */ import com.appdynamics.common.util.health.SimpleHealthCheck;
/*    */ import com.codahale.metrics.health.HealthCheck.Result;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
/*    */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
/*    */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
/*    */ import org.elasticsearch.client.Client;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ElasticSearchHealthCheck
/*    */   extends SimpleHealthCheck
/*    */ {
/*    */   static final int TIMEOUT_SECONDS = 5;
/*    */   
/*    */   protected ElasticSearchHealthCheck(String name)
/*    */   {
/* 25 */     super(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract List<Client> getClients();
/*    */   
/*    */ 
/*    */ 
/*    */   public HealthCheck.Result check()
/*    */   {
/* 37 */     Map<String, String> clientStatusMessages = new TreeMap();
/* 38 */     boolean isHealthy = true;
/*    */     
/* 40 */     for (Client client : getClients()) {
/* 41 */       ClusterHealthResponse response = ESUtils.getClusterHealth(client);
/*    */       
/* 43 */       ClusterHealthStatus status = response.getStatus();
/* 44 */       String message = formatClusterHealthResponse(response);
/* 45 */       clientStatusMessages.put(response.getClusterName(), message);
/*    */       
/* 47 */       if ((status != ClusterHealthStatus.GREEN) && (status != ClusterHealthStatus.YELLOW)) {
/* 48 */         isHealthy = false;
/*    */       }
/*    */     }
/*    */     
/* 52 */     StringBuilder messageBuilder = new StringBuilder();
/* 53 */     boolean isFirst = true;
/* 54 */     for (String message : clientStatusMessages.values()) {
/* 55 */       if (isFirst) {
/* 56 */         isFirst = false;
/*    */       } else {
/* 58 */         messageBuilder.append(", ");
/*    */       }
/* 60 */       messageBuilder.append(message);
/*    */     }
/*    */     
/* 63 */     return isHealthy ? HealthCheck.Result.healthy(messageBuilder.toString()) : HealthCheck.Result.unhealthy(messageBuilder.toString());
/*    */   }
/*    */   
/*    */   public static String formatClusterHealthResponse(ClusterHealthResponse response) {
/* 67 */     return formatClusterHealthResponse(response.getClusterName(), response.getStatus(), response.getNumberOfDataNodes(), response.getNumberOfNodes(), response.getActiveShards(), response.getRelocatingShards(), response.getInitializingShards(), response.getUnassignedShards(), response.isTimedOut());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String formatClusterHealthResponse(String clusterName, ClusterHealthStatus status, int numberOfDataNodes, int numberOfNodes, int activeShards, int relocatingShards, int initializingShards, int unassignedShards, boolean timedOut)
/*    */   {
/* 77 */     return String.format("Current [%s] cluster state: [%s], data nodes: [%d], nodes: [%d], active shards: [%d], relocating shards: [%d], initializing shards: [%d], unassigned shards: [%d], timed out: [%s]", new Object[] { clusterName, status, Integer.valueOf(numberOfDataNodes), Integer.valueOf(numberOfNodes), Integer.valueOf(activeShards), Integer.valueOf(relocatingShards), Integer.valueOf(initializingShards), Integer.valueOf(unassignedShards), Boolean.valueOf(timedOut) });
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/ElasticSearchHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */