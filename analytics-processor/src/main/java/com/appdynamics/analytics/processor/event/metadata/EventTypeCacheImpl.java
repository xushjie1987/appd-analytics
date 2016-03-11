/*     */ package com.appdynamics.analytics.processor.event.metadata;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.DefaultIndexNameResolver;
/*     */ import com.appdynamics.analytics.processor.event.EventTypeMetaData;
/*     */ import com.appdynamics.analytics.processor.event.configuration.EventServiceConfiguration;
/*     */ import com.google.common.cache.Cache;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.elasticsearch.client.Client;
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
/*     */ @Singleton
/*     */ public class EventTypeCacheImpl
/*     */   implements EventTypeCache
/*     */ {
/*     */   static final String FIELD_SEPARATOR = "___";
/*     */   volatile Cache<String, Integer> accountEventTypeCache;
/*     */   volatile Cache<String, EventTypeMetaData> eventTypeMetaDataCache;
/*     */   final ClientProvider clientProvider;
/*     */   
/*     */   @Inject
/*     */   public EventTypeCacheImpl(ClientProvider clientProvider, EventServiceConfiguration eventServiceConfiguration)
/*     */   {
/*  55 */     this.clientProvider = clientProvider;
/*  56 */     this.accountEventTypeCache = CacheBuilder.newBuilder().maximumSize(eventServiceConfiguration.getEventTypeCacheSize()).expireAfterWrite(eventServiceConfiguration.getEventTypeCacheTtlMinutes(), TimeUnit.MINUTES).build();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */     this.eventTypeMetaDataCache = CacheBuilder.newBuilder().maximumSize(eventServiceConfiguration.getEventTypeCacheSize()).expireAfterWrite(eventServiceConfiguration.getEventTypeMetaDataCacheTtlMinutes(), TimeUnit.MINUTES).build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void storeAccountNameEventType(String accountName, String eventType)
/*     */   {
/*  70 */     Client client = this.clientProvider.getInsertClient(accountName);
/*  71 */     this.accountEventTypeCache.put(buildAccountWithEventCacheKey(accountName, eventType), Integer.valueOf(client.hashCode()));
/*     */   }
/*     */   
/*     */   public void removeAccountNameEventType(String accountName, String eventType)
/*     */   {
/*  76 */     this.accountEventTypeCache.invalidate(buildAccountWithEventCacheKey(accountName, eventType));
/*     */   }
/*     */   
/*     */   public boolean accountNameEventTypeExists(String accountName, String eventType)
/*     */   {
/*  81 */     Object value = this.accountEventTypeCache.getIfPresent(buildAccountWithEventCacheKey(accountName, eventType));
/*  82 */     return value != null;
/*     */   }
/*     */   
/*     */   public boolean accountNameEventTypeExistsWithCurrentInsertClient(String accountName, String eventType)
/*     */   {
/*  87 */     Client client = this.clientProvider.getInsertClient(accountName);
/*  88 */     Integer clientHash = getClientHashForAccountNameEventType(accountName, eventType);
/*  89 */     return (clientHash != null) && (client.hashCode() == clientHash.intValue());
/*     */   }
/*     */   
/*     */   Integer getClientHashForAccountNameEventType(String accountName, String eventType) {
/*  93 */     return (Integer)this.accountEventTypeCache.getIfPresent(buildAccountWithEventCacheKey(accountName, eventType));
/*     */   }
/*     */   
/*     */   public void storeEventTypeMetadata(String accountName, String eventType, EventTypeMetaData eventTypeMetaData)
/*     */   {
/*  98 */     if (eventTypeMetaData != null) {
/*  99 */       String key = buildAccountWithEventCacheKey(accountName, eventType);
/* 100 */       this.eventTypeMetaDataCache.put(key, eventTypeMetaData);
/*     */     }
/*     */   }
/*     */   
/*     */   public EventTypeMetaData getEventTypeMetadata(String accountName, String eventType)
/*     */   {
/* 106 */     return (EventTypeMetaData)this.eventTypeMetaDataCache.getIfPresent(buildAccountWithEventCacheKey(accountName, eventType));
/*     */   }
/*     */   
/*     */   public void invalidateEventTypeMetaData(String accountName, String eventType)
/*     */   {
/* 111 */     String key = buildAccountWithEventCacheKey(accountName, eventType);
/* 112 */     this.eventTypeMetaDataCache.invalidate(key);
/*     */   }
/*     */   
/*     */   public void invalidateAllEntries(Collection<String> accounts)
/*     */   {
/* 117 */     invalidateAllAccountEntriesInCache(this.eventTypeMetaDataCache, accounts);
/* 118 */     invalidateAllAccountEntriesInCache(this.accountEventTypeCache, accounts);
/*     */   }
/*     */   
/*     */   private String buildAccountWithEventCacheKey(String accountName, String eventType) {
/* 122 */     eventType = DefaultIndexNameResolver.validateAndResolveEventType(eventType);
/* 123 */     accountName = AccountConfiguration.normalizeAccountName(accountName);
/* 124 */     return accountName + "___" + eventType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void invalidateAllAccountEntriesInCache(Cache<String, ?> cache, Collection<String> accounts)
/*     */   {
/* 132 */     List<String> keysToDelete = new ArrayList();
/* 133 */     ConcurrentMap<String, ?> concurrentMap = cache.asMap();
/* 134 */     for (Map.Entry<String, ?> entry : concurrentMap.entrySet()) {
/* 135 */       accountWithEvent = (String)entry.getKey();
/* 136 */       account = accountWithEvent.split("___")[0];
/* 137 */       for (String accountToCheck : accounts)
/* 138 */         if (AccountConfiguration.normalizeAccountName(accountToCheck).equals(account))
/* 139 */           keysToDelete.add(accountWithEvent);
/*     */     }
/*     */     String accountWithEvent;
/*     */     String account;
/* 143 */     cache.invalidateAll(keysToDelete);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/metadata/EventTypeCacheImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */