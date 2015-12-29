package com.appdynamics.common.util.lifecycle;

import io.dropwizard.lifecycle.Managed;
import javax.annotation.PostConstruct;

public abstract interface LifecycleAware
  extends Managed, Stoppable
{
  @PostConstruct
  public abstract void start();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/lifecycle/LifecycleAware.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */