/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.compaction;
/*    */ 
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class IndexCompactionParameters { @NotNull
/*    */   String indexToCompact;
/*    */   @NotNull
/*    */   String clusterToProcess;
/*    */   Double flushThreshold;
/* 11 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof IndexCompactionParameters)) return false; IndexCompactionParameters other = (IndexCompactionParameters)o; if (!other.canEqual(this)) return false; Object this$indexToCompact = getIndexToCompact();Object other$indexToCompact = other.getIndexToCompact(); if (this$indexToCompact == null ? other$indexToCompact != null : !this$indexToCompact.equals(other$indexToCompact)) return false; Object this$clusterToProcess = getClusterToProcess();Object other$clusterToProcess = other.getClusterToProcess(); if (this$clusterToProcess == null ? other$clusterToProcess != null : !this$clusterToProcess.equals(other$clusterToProcess)) return false; Object this$flushThreshold = getFlushThreshold();Object other$flushThreshold = other.getFlushThreshold(); if (this$flushThreshold == null ? other$flushThreshold != null : !this$flushThreshold.equals(other$flushThreshold)) return false; Object this$debugMode = getDebugMode();Object other$debugMode = other.getDebugMode(); if (this$debugMode == null ? other$debugMode != null : !this$debugMode.equals(other$debugMode)) return false; Object this$scrollTimeout = getScrollTimeout();Object other$scrollTimeout = other.getScrollTimeout(); if (this$scrollTimeout == null ? other$scrollTimeout != null : !this$scrollTimeout.equals(other$scrollTimeout)) return false; Object this$scrollBatchSize = getScrollBatchSize();Object other$scrollBatchSize = other.getScrollBatchSize(); if (this$scrollBatchSize == null ? other$scrollBatchSize != null : !this$scrollBatchSize.equals(other$scrollBatchSize)) return false; Object this$copyThreadPoolSize = getCopyThreadPoolSize();Object other$copyThreadPoolSize = other.getCopyThreadPoolSize(); if (this$copyThreadPoolSize == null ? other$copyThreadPoolSize != null : !this$copyThreadPoolSize.equals(other$copyThreadPoolSize)) return false; Object this$numberOfParallelCopiers = getNumberOfParallelCopiers();Object other$numberOfParallelCopiers = other.getNumberOfParallelCopiers();return this$numberOfParallelCopiers == null ? other$numberOfParallelCopiers == null : this$numberOfParallelCopiers.equals(other$numberOfParallelCopiers); } public boolean canEqual(Object other) { return other instanceof IndexCompactionParameters; } public int hashCode() { int PRIME = 31;int result = 1;Object $indexToCompact = getIndexToCompact();result = result * 31 + ($indexToCompact == null ? 0 : $indexToCompact.hashCode());Object $clusterToProcess = getClusterToProcess();result = result * 31 + ($clusterToProcess == null ? 0 : $clusterToProcess.hashCode());Object $flushThreshold = getFlushThreshold();result = result * 31 + ($flushThreshold == null ? 0 : $flushThreshold.hashCode());Object $debugMode = getDebugMode();result = result * 31 + ($debugMode == null ? 0 : $debugMode.hashCode());Object $scrollTimeout = getScrollTimeout();result = result * 31 + ($scrollTimeout == null ? 0 : $scrollTimeout.hashCode());Object $scrollBatchSize = getScrollBatchSize();result = result * 31 + ($scrollBatchSize == null ? 0 : $scrollBatchSize.hashCode());Object $copyThreadPoolSize = getCopyThreadPoolSize();result = result * 31 + ($copyThreadPoolSize == null ? 0 : $copyThreadPoolSize.hashCode());Object $numberOfParallelCopiers = getNumberOfParallelCopiers();result = result * 31 + ($numberOfParallelCopiers == null ? 0 : $numberOfParallelCopiers.hashCode());return result; } public String toString() { return "IndexCompactionParameters(indexToCompact=" + getIndexToCompact() + ", clusterToProcess=" + getClusterToProcess() + ", flushThreshold=" + getFlushThreshold() + ", debugMode=" + getDebugMode() + ", scrollTimeout=" + getScrollTimeout() + ", scrollBatchSize=" + getScrollBatchSize() + ", copyThreadPoolSize=" + getCopyThreadPoolSize() + ", numberOfParallelCopiers=" + getNumberOfParallelCopiers() + ")"; }
/*    */   
/*    */   Boolean debugMode;
/*    */   Long scrollTimeout;
/* 15 */   public String getIndexToCompact() { return this.indexToCompact; } public void setIndexToCompact(String indexToCompact) { this.indexToCompact = indexToCompact; }
/*    */   Long scrollBatchSize;
/* 17 */   public String getClusterToProcess() { return this.clusterToProcess; } public void setClusterToProcess(String clusterToProcess) { this.clusterToProcess = clusterToProcess; }
/* 18 */   public Double getFlushThreshold() { return this.flushThreshold; } public void setFlushThreshold(Double flushThreshold) { this.flushThreshold = flushThreshold; }
/* 19 */   public Boolean getDebugMode() { return this.debugMode; } public void setDebugMode(Boolean debugMode) { this.debugMode = debugMode; }
/* 20 */   public Long getScrollTimeout() { return this.scrollTimeout; } public void setScrollTimeout(Long scrollTimeout) { this.scrollTimeout = scrollTimeout; }
/* 21 */   public Long getScrollBatchSize() { return this.scrollBatchSize; } public void setScrollBatchSize(Long scrollBatchSize) { this.scrollBatchSize = scrollBatchSize; }
/* 22 */   public Integer getCopyThreadPoolSize() { return this.copyThreadPoolSize; } public void setCopyThreadPoolSize(Integer copyThreadPoolSize) { this.copyThreadPoolSize = copyThreadPoolSize; }
/* 23 */   public Integer getNumberOfParallelCopiers() { return this.numberOfParallelCopiers; } public void setNumberOfParallelCopiers(Integer numberOfParallelCopiers) { this.numberOfParallelCopiers = numberOfParallelCopiers; }
/*    */   
/*    */   public void mergeIndexCompactionParametersWithDefaults(IndexCompactionParameters defaultIndexCompactionParameters) {
/* 26 */     if (this.flushThreshold == null) {
/* 27 */       this.flushThreshold = defaultIndexCompactionParameters.flushThreshold;
/*    */     }
/* 29 */     if (this.debugMode == null) {
/* 30 */       this.debugMode = defaultIndexCompactionParameters.debugMode;
/*    */     }
/* 32 */     if (this.scrollTimeout == null) {
/* 33 */       this.scrollTimeout = defaultIndexCompactionParameters.scrollTimeout;
/*    */     }
/* 35 */     if (this.scrollBatchSize == null) {
/* 36 */       this.scrollBatchSize = defaultIndexCompactionParameters.scrollBatchSize;
/*    */     }
/* 38 */     if (this.copyThreadPoolSize == null) {
/* 39 */       this.copyThreadPoolSize = defaultIndexCompactionParameters.copyThreadPoolSize;
/*    */     }
/* 41 */     if (this.numberOfParallelCopiers == null) {
/* 42 */       this.numberOfParallelCopiers = defaultIndexCompactionParameters.numberOfParallelCopiers;
/*    */     }
/*    */   }
/*    */   
/*    */   public Integer copyThreadPoolSize;
/*    */   public Integer numberOfParallelCopiers;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/compaction/IndexCompactionParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */