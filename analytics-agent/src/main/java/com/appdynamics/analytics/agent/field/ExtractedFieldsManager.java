/*     */ package com.appdynamics.analytics.agent.field;
/*     */ 
/*     */ import com.appdynamics.analytics.pipeline.framework.PipelineConfiguration;
/*     */ import com.appdynamics.analytics.pipeline.framework.PipelineStageConfiguration;
/*     */ import com.appdynamics.analytics.shared.rest.client.eventservice.ExtractedFieldsClient;
/*     */ import com.appdynamics.analytics.shared.rest.client.eventservice.ExtractedFieldsClient.ExtractedFieldDefinition;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.concurrent.NotThreadSafe;
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
/*     */ @NotThreadSafe
/*     */ public class ExtractedFieldsManager
/*     */ {
/*  36 */   private static final Logger log = LoggerFactory.getLogger(ExtractedFieldsManager.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private final ExtractedFieldsClient extractedFieldsClient;
/*     */   
/*     */ 
/*     */ 
/*     */   private final String accountName;
/*     */   
/*     */ 
/*     */ 
/*     */   private final String accessKey;
/*     */   
/*     */ 
/*     */   private final String eventType;
/*     */   
/*     */ 
/*     */ 
/*     */   public ExtractedFieldsManager(ExtractedFieldsClient extractedFieldsClient, String accountName, String accessKey, String eventType)
/*     */   {
/*  57 */     this.extractedFieldsClient = extractedFieldsClient;
/*  58 */     this.accountName = accountName;
/*  59 */     this.accessKey = accessKey;
/*  60 */     this.eventType = eventType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getExtractedFieldPatterns(String sourceType)
/*     */   {
/*  70 */     if (Strings.isNullOrEmpty(sourceType)) {
/*  71 */       return Collections.emptySet();
/*     */     }
/*     */     
/*  74 */     SetMultimap<String, String> extractedFieldPatterns = getExtractedFieldPatterns(Lists.newArrayList(new String[] { sourceType }));
/*  75 */     return extractedFieldPatterns.get(sourceType);
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
/*     */   public SetMultimap<String, String> getExtractedFieldPatterns(List<String> sourceTypes)
/*     */   {
/*  88 */     SetMultimap<String, String> extractedFieldPatterns = HashMultimap.create();
/*     */     
/*  90 */     List<ExtractedFieldsClient.ExtractedFieldDefinition> extractedFields = this.extractedFieldsClient.getExtractedFields(this.accountName, this.accessKey, this.eventType, sourceTypes);
/*     */     
/*     */ 
/*  93 */     if ((extractedFields == null) || (extractedFields.isEmpty())) {
/*  94 */       return extractedFieldPatterns;
/*     */     }
/*     */     
/*  97 */     for (ExtractedFieldsClient.ExtractedFieldDefinition extractedField : extractedFields) {
/*  98 */       extractedFieldPatterns.put(extractedField.getSourceType(), extractedField.getPattern());
/*     */     }
/*     */     
/* 101 */     return extractedFieldPatterns;
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
/*     */   public void addExtractedFieldPatterns(PipelineConfiguration pipelineConfiguration, Set<String> extractedFieldPatterns)
/*     */   {
/* 118 */     if ((extractedFieldPatterns == null) || (extractedFieldPatterns.isEmpty())) {
/* 119 */       return;
/*     */     }
/*     */     
/* 122 */     List<String> grokPatterns = getGrokPatterns(pipelineConfiguration);
/*     */     
/*     */ 
/* 125 */     if (grokPatterns == null)
/*     */     {
/* 127 */       createGrokStageAndAddExtractedFieldPatterns(pipelineConfiguration, extractedFieldPatterns);
/*     */     }
/*     */     else {
/* 130 */       grokPatterns.addAll(extractedFieldPatterns);
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
/*     */   List<String> getGrokPatterns(PipelineConfiguration pipelineConfiguration)
/*     */   {
/* 143 */     for (PipelineStageConfiguration stage : pipelineConfiguration.getStages()) {
/* 144 */       if ("xform:grok".equals(stage.getUri())) {
/* 145 */         Map<String, Object> stageProperties = stage.getProperties();
/* 146 */         if (stageProperties == null) {
/* 147 */           stageProperties = new HashMap();
/*     */         }
/*     */         
/* 150 */         if (!stageProperties.containsKey("patterns")) {
/* 151 */           stageProperties.put("patterns", Lists.newArrayList());
/*     */         }
/*     */         
/* 154 */         return (List)stageProperties.get("patterns");
/*     */       }
/*     */     }
/*     */     
/* 158 */     return null;
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
/*     */   void createGrokStageAndAddExtractedFieldPatterns(PipelineConfiguration pipelineConfiguration, Set<String> extractedFieldPatterns)
/*     */   {
/* 175 */     List<PipelineStageConfiguration> stageConfigurations = pipelineConfiguration.getStages();
/*     */     
/*     */ 
/* 178 */     int indexOfAddFieldScriptsStage = -1;
/* 179 */     Iterator<PipelineStageConfiguration> stageConfigurationsIterator = stageConfigurations.iterator();
/* 180 */     for (int i = 0; stageConfigurationsIterator.hasNext(); i++) {
/* 181 */       PipelineStageConfiguration stageConfiguration = (PipelineStageConfiguration)stageConfigurationsIterator.next();
/* 182 */       if ("xform:field:add:script".equals(stageConfiguration.getUri())) {
/* 183 */         indexOfAddFieldScriptsStage = i;
/* 184 */         break;
/*     */       }
/*     */     }
/*     */     
/* 188 */     if (indexOfAddFieldScriptsStage == -1) {
/* 189 */       throw new IllegalStateException(String.format("Could not find pipeline stage [%s] in pipeline configuration with id [%s]", new Object[] { "xform:field:add:script", pipelineConfiguration.getId() }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 195 */     int indexOfGrokStage = indexOfAddFieldScriptsStage + 1;
/*     */     
/*     */ 
/* 198 */     Map<String, Object> grokStageProperties = new HashMap(1);
/* 199 */     grokStageProperties.put("patterns", Lists.newArrayList(extractedFieldPatterns));
/*     */     
/* 201 */     PipelineStageConfiguration grokStageConfiguration = new PipelineStageConfiguration();
/* 202 */     grokStageConfiguration.setUri("xform:grok");
/* 203 */     grokStageConfiguration.setProperties(grokStageProperties);
/* 204 */     stageConfigurations.add(indexOfGrokStage, grokStageConfiguration);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/field/ExtractedFieldsManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */