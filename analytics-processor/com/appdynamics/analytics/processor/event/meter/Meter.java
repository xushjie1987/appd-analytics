/*     */ package com.appdynamics.analytics.processor.event.meter;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import javax.annotation.concurrent.ThreadSafe;
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
/*     */ @ThreadSafe
/*     */ class Meter
/*     */   extends MeterKey
/*     */ {
/*  19 */   private static final Logger log = LoggerFactory.getLogger(Meter.class);
/*     */   
/*     */   private final String accountName;
/*     */   private final String eventType;
/*     */   private final String meterType;
/*     */   private final long timeWindowStart;
/*     */   private final int hashCode;
/*     */   private final long quotaRemaining;
/*     */   private final boolean persistable;
/*     */   private final AtomicLong quotaUsed;
/*     */   private final AtomicLong quotaExcess;
/*     */   
/*     */   Meter(String accountName, String eventType, String meterType, long timeWindowStart, long quotaRemaining, boolean persistable)
/*     */   {
/*  33 */     if (quotaRemaining < 0L) {
/*  34 */       log.warn("Quota remaining is negative [{}] for account [{}] event type [{}].", new Object[] { Long.valueOf(quotaRemaining), accountName, eventType });
/*     */     }
/*     */     
/*  37 */     this.accountName = accountName;
/*  38 */     this.eventType = eventType;
/*  39 */     this.meterType = meterType;
/*  40 */     this.timeWindowStart = timeWindowStart;
/*  41 */     this.hashCode = hashCode(this);
/*  42 */     this.quotaRemaining = quotaRemaining;
/*  43 */     this.persistable = persistable;
/*  44 */     this.quotaUsed = new AtomicLong(0L);
/*  45 */     this.quotaExcess = new AtomicLong(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Meter(String accountName, String eventType, String meterType, long timeWindowStart, long quotaRemaining)
/*     */   {
/*  55 */     this(accountName, eventType, meterType, timeWindowStart, quotaRemaining, true);
/*     */   }
/*     */   
/*     */   String getAccountName()
/*     */   {
/*  60 */     return this.accountName;
/*     */   }
/*     */   
/*     */   String getEventType()
/*     */   {
/*  65 */     return this.eventType;
/*     */   }
/*     */   
/*     */   String getMeterType()
/*     */   {
/*  70 */     return this.meterType;
/*     */   }
/*     */   
/*     */   long getTimeWindowStart()
/*     */   {
/*  75 */     return this.timeWindowStart;
/*     */   }
/*     */   
/*     */   boolean isPersistable()
/*     */   {
/*  80 */     return this.persistable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   long useQuota(long bytes)
/*     */   {
/*  89 */     long beforeQuotaRemaining = this.quotaRemaining - this.quotaUsed.get();
/*  90 */     if (beforeQuotaRemaining >= bytes) {
/*  91 */       this.quotaUsed.addAndGet(bytes);
/*     */     } else {
/*  93 */       this.quotaExcess.addAndGet(bytes - beforeQuotaRemaining);
/*  94 */       this.quotaUsed.addAndGet(beforeQuotaRemaining);
/*     */     }
/*  96 */     long afterQuotaRemaining = this.quotaRemaining - this.quotaUsed.get();
/*  97 */     return afterQuotaRemaining - this.quotaExcess.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   MeterReapResult reap()
/*     */   {
/* 106 */     long usage = this.quotaUsed.getAndSet(0L);
/* 107 */     long excess = this.quotaExcess.getAndSet(0L);
/* 108 */     usage = usage >= 0L ? usage : 0L;
/* 109 */     excess = excess >= 0L ? excess : 0L;
/* 110 */     return new MeterReapResult(usage, excess);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 117 */     return this.hashCode;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/Meter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */