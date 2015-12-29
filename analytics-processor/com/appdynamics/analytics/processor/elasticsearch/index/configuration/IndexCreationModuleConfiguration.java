/*    */ package com.appdynamics.analytics.processor.elasticsearch.index.configuration;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IndexCreationModuleConfiguration
/*    */ {
/*    */   @JsonProperty
/*    */   EventIndexDefaults eventIndexDefaults;
/*    */   
/* 18 */   public EventIndexDefaults getEventIndexDefaults() { return this.eventIndexDefaults; }
/* 19 */   public void setEventIndexDefaults(EventIndexDefaults eventIndexDefaults) { this.eventIndexDefaults = eventIndexDefaults; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/configuration/IndexCreationModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */