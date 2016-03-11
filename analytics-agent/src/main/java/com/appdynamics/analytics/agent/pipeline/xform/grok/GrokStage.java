/*     */ package com.appdynamics.analytics.agent.pipeline.xform.grok;
/*     */ 
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*     */ import com.appdynamics.common.util.grok.GrokPattern;
/*     */ import com.appdynamics.common.util.grok.GrokValueType;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.lang.BooleanUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GrokStage
/*     */   extends AbstractPipelineStage<Map<String, Object>, Map<String, Object>>
/*     */ {
/*  40 */   private static final Logger log = LoggerFactory.getLogger(GrokStage.class);
/*     */   
/*     */ 
/*     */   final String source;
/*     */   
/*     */ 
/*     */   final GrokPattern[] grokPatterns;
/*     */   
/*     */ 
/*     */   final Matcher[] matchers;
/*     */   
/*     */ 
/*     */   GrokStage(PipelineStageParameters<Map<String, Object>> parameters, String source, ArrayList<GrokPattern> patterns)
/*     */   {
/*  54 */     super(parameters);
/*  55 */     this.source = source;
/*  56 */     int numberOfPatterns = patterns.size();
/*  57 */     this.grokPatterns = new GrokPattern[numberOfPatterns];
/*  58 */     this.matchers = new Matcher[numberOfPatterns];
/*     */     
/*  60 */     for (int i = 0; i < numberOfPatterns; i++) {
/*  61 */       GrokPattern pattern = (GrokPattern)patterns.get(i);
/*  62 */       Matcher reusableMatcher = pattern.getPattern().matcher("");
/*  63 */       Preconditions.checkArgument(pattern.getAliases().length > 0, "The Grok pattern [%s] is not valid as it does not have any alias definitions. Without aliases, no sub-strings can be extracted into fields", new Object[] { pattern });
/*     */       
/*     */ 
/*  66 */       this.grokPatterns[i] = pattern;
/*  67 */       this.matchers[i] = reusableMatcher;
/*     */     }
/*     */   }
/*     */   
/*     */   public void process(Map<String, Object> input)
/*     */   {
/*  73 */     Object o = input.get(this.source);
/*  74 */     if (o != null) {
/*  75 */       int matchCount = matchAndExtract(input, o);
/*  76 */       if (log.isTraceEnabled()) {
/*  77 */         log.trace("Found [{}] Capture Groups. End result is [{}]", Integer.valueOf(matchCount), input);
/*     */       }
/*     */     }
/*     */     
/*  81 */     invokeNext(input);
/*     */   }
/*     */   
/*     */   private int matchAndExtract(Map<String, Object> input, Object o) {
/*  85 */     String s = o.toString();
/*  86 */     int matchCount = 0;
/*     */     
/*  88 */     for (int i = 0; i < this.grokPatterns.length; i++) {
/*  89 */       GrokPattern pattern = this.grokPatterns[i];
/*  90 */       Matcher reusableMatcher = this.matchers[i];
/*     */       
/*  92 */       reusableMatcher.reset(s);
/*  93 */       int[] captureGroupIds = pattern.getAliasGroupIds();
/*  94 */       String[] captureGroupNames = pattern.getAliases();
/*  95 */       GrokValueType[] valueTypes = pattern.getValueTypes();
/*     */       
/*     */ 
/*  98 */       while (reusableMatcher.find()) {
/*  99 */         for (int j = 0; j < captureGroupIds.length; j++) {
/* 100 */           int captureGroupId = captureGroupIds[j];
/*     */           
/*     */ 
/* 103 */           String captureGroupValue = reusableMatcher.group(captureGroupId);
/* 104 */           if (captureGroupValue != null)
/*     */           {
/* 106 */             GrokValueType valueType = valueTypes[j];
/* 107 */             switch (valueType) {
/*     */             case BOOLEAN: 
/* 109 */               Boolean b = BooleanUtils.toBooleanObject(captureGroupValue);
/* 110 */               if (b != null) {
/* 111 */                 input.put(captureGroupNames[j], b);
/*     */               } else {
/* 113 */                 log.warn("The Grok alias [{}] has been marked to be parsed as [{}] but parsing failed for [{}]", new Object[] { captureGroupNames[j], valueType, captureGroupValue });
/*     */               }
/*     */               
/*     */ 
/* 117 */               break;
/*     */             case NUMBER: 
/* 119 */               Double d = Doubles.tryParse(captureGroupValue);
/* 120 */               if (d != null) {
/* 121 */                 input.put(captureGroupNames[j], d);
/*     */               } else {
/* 123 */                 log.warn("The Grok alias [{}] has been marked to be parsed as [{}] but parsing failed for [{}]", new Object[] { captureGroupNames[j], valueType, captureGroupValue });
/*     */               }
/*     */               
/*     */ 
/* 127 */               break;
/*     */             case STRING: 
/*     */             default: 
/* 130 */               input.put(captureGroupNames[j], captureGroupValue);
/*     */             }
/* 132 */             matchCount++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 138 */     return matchCount;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/grok/GrokStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */