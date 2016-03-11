/*     */ package com.appdynamics.analytics.processor.event.upsert;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.exception.ElasticSearchExceptionUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.scripts.MergeUpsertScript;
/*     */ import com.appdynamics.analytics.processor.event.ElasticSearchMetaService;
/*     */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailure;
/*     */ import com.appdynamics.analytics.processor.event.exception.BulkFailureException;
/*     */ import com.appdynamics.common.io.payload.Bytes;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.appdynamics.common.util.exception.TransientException;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Charsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ import org.elasticsearch.ElasticsearchException;
/*     */ import org.elasticsearch.action.bulk.BulkItemResponse;
/*     */ import org.elasticsearch.action.bulk.BulkItemResponse.Failure;
/*     */ import org.elasticsearch.action.bulk.BulkRequestBuilder;
/*     */ import org.elasticsearch.action.bulk.BulkResponse;
/*     */ import org.elasticsearch.action.get.GetResponse;
/*     */ import org.elasticsearch.action.get.MultiGetItemResponse;
/*     */ import org.elasticsearch.action.get.MultiGetRequest.Item;
/*     */ import org.elasticsearch.action.get.MultiGetRequestBuilder;
/*     */ import org.elasticsearch.action.get.MultiGetResponse;
/*     */ import org.elasticsearch.action.get.MultiGetResponse.Failure;
/*     */ import org.elasticsearch.action.index.IndexRequest;
/*     */ import org.elasticsearch.action.update.UpdateRequest;
/*     */ import org.elasticsearch.action.update.UpdateRequestBuilder;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.common.bytes.BytesArray;
/*     */ import org.elasticsearch.common.bytes.BytesReference;
/*     */ import org.elasticsearch.common.bytes.ChannelBufferBytesReference;
/*     */ import org.elasticsearch.common.netty.buffer.ChannelBuffer;
/*     */ import org.elasticsearch.common.netty.buffer.ChannelBuffers;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.rest.RestStatus;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ @ThreadSafe
/*     */ public class ElasticSearchUpsertProcessor
/*     */ {
/*  66 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchUpsertProcessor.class);
/*     */   
/*  68 */   private static final String MERGE_SCRIPT_NAME = MergeUpsertScript.class.getSimpleName();
/*     */   
/*     */ 
/*  71 */   private static final byte[] UPSERT_FRAG_COMMA_ACCOUNT_NAME = ", \"accountName\": \"".getBytes(Charsets.UTF_8);
/*     */   
/*  73 */   private static final byte[] UPSERT_FRAG_COMMA_EVENT_TIMESTAMP = ", \"eventTimestamp\": \"".getBytes(Charsets.UTF_8);
/*     */   
/*  75 */   private static final byte[] UPSERT_FRAG_COMMA_PICKUP_TIMESTAMP = ", \"pickupTimestamp\": \"".getBytes(Charsets.UTF_8);
/*     */   
/*     */ 
/*  78 */   private static final byte[] UPSERT_FRAG_DOUBLE_QUOTES = "\"".getBytes(Charsets.UTF_8);
/*  79 */   private static final byte[] UPSERT_FRAG_CLOSE_BRACE = "}".getBytes(Charsets.UTF_8);
/*     */   
/*  81 */   private static final byte[] UPSERT_FRAG_APPLIED_FIELDS_START_WITH_COMMA = ", \"_appliedUpdates\": [\"".getBytes(Charsets.UTF_8);
/*     */   
/*  83 */   private static final byte[] UPSERT_FRAG_APPLIED_FIELDS_END = "\"]".getBytes(Charsets.UTF_8);
/*     */   
/*  85 */   private static ThreadLocal<LinkedHashMap<UpsertTaskGroup, UpsertTaskGroup>> TLS_UPSERT_GROUP_MAPS = new ThreadLocal()
/*     */   {
/*     */     protected LinkedHashMap<ElasticSearchUpsertProcessor.UpsertTaskGroup, ElasticSearchUpsertProcessor.UpsertTaskGroup> initialValue()
/*     */     {
/*  89 */       return new LinkedHashMap();
/*     */     }
/*     */   };
/*     */   final ElasticSearchMetaService metaService;
/*     */   final ClientProvider clientProvider;
/*     */   final IndexNameResolver indexNameResolver;
/*     */   final TimeValue callTimeout;
/*     */   final int retriesOnConflict;
/*     */   
/*     */   public ElasticSearchUpsertProcessor(ElasticSearchMetaService metaService, TimeValue callTimeout, int retriesOnConflict)
/*     */   {
/* 100 */     this.metaService = metaService;
/* 101 */     this.clientProvider = metaService.getClientProvider();
/* 102 */     this.indexNameResolver = metaService.getIndexNameResolver();
/* 103 */     this.callTimeout = callTimeout;
/* 104 */     this.retriesOnConflict = retriesOnConflict;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void process(int requestVersion, List<? extends Upsert> upserts)
/*     */   {
/* 114 */     log.debug("About to regroup [{}] upserts", Integer.valueOf(upserts.size()));
/*     */     
/*     */ 
/*     */ 
/* 118 */     LinkedHashMap<UpsertTaskGroup, UpsertTaskGroup> taskGroups = (LinkedHashMap)TLS_UPSERT_GROUP_MAPS.get();
/*     */     try {
/* 120 */       groupTasksAndCollectInfo(requestVersion, upserts, taskGroups);
/* 121 */       log.debug("Upserts are now in [{}] groups and about to lookup existing indices", Integer.valueOf(taskGroups.size()));
/* 122 */       fillIndexNameWhereDocExists(taskGroups);
/* 123 */       log.debug("About to perform actual upserts");
/* 124 */       upsert(taskGroups);
/* 125 */       log.debug("Completed upserts");
/*     */     }
/*     */     finally
/*     */     {
/* 129 */       if (taskGroups.size() > 0) {
/* 130 */         taskGroups.clear();
/*     */       }
/*     */     }
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
/*     */   void groupTasksAndCollectInfo(int requestVersion, List<? extends Upsert> upserts, LinkedHashMap<UpsertTaskGroup, UpsertTaskGroup> taskGroups)
/*     */   {
/* 145 */     String accountNameRaw = null;
/* 146 */     String eventTypeRaw = null;
/* 147 */     String accountNameAdjusted = null;
/* 148 */     String eventTypeAdjusted = null;
/* 149 */     UpsertTaskGroup group = null;
/* 150 */     for (Upsert upsert : upserts) {
/* 151 */       UpsertTask upsertTask = new UpsertTask(upsert);
/*     */       
/*     */ 
/* 154 */       if ((accountNameRaw == null) || (eventTypeRaw == null) || (!accountNameRaw.equals(upsert.getAccountName())) || (!eventTypeRaw.equals(upsert.getEventType())))
/*     */       {
/* 156 */         accountNameRaw = upsert.getAccountName();
/* 157 */         accountNameAdjusted = AccountConfiguration.normalizeAccountName(accountNameRaw);
/* 158 */         eventTypeRaw = upsert.getEventType();
/* 159 */         eventTypeAdjusted = this.indexNameResolver.resolveEventType(eventTypeRaw);
/* 160 */         this.metaService.verifyEventTypeRepairingIndicesIfNecessary(requestVersion, accountNameAdjusted, eventTypeAdjusted);
/*     */         
/* 162 */         group = null;
/*     */       }
/* 164 */       if (group == null)
/*     */       {
/* 166 */         UpsertTaskGroup tempGroup = new UpsertTaskGroup(accountNameAdjusted, eventTypeAdjusted);
/*     */         
/* 168 */         group = (UpsertTaskGroup)taskGroups.get(tempGroup);
/*     */         
/* 170 */         if (group == null)
/*     */         {
/* 172 */           group = tempGroup;
/* 173 */           group.docType = this.indexNameResolver.getDocType(accountNameAdjusted, eventTypeAdjusted);
/*     */           
/*     */ 
/* 176 */           group.defaultWriteIndexName = this.indexNameResolver.resolveInsertAlias(accountNameAdjusted, eventTypeAdjusted);
/*     */           
/* 178 */           group.searchIndexNames = this.metaService.getSearchIndexNamesForUpsert(accountNameAdjusted, eventTypeAdjusted);
/*     */           
/*     */ 
/*     */ 
/* 182 */           taskGroups.put(group, group);
/*     */         }
/*     */       }
/*     */       
/* 186 */       upsertTask.perIdxMergeReqId = UpsertCodec.makeBatchRequestId(upsert);
/* 187 */       upsertTask.mergeFields = UpsertCodec.splitCsvMergeFields(upsert.getCsvMergeFields());
/*     */       
/* 189 */       group.add(upsertTask);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void fillIndexNameWhereDocExists(LinkedHashMap<UpsertTaskGroup, UpsertTaskGroup> taskGroups)
/*     */   {
/* 201 */     boolean debug = log.isDebugEnabled();
/*     */     
/*     */ 
/* 204 */     for (UpsertTaskGroup taskGroup : taskGroups.keySet()) {
/* 205 */       int taskSize = taskGroup.tasks.size();
/* 206 */       int searchIndicesSize = taskGroup.searchIndexNames.size();
/* 207 */       if (debug) {
/* 208 */         log.debug("Searching for pre-existing documents for group with account name [{}] and event type [{}]. There are [{}] documents to look for and [{}] indices to search in", new Object[] { taskGroup.adjustedAccountName, taskGroup.adjustedEventType, Integer.valueOf(taskSize), Integer.valueOf(searchIndicesSize) });
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 213 */       MultiGetRequestBuilder builder = new MultiGetRequestBuilder(this.clientProvider.getSearchClient(taskGroup.adjustedAccountName));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 219 */       int numMultiGet = 0;
/* 220 */       for (int i = 0; i < searchIndicesSize; i++) {
/* 221 */         String index = (String)taskGroup.searchIndexNames.get(i);
/*     */         
/* 223 */         for (int j = 0; j < taskSize; j++) {
/* 224 */           UpsertTask task = (UpsertTask)taskGroup.tasks.get(j);
/* 225 */           String docId = task.upsert.getCorrelationId();
/* 226 */           MultiGetRequest.Item item = new MultiGetRequest.Item(index, taskGroup.docType, docId).fields(new String[] { "_id" });
/*     */           
/*     */ 
/* 229 */           builder.add(item);
/* 230 */           numMultiGet++;
/*     */         }
/*     */       }
/*     */       
/* 234 */       log.debug("About to make [{}] bulk gets to find pre-existing documents", Integer.valueOf(numMultiGet));
/*     */       MultiGetResponse multiGetResponse;
/*     */       try
/*     */       {
/* 238 */         multiGetResponse = (MultiGetResponse)builder.get(this.callTimeout);
/*     */       } catch (ElasticsearchException e) {
/* 240 */         throw new TransientException("Error occurred while searching for existing documents for group with account name [" + taskGroup.adjustedAccountName + "] and event type" + " [" + taskGroup.adjustedEventType + "]. " + e.getMessage(), e);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 246 */       MultiGetItemResponse[] itemResponses = multiGetResponse.getResponses();
/*     */       
/* 248 */       for (int i = 0; i < itemResponses.length; i++) {
/* 249 */         MultiGetItemResponse itemResponse = itemResponses[i];
/* 250 */         UpsertTask task = (UpsertTask)taskGroup.tasks.get(i % taskSize);
/* 251 */         if (!itemResponse.getId().equals(task.upsert.getCorrelationId()))
/*     */         {
/* 253 */           log.error("The search response for document [{}] got mixed up with document [{}]", task.upsert.getCorrelationId(), itemResponse.getId());
/*     */         }
/*     */         
/* 256 */         if (itemResponse.isFailed())
/*     */         {
/* 258 */           throw new TransientException("Search request for document " + task.upsert.getCorrelationId() + " failed on index " + itemResponse.getIndex() + " with " + itemResponse.getFailure().getMessage());
/*     */         }
/*     */         
/* 261 */         if (itemResponse.getResponse().isExists()) {
/* 262 */           String index = itemResponse.getResponse().getIndex();
/*     */           
/* 264 */           if (index != null)
/*     */           {
/*     */ 
/* 267 */             if (task.indexNameWhereDocExists != null) {
/* 268 */               log.error("Document [{}] is in [{}] index but was also found in [{}]", new Object[] { task.upsert.getCorrelationId(), task.indexNameWhereDocExists, index });
/*     */             }
/* 270 */             else if (debug) {
/* 271 */               log.debug("Document [{}] is in [{}] index", task.upsert.getCorrelationId(), index);
/*     */             }
/*     */             
/* 274 */             task.indexNameWhereDocExists = index;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
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
/*     */   void upsert(LinkedHashMap<UpsertTaskGroup, UpsertTaskGroup> taskGroups)
/*     */   {
/* 292 */     List<BulkFailure> bulkFailures = new ArrayList();
/*     */     
/* 294 */     Iterator<Map.Entry<UpsertTaskGroup, UpsertTaskGroup>> taskGroupIter = taskGroups.entrySet().iterator();
/* 295 */     while (taskGroupIter.hasNext()) {
/* 296 */       UpsertTaskGroup taskGroup = (UpsertTaskGroup)((Map.Entry)taskGroupIter.next()).getKey();
/* 297 */       Client insertClient = this.clientProvider.getInsertClient(taskGroup.adjustedAccountName);
/* 298 */       List<UpdateRequest> allUpdateRequests = new ArrayList();
/* 299 */       int size = taskGroup.tasks.size();
/*     */       
/* 301 */       for (int j = 0; j < size; j++) {
/* 302 */         UpsertTask task = (UpsertTask)taskGroup.tasks.get(j);
/* 303 */         Upsert upsert = task.upsert;
/* 304 */         BytesReference finalBytes = smartUpdateJsonBytes(task);
/*     */         
/* 306 */         UpdateRequestBuilder requestBuilder = ((UpdateRequestBuilder)insertClient.prepareUpdate().setIndex(task.indexNameWhereDocExists == null ? taskGroup.defaultWriteIndexName : task.indexNameWhereDocExists)).setId(upsert.getCorrelationId()).setType(taskGroup.docType).setScript(MERGE_SCRIPT_NAME, MergeUpsertScript.SCRIPT_TYPE).setScriptLang("native");
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
/* 317 */         addScriptParams(requestBuilder, task.mergeFields, finalBytes, task.perIdxMergeReqId);
/* 318 */         if (task.indexNameWhereDocExists == null)
/*     */         {
/*     */ 
/* 321 */           requestBuilder.setUpsert(new IndexRequest().source(finalBytes));
/*     */         }
/* 323 */         UpdateRequest request = (UpdateRequest)requestBuilder.setRetryOnConflict(this.retriesOnConflict).request();
/* 324 */         allUpdateRequests.add(request);
/*     */       }
/* 326 */       taskGroupIter.remove();
/*     */       
/*     */ 
/* 329 */       List<UpdateRequest> updateRequests = allUpdateRequests;
/* 330 */       LinkedList<Client> clients = new LinkedList(this.clientProvider.getAllInsertClients(taskGroup.adjustedAccountName));
/*     */       
/*     */       BulkResponse bulkResponse;
/*     */       
/*     */       do
/*     */       {
/* 336 */         bulkResponse = executeBulkUpdateRequest(taskGroup, updateRequests, (Client)clients.removeFirst(), bulkFailures);
/*     */         
/*     */ 
/* 339 */         if (bulkResponse == null) {
/*     */           break;
/*     */         }
/* 342 */         updateRequests = getFailedDueToIndexMissing(bulkResponse, allUpdateRequests, bulkFailures);
/* 343 */       } while ((bulkResponse.hasFailures()) && (updateRequests.size() > 0) && (clients.size() > 0));
/*     */     }
/*     */     
/* 346 */     if (!bulkFailures.isEmpty()) {
/* 347 */       BulkFailureException bulkFailureException = new BulkFailureException("Encountered bulk upsert failures");
/* 348 */       bulkFailureException.getFailures().addAll(bulkFailures);
/* 349 */       throw bulkFailureException;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   private BulkResponse executeBulkUpdateRequest(UpsertTaskGroup taskGroup, List<UpdateRequest> allUpdateRequests, Client client, List<BulkFailure> bulkFailures)
/*     */   {
/*     */     String status;
/*     */     
/*     */ 
/*     */     int i;
/*     */     
/*     */ 
/*     */     String clusterName;
/*     */     
/*     */     try
/*     */     {
/* 368 */       BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
/* 369 */       for (UpdateRequest request : allUpdateRequests) {
/* 370 */         bulkRequestBuilder.add(request);
/*     */       }
/* 372 */       return (BulkResponse)bulkRequestBuilder.get(this.callTimeout);
/*     */     } catch (Exception e) {
/*     */       String status;
/* 375 */       if ((e instanceof ElasticsearchException)) {
/* 376 */         status = ((ElasticsearchException)e).status().toString();
/*     */       } else {
/* 378 */         status = RestStatus.INTERNAL_SERVER_ERROR.toString();
/*     */       }
/*     */       
/* 381 */       i = 0;
/* 382 */       clusterName = ESUtils.getClusterName(client);
/*     */       
/* 384 */       for (UpsertTask task : taskGroup.tasks) {
/* 385 */         Upsert upsert = task.upsert;
/* 386 */         Bytes bytes = upsert.getBytes();
/* 387 */         String json = new String(bytes.getArray(), bytes.getOffset(), bytes.getLength(), Charsets.UTF_8);
/* 388 */         log.error("Event Service bulk upsert error on cluster [{}], with account [{}], eventType [{}], item position [{}], item [\n{}\n]", new Object[] { clusterName, taskGroup.adjustedAccountName, taskGroup.adjustedEventType, Integer.valueOf(i++), json, e });
/*     */         
/*     */ 
/*     */ 
/* 392 */         BulkFailure bulkFailure = new BulkFailure(status, true, i, upsert.getCorrelationId(), e.getMessage());
/* 393 */         bulkFailures.add(bulkFailure);
/* 394 */         i++;
/*     */       }
/*     */     }
/* 397 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<UpdateRequest> getFailedDueToIndexMissing(BulkResponse bulkResponse, List<UpdateRequest> allUpdateRequests, List<BulkFailure> bulkFailures)
/*     */   {
/* 418 */     List<UpdateRequest> updateRequestsToRetry = new ArrayList();
/* 419 */     Set<String> failedIds = new HashSet();
/* 420 */     for (BulkItemResponse responseItem : bulkResponse.getItems()) {
/* 421 */       if (responseItem.isFailed())
/*     */       {
/*     */ 
/* 424 */         if ((responseItem.getFailure().getStatus() == RestStatus.NOT_FOUND) && (responseItem.getFailureMessage().contains("IndexMissingException")))
/*     */         {
/* 426 */           failedIds.add(responseItem.getId());
/*     */         }
/*     */         else
/*     */         {
/* 430 */           BulkFailure bulkFailure = ElasticSearchExceptionUtils.getBulkFailure(responseItem);
/* 431 */           bulkFailures.add(bulkFailure);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 436 */     for (UpdateRequest updateRequest : allUpdateRequests) {
/* 437 */       if (failedIds.contains(updateRequest.id())) {
/* 438 */         updateRequestsToRetry.add(updateRequest);
/*     */       }
/*     */     }
/* 441 */     return updateRequestsToRetry;
/*     */   }
/*     */   
/*     */ 
/*     */   private static UpdateRequestBuilder addScriptParams(UpdateRequestBuilder requestBuilder, List<String> mergeFields, BytesReference finalBytes, String perIdxMergeReqId)
/*     */   {
/* 447 */     return requestBuilder.addScriptParam("mergeFields", mergeFields).addScriptParam("doc", finalBytes).addScriptParam("reqId", perIdxMergeReqId);
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
/*     */   private static BytesReference smartUpdateJsonBytes(UpsertTask task)
/*     */   {
/* 464 */     Upsert upsert = task.upsert;
/* 465 */     Bytes bytes = upsert.getBytes();
/*     */     
/*     */ 
/* 468 */     int count = 1;
/*     */     
/* 470 */     count += 3;
/*     */     
/* 472 */     if (!upsert.isAccountNameInBytes())
/*     */     {
/* 474 */       count += 3;
/*     */     }
/* 476 */     if (!upsert.isEventTimestampInBytes())
/*     */     {
/* 478 */       count += 3;
/*     */     }
/* 480 */     if (!upsert.isPickupTimestampInBytes())
/*     */     {
/* 482 */       count += 3;
/*     */     }
/*     */     
/* 485 */     if (count == 1)
/*     */     {
/* 487 */       return new BytesArray(bytes.getArray(), bytes.getOffset(), bytes.getLength());
/*     */     }
/*     */     
/* 490 */     count++;
/* 491 */     ChannelBuffer[] originalJsonPlusExtra = new ChannelBuffer[count];
/*     */     
/*     */ 
/* 494 */     count = 0;
/*     */     
/* 496 */     originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(bytes.getArray(), bytes.getOffset(), bytes.getLength() - 1);
/* 497 */     originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_APPLIED_FIELDS_START_WITH_COMMA);
/* 498 */     originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(task.perIdxMergeReqId.getBytes(Charsets.UTF_8));
/* 499 */     originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_APPLIED_FIELDS_END);
/* 500 */     if (!upsert.isAccountNameInBytes()) {
/* 501 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_COMMA_ACCOUNT_NAME);
/* 502 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(upsert.getAccountName().getBytes(Charsets.UTF_8));
/* 503 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_DOUBLE_QUOTES);
/*     */     }
/*     */     
/* 506 */     String timestamp = null;
/* 507 */     if (!upsert.isEventTimestampInBytes()) {
/* 508 */       timestamp = TimeKeeper.currentUtcTimeIso8601();
/* 509 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_COMMA_EVENT_TIMESTAMP);
/* 510 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(timestamp.getBytes(Charsets.UTF_8));
/* 511 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_DOUBLE_QUOTES);
/*     */     }
/*     */     
/* 514 */     if (!upsert.isPickupTimestampInBytes()) {
/* 515 */       if (timestamp == null) {
/* 516 */         timestamp = TimeKeeper.currentUtcTimeIso8601();
/*     */       }
/* 518 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_COMMA_PICKUP_TIMESTAMP);
/* 519 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(timestamp.getBytes(Charsets.UTF_8));
/* 520 */       originalJsonPlusExtra[(count++)] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_DOUBLE_QUOTES);
/*     */     }
/*     */     
/* 523 */     originalJsonPlusExtra[count] = ChannelBuffers.wrappedBuffer(UPSERT_FRAG_CLOSE_BRACE);
/*     */     
/* 525 */     return new ChannelBufferBytesReference(ChannelBuffers.wrappedBuffer(originalJsonPlusExtra));
/*     */   }
/*     */   
/*     */   static class UpsertTaskGroup { final String adjustedAccountName;
/*     */     final String adjustedEventType;
/*     */     String docType;
/*     */     
/* 532 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof UpsertTaskGroup)) return false; UpsertTaskGroup other = (UpsertTaskGroup)o; if (!other.canEqual(this)) return false; Object this$adjustedAccountName = this.adjustedAccountName;Object other$adjustedAccountName = other.adjustedAccountName; if (this$adjustedAccountName == null ? other$adjustedAccountName != null : !this$adjustedAccountName.equals(other$adjustedAccountName)) return false; Object this$adjustedEventType = this.adjustedEventType;Object other$adjustedEventType = other.adjustedEventType;return this$adjustedEventType == null ? other$adjustedEventType == null : this$adjustedEventType.equals(other$adjustedEventType); } public boolean canEqual(Object other) { return other instanceof UpsertTaskGroup; } public int hashCode() { int PRIME = 31;int result = 1;Object $adjustedAccountName = this.adjustedAccountName;result = result * 31 + ($adjustedAccountName == null ? 0 : $adjustedAccountName.hashCode());Object $adjustedEventType = this.adjustedEventType;result = result * 31 + ($adjustedEventType == null ? 0 : $adjustedEventType.hashCode());return result;
/*     */     }
/*     */     
/*     */ 
/*     */     String defaultWriteIndexName;
/*     */     
/*     */     List<String> searchIndexNames;
/*     */     List<ElasticSearchUpsertProcessor.UpsertTask> tasks;
/*     */     @VisibleForTesting
/*     */     UpsertTaskGroup(String adjustedAccountName, String adjustedEventType)
/*     */     {
/* 543 */       this.adjustedAccountName = adjustedAccountName;
/* 544 */       this.adjustedEventType = adjustedEventType;
/*     */     }
/*     */     
/*     */     void add(ElasticSearchUpsertProcessor.UpsertTask task) {
/* 548 */       if (this.tasks == null) {
/* 549 */         this.tasks = new ArrayList(8);
/*     */       }
/* 551 */       this.tasks.add(task);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static class UpsertTask
/*     */   {
/*     */     final Upsert upsert;
/*     */     
/*     */     String indexNameWhereDocExists;
/*     */     String perIdxMergeReqId;
/*     */     List<String> mergeFields;
/*     */     
/*     */     @VisibleForTesting
/*     */     UpsertTask(Upsert upsert)
/*     */     {
/* 567 */       this.upsert = upsert;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/upsert/ElasticSearchUpsertProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */