package com.appdynamics.analytics.processor.event.upsert;

import com.appdynamics.common.io.payload.Bytes;
import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public abstract interface Upsert
{
  @NotNull
  public abstract String getAccountName();
  
  @NotNull
  public abstract String getEventType();
  
  @NotNull
  public abstract String getBatchId();
  
  @Min(0L)
  public abstract int getBatchPosition();
  
  @Nullable
  public abstract String getCsvMergeFields();
  
  @NotNull
  public abstract String getCorrelationIdField();
  
  @NotNull
  public abstract String getCorrelationId();
  
  public abstract boolean isAccountNameInBytes();
  
  public abstract boolean isPickupTimestampInBytes();
  
  public abstract boolean isEventTimestampInBytes();
  
  @Nullable
  public abstract Bytes getBytes();
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/upsert/Upsert.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */