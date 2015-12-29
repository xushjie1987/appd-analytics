/*    */ package com.appdynamics.analytics.agent.pipeline.event;
/*    */ 
/*    */ import javax.validation.constraints.Min;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ public class EventServiceConfiguration
/*    */ {
/*    */   private static final int DEFAULT_SOCKET_TIMEOUT_MILLIS = 30000;
/*    */   private static final int DEFAULT_CONNECTION_TIMEOUT_MILLIS = 30000;
/*    */   @NotEmpty
/*    */   @org.hibernate.validator.constraints.URL
/*    */   String endpoint;
/*    */   @NotEmpty
/*    */   String accountName;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 18 */     if (o == this) return true; if (!(o instanceof EventServiceConfiguration)) return false; EventServiceConfiguration other = (EventServiceConfiguration)o; if (!other.canEqual(this)) return false; Object this$endpoint = getEndpoint();Object other$endpoint = other.getEndpoint(); if (this$endpoint == null ? other$endpoint != null : !this$endpoint.equals(other$endpoint)) return false; Object this$accountName = getAccountName();Object other$accountName = other.getAccountName(); if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$accessKey = getAccessKey();Object other$accessKey = other.getAccessKey(); if (this$accessKey == null ? other$accessKey != null : !this$accessKey.equals(other$accessKey)) return false; Object this$proxyHost = getProxyHost();Object other$proxyHost = other.getProxyHost(); if (this$proxyHost == null ? other$proxyHost != null : !this$proxyHost.equals(other$proxyHost)) return false; Object this$proxyPort = getProxyPort();Object other$proxyPort = other.getProxyPort(); if (this$proxyPort == null ? other$proxyPort != null : !this$proxyPort.equals(other$proxyPort)) return false; Object this$proxyUsername = getProxyUsername();Object other$proxyUsername = other.getProxyUsername(); if (this$proxyUsername == null ? other$proxyUsername != null : !this$proxyUsername.equals(other$proxyUsername)) return false; Object this$proxyPassword = getProxyPassword();Object other$proxyPassword = other.getProxyPassword(); if (this$proxyPassword == null ? other$proxyPassword != null : !this$proxyPassword.equals(other$proxyPassword)) return false; if (getSocketTimeoutMillis() != other.getSocketTimeoutMillis()) return false; return getConnectionTimeoutMillis() == other.getConnectionTimeoutMillis(); } public boolean canEqual(Object other) { return other instanceof EventServiceConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $endpoint = getEndpoint();result = result * 31 + ($endpoint == null ? 0 : $endpoint.hashCode());Object $accountName = getAccountName();result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $accessKey = getAccessKey();result = result * 31 + ($accessKey == null ? 0 : $accessKey.hashCode());Object $proxyHost = getProxyHost();result = result * 31 + ($proxyHost == null ? 0 : $proxyHost.hashCode());Object $proxyPort = getProxyPort();result = result * 31 + ($proxyPort == null ? 0 : $proxyPort.hashCode());Object $proxyUsername = getProxyUsername();result = result * 31 + ($proxyUsername == null ? 0 : $proxyUsername.hashCode());Object $proxyPassword = getProxyPassword();result = result * 31 + ($proxyPassword == null ? 0 : $proxyPassword.hashCode());result = result * 31 + getSocketTimeoutMillis();result = result * 31 + getConnectionTimeoutMillis();return result; } public String toString() { return "EventServiceConfiguration(endpoint=" + getEndpoint() + ", accountName=" + getAccountName() + ", accessKey=" + getAccessKey() + ", proxyHost=" + getProxyHost() + ", proxyPort=" + getProxyPort() + ", proxyUsername=" + getProxyUsername() + ", proxyPassword=" + getProxyPassword() + ", socketTimeoutMillis=" + getSocketTimeoutMillis() + ", connectionTimeoutMillis=" + getConnectionTimeoutMillis() + ")"; }
/*    */   
/*    */   @NotEmpty
/*    */   String accessKey;
/*    */   String proxyHost;
/*    */   Integer proxyPort;
/*    */   String proxyUsername;
/*    */   String proxyPassword;
/*    */   public String getEndpoint() {
/* 27 */     return this.endpoint; } public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 32 */   public String getAccountName() { return this.accountName; } public void setAccountName(String accountName) { this.accountName = accountName; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 38 */   public String getAccessKey() { return this.accessKey; } public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
/*    */   
/*    */ 
/*    */ 
/* 42 */   public String getProxyHost() { return this.proxyHost; } public void setProxyHost(String proxyHost) { this.proxyHost = proxyHost; }
/*    */   
/*    */ 
/*    */ 
/* 46 */   public Integer getProxyPort() { return this.proxyPort; } public void setProxyPort(Integer proxyPort) { this.proxyPort = proxyPort; }
/*    */   
/*    */ 
/*    */ 
/* 50 */   public String getProxyUsername() { return this.proxyUsername; } public void setProxyUsername(String proxyUsername) { this.proxyUsername = proxyUsername; }
/*    */   
/*    */ 
/*    */ 
/* 54 */   public String getProxyPassword() { return this.proxyPassword; } public void setProxyPassword(String proxyPassword) { this.proxyPassword = proxyPassword; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Min(1000L)
/* 61 */   int socketTimeoutMillis = 30000;
/* 62 */   public int getSocketTimeoutMillis() { return this.socketTimeoutMillis; } public void setSocketTimeoutMillis(int socketTimeoutMillis) { this.socketTimeoutMillis = socketTimeoutMillis; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @Min(1000L)
/* 68 */   int connectionTimeoutMillis = 30000;
/* 69 */   public int getConnectionTimeoutMillis() { return this.connectionTimeoutMillis; } public void setConnectionTimeoutMillis(int connectionTimeoutMillis) { this.connectionTimeoutMillis = connectionTimeoutMillis; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/event/EventServiceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */