package com.appdynamics.analytics.shared.rest.client.eventservice;

import java.util.List;
import rx.Observable;

public abstract interface AsyncEventServiceClient
{
  public abstract Observable<Void> registerEventType(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract Observable<Void> updateEventType(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract Observable<String> getEventType(String paramString1, String paramString2, String paramString3);
  
  public abstract Observable<String> getEventTypeUsageNumDocuments(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);
  
  public abstract Observable<String> getEventTypeUsageNumDocumentFragments(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt);
  
  public abstract Observable<String> getEventTypeUsageNumBytes(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt);
  
  public abstract Observable<Void> deleteEventType(String paramString1, String paramString2, String paramString3);
  
  public abstract Observable<Void> publishEvents(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract Observable<Void> upsertEvents(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, List<String> paramList);
  
  public abstract Observable<String> searchEvents(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract Observable<String> multiSearchEvents(String paramString1, String paramString2, String paramString3);
  
  public abstract Observable<String> queryEvents(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, boolean paramBoolean);
  
  public abstract Observable<String> searchErrors(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract Observable<Void> ping(String paramString1, String paramString2);
  
  public abstract Observable<String> relevantFields(String paramString1, String paramString2, String paramString3, String paramString4);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/AsyncEventServiceClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */