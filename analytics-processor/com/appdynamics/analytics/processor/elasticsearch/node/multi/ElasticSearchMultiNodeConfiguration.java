/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.multi;
/*    */ 
/*    */ import com.appdynamics.common.io.file.FilePollerConfiguration;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
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
/*    */ public class ElasticSearchMultiNodeConfiguration
/*    */   extends FilePollerConfiguration
/*    */ {
/* 30 */   public String toString() { return "ElasticSearchMultiNodeConfiguration(callTimeout=" + getCallTimeout() + ", seedClusterRoutes=" + getSeedClusterRoutes() + ", commonNodeSettings=" + getCommonNodeSettings() + ", commonTribeNodeSettings=" + getCommonTribeNodeSettings() + ", numAccountsRoutingWeight=" + getNumAccountsRoutingWeight() + ", clusterSizeRoutingWeight=" + getClusterSizeRoutingWeight() + ", customersPerClusterErrorThreshold=" + getCustomersPerClusterErrorThreshold() + ", customersPerClusterWarnThreshold=" + getCustomersPerClusterWarnThreshold() + ", clusterSizeSeedThresholdBytes=" + getClusterSizeSeedThresholdBytes() + ")"; }
/*    */   
/* 32 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ElasticSearchMultiNodeConfiguration)) return false; ElasticSearchMultiNodeConfiguration other = (ElasticSearchMultiNodeConfiguration)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$callTimeout = getCallTimeout();Object other$callTimeout = other.getCallTimeout(); if (this$callTimeout == null ? other$callTimeout != null : !this$callTimeout.equals(other$callTimeout)) return false; Object this$seedClusterRoutes = getSeedClusterRoutes();Object other$seedClusterRoutes = other.getSeedClusterRoutes(); if (this$seedClusterRoutes == null ? other$seedClusterRoutes != null : !this$seedClusterRoutes.equals(other$seedClusterRoutes)) return false; Object this$commonNodeSettings = getCommonNodeSettings();Object other$commonNodeSettings = other.getCommonNodeSettings(); if (this$commonNodeSettings == null ? other$commonNodeSettings != null : !this$commonNodeSettings.equals(other$commonNodeSettings)) return false; Object this$commonTribeNodeSettings = getCommonTribeNodeSettings();Object other$commonTribeNodeSettings = other.getCommonTribeNodeSettings(); if (this$commonTribeNodeSettings == null ? other$commonTribeNodeSettings != null : !this$commonTribeNodeSettings.equals(other$commonTribeNodeSettings)) return false; if (Double.compare(getNumAccountsRoutingWeight(), other.getNumAccountsRoutingWeight()) != 0) return false; if (Double.compare(getClusterSizeRoutingWeight(), other.getClusterSizeRoutingWeight()) != 0) return false; if (getCustomersPerClusterErrorThreshold() != other.getCustomersPerClusterErrorThreshold()) return false; if (getCustomersPerClusterWarnThreshold() != other.getCustomersPerClusterWarnThreshold()) return false; return getClusterSizeSeedThresholdBytes() == other.getClusterSizeSeedThresholdBytes(); } public boolean canEqual(Object other) { return other instanceof ElasticSearchMultiNodeConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $callTimeout = getCallTimeout();result = result * 31 + ($callTimeout == null ? 0 : $callTimeout.hashCode());Object $seedClusterRoutes = getSeedClusterRoutes();result = result * 31 + ($seedClusterRoutes == null ? 0 : $seedClusterRoutes.hashCode());Object $commonNodeSettings = getCommonNodeSettings();result = result * 31 + ($commonNodeSettings == null ? 0 : $commonNodeSettings.hashCode());Object $commonTribeNodeSettings = getCommonTribeNodeSettings();result = result * 31 + ($commonTribeNodeSettings == null ? 0 : $commonTribeNodeSettings.hashCode());long $numAccountsRoutingWeight = Double.doubleToLongBits(getNumAccountsRoutingWeight());result = result * 31 + (int)($numAccountsRoutingWeight >>> 32 ^ $numAccountsRoutingWeight);long $clusterSizeRoutingWeight = Double.doubleToLongBits(getClusterSizeRoutingWeight());result = result * 31 + (int)($clusterSizeRoutingWeight >>> 32 ^ $clusterSizeRoutingWeight);result = result * 31 + getCustomersPerClusterErrorThreshold();result = result * 31 + getCustomersPerClusterWarnThreshold();long $clusterSizeSeedThresholdBytes = getClusterSizeSeedThresholdBytes();result = result * 31 + (int)($clusterSizeSeedThresholdBytes >>> 32 ^ $clusterSizeSeedThresholdBytes);return result;
/*    */   }
/*    */   
/* 35 */   public static final long DEFAULT_REMOTE_CALL_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(10L);
/*    */   
/*    */ 
/* 38 */   public TimeUnitConfiguration getCallTimeout() { return this.callTimeout; } public void setCallTimeout(TimeUnitConfiguration callTimeout) { this.callTimeout = callTimeout; }
/*    */   
/*    */ 
/* 41 */   public SeedClusterRoutes getSeedClusterRoutes() { return this.seedClusterRoutes; } public void setSeedClusterRoutes(SeedClusterRoutes seedClusterRoutes) { this.seedClusterRoutes = seedClusterRoutes; }
/*    */   
/*    */   @Valid
/*    */   TimeUnitConfiguration callTimeout;
/*    */   private SeedClusterRoutes seedClusterRoutes;
/*    */   public Map<String, String> getCommonNodeSettings()
/*    */   {
/* 48 */     return this.commonNodeSettings; } public void setCommonNodeSettings(Map<String, String> commonNodeSettings) { this.commonNodeSettings = commonNodeSettings; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 55 */   public Map<String, String> getCommonTribeNodeSettings() { return this.commonTribeNodeSettings; } public void setCommonTribeNodeSettings(Map<String, String> commonTribeNodeSettings) { this.commonTribeNodeSettings = commonTribeNodeSettings; }
/*    */   
/*    */ 
/*    */   private Map<String, String> commonNodeSettings;
/*    */   
/*    */   private Map<String, String> commonTribeNodeSettings;
/*    */   
/*    */   public double getNumAccountsRoutingWeight()
/*    */   {
/* 64 */     return this.numAccountsRoutingWeight; } public void setNumAccountsRoutingWeight(double numAccountsRoutingWeight) { this.numAccountsRoutingWeight = numAccountsRoutingWeight; } private double numAccountsRoutingWeight = 1.0D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 72 */   public double getClusterSizeRoutingWeight() { return this.clusterSizeRoutingWeight; } public void setClusterSizeRoutingWeight(double clusterSizeRoutingWeight) { this.clusterSizeRoutingWeight = clusterSizeRoutingWeight; } private double clusterSizeRoutingWeight = 1.0D;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 78 */   public int getCustomersPerClusterErrorThreshold() { return this.customersPerClusterErrorThreshold; } public void setCustomersPerClusterErrorThreshold(int customersPerClusterErrorThreshold) { this.customersPerClusterErrorThreshold = customersPerClusterErrorThreshold; } private int customersPerClusterErrorThreshold = 1000;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 84 */   public int getCustomersPerClusterWarnThreshold() { return this.customersPerClusterWarnThreshold; } public void setCustomersPerClusterWarnThreshold(int customersPerClusterWarnThreshold) { this.customersPerClusterWarnThreshold = customersPerClusterWarnThreshold; } private int customersPerClusterWarnThreshold = 800;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 90 */   public long getClusterSizeSeedThresholdBytes() { return this.clusterSizeSeedThresholdBytes; } public void setClusterSizeSeedThresholdBytes(long clusterSizeSeedThresholdBytes) { this.clusterSizeSeedThresholdBytes = clusterSizeSeedThresholdBytes; } private long clusterSizeSeedThresholdBytes = 53687091200L;
/*    */   
/*    */   public ElasticSearchMultiNodeConfiguration() {
/* 93 */     this.callTimeout = new TimeUnitConfiguration(DEFAULT_REMOTE_CALL_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
/* 94 */     this.commonNodeSettings = new HashMap();
/* 95 */     this.commonTribeNodeSettings = new HashMap();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/multi/ElasticSearchMultiNodeConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */