package com.appdynamics.analytics.pipeline.api;

public abstract interface PipelineStageFactory<IN, OUT>
{
  public abstract PipelineStage<IN, OUT> create(PipelineStageParameters<OUT> paramPipelineStageParameters);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/api/PipelineStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */