/*    */ package com.appdynamics.analytics.processor.query;
/*    */ 
/*    */ import org.antlr.v4.runtime.CharStream;
/*    */ import org.antlr.v4.runtime.misc.Interval;
/*    */ import org.antlr.v4.runtime.misc.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CaseInsensitiveStream
/*    */   implements CharStream
/*    */ {
/*    */   private CharStream stream;
/*    */   
/*    */   public CaseInsensitiveStream(CharStream stream)
/*    */   {
/* 19 */     this.stream = stream;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public String getText(@NotNull Interval interval)
/*    */   {
/* 25 */     return this.stream.getText(interval);
/*    */   }
/*    */   
/*    */   public void consume()
/*    */   {
/* 30 */     this.stream.consume();
/*    */   }
/*    */   
/*    */   public int LA(int i)
/*    */   {
/* 35 */     int result = this.stream.LA(i);
/*    */     
/* 37 */     switch (result) {
/*    */     case -1: 
/*    */     case 0: 
/* 40 */       return result;
/*    */     }
/* 42 */     return Character.toUpperCase(result);
/*    */   }
/*    */   
/*    */ 
/*    */   public int mark()
/*    */   {
/* 48 */     return this.stream.mark();
/*    */   }
/*    */   
/*    */   public void release(int marker)
/*    */   {
/* 53 */     this.stream.release(marker);
/*    */   }
/*    */   
/*    */   public int index()
/*    */   {
/* 58 */     return this.stream.index();
/*    */   }
/*    */   
/*    */   public void seek(int index)
/*    */   {
/* 63 */     this.stream.seek(index);
/*    */   }
/*    */   
/*    */   public int size()
/*    */   {
/* 68 */     return this.stream.size();
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public String getSourceName()
/*    */   {
/* 74 */     return this.stream.getSourceName();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/CaseInsensitiveStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */