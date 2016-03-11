package com.appdynamics.analytics.processor.event;

import java.io.IOException;
import java.util.List;

public abstract interface ExtractedFieldsService
{
  public abstract List<ExtractedFieldDefinition> getExtractedFields(String paramString1, String paramString2);
  
  public abstract List<ExtractedFieldDefinition> getExtractedFields(String paramString1, String paramString2, List<String> paramList);
  
  public abstract ExtractedFieldDefinition getExtractedField(String paramString1, String paramString2, String paramString3)
    throws IOException;
  
  public abstract void createExtractedField(ExtractedFieldDefinition paramExtractedFieldDefinition)
    throws IOException;
  
  public abstract void updateExtractedField(ExtractedFieldDefinition paramExtractedFieldDefinition)
    throws IOException;
  
  public abstract void deleteExtractedField(String paramString1, String paramString2, String paramString3);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/ExtractedFieldsService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */