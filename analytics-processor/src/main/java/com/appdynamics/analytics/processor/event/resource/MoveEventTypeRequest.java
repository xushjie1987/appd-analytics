/*    */ package com.appdynamics.analytics.processor.event.resource;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
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
/*    */ public class MoveEventTypeRequest
/*    */ {
/*    */   String targetEventType;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 21 */     if (o == this) return true; if (!(o instanceof MoveEventTypeRequest)) return false; MoveEventTypeRequest other = (MoveEventTypeRequest)o; if (!other.canEqual(this)) return false; Object this$targetEventType = getTargetEventType();Object other$targetEventType = other.getTargetEventType(); if (this$targetEventType == null ? other$targetEventType != null : !this$targetEventType.equals(other$targetEventType)) return false; Object this$docIds = getDocIds();Object other$docIds = other.getDocIds();return this$docIds == null ? other$docIds == null : this$docIds.equals(other$docIds); } public boolean canEqual(Object other) { return other instanceof MoveEventTypeRequest; } public int hashCode() { int PRIME = 31;int result = 1;Object $targetEventType = getTargetEventType();result = result * 31 + ($targetEventType == null ? 0 : $targetEventType.hashCode());Object $docIds = getDocIds();result = result * 31 + ($docIds == null ? 0 : $docIds.hashCode());return result; }
/* 22 */   public String toString() { return "MoveEventTypeRequest(targetEventType=" + getTargetEventType() + ")"; }
/*    */   
/* 24 */   public String getTargetEventType() { return this.targetEventType; } public void setTargetEventType(String targetEventType) { this.targetEventType = targetEventType; }
/* 25 */   public List<String> getDocIds() { return this.docIds; } public void setDocIds(List<String> docIds) { this.docIds = docIds; } List<String> docIds = Lists.newArrayList();
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/resource/MoveEventTypeRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */