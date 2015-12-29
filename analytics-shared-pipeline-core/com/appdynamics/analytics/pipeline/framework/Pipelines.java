/*     */ package com.appdynamics.analytics.pipeline.framework;
/*     */ 
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageFactory;
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.analytics.pipeline.util.PipelineHelper;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.appdynamics.common.util.item.Items;
/*     */ import com.appdynamics.common.util.lifecycle.LifecycleAware;
/*     */ import com.appdynamics.common.util.lifecycle.LifecycleHelper;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
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
/*     */ public class Pipelines
/*     */   extends Items<Object, Pipeline<?>>
/*     */   implements LifecycleAware
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(Pipelines.class);
/*     */   
/*     */   final Map<String, PipelineStageFactory> pipelineStageFactories;
/*     */   
/*     */   final List<StaticPipelineConfiguration> optStaticPipelineConfigurations;
/*     */   private final ExecutorService pipelineExecutorService;
/*     */   
/*     */   public Pipelines(Map<String, PipelineStageFactory> pipelineStageFactories, List<StaticPipelineConfiguration> optStaticPipelineConfigurations)
/*     */   {
/*  47 */     super("Pipeline");
/*  48 */     this.pipelineStageFactories = pipelineStageFactories;
/*  49 */     this.optStaticPipelineConfigurations = optStaticPipelineConfigurations;
/*     */     
/*  51 */     log.debug("Loaded pipeline stage factories [\n  - {}\n]", Joiner.on("\n  - ").join(pipelineStageFactories.values()));
/*     */     
/*     */ 
/*  54 */     this.pipelineExecutorService = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("pipeline-thread-%d").setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
/*     */     {
/*     */ 
/*     */ 
/*     */       public void uncaughtException(Thread t, Throwable e)
/*     */       {
/*     */ 
/*     */ 
/*  62 */         Pipelines.log.error("Unexpected error occurred on thread [" + t.getName() + "]", e);
/*     */       }
/*     */     }).build());
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
/*     */ 
/*     */   public Pipeline createPipeline(PipelineConfiguration pipelineConfiguration)
/*     */   {
/*  80 */     Preconditions.checkArgument(pipelineConfiguration.isEnabled(), "Pipeline could not be created as the configuration indicates that it is not enabled");
/*     */     
/*     */ 
/*  83 */     List<PipelineStageConfiguration> scList = pipelineConfiguration.getStages();
/*  84 */     PipelineStageConfiguration[] stageConfigurations = (PipelineStageConfiguration[])scList.toArray(new PipelineStageConfiguration[scList.size()]);
/*     */     
/*     */ 
/*  87 */     PipelineStage[] stages = new PipelineStage[stageConfigurations.length];
/*  88 */     Object pipelineId = pipelineConfiguration.getId();
/*     */     
/*  90 */     PipelineStage nextStage = null;
/*     */     
/*  92 */     for (int i = stageConfigurations.length - 1; i >= 0; i--) {
/*  93 */       String uri = stageConfigurations[i].getUri();
/*  94 */       PipelineStageFactory pc = (PipelineStageFactory)this.pipelineStageFactories.get(uri);
/*  95 */       if (pc == null) {
/*  96 */         throw new ConfigurationException("No pipeline stage could be found for URI [" + uri + "]");
/*     */       }
/*     */       try {
/*  99 */         Map<String, Object> properties = stageConfigurations[i].getProperties();
/* 100 */         PipelineStageParameters stageParams = PipelineHelper.parameters(pipelineId, nextStage, properties);
/* 101 */         stages[i] = pc.create(stageParams);
/* 102 */         if (stages[i] == null) {
/* 103 */           throw new NullPointerException("Pipeline stage created by factory [" + uri + "] is null");
/*     */         }
/*     */       } catch (RuntimeException e) {
/* 106 */         throw new ConfigurationException("Error occurred while configuring pipeline stage [" + uri + "]", e);
/*     */       }
/*     */       
/* 109 */       nextStage = stages[i];
/*     */     }
/*     */     
/* 112 */     return new Pipeline(pipelineId, stages);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final Future<Void> executePipeline(Pipeline pipeline)
/*     */   {
/* 122 */     return this.pipelineExecutorService.submit(wrapCallable(pipeline, null));
/*     */   }
/*     */   
/*     */   private static Callable<Void> wrapCallable(Pipeline pipeline, final Object input) {
/* 126 */     new Callable()
/*     */     {
/*     */       public Void call() throws Exception
/*     */       {
/*     */         try {
/* 131 */           this.val$pipeline.call(input);
/*     */         } catch (Throwable t) {
/* 133 */           throw new RuntimeException("Error occurred while processing pipeline [" + String.valueOf(this.val$pipeline.getId()) + "]", t);
/*     */         }
/*     */         
/*     */ 
/* 137 */         return null;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void start()
/*     */   {
/* 147 */     if ((this.optStaticPipelineConfigurations != null) && (this.optStaticPipelineConfigurations.size() > 0)) {
/* 148 */       log.debug("Attempting to load [{}] statically defined pipelines", Integer.valueOf(this.optStaticPipelineConfigurations.size()));
/* 149 */       for (StaticPipelineConfiguration pipelineConfiguration : this.optStaticPipelineConfigurations) {
/* 150 */         createAddStartAndExecuteIfEnabled(pipelineConfiguration);
/*     */       }
/*     */     } else {
/* 153 */       log.debug("No static pipelines have been defined");
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
/*     */   public final void createAddStartAndExecuteIfEnabled(StaticPipelineConfiguration pipelineConfiguration)
/*     */   {
/* 181 */     if (!pipelineConfiguration.isEnabled()) {
/* 182 */       log.info("Ignored statically defined but disabled pipeline [{}]", pipelineConfiguration.getId());
/* 183 */       return;
/*     */     }
/*     */     
/* 186 */     int instances = pipelineConfiguration.getInstances();
/* 187 */     String originalPipelineId = String.valueOf(pipelineConfiguration.getId());
/*     */     
/* 189 */     for (int i = 0; i < instances; i++) {
/* 190 */       String newPipelineId = originalPipelineId;
/* 191 */       if (instances > 1)
/*     */       {
/* 193 */         newPipelineId = originalPipelineId + "-" + i;
/*     */       }
/*     */       
/* 196 */       Pipeline pipeline = (Pipeline)get(newPipelineId);
/* 197 */       if (pipeline == null) {
/* 198 */         pipelineConfiguration.setId(newPipelineId);
/* 199 */         createAddStartAndExecute(pipelineConfiguration);
/* 200 */         log.info("Loaded and started statically defined pipeline [{}]", newPipelineId);
/* 201 */       } else if (pipeline.getState() == Pipeline.State.CREATED) {
/* 202 */         pipeline.start();
/* 203 */         log.info("Started previously created pipeline [{}]", newPipelineId);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Pipeline createAddAndStart(PipelineConfiguration pipelineConfiguration)
/*     */   {
/* 215 */     Pipeline pipeline = createPipeline(pipelineConfiguration);
/* 216 */     add(pipeline);
/* 217 */     pipeline.start();
/* 218 */     return pipeline;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Future<Void> createAddStartAndExecute(PipelineConfiguration pipelineConfiguration)
/*     */   {
/* 228 */     Pipeline pipeline = createAddAndStart(pipelineConfiguration);
/* 229 */     return executePipeline(pipeline);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void stop()
/*     */   {
/* 239 */     Collection<? extends Pipeline<?>> all = getAll();
/* 240 */     if (all.size() > 0) {
/* 241 */       log.info("Attempting to stop pipelines");
/* 242 */       LifecycleHelper.stopAll(all);
/*     */     }
/* 244 */     ConcurrencyHelper.stop(this.pipelineExecutorService, log);
/* 245 */     log.info("Pipelines have stopped");
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/Pipelines.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */