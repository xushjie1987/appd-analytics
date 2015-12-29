package com.appdynamics.common.util.lifecycle;

import javax.annotation.PreDestroy;

public abstract interface Stoppable
{
  @PreDestroy
  public abstract void stop();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/lifecycle/Stoppable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */