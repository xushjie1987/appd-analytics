/*    */ package com.appdynamics.analytics.processor.zookeeper.client;
/*    */ 
/*    */ import javax.validation.constraints.Min;
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
/*    */ public class ZookeeperClientConfiguration
/*    */ {
/* 18 */   public String toString() { return "ZookeeperClientConfiguration(baseSleepTimeMs=" + getBaseSleepTimeMs() + ", maxRetries=" + getMaxRetries() + ", zookeeperConnect=" + getZookeeperConnect() + ")"; }
/* 19 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ZookeeperClientConfiguration)) return false; ZookeeperClientConfiguration other = (ZookeeperClientConfiguration)o; if (!other.canEqual(this)) return false; if (getBaseSleepTimeMs() != other.getBaseSleepTimeMs()) return false; if (getMaxRetries() != other.getMaxRetries()) return false; Object this$zookeeperConnect = getZookeeperConnect();Object other$zookeeperConnect = other.getZookeeperConnect();return this$zookeeperConnect == null ? other$zookeeperConnect == null : this$zookeeperConnect.equals(other$zookeeperConnect); } public boolean canEqual(Object other) { return other instanceof ZookeeperClientConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + getBaseSleepTimeMs();result = result * 31 + getMaxRetries();Object $zookeeperConnect = getZookeeperConnect();result = result * 31 + ($zookeeperConnect == null ? 0 : $zookeeperConnect.hashCode());return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @Min(1000L)
/* 26 */   int baseSleepTimeMs = 1000;
/* 27 */   public int getBaseSleepTimeMs() { return this.baseSleepTimeMs; } public void setBaseSleepTimeMs(int baseSleepTimeMs) { this.baseSleepTimeMs = baseSleepTimeMs; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 32 */   public int getMaxRetries() { return this.maxRetries; } public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; } int maxRetries = 29;
/*    */   
/*    */   String zookeeperConnect;
/*    */   
/*    */ 
/* 37 */   public String getZookeeperConnect() { return this.zookeeperConnect; } public void setZookeeperConnect(String zookeeperConnect) { this.zookeeperConnect = zookeeperConnect; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/client/ZookeeperClientConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */