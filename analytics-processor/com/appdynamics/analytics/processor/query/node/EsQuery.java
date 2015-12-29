/*    */ package com.appdynamics.analytics.processor.query.node;
/*    */ 
/*    */ import com.appdynamics.common.util.misc.Pair;
/*    */ import java.util.List;
/*    */ import org.elasticsearch.ElasticsearchException;
/*    */ import org.elasticsearch.common.xcontent.ToXContent;
/*    */ import org.elasticsearch.common.xcontent.XContentBuilder;
/*    */ import org.elasticsearch.common.xcontent.XContentFactory;
/*    */ import org.elasticsearch.index.query.FilterBuilder;
/*    */ import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EsQuery
/*    */   implements EsNode
/*    */ {
/*    */   private FilterBuilder filterBuilder;
/*    */   private List<Pair<String, String>> fieldLabels;
/*    */   private boolean allFields;
/*    */   private List<AbstractAggregationBuilder> aggsBuilders;
/*    */   private String docType;
/*    */   
/* 26 */   public FilterBuilder getFilterBuilder() { return this.filterBuilder; }
/* 27 */   public List<Pair<String, String>> getFieldLabels() { return this.fieldLabels; }
/* 28 */   public boolean isAllFields() { return this.allFields; }
/* 29 */   public List<AbstractAggregationBuilder> getAggsBuilders() { return this.aggsBuilders; }
/* 30 */   public String getDocType() { return this.docType; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EsQuery(List<Pair<String, String>> fieldLabels, boolean allFields, List<AbstractAggregationBuilder> aggsBuilder, String docType, FilterBuilder filterBuilder)
/*    */   {
/* 38 */     this.fieldLabels = fieldLabels;
/* 39 */     this.allFields = allFields;
/* 40 */     this.aggsBuilders = aggsBuilder;
/* 41 */     this.docType = docType;
/* 42 */     this.filterBuilder = filterBuilder;
/*    */   }
/*    */   
/*    */   public String getFilterAsCompactJson() {
/*    */     try {
/* 47 */       XContentBuilder builder = XContentFactory.jsonBuilder();
/* 48 */       this.filterBuilder.toXContent(builder, ToXContent.EMPTY_PARAMS);
/* 49 */       return builder.string();
/*    */     } catch (Exception e) {
/* 51 */       throw new ElasticsearchException("Failed to build filter", e);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getAggsAsCompactJson() {
/*    */     try {
/* 57 */       XContentBuilder builder = XContentFactory.jsonBuilder();
/* 58 */       for (AbstractAggregationBuilder aggsBuilder : this.aggsBuilders) {
/* 59 */         aggsBuilder.toXContent(builder, ToXContent.EMPTY_PARAMS);
/*    */       }
/* 61 */       return builder.string();
/*    */     } catch (Exception e) {
/* 63 */       throw new ElasticsearchException("Failed to build aggregations", e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/node/EsQuery.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */