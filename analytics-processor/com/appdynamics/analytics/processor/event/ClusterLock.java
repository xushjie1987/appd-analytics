package com.appdynamics.analytics.processor.event;

import com.google.inject.ImplementedBy;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@ImplementedBy(SingleNodeClusterLock.class)
public abstract interface ClusterLock
{
  public abstract <T> T acquireAndExecute(String paramString, long paramLong, TimeUnit paramTimeUnit, Callable<? extends T> paramCallable);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ClusterLock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */