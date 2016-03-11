/*    */ package com.appdynamics.analytics.agent.pipeline.event;
/*    */ 
/*    */ import com.appdynamics.analytics.message.api.MessagePack;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*    */ import com.google.inject.Inject;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import javax.annotation.PostConstruct;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventServicePublishStageFactory
/*    */   extends AbstractPipelineStageFactory<MessagePack<String, String>, MessagePack<String, String>, EventServicePublishStageFactoryConfig, Object>
/*    */ {
/*    */   @Inject
/*    */   volatile Environment environment;
/*    */   @Inject
/*    */   volatile EventServiceClientModule.EventServiceClientFactory clientFactory;
/*    */   @Inject
/*    */   volatile ConsolidatedHealthCheck healthCheck;
/*    */   volatile MeteredHealthCheck meteredHealthCheck;
/*    */   
/*    */   @PostConstruct
/*    */   void start()
/*    */   {
/* 40 */     this.meteredHealthCheck = new MeteredHealthCheck(getUri(), this.environment);
/* 41 */     this.healthCheck.register(this.meteredHealthCheck);
/*    */   }
/*    */   
/*    */ 
/*    */   public PipelineStage<MessagePack<String, String>, MessagePack<String, String>> create(PipelineStageParameters<MessagePack<String, String>> parameters)
/*    */   {
/* 47 */     EventServicePublishStageFactoryConfig configuration = (EventServicePublishStageFactoryConfig)getConfiguration();
/*    */     
/* 49 */     return new EventServicePublishStage(parameters, configuration, this.meteredHealthCheck, this.clientFactory.createEventServiceClient(), this.clientFactory.getAccountName(), this.clientFactory.getAccessKey());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/event/EventServicePublishStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */