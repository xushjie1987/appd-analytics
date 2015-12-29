/*    */ package com.appdynamics.analytics.shared.rest.dto.elasticsearch;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class SearchResponse<T>
/*    */ {
/*    */   Hits<T> hits;
/*    */   String error;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 14 */     if (o == this) return true; if (!(o instanceof SearchResponse)) return false; SearchResponse<?> other = (SearchResponse)o; if (!other.canEqual(this)) return false; Object this$hits = getHits();Object other$hits = other.getHits(); if (this$hits == null ? other$hits != null : !this$hits.equals(other$hits)) return false; Object this$error = getError();Object other$error = other.getError();return this$error == null ? other$error == null : this$error.equals(other$error); } public boolean canEqual(Object other) { return other instanceof SearchResponse; } public int hashCode() { int PRIME = 31;int result = 1;Object $hits = getHits();result = result * 31 + ($hits == null ? 0 : $hits.hashCode());Object $error = getError();result = result * 31 + ($error == null ? 0 : $error.hashCode());return result; } public String toString() { return "SearchResponse(hits=" + getHits() + ", error=" + getError() + ")"; }
/*    */   
/*    */ 
/* 17 */   public Hits<T> getHits() { return this.hits; } public void setHits(Hits<T> hits) { this.hits = hits; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 22 */   public String getError() { return this.error; } public void setError(String error) { this.error = error; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/elasticsearch/SearchResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */