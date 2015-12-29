/*    */ package com.appdynamics.analytics.processor.query.node;
/*    */ 
/*    */ import org.elasticsearch.index.query.FilterBuilder;
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
/*    */ public class EsFilter
/*    */   implements EsNode
/*    */ {
/*    */   public FilterBuilder filterBuilder;
/*    */   
/* 19 */   public void setFilterBuilder(FilterBuilder filterBuilder) { this.filterBuilder = filterBuilder; } public FilterBuilder getFilterBuilder() { return this.filterBuilder; }
/*    */   
/*    */   public EsFilter(FilterBuilder filterBuilder) {
/* 22 */     this.filterBuilder = filterBuilder;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/node/EsFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */