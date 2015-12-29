/*    */ package com.appdynamics.analytics.processor.migration.release40.core33833;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.MetaDataIndexConfiguration;
/*    */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchInput;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.google.common.base.Charsets;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import org.kohsuke.args4j.Option;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class Input
/*    */   extends ElasticSearchInput
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 29 */     if (o == this) return true; if (!(o instanceof Input)) return false; Input other = (Input)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$oldIndexName = getOldIndexName();Object other$oldIndexName = other.getOldIndexName(); if (this$oldIndexName == null ? other$oldIndexName != null : !this$oldIndexName.equals(other$oldIndexName)) return false; Object this$newIndexName = getNewIndexName();Object other$newIndexName = other.getNewIndexName(); if (this$newIndexName == null ? other$newIndexName != null : !this$newIndexName.equals(other$newIndexName)) return false; Object this$newIndexConfig = getNewIndexConfig();Object other$newIndexConfig = other.getNewIndexConfig(); if (this$newIndexConfig == null ? other$newIndexConfig != null : !this$newIndexConfig.equals(other$newIndexConfig)) return false; Object this$newAliasName = getNewAliasName();Object other$newAliasName = other.getNewAliasName(); if (this$newAliasName == null ? other$newAliasName != null : !this$newAliasName.equals(other$newAliasName)) return false; if (getFetchSize() != other.getFetchSize()) return false; if (getRetries() != other.getRetries()) return false; Object this$timeout = getTimeout();Object other$timeout = other.getTimeout();return this$timeout == null ? other$timeout == null : this$timeout.equals(other$timeout); } public boolean canEqual(Object other) { return other instanceof Input; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $oldIndexName = getOldIndexName();result = result * 31 + ($oldIndexName == null ? 0 : $oldIndexName.hashCode());Object $newIndexName = getNewIndexName();result = result * 31 + ($newIndexName == null ? 0 : $newIndexName.hashCode());Object $newIndexConfig = getNewIndexConfig();result = result * 31 + ($newIndexConfig == null ? 0 : $newIndexConfig.hashCode());Object $newAliasName = getNewAliasName();result = result * 31 + ($newAliasName == null ? 0 : $newAliasName.hashCode());result = result * 31 + getFetchSize();result = result * 31 + getRetries();Object $timeout = getTimeout();result = result * 31 + ($timeout == null ? 0 : $timeout.hashCode());return result; }
/* 30 */   public String toString() { return "Input(super=" + super.toString() + ", oldIndexName=" + getOldIndexName() + ", newIndexName=" + getNewIndexName() + ", newIndexConfig=" + getNewIndexConfig() + ", newAliasName=" + getNewAliasName() + ", fetchSize=" + getFetchSize() + ", retries=" + getRetries() + ", timeout=" + getTimeout() + ")"; }
/*    */   
/*    */   @NotNull
/* 33 */   String oldIndexName = "appdynamics_meters";
/* 34 */   public String getOldIndexName() { return this.oldIndexName; } public void setOldIndexName(String oldIndexName) { this.oldIndexName = oldIndexName; }
/*    */   @NotNull
/* 36 */   String newIndexName = "appdynamics_meters_v2";
/* 37 */   public String getNewIndexName() { return this.newIndexName; } public void setNewIndexName(String newIndexName) { this.newIndexName = newIndexName; }
/*    */   
/*    */   @NotNull
/* 40 */   MetaDataIndexConfiguration newIndexConfig; public MetaDataIndexConfiguration getNewIndexConfig() { return this.newIndexConfig; } public void setNewIndexConfig(MetaDataIndexConfiguration newIndexConfig) { this.newIndexConfig = newIndexConfig; }
/*    */   @NotNull
/* 42 */   String newAliasName = "appdynamics_meters";
/* 43 */   public String getNewAliasName() { return this.newAliasName; } public void setNewAliasName(String newAliasName) { this.newAliasName = newAliasName; } @Option(name="-fs", aliases={"--fetch-size"}, usage="The fetch size", metaVar="FETCH-SIZE", required=false)
/*    */   @Min(1L)
/* 45 */   int fetchSize = 100;
/*    */   
/*    */ 
/* 48 */   public int getFetchSize() { return this.fetchSize; } public void setFetchSize(int fetchSize) { this.fetchSize = fetchSize; } @Option(name="-r", aliases={"--retries"}, usage="The retry count", metaVar="RETRIES", required=false)
/*    */   @Min(1L)
/* 50 */   int retries = 6;
/*    */   
/* 52 */   public int getRetries() { return this.retries; } public void setRetries(int retries) { this.retries = retries; } @NotNull
/*    */   @Valid
/* 54 */   TimeUnitConfiguration timeout = new TimeUnitConfiguration(1L, TimeUnit.MINUTES);
/*    */   
/* 56 */   public TimeUnitConfiguration getTimeout() { return this.timeout; } public void setTimeout(TimeUnitConfiguration timeout) { this.timeout = timeout; }
/*    */   
/*    */   public Input() {
/* 59 */     String s = "settings:\n  number_of_shards: 2\n  number_of_replicas: 1\nmappings:\n  bytes:\n    properties:\n      accountName:\n        type: \"string\"\n        index: not_analyzed\n      eventType:\n        type: \"string\"\n        index: not_analyzed\n      meterType:\n        type: \"string\"\n        index: not_analyzed\n      day:\n        type: \"date\"\n      hour:\n        type: \"integer\"\n      countTotal:\n        type: \"long\"";
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     try
/*    */     {
/* 82 */       this.newIndexConfig = ((MetaDataIndexConfiguration)Reader.readFrom(MetaDataIndexConfiguration.class, new ByteArrayInputStream(s.getBytes(Charsets.UTF_8))));
/*    */     }
/*    */     catch (IOException e) {
/* 85 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/migration/release40/core33833/Input.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */