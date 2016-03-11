package com.appdynamics.analytics.processor.elasticsearch.index.rolling;

import com.appdynamics.analytics.processor.admin.ForcedRolloverRequest;
import com.appdynamics.common.util.lifecycle.RunningState;

public abstract interface RollingIndexController
{
  public abstract void start();
  
  public abstract void close();
  
  public abstract RunningState getRunningState();
  
  public abstract void forceRollover(ForcedRolloverRequest paramForcedRolloverRequest);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/RollingIndexController.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */