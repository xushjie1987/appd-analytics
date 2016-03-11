/*    */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*    */ 
/*    */ import javax.annotation.concurrent.ThreadSafe;
/*    */ 
/*    */ 
/*    */ @ThreadSafe
/*    */ public final class StoreReport
/*    */   implements StoreReportMBean
/*    */ {
/*    */   volatile long clusterSizeBytes;
/*    */   volatile int numberOfIndices;
/*    */   volatile int numberOfShards;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 16 */     if (o == this) return true; if (!(o instanceof StoreReport)) return false; StoreReport other = (StoreReport)o; if (getClusterSizeBytes() != other.getClusterSizeBytes()) return false; if (getNumberOfIndices() != other.getNumberOfIndices()) return false; return getNumberOfShards() == other.getNumberOfShards(); } public int hashCode() { int PRIME = 31;int result = 1;long $clusterSizeBytes = getClusterSizeBytes();result = result * 31 + (int)($clusterSizeBytes >>> 32 ^ $clusterSizeBytes);result = result * 31 + getNumberOfIndices();result = result * 31 + getNumberOfShards();return result; } public String toString() { return "StoreReport(clusterSizeBytes=" + getClusterSizeBytes() + ", numberOfIndices=" + getNumberOfIndices() + ", numberOfShards=" + getNumberOfShards() + ")"; }
/*    */   
/*    */ 
/* 19 */   public long getClusterSizeBytes() { return this.clusterSizeBytes; } public void setClusterSizeBytes(long clusterSizeBytes) { this.clusterSizeBytes = clusterSizeBytes; }
/* 20 */   public int getNumberOfIndices() { return this.numberOfIndices; } public void setNumberOfIndices(int numberOfIndices) { this.numberOfIndices = numberOfIndices; }
/* 21 */   public int getNumberOfShards() { return this.numberOfShards; } public void setNumberOfShards(int numberOfShards) { this.numberOfShards = numberOfShards; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/StoreReport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */