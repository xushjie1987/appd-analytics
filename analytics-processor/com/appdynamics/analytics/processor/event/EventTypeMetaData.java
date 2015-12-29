/*     */ package com.appdynamics.analytics.processor.event;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.event.configuration.EventTypeMetaDataDefaults;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import java.beans.ConstructorProperties;
/*     */ import javax.validation.constraints.Max;
/*     */ import javax.validation.constraints.Min;
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
/*     */ 
/*     */ 
/*     */ @JsonIgnoreProperties(ignoreUnknown=true)
/*     */ public class EventTypeMetaData
/*     */ {
/*  27 */   public String toString() { return "EventTypeMetaData(accountName=" + getAccountName() + ", eventType=" + getEventType() + ", hotLifespanInDays=" + getHotLifespanInDays() + ", maxDailyDataVolumeBytes=" + getMaxDailyDataVolumeBytes() + ", lookBackTimePeriodSeconds=" + getLookBackTimePeriodSeconds() + ", dailyDocumentCapVolume=" + getDailyDocumentCapVolume() + ", expirationDate=" + getExpirationDate() + ", creationDate=" + getCreationDate() + ")"; }
/*  28 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof EventTypeMetaData)) return false; EventTypeMetaData other = (EventTypeMetaData)o; if (!other.canEqual(this)) return false; Object this$accountName = getAccountName();Object other$accountName = other.getAccountName(); if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$eventType = getEventType();Object other$eventType = other.getEventType(); if (this$eventType == null ? other$eventType != null : !this$eventType.equals(other$eventType)) return false; Object this$hotLifespanInDays = getHotLifespanInDays();Object other$hotLifespanInDays = other.getHotLifespanInDays(); if (this$hotLifespanInDays == null ? other$hotLifespanInDays != null : !this$hotLifespanInDays.equals(other$hotLifespanInDays)) return false; Object this$maxDailyDataVolumeBytes = getMaxDailyDataVolumeBytes();Object other$maxDailyDataVolumeBytes = other.getMaxDailyDataVolumeBytes(); if (this$maxDailyDataVolumeBytes == null ? other$maxDailyDataVolumeBytes != null : !this$maxDailyDataVolumeBytes.equals(other$maxDailyDataVolumeBytes)) return false; Object this$lookBackTimePeriodSeconds = getLookBackTimePeriodSeconds();Object other$lookBackTimePeriodSeconds = other.getLookBackTimePeriodSeconds(); if (this$lookBackTimePeriodSeconds == null ? other$lookBackTimePeriodSeconds != null : !this$lookBackTimePeriodSeconds.equals(other$lookBackTimePeriodSeconds)) return false; Object this$dailyDocumentCapVolume = getDailyDocumentCapVolume();Object other$dailyDocumentCapVolume = other.getDailyDocumentCapVolume(); if (this$dailyDocumentCapVolume == null ? other$dailyDocumentCapVolume != null : !this$dailyDocumentCapVolume.equals(other$dailyDocumentCapVolume)) return false; Object this$expirationDate = getExpirationDate();Object other$expirationDate = other.getExpirationDate();return this$expirationDate == null ? other$expirationDate == null : this$expirationDate.equals(other$expirationDate); } public boolean canEqual(Object other) { return other instanceof EventTypeMetaData; } public int hashCode() { int PRIME = 31;int result = 1;Object $accountName = getAccountName();result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $eventType = getEventType();result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());Object $hotLifespanInDays = getHotLifespanInDays();result = result * 31 + ($hotLifespanInDays == null ? 0 : $hotLifespanInDays.hashCode());Object $maxDailyDataVolumeBytes = getMaxDailyDataVolumeBytes();result = result * 31 + ($maxDailyDataVolumeBytes == null ? 0 : $maxDailyDataVolumeBytes.hashCode());Object $lookBackTimePeriodSeconds = getLookBackTimePeriodSeconds();result = result * 31 + ($lookBackTimePeriodSeconds == null ? 0 : $lookBackTimePeriodSeconds.hashCode());Object $dailyDocumentCapVolume = getDailyDocumentCapVolume();result = result * 31 + ($dailyDocumentCapVolume == null ? 0 : $dailyDocumentCapVolume.hashCode());Object $expirationDate = getExpirationDate();result = result * 31 + ($expirationDate == null ? 0 : $expirationDate.hashCode());return result; } @ConstructorProperties({"accountName", "eventType", "hotLifespanInDays", "maxDailyDataVolumeBytes", "lookBackTimePeriodSeconds", "dailyDocumentCapVolume", "expirationDate", "creationDate"})
/*  29 */   public EventTypeMetaData(String accountName, String eventType, Integer hotLifespanInDays, Long maxDailyDataVolumeBytes, Long lookBackTimePeriodSeconds, Long dailyDocumentCapVolume, DateTime expirationDate, DateTime creationDate) { this.accountName = accountName;this.eventType = eventType;this.hotLifespanInDays = hotLifespanInDays;this.maxDailyDataVolumeBytes = maxDailyDataVolumeBytes;this.lookBackTimePeriodSeconds = lookBackTimePeriodSeconds;this.dailyDocumentCapVolume = dailyDocumentCapVolume;this.expirationDate = expirationDate;this.creationDate = creationDate;
/*     */   }
/*     */   
/*  32 */   private static final Logger log = LoggerFactory.getLogger(EventTypeMetaData.class);
/*     */   public static final String MAX_DAILY_DATA_VOLUME_BYTES = "maxDailyDataVolumeBytes";
/*     */   private String accountName;
/*     */   
/*     */   public EventTypeMetaData(String accountName, String eventType, EventTypeMetaDataDefaults configuration)
/*     */   {
/*  38 */     this(accountName, eventType, configuration.getHotLifespanInDays(), null, configuration.getLookBackTimePeriodSeconds(), null, null, TimeKeeper.currentUtcTime());
/*     */   }
/*     */   
/*     */   private String eventType;
/*     */   @Min(0L)
/*     */   @Max(720L)
/*     */   private Integer hotLifespanInDays;
/*     */   public void fill(String accountName, String eventType, EventTypeMetaDataDefaults eventTypeMetaDataDefaults) {
/*  46 */     if (this.accountName == null) {
/*  47 */       this.accountName = accountName;
/*     */     }
/*  49 */     if (this.eventType == null) {
/*  50 */       this.eventType = eventType;
/*     */     }
/*  52 */     if (this.hotLifespanInDays == null) {
/*  53 */       this.hotLifespanInDays = eventTypeMetaDataDefaults.getHotLifespanInDays();
/*     */     }
/*  55 */     if (this.lookBackTimePeriodSeconds == null) {
/*  56 */       this.lookBackTimePeriodSeconds = eventTypeMetaDataDefaults.getLookBackTimePeriodSeconds();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getAccountName()
/*     */   {
/*  63 */     return this.accountName; } public void setAccountName(String accountName) { this.accountName = accountName; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  68 */   public String getEventType() { return this.eventType; } public void setEventType(String eventType) { this.eventType = eventType; }
/*     */   
/*     */   private Long maxDailyDataVolumeBytes;
/*     */   private Long lookBackTimePeriodSeconds;
/*     */   private Long dailyDocumentCapVolume;
/*     */   private DateTime expirationDate;
/*     */   public Integer getHotLifespanInDays()
/*     */   {
/*  76 */     return this.hotLifespanInDays; } public void setHotLifespanInDays(Integer hotLifespanInDays) { this.hotLifespanInDays = hotLifespanInDays; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public Long getMaxDailyDataVolumeBytes() { return this.maxDailyDataVolumeBytes; } public void setMaxDailyDataVolumeBytes(Long maxDailyDataVolumeBytes) { this.maxDailyDataVolumeBytes = maxDailyDataVolumeBytes; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   public Long getLookBackTimePeriodSeconds() { return this.lookBackTimePeriodSeconds; } public void setLookBackTimePeriodSeconds(Long lookBackTimePeriodSeconds) { this.lookBackTimePeriodSeconds = lookBackTimePeriodSeconds; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */   public Long getDailyDocumentCapVolume() { return this.dailyDocumentCapVolume; } public void setDailyDocumentCapVolume(Long dailyDocumentCapVolume) { this.dailyDocumentCapVolume = dailyDocumentCapVolume; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 100 */   public DateTime getExpirationDate() { return this.expirationDate; } public void setExpirationDate(DateTime expirationDate) { this.expirationDate = expirationDate; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 105 */   public DateTime getCreationDate() { return this.creationDate; } public void setCreationDate(DateTime creationDate) { this.creationDate = creationDate; } private DateTime creationDate = TimeKeeper.currentUtcTime();
/*     */   
/*     */   public EventTypeMetaData() {}
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/EventTypeMetaData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */