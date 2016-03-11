/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.TransientException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IndexCreationFailure
/*    */   extends TransientException
/*    */ {
/*    */   public IndexCreationFailure(String indexName)
/*    */   {
/* 17 */     super("Index creation failed for index [" + indexName + "] - acknowledgement failed");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/IndexCreationFailure.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */