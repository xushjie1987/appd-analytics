/*    */ package com.appdynamics.common.util.health;
/*    */ 
/*    */ import javax.annotation.concurrent.ThreadSafe;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ThreadSafe
/*    */ public class HealthReport
/*    */   implements HealthReportMBean
/*    */ {
/*    */   private final String name;
/*    */   private final String buildInfo;
/*    */   private volatile boolean healthy;
/*    */   
/*    */   public HealthReport(String name, String buildInfo, boolean healthy)
/*    */   {
/* 23 */     this.name = name;
/* 24 */     this.buildInfo = buildInfo;
/* 25 */     this.healthy = healthy;
/*    */   }
/*    */   
/*    */   public void setHealthy(boolean healthy) {
/* 29 */     this.healthy = healthy;
/*    */   }
/*    */   
/*    */   public boolean isHealthy()
/*    */   {
/* 34 */     return this.healthy;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 39 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getBuildInfo()
/*    */   {
/* 44 */     return this.buildInfo;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/HealthReport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */