/*    */ package com.appdynamics.analytics.agent.source;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.field.ExtractedFieldsManager;
/*    */ import com.appdynamics.analytics.agent.input.tail.FileSignature;
/*    */ import com.appdynamics.analytics.agent.input.tail.TailFileState;
/*    */ import com.appdynamics.analytics.agent.pipeline.dynamic.LogSourceJobFileParser;
/*    */ import com.appdynamics.analytics.agent.source.tail.DirectoryPoller;
/*    */ import com.appdynamics.analytics.agent.source.tail.FileInputScanner;
/*    */ import com.appdynamics.analytics.agent.source.tail.TailLogSource;
/*    */ import com.appdynamics.analytics.agent.source.tail.TailLogSourceConfiguration;
/*    */ import com.appdynamics.analytics.pipeline.framework.Pipeline;
/*    */ import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
/*    */ import com.appdynamics.analytics.pipeline.framework.Pipelines;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*    */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */ import com.appdynamics.common.util.lifecycle.RunningState;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogSourcesModule
/*    */   extends Module<LogSourcesConfiguration>
/*    */ {
/* 38 */   private static final Logger log = LoggerFactory.getLogger(LogSourcesModule.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @Provides
/*    */   @Singleton
/*    */   LogSources makeLogSources(Pipelines pipelines, LogSourceJobFileParser logSourceJobFileParser, LifecycleInjector lifecycleInjector, ConsolidatedHealthCheck healthCheck, ExtractedFieldsManager extractedFieldsManager)
/*    */   {
/* 47 */     LcfFactory factory = new LcfFactory(pipelines);
/* 48 */     LogSources logSources = new LogSources(factory, (LogSourcesConfiguration)getConfiguration(), extractedFieldsManager, Executors.newCachedThreadPool(ConcurrencyHelper.newDaemonThreadFactory("log-sources-admin-thread-%d")));
/*    */     
/* 50 */     logSources.loadWatermarks(((LogSourcesConfiguration)getConfiguration()).getJobPaths(), logSourceJobFileParser);
/*    */     
/* 52 */     logSources = (LogSources)lifecycleInjector.inject(logSources);
/* 53 */     healthCheck.register(logSources);
/* 54 */     return logSources;
/*    */   }
/*    */   
/*    */   @Inject
/*    */   void selfStart(LogSources logSources) {
/* 59 */     log.info("Started");
/*    */   }
/*    */   
/*    */ 
/*    */   static class LcfFactory
/*    */     implements LogComponentFactory
/*    */   {
/*    */     final Pipelines pipelines;
/*    */     
/*    */     LcfFactory(Pipelines pipelines)
/*    */     {
/* 70 */       this.pipelines = pipelines;
/*    */     }
/*    */     
/*    */     public RunningState createRunningState(boolean initialValue) {
/* 74 */       return new RunningState(initialValue);
/*    */     }
/*    */     
/*    */     public Pipeline createPipeline(PipelineConfiguration pipelineConfiguration) {
/* 78 */       return this.pipelines.createPipeline(pipelineConfiguration);
/*    */     }
/*    */     
/*    */     public DirectoryPoller createDirectoryPoller(FilePathConfiguration filePathConfiguration) {
/* 82 */       return new DirectoryPoller(filePathConfiguration);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     public FileInputScanner createFileInputScanner(DirectoryPoller directoryPoller, Map<FileSignature, TailFileState> partialIdToTailStates, Map<FileSignature, TailFileState> fileIdToTailStates, Function<TailFileState, Boolean> ensureInputExists)
/*    */     {
/* 89 */       return new FileInputScanner(directoryPoller, partialIdToTailStates, fileIdToTailStates, ensureInputExists);
/*    */     }
/*    */     
/*    */     public LogSource createLogSource(LogSourceConfiguration configuration, ExecutorService executorService) throws IllegalArgumentException
/*    */     {
/* 94 */       if ((configuration instanceof TailLogSourceConfiguration)) {
/* 95 */         TailLogSourceConfiguration tailLogSourceConfiguration = (TailLogSourceConfiguration)configuration;
/* 96 */         return new TailLogSource(tailLogSourceConfiguration, this, executorService);
/*    */       }
/* 98 */       throw new IllegalArgumentException("Unrecognized log source.");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/LogSourcesModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */