/*     */ package com.appdynamics.common.util.datetime;
/*     */ 
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.DateTimeZone;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.joda.time.format.ISODateTimeFormat;
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
/*     */ @ThreadSafe
/*     */ public class TimeKeeper
/*     */ {
/*     */   public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
/*  35 */   private static final DateTimeFormatter UTC_ISO_8601_FORMATTER = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);
/*     */   
/*     */   private final Ticker ticker;
/*     */   private volatile CachedTimes cachedTimes;
/*     */   
/*     */   public TimeKeeper(Ticker ticker)
/*     */   {
/*  42 */     this.ticker = ticker;
/*  43 */     tryUpdateCachedTimes();
/*     */   }
/*     */   
/*     */   public TimeKeeper() {
/*  47 */     this(new Ticker());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DateTime utcTime(long millis)
/*     */   {
/*  55 */     return new DateTime(millis, DateTimeZone.UTC);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static DateTime currentUtcTime()
/*     */   {
/*  62 */     return DateTime.now(DateTimeZone.UTC);
/*     */   }
/*     */   
/*     */   public static DateTimeFormatter utcIso8601Formatter() {
/*  66 */     return UTC_ISO_8601_FORMATTER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String utcTimeIso8601(long utcMillis)
/*     */   {
/*  75 */     return UTC_ISO_8601_FORMATTER.print(utcMillis);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String currentUtcTimeIso8601()
/*     */   {
/*  83 */     return utcTimeIso8601(System.currentTimeMillis());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static DateTime minute(long utcMillis)
/*     */   {
/*  91 */     return utcTime(utcMillis).withSecondOfMinute(0).withMillisOfSecond(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DateTime currentMinute()
/*     */   {
/*  99 */     return minute(System.currentTimeMillis());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void tryUpdateCachedTimes()
/*     */   {
/* 110 */     long now = this.ticker.currentTimeMillis();
/* 111 */     if ((this.cachedTimes == null) || (now >= this.cachedTimes.expiresAtMillis)) {
/* 112 */       DateTime nowMinute = minute(now);
/* 113 */       this.cachedTimes = new CachedTimes(nowMinute, nowMinute.plusMinutes(1).getMillis(), null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateTime getPreviousMinute()
/*     */   {
/* 121 */     tryUpdateCachedTimes();
/* 122 */     return this.cachedTimes.prevMinute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateTime getCurrentDay()
/*     */   {
/* 129 */     tryUpdateCachedTimes();
/* 130 */     return this.cachedTimes.currDay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DateTime getCurrentMinute()
/*     */   {
/* 137 */     tryUpdateCachedTimes();
/* 138 */     return this.cachedTimes.currMinute;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class CachedTimes
/*     */   {
/*     */     final DateTime prevMinute;
/*     */     
/*     */     final DateTime currDay;
/*     */     
/*     */     final DateTime currMinute;
/*     */     
/*     */     final long expiresAtMillis;
/*     */     
/*     */ 
/*     */     private CachedTimes(DateTime currMinute, long expiresAtMillis)
/*     */     {
/* 155 */       this.prevMinute = currMinute.minusMinutes(1);
/* 156 */       this.currDay = currMinute.withTimeAtStartOfDay();
/* 157 */       this.currMinute = currMinute;
/* 158 */       this.expiresAtMillis = expiresAtMillis;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/datetime/TimeKeeper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */