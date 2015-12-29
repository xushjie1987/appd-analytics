/*     */ package com.appdynamics.analytics.processor.event.upsert;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.scripts.MergeUpsertScript;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.scripts.MergeUpsertScriptFactory;
/*     */ import com.appdynamics.common.io.codec.Json;
/*     */ import com.appdynamics.common.io.codec.Json.ScanResult;
/*     */ import com.appdynamics.common.io.codec.Json.Walker;
/*     */ import com.appdynamics.common.io.payload.Bytes;
/*     */ import com.appdynamics.common.util.misc.ThreadLocalObjects;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.common.hash.Hashing;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.elasticsearch.common.bytes.BytesArray;
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
/*     */ public abstract class UpsertCodec
/*     */ {
/*  45 */   private static final Logger log = LoggerFactory.getLogger(UpsertCodec.class);
/*     */   
/*     */ 
/*     */   static final String UPSERT_KEY_ELEMENT_SEPARATOR = "±";
/*     */   
/*     */   static final String UPSERT_CSV_MERGE_FIELD_SEPARATOR = ",";
/*     */   
/*  52 */   private static final Splitter UPSERT_KEY_SPLITTER = Splitter.on("±");
/*  53 */   private static final Splitter UPSERT_KEY_CSV_MERGE_FIELD_SPLITTER = Splitter.on(",").trimResults();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String validateUpsertKeyElement(String element)
/*     */   {
/*  65 */     Preconditions.checkArgument((element != null) && (element.length() > 0), "Upsert 'key' elements cannot be null/empty");
/*     */     
/*  67 */     if (element.contains("±")) {
/*  68 */       throw new IllegalArgumentException("The upsert 'key' element [" + element + "]" + " contains an illegal character [" + "±" + "]");
/*     */     }
/*     */     
/*  71 */     return element;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int validateUpsertKeyElement(int i)
/*     */   {
/*  79 */     Preconditions.checkArgument(i >= 0, "Upsert 'key' elements cannot be negative");
/*  80 */     return i;
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
/*     */   public static byte[] encodeKey(Upsert upsert)
/*     */   {
/* 113 */     StringBuilder sb = ThreadLocalObjects.stringBuilder();
/*     */     try {
/* 115 */       sb.append(validateUpsertKeyElement(upsert.getAccountName())).append("±").append(validateUpsertKeyElement(upsert.getEventType())).append("±").append(validateUpsertKeyElement(upsert.getBatchId())).append("±").append(validateUpsertKeyElement(upsert.getBatchPosition())).append("±").append(Strings.nullToEmpty(upsert.getCsvMergeFields())).append("±").append(validateUpsertKeyElement(upsert.getCorrelationIdField())).append("±").append(validateUpsertKeyElement(upsert.getCorrelationId())).append("±").append(upsert.isAccountNameInBytes()).append("±").append(upsert.isEventTimestampInBytes()).append("±").append(upsert.isPickupTimestampInBytes());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */       return sb.toString().getBytes(Charsets.UTF_8);
/*     */     } finally {
/* 139 */       sb.setLength(0);
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
/*     */ 
/*     */   public static MutableUpsert decodeKey(Bytes bytes)
/*     */   {
/* 155 */     byte[] array = bytes.getArray();
/* 156 */     Preconditions.checkNotNull(array, "No content is available to decode as an upsert");
/* 157 */     Iterator<String> iterator = UPSERT_KEY_SPLITTER.split(new String(array, bytes.getOffset(), bytes.getLength(), Charsets.UTF_8)).iterator();
/*     */     
/*     */ 
/* 160 */     MutableUpsert upsert = new MutableUpsert();
/* 161 */     upsert.setAccountName((String)iterator.next());
/* 162 */     upsert.setEventType((String)iterator.next());
/* 163 */     upsert.setBatchId((String)iterator.next());
/* 164 */     upsert.setBatchPosition(Integer.parseInt((String)iterator.next()));
/* 165 */     upsert.setCsvMergeFields(Strings.emptyToNull((String)iterator.next()));
/* 166 */     upsert.setCorrelationIdField((String)iterator.next());
/* 167 */     upsert.setCorrelationId((String)iterator.next());
/* 168 */     upsert.setAccountNameInBytes(Boolean.parseBoolean((String)iterator.next()));
/* 169 */     upsert.setEventTimestampInBytes(Boolean.parseBoolean((String)iterator.next()));
/* 170 */     upsert.setPickupTimestampInBytes(Boolean.parseBoolean((String)iterator.next()));
/*     */     
/* 172 */     return upsert;
/*     */   }
/*     */   
/*     */   public static List<String> splitCsvMergeFields(String csvMergeFields) {
/* 176 */     if ((csvMergeFields == null) || (csvMergeFields.isEmpty())) {
/* 177 */       return Collections.emptyList();
/*     */     }
/* 179 */     return UPSERT_KEY_CSV_MERGE_FIELD_SPLITTER.splitToList(csvMergeFields);
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
/*     */   public static List<MutableUpsert> decode(String accountName, String eventType, String idPath, String csvMergeFields, byte[] data, int offset, int length)
/*     */   {
/* 210 */     List<MutableUpsert> upsertItems = new ArrayList(8);
/*     */     
/* 212 */     boolean debug = log.isDebugEnabled();
/* 213 */     Json.Walker walker = ThreadLocalObjects.jsonWalker();
/*     */     try {
/* 215 */       if (debug) {
/* 216 */         log.debug("Attempting to decode JSON array of length [{}] from offset [{}] up to [{}] and extract [{}]", new Object[] { Integer.valueOf(data.length), Integer.valueOf(offset), Integer.valueOf(length), idPath });
/*     */       }
/*     */       
/* 219 */       List<Json.ScanResult> skippedRanges = Json.scanChildrenOfArray(walker, data, offset, length);
/* 220 */       batchId = makeUpsertBatchId(data, offset, length);
/* 221 */       log.debug("Decoded and identified [{}] array elements with group hash [{}]", Integer.valueOf(skippedRanges.size()), batchId);
/*     */       
/*     */ 
/* 224 */       arrayPosition = 0;
/* 225 */       for (iterator = skippedRanges.iterator(); iterator.hasNext();) {
/* 226 */         Json.ScanResult range = (Json.ScanResult)iterator.next();
/* 227 */         int rangeOffset = ((Integer)range.getLeft()).intValue();
/* 228 */         int rangeLength = ((Integer)range.getRight()).intValue();
/*     */         
/* 230 */         if (debug) {
/* 231 */           log.debug("Identified array fragment [{}] with offset [{}] and length [{}] as JSON [\n{}\n]", new Object[] { Integer.valueOf(arrayPosition), Integer.valueOf(rangeOffset), Integer.valueOf(rangeLength), new String(data, rangeOffset, rangeLength, Charsets.UTF_8) });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 236 */         HashMap<String, JsonNode> fieldNamesToFind = new HashMap(8);
/* 237 */         fieldNamesToFind.put(idPath, null);
/* 238 */         fieldNamesToFind.put("accountName", null);
/* 239 */         fieldNamesToFind.put("eventTimestamp", null);
/* 240 */         fieldNamesToFind.put("pickupTimestamp", null);
/* 241 */         Json.readTopLevelFields(walker, data, rangeOffset, rangeLength, fieldNamesToFind);
/*     */         
/* 243 */         JsonNode idNode = (JsonNode)fieldNamesToFind.remove(idPath);
/* 244 */         if (idNode == null) {
/* 245 */           throw new IllegalArgumentException("The id path [" + idPath + "] does not seem to exist in the" + " JSON array item at position [" + arrayPosition + "]");
/*     */         }
/*     */         
/* 248 */         if (debug) {
/* 249 */           log.debug("Extracted id [{}]", idNode);
/*     */         }
/* 251 */         boolean foundAccountName = fieldNamesToFind.remove("accountName") != null;
/* 252 */         boolean foundEventTimestamp = fieldNamesToFind.remove("eventTimestamp") != null;
/* 253 */         boolean foundPickupTimestamp = fieldNamesToFind.remove("pickupTimestamp") != null;
/*     */         
/* 255 */         MutableUpsert upsert = new MutableUpsert();
/* 256 */         upsert.setAccountName(accountName);
/* 257 */         upsert.setEventType(eventType);
/* 258 */         upsert.setBatchId(batchId);
/* 259 */         upsert.setBatchPosition(arrayPosition);
/* 260 */         upsert.setCsvMergeFields(csvMergeFields);
/* 261 */         upsert.setCorrelationIdField(idPath);
/* 262 */         upsert.setCorrelationId(idNode.asText());
/* 263 */         upsert.setAccountNameInBytes(foundAccountName);
/* 264 */         upsert.setEventTimestampInBytes(foundEventTimestamp);
/* 265 */         upsert.setPickupTimestampInBytes(foundPickupTimestamp);
/* 266 */         upsert.setBytes(new Bytes(data, rangeOffset, rangeLength));
/* 267 */         upsertItems.add(upsert);
/*     */         
/* 269 */         arrayPosition++;
/* 270 */         iterator.remove(); } } catch (IOException e) { String batchId;
/*     */       int arrayPosition;
/*     */       Iterator<Json.ScanResult> iterator;
/* 273 */       throw new IllegalArgumentException("Exception occurred while parsing the JSON string", e);
/*     */     } finally {
/* 275 */       walker.endWalk();
/*     */     }
/*     */     
/* 278 */     Preconditions.checkArgument(!upsertItems.isEmpty(), "Upsert elements could not be extracted. It appears to be empty");
/* 279 */     return upsertItems;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String makeUpsertBatchId(byte[] data, int offset, int length)
/*     */   {
/* 290 */     return Hashing.murmur3_32().newHasher().putBytes(data, offset, length).putInt(offset).putInt(length).hash().toString();
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
/*     */   public static String makeBatchRequestId(Upsert upsert)
/*     */   {
/* 306 */     StringBuilder sb = ThreadLocalObjects.stringBuilder();
/* 307 */     Preconditions.checkArgument(sb.length() == 0, "Reusable StringBuilder already has some content");
/*     */     try {
/* 309 */       internalMakeBatchRequestId(upsert.getBatchId(), upsert.getBatchPosition(), sb);
/* 310 */       return sb.toString();
/*     */     } finally {
/* 312 */       sb.setLength(0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String makeSummaryBatchRequestId(Collection<String> appliedRequestIds)
/*     */   {
/* 322 */     StringBuilder sb = ThreadLocalObjects.stringBuilder();
/* 323 */     Preconditions.checkArgument(sb.length() == 0, "Reusable StringBuilder already has some content");
/*     */     try {
/* 325 */       for (String appliedRequestId : appliedRequestIds)
/*     */       {
/* 327 */         sb.append("±");
/* 328 */         internalMakeBatchRequestId(appliedRequestId, 0, sb);
/*     */       }
/* 330 */       return sb.toString();
/*     */     } finally {
/* 332 */       sb.setLength(0);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void internalMakeBatchRequestId(String batchId, int batchPosition, StringBuilder appendTo) {
/* 337 */     appendTo.append(batchId).append('-').append(batchPosition);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void merge(Map<Object, Object> mergeDestination, Upsert upsertToMerge, String... mergeFields)
/*     */   {
/* 347 */     Bytes jsonSourceDoc = upsertToMerge.getBytes();
/*     */     
/* 349 */     MergeUpsertScript script = new MergeUpsertScriptFactory().newScript(ImmutableMap.of("doc", new BytesArray(jsonSourceDoc.getArray(), jsonSourceDoc.getOffset(), jsonSourceDoc.getLength()), "mergeFields", Lists.newArrayList(mergeFields), "reqId", makeBatchRequestId(upsertToMerge)));
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
/* 362 */     script.setNextVar("ctx", ImmutableMap.of("_source", mergeDestination));
/*     */     
/*     */ 
/*     */ 
/* 366 */     script.run();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/upsert/UpsertCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */