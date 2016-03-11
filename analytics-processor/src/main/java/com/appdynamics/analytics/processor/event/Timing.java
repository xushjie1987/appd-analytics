package com.appdynamics.analytics.processor.event;

import com.google.inject.ImplementedBy;
import org.joda.time.DateTime;

@ImplementedBy(DefaultTiming.class)
public abstract interface Timing
{
  public abstract long currentTimeMillis();
  
  public abstract DateTime currentDateTime();
  
  public abstract void sleep(long paramLong)
    throws InterruptedException;
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/Timing.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */