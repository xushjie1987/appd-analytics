/*    */ package com.appdynamics.analytics.message.util;
/*    */ 
/*    */ import com.appdynamics.analytics.message.api.MessagePack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SynchronizedMessagePack<SRC_ID, MSG>
/*    */   implements MessagePack<SRC_ID, MSG>
/*    */ {
/*    */   final MessagePack<SRC_ID, MSG> actual;
/*    */   
/*    */   public MessagePack<SRC_ID, MSG> getActual()
/*    */   {
/* 18 */     return this.actual;
/*    */   }
/*    */   
/*    */   public SynchronizedMessagePack(MessagePack<SRC_ID, MSG> actual) {
/* 22 */     this.actual = actual;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object getLock()
/*    */   {
/* 35 */     return this;
/*    */   }
/*    */   
/*    */   public SRC_ID getSourceId()
/*    */   {
/* 40 */     return (SRC_ID)this.actual.getSourceId();
/*    */   }
/*    */   
/*    */   public synchronized int size()
/*    */   {
/* 45 */     return this.actual.size();
/*    */   }
/*    */   
/*    */   public synchronized MSG peek()
/*    */   {
/* 50 */     return (MSG)this.actual.peek();
/*    */   }
/*    */   
/*    */   public synchronized MSG poll()
/*    */   {
/* 55 */     return (MSG)this.actual.poll();
/*    */   }
/*    */   
/*    */   public synchronized void returnUndelivered(MSG undeliverableMsg)
/*    */   {
/* 60 */     this.actual.returnUndelivered(undeliverableMsg);
/*    */   }
/*    */   
/*    */   public synchronized void returnUndelivered(MSG undeliverableMsg, Throwable cause)
/*    */   {
/* 65 */     this.actual.returnUndelivered(undeliverableMsg, cause);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/message/util/SynchronizedMessagePack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */