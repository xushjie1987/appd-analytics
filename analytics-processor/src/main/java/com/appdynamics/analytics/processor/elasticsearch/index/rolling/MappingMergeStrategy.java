package com.appdynamics.analytics.processor.elasticsearch.index.rolling;

import com.fasterxml.jackson.databind.JsonNode;

public abstract interface MappingMergeStrategy
{
  public abstract JsonNode merge(JsonNode paramJsonNode1, JsonNode paramJsonNode2);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/MappingMergeStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */