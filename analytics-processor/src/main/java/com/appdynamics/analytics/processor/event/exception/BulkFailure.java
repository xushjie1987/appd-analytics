/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BulkFailure
/*    */ {
/*    */   protected final String status;
/*    */   
/*    */   protected final boolean isTransient;
/*    */   
/*    */   protected final int bulkRequestIndex;
/*    */   
/*    */   protected final String correlationId;
/*    */   
/*    */   protected final String message;
/*    */   
/*    */ 
/*    */   public String getStatus()
/*    */   {
/* 20 */     return this.status;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isTransient()
/*    */   {
/* 26 */     return this.isTransient;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getBulkRequestIndex()
/*    */   {
/* 33 */     return this.bulkRequestIndex;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCorrelationId()
/*    */   {
/* 44 */     return this.correlationId;
/*    */   }
/*    */   
/*    */   public String getMessage()
/*    */   {
/* 49 */     return this.message;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BulkFailure(String status, boolean isTransient, int bulkRequestIndex, String correlationId, String message)
/*    */   {
/* 60 */     this.status = status;
/* 61 */     this.isTransient = isTransient;
/* 62 */     this.bulkRequestIndex = bulkRequestIndex;
/* 63 */     this.correlationId = correlationId;
/* 64 */     this.message = message;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 72 */     return "[status: [" + this.status + "], isTransient: [" + this.isTransient + "], bulkRequestIndex: [" + this.bulkRequestIndex + "], correlationId: [" + this.correlationId + "], message: [" + this.message + "]]";
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/BulkFailure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */