package com.appdynamics.common.util.health;

public abstract interface HealthReportMBean
{
  public abstract String getName();
  
  public abstract String getBuildInfo();
  
  public abstract boolean isHealthy();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/HealthReportMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */