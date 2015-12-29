package com.appdynamics.analytics.agent;

public abstract interface Constants
{
  public static final String FILE_NAME_AGENT_YML = "analytics-agent.yml";
  public static final String FILE_NAME_AGENT_PROPERTIES = "analytics-agent.properties";
  public static final String MODE_EMBEDDED_PROP = "analytics.mode.embedded";
  public static final String CONFIG_VERSION_PROP = "ad.configuration.version";
  public static final int CONFIG_VERSION_CURRENT = 1;
  public static final String FILE_NAME_PIPELINE_TEMPLATE = "file-tail-pipeline.yml.template";
  public static final String DIR_NAME_JOB = "job";
  public static final String FILE_GLOB_JOB = "**.job";
  public static final String DIR_NAME_GROK = "grok";
  public static final String FILE_GLOB_GROK = "**.grok";
  public static final String VAR_PIPELINE_ID = "pipeline.id";
  public static final String VAR_DATE_TIME = "calendar.now.utc";
  public static final String FIELD_MESSAGE = "message";
  public static final String FIELD_PICKUP_TIMESTAMP = "pickupTimestamp";
  public static final String FIELD_EVENT_TIMESTAMP = "eventTimestamp";
  public static final String ADD_FIELD_SCRIPT_STAGE_URI = "xform:field:add:script";
  public static final String ADD_FIELD_STAGE_URI = "xform:field:add";
  public static final String ADD_FIELD_STAGE_HOST_PROP = "host";
  public static final String ADD_FIELD_STAGE_SOURCE_PROP = "source";
  public static final String ADD_FIELD_STAGE_SOURCE_TYPE_PROP = "sourceType";
  public static final String GROK_STAGE_URI = "xform:grok";
  public static final String GROK_STAGE_PATTERNS_PROP = "patterns";
}


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/Constants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */