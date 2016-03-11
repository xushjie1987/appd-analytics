package com.appdynamics.analytics.processor.elasticsearch.index.creation;

import com.appdynamics.analytics.processor.elasticsearch.index.configuration.MetaDataIndexConfiguration;
import java.util.List;
import java.util.Map;
import org.elasticsearch.client.Client;

public abstract interface IndexCreationManager
{
  public abstract boolean createIndexLocking(Client paramClient, String paramString, Map<String, Object> paramMap);
  
  public abstract boolean createIndexLocking(Client paramClient, String paramString, MetaDataIndexConfiguration paramMetaDataIndexConfiguration);
  
  public abstract boolean createIndexLocking(Client paramClient, String paramString, Map<String, Object> paramMap, List<String> paramList, Map<String, Map<String, Object>> paramMap1);
  
  public abstract boolean createIndexNotLocking(Client paramClient, String paramString, Map<String, Object> paramMap, List<String> paramList, Map<String, Map<String, Object>> paramMap1);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/creation/IndexCreationManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */