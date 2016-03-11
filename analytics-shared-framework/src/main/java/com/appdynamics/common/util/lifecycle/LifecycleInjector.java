package com.appdynamics.common.util.lifecycle;

public abstract interface LifecycleInjector
{
  public abstract <T> T inject(T paramT);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/lifecycle/LifecycleInjector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */