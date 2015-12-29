/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.multi;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SeedClusterRoutes
/*    */ {
/*    */   private String adminCluster;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private String dynamicClusters;
/*    */   
/*    */ 
/*    */ 
/* 18 */   public String toString() { return "SeedClusterRoutes(adminCluster=" + getAdminCluster() + ", dynamicClusters=" + getDynamicClusters() + ")"; }
/*    */   
/* 20 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof SeedClusterRoutes)) return false; SeedClusterRoutes other = (SeedClusterRoutes)o; if (!other.canEqual(this)) return false; Object this$adminCluster = getAdminCluster();Object other$adminCluster = other.getAdminCluster(); if (this$adminCluster == null ? other$adminCluster != null : !this$adminCluster.equals(other$adminCluster)) return false; Object this$dynamicClusters = getDynamicClusters();Object other$dynamicClusters = other.getDynamicClusters();return this$dynamicClusters == null ? other$dynamicClusters == null : this$dynamicClusters.equals(other$dynamicClusters); } public boolean canEqual(Object other) { return other instanceof SeedClusterRoutes; } public int hashCode() { int PRIME = 31;int result = 1;Object $adminCluster = getAdminCluster();result = result * 31 + ($adminCluster == null ? 0 : $adminCluster.hashCode());Object $dynamicClusters = getDynamicClusters();result = result * 31 + ($dynamicClusters == null ? 0 : $dynamicClusters.hashCode());return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 27 */   public String getAdminCluster() { return this.adminCluster; } public void setAdminCluster(String adminCluster) { this.adminCluster = adminCluster; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 33 */   public String getDynamicClusters() { return this.dynamicClusters; } public void setDynamicClusters(String dynamicClusters) { this.dynamicClusters = dynamicClusters; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/multi/SeedClusterRoutes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */