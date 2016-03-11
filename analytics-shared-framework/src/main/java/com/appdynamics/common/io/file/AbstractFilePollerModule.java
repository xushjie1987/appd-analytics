/*     */ package com.appdynamics.common.io.file;
/*     */ 
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.io.FileFilterPathMatcher;
/*     */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.io.File;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.util.List;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.apache.commons.io.monitor.FileAlterationListener;
/*     */ import org.apache.commons.io.monitor.FileAlterationMonitor;
/*     */ import org.apache.commons.io.monitor.FileAlterationObserver;
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
/*     */ public abstract class AbstractFilePollerModule<F extends FilePollerConfiguration>
/*     */   extends Module<F>
/*     */ {
/*  32 */   private static final Logger log = LoggerFactory.getLogger(AbstractFilePollerModule.class);
/*     */   
/*     */   protected volatile FileAlterationMonitor monitor;
/*     */   
/*     */ 
/*     */   public void configure()
/*     */   {
/*  39 */     super.configure();
/*     */     
/*  41 */     long millis = ((FilePollerConfiguration)getConfiguration()).getPoll().toMilliseconds();
/*  42 */     FileAlterationMonitor fam = new FileAlterationMonitor(millis);
/*  43 */     fam.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setNameFormat(getUri() + "-poller-thread-%d").build());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  48 */     this.monitor = fam;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract FileAlterationListener newListener(String paramString, PathMatcher paramPathMatcher);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @PostConstruct
/*     */   protected void onStart()
/*     */   {
/*  65 */     log.debug("Starting [{}]", getUri());
/*     */     try
/*     */     {
/*  68 */       this.monitor.start();
/*     */     } catch (Exception e) {
/*  70 */       Throwables.propagate(e);
/*     */     }
/*     */     
/*     */ 
/*  74 */     List<FilePathConfiguration> pathConfigs = ((FilePollerConfiguration)getConfiguration()).getPaths();
/*  75 */     for (FilePathConfiguration pathConfig : pathConfigs) {
/*  76 */       File pathDir = new File(pathConfig.getPath());
/*  77 */       if (!pathDir.exists()) {
/*  78 */         throw new IllegalArgumentException("Directory [" + pathDir.getAbsolutePath() + "] that is supposed to" + " be monitored by [" + getUri() + "] does not exist");
/*     */       }
/*     */       
/*  81 */       String path = pathDir.getAbsolutePath();
/*  82 */       log.info("Directory [{}] with files matching [{}] will be monitored by [{}]", new Object[] { path, pathConfig.getNameGlob(), getUri() });
/*     */       
/*     */ 
/*  85 */       PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pathConfig.getNameGlob());
/*     */       
/*     */ 
/*  88 */       FileFilterPathMatcher ffpm = new FileFilterPathMatcher(matcher);
/*  89 */       FileAlterationObserver observer = new FileAlterationObserver(path, ffpm);
/*     */       
/*  91 */       FileAlterationListener listener = newListener(path, matcher);
/*  92 */       observer.addListener(listener);
/*  93 */       this.monitor.addObserver(observer);
/*     */     }
/*  95 */     log.debug("Started [{}]", getUri());
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   protected void stop() {
/* 100 */     log.debug("Stopping [{}]", getUri());
/*     */     try {
/* 102 */       this.monitor.stop();
/*     */     } catch (Exception e) {
/* 104 */       Throwables.propagate(e);
/*     */     }
/* 106 */     log.debug("Stopped [{}]", getUri());
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/file/AbstractFilePollerModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */