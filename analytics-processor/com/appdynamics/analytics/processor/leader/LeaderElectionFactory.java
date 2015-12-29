package com.appdynamics.analytics.processor.leader;

public abstract interface LeaderElectionFactory
{
  public abstract LeaderElection makeLeader(String paramString);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/leader/LeaderElectionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */