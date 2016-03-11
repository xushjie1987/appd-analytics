/*     */ package com.appdynamics.analytics.processor.event.meter;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.AccountManager;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.appdynamics.common.util.var.SystemVariableResolver;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.Hashing;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.Nullable;
/*     */ import org.elasticsearch.ElasticsearchException;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.bulk.BulkRequestBuilder;
/*     */ import org.elasticsearch.action.bulk.BulkResponse;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.search.SearchType;
/*     */ import org.elasticsearch.action.update.UpdateRequest;
/*     */ import org.elasticsearch.action.update.UpdateRequestBuilder;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.index.query.BoolFilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilder;
/*     */ import org.elasticsearch.index.query.FilterBuilders;
/*     */ import org.elasticsearch.index.query.FilteredQueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.index.query.RangeFilterBuilder;
/*     */ import org.elasticsearch.rest.RestStatus;
/*     */ import org.elasticsearch.script.ScriptService.ScriptType;
/*     */ import org.elasticsearch.search.aggregations.Aggregation;
/*     */ import org.elasticsearch.search.aggregations.Aggregations;
/*     */ import org.elasticsearch.search.aggregations.metrics.sum.Sum;
/*     */ import org.elasticsearch.search.aggregations.metrics.sum.SumBuilder;
/*     */ import org.joda.time.DateTime;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public class MetersStore
/*     */ {
/*  49 */   private static final Logger log = LoggerFactory.getLogger(MetersStore.class);
/*     */   
/*     */   public static final String METERS_INDEX = "appdynamics_meters";
/*     */   
/*     */   public static final String METERS_INDEX_CURRENT_VERSION = "appdynamics_meters_v2";
/*  54 */   public static final String BYTES_METER_TYPE = MeterType.bytesMeter.name();
/*  55 */   public static final String DOC_FRAGMENTS_METER_TYPE = MeterType.docFragmentsMeter.name();
/*  56 */   public static final String MAX_SEARCHES_METER_TYPE = MeterType.maxSearchesMeter.name();
/*     */   static final String NAME_DOC_TYPE = "bytes";
/*     */   static final int UPSERT_RETRY_COUNT = 3;
/*     */   static final long UPSERT_TIMEOUT_MILLIS = 30000L;
/*     */   public static final String ACCOUNT_NAME = "accountName";
/*     */   public static final String EVENT_TYPE = "eventType";
/*     */   public static final String METER_TYPE = "meterType";
/*     */   public static final String DAY = "day";
/*     */   public static final String HOUR = "hour";
/*     */   public static final String COUNT_TOTAL = "countTotal";
/*     */   public static final String OVERAGE_TOTAL = "overageTotal";
/*     */   static final String UPSERT_PARAM_COUNT_NOW = "countNow";
/*     */   static final String UPSERT_PARAM_OVERAGE_NOW = "overageNow";
/*     */   final ClientProvider clientProvider;
/*     */   final AccountManager accountManager;
/*     */   final long usageTimeoutMillis;
/*     */   final String uniqueQueryIdPrefix;
/*     */   
/*     */   MetersStore(ClientProvider clientProvider, AccountManager accountManager, long usageTimeoutMillis)
/*     */   {
/*  76 */     this.clientProvider = clientProvider;
/*  77 */     this.accountManager = accountManager;
/*  78 */     this.usageTimeoutMillis = usageTimeoutMillis;
/*  79 */     this.uniqueQueryIdPrefix = (MetersStore.class.getSimpleName() + "-" + SystemVariableResolver.getHostName() + "-" + SystemVariableResolver.getProcessId() + "-");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getUsageBytes(String accountName, String eventType, DateTime date, int hourOfDay)
/*     */   {
/*  94 */     return getUsage(accountName, eventType, MeterType.bytesMeter.name(), date, date, Integer.valueOf(hourOfDay), false);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getOverageBytes(String accountName, String eventType, DateTime date, int hourOfDay)
/*     */   {
/* 100 */     return getUsage(accountName, eventType, MeterType.bytesMeter.name(), date, date, Integer.valueOf(hourOfDay), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getUsageBytes(String accountName, String eventType, DateTime fromDay, DateTime toDay)
/*     */   {
/* 114 */     return getUsage(accountName, eventType, MeterType.bytesMeter.name(), fromDay, toDay, null, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getOverageBytes(String accountName, String eventType, DateTime fromDay, DateTime toDay)
/*     */   {
/* 120 */     return getUsage(accountName, eventType, MeterType.bytesMeter.name(), fromDay, toDay, null, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getUsageDocumentFragments(String accountName, String eventType, DateTime date, int hourOfDay)
/*     */   {
/* 134 */     return getUsage(accountName, eventType, MeterType.docFragmentsMeter.name(), date, date, Integer.valueOf(hourOfDay), false);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getOverageDocumentFragments(String accountName, String eventType, DateTime date, int hourOfDay)
/*     */   {
/* 140 */     return getUsage(accountName, eventType, MeterType.docFragmentsMeter.name(), date, date, Integer.valueOf(hourOfDay), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getUsageDocumentFragments(String accountName, String eventType, DateTime fromDay, DateTime toDay)
/*     */   {
/* 154 */     return getUsage(accountName, eventType, MeterType.docFragmentsMeter.name(), fromDay, toDay, null, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getOverageDocumentFragments(String accountName, String eventType, DateTime fromDay, DateTime toDay)
/*     */   {
/* 160 */     return getUsage(accountName, eventType, MeterType.docFragmentsMeter.name(), fromDay, toDay, null, true);
/*     */   }
/*     */   
/*     */   private static String checkEventAndMeterType(String eventType, String meterType) {
/* 164 */     MeterType type = MeterType.valueOf(meterType);
/* 165 */     if (type == MeterType.maxSearchesMeter)
/*     */     {
/*     */ 
/* 168 */       return "";
/*     */     }
/*     */     
/* 171 */     Preconditions.checkNotNull(eventType, "Event type cannot be null");
/* 172 */     Preconditions.checkNotNull(meterType, "Meter type cannot be null");
/* 173 */     return eventType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   long getUsage(String accountName, String eventType, String meterType, DateTime fromDay, DateTime toDay, @Nullable Integer hourOfDay, boolean excess)
/*     */   {
/* 190 */     Preconditions.checkNotNull(accountName, "Account name cannot be null");
/* 191 */     eventType = checkEventAndMeterType(eventType, meterType);
/* 192 */     Preconditions.checkNotNull(fromDay, "From day cannot be null");
/* 193 */     Preconditions.checkNotNull(toDay, "To day cannot be null");
/*     */     
/* 195 */     String sumField = "countTotalSum";
/*     */     
/* 197 */     if (excess) {
/* 198 */       sumField = "overageTotalSum";
/*     */     }
/*     */     
/* 201 */     if (log.isDebugEnabled()) {
/* 202 */       log.debug("Querying usage for account [{}] event type [{}] meter type [{}] for date range [{} : {}]", new Object[] { accountName, eventType, meterType, fromDay, toDay });
/*     */     }
/*     */     try
/*     */     {
/* 206 */       FilterBuilder accountFilter = FilterBuilders.termFilter("accountName", accountName);
/* 207 */       FilterBuilder meterTypeFilter = FilterBuilders.termFilter("meterType", meterType);
/* 208 */       FilterBuilder timeFilter = FilterBuilders.rangeFilter("day").from(fromDay).to(toDay).includeLower(true).includeUpper(true);
/*     */       
/*     */ 
/* 211 */       BoolFilterBuilder filter = FilterBuilders.boolFilter().must(accountFilter).must(meterTypeFilter).must(timeFilter);
/*     */       
/* 213 */       FilterBuilder eventTypeFilter = FilterBuilders.termFilter("eventType", eventType);
/* 214 */       filter = filter.must(eventTypeFilter);
/*     */       
/* 216 */       if (hourOfDay != null) {
/* 217 */         FilterBuilder hourOfDayFilter = FilterBuilders.termFilter("hour", hourOfDay);
/* 218 */         filter = filter.must(hourOfDayFilter);
/*     */       }
/*     */       
/* 221 */       FilteredQueryBuilder finalQueryBuilder = QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), filter);
/*     */       
/*     */ 
/* 224 */       SearchResponse response = (SearchResponse)this.clientProvider.getAdminClient().prepareSearch(new String[] { "appdynamics_meters" }).setSize(0).setSearchType(SearchType.COUNT).setQuery(finalQueryBuilder).addAggregation(new SumBuilder(sumField).field(excess ? "overageTotal" : "countTotal")).execute().actionGet(this.usageTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 233 */       if (response.status() != RestStatus.OK) {
/* 234 */         throw new RuntimeException(response.toString());
/*     */       }
/*     */       
/* 237 */       Aggregation aggregation = response.getAggregations().get(sumField);
/* 238 */       Sum sum = (Sum)aggregation;
/* 239 */       double usage = sum.getValue();
/* 240 */       return usage;
/*     */     } catch (RuntimeException e) {
/* 242 */       String msg = String.format("Error occurred while retrieving usage for account [%s] for date range [%s : %s]", new Object[] { accountName, fromDay, toDay });
/*     */       
/* 244 */       throw new RuntimeException(msg, e);
/*     */     }
/*     */   }
/*     */   
/*     */   String generateQueryId(DateTime fromDay, DateTime toDay) {
/* 249 */     return this.uniqueQueryIdPrefix + '[' + fromDay + " : " + toDay + ']';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void submitMeters(Map<? extends MeterKey, MeterReapResult> reapedUsageBytes)
/*     */   {
/* 261 */     int reapCount = reapedUsageBytes.size();
/* 262 */     log.debug("Received [{}] reaped meters for submission", Integer.valueOf(reapCount));
/*     */     
/* 264 */     StringBuilder reusableSb = new StringBuilder();
/* 265 */     boolean debug = log.isDebugEnabled();
/*     */     
/* 267 */     Client client = this.clientProvider.getAdminClient();
/* 268 */     BulkRequestBuilder builder = client.prepareBulk();
/* 269 */     Iterator<? extends Map.Entry<? extends MeterKey, MeterReapResult>> it = reapedUsageBytes.entrySet().iterator();
/* 270 */     while (it.hasNext()) {
/* 271 */       Map.Entry<? extends MeterKey, MeterReapResult> entry = (Map.Entry)it.next();
/* 272 */       MeterKey key = (MeterKey)entry.getKey();
/* 273 */       if (key.isPersistable())
/*     */       {
/*     */ 
/*     */ 
/* 277 */         DateTime dt = TimeKeeper.utcTime(key.getTimeWindowStart());
/* 278 */         int hourOfDay = dt.getHourOfDay();
/* 279 */         String day = dt.withTimeAtStartOfDay().toDateTimeISO().toString();
/*     */         
/* 281 */         MeterReapResult meterCount = (MeterReapResult)entry.getValue();
/* 282 */         String eventType = key.getEventType();
/* 283 */         String meterType = key.getMeterType();
/* 284 */         eventType = checkEventAndMeterType(eventType, meterType);
/* 285 */         String docId = makeDocId(key.getAccountName(), eventType, meterType, day, hourOfDay);
/* 286 */         String insertDoc = makeInsertDoc(reusableSb, key.getAccountName(), eventType, meterType, day, hourOfDay, meterCount);
/*     */         
/*     */ 
/* 289 */         if (debug) {
/* 290 */           log.debug("Bulk request item [{}] and doc id [{}]", insertDoc, docId);
/*     */         }
/*     */         
/* 293 */         if (meterCount.getUsage() > 0L) {
/* 294 */           builder.add(updateRequest(client, insertDoc, docId, meterCount, false));
/*     */         }
/* 296 */         if (meterCount.getExcess() > 0L) {
/* 297 */           builder.add(updateRequest(client, insertDoc, docId, meterCount, true));
/*     */         }
/* 299 */         it.remove();
/*     */       }
/*     */     }
/*     */     try {
/* 303 */       BulkResponse response = (BulkResponse)builder.execute().actionGet(30000L, TimeUnit.MILLISECONDS);
/* 304 */       if (response.hasFailures()) {
/* 305 */         throw new RuntimeException("Error response [\n" + response.buildFailureMessage() + "\n]");
/*     */       }
/*     */     } catch (ElasticsearchException e) {
/* 308 */       Throwables.propagate(e);
/*     */     }
/*     */     
/* 311 */     log.debug("Submitted [{}] reaped meters", Integer.valueOf(reapCount));
/*     */   }
/*     */   
/*     */   private UpdateRequest updateRequest(Client client, String insertDoc, String docId, MeterReapResult meterCount, boolean isExcess)
/*     */   {
/* 316 */     return (UpdateRequest)client.prepareUpdate("appdynamics_meters", "bytes", docId).setUpsert(insertDoc).setScript(MeterStoreUpsertFactory.class.getSimpleName(), ScriptService.ScriptType.INLINE).setScriptLang("native").addScriptParam("countNow", Long.valueOf(isExcess ? 0L : meterCount.getUsage())).addScriptParam("overageNow", Long.valueOf(isExcess ? meterCount.getExcess() : 0L)).setRetryOnConflict(3).request();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String makeDocId(String accountName, String eventType, String meterType, String day, int hourOfDay)
/*     */   {
/* 331 */     return Hashing.sha1().newHasher().putString(accountName, Charsets.UTF_8).putString(eventType, Charsets.UTF_8).putString(meterType, Charsets.UTF_8).putString(day, Charsets.UTF_8).putInt(hourOfDay).hash().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String makeInsertDoc(StringBuilder sb, String accountName, String eventType, String meterType, String day, int hourOfDay, MeterReapResult reap)
/*     */   {
/* 342 */     sb.setLength(0);
/* 343 */     sb.append("{\n").append("  \"accountName\": \"").append(accountName).append("\",\n").append("  \"eventType\": \"").append(eventType).append("\",\n").append("  \"meterType\": \"").append(meterType).append("\",\n").append("  \"day\": \"").append(day).append("\",\n").append("  \"hour\": ").append(hourOfDay).append(",\n").append("  \"countTotal\": ").append(reap.getUsage()).append(",\n").append("  \"overageTotal\": ").append(reap.getExcess()).append("\n").append("}");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 354 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static enum MeterType
/*     */   {
/* 359 */     bytesMeter, 
/* 360 */     docFragmentsMeter, 
/* 361 */     maxSearchesMeter;
/*     */     
/*     */     private MeterType() {}
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/MetersStore.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */