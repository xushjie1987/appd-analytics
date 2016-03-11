/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.configuration;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.beans.ConstructorProperties;
/*    */ import javax.validation.constraints.Min;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventIndexDefaults
/*    */ {
/*    */   @Min(1L)
/*    */   @JsonProperty("number_of_shards")
/*    */   Integer shards;
/*    */   @Min(0L)
/*    */   @JsonProperty("number_of_replicas")
/*    */   Integer replicas;
/*    */   
/*    */   @ConstructorProperties({"shards", "replicas"})
/*    */   public EventIndexDefaults(Integer shards, Integer replicas)
/*    */   {
/* 25 */     this.shards = shards;this.replicas = replicas; }
/*    */   
/* 27 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof EventIndexDefaults)) return false; EventIndexDefaults other = (EventIndexDefaults)o; if (!other.canEqual(this)) return false; Object this$shards = getShards();Object other$shards = other.getShards(); if (this$shards == null ? other$shards != null : !this$shards.equals(other$shards)) return false; Object this$replicas = getReplicas();Object other$replicas = other.getReplicas();return this$replicas == null ? other$replicas == null : this$replicas.equals(other$replicas); } public boolean canEqual(Object other) { return other instanceof EventIndexDefaults; } public int hashCode() { int PRIME = 31;int result = 1;Object $shards = getShards();result = result * 31 + ($shards == null ? 0 : $shards.hashCode());Object $replicas = getReplicas();result = result * 31 + ($replicas == null ? 0 : $replicas.hashCode());return result; } public String toString() { return "EventIndexDefaults(shards=" + getShards() + ", replicas=" + getReplicas() + ")"; }
/*    */   
/*    */ 
/*    */ 
/* 31 */   public Integer getShards() { return this.shards; } public void setShards(Integer shards) { this.shards = shards; }
/*    */   
/*    */ 
/*    */ 
/* 35 */   public Integer getReplicas() { return this.replicas; } public void setReplicas(Integer replicas) { this.replicas = replicas; }
/*    */   
/*    */   public EventIndexDefaults() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/configuration/EventIndexDefaults.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */