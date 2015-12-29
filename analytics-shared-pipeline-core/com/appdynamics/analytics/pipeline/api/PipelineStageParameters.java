package com.appdynamics.analytics.pipeline.api;

public abstract interface PipelineStageParameters<OUT>
{
  public abstract Object getPipelineId();
  
  public abstract PipelineStage<? super OUT, ?> getOptionalNextStage();
  
  public abstract Object getRawStageConfiguration();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/api/PipelineStageParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */