/*     */ package com.appdynamics.analytics.processor.elasticsearch.scripts;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.misc.Collections;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import org.elasticsearch.common.bytes.BytesReference;
/*     */ import org.elasticsearch.common.netty.buffer.ChannelBufferInputStream;
/*     */ import org.elasticsearch.script.AbstractExecutableScript;
/*     */ import org.elasticsearch.script.ScriptService.ScriptType;
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
/*     */ 
/*     */ public class MergeUpsertScript
/*     */   extends AbstractExecutableScript
/*     */ {
/*  37 */   private static final Logger log = LoggerFactory.getLogger(MergeUpsertScript.class);
/*     */   
/*     */   public static final String PARAM_MERGE_FIELDS = "mergeFields";
/*     */   
/*     */   public static final String PARAM_DOC = "doc";
/*     */   public static final String PARAM_REQUEST_ID = "reqId";
/*     */   public static final String FIELD_APPLIED_REQUESTS = "_appliedUpdates";
/*     */   public static final String FIELD_CTX = "ctx";
/*     */   public static final String FIELD_SOURCE = "_source";
/*  46 */   public static final ScriptService.ScriptType SCRIPT_TYPE = ScriptService.ScriptType.INLINE;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean debug;
/*     */   
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   private final String reqId;
/*     */   
/*     */ 
/*     */ 
/*     */   @Nullable
/*     */   private final List<String> externallyAppliedIdsInSrcDoc;
/*     */   
/*     */ 
/*     */   private final Set<Object> mergeFields;
/*     */   
/*     */ 
/*     */   private final Map<?, ?> newDocToMerge;
/*     */   
/*     */ 
/*     */   private Map ctx;
/*     */   
/*     */ 
/*     */   private List<String> appliedIdsInDestinationDoc;
/*     */   
/*     */ 
/*     */   private ArrayList<String> idsThatWereAppliedHere;
/*     */   
/*     */ 
/*     */ 
/*     */   public MergeUpsertScript(@Nullable String reqId, Set<Object> mergeFields, Map<?, ?> newDocToMerge, @Nullable Map<Object, Object> docToMergeInto)
/*     */   {
/*  81 */     this.debug = log.isDebugEnabled();
/*  82 */     this.reqId = reqId;
/*  83 */     this.externallyAppliedIdsInSrcDoc = ((List)newDocToMerge.remove("_appliedUpdates"));
/*  84 */     this.newDocToMerge = newDocToMerge;
/*  85 */     this.mergeFields = mergeFields;
/*     */     
/*  87 */     this.ctx = new HashMap(2);
/*  88 */     this.ctx.put("_source", docToMergeInto);
/*     */     
/*  90 */     if (this.debug) {
/*  91 */       if (reqId == null) {
/*  92 */         log.debug("Script parameter [{}] is missing. Update will be forcibly applied", "reqId");
/*     */       }
/*  94 */       if (this.externallyAppliedIdsInSrcDoc != null) {
/*  95 */         log.debug("This appears to be an externally merged upsert composed of {} ids", this.externallyAppliedIdsInSrcDoc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   MergeUpsertScript(Map<String, Object> params)
/*     */   {
/* 102 */     this((String)params.get("reqId"), new HashSet(getList(params, "mergeFields")), getMap(params, "doc"), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Map<?, ?> getMap(Map<String, Object> params, String paramName)
/*     */   {
/* 110 */     Object obj = params.get(paramName);
/*     */     Map<?, ?> map;
/* 112 */     if (obj == null) {
/* 113 */       map = new HashMap();
/*     */ 
/*     */     }
/* 116 */     else if ((obj instanceof BytesReference)) {
/* 117 */       BytesReference bytesRef = (BytesReference)obj;
/*     */       try {
/* 119 */         ChannelBufferInputStream cbis = new ChannelBufferInputStream(bytesRef.toChannelBuffer());Throwable localThrowable2 = null;
/* 120 */         try { map = (Map)Reader.DEFAULT_JSON_MAPPER.readValue(cbis, Map.class);
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/*     */           Map<?, ?> map;
/* 119 */           localThrowable2 = localThrowable1;throw localThrowable1;
/*     */         } finally {
/* 121 */           if (cbis != null) if (localThrowable2 != null) try { cbis.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else cbis.close();
/*     */         }
/* 123 */       } catch (IOException e) { throw new IllegalArgumentException("Script parameter [" + paramName + "] could not be read as a Map", e);
/*     */       }
/*     */     } else { Map<?, ?> map;
/* 126 */       if ((obj instanceof Map)) {
/* 127 */         map = (Map)obj;
/*     */       } else {
/* 129 */         throw new IllegalArgumentException("Script parameter [" + paramName + "] could not be read as a Map. It appears to be a type" + " of [" + obj.getClass().getName() + "]");
/*     */       }
/*     */     }
/*     */     
/*     */     Map<?, ?> map;
/*     */     
/* 135 */     return map;
/*     */   }
/*     */   
/*     */   private static List<?> getList(Map<String, Object> params, String paramName) {
/* 139 */     Object obj = params.get(paramName);
/*     */     List<?> list;
/* 141 */     if (obj == null) {
/* 142 */       list = new ArrayList(4);
/*     */     } else {
/*     */       List<?> list;
/* 145 */       if ((obj instanceof List)) {
/* 146 */         list = (List)obj;
/*     */       } else {
/* 148 */         throw new IllegalArgumentException("Script parameter [" + paramName + "] could not be read as a List. It appears to be a type" + " of [" + obj.getClass().getName() + "]");
/*     */       }
/*     */     }
/*     */     
/*     */     List<?> list;
/*     */     
/* 154 */     return list;
/*     */   }
/*     */   
/*     */   public void setNextVar(String name, Object value)
/*     */   {
/* 159 */     if (this.debug) {
/* 160 */       log.debug("Script variable [{}] has been set to [{}]", name, value);
/*     */     }
/* 162 */     if (("ctx".equals(name)) && (value != null) && ((value instanceof Map))) {
/* 163 */       this.ctx = ((Map)value);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object run()
/*     */   {
/* 169 */     if (!this.ctx.containsKey("_source")) {
/* 170 */       throw new IllegalStateException(MergeUpsertScript.class.getSimpleName() + " does not have access to the [" + "_source" + "] field. Please ensure that it is enabled");
/*     */     }
/*     */     try
/*     */     {
/* 174 */       Map<Object, Object> destinationDoc = (Map)this.ctx.get("_source");
/* 175 */       this.appliedIdsInDestinationDoc = ((List)destinationDoc.get("_appliedUpdates"));
/* 176 */       if (this.appliedIdsInDestinationDoc == null) {
/* 177 */         this.appliedIdsInDestinationDoc = new ArrayList(4);
/*     */       }
/* 179 */       if (this.debug) {
/* 180 */         log.debug("Trying to merge [{}] with contents [\n{}\n] into [\n{}\n]", new Object[] { this.reqId, this.newDocToMerge, destinationDoc });
/*     */       }
/*     */       
/*     */ 
/* 184 */       if (this.externallyAppliedIdsInSrcDoc != null) {
/* 185 */         int numAlreadyPresent = 0;
/* 186 */         for (String alreadyAppliedId : this.externallyAppliedIdsInSrcDoc) {
/* 187 */           numAlreadyPresent += (shouldMerge(alreadyAppliedId) ? 0 : 1);
/*     */         }
/*     */         
/*     */ 
/* 191 */         if (numAlreadyPresent == this.externallyAppliedIdsInSrcDoc.size()) {
/* 192 */           if (this.debug) {
/* 193 */             log.debug("Skipping merge as all the ids {} given have already been merged before {}", this.externallyAppliedIdsInSrcDoc, this.appliedIdsInDestinationDoc);
/*     */           }
/*     */           
/* 196 */           return null; }
/* 197 */         if ((this.debug) && (numAlreadyPresent > 0)) {
/* 198 */           log.debug("[{}] out of ids [{}] have already been merged before {}", new Object[] { Integer.valueOf(numAlreadyPresent), Integer.valueOf(this.externallyAppliedIdsInSrcDoc.size()), this.appliedIdsInDestinationDoc });
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 204 */       if (shouldMerge(this.reqId)) {
/* 205 */         iterateOverNewDocAndMerge(destinationDoc);
/* 206 */       } else if (this.debug) {
/* 207 */         log.debug("Skipping merge as the id [{}] has already been merged before {}", this.reqId, this.appliedIdsInDestinationDoc);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 212 */       if (this.idsThatWereAppliedHere != null) {
/* 213 */         for (String s : this.idsThatWereAppliedHere) {
/* 214 */           if (!this.appliedIdsInDestinationDoc.contains(s)) {
/* 215 */             this.appliedIdsInDestinationDoc.add(s);
/*     */           }
/*     */         }
/*     */       }
/* 219 */       if (this.appliedIdsInDestinationDoc.size() > 0) {
/* 220 */         destinationDoc.put("_appliedUpdates", this.appliedIdsInDestinationDoc);
/*     */       }
/* 222 */       if (this.debug) {
/* 223 */         log.debug("Merged contents [\n{}\n]", destinationDoc);
/*     */       }
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 228 */       log.error("Error occurred while processing upsert", t);
/* 229 */       throw t;
/*     */     }
/* 231 */     return null;
/*     */   }
/*     */   
/*     */   private boolean shouldMerge(String reqId) {
/* 235 */     if (reqId == null)
/*     */     {
/* 237 */       return true;
/*     */     }
/*     */     
/* 240 */     if (this.appliedIdsInDestinationDoc.contains(reqId)) {
/* 241 */       return false;
/*     */     }
/* 243 */     if (this.idsThatWereAppliedHere == null) {
/* 244 */       this.idsThatWereAppliedHere = new ArrayList(4);
/*     */     }
/* 246 */     this.idsThatWereAppliedHere.add(reqId);
/*     */     
/* 248 */     return true;
/*     */   }
/*     */   
/*     */   private void iterateOverNewDocAndMerge(Map<Object, Object> destinationDoc) {
/* 252 */     for (Map.Entry entry : this.newDocToMerge.entrySet())
/*     */     {
/* 254 */       if (entry.getKey().toString().charAt(0) != '_')
/*     */       {
/*     */ 
/*     */ 
/* 258 */         if (this.mergeFields.contains(entry.getKey()))
/*     */         {
/* 260 */           doMerge(destinationDoc, entry);
/*     */         }
/*     */         else
/* 263 */           destinationDoc.put(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void doMerge(Map destinationDoc, Map.Entry entryToMerge) {
/* 269 */     Object key = entryToMerge.getKey();
/* 270 */     Object valueToMerge = entryToMerge.getValue();
/*     */     
/*     */ 
/* 273 */     List<Object> multiValuesInMainDoc = Collections.castOrNewList(destinationDoc.get(key));
/*     */     int i;
/* 275 */     List<String> externallyAppliedIds; if ((valueToMerge instanceof List)) {
/* 276 */       List<Object> listOfValues = (List)valueToMerge;
/* 277 */       i = 0;
/* 278 */       externallyAppliedIds = (this.externallyAppliedIdsInSrcDoc == null) || (this.externallyAppliedIdsInSrcDoc.size() != listOfValues.size()) ? null : this.externallyAppliedIdsInSrcDoc;
/*     */       
/*     */ 
/* 281 */       if ((this.debug) && (this.externallyAppliedIdsInSrcDoc != null) && (externallyAppliedIds == null))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 286 */         log.debug("Size of externally merged ids does not match the number of items in the actual list. Uniqueness check cannot be performed so, items might appear more than once {}", this.externallyAppliedIdsInSrcDoc);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 291 */       for (Object o : listOfValues)
/*     */       {
/* 293 */         if ((externallyAppliedIds == null) || (shouldMerge((String)externallyAppliedIds.get(i)))) {
/* 294 */           multiValuesInMainDoc.add(o);
/* 295 */         } else if (this.debug) {
/* 296 */           log.debug("Skipping merge as the id [{}] has already been merged before {}", this.reqId, this.appliedIdsInDestinationDoc);
/*     */         }
/*     */         
/* 299 */         i++;
/*     */       }
/* 301 */     } else if ((this.externallyAppliedIdsInSrcDoc == null) || (shouldMerge((String)this.externallyAppliedIdsInSrcDoc.get(0)))) {
/* 302 */       multiValuesInMainDoc.add(valueToMerge);
/*     */     }
/*     */     
/* 305 */     destinationDoc.put(key, multiValuesInMainDoc);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/scripts/MergeUpsertScript.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */