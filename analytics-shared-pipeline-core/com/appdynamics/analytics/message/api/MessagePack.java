package com.appdynamics.analytics.message.api;

public abstract interface MessagePack<SRC_ID, MSG>
{
  public abstract SRC_ID getSourceId();
  
  public abstract int size();
  
  public abstract MSG peek();
  
  public abstract MSG poll();
  
  public abstract void returnUndelivered(MSG paramMSG);
  
  public abstract void returnUndelivered(MSG paramMSG, Throwable paramThrowable);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/message/api/MessagePack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */