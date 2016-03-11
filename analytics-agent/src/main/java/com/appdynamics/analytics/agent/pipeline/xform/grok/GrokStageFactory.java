/*    */ package com.appdynamics.analytics.agent.pipeline.xform.grok;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.appdynamics.common.io.IoHelper;
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.grok.Grok;
/*    */ import com.appdynamics.common.util.grok.GrokPattern;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.inject.Inject;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.FileSystem;
/*    */ import java.nio.file.FileSystems;
/*    */ import java.nio.file.InvalidPathException;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.PathMatcher;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class GrokStageFactory
/*    */   extends AbstractPipelineStageFactory<Map<String, Object>, Map<String, Object>, GrokStageFactoryConfiguration, GrokStageConfiguration>
/*    */ {
/* 37 */   private static final Logger log = LoggerFactory.getLogger(GrokStageFactory.class);
/*    */   
/*    */   volatile Grok grok;
/*    */   
/*    */   public void configure()
/*    */   {
/*    */     try
/*    */     {
/* 45 */       GrokStageFactoryConfiguration cfg = (GrokStageFactoryConfiguration)getConfiguration();
/* 46 */       Path path = Paths.get(cfg.getPath(), new String[0]);
/* 47 */       PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + cfg.getNameGlob());
/*    */       
/*    */ 
/* 50 */       List<Path> grokFilePaths = IoHelper.findMatchingPaths(path, pathMatcher);
/*    */       
/*    */ 
/* 53 */       this.grok = new Grok(grokFilePaths);
/*    */     } catch (InvalidPathException|IOException e) {
/* 55 */       throw new ConfigurationException(e);
/*    */     }
/*    */     
/* 58 */     super.configure();
/*    */   }
/*    */   
/*    */   @Inject
/*    */   public void start(Environment environment) {
/* 63 */     GrokStageResource grokToolResource = new GrokStageResource(this);
/* 64 */     environment.jersey().register(grokToolResource);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public GrokStage create(PipelineStageParameters<Map<String, Object>> parameters)
/*    */   {
/* 71 */     GrokStageConfiguration stageConfiguration = (GrokStageConfiguration)extract(parameters);
/* 72 */     String source = stageConfiguration.mergeSourceAndValidate(((GrokStageFactoryConfiguration)getConfiguration()).getSource());
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 77 */     String configPattern = stageConfiguration.getPattern();
/* 78 */     List<String> configPatterns = stageConfiguration.getPatterns();
/* 79 */     if (configPatterns == null)
/*    */     {
/* 81 */       configPatterns = Lists.newArrayList(new String[] { configPattern });
/* 82 */     } else if (configPattern != null) {
/* 83 */       configPatterns.add(configPattern);
/*    */     }
/*    */     
/* 86 */     ArrayList<GrokPattern> grokPatterns = new ArrayList(configPatterns.size());
/* 87 */     for (String pattern : configPatterns) {
/* 88 */       grokPatterns.add(this.grok.compile(pattern));
/*    */     }
/*    */     
/* 91 */     GrokStage stage = new GrokStage(parameters, source, grokPatterns);
/* 92 */     log.trace("Pipeline stage [{} {}] has been configured to use [{}] as source to extract fields using the patterns {}", new Object[] { parameters.getPipelineId(), getUri(), source, configPatterns });
/*    */     
/* 94 */     return stage;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/grok/GrokStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */