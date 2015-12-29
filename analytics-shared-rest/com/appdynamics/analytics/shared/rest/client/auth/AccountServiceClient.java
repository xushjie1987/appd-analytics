/*     */ package com.appdynamics.analytics.shared.rest.client.auth;
/*     */ 
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import lombok.NonNull;
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
/*     */ public abstract interface AccountServiceClient
/*     */ {
/*     */   public abstract void addOrUpdateAccounts(String paramString, List<Account> paramList)
/*     */     throws RestException;
/*     */   
/*     */   public abstract String getAccountConfiguration(String paramString1, String paramString2)
/*     */     throws RestException;
/*     */   
/*     */   public abstract List<Account> getAccountConfigurations(String paramString, List<String> paramList)
/*     */     throws IOException, RestException;
/*     */   
/*     */   @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
/*     */   @JsonIgnoreProperties(ignoreUnknown=true)
/*     */   public static class Account
/*     */   {
/*     */     @NotNull
/*     */     private String accountName;
/*     */     @NotNull
/*     */     private String accessKey;
/*     */     private String expirationDate;
/*     */     private String eumAccountName;
/*     */     private List<AccountServiceClient.LicenseData> licensingConfigurations;
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 113 */       if (o == this) return true; if (!(o instanceof Account)) return false; Account other = (Account)o; if (!other.canEqual(this)) return false; Object this$accountName = getAccountName();Object other$accountName = other.getAccountName(); if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$accessKey = getAccessKey();Object other$accessKey = other.getAccessKey(); if (this$accessKey == null ? other$accessKey != null : !this$accessKey.equals(other$accessKey)) return false; Object this$expirationDate = getExpirationDate();Object other$expirationDate = other.getExpirationDate(); if (this$expirationDate == null ? other$expirationDate != null : !this$expirationDate.equals(other$expirationDate)) return false; Object this$eumAccountName = getEumAccountName();Object other$eumAccountName = other.getEumAccountName(); if (this$eumAccountName == null ? other$eumAccountName != null : !this$eumAccountName.equals(other$eumAccountName)) return false; Object this$licensingConfigurations = getLicensingConfigurations();Object other$licensingConfigurations = other.getLicensingConfigurations();return this$licensingConfigurations == null ? other$licensingConfigurations == null : this$licensingConfigurations.equals(other$licensingConfigurations); } public boolean canEqual(Object other) { return other instanceof Account; } public int hashCode() { int PRIME = 31;int result = 1;Object $accountName = getAccountName();result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $accessKey = getAccessKey();result = result * 31 + ($accessKey == null ? 0 : $accessKey.hashCode());Object $expirationDate = getExpirationDate();result = result * 31 + ($expirationDate == null ? 0 : $expirationDate.hashCode());Object $eumAccountName = getEumAccountName();result = result * 31 + ($eumAccountName == null ? 0 : $eumAccountName.hashCode());Object $licensingConfigurations = getLicensingConfigurations();result = result * 31 + ($licensingConfigurations == null ? 0 : $licensingConfigurations.hashCode());return result; } public String toString() { return "AccountServiceClient.Account(accountName=" + getAccountName() + ", accessKey=" + getAccessKey() + ", expirationDate=" + getExpirationDate() + ", eumAccountName=" + getEumAccountName() + ", licensingConfigurations=" + getLicensingConfigurations() + ")"; }
/*     */     @ConstructorProperties({"accountName", "accessKey", "expirationDate", "eumAccountName", "licensingConfigurations"})
/* 115 */     public Account(String accountName, String accessKey, String expirationDate, String eumAccountName, List<AccountServiceClient.LicenseData> licensingConfigurations) { this.accountName = accountName;this.accessKey = accessKey;this.expirationDate = expirationDate;this.eumAccountName = eumAccountName;this.licensingConfigurations = licensingConfigurations;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 120 */     public String getAccountName() { return this.accountName; } public void setAccountName(String accountName) { this.accountName = accountName; }
/*     */     
/*     */ 
/* 123 */     public String getAccessKey() { return this.accessKey; } public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
/*     */     
/* 125 */     public String getExpirationDate() { return this.expirationDate; } public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
/*     */     
/* 127 */     public String getEumAccountName() { return this.eumAccountName; } public void setEumAccountName(String eumAccountName) { this.eumAccountName = eumAccountName; }
/*     */     
/* 129 */     public List<AccountServiceClient.LicenseData> getLicensingConfigurations() { return this.licensingConfigurations; } public void setLicensingConfigurations(List<AccountServiceClient.LicenseData> licensingConfigurations) { this.licensingConfigurations = licensingConfigurations; }
/*     */     
/*     */     public Account(String accountName, String accessKey, String expirationDate) {
/* 132 */       this.accountName = accountName;
/* 133 */       this.accessKey = accessKey;
/* 134 */       this.expirationDate = expirationDate;
/*     */     }
/*     */     
/*     */     public Account(String accountName, String accessKey, String expirationDate, List<AccountServiceClient.LicenseData> licensingConfigurations)
/*     */     {
/* 139 */       this(accountName, accessKey, expirationDate);
/* 140 */       this.licensingConfigurations = licensingConfigurations; }
/*     */     
/*     */     public Account() {} }
/*     */   
/*     */   @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
/*     */   @JsonIgnoreProperties(ignoreUnknown=true)
/*     */   public static class LicenseData { @NonNull
/*     */     String eventType;
/*     */     
/* 149 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof LicenseData)) return false; LicenseData other = (LicenseData)o; if (!other.canEqual(this)) return false; Object this$eventType = getEventType();Object other$eventType = other.getEventType(); if (this$eventType == null ? other$eventType != null : !this$eventType.equals(other$eventType)) return false; Object this$dataRetentionPeriodDays = getDataRetentionPeriodDays();Object other$dataRetentionPeriodDays = other.getDataRetentionPeriodDays(); if (this$dataRetentionPeriodDays == null ? other$dataRetentionPeriodDays != null : !this$dataRetentionPeriodDays.equals(other$dataRetentionPeriodDays)) return false; Object this$maxDailyBytes = getMaxDailyBytes();Object other$maxDailyBytes = other.getMaxDailyBytes(); if (this$maxDailyBytes == null ? other$maxDailyBytes != null : !this$maxDailyBytes.equals(other$maxDailyBytes)) return false; Object this$maxDailyDocuments = getMaxDailyDocuments();Object other$maxDailyDocuments = other.getMaxDailyDocuments(); if (this$maxDailyDocuments == null ? other$maxDailyDocuments != null : !this$maxDailyDocuments.equals(other$maxDailyDocuments)) return false; Object this$expirationDate = getExpirationDate();Object other$expirationDate = other.getExpirationDate();return this$expirationDate == null ? other$expirationDate == null : this$expirationDate.equals(other$expirationDate); } public boolean canEqual(Object other) { return other instanceof LicenseData; } public int hashCode() { int PRIME = 31;int result = 1;Object $eventType = getEventType();result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());Object $dataRetentionPeriodDays = getDataRetentionPeriodDays();result = result * 31 + ($dataRetentionPeriodDays == null ? 0 : $dataRetentionPeriodDays.hashCode());Object $maxDailyBytes = getMaxDailyBytes();result = result * 31 + ($maxDailyBytes == null ? 0 : $maxDailyBytes.hashCode());Object $maxDailyDocuments = getMaxDailyDocuments();result = result * 31 + ($maxDailyDocuments == null ? 0 : $maxDailyDocuments.hashCode());Object $expirationDate = getExpirationDate();result = result * 31 + ($expirationDate == null ? 0 : $expirationDate.hashCode());return result; } public String toString() { return "AccountServiceClient.LicenseData(eventType=" + getEventType() + ", dataRetentionPeriodDays=" + getDataRetentionPeriodDays() + ", maxDailyBytes=" + getMaxDailyBytes() + ", maxDailyDocuments=" + getMaxDailyDocuments() + ", expirationDate=" + getExpirationDate() + ")"; }
/*     */     @ConstructorProperties({"eventType", "dataRetentionPeriodDays", "maxDailyBytes", "maxDailyDocuments", "expirationDate"})
/* 151 */     public LicenseData(@NonNull String eventType, @NonNull Long dataRetentionPeriodDays, Long maxDailyBytes, Long maxDailyDocuments, String expirationDate) { if (eventType == null) throw new NullPointerException("eventType"); if (dataRetentionPeriodDays == null) throw new NullPointerException("dataRetentionPeriodDays"); this.eventType = eventType;this.dataRetentionPeriodDays = dataRetentionPeriodDays;this.maxDailyBytes = maxDailyBytes;this.maxDailyDocuments = maxDailyDocuments;this.expirationDate = expirationDate;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @NonNull
/*     */     Long dataRetentionPeriodDays;
/*     */     
/*     */     Long maxDailyBytes;
/*     */     Long maxDailyDocuments;
/*     */     String expirationDate;
/*     */     @NonNull
/* 163 */     public String getEventType() { return this.eventType; } public void setEventType(@NonNull String eventType) { if (eventType == null) throw new NullPointerException("eventType"); this.eventType = eventType;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @NonNull
/* 169 */     public Long getDataRetentionPeriodDays() { return this.dataRetentionPeriodDays; } public void setDataRetentionPeriodDays(@NonNull Long dataRetentionPeriodDays) { if (dataRetentionPeriodDays == null) throw new NullPointerException("dataRetentionPeriodDays"); this.dataRetentionPeriodDays = dataRetentionPeriodDays;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 174 */     public Long getMaxDailyBytes() { return this.maxDailyBytes; } public void setMaxDailyBytes(Long maxDailyBytes) { this.maxDailyBytes = maxDailyBytes; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 179 */     public Long getMaxDailyDocuments() { return this.maxDailyDocuments; } public void setMaxDailyDocuments(Long maxDailyDocuments) { this.maxDailyDocuments = maxDailyDocuments; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 184 */     public String getExpirationDate() { return this.expirationDate; } public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
/*     */     
/*     */     public LicenseData() {}
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/auth/AccountServiceClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */