package com.appdynamics.common.util.execution;

public abstract interface Retriable
{
  public abstract int getAttempt();
  
  public abstract int incAttempt();
  
  public abstract boolean done();
  
  public abstract void done(Throwable paramThrowable);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/execution/Retriable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */