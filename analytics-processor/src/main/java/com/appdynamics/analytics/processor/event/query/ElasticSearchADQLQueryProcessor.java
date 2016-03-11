/*     */ package com.appdynamics.analytics.processor.event.query;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*     */ import com.appdynamics.analytics.processor.query.ADQLParsingHelper;
/*     */ import com.appdynamics.analytics.processor.query.ParsingException;
/*     */ import com.appdynamics.analytics.processor.query.QueryProcessor;
/*     */ import com.appdynamics.analytics.processor.query.node.EsQuery;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import com.google.common.base.Function;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.elasticsearch.ElasticsearchTimeoutException;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.search.SearchType;
/*     */ import org.elasticsearch.action.search.ShardSearchFailure;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.common.xcontent.ToXContent;
/*     */ import org.elasticsearch.common.xcontent.XContentBuilder;
/*     */ import org.elasticsearch.common.xcontent.XContentFactory;
/*     */ import org.elasticsearch.index.query.FilterBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.index.query.RangeQueryBuilder;
/*     */ import org.elasticsearch.rest.RestStatus;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.elasticsearch.search.SearchHits;
/*     */ import org.elasticsearch.search.SearchShardTarget;
/*     */ import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
/*     */ import org.elasticsearch.search.aggregations.Aggregation;
/*     */ import org.elasticsearch.search.aggregations.Aggregations;
/*     */ import org.elasticsearch.search.aggregations.metrics.InternalNumericMetricsAggregation.SingleValue;
/*     */ import org.elasticsearch.search.sort.SortOrder;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public class ElasticSearchADQLQueryProcessor
/*     */ {
/*  48 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchADQLQueryProcessor.class);
/*     */   
/*     */   final ClientProvider clientProvider;
/*     */   
/*     */   final IndexNameResolver indexNameResolver;
/*     */   
/*     */   final TimeValue callTimeout;
/*     */   
/*     */ 
/*     */   public ElasticSearchADQLQueryProcessor(ClientProvider clientProvider, IndexNameResolver indexNameResolver, TimeValue callTimeout)
/*     */   {
/*  59 */     this.clientProvider = clientProvider;
/*  60 */     this.indexNameResolver = indexNameResolver;
/*  61 */     this.callTimeout = callTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public QueryResult query(String accountName, String queryString, String startTime, String endTime, int limitResults, boolean returnEsJson, final Function<String, EventSchema> getEventSchemaForEventFunction, OutputStream out)
/*     */     throws IOException
/*     */   {
/*  73 */     EsQuery query = QueryProcessor.ADQLtoESQuery(queryString, getEventSchemaForEventFunction);
/*     */     
/*  75 */     final String eventType = query.getDocType();
/*     */     
/*  77 */     String searchAlias = this.indexNameResolver.resolveSearchAlias(accountName, eventType);
/*  78 */     Client client = this.clientProvider.getSearchClient(accountName);
/*     */     
/*  80 */     SearchRequestBuilder searchRequest = client.prepareSearch(new String[0]).setIndices(new String[] { searchAlias });
/*     */     
/*     */ 
/*     */ 
/*  84 */     String timestamp = ADQLParsingHelper.getTimestampFieldForEventType(eventType);
/*     */     
/*  86 */     QueryBuilder queryBuilder = null;
/*  87 */     if ((!StringUtils.isBlank(startTime)) || (!StringUtils.isBlank(endTime))) {
/*  88 */       RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(timestamp);
/*  89 */       if (!StringUtils.isBlank(startTime)) {
/*  90 */         rangeQueryBuilder.from(Long.parseLong(startTime));
/*     */       }
/*  92 */       if (!StringUtils.isBlank(endTime)) {
/*  93 */         rangeQueryBuilder.to(Long.parseLong(endTime));
/*     */       }
/*  95 */       queryBuilder = rangeQueryBuilder;
/*     */     } else {
/*  97 */       queryBuilder = QueryBuilders.matchAllQuery();
/*     */     }
/*     */     
/* 100 */     FilterBuilder filterBuilder = query.getFilterBuilder();
/* 101 */     if (filterBuilder != null) {
/* 102 */       searchRequest.setQuery(QueryBuilders.filteredQuery(queryBuilder, filterBuilder));
/*     */     } else {
/* 104 */       searchRequest.setQuery(queryBuilder);
/*     */     }
/*     */     
/* 107 */     List<Pair<String, String>> fieldLabels = query.getFieldLabels();
/*     */     
/* 109 */     List<AbstractAggregationBuilder> aggsBuilders = query.getAggsBuilders();
/* 110 */     if (aggsBuilders != null) {
/* 111 */       for (AbstractAggregationBuilder aggBuilder : aggsBuilders) {
/* 112 */         if (aggBuilder != null) {
/* 113 */           searchRequest.addAggregation(aggBuilder);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 118 */     if (query.isAllFields()) {
/* 119 */       for (String field : ((EventSchema)getEventSchemaForEventFunction.apply(eventType)).getFields()) {
/* 120 */         fieldLabels.add(new Pair(field, null));
/*     */       }
/*     */     } else {
/* 123 */       for (Pair<String, String> fieldLabel : fieldLabels) {
/* 124 */         searchRequest.addField((String)fieldLabel.getLeft());
/*     */       }
/*     */     }
/*     */     
/* 128 */     searchRequest.setSize(limitResults);
/*     */     
/*     */ 
/* 131 */     if (aggsBuilders == null) {
/* 132 */       searchRequest.addSort("eventTimestamp", SortOrder.DESC);
/*     */     } else {
/* 134 */       searchRequest.setSearchType(SearchType.COUNT);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 141 */     log.warn("Processing ADQL on index [{}]\nADQL: [{}]\n{}", new Object[] { searchAlias, queryString, searchRequest.toString() });
/*     */     
/* 143 */     SearchResponse response = (SearchResponse)searchRequest.execute().actionGet(this.callTimeout);
/*     */     
/* 145 */     if (response.isTimedOut()) {
/* 146 */       throw new ElasticsearchTimeoutException("Search query timed out");
/*     */     }
/*     */     
/* 149 */     if (out == null) {
/* 150 */       out = new ByteArrayOutputStream();
/*     */     }
/*     */     
/* 153 */     if (returnEsJson) {
/* 154 */       serializeResultsToEsJson(out, response);
/*     */     } else {
/* 156 */       Function<Pair<String, String>, EventFieldType> getFieldTypeFunction = new Function()
/*     */       {
/*     */         public EventFieldType apply(Pair<String, String> fieldAggr) {
/* 159 */           if (QueryConstants.AGGR_FUNCTION_TO_TYPE_OVERRIDE.containsKey(fieldAggr.getRight())) {
/* 160 */             return (EventFieldType)QueryConstants.AGGR_FUNCTION_TO_TYPE_OVERRIDE.get(fieldAggr.getRight());
/*     */           }
/* 162 */           return ((EventSchema)getEventSchemaForEventFunction.apply(eventType)).getFieldType((String)fieldAggr.getLeft());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         public boolean equals(Object object)
/*     */         {
/* 169 */           return this == object;
/*     */         }
/* 171 */       };
/* 172 */       serializeResultsToJson(response, fieldLabels, getFieldTypeFunction, out);
/*     */     }
/*     */     
/* 175 */     return new QueryResult(eventType, out);
/*     */   }
/*     */   
/*     */   private void serializeResultsToEsJson(OutputStream out, SearchResponse response) throws IOException {
/* 179 */     XContentBuilder builder = XContentFactory.jsonBuilder(out);
/* 180 */     builder.startObject();
/* 181 */     response.toXContent(builder, ToXContent.EMPTY_PARAMS);
/* 182 */     builder.endObject();
/* 183 */     builder.close();
/*     */   }
/*     */   
/*     */   private Pair<String, String> parseAggegrateFunctionAndRawField(String field) {
/* 187 */     int leftBraceIndex = field.indexOf('(');
/* 188 */     if ((leftBraceIndex > 0) && (leftBraceIndex < field.indexOf(')'))) {
/* 189 */       String[] parts = StringUtils.split(field, "()");
/* 190 */       if (parts.length == 2) {
/* 191 */         return new Pair(parts[1], parts[0]);
/*     */       }
/*     */     }
/* 194 */     return new Pair(field, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void serializeResultsToJson(SearchResponse response, List<Pair<String, String>> fieldLabels, Function<Pair<String, String>, EventFieldType> getFieldTypeFunction, OutputStream output)
/*     */     throws IOException
/*     */   {
/* 202 */     XContentBuilder builder = XContentFactory.jsonBuilder(output);
/* 203 */     builder.startObject();
/*     */     
/* 205 */     EventFieldType[] fieldToTypeLookup = new EventFieldType[fieldLabels.size()];
/* 206 */     SearchHit[] hits = response.getHits().getHits();
/* 207 */     Aggregations aggs = response.getAggregations();
/* 208 */     ShardSearchFailure[] failures = response.getShardFailures();
/*     */     
/* 210 */     boolean hasAggs = (aggs != null) && (aggs.asList().size() > 0);
/*     */     
/* 212 */     builder.startArray("fields");
/* 213 */     int index = 0;
/* 214 */     for (Pair<String, String> fieldLabel : fieldLabels) {
/* 215 */       String selectField = (String)fieldLabel.getLeft();
/* 216 */       String label = (String)fieldLabel.getRight();
/* 217 */       if (label == null) {
/* 218 */         label = selectField;
/*     */       }
/*     */       
/* 221 */       Pair<String, String> fieldAggr = parseAggegrateFunctionAndRawField(selectField);
/* 222 */       String field = (String)fieldAggr.getLeft();
/* 223 */       String aggregation = (String)fieldAggr.getRight();
/*     */       
/* 225 */       EventFieldType eventFieldType = (EventFieldType)getFieldTypeFunction.apply(fieldAggr);
/* 226 */       String friendlyFieldType = (String)QueryConstants.FIELD_TYPE_NAMES.get(eventFieldType);
/* 227 */       fieldToTypeLookup[index] = eventFieldType;
/*     */       
/* 229 */       if (fieldAggr.getRight() != null) {
/* 230 */         hasAggs = true;
/*     */       }
/*     */       
/* 233 */       builder.startObject();
/* 234 */       builder.field("label", label);
/* 235 */       builder.field("field", field);
/* 236 */       builder.field("type", friendlyFieldType);
/* 237 */       builder.field("aggregation", aggregation);
/* 238 */       builder.endObject();
/* 239 */       index++;
/*     */     }
/* 241 */     builder.endArray();
/*     */     
/* 243 */     long total = 1L;
/* 244 */     if (!hasAggs) {
/* 245 */       total = hits.length;
/*     */     }
/* 247 */     builder.field("total", total);
/*     */     
/* 249 */     builder.startArray("results");
/*     */     
/* 251 */     if (hasAggs) {
/* 252 */       builder.startArray();
/* 253 */       index = 0;
/* 254 */       for (Pair<String, String> field : fieldLabels) {
/* 255 */         Pair<String, String> fieldAggr = parseAggegrateFunctionAndRawField((String)field.getLeft());
/*     */         
/* 257 */         if ("count".equalsIgnoreCase((String)fieldAggr.getRight())) {
/* 258 */           builder.value(response.getHits().getTotalHits());
/*     */         } else {
/* 260 */           Aggregation agg = null;
/* 261 */           if ((aggs != null) && (aggs.getAsMap() != null)) {
/* 262 */             agg = (Aggregation)aggs.getAsMap().get(field.getLeft());
/*     */           }
/* 264 */           if (agg == null) {
/* 265 */             builder.value((Object)null);
/* 266 */           } else if ((agg instanceof InternalNumericMetricsAggregation.SingleValue)) {
/* 267 */             InternalNumericMetricsAggregation.SingleValue metric = (InternalNumericMetricsAggregation.SingleValue)agg;
/*     */             
/*     */ 
/* 270 */             if (fieldToTypeLookup[index] == EventFieldType.NUMBER_INT) {
/* 271 */               double unexpectedValue = metric.value() - metric.value();
/* 272 */               if (unexpectedValue != 0.0D) {
/* 273 */                 log.warn("Expected integer aggregation result is actually a floating point number. Value={} and aggregation result: {}", Double.valueOf(metric.value()), agg.toString());
/*     */               }
/*     */               
/* 276 */               builder.value(metric.value());
/*     */             } else {
/* 278 */               builder.value(metric.value());
/*     */             }
/*     */           } else {
/* 281 */             throw new ParsingException(String.format("Unsupported aggregation result encountered %s", new Object[] { agg.toString() }));
/*     */           }
/*     */         }
/*     */         
/* 285 */         index++;
/*     */       }
/* 287 */       builder.endArray();
/*     */     } else {
/* 289 */       for (SearchHit hit : hits) {
/* 290 */         builder.startArray();
/* 291 */         if (hit.sourceAsMap() != null) {
/* 292 */           for (Pair<String, String> fieldLabel : fieldLabels) {
/* 293 */             if (hit.sourceAsMap().containsKey(fieldLabel.getLeft())) {
/* 294 */               builder.value(hit.sourceAsMap().get(fieldLabel.getLeft()));
/*     */             } else {
/* 296 */               builder.value((Object)null);
/*     */             }
/*     */           }
/*     */         } else {
/* 300 */           for (Pair<String, String> fieldLabel : fieldLabels) {
/* 301 */             if (hit.fields().containsKey(fieldLabel.getLeft())) {
/* 302 */               builder.value((Iterable)hit.fields().get(fieldLabel.getLeft()));
/*     */             } else {
/* 304 */               builder.value((Object)null);
/*     */             }
/*     */           }
/*     */         }
/* 308 */         builder.endArray();
/*     */       }
/*     */     }
/*     */     
/* 312 */     builder.endArray();
/*     */     
/* 314 */     if (failures.length > 0) {
/* 315 */       builder.startObject();
/* 316 */       builder.startArray();
/* 317 */       for (ShardSearchFailure failure : failures) {
/* 318 */         builder.startObject();
/* 319 */         builder.field("status", failure.status().getStatus());
/* 320 */         builder.field("reason", failure.reason());
/* 321 */         if (failure.shard() != null) {
/* 322 */           builder.field("id", failure.shard().shardId());
/* 323 */           builder.field("details", failure.shard().indexText());
/*     */         }
/* 325 */         builder.endObject();
/*     */       }
/*     */       
/* 328 */       builder.endArray();
/* 329 */       builder.endObject();
/*     */     }
/*     */     
/* 332 */     builder.endObject();
/* 333 */     builder.close();
/*     */   }
/*     */   
/*     */   private boolean isFilteredField(List<String> filterFields, String fieldName) {
/* 337 */     if ((filterFields != null) && (filterFields.size() > 0) && (filterFields.contains(fieldName.toLowerCase())))
/*     */     {
/* 339 */       return true;
/*     */     }
/* 341 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/query/ElasticSearchADQLQueryProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */