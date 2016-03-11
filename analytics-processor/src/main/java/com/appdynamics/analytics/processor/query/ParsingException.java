/*    */ package com.appdynamics.analytics.processor.query;
/*    */ 
/*    */ import org.antlr.v4.runtime.RecognitionException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParsingException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final int line;
/*    */   private final int charPositionInLine;
/*    */   
/*    */   public ParsingException(String message, RecognitionException cause, int line, int charPositionInLine)
/*    */   {
/* 20 */     super(message, cause);
/*    */     
/* 22 */     this.line = line;
/* 23 */     this.charPositionInLine = charPositionInLine;
/*    */   }
/*    */   
/*    */   public ParsingException(String message) {
/* 27 */     this(message, null, 1, 0);
/*    */   }
/*    */   
/*    */   public int getLineNumber() {
/* 31 */     return this.line;
/*    */   }
/*    */   
/*    */   public int getColumnNumber() {
/* 35 */     return this.charPositionInLine + 1;
/*    */   }
/*    */   
/*    */   public String getErrorMessage() {
/* 39 */     return super.getMessage();
/*    */   }
/*    */   
/*    */   public String getMessage()
/*    */   {
/* 44 */     return String.format("line %s:%s: %s", new Object[] { Integer.valueOf(getLineNumber()), Integer.valueOf(getColumnNumber()), getErrorMessage() });
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/query/ParsingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */