package com.appdynamics.analytics.processor.event;

import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
import com.google.inject.ImplementedBy;
import java.util.List;
import javax.annotation.concurrent.ThreadSafe;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.collect.UnmodifiableIterator;

@ImplementedBy(ElasticSearchEventService.class)
@ThreadSafe
public abstract interface ElasticSearchMetaService
{
  public abstract ClientProvider getClientProvider();
  
  public abstract IndexNameResolver getIndexNameResolver();
  
  public abstract List<String> indicesAsOrderedList(UnmodifiableIterator<String> paramUnmodifiableIterator);
  
  public abstract ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> getSearchAliasMetaData(String paramString1, String paramString2, String paramString3);
  
  public abstract void verifyEventTypeRepairingIndicesIfNecessary(int paramInt, String paramString1, String paramString2);
  
  public abstract void updateEventTypeMetaDataAndAliasFilter(List<AccountConfiguration> paramList);
  
  public abstract List<String> getSearchIndexNamesForUpsert(String paramString1, String paramString2);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ElasticSearchMetaService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */