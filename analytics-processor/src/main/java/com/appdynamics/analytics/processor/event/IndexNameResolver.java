package com.appdynamics.analytics.processor.event;

import com.google.inject.ImplementedBy;
import org.joda.time.DateTime;

@ImplementedBy(DefaultIndexNameResolver.class)
public abstract interface IndexNameResolver
{
  public abstract String resolveInsertAlias(String paramString1, String paramString2);
  
  public abstract String resolveSearchAlias(String paramString1, String paramString2);
  
  public abstract String accountNameFromSearchAlias(String paramString);
  
  public abstract String eventTypeFromSearchAlias(String paramString);
  
  public abstract String resolveBaseIndexName(String paramString1, String paramString2);
  
  public abstract String getDocType(String paramString1, String paramString2);
  
  public abstract String resolveEventType(String paramString);
  
  public abstract DateTime indexCreationDateFromFullName(String paramString);
  
  public abstract String accountNameFromIndex(String paramString);
  
  public abstract String eventTypeFromIndex(String paramString);
  
  public abstract String baseIndexNameFromFullName(String paramString);
  
  public abstract String appendTimestampToBaseIndexName(String paramString);
  
  public abstract boolean isInsertAlias(String paramString);
  
  public abstract boolean isSearchAlias(String paramString);
  
  public abstract boolean isEventServiceIndex(String paramString);
  
  public abstract boolean isOldIndexManagementIndex(String paramString);
  
  public abstract String resolveOldIndexManagementDynamicIndexName();
  
  public abstract String resolveOldIndexManagementStaticIndexName();
  
  public abstract String oldIndexManagementDynamicEventTypesInsertAlias();
  
  public abstract String oldIndexManagementStaticEventTypesInsertAlias();
  
  public abstract String oldIndexManagementDynamicEventTypeDocType(String paramString1, String paramString2);
  
  public abstract String oldIndexManagementStaticEventTypeDocType(String paramString1, String paramString2);
  
  public abstract boolean isOldIndexManagementDynamicType(String paramString);
  
  public abstract boolean isOldIndexManagementStaticType(String paramString);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/IndexNameResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */