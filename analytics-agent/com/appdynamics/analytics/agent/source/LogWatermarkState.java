package com.appdynamics.analytics.agent.source;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({@com.fasterxml.jackson.annotation.JsonSubTypes.Type(value=com.appdynamics.analytics.agent.source.tail.TailLogWatermarkState.class, name="tailLogWatermarkState")})
public class LogWatermarkState {}


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/LogWatermarkState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */