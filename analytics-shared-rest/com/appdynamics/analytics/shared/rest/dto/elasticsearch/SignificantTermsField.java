/*    */ package com.appdynamics.analytics.shared.rest.dto.elasticsearch;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SignificantTermsField
/*    */ {
/*    */   @JsonProperty("doc_count")
/*    */   long docCount;
/*    */   @JsonProperty("buckets")
/*    */   Iterable<JsonNode> terms;
/*    */   
/*    */   @ConstructorProperties({"docCount", "terms"})
/*    */   public SignificantTermsField(long docCount, Iterable<JsonNode> terms)
/*    */   {
/* 19 */     this.docCount = docCount;this.terms = terms; }
/* 20 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof SignificantTermsField)) return false; SignificantTermsField other = (SignificantTermsField)o; if (!other.canEqual(this)) return false; if (getDocCount() != other.getDocCount()) return false; Object this$terms = getTerms();Object other$terms = other.getTerms();return this$terms == null ? other$terms == null : this$terms.equals(other$terms); } public boolean canEqual(Object other) { return other instanceof SignificantTermsField; } public int hashCode() { int PRIME = 31;int result = 1;long $docCount = getDocCount();result = result * 31 + (int)($docCount >>> 32 ^ $docCount);Object $terms = getTerms();result = result * 31 + ($terms == null ? 0 : $terms.hashCode());return result; } public String toString() { return "SignificantTermsField(docCount=" + getDocCount() + ", terms=" + getTerms() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 27 */   public long getDocCount() { return this.docCount; } public void setDocCount(long docCount) { this.docCount = docCount; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 33 */   public Iterable<JsonNode> getTerms() { return this.terms; } public void setTerms(Iterable<JsonNode> terms) { this.terms = terms; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/elasticsearch/SignificantTermsField.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */