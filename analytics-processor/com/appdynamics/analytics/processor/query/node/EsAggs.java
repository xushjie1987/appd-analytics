/*    */ package com.appdynamics.analytics.processor.query.node;
/*    */ 
/*    */ import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EsAggs
/*    */   implements EsNode
/*    */ {
/*    */   private String name;
/*    */   private AbstractAggregationBuilder aggsBuilder;
/*    */   
/* 19 */   public String getName() { return this.name; } public void setName(String name) { this.name = name; }
/*    */   
/* 21 */   public AbstractAggregationBuilder getAggsBuilder() { return this.aggsBuilder; } public void setAggsBuilder(AbstractAggregationBuilder aggsBuilder) { this.aggsBuilder = aggsBuilder; }
/*    */   
/*    */   public EsAggs(String name, AbstractAggregationBuilder aggsBuilder) {
/* 24 */     this.name = name;
/* 25 */     this.aggsBuilder = aggsBuilder;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/node/EsAggs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */