/*    */ package com.appdynamics.analytics.pipeline.source;
/*    */ 
/*    */ import com.appdynamics.analytics.message.MessageSources;
/*    */ import com.appdynamics.analytics.message.api.MessagePack;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.google.inject.Inject;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MessageSourceStageFactory
/*    */   extends AbstractPipelineStageFactory<Void, MessagePack<?, ?>, Object, MessageSourceConfiguration>
/*    */ {
/* 21 */   private static final Logger log = LoggerFactory.getLogger(MessageSourceStageFactory.class);
/*    */   
/*    */   private MessageSources messageSources;
/*    */   
/*    */   @Inject
/*    */   public void onStart(MessageSources messageSources)
/*    */   {
/* 28 */     this.messageSources = messageSources;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PipelineStage<Void, MessagePack<?, ?>> create(PipelineStageParameters<MessagePack<?, ?>> parameters)
/*    */   {
/* 40 */     return new MessageSourceStage(parameters, (MessageSourceConfiguration)extract(parameters), this.messageSources);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/source/MessageSourceStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */