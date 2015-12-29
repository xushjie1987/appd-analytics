package com.appdynamics.analytics.processor.elasticsearch.index.rolling;

import org.elasticsearch.action.admin.indices.stats.IndexStats;

public abstract interface RollingIndexShardSizingStrategy
{
  public abstract int calculateNumberOfShards(IndexStats paramIndexStats);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/rolling/RollingIndexShardSizingStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */