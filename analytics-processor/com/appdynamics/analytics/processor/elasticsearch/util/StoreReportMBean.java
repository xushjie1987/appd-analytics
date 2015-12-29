package com.appdynamics.analytics.processor.elasticsearch.util;

public abstract interface StoreReportMBean
{
  public abstract long getClusterSizeBytes();
  
  public abstract int getNumberOfIndices();
  
  public abstract int getNumberOfShards();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/StoreReportMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */