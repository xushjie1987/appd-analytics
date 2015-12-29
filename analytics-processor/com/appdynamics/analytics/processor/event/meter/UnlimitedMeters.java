/*    */ package com.appdynamics.analytics.processor.event.meter;
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
/*    */ class UnlimitedMeters
/*    */   extends Meters
/*    */ {
/*    */   public long updateAndCheckBytes(String accountName, String eventType, long byteCount)
/*    */   {
/* 17 */     return Long.MAX_VALUE;
/*    */   }
/*    */   
/*    */ 
/*    */   public long updateAndCheckDocumentFragments(String accountName, String eventType, long numDocuments)
/*    */   {
/* 23 */     return Long.MAX_VALUE;
/*    */   }
/*    */   
/*    */ 
/*    */   public long updateAndCheckSearchThreshold(String accountName, String eventType)
/*    */   {
/* 29 */     return Long.MAX_VALUE;
/*    */   }
/*    */   
/*    */ 
/*    */   public long getDailyBytesLimit(String accountName, String eventType)
/*    */   {
/* 35 */     return Long.MAX_VALUE;
/*    */   }
/*    */   
/*    */ 
/*    */   public long getDailyDocumentsLimit(String accountName, String eventType)
/*    */   {
/* 41 */     return Long.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/UnlimitedMeters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */