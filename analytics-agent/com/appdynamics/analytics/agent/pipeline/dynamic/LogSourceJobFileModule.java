/*    */ package com.appdynamics.analytics.agent.pipeline.dynamic;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.source.LogSources;
/*    */ import com.appdynamics.common.framework.util.FrameworkHelper;
/*    */ import com.appdynamics.common.io.file.AbstractFilePollerModule;
/*    */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*    */ import java.io.File;
/*    */ import java.net.URL;
/*    */ import java.nio.file.PathMatcher;
/*    */ import javax.annotation.Nullable;
/*    */ import org.apache.commons.io.monitor.FileAlterationListener;
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
/*    */ public class LogSourceJobFileModule
/*    */   extends AbstractFilePollerModule<LogSourceJobFileConfiguration>
/*    */ {
/* 32 */   private static final Logger log = LoggerFactory.getLogger(LogSourceJobFileModule.class);
/*    */   
/*    */   private volatile LogSourceJobFileParser jobFileParser;
/*    */   
/*    */   private volatile LogSourceJobFileListener listener;
/*    */   @Inject
/*    */   @Nullable
/*    */   private volatile LogSources logSources;
/*    */   
/*    */   public void configure()
/*    */   {
/* 43 */     String templatePath = findTemplatePath("file-tail-pipeline.yml.template");
/* 44 */     String jobFilesDirPath = getJobFilesDirPath();
/* 45 */     this.jobFileParser = new LogSourceJobFileParser(jobFilesDirPath, templatePath, FrameworkHelper.getProperties());
/*    */     
/* 47 */     bind(LogSourceJobFileParser.class).toInstance(this.jobFileParser);
/*    */     
/* 49 */     super.configure();
/*    */   }
/*    */   
/*    */   protected FileAlterationListener newListener(String path, PathMatcher pathMatcher)
/*    */   {
/* 54 */     if (this.listener == null) {
/* 55 */       this.listener = new LogSourceJobFileListener(this.logSources, this.jobFileParser);
/*    */     }
/* 57 */     return this.listener;
/*    */   }
/*    */   
/*    */   private String findTemplatePath(String inputPath) {
/* 61 */     File file = new File(inputPath);
/* 62 */     if (!file.exists()) {
/* 63 */       URL url = getClass().getClassLoader().getResource(inputPath);
/* 64 */       if (url == null) {
/* 65 */         throw new IllegalArgumentException("The template file that is supposed to be located at [" + file.getAbsolutePath() + "] does not exist");
/*    */       }
/*    */       
/* 68 */       return file.getPath();
/*    */     }
/* 70 */     return file.getAbsolutePath();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private String getJobFilesDirPath()
/*    */   {
/* 80 */     String jobFilesDirPath = ((FilePathConfiguration)Iterables.getOnlyElement(((LogSourceJobFileConfiguration)getConfiguration()).getPaths())).getPath();
/* 81 */     File file = new File(jobFilesDirPath);
/* 82 */     if (!file.isDirectory()) {
/* 83 */       throw new IllegalArgumentException(String.format("Configured job files directory path [%s] either does not exist or is not a directory", new Object[] { jobFilesDirPath }));
/*    */     }
/*    */     
/*    */ 
/* 87 */     return jobFilesDirPath;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */