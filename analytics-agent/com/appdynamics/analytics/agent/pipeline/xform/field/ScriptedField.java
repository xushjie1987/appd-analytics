package com.appdynamics.analytics.agent.pipeline.xform.field;

import java.util.Map;

abstract interface ScriptedField
{
  public abstract Object value(Object paramObject, Map<String, Object> paramMap);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/field/ScriptedField.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */