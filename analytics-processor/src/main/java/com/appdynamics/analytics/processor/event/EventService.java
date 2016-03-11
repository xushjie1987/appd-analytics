package com.appdynamics.analytics.processor.event;

import com.appdynamics.analytics.processor.event.parsers.ObjectListParser;
import com.appdynamics.analytics.processor.event.resource.MoveEventTypeRequest;
import com.appdynamics.analytics.processor.event.upsert.Upsert;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.ImplementedBy;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTime;

@ImplementedBy(ElasticSearchEventService.class)
public abstract interface EventService
{
  public static final String META_DATA_FIELD = "metaData";
  public static final String PICKUP_TIMESTAMP = "pickupTimestamp";
  public static final String EVENT_TIMESTAMP = "eventTimestamp";
  public static final String FIELD_ID = "_id";
  public static final int CURRENT_VERSION = 2;
  
  public abstract void registerEventType(int paramInt, String paramString1, String paramString2, String paramString3);
  
  public abstract void updateEventType(int paramInt, String paramString1, String paramString2, String paramString3);
  
  public abstract void bulkUpdateEventType(int paramInt, String paramString1, String paramString2);
  
  public abstract JsonNode getEventType(int paramInt, String paramString1, String paramString2);
  
  public abstract boolean eventTypeExists(int paramInt, String paramString1, String paramString2);
  
  public abstract void deleteEventType(String paramString1, String paramString2);
  
  public abstract void publishEvents(int paramInt1, String paramString1, String paramString2, byte[] paramArrayOfByte, int paramInt2, int paramInt3, String paramString3);
  
  public abstract void moveEvents(int paramInt, String paramString1, String paramString2, MoveEventTypeRequest paramMoveEventTypeRequest);
  
  public abstract void publishEvents(int paramInt, String paramString1, String paramString2, ObjectListParser paramObjectListParser, String paramString3);
  
  public abstract EventTypeMetaData getEventTypeMetaData(String paramString1, String paramString2);
  
  public abstract void searchEvents(int paramInt, String paramString1, String paramString2, String paramString3, OutputStream paramOutputStream);
  
  public abstract void multiSearchEvents(int paramInt, String paramString1, String paramString2, OutputStream paramOutputStream);
  
  public abstract void queryEvents(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt2, boolean paramBoolean, OutputStream paramOutputStream);
  
  public abstract Map<String, Set<String>> eventTypesByAccount();
  
  public abstract void upsertEvents(int paramInt, List<? extends Upsert> paramList);
  
  public abstract long getCount(int paramInt, String paramString1, String paramString2, DateTime paramDateTime1, DateTime paramDateTime2);
  
  public abstract JsonNode relevantFields(int paramInt, String paramString1, String paramString2, String paramString3);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/EventService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */