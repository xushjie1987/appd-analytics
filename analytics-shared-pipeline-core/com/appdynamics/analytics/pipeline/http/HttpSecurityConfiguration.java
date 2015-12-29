/*    */ package com.appdynamics.analytics.pipeline.http;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpSecurityConfiguration
/*    */ {
/*    */   @JsonProperty
/*    */   TrustStoreConfiguration trustStore;
/*    */   
/* 21 */   public String toString() { return "HttpSecurityConfiguration(trustStore=" + getTrustStore() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $trustStore = getTrustStore();result = result * 31 + ($trustStore == null ? 0 : $trustStore.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof HttpSecurityConfiguration; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof HttpSecurityConfiguration)) return false; HttpSecurityConfiguration other = (HttpSecurityConfiguration)o; if (!other.canEqual(this)) return false; Object this$trustStore = getTrustStore();Object other$trustStore = other.getTrustStore();return this$trustStore == null ? other$trustStore == null : this$trustStore.equals(other$trustStore);
/*    */   }
/*    */   
/* 24 */   public void setTrustStore(TrustStoreConfiguration trustStore) { this.trustStore = trustStore; } public TrustStoreConfiguration getTrustStore() { return this.trustStore; }
/*    */   
/*    */   public static class TrustStoreConfiguration {
/* 27 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof TrustStoreConfiguration)) return false; TrustStoreConfiguration other = (TrustStoreConfiguration)o; if (!other.canEqual(this)) return false; Object this$file = getFile();Object other$file = other.getFile(); if (this$file == null ? other$file != null : !this$file.equals(other$file)) return false; Object this$password = getPassword();Object other$password = other.getPassword();return this$password == null ? other$password == null : this$password.equals(other$password); } public boolean canEqual(Object other) { return other instanceof TrustStoreConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $file = getFile();result = result * 31 + ($file == null ? 0 : $file.hashCode());Object $password = getPassword();result = result * 31 + ($password == null ? 0 : $password.hashCode());return result; } public String toString() { return "HttpSecurityConfiguration.TrustStoreConfiguration(file=" + getFile() + ", password=" + getPassword() + ")"; }
/*    */     
/*    */ 
/*    */ 
/* 31 */     public String getFile() { return this.file; } public void setFile(String file) { this.file = file; }
/*    */     
/*    */ 
/*    */ 
/* 35 */     public String getPassword() { return this.password; } public void setPassword(String password) { this.password = password; }
/*    */     
/*    */     @JsonProperty
/*    */     @NotNull
/*    */     String file;
/*    */     @JsonProperty
/*    */     @NotNull
/*    */     String password;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/http/HttpSecurityConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */