/*    */ package com.appdynamics.common.util.datetime;
/*    */ 
/*    */ import org.joda.time.IllegalFieldValueException;
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ import org.joda.time.format.DateTimeParser;
/*    */ import org.joda.time.format.DateTimeParserBucket;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public final class FastDateTimeParser
/*    */ {
/* 22 */   private static final Logger log = LoggerFactory.getLogger(FastDateTimeParser.class);
/*    */   
/*    */   public static final int MATCH_FAILURE = -1;
/*    */   
/*    */   private final DateTimeParser parser;
/*    */   
/*    */   private final DateTimeParserBucket bucket;
/*    */   
/*    */   private final Object resetState;
/*    */   private String currentText;
/*    */   private int currentPosition;
/*    */   
/*    */   public FastDateTimeParser(DateTimeFormatter formatter)
/*    */   {
/* 36 */     this.parser = formatter.getParser();
/* 37 */     this.bucket = new DateTimeParserBucket(0L, formatter.getChronology(), formatter.getLocale(), formatter.getPivotYear(), formatter.getDefaultYear());
/*    */     
/*    */ 
/* 40 */     this.resetState = this.bucket.saveState();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void reset()
/*    */   {
/* 48 */     reset("");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void reset(String text)
/*    */   {
/* 56 */     this.currentText = text;
/* 57 */     this.currentPosition = 0;
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
/*    */   public long next()
/*    */   {
/* 70 */     long returnVal = -1L;
/*    */     
/* 72 */     boolean trace = log.isTraceEnabled();
/* 73 */     int length = this.currentText.length();
/*    */     
/* 75 */     while (this.currentPosition < length) {
/* 76 */       this.bucket.restoreState(this.resetState);
/*    */       
/* 78 */       int nextPosition = this.parser.parseInto(this.bucket, this.currentText, this.currentPosition);
/*    */       
/* 80 */       if (nextPosition >= this.currentPosition) {
/*    */         try {
/* 82 */           returnVal = this.bucket.computeMillis(true, this.currentText.substring(this.currentPosition, nextPosition));
/*    */           
/* 84 */           this.currentPosition = nextPosition;
/*    */         }
/*    */         catch (IllegalFieldValueException e) {
/* 87 */           if (trace) {
/* 88 */             log.error("Error occurred while parsing string [" + this.currentText + "] at position [" + this.currentPosition + "]", e);
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 93 */       this.currentPosition += 1;
/*    */     }
/*    */     
/* 96 */     return returnVal;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/datetime/FastDateTimeParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */