/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.multi;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ZookeeperConstants;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.inject.ImplementedBy;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.curator.utils.ZKPaths;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ImplementedBy(DefaultClusterRouter.class)
/*    */ public abstract interface ClusterRouter
/*    */ {
/* 49 */   public static final String ANALYTICS_ZK_PATH = ZKPaths.makePath(ZookeeperConstants.ZK_BASE_PATH, "routes");
/*    */   public static final String DYNAMICALLY_ROUTED_CLUSTERS = "clusters.dynamic";
/*    */   public static final String ADMIN_CLUSTER = "cluster.admin";
/*    */   
/*    */   public abstract boolean setAdminCluster(String paramString)
/*    */     throws Exception;
/*    */   
/*    */   public abstract boolean updateAdminCluster(String paramString)
/*    */     throws Exception;
/*    */   
/*    */   public abstract String findAdminCluster();
/*    */   
/*    */   public abstract List<String> findClusters(String paramString)
/*    */     throws Exception;
/*    */   
/*    */   public abstract List<String> getAllDynamicClusters();
/*    */   
/*    */   public abstract Map<String, String> listAllRouteProperties();
/*    */   
/*    */   public abstract List<String> createDynamicRoute(List<String> paramList1, List<String> paramList2)
/*    */     throws Exception;
/*    */   
/*    */   public abstract List<String> modifyDynamicRoute(List<String> paramList1, List<String> paramList2)
/*    */     throws Exception;
/*    */   
/*    */   public abstract void removeDynamicRoute(List<String> paramList)
/*    */     throws Exception;
/*    */   
/*    */   public abstract void bulkRemoveDynamicRoutes(Collection<String> paramCollection)
/*    */     throws Exception;
/*    */   
/*    */   public abstract ImmutableMap<String, ImmutableList<String>> getAllDynamicRoutes()
/*    */     throws Exception;
/*    */   
/*    */   public abstract boolean addDynamicCluster(String paramString)
/*    */     throws Exception;
/*    */   
/*    */   public abstract boolean removeDynamicCluster(String paramString)
/*    */     throws Exception;
/*    */   
/*    */   public abstract void seedRoutes(SeedClusterRoutes paramSeedClusterRoutes);
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/multi/ClusterRouter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */