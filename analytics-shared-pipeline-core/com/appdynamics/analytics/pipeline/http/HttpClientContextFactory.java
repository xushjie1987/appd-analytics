package com.appdynamics.analytics.pipeline.http;

import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;

public abstract interface HttpClientContextFactory
{
  public abstract HttpClientContext makeNew(HttpHost paramHttpHost);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/http/HttpClientContextFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */