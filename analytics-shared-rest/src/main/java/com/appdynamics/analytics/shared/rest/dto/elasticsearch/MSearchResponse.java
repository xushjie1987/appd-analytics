/*    */ package com.appdynamics.analytics.shared.rest.dto.elasticsearch;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class MSearchResponse<T>
/*    */ {
/*    */   List<SearchResponse<T>> responses;
/*    */   
/* 16 */   public String toString() { return "MSearchResponse(responses=" + getResponses() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $responses = getResponses();result = result * 31 + ($responses == null ? 0 : $responses.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof MSearchResponse; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof MSearchResponse)) return false; MSearchResponse<?> other = (MSearchResponse)o; if (!other.canEqual(this)) return false; Object this$responses = getResponses();Object other$responses = other.getResponses();return this$responses == null ? other$responses == null : this$responses.equals(other$responses);
/*    */   }
/*    */   
/* 19 */   public void setResponses(List<SearchResponse<T>> responses) { this.responses = responses; } public List<SearchResponse<T>> getResponses() { return this.responses; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/elasticsearch/MSearchResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */