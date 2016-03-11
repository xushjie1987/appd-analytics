package com.appdynamics.analytics.shared.rest.dto;

public abstract interface Identifiable<ID>
{
  public abstract ID getId();
  
  public abstract void setId(ID paramID);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/dto/Identifiable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */