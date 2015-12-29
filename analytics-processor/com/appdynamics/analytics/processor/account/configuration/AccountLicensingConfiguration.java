/*    */ package com.appdynamics.analytics.processor.account.configuration;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ import java.beans.ConstructorProperties;
/*    */ import lombok.NonNull;
/*    */ import org.apache.commons.lang.ObjectUtils;
/*    */ import org.joda.time.DateTime;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class AccountLicensingConfiguration
/*    */   implements Comparable<AccountLicensingConfiguration>
/*    */ {
/*    */   @NonNull
/*    */   String eventType;
/*    */   @NonNull
/*    */   Long dataRetentionPeriodDays;
/*    */   Long maxDailyBytes;
/*    */   Long maxDailyDocuments;
/*    */   DateTime expirationDate;
/*    */   
/* 25 */   public String toString() { return "AccountLicensingConfiguration(eventType=" + getEventType() + ", dataRetentionPeriodDays=" + getDataRetentionPeriodDays() + ", maxDailyBytes=" + getMaxDailyBytes() + ", maxDailyDocuments=" + getMaxDailyDocuments() + ", expirationDate=" + getExpirationDate() + ")"; }
/*    */   @ConstructorProperties({"eventType", "dataRetentionPeriodDays", "maxDailyBytes", "maxDailyDocuments", "expirationDate"})
/* 27 */   public AccountLicensingConfiguration(@NonNull String eventType, @NonNull Long dataRetentionPeriodDays, Long maxDailyBytes, Long maxDailyDocuments, DateTime expirationDate) { if (eventType == null) throw new NullPointerException("eventType"); if (dataRetentionPeriodDays == null) throw new NullPointerException("dataRetentionPeriodDays"); this.eventType = eventType;this.dataRetentionPeriodDays = dataRetentionPeriodDays;this.maxDailyBytes = maxDailyBytes;this.maxDailyDocuments = maxDailyDocuments;this.expirationDate = expirationDate; }
/*    */   
/* 29 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof AccountLicensingConfiguration)) return false; AccountLicensingConfiguration other = (AccountLicensingConfiguration)o; if (!other.canEqual(this)) return false; Object this$eventType = getEventType();Object other$eventType = other.getEventType(); if (this$eventType == null ? other$eventType != null : !this$eventType.equals(other$eventType)) return false; Object this$dataRetentionPeriodDays = getDataRetentionPeriodDays();Object other$dataRetentionPeriodDays = other.getDataRetentionPeriodDays(); if (this$dataRetentionPeriodDays == null ? other$dataRetentionPeriodDays != null : !this$dataRetentionPeriodDays.equals(other$dataRetentionPeriodDays)) return false; Object this$maxDailyBytes = getMaxDailyBytes();Object other$maxDailyBytes = other.getMaxDailyBytes(); if (this$maxDailyBytes == null ? other$maxDailyBytes != null : !this$maxDailyBytes.equals(other$maxDailyBytes)) return false; Object this$maxDailyDocuments = getMaxDailyDocuments();Object other$maxDailyDocuments = other.getMaxDailyDocuments(); if (this$maxDailyDocuments == null ? other$maxDailyDocuments != null : !this$maxDailyDocuments.equals(other$maxDailyDocuments)) return false; Object this$expirationDate = getExpirationDate();Object other$expirationDate = other.getExpirationDate();return this$expirationDate == null ? other$expirationDate == null : this$expirationDate.equals(other$expirationDate); } public boolean canEqual(Object other) { return other instanceof AccountLicensingConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $eventType = getEventType();result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());Object $dataRetentionPeriodDays = getDataRetentionPeriodDays();result = result * 31 + ($dataRetentionPeriodDays == null ? 0 : $dataRetentionPeriodDays.hashCode());Object $maxDailyBytes = getMaxDailyBytes();result = result * 31 + ($maxDailyBytes == null ? 0 : $maxDailyBytes.hashCode());Object $maxDailyDocuments = getMaxDailyDocuments();result = result * 31 + ($maxDailyDocuments == null ? 0 : $maxDailyDocuments.hashCode());Object $expirationDate = getExpirationDate();result = result * 31 + ($expirationDate == null ? 0 : $expirationDate.hashCode());return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @NonNull
/* 37 */   public String getEventType() { return this.eventType; } public void setEventType(@NonNull String eventType) { if (eventType == null) throw new NullPointerException("eventType"); this.eventType = eventType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @NonNull
/* 43 */   public Long getDataRetentionPeriodDays() { return this.dataRetentionPeriodDays; } public void setDataRetentionPeriodDays(@NonNull Long dataRetentionPeriodDays) { if (dataRetentionPeriodDays == null) throw new NullPointerException("dataRetentionPeriodDays"); this.dataRetentionPeriodDays = dataRetentionPeriodDays;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 48 */   public Long getMaxDailyBytes() { return this.maxDailyBytes; } public void setMaxDailyBytes(Long maxDailyBytes) { this.maxDailyBytes = maxDailyBytes; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 53 */   public Long getMaxDailyDocuments() { return this.maxDailyDocuments; } public void setMaxDailyDocuments(Long maxDailyDocuments) { this.maxDailyDocuments = maxDailyDocuments; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 58 */   public DateTime getExpirationDate() { return this.expirationDate; } public void setExpirationDate(DateTime expirationDate) { this.expirationDate = expirationDate; }
/*    */   
/*    */   public int compareTo(AccountLicensingConfiguration other)
/*    */   {
/* 62 */     if (other == null) {
/* 63 */       return 1;
/*    */     }
/*    */     
/*    */ 
/* 67 */     if (ObjectUtils.compare(this.eventType, other.eventType) != 0) {
/* 68 */       return ObjectUtils.compare(this.eventType, other.eventType);
/*    */     }
/* 70 */     if (ObjectUtils.compare(this.dataRetentionPeriodDays, other.dataRetentionPeriodDays) != 0) {
/* 71 */       return ObjectUtils.compare(this.dataRetentionPeriodDays, other.dataRetentionPeriodDays);
/*    */     }
/* 73 */     if (ObjectUtils.compare(this.maxDailyBytes, other.maxDailyBytes) != 0) {
/* 74 */       return ObjectUtils.compare(this.maxDailyBytes, other.maxDailyBytes);
/*    */     }
/* 76 */     if (ObjectUtils.compare(this.maxDailyDocuments, other.maxDailyDocuments) != 0) {
/* 77 */       return ObjectUtils.compare(this.maxDailyDocuments, other.maxDailyDocuments);
/*    */     }
/* 79 */     if (ObjectUtils.compare(this.expirationDate, other.expirationDate) != 0) {
/* 80 */       return ObjectUtils.compare(this.expirationDate, other.expirationDate);
/*    */     }
/* 82 */     return 0;
/*    */   }
/*    */   
/*    */   public AccountLicensingConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/account/configuration/AccountLicensingConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */