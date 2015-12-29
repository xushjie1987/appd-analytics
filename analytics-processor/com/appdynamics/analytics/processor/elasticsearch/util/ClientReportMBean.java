package com.appdynamics.analytics.processor.elasticsearch.util;

import java.util.List;

public abstract interface ClientReportMBean
{
  public abstract List<ClientReportClientDataMBean> getClientReportClientData();
  
  public static abstract interface ClientReportClientDataMBean
  {
    public abstract String getClusterName();
    
    public abstract long getClusterSizeBytes();
    
    public abstract long getClusterStateSizeEstimateBytes();
  }
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ClientReportMBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */