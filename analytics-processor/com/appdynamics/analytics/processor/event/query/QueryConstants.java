/*    */ package com.appdynamics.analytics.processor.event.query;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ public class QueryConstants
/*    */ {
/*    */   public static final String TOTAL = "total";
/*    */   public static final String RESULTS = "results";
/*    */   public static final String FIELDS = "fields";
/*    */   public static final String FIELD = "field";
/*    */   public static final String LABEL = "label";
/*    */   public static final String ERROR = "error";
/*    */   public static final String STATUS = "status";
/*    */   public static final String REASON = "reason";
/*    */   public static final String DETAILS = "details";
/*    */   public static final String ID = "id";
/*    */   public static final String LOG_EVENT_TYPE = "log_v1";
/*    */   public static final String BIZ_TXN_EVENT_TYPE = "biz_txn_v1";
/*    */   public static final String BROWSER_RECORD = "browserrecord";
/*    */   public static final String PROPERTIES = "properties";
/*    */   public static final String TYPE = "type";
/*    */   public static final String STRING = "string";
/*    */   public static final String AGGREGATION = "aggregation";
/*    */   public static final String BUCKETS = "buckets";
/*    */   public static final String AGGR_FUNCTION_COUNT = "count";
/*    */   public static final String AGGR_FUNCTION_SUM = "sum";
/*    */   public static final String AGGR_FUNCTION_AVG = "avg";
/*    */   public static final String AGGR_FUNCTION_MIN = "min";
/*    */   public static final String AGGR_FUNCTION_MAX = "max";
/* 42 */   static final HashMap<String, EventFieldType> AGGR_FUNCTION_TO_TYPE_OVERRIDE = new HashMap();
/*    */   
/*    */   public static final String FIELD_TYPE_UNKNOWN = "unknown";
/*    */   
/*    */   public static final String FIELD_TYPE_STRING = "string";
/*    */   
/*    */   public static final String FIELD_TYPE_INTEGER = "integer";
/*    */   
/*    */   public static final String FIELD_TYPE_FLOAT = "float";
/*    */   public static final String FIELD_TYPE_BOOLEAN = "boolean";
/*    */   public static final String FIELD_TYPE_DATE = "date";
/*    */   public static final String FIELD_TYPE_NULL = "null";
/* 54 */   static final HashMap<EventFieldType, String> FIELD_TYPE_NAMES = new HashMap();
/*    */   
/*    */   static {
/* 57 */     AGGR_FUNCTION_TO_TYPE_OVERRIDE.put("count", EventFieldType.NUMBER_INT);
/* 58 */     AGGR_FUNCTION_TO_TYPE_OVERRIDE.put("avg", EventFieldType.NUMBER_FLOAT);
/*    */     
/* 60 */     FIELD_TYPE_NAMES.put(EventFieldType.UNKNOWN, "unknown");
/* 61 */     FIELD_TYPE_NAMES.put(EventFieldType.STRING, "string");
/* 62 */     FIELD_TYPE_NAMES.put(EventFieldType.NUMBER_INT, "integer");
/* 63 */     FIELD_TYPE_NAMES.put(EventFieldType.NUMBER_FLOAT, "float");
/* 64 */     FIELD_TYPE_NAMES.put(EventFieldType.BOOLEAN, "boolean");
/* 65 */     FIELD_TYPE_NAMES.put(EventFieldType.DATETIME, "date");
/* 66 */     FIELD_TYPE_NAMES.put(EventFieldType.NULL, "null");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/query/QueryConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */