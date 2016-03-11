/*    */ package com.appdynamics.analytics.shared.rest.dto.elasticsearch;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ 
/*    */ @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class Hit<T>
/*    */ {
/*    */   @JsonProperty("_index")
/*    */   String index;
/*    */   @JsonProperty("_source")
/*    */   T source;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 15 */     if (o == this) return true; if (!(o instanceof Hit)) return false; Hit<?> other = (Hit)o; if (!other.canEqual(this)) return false; Object this$index = getIndex();Object other$index = other.getIndex(); if (this$index == null ? other$index != null : !this$index.equals(other$index)) return false; Object this$source = getSource();Object other$source = other.getSource();return this$source == null ? other$source == null : this$source.equals(other$source); } public boolean canEqual(Object other) { return other instanceof Hit; } public int hashCode() { int PRIME = 31;int result = 1;Object $index = getIndex();result = result * 31 + ($index == null ? 0 : $index.hashCode());Object $source = getSource();result = result * 31 + ($source == null ? 0 : $source.hashCode());return result; } public String toString() { return "Hit(index=" + getIndex() + ", source=" + getSource() + ")"; }
/*    */   
/*    */ 
/*    */ 
/* 19 */   public String getIndex() { return this.index; } public void setIndex(String index) { this.index = index; }
/*    */   
/* 21 */   public T getSource() { return (T)this.source; } public void setSource(T source) { this.source = source; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/elasticsearch/Hit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */