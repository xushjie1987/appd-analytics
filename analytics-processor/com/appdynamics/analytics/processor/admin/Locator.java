package com.appdynamics.analytics.processor.admin;

public abstract interface Locator
{
  public abstract String findActiveClusterName(String paramString);
  
  public abstract String findTopicName(String paramString, ActionType paramActionType);
  
  public abstract String findTopicName(String paramString1, String paramString2, ActionType paramActionType);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/Locator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */