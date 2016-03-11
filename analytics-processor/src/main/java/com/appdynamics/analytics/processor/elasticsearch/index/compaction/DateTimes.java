/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.compaction;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*    */ import org.joda.time.DateTime;
/*    */ import org.joda.time.format.DateTimeFormat;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateTimes
/*    */ {
/*    */   public static DateTime getNextDateTimeOccurrence(String time, String format)
/*    */   {
/* 29 */     DateTime startTimeParsed = DateTime.parse(time, DateTimeFormat.forPattern(format));
/* 30 */     DateTime dt = TimeKeeper.currentUtcTime().withHourOfDay(startTimeParsed.getHourOfDay()).withMinuteOfHour(startTimeParsed.getMinuteOfHour()).withSecondOfMinute(startTimeParsed.getSecondOfMinute()).withMillisOfSecond(startTimeParsed.getMillisOfSecond());
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 35 */     if (dt.isBeforeNow()) {
/* 36 */       dt = dt.plusDays(1);
/*    */     }
/* 38 */     return dt;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/compaction/DateTimes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */