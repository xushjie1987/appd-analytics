package com.appdynamics.analytics.processor.tool.executor;

import com.appdynamics.analytics.processor.tool.executor.response.ExecutionResponse;

public abstract interface ExecutionStep
{
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract ExecutionResponse executeStep(ExecutionContext paramExecutionContext);
  
  public abstract ExecutionStep copy();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/ExecutionStep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */