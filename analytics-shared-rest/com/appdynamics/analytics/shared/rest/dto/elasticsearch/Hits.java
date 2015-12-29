/*    */ package com.appdynamics.analytics.shared.rest.dto.elasticsearch;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class Hits<T>
/*    */ {
/*    */   int total;
/*    */   List<Hit<T>> hits;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 16 */     if (o == this) return true; if (!(o instanceof Hits)) return false; Hits<?> other = (Hits)o; if (!other.canEqual(this)) return false; if (getTotal() != other.getTotal()) return false; Object this$hits = getHits();Object other$hits = other.getHits();return this$hits == null ? other$hits == null : this$hits.equals(other$hits); } public boolean canEqual(Object other) { return other instanceof Hits; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + getTotal();Object $hits = getHits();result = result * 31 + ($hits == null ? 0 : $hits.hashCode());return result; } public String toString() { return "Hits(total=" + getTotal() + ", hits=" + getHits() + ")"; }
/*    */   
/*    */ 
/* 19 */   public int getTotal() { return this.total; } public void setTotal(int total) { this.total = total; }
/* 20 */   public List<Hit<T>> getHits() { return this.hits; } public void setHits(List<Hit<T>> hits) { this.hits = hits; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/elasticsearch/Hits.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */