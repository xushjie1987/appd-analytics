package com.appdynamics.analytics.processor.event.hiddenfields;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.ImplementedBy;
import java.util.List;

@ImplementedBy(ElasticSearchHiddenFieldsService.class)
public abstract interface HiddenFieldsService
{
  public abstract void hideFields(String paramString1, String paramString2, List<HiddenField> paramList);
  
  public abstract List<HiddenField> getHiddenFields(String paramString1, String paramString2);
  
  public abstract void unhideField(String paramString1, String paramString2, String paramString3);
  
  public abstract void pruneHiddenFieldsFromMapping(String paramString1, String paramString2, ObjectNode paramObjectNode);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/hiddenfields/HiddenFieldsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */