/*    */ package com.appdynamics.analytics.processor.event.upsert;
/*    */ 
/*    */ import com.appdynamics.common.io.payload.Bytes;
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ 
/*    */ 
/*    */ 
/*    */ @VisibleForTesting
/*    */ public class MutableUpsert
/*    */   implements Upsert
/*    */ {
/*    */   String accountName;
/*    */   String eventType;
/*    */   String batchId;
/*    */   int batchPosition;
/*    */   String csvMergeFields;
/*    */   String correlationIdField;
/*    */   String correlationId;
/*    */   boolean accountNameInBytes;
/*    */   boolean pickupTimestampInBytes;
/*    */   boolean eventTimestampInBytes;
/*    */   Bytes bytes;
/*    */   
/* 24 */   public String toString() { return "MutableUpsert(accountName=" + getAccountName() + ", eventType=" + getEventType() + ", batchId=" + getBatchId() + ", batchPosition=" + getBatchPosition() + ", csvMergeFields=" + getCsvMergeFields() + ", correlationIdField=" + getCorrelationIdField() + ", correlationId=" + getCorrelationId() + ", accountNameInBytes=" + isAccountNameInBytes() + ", pickupTimestampInBytes=" + isPickupTimestampInBytes() + ", eventTimestampInBytes=" + isEventTimestampInBytes() + ")"; }
/*    */   
/*    */   public boolean equals(Object o) {
/* 27 */     if (o == this) return true; if (!(o instanceof MutableUpsert)) return false; MutableUpsert other = (MutableUpsert)o; if (!other.canEqual(this)) return false; Object this$accountName = getAccountName();Object other$accountName = other.getAccountName(); if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$eventType = getEventType();Object other$eventType = other.getEventType(); if (this$eventType == null ? other$eventType != null : !this$eventType.equals(other$eventType)) return false; Object this$batchId = getBatchId();Object other$batchId = other.getBatchId(); if (this$batchId == null ? other$batchId != null : !this$batchId.equals(other$batchId)) return false; if (getBatchPosition() != other.getBatchPosition()) return false; Object this$csvMergeFields = getCsvMergeFields();Object other$csvMergeFields = other.getCsvMergeFields(); if (this$csvMergeFields == null ? other$csvMergeFields != null : !this$csvMergeFields.equals(other$csvMergeFields)) return false; Object this$correlationIdField = getCorrelationIdField();Object other$correlationIdField = other.getCorrelationIdField(); if (this$correlationIdField == null ? other$correlationIdField != null : !this$correlationIdField.equals(other$correlationIdField)) return false; Object this$correlationId = getCorrelationId();Object other$correlationId = other.getCorrelationId(); if (this$correlationId == null ? other$correlationId != null : !this$correlationId.equals(other$correlationId)) return false; if (isAccountNameInBytes() != other.isAccountNameInBytes()) return false; if (isPickupTimestampInBytes() != other.isPickupTimestampInBytes()) return false; if (isEventTimestampInBytes() != other.isEventTimestampInBytes()) return false; Object this$bytes = getBytes();Object other$bytes = other.getBytes();return this$bytes == null ? other$bytes == null : this$bytes.equals(other$bytes); } public boolean canEqual(Object other) { return other instanceof MutableUpsert; } public int hashCode() { int PRIME = 31;int result = 1;Object $accountName = getAccountName();result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $eventType = getEventType();result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());Object $batchId = getBatchId();result = result * 31 + ($batchId == null ? 0 : $batchId.hashCode());result = result * 31 + getBatchPosition();Object $csvMergeFields = getCsvMergeFields();result = result * 31 + ($csvMergeFields == null ? 0 : $csvMergeFields.hashCode());Object $correlationIdField = getCorrelationIdField();result = result * 31 + ($correlationIdField == null ? 0 : $correlationIdField.hashCode());Object $correlationId = getCorrelationId();result = result * 31 + ($correlationId == null ? 0 : $correlationId.hashCode());result = result * 31 + (isAccountNameInBytes() ? 1231 : 1237);result = result * 31 + (isPickupTimestampInBytes() ? 1231 : 1237);result = result * 31 + (isEventTimestampInBytes() ? 1231 : 1237);Object $bytes = getBytes();result = result * 31 + ($bytes == null ? 0 : $bytes.hashCode());return result;
/*    */   }
/*    */   
/*    */ 
/* 31 */   public String getAccountName() { return this.accountName; } public void setAccountName(String accountName) { this.accountName = accountName; }
/* 32 */   public String getEventType() { return this.eventType; } public void setEventType(String eventType) { this.eventType = eventType; }
/*    */   
/*    */ 
/* 35 */   public String getBatchId() { return this.batchId; } public void setBatchId(String batchId) { this.batchId = batchId; }
/* 36 */   public int getBatchPosition() { return this.batchPosition; } public void setBatchPosition(int batchPosition) { this.batchPosition = batchPosition; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */   public String getCsvMergeFields() { return this.csvMergeFields; } public void setCsvMergeFields(String csvMergeFields) { this.csvMergeFields = csvMergeFields; }
/*    */   
/*    */ 
/*    */ 
/* 46 */   public String getCorrelationIdField() { return this.correlationIdField; } public void setCorrelationIdField(String correlationIdField) { this.correlationIdField = correlationIdField; }
/* 47 */   public String getCorrelationId() { return this.correlationId; } public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
/*    */   
/*    */ 
/* 50 */   public boolean isAccountNameInBytes() { return this.accountNameInBytes; } public void setAccountNameInBytes(boolean accountNameInBytes) { this.accountNameInBytes = accountNameInBytes; }
/* 51 */   public boolean isPickupTimestampInBytes() { return this.pickupTimestampInBytes; } public void setPickupTimestampInBytes(boolean pickupTimestampInBytes) { this.pickupTimestampInBytes = pickupTimestampInBytes; }
/* 52 */   public boolean isEventTimestampInBytes() { return this.eventTimestampInBytes; } public void setEventTimestampInBytes(boolean eventTimestampInBytes) { this.eventTimestampInBytes = eventTimestampInBytes; }
/*    */   
/*    */ 
/* 55 */   public Bytes getBytes() { return this.bytes; } public void setBytes(Bytes bytes) { this.bytes = bytes; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/upsert/MutableUpsert.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */