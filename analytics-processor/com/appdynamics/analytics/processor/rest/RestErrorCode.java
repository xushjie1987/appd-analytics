package com.appdynamics.analytics.processor.rest;

import javax.ws.rs.core.Response.Status;

public abstract interface RestErrorCode
{
  public abstract Response.Status getStatus();
  
  public abstract String getSubStatus();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/RestErrorCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */