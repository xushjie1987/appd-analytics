/*     */ package com.appdynamics.analytics.processor.account.configuration;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.exception.IllegalAccountNameException;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.google.common.base.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.joda.time.DateTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JsonIgnoreProperties(ignoreUnknown=true)
/*     */ public class AccountConfiguration
/*     */   implements Comparable<AccountConfiguration>
/*     */ {
/*     */   public static final String INVALID_ACCOUNT_NAME_CHARACTERS = "\\\\*?<>|:\\[\\]{}'\"*/, ";
/*     */   @NotNull
/*     */   String accountName;
/*     */   @NotNull
/*     */   String accessKey;
/*     */   DateTime expirationDate;
/*     */   String eumAccountName;
/*     */   List<AccountLicensingConfiguration> licensingConfigurations;
/*     */   
/*  29 */   public String toString() { return "AccountConfiguration(accountName=" + getAccountName() + ", expirationDate=" + getExpirationDate() + ", licensingConfigurations=" + getLicensingConfigurations() + ")"; }
/*     */   
/*  31 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof AccountConfiguration)) return false; AccountConfiguration other = (AccountConfiguration)o; if (!other.canEqual(this)) return false; Object this$accountName = getAccountName();Object other$accountName = other.getAccountName(); if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$accessKey = getAccessKey();Object other$accessKey = other.getAccessKey(); if (this$accessKey == null ? other$accessKey != null : !this$accessKey.equals(other$accessKey)) return false; Object this$expirationDate = getExpirationDate();Object other$expirationDate = other.getExpirationDate(); if (this$expirationDate == null ? other$expirationDate != null : !this$expirationDate.equals(other$expirationDate)) return false; Object this$eumAccountName = getEumAccountName();Object other$eumAccountName = other.getEumAccountName(); if (this$eumAccountName == null ? other$eumAccountName != null : !this$eumAccountName.equals(other$eumAccountName)) return false; Object this$licensingConfigurations = getLicensingConfigurations();Object other$licensingConfigurations = other.getLicensingConfigurations();return this$licensingConfigurations == null ? other$licensingConfigurations == null : this$licensingConfigurations.equals(other$licensingConfigurations); } public boolean canEqual(Object other) { return other instanceof AccountConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $accountName = getAccountName();result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $accessKey = getAccessKey();result = result * 31 + ($accessKey == null ? 0 : $accessKey.hashCode());Object $expirationDate = getExpirationDate();result = result * 31 + ($expirationDate == null ? 0 : $expirationDate.hashCode());Object $eumAccountName = getEumAccountName();result = result * 31 + ($eumAccountName == null ? 0 : $eumAccountName.hashCode());Object $licensingConfigurations = getLicensingConfigurations();result = result * 31 + ($licensingConfigurations == null ? 0 : $licensingConfigurations.hashCode());return result;
/*     */   }
/*     */   
/*     */   public String getAccountName()
/*     */   {
/*  36 */     return this.accountName;
/*     */   }
/*     */   
/*  39 */   public String getAccessKey() { return this.accessKey; }
/*     */   
/*  41 */   public DateTime getExpirationDate() { return this.expirationDate; }
/*     */   
/*  43 */   public void setEumAccountName(String eumAccountName) { this.eumAccountName = eumAccountName; }
/*  44 */   public String getEumAccountName() { return this.eumAccountName; }
/*     */   
/*     */ 
/*     */   public AccountConfiguration()
/*     */   {
/*  49 */     this.licensingConfigurations = new ArrayList();
/*     */   }
/*     */   
/*     */   public AccountConfiguration(String accountName, String accessKey, DateTime expirationDate) {
/*  53 */     this();
/*  54 */     this.accountName = normalizeAccountName(accountName);
/*  55 */     this.accessKey = accessKey;
/*  56 */     this.expirationDate = expirationDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<AccountLicensingConfiguration> getLicensingConfigurations()
/*     */   {
/*  65 */     Collections.sort(this.licensingConfigurations);
/*  66 */     return this.licensingConfigurations;
/*     */   }
/*     */   
/*     */   public int compareTo(@NotNull AccountConfiguration o)
/*     */   {
/*  71 */     return this.accountName.compareTo(o.accountName);
/*     */   }
/*     */   
/*     */   public void setAccountName(String accountName) {
/*  75 */     this.accountName = normalizeAccountName(accountName);
/*     */   }
/*     */   
/*     */   public void setAccessKey(String accessKey) {
/*  79 */     this.accessKey = accessKey;
/*     */   }
/*     */   
/*     */   public void setExpirationDate(DateTime expirationDate) {
/*  83 */     this.expirationDate = expirationDate;
/*     */   }
/*     */   
/*     */   public void setLicensingConfigurations(List<AccountLicensingConfiguration> licensingConfigurations) {
/*  87 */     this.licensingConfigurations = licensingConfigurations;
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
/*     */   public static String normalizeAccountName(String accountName)
/*     */   {
/* 100 */     if ((Strings.isNullOrEmpty(accountName)) || (accountName.matches(".*?[\\\\*?<>|:\\[\\]{}'\"*/, ].*")))
/*     */     {
/* 102 */       throw new IllegalAccountNameException("AccountName [" + accountName + "] must be non-null, not empty, and" + " not contain any of the following characters: [:\\[\\]{}'\"*/,]");
/*     */     }
/*     */     
/* 105 */     return accountName.toLowerCase();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/account/configuration/AccountConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */