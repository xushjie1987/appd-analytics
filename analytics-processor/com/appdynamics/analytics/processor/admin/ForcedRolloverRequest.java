/*    */ package com.appdynamics.analytics.processor.admin;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.MappingMergeStrategy;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ForcedRolloverRequest
/*    */ {
/*    */   @NotNull
/*    */   String cluster;
/*    */   @NotNull
/*    */   String index;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 24 */     if (o == this) return true; if (!(o instanceof ForcedRolloverRequest)) return false; ForcedRolloverRequest other = (ForcedRolloverRequest)o; if (!other.canEqual(this)) return false; Object this$cluster = getCluster();Object other$cluster = other.getCluster(); if (this$cluster == null ? other$cluster != null : !this$cluster.equals(other$cluster)) return false; Object this$index = getIndex();Object other$index = other.getIndex(); if (this$index == null ? other$index != null : !this$index.equals(other$index)) return false; Object this$numberOfShards = getNumberOfShards();Object other$numberOfShards = other.getNumberOfShards(); if (this$numberOfShards == null ? other$numberOfShards != null : !this$numberOfShards.equals(other$numberOfShards)) return false; Object this$eventTypeMappingOverride = getEventTypeMappingOverride();Object other$eventTypeMappingOverride = other.getEventTypeMappingOverride(); if (this$eventTypeMappingOverride == null ? other$eventTypeMappingOverride != null : !this$eventTypeMappingOverride.equals(other$eventTypeMappingOverride)) return false; Object this$mappingMergeStrategy = getMappingMergeStrategy();Object other$mappingMergeStrategy = other.getMappingMergeStrategy();return this$mappingMergeStrategy == null ? other$mappingMergeStrategy == null : this$mappingMergeStrategy.equals(other$mappingMergeStrategy); } public boolean canEqual(Object other) { return other instanceof ForcedRolloverRequest; } public int hashCode() { int PRIME = 31;int result = 1;Object $cluster = getCluster();result = result * 31 + ($cluster == null ? 0 : $cluster.hashCode());Object $index = getIndex();result = result * 31 + ($index == null ? 0 : $index.hashCode());Object $numberOfShards = getNumberOfShards();result = result * 31 + ($numberOfShards == null ? 0 : $numberOfShards.hashCode());Object $eventTypeMappingOverride = getEventTypeMappingOverride();result = result * 31 + ($eventTypeMappingOverride == null ? 0 : $eventTypeMappingOverride.hashCode());Object $mappingMergeStrategy = getMappingMergeStrategy();result = result * 31 + ($mappingMergeStrategy == null ? 0 : $mappingMergeStrategy.hashCode());return result; } public String toString() { return "ForcedRolloverRequest(cluster=" + getCluster() + ", index=" + getIndex() + ", numberOfShards=" + getNumberOfShards() + ", eventTypeMappingOverride=" + getEventTypeMappingOverride() + ", mappingMergeStrategy=" + getMappingMergeStrategy() + ")"; }
/*    */   
/*    */ 
/* 27 */   public String getCluster() { return this.cluster; } public void setCluster(String cluster) { this.cluster = cluster; }
/*    */   
/*    */ 
/* 30 */   public String getIndex() { return this.index; } public void setIndex(String index) { this.index = index; }
/*    */   
/* 32 */   public Integer getNumberOfShards() { return this.numberOfShards; } public void setNumberOfShards(Integer numberOfShards) { this.numberOfShards = numberOfShards; } Integer numberOfShards = null;
/*    */   
/*    */   JsonNode eventTypeMappingOverride;
/*    */   MappingMergeStrategy mappingMergeStrategy;
/*    */   
/* 37 */   public JsonNode getEventTypeMappingOverride() { return this.eventTypeMappingOverride; } public void setEventTypeMappingOverride(JsonNode eventTypeMappingOverride) { this.eventTypeMappingOverride = eventTypeMappingOverride; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 42 */   public MappingMergeStrategy getMappingMergeStrategy() { return this.mappingMergeStrategy; } public void setMappingMergeStrategy(MappingMergeStrategy mappingMergeStrategy) { this.mappingMergeStrategy = mappingMergeStrategy; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/ForcedRolloverRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */