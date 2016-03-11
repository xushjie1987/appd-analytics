package com.appdynamics.analytics.processor.event.metadata;

import com.appdynamics.analytics.processor.event.EventTypeMetaData;
import com.google.inject.ImplementedBy;
import java.util.Collection;

@ImplementedBy(EventTypeCacheImpl.class)
public abstract interface EventTypeCache
{
  public abstract void storeAccountNameEventType(String paramString1, String paramString2);
  
  public abstract boolean accountNameEventTypeExists(String paramString1, String paramString2);
  
  public abstract boolean accountNameEventTypeExistsWithCurrentInsertClient(String paramString1, String paramString2);
  
  public abstract void removeAccountNameEventType(String paramString1, String paramString2);
  
  public abstract void storeEventTypeMetadata(String paramString1, String paramString2, EventTypeMetaData paramEventTypeMetaData);
  
  public abstract EventTypeMetaData getEventTypeMetadata(String paramString1, String paramString2);
  
  public abstract void invalidateEventTypeMetaData(String paramString1, String paramString2);
  
  public abstract void invalidateAllEntries(Collection<String> paramCollection);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/metadata/EventTypeCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */