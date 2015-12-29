/*    */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElasticSearchStoreHealth
/*    */ {
/*    */   OverallHealth overallHealth;
/*    */   Map<String, Node> nodes;
/*    */   
/*    */   @ConstructorProperties({"overallHealth", "nodes"})
/*    */   public ElasticSearchStoreHealth(OverallHealth overallHealth, Map<String, Node> nodes)
/*    */   {
/* 22 */     this.overallHealth = overallHealth;this.nodes = nodes; }
/*    */   
/* 24 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ElasticSearchStoreHealth)) return false; ElasticSearchStoreHealth other = (ElasticSearchStoreHealth)o; if (!other.canEqual(this)) return false; Object this$overallHealth = getOverallHealth();Object other$overallHealth = other.getOverallHealth(); if (this$overallHealth == null ? other$overallHealth != null : !this$overallHealth.equals(other$overallHealth)) return false; Object this$nodes = getNodes();Object other$nodes = other.getNodes();return this$nodes == null ? other$nodes == null : this$nodes.equals(other$nodes); } public boolean canEqual(Object other) { return other instanceof ElasticSearchStoreHealth; } public int hashCode() { int PRIME = 31;int result = 1;Object $overallHealth = getOverallHealth();result = result * 31 + ($overallHealth == null ? 0 : $overallHealth.hashCode());Object $nodes = getNodes();result = result * 31 + ($nodes == null ? 0 : $nodes.hashCode());return result; } public String toString() { return "ElasticSearchStoreHealth(overallHealth=" + getOverallHealth() + ", nodes=" + getNodes() + ")"; }
/*    */   
/*    */ 
/* 27 */   public OverallHealth getOverallHealth() { return this.overallHealth; } public void setOverallHealth(OverallHealth overallHealth) { this.overallHealth = overallHealth; }
/*    */   
/*    */ 
/* 30 */   public Map<String, Node> getNodes() { return this.nodes; } public void setNodes(Map<String, Node> nodes) { this.nodes = nodes; }
/*    */   public static class Node { @ConstructorProperties({"nodeDetails", "nodeHealth"})
/* 32 */     public Node(ElasticSearchStoreHealth.NodeDetails nodeDetails, ElasticSearchStoreHealth.NodeHealth nodeHealth) { this.nodeDetails = nodeDetails;this.nodeHealth = nodeHealth; }
/*    */     ElasticSearchStoreHealth.NodeDetails nodeDetails;
/* 34 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Node)) return false; Node other = (Node)o; if (!other.canEqual(this)) return false; Object this$nodeDetails = getNodeDetails();Object other$nodeDetails = other.getNodeDetails(); if (this$nodeDetails == null ? other$nodeDetails != null : !this$nodeDetails.equals(other$nodeDetails)) return false; Object this$nodeHealth = getNodeHealth();Object other$nodeHealth = other.getNodeHealth();return this$nodeHealth == null ? other$nodeHealth == null : this$nodeHealth.equals(other$nodeHealth); } public boolean canEqual(Object other) { return other instanceof Node; } public int hashCode() { int PRIME = 31;int result = 1;Object $nodeDetails = getNodeDetails();result = result * 31 + ($nodeDetails == null ? 0 : $nodeDetails.hashCode());Object $nodeHealth = getNodeHealth();result = result * 31 + ($nodeHealth == null ? 0 : $nodeHealth.hashCode());return result; } public String toString() { return "ElasticSearchStoreHealth.Node(nodeDetails=" + getNodeDetails() + ", nodeHealth=" + getNodeHealth() + ")"; }
/*    */     ElasticSearchStoreHealth.NodeHealth nodeHealth;
/* 36 */     public ElasticSearchStoreHealth.NodeDetails getNodeDetails() { return this.nodeDetails; } public void setNodeDetails(ElasticSearchStoreHealth.NodeDetails nodeDetails) { this.nodeDetails = nodeDetails; }
/* 37 */     public ElasticSearchStoreHealth.NodeHealth getNodeHealth() { return this.nodeHealth; } public void setNodeHealth(ElasticSearchStoreHealth.NodeHealth nodeHealth) { this.nodeHealth = nodeHealth; }
/*    */     public Node() {} }
/*    */   public static class NodeDetails { String nodeName; @ConstructorProperties({"nodeName", "hostName", "hostAddress"})
/* 40 */     public NodeDetails(String nodeName, String hostName, String hostAddress) { this.nodeName = nodeName;this.hostName = hostName;this.hostAddress = hostAddress; }
/*    */     String hostName;
/* 42 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof NodeDetails)) return false; NodeDetails other = (NodeDetails)o; if (!other.canEqual(this)) return false; Object this$nodeName = getNodeName();Object other$nodeName = other.getNodeName(); if (this$nodeName == null ? other$nodeName != null : !this$nodeName.equals(other$nodeName)) return false; Object this$hostName = getHostName();Object other$hostName = other.getHostName(); if (this$hostName == null ? other$hostName != null : !this$hostName.equals(other$hostName)) return false; Object this$hostAddress = getHostAddress();Object other$hostAddress = other.getHostAddress();return this$hostAddress == null ? other$hostAddress == null : this$hostAddress.equals(other$hostAddress); } public boolean canEqual(Object other) { return other instanceof NodeDetails; } public int hashCode() { int PRIME = 31;int result = 1;Object $nodeName = getNodeName();result = result * 31 + ($nodeName == null ? 0 : $nodeName.hashCode());Object $hostName = getHostName();result = result * 31 + ($hostName == null ? 0 : $hostName.hashCode());Object $hostAddress = getHostAddress();result = result * 31 + ($hostAddress == null ? 0 : $hostAddress.hashCode());return result; } public String toString() { return "ElasticSearchStoreHealth.NodeDetails(nodeName=" + getNodeName() + ", hostName=" + getHostName() + ", hostAddress=" + getHostAddress() + ")"; }
/*    */     String hostAddress;
/* 44 */     public String getNodeName() { return this.nodeName; } public void setNodeName(String nodeName) { this.nodeName = nodeName; }
/* 45 */     public String getHostName() { return this.hostName; } public void setHostName(String hostName) { this.hostName = hostName; }
/* 46 */     public String getHostAddress() { return this.hostAddress; } public void setHostAddress(String hostAddress) { this.hostAddress = hostAddress; }
/*    */     public NodeDetails() {} }
/*    */   public static class NodeHealth { ElasticSearchStoreHealth.HistogramData diskUsageInPercentAvailable; @ConstructorProperties({"diskUsageInPercentAvailable", "oldGcTimes", "youngGcTimes", "heapUsageInPercentUsed"})
/* 49 */     public NodeHealth(ElasticSearchStoreHealth.HistogramData diskUsageInPercentAvailable, ElasticSearchStoreHealth.HistogramData oldGcTimes, ElasticSearchStoreHealth.HistogramData youngGcTimes, ElasticSearchStoreHealth.HistogramData heapUsageInPercentUsed) { this.diskUsageInPercentAvailable = diskUsageInPercentAvailable;this.oldGcTimes = oldGcTimes;this.youngGcTimes = youngGcTimes;this.heapUsageInPercentUsed = heapUsageInPercentUsed; }
/*    */     ElasticSearchStoreHealth.HistogramData oldGcTimes;
/* 51 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof NodeHealth)) return false; NodeHealth other = (NodeHealth)o; if (!other.canEqual(this)) return false; Object this$diskUsageInPercentAvailable = getDiskUsageInPercentAvailable();Object other$diskUsageInPercentAvailable = other.getDiskUsageInPercentAvailable(); if (this$diskUsageInPercentAvailable == null ? other$diskUsageInPercentAvailable != null : !this$diskUsageInPercentAvailable.equals(other$diskUsageInPercentAvailable)) return false; Object this$oldGcTimes = getOldGcTimes();Object other$oldGcTimes = other.getOldGcTimes(); if (this$oldGcTimes == null ? other$oldGcTimes != null : !this$oldGcTimes.equals(other$oldGcTimes)) return false; Object this$youngGcTimes = getYoungGcTimes();Object other$youngGcTimes = other.getYoungGcTimes(); if (this$youngGcTimes == null ? other$youngGcTimes != null : !this$youngGcTimes.equals(other$youngGcTimes)) return false; Object this$heapUsageInPercentUsed = getHeapUsageInPercentUsed();Object other$heapUsageInPercentUsed = other.getHeapUsageInPercentUsed();return this$heapUsageInPercentUsed == null ? other$heapUsageInPercentUsed == null : this$heapUsageInPercentUsed.equals(other$heapUsageInPercentUsed); } public boolean canEqual(Object other) { return other instanceof NodeHealth; } public int hashCode() { int PRIME = 31;int result = 1;Object $diskUsageInPercentAvailable = getDiskUsageInPercentAvailable();result = result * 31 + ($diskUsageInPercentAvailable == null ? 0 : $diskUsageInPercentAvailable.hashCode());Object $oldGcTimes = getOldGcTimes();result = result * 31 + ($oldGcTimes == null ? 0 : $oldGcTimes.hashCode());Object $youngGcTimes = getYoungGcTimes();result = result * 31 + ($youngGcTimes == null ? 0 : $youngGcTimes.hashCode());Object $heapUsageInPercentUsed = getHeapUsageInPercentUsed();result = result * 31 + ($heapUsageInPercentUsed == null ? 0 : $heapUsageInPercentUsed.hashCode());return result; } public String toString() { return "ElasticSearchStoreHealth.NodeHealth(diskUsageInPercentAvailable=" + getDiskUsageInPercentAvailable() + ", oldGcTimes=" + getOldGcTimes() + ", youngGcTimes=" + getYoungGcTimes() + ", heapUsageInPercentUsed=" + getHeapUsageInPercentUsed() + ")"; }
/*    */     
/*    */     ElasticSearchStoreHealth.HistogramData youngGcTimes;
/*    */     ElasticSearchStoreHealth.HistogramData heapUsageInPercentUsed;
/* 55 */     public ElasticSearchStoreHealth.HistogramData getDiskUsageInPercentAvailable() { return this.diskUsageInPercentAvailable; } public void setDiskUsageInPercentAvailable(ElasticSearchStoreHealth.HistogramData diskUsageInPercentAvailable) { this.diskUsageInPercentAvailable = diskUsageInPercentAvailable; }
/*    */     
/*    */ 
/* 58 */     public ElasticSearchStoreHealth.HistogramData getOldGcTimes() { return this.oldGcTimes; } public void setOldGcTimes(ElasticSearchStoreHealth.HistogramData oldGcTimes) { this.oldGcTimes = oldGcTimes; }
/*    */     
/*    */ 
/* 61 */     public ElasticSearchStoreHealth.HistogramData getYoungGcTimes() { return this.youngGcTimes; } public void setYoungGcTimes(ElasticSearchStoreHealth.HistogramData youngGcTimes) { this.youngGcTimes = youngGcTimes; }
/*    */     
/*    */ 
/* 64 */     public ElasticSearchStoreHealth.HistogramData getHeapUsageInPercentUsed() { return this.heapUsageInPercentUsed; } public void setHeapUsageInPercentUsed(ElasticSearchStoreHealth.HistogramData heapUsageInPercentUsed) { this.heapUsageInPercentUsed = heapUsageInPercentUsed; }
/*    */     
/*    */     public NodeHealth() {} }
/*    */   
/*    */   public static class HistogramData { List<Long> histValues;
/*    */     long min;
/*    */     long max; double mean;
/*    */     @ConstructorProperties({"histValues", "min", "max", "mean", "median", "percentile95"})
/* 72 */     public HistogramData(List<Long> histValues, long min, long max, double mean, double median, double percentile95) { this.histValues = histValues;this.min = min;this.max = max;this.mean = mean;this.median = median;this.percentile95 = percentile95; }
/*    */     double median;
/* 74 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof HistogramData)) return false; HistogramData other = (HistogramData)o; if (!other.canEqual(this)) return false; Object this$histValues = getHistValues();Object other$histValues = other.getHistValues(); if (this$histValues == null ? other$histValues != null : !this$histValues.equals(other$histValues)) return false; if (getMin() != other.getMin()) return false; if (getMax() != other.getMax()) return false; if (Double.compare(getMean(), other.getMean()) != 0) return false; if (Double.compare(getMedian(), other.getMedian()) != 0) return false; return Double.compare(getPercentile95(), other.getPercentile95()) == 0; } public boolean canEqual(Object other) { return other instanceof HistogramData; } public int hashCode() { int PRIME = 31;int result = 1;Object $histValues = getHistValues();result = result * 31 + ($histValues == null ? 0 : $histValues.hashCode());long $min = getMin();result = result * 31 + (int)($min >>> 32 ^ $min);long $max = getMax();result = result * 31 + (int)($max >>> 32 ^ $max);long $mean = Double.doubleToLongBits(getMean());result = result * 31 + (int)($mean >>> 32 ^ $mean);long $median = Double.doubleToLongBits(getMedian());result = result * 31 + (int)($median >>> 32 ^ $median);long $percentile95 = Double.doubleToLongBits(getPercentile95());result = result * 31 + (int)($percentile95 >>> 32 ^ $percentile95);return result; } public String toString() { return "ElasticSearchStoreHealth.HistogramData(histValues=" + getHistValues() + ", min=" + getMin() + ", max=" + getMax() + ", mean=" + getMean() + ", median=" + getMedian() + ", percentile95=" + getPercentile95() + ")"; }
/*    */     double percentile95;
/* 76 */     public List<Long> getHistValues() { return this.histValues; } public void setHistValues(List<Long> histValues) { this.histValues = histValues; }
/* 77 */     public long getMin() { return this.min; } public void setMin(long min) { this.min = min; }
/* 78 */     public long getMax() { return this.max; } public void setMax(long max) { this.max = max; }
/* 79 */     public double getMean() { return this.mean; } public void setMean(double mean) { this.mean = mean; }
/* 80 */     public double getMedian() { return this.median; } public void setMedian(double median) { this.median = median; }
/* 81 */     public double getPercentile95() { return this.percentile95; } public void setPercentile95(double percentile95) { this.percentile95 = percentile95; }
/*    */     
/*    */     public HistogramData() {} }
/*    */   
/*    */   public static class OverallHealth { ElasticSearchStoreHealth.Status status;
/*    */     List<String> descriptions;
/*    */     @ConstructorProperties({"status", "descriptions"})
/* 88 */     public OverallHealth(ElasticSearchStoreHealth.Status status, List<String> descriptions) { this.status = status;this.descriptions = descriptions; }
/*    */     
/* 90 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof OverallHealth)) return false; OverallHealth other = (OverallHealth)o; if (!other.canEqual(this)) return false; Object this$status = getStatus();Object other$status = other.getStatus(); if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false; Object this$descriptions = getDescriptions();Object other$descriptions = other.getDescriptions();return this$descriptions == null ? other$descriptions == null : this$descriptions.equals(other$descriptions); } public boolean canEqual(Object other) { return other instanceof OverallHealth; } public int hashCode() { int PRIME = 31;int result = 1;Object $status = getStatus();result = result * 31 + ($status == null ? 0 : $status.hashCode());Object $descriptions = getDescriptions();result = result * 31 + ($descriptions == null ? 0 : $descriptions.hashCode());return result; } public String toString() { return "ElasticSearchStoreHealth.OverallHealth(status=" + getStatus() + ", descriptions=" + getDescriptions() + ")"; }
/*    */     
/* 92 */     public ElasticSearchStoreHealth.Status getStatus() { return this.status; } public void setStatus(ElasticSearchStoreHealth.Status status) { this.status = status; }
/* 93 */     public List<String> getDescriptions() { return this.descriptions; } public void setDescriptions(List<String> descriptions) { this.descriptions = descriptions; }
/*    */     
/*    */     public OverallHealth() {} }
/*    */   public ElasticSearchStoreHealth() {}
/* 97 */   public static enum Status { HEALTHY, 
/* 98 */     APPROACHING_UNHEALTHY, 
/* 99 */     UNHEALTHY;
/*    */     
/*    */     private Status() {}
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ElasticSearchStoreHealth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */