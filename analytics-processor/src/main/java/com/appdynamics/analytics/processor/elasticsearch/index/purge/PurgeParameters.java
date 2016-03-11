/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.purge;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PurgeParameters
/*    */ {
/*    */   String indexToPurge;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 15 */   public String toString() { return "PurgeParameters(indexToPurge=" + getIndexToPurge() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $indexToPurge = getIndexToPurge();result = result * 31 + ($indexToPurge == null ? 0 : $indexToPurge.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof PurgeParameters; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof PurgeParameters)) return false; PurgeParameters other = (PurgeParameters)o; if (!other.canEqual(this)) return false; Object this$indexToPurge = getIndexToPurge();Object other$indexToPurge = other.getIndexToPurge();return this$indexToPurge == null ? other$indexToPurge == null : this$indexToPurge.equals(other$indexToPurge); }
/*    */   
/* 17 */   public void setIndexToPurge(String indexToPurge) { this.indexToPurge = indexToPurge; } public String getIndexToPurge() { return this.indexToPurge; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/purge/PurgeParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */