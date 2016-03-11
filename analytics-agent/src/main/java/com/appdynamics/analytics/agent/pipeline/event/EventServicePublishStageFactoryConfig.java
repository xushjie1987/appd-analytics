/*    */ package com.appdynamics.analytics.agent.pipeline.event;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lombok.NonNull;
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
/*    */ public class EventServicePublishStageFactoryConfig
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 21 */     if (o == this) return true; if (!(o instanceof EventServicePublishStageFactoryConfig)) return false; EventServicePublishStageFactoryConfig other = (EventServicePublishStageFactoryConfig)o; if (!other.canEqual(this)) return false; if (isUpsert() != other.isUpsert()) return false; Object this$idPath = getIdPath();Object other$idPath = other.getIdPath(); if (this$idPath == null ? other$idPath != null : !this$idPath.equals(other$idPath)) return false; Object this$mergeFields = getMergeFields();Object other$mergeFields = other.getMergeFields(); if (this$mergeFields == null ? other$mergeFields != null : !this$mergeFields.equals(other$mergeFields)) return false; return isEventBatchingEnabled() == other.isEventBatchingEnabled(); } public boolean canEqual(Object other) { return other instanceof EventServicePublishStageFactoryConfig; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + (isUpsert() ? 1231 : 1237);Object $idPath = getIdPath();result = result * 31 + ($idPath == null ? 0 : $idPath.hashCode());Object $mergeFields = getMergeFields();result = result * 31 + ($mergeFields == null ? 0 : $mergeFields.hashCode());result = result * 31 + (isEventBatchingEnabled() ? 1231 : 1237);return result; } public String toString() { return "EventServicePublishStageFactoryConfig(upsert=" + isUpsert() + ", idPath=" + getIdPath() + ", mergeFields=" + getMergeFields() + ", eventBatchingEnabled=" + isEventBatchingEnabled() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 28 */   public boolean isUpsert() { return this.upsert; } public void setUpsert(boolean upsert) { this.upsert = upsert; } boolean upsert = false;
/*    */   @NonNull
/* 30 */   String idPath = ""; @NonNull
/* 31 */   public String getIdPath() { return this.idPath; } public void setIdPath(@NonNull String idPath) { if (idPath == null) throw new NullPointerException("idPath"); this.idPath = idPath; }
/*    */   @NonNull
/* 33 */   List<String> mergeFields = new ArrayList(); @NonNull
/* 34 */   public List<String> getMergeFields() { return this.mergeFields; } public void setMergeFields(@NonNull List<String> mergeFields) { if (mergeFields == null) throw new NullPointerException("mergeFields"); this.mergeFields = mergeFields;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 41 */   public boolean isEventBatchingEnabled() { return this.eventBatchingEnabled; } public void setEventBatchingEnabled(boolean eventBatchingEnabled) { this.eventBatchingEnabled = eventBatchingEnabled; } boolean eventBatchingEnabled = false;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/event/EventServicePublishStageFactoryConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */