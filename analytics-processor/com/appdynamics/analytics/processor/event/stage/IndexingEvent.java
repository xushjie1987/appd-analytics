/*    */ package com.appdynamics.analytics.processor.event.stage;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.admin.ActionType;
/*    */ import com.appdynamics.analytics.processor.event.dto.GenericMessage;
/*    */ import com.appdynamics.common.io.payload.Bytes;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IndexingEvent
/*    */   implements GenericMessage
/*    */ {
/*    */   final String source;
/*    */   final String id;
/*    */   final ActionType type;
/*    */   @Nullable
/*    */   final Bytes key;
/*    */   final Bytes body;
/*    */   
/*    */   public IndexingEvent(String source, String id, byte[] body, ActionType type)
/*    */   {
/* 28 */     this(source, id, type, null, new Bytes(body));
/*    */   }
/*    */   
/*    */   public IndexingEvent(String source, String id, ActionType type, Bytes body) {
/* 32 */     this(source, id, type, null, body);
/*    */   }
/*    */   
/*    */   public IndexingEvent(String source, String id, ActionType type, @Nullable Bytes key, Bytes body) {
/* 36 */     this.source = source;
/* 37 */     this.id = id;
/* 38 */     this.type = type;
/* 39 */     this.key = key;
/* 40 */     this.body = body;
/*    */   }
/*    */   
/*    */   public String getSource()
/*    */   {
/* 45 */     return this.source;
/*    */   }
/*    */   
/*    */   public String getId()
/*    */   {
/* 50 */     return this.id;
/*    */   }
/*    */   
/*    */ 
/*    */   public byte[] getBody()
/*    */   {
/* 56 */     return this.body.getOrCopyArray();
/*    */   }
/*    */   
/*    */   public ActionType getType() {
/* 60 */     return this.type;
/*    */   }
/*    */   
/*    */   public Bytes getKey()
/*    */   {
/* 65 */     return this.key;
/*    */   }
/*    */   
/*    */   public Bytes getBodyBytes() {
/* 69 */     return this.body;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/stage/IndexingEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */