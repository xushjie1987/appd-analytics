package com.appdynamics.analytics.processor.event.dto;

public abstract interface GenericMessage
{
  public abstract String getSource();
  
  public abstract String getId();
  
  public abstract byte[] getBody();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/dto/GenericMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */