/*    */ package com.appdynamics.analytics.processor.route;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JsonIgnoreProperties(ignoreUnknown=true)
/*    */ public class RouteCleanupParameters
/*    */ {
/*    */   Boolean debugMode;
/*    */   Integer batchDeleteSize;
/*    */   Integer numBatchesToProcess;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 17 */     if (o == this) return true; if (!(o instanceof RouteCleanupParameters)) return false; RouteCleanupParameters other = (RouteCleanupParameters)o; if (!other.canEqual(this)) return false; Object this$debugMode = getDebugMode();Object other$debugMode = other.getDebugMode(); if (this$debugMode == null ? other$debugMode != null : !this$debugMode.equals(other$debugMode)) return false; Object this$batchDeleteSize = getBatchDeleteSize();Object other$batchDeleteSize = other.getBatchDeleteSize(); if (this$batchDeleteSize == null ? other$batchDeleteSize != null : !this$batchDeleteSize.equals(other$batchDeleteSize)) return false; Object this$numBatchesToProcess = getNumBatchesToProcess();Object other$numBatchesToProcess = other.getNumBatchesToProcess();return this$numBatchesToProcess == null ? other$numBatchesToProcess == null : this$numBatchesToProcess.equals(other$numBatchesToProcess); } public boolean canEqual(Object other) { return other instanceof RouteCleanupParameters; } public int hashCode() { int PRIME = 31;int result = 1;Object $debugMode = getDebugMode();result = result * 31 + ($debugMode == null ? 0 : $debugMode.hashCode());Object $batchDeleteSize = getBatchDeleteSize();result = result * 31 + ($batchDeleteSize == null ? 0 : $batchDeleteSize.hashCode());Object $numBatchesToProcess = getNumBatchesToProcess();result = result * 31 + ($numBatchesToProcess == null ? 0 : $numBatchesToProcess.hashCode());return result; } public String toString() { return "RouteCleanupParameters(debugMode=" + getDebugMode() + ", batchDeleteSize=" + getBatchDeleteSize() + ", numBatchesToProcess=" + getNumBatchesToProcess() + ")"; }
/*    */   
/*    */ 
/*    */ 
/* 21 */   public Boolean getDebugMode() { return this.debugMode; } public void setDebugMode(Boolean debugMode) { this.debugMode = debugMode; }
/* 22 */   public Integer getBatchDeleteSize() { return this.batchDeleteSize; } public void setBatchDeleteSize(Integer batchDeleteSize) { this.batchDeleteSize = batchDeleteSize; }
/* 23 */   public Integer getNumBatchesToProcess() { return this.numBatchesToProcess; } public void setNumBatchesToProcess(Integer numBatchesToProcess) { this.numBatchesToProcess = numBatchesToProcess; }
/*    */   
/*    */   public void mergeRouteCleanupParametersWithDefaults(RouteCleanupParameters defaultParameters) {
/* 26 */     if (this.debugMode == null) {
/* 27 */       this.debugMode = defaultParameters.debugMode;
/*    */     }
/* 29 */     if (this.batchDeleteSize == null) {
/* 30 */       this.batchDeleteSize = defaultParameters.batchDeleteSize;
/*    */     }
/* 32 */     if (this.numBatchesToProcess == null) {
/* 33 */       this.numBatchesToProcess = defaultParameters.numBatchesToProcess;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/route/RouteCleanupParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */