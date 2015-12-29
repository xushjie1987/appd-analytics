/*     */ package com.appdynamics.analytics.pipeline.source;
/*     */ 
/*     */ import com.appdynamics.analytics.message.MessageSources;
/*     */ import com.appdynamics.analytics.message.api.MessagePack;
/*     */ import com.appdynamics.analytics.message.api.MessageSource;
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*     */ import com.appdynamics.common.util.exception.ExceptionHandler;
/*     */ import com.appdynamics.common.util.exception.Exceptions;
/*     */ import com.appdynamics.common.util.execution.Operation;
/*     */ import com.appdynamics.common.util.execution.Retriable;
/*     */ import com.appdynamics.common.util.execution.RetryConfiguration;
/*     */ import com.appdynamics.common.util.execution.RetryWrapper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageSourceStage<SRC_ID, MSG>
/*     */   extends AbstractPipelineStage<Void, MessagePack<SRC_ID, MSG>>
/*     */ {
/*  39 */   private static final Logger log = LoggerFactory.getLogger(MessageSourceStage.class);
/*     */   
/*     */   final PipelineStageParameters<MessagePack<SRC_ID, MSG>> parameters;
/*     */   final MessageSourceConfiguration configuration;
/*     */   final MessageSources messageSources;
/*     */   final ExceptionHandler exceptionHandler;
/*     */   private volatile MessageSource<SRC_ID, MSG> source;
/*     */   private volatile RetryWrapper<SRC_ID> optRetryWrapper;
/*     */   
/*     */   MessageSourceStage(PipelineStageParameters<MessagePack<SRC_ID, MSG>> parameters, MessageSourceConfiguration configuration, MessageSources messageSources)
/*     */   {
/*  50 */     super(parameters);
/*  51 */     this.parameters = parameters;
/*  52 */     this.configuration = configuration;
/*  53 */     this.messageSources = messageSources;
/*  54 */     this.exceptionHandler = new ExceptionHandler(log, String.format("Pipeline [%s] was interrupted when it was polling the message source", new Object[] { printableName() }), String.format("Transient error occurred while processing input on pipeline [%s]", new Object[] { printableName() }), String.format("Permanent error occurred while processing input on pipeline [%s]", new Object[] { printableName() }), String.format("Unexpected error occurred while processing input on pipeline [%s]", new Object[] { printableName() }), configuration.isRethrowUnknownExceptions());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   RetryWrapper<SRC_ID> getOptRetryWrapper()
/*     */   {
/*  63 */     return this.optRetryWrapper;
/*     */   }
/*     */   
/*     */ 
/*     */   public void start()
/*     */   {
/*  69 */     Object messageSourceId = this.configuration.getMessageSourceId();
/*  70 */     Object messageSourceParams = this.configuration.getMessageSourceParams();
/*  71 */     this.source = (messageSourceParams == null ? this.messageSources.get(messageSourceId) : this.messageSources.get(messageSourceId, messageSourceParams));
/*     */     
/*     */ 
/*  74 */     Preconditions.checkArgument(this.source != null, "There is no message source registered under [" + String.valueOf(messageSourceId) + "]");
/*     */     
/*     */ 
/*  77 */     RetryConfiguration optRetryConfiguration = this.configuration.getRetry();
/*  78 */     if ((this.source.supportsRetriable()) && (optRetryConfiguration == null)) {
/*  79 */       log.debug("The source ([{}]) from which this pipeline ([{}]) is configured to consume produces [{}] messages. But retry options have not been provided, so retry options with default values will be used", new Object[] { this.source.getId(), this.parameters.getPipelineId(), Retriable.class.getSimpleName() });
/*     */       
/*     */ 
/*     */ 
/*  83 */       optRetryConfiguration = new RetryConfiguration();
/*  84 */     } else if ((!this.source.supportsRetriable()) && (optRetryConfiguration != null)) {
/*  85 */       log.warn("The source ([{}]) from which this pipeline ([{}]) is configured to consume, does not produce [{}] messages. But retry options have been provided and so they will be ignored", new Object[] { this.source.getId(), this.parameters.getPipelineId(), Retriable.class.getSimpleName() });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  90 */       optRetryConfiguration = null;
/*     */     }
/*  92 */     this.optRetryWrapper = (optRetryConfiguration != null ? new RetryWrapper(messageSourceId, optRetryConfiguration) : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void process(Void input)
/*     */   {
/*     */     try
/*     */     {
/* 103 */       doWork();
/*     */     } catch (InterruptedException e) {
/* 105 */       Exceptions.rethrowAsRuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void doWork() throws InterruptedException
/*     */   {
/* 111 */     boolean hasNext = this.optionalNextStage != null;
/* 112 */     boolean useRetryWrapper = this.optRetryWrapper != null;
/* 113 */     int maxMessagesPerPoll = this.configuration.getMaxMessagesPerPoll();
/* 114 */     long pollTimeoutMillis = this.configuration.getPollTimeoutMillis();
/*     */     
/* 116 */     RetryWrapper<SRC_ID> localRetryWrapper = this.optRetryWrapper;
/* 117 */     MessageSource<SRC_ID, MSG> localSource = this.source;
/* 118 */     PipelineStage<? super MessagePack<SRC_ID, MSG>, ?> localNextStage = this.optionalNextStage;
/*     */     for (;;)
/*     */     {
/* 121 */       MessagePack<SRC_ID, MSG> msgPack = null;
/*     */       try {
/* 123 */         msgPack = localSource.poll(maxMessagesPerPoll, pollTimeoutMillis, TimeUnit.MILLISECONDS);
/* 124 */         if ((msgPack == null) || (hasNext))
/*     */         {
/*     */ 
/*     */ 
/* 128 */           if (useRetryWrapper) {
/* 129 */             Retriable retriable = (Retriable)msgPack;
/*     */             
/* 131 */             Operation<? super Retriable> operation = localNextStage;
/* 132 */             localRetryWrapper.invoke(retriable, operation);
/*     */           } else {
/* 134 */             localNextStage.process(msgPack);
/*     */           } }
/*     */       } catch (Throwable t) {
/* 137 */         this.exceptionHandler.handle(t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public final void stop()
/*     */   {
/*     */     try {
/* 145 */       this.source.close();
/* 146 */       this.source = null;
/*     */     } catch (IOException e) {
/* 148 */       log.warn("Failed to close the message source", e);
/*     */     }
/* 150 */     super.stop();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/source/MessageSourceStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */