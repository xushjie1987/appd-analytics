package com.appdynamics.analytics.processor.elasticsearch.node.provider;

import com.google.inject.ImplementedBy;
import java.util.List;
import org.elasticsearch.client.Client;

@ImplementedBy(SingleClientProvider.class)
public abstract interface ClientProvider
{
  public abstract Client getAdminClient();
  
  public abstract Client getClusterClient(String paramString);
  
  public abstract Client getInsertClient(String paramString);
  
  public abstract List<Client> getAllInsertClients(String paramString);
  
  public abstract Client getSearchClient(String paramString);
  
  public abstract List<Client> getAllInsertClients();
  
  public abstract List<Client> getAllClients();
  
  public abstract List<String> getAllClusterNames();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/provider/ClientProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */