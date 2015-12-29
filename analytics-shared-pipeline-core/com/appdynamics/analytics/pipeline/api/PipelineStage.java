package com.appdynamics.analytics.pipeline.api;

import com.appdynamics.common.util.execution.Operation;
import com.appdynamics.common.util.lifecycle.LifecycleAware;

public abstract interface PipelineStage<IN, OUT>
  extends LifecycleAware, Operation<IN>
{
  public abstract void process(IN paramIN);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/api/PipelineStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */