/*    */ package com.appdynamics.analytics.agent.pipeline.dynamic;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.source.LogSources;
/*    */ import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
/*    */ import java.io.File;
/*    */ import javax.annotation.concurrent.GuardedBy;
/*    */ import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogSourceJobFileListener
/*    */   extends FileAlterationListenerAdaptor
/*    */ {
/* 30 */   private static final Logger log = LoggerFactory.getLogger(LogSourceJobFileListener.class);
/*    */   
/*    */ 
/*    */   final LogSourceJobFileParser jobFileParser;
/*    */   
/*    */ 
/*    */   @GuardedBy("this")
/*    */   final LogSources logSources;
/*    */   
/*    */ 
/*    */   LogSourceJobFileListener(LogSources sources, LogSourceJobFileParser jobFileParser)
/*    */   {
/* 42 */     this.logSources = sources;
/* 43 */     this.jobFileParser = jobFileParser;
/*    */   }
/*    */   
/*    */   public void onFileCreate(File file)
/*    */   {
/* 48 */     log.debug("File [{}] was created", file.getAbsolutePath());
/* 49 */     upsert(file);
/*    */   }
/*    */   
/*    */   public void onFileChange(File file)
/*    */   {
/* 54 */     log.debug("File [{}] was modified", file.getAbsolutePath());
/* 55 */     upsert(file);
/*    */   }
/*    */   
/*    */   public void onFileDelete(File file)
/*    */   {
/* 60 */     log.debug("File [{}] was deleted", file.getAbsolutePath());
/* 61 */     upsertOrRemovePipeline(file.getName(), null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   void upsert(File input)
/*    */   {
/*    */     try
/*    */     {
/* 75 */       PipelineConfiguration pipelineConfiguration = this.jobFileParser.parsePipelineConfigurationFromFile(input);
/* 76 */       upsertOrRemovePipeline(pipelineConfiguration.getId(), pipelineConfiguration);
/*    */     } catch (Exception e) {
/* 78 */       log.error("Error occurred while processing file [" + input.getAbsolutePath() + "]", e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   void upsertOrRemovePipeline(Object pipelineId, PipelineConfiguration optNewPipelineConfig)
/*    */   {
/* 87 */     synchronized (this.logSources) {
/* 88 */       boolean exists = this.logSources.existsSource(pipelineId);
/* 89 */       if (optNewPipelineConfig != null) {
/* 90 */         this.logSources.upsertSourceFromPipeline(pipelineId, optNewPipelineConfig);
/*    */       }
/* 92 */       else if (exists) {
/* 93 */         this.logSources.removeSourceFromPipeline(pipelineId);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */