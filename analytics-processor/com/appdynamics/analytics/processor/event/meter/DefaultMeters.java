/*     */ package com.appdynamics.analytics.processor.event.meter;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.event.EventService;
/*     */ import com.appdynamics.analytics.processor.event.EventTypeMetaData;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.cache.Cache;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.joda.time.DateTime;
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
/*     */ class DefaultMeters
/*     */   extends Meters
/*     */ {
/*  28 */   private static final Logger log = LoggerFactory.getLogger(DefaultMeters.class);
/*     */   
/*     */ 
/*     */   static final int CACHE_SIZE = 4000;
/*     */   
/*     */ 
/*     */   static final int DATA_DOCUMENT_CACHE_EXPIRE_MINUTES = 5;
/*     */   
/*     */ 
/*     */   private final TimeKeeper timeKeeper;
/*     */   
/*     */ 
/*     */   private final MetersStore metersStore;
/*     */   
/*     */ 
/*     */   private final EventService eventService;
/*     */   
/*     */   private final MeteringModuleConfiguration configuration;
/*     */   
/*     */   private final ThreadLocal<MutableMeterKey> tlsMutableMeterKeys;
/*     */   
/*     */   private final Cache<CacheKey, Long> dailyDataCapCache;
/*     */   
/*     */   private final Cache<CacheKey, Long> dailyDocumentCapCache;
/*     */   
/*     */ 
/*     */   DefaultMeters(final TimeKeeper timeKeeper, MetersStore metersStore, EventService eventService, MeteringModuleConfiguration configuration)
/*     */   {
/*  56 */     this.timeKeeper = timeKeeper;
/*  57 */     this.tlsMutableMeterKeys = new ThreadLocal()
/*     */     {
/*     */       protected MutableMeterKey initialValue() {
/*  60 */         return new MutableMeterKey("", "", "", timeKeeper.getCurrentMinute().getMillis(), true);
/*     */       }
/*  62 */     };
/*  63 */     this.metersStore = metersStore;
/*  64 */     this.eventService = eventService;
/*  65 */     this.configuration = configuration;
/*  66 */     this.dailyDataCapCache = buildCache(4000, 5, TimeUnit.MINUTES);
/*  67 */     this.dailyDocumentCapCache = buildCache(4000, 5, TimeUnit.MINUTES);
/*     */   }
/*     */   
/*     */   Cache buildCache(int size, int duration, TimeUnit unit) {
/*  71 */     return CacheBuilder.newBuilder().maximumSize(size).expireAfterWrite(duration, unit).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long updateAndCheckBytes(String accountName, String eventType, long byteCount)
/*     */   {
/*  79 */     Preconditions.checkArgument(byteCount >= 0L, "Usage count in bytes cannot be negative");
/*     */     
/*  81 */     long quotaRemaining = -1L;
/*  82 */     long currentMinuteMillis = 0L;
/*  83 */     for (int attempt = 0; (quotaRemaining < 0L) && (attempt < 2); attempt++) {
/*  84 */       long tempTimeMillis = this.timeKeeper.getCurrentMinute().getMillis();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */       if (tempTimeMillis == currentMinuteMillis) {
/*     */         break;
/*     */       }
/*     */       
/*  98 */       currentMinuteMillis = tempTimeMillis;
/*     */       
/* 100 */       Meter accountMeter = findBytesMeter(accountName, eventType, currentMinuteMillis);
/*     */       
/*     */ 
/*     */ 
/* 104 */       quotaRemaining = accountMeter.useQuota(byteCount);
/*     */     }
/*     */     
/* 107 */     return quotaRemaining;
/*     */   }
/*     */   
/*     */   public long updateAndCheckDocumentFragments(String accountName, String eventType, long numDocuments)
/*     */   {
/* 112 */     Preconditions.checkArgument(numDocuments >= 0L, "Usage count in bytes cannot be negative");
/*     */     
/* 114 */     long quotaRemaining = -1L;
/* 115 */     long currentMinuteMillis = 0L;
/* 116 */     for (int attempt = 0; (quotaRemaining < 0L) && (attempt < 2); attempt++) {
/* 117 */       long tempTimeMillis = this.timeKeeper.getCurrentMinute().getMillis();
/* 118 */       if (tempTimeMillis == currentMinuteMillis) {
/*     */         break;
/*     */       }
/* 121 */       currentMinuteMillis = tempTimeMillis;
/*     */       
/* 123 */       Meter accountMeter = findDocFragmentMeter(accountName, eventType, currentMinuteMillis);
/* 124 */       quotaRemaining = accountMeter.useQuota(numDocuments);
/*     */     }
/*     */     
/* 127 */     return quotaRemaining;
/*     */   }
/*     */   
/*     */   public long updateAndCheckSearchThreshold(String accountName, String eventType)
/*     */   {
/* 132 */     long quotaRemaining = -1L;
/* 133 */     long currentMinuteMillis = 0L;
/* 134 */     for (int attempt = 0; (quotaRemaining < 0L) && (attempt < 2); attempt++) {
/* 135 */       long tempTimeMillis = this.timeKeeper.getCurrentMinute().getMillis();
/* 136 */       if (tempTimeMillis == currentMinuteMillis) {
/*     */         break;
/*     */       }
/* 139 */       currentMinuteMillis = tempTimeMillis;
/*     */       
/* 141 */       Meter accountMeter = findSearchMeter(accountName, eventType, currentMinuteMillis);
/* 142 */       quotaRemaining = accountMeter.useQuota(1L);
/*     */     }
/*     */     
/* 145 */     return quotaRemaining;
/*     */   }
/*     */   
/*     */   public long getDailyBytesLimit(String accountName, String eventType)
/*     */   {
/* 150 */     CacheKey key = new CacheKey(accountName, eventType);
/* 151 */     Long eventTypeQuota = (Long)this.dailyDataCapCache.getIfPresent(key);
/* 152 */     if (eventTypeQuota == null) {
/* 153 */       EventTypeMetaData eventTypeMetaData = this.eventService.getEventTypeMetaData(accountName, eventType);
/*     */       
/* 155 */       if (eventTypeMetaData != null) {
/* 156 */         eventTypeQuota = eventTypeMetaData.getMaxDailyDataVolumeBytes();
/* 157 */         if (eventTypeQuota == null)
/*     */         {
/*     */ 
/* 160 */           if (eventTypeMetaData.getDailyDocumentCapVolume() != null) {
/* 161 */             eventTypeQuota = Long.valueOf(Long.MAX_VALUE);
/*     */           }
/*     */           else {
/* 164 */             eventTypeQuota = Long.valueOf(this.configuration.getMaxDailyEventTypeBytesQuota());
/*     */           }
/*     */         }
/*     */       } else {
/* 168 */         eventTypeQuota = Long.valueOf(this.configuration.getMaxDailyEventTypeBytesQuota());
/*     */       }
/*     */       
/* 171 */       this.dailyDataCapCache.put(key, eventTypeQuota);
/*     */     }
/* 173 */     return eventTypeQuota.longValue();
/*     */   }
/*     */   
/*     */   public long getDailyDocumentsLimit(String accountName, String eventType)
/*     */   {
/* 178 */     CacheKey key = new CacheKey(accountName, eventType);
/* 179 */     Long dailyDocumentCap = (Long)this.dailyDocumentCapCache.getIfPresent(key);
/* 180 */     if (dailyDocumentCap == null) {
/* 181 */       EventTypeMetaData eventTypeMetaData = this.eventService.getEventTypeMetaData(accountName, eventType);
/*     */       
/* 183 */       if (eventTypeMetaData != null) {
/* 184 */         dailyDocumentCap = eventTypeMetaData.getDailyDocumentCapVolume();
/* 185 */         if (dailyDocumentCap == null)
/*     */         {
/*     */ 
/* 188 */           if (eventTypeMetaData.getMaxDailyDataVolumeBytes() != null) {
/* 189 */             dailyDocumentCap = Long.valueOf(Long.MAX_VALUE);
/*     */           }
/*     */           else {
/* 192 */             dailyDocumentCap = Long.valueOf(this.configuration.getMaxDailyEventTypeDocumentsQuota());
/*     */           }
/*     */         }
/*     */       } else {
/* 196 */         dailyDocumentCap = Long.valueOf(this.configuration.getMaxDailyEventTypeDocumentsQuota());
/*     */       }
/*     */       
/* 199 */       this.dailyDocumentCapCache.put(key, dailyDocumentCap);
/*     */     }
/* 201 */     return dailyDocumentCap.longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Meter findBytesMeter(final String accountName, final String eventType, final long currentMinuteMillis)
/*     */   {
/* 212 */     findMeter(MetersStore.MeterType.bytesMeter, true, accountName, eventType, currentMinuteMillis, new MeterProvider()
/*     */     {
/*     */       public DefaultMeters.ProvidedMeter create()
/*     */       {
/* 216 */         RuntimeException usageRetrievalException = null;
/*     */         Long eventTypeQuota;
/*     */         try {
/* 219 */           eventTypeQuota = Long.valueOf(DefaultMeters.this.getDailyBytesLimit(accountName, eventType));
/*     */         } catch (RuntimeException e) {
/* 221 */           eventTypeQuota = Long.valueOf(DefaultMeters.this.configuration.getMaxDailyEventTypeBytesQuota());
/* 222 */           DefaultMeters.log.info("Error attempting to get event type metadata for account [" + accountName + "] event type [" + eventType + "]. Defaulting to [" + eventTypeQuota + "]", e);
/*     */           
/* 224 */           usageRetrievalException = e;
/*     */         }
/*     */         
/*     */ 
/* 228 */         DateTime currDay = DefaultMeters.this.timeKeeper.getCurrentDay();
/* 229 */         DateTime nextDay = currDay.plusDays(1).withTimeAtStartOfDay();
/*     */         long eventTypeQuotaRemaining;
/*     */         try {
/* 232 */           long eventTypeUsed = DefaultMeters.this.metersStore.getUsageBytes(accountName, eventType, currDay, nextDay);
/* 233 */           eventTypeQuotaRemaining = eventTypeQuota.longValue() - eventTypeUsed;
/*     */         } catch (RuntimeException e) {
/* 235 */           eventTypeQuotaRemaining = Long.MAX_VALUE;
/* 236 */           DefaultMeters.log.info("Error attempting to get usage bytes for account [" + accountName + "] event type [" + eventType + "]. Defaulting to [" + eventTypeQuotaRemaining + "]", e);
/*     */           
/*     */ 
/* 239 */           usageRetrievalException = e;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 246 */         Meter meter = new Meter(accountName, eventType, MetersStore.MeterType.bytesMeter.name(), currentMinuteMillis, eventTypeQuotaRemaining);
/*     */         
/* 248 */         return new DefaultMeters.ProvidedMeter(meter, usageRetrievalException);
/*     */       }
/*     */     });
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
/*     */   private Meter findDocFragmentMeter(final String accountName, final String eventType, final long currentMinuteMillis)
/*     */   {
/* 263 */     findMeter(MetersStore.MeterType.docFragmentsMeter, true, accountName, eventType, currentMinuteMillis, new MeterProvider()
/*     */     {
/*     */       public DefaultMeters.ProvidedMeter create()
/*     */       {
/* 267 */         RuntimeException usageRetrievalException = null;
/*     */         Long dailyDocumentCap;
/*     */         try {
/* 270 */           dailyDocumentCap = Long.valueOf(DefaultMeters.this.getDailyDocumentsLimit(accountName, eventType));
/*     */         } catch (RuntimeException e) {
/* 272 */           dailyDocumentCap = Long.valueOf(DefaultMeters.this.configuration.getMaxDailyEventTypeDocumentsQuota());
/* 273 */           DefaultMeters.log.info("Error attempting to get event type metadata for account [" + accountName + "] event type [" + eventType + "]. Defaulting to [" + dailyDocumentCap + "]", e);
/*     */           
/*     */ 
/* 276 */           usageRetrievalException = e;
/*     */         }
/*     */         
/*     */ 
/* 280 */         DateTime currDay = DefaultMeters.this.timeKeeper.getCurrentDay();
/* 281 */         DateTime nextDay = currDay.plusDays(1).withTimeAtStartOfDay();
/*     */         long docFragmentsUsedCount;
/*     */         try {
/* 284 */           docFragmentsUsedCount = DefaultMeters.this.metersStore.getUsageDocumentFragments(accountName, eventType, currDay, nextDay);
/*     */         }
/*     */         catch (RuntimeException e) {
/* 287 */           docFragmentsUsedCount = 0L;
/* 288 */           DefaultMeters.log.info("Error attempting to get document fragments used count for account [" + accountName + "] event type [" + eventType + "]. Defaulting to [" + docFragmentsUsedCount + "]", e);
/*     */           
/*     */ 
/* 291 */           usageRetrievalException = e;
/*     */         }
/*     */         
/* 294 */         Meter meter = new Meter(accountName, eventType, MetersStore.MeterType.docFragmentsMeter.name(), currentMinuteMillis, dailyDocumentCap.longValue() - docFragmentsUsedCount);
/*     */         
/* 296 */         return new DefaultMeters.ProvidedMeter(meter, usageRetrievalException);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Meter findSearchMeter(final String accountName, final String eventType, final long currentMinuteMillis)
/*     */   {
/* 310 */     findMeter(MetersStore.MeterType.maxSearchesMeter, false, accountName, eventType, currentMinuteMillis, new MeterProvider()
/*     */     {
/*     */       public DefaultMeters.ProvidedMeter create()
/*     */       {
/* 314 */         Meter meter = new Meter(accountName, eventType, MetersStore.MeterType.maxSearchesMeter.name(), currentMinuteMillis, DefaultMeters.this.configuration.getEventTypeMaxSearchesPerMinute());
/*     */         
/* 316 */         return new DefaultMeters.ProvidedMeter(meter, null);
/*     */       }
/*     */     });
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
/*     */   private Meter findMeter(MetersStore.MeterType meterType, boolean shouldPersist, String accountName, String eventType, long currentMinuteMillis, MeterProvider meterProvider)
/*     */   {
/* 331 */     MutableMeterKey meterKey = (MutableMeterKey)this.tlsMutableMeterKeys.get();
/* 332 */     meterKey.reuse(accountName, eventType, meterType.name(), currentMinuteMillis, shouldPersist);
/*     */     
/*     */ 
/* 335 */     Meter meter = (Meter)this.meters.get(meterKey);
/* 336 */     if (meter != null) {
/* 337 */       return meter;
/*     */     }
/*     */     
/* 340 */     ProvidedMeter providedMeter = meterProvider.create();
/* 341 */     meter = providedMeter.getMeter();
/*     */     
/* 343 */     Meter oldMeter = (Meter)this.meters.putIfAbsent(meter, meter);
/* 344 */     if (oldMeter != null) {
/* 345 */       meter = oldMeter;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 351 */     if ((oldMeter == null) && (providedMeter.getUsageRetrievalException() != null)) {
/* 352 */       throw providedMeter.getUsageRetrievalException();
/*     */     }
/*     */     
/* 355 */     return meter; }
/*     */   
/*     */   static abstract interface MeterProvider { public abstract DefaultMeters.ProvidedMeter create(); }
/*     */   
/*     */   static class ProvidedMeter { final Meter meter;
/*     */     final RuntimeException usageRetrievalException;
/*     */     
/*     */     @ConstructorProperties({"meter", "usageRetrievalException"})
/* 363 */     public ProvidedMeter(Meter meter, RuntimeException usageRetrievalException) { this.meter = meter;this.usageRetrievalException = usageRetrievalException; }
/* 364 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ProvidedMeter)) return false; ProvidedMeter other = (ProvidedMeter)o; if (!other.canEqual(this)) return false; Object this$meter = getMeter();Object other$meter = other.getMeter(); if (this$meter == null ? other$meter != null : !this$meter.equals(other$meter)) return false; Object this$usageRetrievalException = getUsageRetrievalException();Object other$usageRetrievalException = other.getUsageRetrievalException();return this$usageRetrievalException == null ? other$usageRetrievalException == null : this$usageRetrievalException.equals(other$usageRetrievalException); } public boolean canEqual(Object other) { return other instanceof ProvidedMeter; } public int hashCode() { int PRIME = 31;int result = 1;Object $meter = getMeter();result = result * 31 + ($meter == null ? 0 : $meter.hashCode());Object $usageRetrievalException = getUsageRetrievalException();result = result * 31 + ($usageRetrievalException == null ? 0 : $usageRetrievalException.hashCode());return result; } public String toString() { return "DefaultMeters.ProvidedMeter(meter=" + getMeter() + ", usageRetrievalException=" + getUsageRetrievalException() + ")"; }
/*     */     
/* 366 */     public Meter getMeter() { return this.meter; }
/* 367 */     public RuntimeException getUsageRetrievalException() { return this.usageRetrievalException; }
/*     */   }
/*     */   
/*     */   public static class CacheKey { @ConstructorProperties({"accountName", "eventType"})
/* 371 */     public CacheKey(String accountName, String eventType) { this.accountName = accountName;this.eventType = eventType; }
/* 372 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof CacheKey)) return false; CacheKey other = (CacheKey)o; if (!other.canEqual(this)) return false; Object this$accountName = this.accountName;Object other$accountName = other.accountName; if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$eventType = this.eventType;Object other$eventType = other.eventType;return this$eventType == null ? other$eventType == null : this$eventType.equals(other$eventType); } public boolean canEqual(Object other) { return other instanceof CacheKey; } public int hashCode() { int PRIME = 31;int result = 1;Object $accountName = this.accountName;result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $eventType = this.eventType;result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());return result;
/*     */     }
/*     */     
/*     */     String accountName;
/*     */     String eventType;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/DefaultMeters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */