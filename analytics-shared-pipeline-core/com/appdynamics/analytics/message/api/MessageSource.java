package com.appdynamics.analytics.message.api;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;

public abstract interface MessageSource<SRC_ID, MSG>
  extends Closeable
{
  public abstract SRC_ID getId();
  
  public abstract boolean supportsRetriable();
  
  public abstract MessagePack<SRC_ID, MSG> poll(int paramInt, long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException;
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/message/api/MessageSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */