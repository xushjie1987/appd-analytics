/*    */ package com.appdynamics.common.util.health;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.appdynamics.common.util.log.LogLevel;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HealthReporterModuleConfiguration
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 19 */     if (o == this) return true; if (!(o instanceof HealthReporterModuleConfiguration)) return false; HealthReporterModuleConfiguration other = (HealthReporterModuleConfiguration)o; if (!other.canEqual(this)) return false; Object this$schedule = getSchedule();Object other$schedule = other.getSchedule(); if (this$schedule == null ? other$schedule != null : !this$schedule.equals(other$schedule)) return false; Object this$healthyLogLevel = getHealthyLogLevel();Object other$healthyLogLevel = other.getHealthyLogLevel();return this$healthyLogLevel == null ? other$healthyLogLevel == null : this$healthyLogLevel.equals(other$healthyLogLevel); } public boolean canEqual(Object other) { return other instanceof HealthReporterModuleConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $schedule = getSchedule();result = result * 31 + ($schedule == null ? 0 : $schedule.hashCode());Object $healthyLogLevel = getHealthyLogLevel();result = result * 31 + ($healthyLogLevel == null ? 0 : $healthyLogLevel.hashCode());return result; } public String toString() { return "HealthReporterModuleConfiguration(schedule=" + getSchedule() + ", healthyLogLevel=" + getHealthyLogLevel() + ")"; } @NotNull
/*    */   @Valid
/* 21 */   TimeUnitConfiguration schedule = new TimeUnitConfiguration(30L, TimeUnit.SECONDS);
/*    */   
/* 23 */   public TimeUnitConfiguration getSchedule() { return this.schedule; } public void setSchedule(TimeUnitConfiguration schedule) { this.schedule = schedule; } @NotNull
/*    */   @Valid
/* 25 */   LogLevel healthyLogLevel = LogLevel.DEBUG;
/*    */   
/* 27 */   public LogLevel getHealthyLogLevel() { return this.healthyLogLevel; } public void setHealthyLogLevel(LogLevel healthyLogLevel) { this.healthyLogLevel = healthyLogLevel; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/HealthReporterModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */