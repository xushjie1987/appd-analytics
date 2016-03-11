package com.appdynamics.analytics.processor.event.metadata;

import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
import com.appdynamics.analytics.processor.account.configuration.AccountLicensingConfiguration;
import com.appdynamics.analytics.processor.event.EventTypeMetaData;
import com.google.inject.ImplementedBy;
import java.util.List;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;

@ImplementedBy(ElasticSearchEventTypeMetaDataService.class)
public abstract interface EventTypeMetaDataService
{
  public static final String META_DATA_IDX_NAME = "event_type_metadata";
  public static final String META_DATA_DOC_NAME = "event_type_metadata";
  public static final String EVENT_TYPE = "eventType";
  
  public abstract EventTypeMetaData getEventTypeMetaData(String paramString1, String paramString2);
  
  public abstract EventTypeMetaData getEventTypeMetaDataNoCache(String paramString1, String paramString2);
  
  public abstract List<EventTypeMetaData> updateEventTypeMetaData(List<AccountConfiguration> paramList);
  
  public abstract EventTypeMetaData storeEventTypeMetaData(EventTypeMetaData paramEventTypeMetaData, AccountLicensingConfiguration paramAccountLicensingConfiguration);
  
  public abstract EventTypeMetaData updateEventTypeMetaDataWithLicense(EventTypeMetaData paramEventTypeMetaData, AccountLicensingConfiguration paramAccountLicensingConfiguration);
  
  public abstract EventTypeMetaData deleteEventTypeMetaData(String paramString1, String paramString2);
  
  public abstract SearchResponse getRawEventTypeMetaDataForFilter(QueryBuilder paramQueryBuilder);
  
  public abstract void verifyEventType(String paramString1, String paramString2);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/metadata/EventTypeMetaDataService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */