/*    */ package com.appdynamics.analytics.processor.event;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
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
/*    */ 
/*    */ 
/*    */ public abstract interface ElasticSearchConstants
/*    */ {
/*    */   public static final String PROPERTY_PREFIX = "ad.es.";
/*    */   public static final String PROPERTIES = "properties";
/*    */   public static final String DOCVALUES = "doc_values";
/*    */   public static final String INDEX = "index";
/*    */   public static final String ANALYZED = "analyzed";
/*    */   public static final String NOT_ANALYZED = "not_analyzed";
/*    */   public static final String DYNAMIC_TEMPLATES = "dynamic_templates";
/*    */   public static final String MAPPING = "mapping";
/*    */   public static final String MATCH = "match";
/*    */   public static final String MATCH_MAPPING_TYPE = "match_mapping_type";
/*    */   public static final String TYPE = "type";
/*    */   public static final String STRING_TYPE = "string";
/*    */   public static final String NUM_SHARDS = "number_of_shards";
/*    */   public static final String NUM_REPLICAS = "number_of_replicas";
/*    */   public static final String DYNAMIC_MAPPER = "index.mapper.dynamic";
/*    */   public static final String DATA_NODE = "node.data";
/*    */   public static final String FIELD_DATA = "fielddata";
/*    */   public static final String FORMAT = "format";
/*    */   public static final String DISABLED = "disabled";
/*    */   public static final String CLUSTER_NAME = "cluster.name";
/*    */   public static final String AD_TRIBE_CLUSTER_NAME = "ad.tribe.cluster.name";
/*    */   public static final String MIN_MASTER_NODES = "discovery.zen.minimum_master_nodes";
/*    */   public static final String GLOBAL_ORDINALS_LOW_CARDINALITY_EXEC_HINT = "global_ordinals_low_cardinality";
/*    */   public static final String MATCH_ALL_VALUE = "*";
/*    */   public static final String DEFAULT_STRING_FIELDS_DYNAMIC_TEMPLATE_NAME = "string_fields";
/*    */   public static final String FIELD_ID = "_id";
/*    */   public static final String FIELD_ACCOUNT_NAME = "accountName";
/*    */   public static final String FIELD_EVENT_TIMESTAMP = "eventTimestamp";
/*    */   public static final String FIELD_PICKUP_TIMESTAMP = "pickupTimestamp";
/*    */   public static final String FIELD_SOURCE = "_source";
/*    */   public static final String DYNAMIC_DEFAULTS_TEMPLATE = "{\"%s\":{\"mapping\":{\"type\":\"string\",\"index\":\"not_analyzed\"},\"match\":\"*\",\"match_mapping_type\":\"string\"}}";
/*    */   public static final String ANALYTICS_SCHEMA_NAME = "analytics";
/*    */   public static final int ANALYTICS_SCHEMA_VERSION = 0;
/*    */   public static final String SCHEMA_VERSION_INDEX = "appdynamics_version";
/*    */   public static final String SOURCE_FIELD = "_source";
/*    */   public static final String FIELDS = "fields";
/* 64 */   public static final ImmutableSet<String> VALID_DOC_VALUE_TYPES = ImmutableSet.of("string", "float", "double", "integer", "long", "short", new String[] { "byte", "date", "binary" });
/*    */   public static final String ALL_FIELD = "_all";
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ElasticSearchConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */