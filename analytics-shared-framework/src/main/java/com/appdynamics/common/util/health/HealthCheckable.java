package com.appdynamics.common.util.health;

import com.codahale.metrics.health.HealthCheck.Result;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public abstract interface HealthCheckable
{
  public abstract String getName();
  
  public abstract HealthCheck.Result check();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/HealthCheckable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */