/*     */ package com.appdynamics.analytics.agent.source;
/*     */ 
/*     */ import com.appdynamics.analytics.agent.base.LogBase;
/*     */ import com.appdynamics.analytics.agent.field.ExtractedFieldsManager;
/*     */ import com.appdynamics.analytics.agent.input.tail.TailLogInputConfiguration;
/*     */ import com.appdynamics.analytics.agent.pipeline.dynamic.LogSourceJobFileParser;
/*     */ import com.appdynamics.analytics.agent.pipeline.file.TailStageConfiguration;
/*     */ import com.appdynamics.analytics.agent.source.tail.LogSourceState;
/*     */ import com.appdynamics.analytics.agent.source.tail.TailLogSourceConfiguration;
/*     */ import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
/*     */ import com.appdynamics.analytics.pipeline.framework.PipelineStageConfiguration;
/*     */ import com.appdynamics.common.io.IoHelper;
/*     */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*     */ import com.appdynamics.common.util.item.Items;
/*     */ import com.codahale.metrics.health.HealthCheck.Result;
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.concurrent.NotThreadSafe;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class LogSources extends LogBase
/*     */ {
/*  52 */   private static final Logger log = LoggerFactory.getLogger(LogSources.class);
/*     */   
/*     */   public static final String WATERMARKS_BASE_FILENAME = "watermarks.json";
/*     */   
/*     */   final File watermarksFile;
/*     */   
/*     */   final File watermarksBackupFile;
/*     */   
/*     */   private final Items<Object, LogSource> sources;
/*     */   
/*     */   private final ExecutorService sourcesThreadPool;
/*     */   
/*     */   private final LogSourcesConfiguration sourcesConfiguration;
/*     */   
/*     */   private final JsonFactory jsonFactory;
/*     */   
/*     */   private final ObjectMapper objectMapper;
/*     */   
/*     */   private final ExtractedFieldsManager extractedFieldsManager;
/*     */   PersistWatermarkThread persistWatermarkThread;
/*     */   java.util.concurrent.Future watermarkFuture;
/*     */   
/*     */   public LogSources(LogComponentFactory factory, LogSourcesConfiguration sourcesConfiguration, ExtractedFieldsManager extractedFieldsManager, ExecutorService executorService)
/*     */   {
/*  76 */     super("Log Sources", factory, executorService);
/*     */     
/*  78 */     this.sources = new Items("Log Sources");
/*  79 */     this.sourcesThreadPool = java.util.concurrent.Executors.newCachedThreadPool(ConcurrencyHelper.newDaemonThreadFactory("log-sources-thread-%d"));
/*  80 */     this.sourcesConfiguration = sourcesConfiguration;
/*  81 */     this.watermarksFile = new File(sourcesConfiguration.getWatermarkPath(), "watermarks.json");
/*  82 */     this.watermarksBackupFile = new File(String.format("%s.backup", new Object[] { this.watermarksFile }));
/*  83 */     this.jsonFactory = new JsonFactory();
/*  84 */     this.objectMapper = Reader.DEFAULT_JSON_MAPPER;
/*  85 */     this.extractedFieldsManager = extractedFieldsManager;
/*     */   }
/*     */   
/*     */   private void addSource(LogSource source) {
/*  89 */     this.sources.add(source);
/*  90 */     if (isRunning()) {
/*  91 */       source.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void addSource(LogSourceConfiguration sourceConfiguration) throws IllegalArgumentException {
/*  96 */     if ((sourceConfiguration instanceof TailLogSourceConfiguration)) {
/*  97 */       Object id = sourceConfiguration.getId();
/*     */       
/*  99 */       if (!this.sources.contains(id)) {
/* 100 */         LogSource logSource = this.factory.createLogSource(sourceConfiguration, this.sourcesThreadPool);
/* 101 */         addSource(logSource);
/*     */       } else {
/* 103 */         log.info("Source [{}] already running, no need to add!", id);
/*     */       }
/*     */     } else {
/* 106 */       throw new IllegalArgumentException("Unrecognized log source.");
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeSource(Object sourceId) {
/* 111 */     LogSource source = (LogSource)this.sources.remove(sourceId);
/* 112 */     if (isRunning()) {
/* 113 */       source.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean existsSource(Object sourceId) {
/* 118 */     return this.sources.get(sourceId) != null;
/*     */   }
/*     */   
/*     */   public synchronized void start()
/*     */   {
/* 123 */     if (!isRunning()) {
/* 124 */       super.start();
/*     */       
/* 126 */       ensureWatermarksPathExists();
/*     */       
/* 128 */       for (LogSource source : this.sources.getAll()) {
/* 129 */         source.start();
/*     */       }
/*     */       
/* 132 */       startWriteWatermarkThread();
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void stop()
/*     */   {
/* 138 */     if (isRunning()) {
/* 139 */       super.stop();
/*     */       
/* 141 */       for (LogSource source : this.sources.getAll()) {
/* 142 */         source.stop();
/*     */       }
/*     */       
/* 145 */       stopWriteWatermarkThread();
/*     */       
/*     */ 
/* 148 */       this.persistWatermarkThread.recordUpdatedWatermark();
/*     */     }
/*     */   }
/*     */   
/*     */   public HealthCheck.Result check()
/*     */   {
/* 154 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 156 */     boolean allRunning = true;
/* 157 */     int i = 0;
/* 158 */     for (LogSource logSource : this.sources.getAll()) {
/* 159 */       if (i > 0) {
/* 160 */         sb.append("; ");
/*     */       }
/* 162 */       i++;
/* 163 */       sb.append("source: ").append(logSource.getId());
/* 164 */       HealthCheck.Result sourceHealth = logSource.check();
/* 165 */       sb.append(", details: ").append(sourceHealth.getMessage());
/* 166 */       allRunning = (allRunning) && (sourceHealth.isHealthy());
/*     */     }
/*     */     
/* 169 */     return allRunning ? HealthCheck.Result.healthy(sb.toString()) : HealthCheck.Result.unhealthy(sb.toString());
/*     */   }
/*     */   
/*     */   public void loadWatermarks(List<FilePathConfiguration> jobPaths, LogSourceJobFileParser logSourceJobFileParser) {
/* 173 */     Collection<LogSourceState> sourceStates = null;
/*     */     try {
/* 175 */       sourceStates = loadWatermarksFromFile(this.watermarksFile);
/* 176 */       loadWatermarksIntoSourceStates(jobPaths, logSourceJobFileParser, sourceStates);
/*     */     } catch (IOException except) {
/* 178 */       log.info("Failed to load source watermarks [{}] with error [{}].  Trying backup file next [{}]", new Object[] { this.watermarksFile, except.getMessage(), this.watermarksBackupFile });
/*     */       try
/*     */       {
/* 181 */         sourceStates = loadWatermarksFromFile(this.watermarksBackupFile);
/* 182 */         loadWatermarksIntoSourceStates(jobPaths, logSourceJobFileParser, sourceStates);
/*     */       } catch (IOException innerExcept) {
/* 184 */         log.info("Failed to load backup source watermarks [{}] with error [{}].  This is probably first run.", this.watermarksBackupFile, innerExcept.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void loadWatermarksIntoSourceStates(List<FilePathConfiguration> jobPaths, LogSourceJobFileParser logSourceJobFileParser, Collection<LogSourceState> sourceStates)
/*     */   {
/* 192 */     List<LogSourceConfiguration> sourceConfigurations = getLogSourceConfigurationsFromPaths(jobPaths, logSourceJobFileParser);
/*     */     
/*     */ 
/* 195 */     List<String> configurationIds = new ArrayList();
/* 196 */     for (LogSourceConfiguration sourceConfiguration : sourceConfigurations) {
/* 197 */       configurationIds.add(sourceConfiguration.getName());
/*     */     }
/*     */     
/* 200 */     for (LogSourceState sourceState : sourceStates) {
/* 201 */       LogSourceConfiguration logSourceConfig = sourceState.getLogSourceConfiguration();
/* 202 */       if (configurationIds.contains(logSourceConfig.getName())) {
/* 203 */         log.info("Log source [{}] initialized from previous watermark state", logSourceConfig.getName());
/* 204 */         LogSource logSource = this.factory.createLogSource(logSourceConfig, this.sourcesThreadPool);
/* 205 */         logSource.setWatermarkState(sourceState.getWatermarkState());
/* 206 */         addSource(logSource);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private List<LogSourceConfiguration> getLogSourceConfigurationsFromPaths(List<FilePathConfiguration> paths, LogSourceJobFileParser logSourceJobFileParser)
/*     */   {
/* 214 */     ArrayList<LogSourceConfiguration> sourceConfigurations = new ArrayList();
/* 215 */     for (FilePathConfiguration path : paths) {
/* 216 */       sourceConfigurations.addAll(getLogSourceConfigurationsFromPath(path, logSourceJobFileParser));
/*     */     }
/* 218 */     return sourceConfigurations;
/*     */   }
/*     */   
/*     */ 
/*     */   private List<LogSourceConfiguration> getLogSourceConfigurationsFromPath(FilePathConfiguration jobPath, final LogSourceJobFileParser logSourceJobFileParser)
/*     */   {
/* 224 */     final ArrayList<LogSourceConfiguration> sourceConfigurations = new ArrayList();
/* 225 */     Path path = Paths.get(jobPath.getPath(), new String[0]);
/* 226 */     final PathMatcher matcher = FileSystems.getDefault().getPathMatcher(IoHelper.getGlobPatternFromPathGlob(jobPath));
/*     */     
/*     */     try
/*     */     {
/* 230 */       Files.walkFileTree(path, new java.util.HashSet(), 1, new java.nio.file.SimpleFileVisitor()
/*     */       {
/*     */         public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
/* 233 */           LogSources.log.debug("Skipping file [{}] because of IO exception [{}]", file, exc.getMessage());
/* 234 */           return FileVisitResult.CONTINUE;
/*     */         }
/*     */         
/*     */         public FileVisitResult visitFile(Path filePath, BasicFileAttributes attr) throws IOException
/*     */         {
/*     */           try {
/* 240 */             boolean matches = matcher.matches(filePath);
/* 241 */             if (matches) {
/* 242 */               PipelineConfiguration pipelineConfiguration = logSourceJobFileParser.parsePipelineConfigurationFromFile(filePath.toFile());
/*     */               
/*     */ 
/* 245 */               if (pipelineConfiguration.isEnabled()) {
/* 246 */                 LogSourceConfiguration logSourceConfiguration = LogSources.this.convertPipelineConfigurationToTailLogSourceConfiguration(pipelineConfiguration.getId(), pipelineConfiguration);
/*     */                 
/*     */ 
/* 249 */                 sourceConfigurations.add(logSourceConfiguration);
/*     */               } else {
/* 251 */                 LogSources.log.info("Skipping source [{}] because it is disabled.", pipelineConfiguration.getId());
/*     */               }
/*     */             }
/*     */           } catch (Exception except) {
/* 255 */             LogSources.log.error(String.format("Failed to load job file [%s], skipping.", new Object[] { filePath.toString() }), except);
/*     */           }
/*     */           
/* 258 */           return FileVisitResult.CONTINUE;
/*     */         }
/*     */       });
/*     */     } catch (IOException except) {
/* 262 */       log.error(String.format("Failed to find job files in directory [%s]", new Object[] { path.toString() }), except);
/*     */     }
/*     */     
/* 265 */     return sourceConfigurations;
/*     */   }
/*     */   
/*     */   public Collection<LogSourceState> loadWatermarksFromFile(File sourceStatesFile) throws IOException
/*     */   {
/* 270 */     LinkedList<LogSourceState> resultSourceStates = new LinkedList();
/*     */     
/* 272 */     JsonParser jsonParser = this.jsonFactory.createParser(sourceStatesFile);
/*     */     
/* 274 */     Iterator<LogSourceState> sourceStates = this.objectMapper.readValues(jsonParser, LogSourceState.class);
/*     */     
/* 276 */     while (sourceStates.hasNext()) {
/* 277 */       LogSourceState sourceState = (LogSourceState)sourceStates.next();
/* 278 */       resultSourceStates.add(sourceState);
/*     */     }
/* 280 */     jsonParser.close();
/*     */     
/* 282 */     return resultSourceStates;
/*     */   }
/*     */   
/*     */   public void saveWatermarks(File sourceStatesFile) throws IOException
/*     */   {
/* 287 */     saveWatermarks(sourceStatesFile, this.sources.getAll());
/*     */   }
/*     */   
/*     */   public void saveWatermarks(File sourceStatesFile, Collection<LogSource> sources) throws IOException
/*     */   {
/* 292 */     JsonGenerator jsonGenerator = this.jsonFactory.createGenerator(sourceStatesFile, JsonEncoding.UTF8);
/*     */     
/*     */ 
/* 295 */     for (LogSource source : sources) {
/* 296 */       LogSourceState sourceState = new LogSourceState(source.getConfiguration(), source.getWatermarkState());
/*     */       
/* 298 */       this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonGenerator, sourceState);
/*     */     }
/*     */     
/* 301 */     jsonGenerator.flush();
/* 302 */     jsonGenerator.close();
/*     */   }
/*     */   
/*     */   private void ensureWatermarksPathExists() {
/* 306 */     File sourcePath = new File(this.sourcesConfiguration.getWatermarkPath());
/* 307 */     if (!sourcePath.exists()) {
/*     */       try {
/* 309 */         if (!sourcePath.mkdir()) {
/* 310 */           log.error("Failed to create path [{}]", sourcePath.getAbsolutePath());
/*     */         }
/*     */       } catch (Exception except) {
/* 313 */         log.error("Failed to create path [{}] because [{}]", sourcePath.getAbsolutePath(), except.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   class PersistWatermarkThread implements Runnable
/*     */   {
/*     */     PersistWatermarkThread() {}
/*     */     
/*     */     void recordUpdatedWatermark() {
/*     */       try {
/* 324 */         LogSources.this.ensureWatermarksPathExists();
/*     */         
/* 326 */         Files.copy(Paths.get(LogSources.this.watermarksFile.getAbsolutePath(), new String[0]), Paths.get(LogSources.this.watermarksBackupFile.getAbsolutePath(), new String[0]), new java.nio.file.CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (FileNotFoundException|NoSuchFileException except) {}catch (IOException except)
/*     */       {
/*     */ 
/* 333 */         LogSources.log.error(String.format("Failed to write sources watermark backup file [%s]... not backing up watermarks!!", new Object[] { LogSources.this.watermarksBackupFile.getAbsolutePath() }), except);
/*     */       }
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 339 */         LogSources.this.saveWatermarks(LogSources.this.watermarksFile);
/*     */       } catch (IOException except) {
/* 341 */         LogSources.log.error(String.format("Failed to write sources watermark [%s] state successfully... not tracking watermarks!!", new Object[] { LogSources.this.watermarksFile.getAbsolutePath() }), except);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void run()
/*     */     {
/* 351 */       while (LogSources.this.isRunning()) {
/* 352 */         recordUpdatedWatermark();
/*     */         
/* 354 */         LogSources.this.sleepWhileRunning(LogSources.this.sourcesConfiguration.getSaveSourceStateInterval().toMilliseconds());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void startWriteWatermarkThread() {
/* 360 */     this.persistWatermarkThread = new PersistWatermarkThread();
/* 361 */     this.watermarkFuture = this.executorService.submit(this.persistWatermarkThread);
/*     */   }
/*     */   
/*     */   void stopWriteWatermarkThread() {
/* 365 */     if (this.watermarkFuture != null) {
/* 366 */       ConcurrencyHelper.cancel(this.watermarkFuture, this.sourcesConfiguration.getSaveSourceStateInterval().toMilliseconds() * 2L, true, log);
/*     */     }
/*     */   }
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
/*     */   public void upsertSourceFromPipeline(Object id, PipelineConfiguration pipelineConfiguration)
/*     */   {
/* 382 */     LogSource logSource = (LogSource)this.sources.get(id);
/* 383 */     LogWatermarkState watermarkState = null;
/* 384 */     if (logSource != null) {
/* 385 */       removeSourceFromPipeline(id);
/* 386 */       watermarkState = logSource.getWatermarkState();
/*     */     }
/* 388 */     if (pipelineConfiguration.isEnabled()) {
/* 389 */       TailLogSourceConfiguration sourceConfiguration = convertPipelineConfigurationToTailLogSourceConfiguration(id, pipelineConfiguration);
/*     */       
/* 391 */       LogSource newLogSource = this.factory.createLogSource(sourceConfiguration, this.sourcesThreadPool);
/* 392 */       if (watermarkState != null) {
/* 393 */         newLogSource.setWatermarkState(watermarkState);
/*     */       }
/* 395 */       addSource(newLogSource);
/*     */     } else {
/* 397 */       log.info("Skipped source [{}] because it is disabled.", id);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeSourceFromPipeline(Object id) {
/* 402 */     removeSource(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getLogSourceTypes()
/*     */   {
/* 411 */     Collection<? extends LogSource> logSources = this.sources.getAll();
/* 412 */     List<String> sourceTypes = new ArrayList(logSources.size());
/* 413 */     for (LogSource logSource : logSources) {
/* 414 */       sourceTypes.add(logSource.getSourceType());
/*     */     }
/* 416 */     return sourceTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<? extends LogSource> getLogSources()
/*     */   {
/* 425 */     return this.sources.getAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   TailLogSourceConfiguration convertPipelineConfigurationToTailLogSourceConfiguration(Object id, PipelineConfiguration configuration)
/*     */   {
/* 432 */     ArrayList<PipelineStageConfiguration> stages = new ArrayList(configuration.getStages());
/* 433 */     PipelineStageConfiguration tailStage = (PipelineStageConfiguration)stages.remove(0);
/*     */     
/* 435 */     TailStageConfiguration tailStageConfiguration = (TailStageConfiguration)Reader.readFrom(TailStageConfiguration.class, tailStage.getProperties());
/*     */     
/*     */ 
/* 438 */     TailLogSourceConfiguration sourceConfiguration = new TailLogSourceConfiguration();
/* 439 */     sourceConfiguration.setName(id.toString());
/*     */     
/* 441 */     FilePathConfiguration pathConfiguration = new FilePathConfiguration();
/* 442 */     pathConfiguration.setPath(tailStageConfiguration.getPath());
/* 443 */     pathConfiguration.setNameGlob(tailStageConfiguration.getNameGlob());
/*     */     
/* 445 */     sourceConfiguration.setSourcePath(pathConfiguration);
/*     */     
/* 447 */     sourceConfiguration.setDirectoryPollingInterval(new TimeUnitConfiguration(tailStageConfiguration.getPollMillis(), TimeUnit.MILLISECONDS));
/*     */     
/*     */ 
/*     */ 
/* 451 */     PipelineConfiguration tailLogPipeline = new PipelineConfiguration();
/* 452 */     tailLogPipeline.setId(configuration.getId());
/* 453 */     tailLogPipeline.setStages(stages);
/* 454 */     tailLogPipeline.setEnabled(configuration.isEnabled());
/*     */     
/* 456 */     TailLogInputConfiguration tailLogConfiguration = new TailLogInputConfiguration();
/* 457 */     tailLogConfiguration.setPipelineConfiguration(tailLogPipeline);
/* 458 */     tailLogConfiguration.setStartAtEnd(tailStageConfiguration.isStartAtEnd());
/* 459 */     sourceConfiguration.setTailLogInputConfiguration(tailLogConfiguration);
/*     */     
/*     */ 
/* 462 */     String sourceType = getSourceType(configuration);
/*     */     
/* 464 */     if (sourceType == null) {
/* 465 */       throw new IllegalStateException(String.format("Could not find mandatory sourceType field within pipeline configuration for log source [%s]", new Object[] { id }));
/*     */     }
/*     */     
/*     */ 
/* 469 */     log.debug("Log source [{}] is configured to have the following source type [{}]", id, sourceType);
/* 470 */     sourceConfiguration.setSourceType(sourceType);
/*     */     
/*     */ 
/* 473 */     Set<String> extractedFieldPatterns = this.extractedFieldsManager.getExtractedFieldPatterns(sourceType);
/* 474 */     log.debug("Retrieved the following extracted field patterns {} for log source [{}]", extractedFieldPatterns, id);
/*     */     
/* 476 */     sourceConfiguration.setExtractedFieldPatterns(extractedFieldPatterns);
/*     */     
/*     */ 
/* 479 */     this.extractedFieldsManager.addExtractedFieldPatterns(tailLogPipeline, extractedFieldPatterns);
/*     */     
/* 481 */     return sourceConfiguration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getSourceType(PipelineConfiguration pipelineConfiguration)
/*     */   {
/* 492 */     for (PipelineStageConfiguration stage : pipelineConfiguration.getStages()) {
/* 493 */       if ("xform:field:add".equals(stage.getUri())) {
/* 494 */         return (String)stage.getProperties().get("sourceType");
/*     */       }
/*     */     }
/* 497 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/LogSources.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */