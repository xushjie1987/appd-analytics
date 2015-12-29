/*    */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ import java.util.List;
/*    */ import javax.annotation.concurrent.ThreadSafe;
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
/*    */ @ThreadSafe
/*    */ public class ClientReport
/*    */   implements ClientReportMBean
/*    */ {
/*    */   volatile List<ClientReportMBean.ClientReportClientDataMBean> clientReportClientData;
/*    */   
/* 23 */   public String toString() { return "ClientReport(clientReportClientData=" + getClientReportClientData() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $clientReportClientData = getClientReportClientData();result = result * 31 + ($clientReportClientData == null ? 0 : $clientReportClientData.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof ClientReport; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ClientReport)) return false; ClientReport other = (ClientReport)o; if (!other.canEqual(this)) return false; Object this$clientReportClientData = getClientReportClientData();Object other$clientReportClientData = other.getClientReportClientData();return this$clientReportClientData == null ? other$clientReportClientData == null : this$clientReportClientData.equals(other$clientReportClientData);
/*    */   }
/*    */   
/*    */ 
/* 27 */   public void setClientReportClientData(List<ClientReportMBean.ClientReportClientDataMBean> clientReportClientData) { this.clientReportClientData = clientReportClientData; } public List<ClientReportMBean.ClientReportClientDataMBean> getClientReportClientData() { return this.clientReportClientData; }
/*    */   
/* 29 */   public static class ClientReportClientData implements ClientReportMBean.ClientReportClientDataMBean { public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ClientReportClientData)) return false; ClientReportClientData other = (ClientReportClientData)o; if (!other.canEqual(this)) return false; Object this$clusterName = getClusterName();Object other$clusterName = other.getClusterName(); if (this$clusterName == null ? other$clusterName != null : !this$clusterName.equals(other$clusterName)) return false; if (getClusterSizeBytes() != other.getClusterSizeBytes()) return false; return getClusterStateSizeEstimateBytes() == other.getClusterStateSizeEstimateBytes(); } public boolean canEqual(Object other) { return other instanceof ClientReportClientData; } public int hashCode() { int PRIME = 31;int result = 1;Object $clusterName = getClusterName();result = result * 31 + ($clusterName == null ? 0 : $clusterName.hashCode());long $clusterSizeBytes = getClusterSizeBytes();result = result * 31 + (int)($clusterSizeBytes >>> 32 ^ $clusterSizeBytes);long $clusterStateSizeEstimateBytes = getClusterStateSizeEstimateBytes();result = result * 31 + (int)($clusterStateSizeEstimateBytes >>> 32 ^ $clusterStateSizeEstimateBytes);return result; } public String toString() { return "ClientReport.ClientReportClientData(clusterName=" + getClusterName() + ", clusterSizeBytes=" + getClusterSizeBytes() + ", clusterStateSizeEstimateBytes=" + getClusterStateSizeEstimateBytes() + ")"; }
/*    */     @ConstructorProperties({"clusterName", "clusterSizeBytes", "clusterStateSizeEstimateBytes"})
/* 31 */     public ClientReportClientData(String clusterName, long clusterSizeBytes, long clusterStateSizeEstimateBytes) { this.clusterName = clusterName;this.clusterSizeBytes = clusterSizeBytes;this.clusterStateSizeEstimateBytes = clusterStateSizeEstimateBytes; }
/*    */     
/* 33 */     public String getClusterName() { return this.clusterName; } public void setClusterName(String clusterName) { this.clusterName = clusterName; }
/* 34 */     public long getClusterSizeBytes() { return this.clusterSizeBytes; } public void setClusterSizeBytes(long clusterSizeBytes) { this.clusterSizeBytes = clusterSizeBytes; }
/* 35 */     public long getClusterStateSizeEstimateBytes() { return this.clusterStateSizeEstimateBytes; } public void setClusterStateSizeEstimateBytes(long clusterStateSizeEstimateBytes) { this.clusterStateSizeEstimateBytes = clusterStateSizeEstimateBytes; }
/*    */     
/*    */     volatile String clusterName;
/*    */     volatile long clusterSizeBytes;
/*    */     volatile long clusterStateSizeEstimateBytes;
/*    */     public ClientReportClientData() {}
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ClientReport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */