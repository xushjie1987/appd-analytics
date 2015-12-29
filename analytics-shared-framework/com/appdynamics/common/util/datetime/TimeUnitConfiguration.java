/*    */ package com.appdynamics.common.util.datetime;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
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
/*    */ public class TimeUnitConfiguration
/*    */ {
/*    */   @Min(1L)
/*    */   long time;
/*    */   
/* 27 */   public long getTime() { return this.time; } public void setTime(long time) { this.time = time; } @NotNull
/*    */   @Valid
/* 29 */   TimeUnit timeUnit = TimeUnit.MILLISECONDS;
/*    */   
/* 31 */   public TimeUnit getTimeUnit() { return this.timeUnit; } public void setTimeUnit(TimeUnit timeUnit) { this.timeUnit = timeUnit; }
/*    */   
/*    */ 
/*    */ 
/*    */   @JsonCreator
/*    */   public TimeUnitConfiguration(@JsonProperty("time") long time, @JsonProperty("timeUnit") TimeUnit timeUnit)
/*    */   {
/* 38 */     this.time = time;
/* 39 */     this.timeUnit = timeUnit;
/*    */   }
/*    */   
/*    */   public long toMilliseconds() {
/* 43 */     return TimeUnit.MILLISECONDS.convert(this.time, this.timeUnit);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 48 */     return getTime() + " " + this.timeUnit.name().toLowerCase();
/*    */   }
/*    */   
/*    */   public TimeUnitConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/datetime/TimeUnitConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */