/*     */ package com.appdynamics.common.util.grok;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSortedMap;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ @ThreadSafe
/*     */ public class Grok
/*     */ {
/*  40 */   private static final Logger log = LoggerFactory.getLogger(Grok.class);
/*     */   
/*     */ 
/*     */   final Map<String, GrokLineAst> namesAndGrokAsts;
/*     */   
/*     */   final Map<String, GrokLine> namesAndGrokLines;
/*     */   
/*     */   final Map<String, String> javaAndGrokAliases;
/*     */   
/*     */ 
/*     */   public Grok(List<Path> grokFilePaths)
/*     */     throws IOException
/*     */   {
/*  53 */     TreeMap<String, GrokLineAst> namesAndGrokLines = new TreeMap();
/*     */     
/*  55 */     for (Path grokFilePath : grokFilePaths) {
/*  56 */       log.debug("Attempting to parse [{}]", grokFilePath);
/*  57 */       int before = namesAndGrokLines.size();
/*  58 */       FileInputStream fis = new FileInputStream(grokFilePath.toFile());Throwable localThrowable2 = null;
/*  59 */       try { GrokParser.parseLines(fis, namesAndGrokLines);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/*  58 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */       } finally {
/*  60 */         if (fis != null) if (localThrowable2 != null) try { fis.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else fis.close(); }
/*  61 */       int after = namesAndGrokLines.size();
/*  62 */       log.debug("Parsed [{}] and [{}] new patterns were detected", grokFilePath, Integer.valueOf(after - before));
/*     */     }
/*  64 */     this.namesAndGrokAsts = ImmutableSortedMap.copyOf(namesAndGrokLines);
/*     */     
/*  66 */     this.namesAndGrokLines = resolveAndTransform(this.namesAndGrokAsts);
/*  67 */     this.javaAndGrokAliases = extractJavaAndGrokAliases(this.namesAndGrokAsts);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Grok(InputStream... utf8GrokStreams)
/*     */     throws IOException
/*     */   {
/*  77 */     TreeMap<String, GrokLineAst> namesAndGrokLines = new TreeMap();
/*     */     
/*  79 */     int i = 0;
/*  80 */     for (InputStream inputStream : utf8GrokStreams) {
/*  81 */       log.debug("Attempting to parse stream [{}]", Integer.valueOf(i));
/*  82 */       int before = namesAndGrokLines.size();
/*     */       try {
/*  84 */         GrokParser.parseLines(inputStream, namesAndGrokLines);
/*     */       } finally {
/*  86 */         IOUtils.closeQuietly(inputStream);
/*     */       }
/*     */       
/*  89 */       int after = namesAndGrokLines.size();
/*  90 */       log.debug("Parsed stream [{}] and [{}] new patterns were detected", Integer.valueOf(i), Integer.valueOf(after - before));
/*  91 */       i++;
/*     */     }
/*  93 */     this.namesAndGrokAsts = ImmutableSortedMap.copyOf(namesAndGrokLines);
/*  94 */     this.namesAndGrokLines = resolveAndTransform(this.namesAndGrokAsts);
/*  95 */     this.javaAndGrokAliases = extractJavaAndGrokAliases(this.namesAndGrokAsts);
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
/*     */   private static Map<String, GrokLine> resolveAndTransform(Map<String, GrokLineAst> asts)
/*     */   {
/* 110 */     TreeMap<String, GrokLine> namesAndPatterns = new TreeMap();
/*     */     
/*     */ 
/* 113 */     for (Map.Entry<String, GrokLineAst> entry : asts.entrySet()) {
/* 114 */       String patternName = (String)entry.getKey();
/*     */       try {
/* 116 */         GrokLine grokLine = ((GrokLineAst)entry.getValue()).resolve(asts);
/* 117 */         log.trace("Resolved pattern [{}] to [{}]", patternName, grokLine);
/* 118 */         namesAndPatterns.put(patternName, grokLine);
/*     */       } catch (RuntimeException e) {
/* 120 */         throw new ConfigurationException("Error occurred while parsing pattern [" + patternName + "]", e);
/*     */       }
/*     */     }
/*     */     
/* 124 */     return ImmutableSortedMap.copyOf(namesAndPatterns);
/*     */   }
/*     */   
/*     */   private static Map<String, String> extractJavaAndGrokAliases(Map<String, GrokLineAst> grokLines) {
/* 128 */     HashMap<String, String> javaAliasesAndGrokAliases = new HashMap(grokLines.size());
/*     */     
/* 130 */     for (Map.Entry<String, GrokLineAst> entry : grokLines.entrySet()) {
/* 131 */       GrokLineAst grokLineAst = (GrokLineAst)entry.getValue();
/* 132 */       for (GrokLineAst.GrokLineAstItem grokLineAstItem : grokLineAst.getValues()) {
/* 133 */         if ((grokLineAstItem instanceof GrokLineAst.PatternRef)) {
/* 134 */           Optional<String> alias = ((GrokLineAst.PatternRef)grokLineAstItem).getAlias();
/* 135 */           if (alias.isPresent()) {
/* 136 */             Optional<String> javaAlias = ((GrokLineAst.PatternRef)grokLineAstItem).getAliasJavaCompliant();
/* 137 */             javaAliasesAndGrokAliases.put(javaAlias.get(), alias.get());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 143 */     return ImmutableMap.copyOf(javaAliasesAndGrokAliases);
/*     */   }
/*     */   
/*     */   public Set<String> getPatternNames() {
/* 147 */     return this.namesAndGrokLines.keySet();
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
/*     */   public GrokPattern compile(String definition)
/*     */   {
/* 160 */     GrokLineAst grokLineAst = GrokParser.parseDefinition(definition, this.namesAndGrokLines.keySet());
/* 161 */     if (grokLineAst == null) {
/* 162 */       throw new ConfigurationException("The pattern input [" + definition + "] is not a valid Grok definition");
/*     */     }
/*     */     
/* 165 */     GrokLine grokLine = grokLineAst.resolve(this.namesAndGrokLines, this.namesAndGrokAsts);
/* 166 */     String tempRandomKey = "tempKey_" + System.nanoTime();
/* 167 */     Map<String, String> javaAndGrokAliasesInDefinition = extractJavaAndGrokAliases(Collections.singletonMap(tempRandomKey, grokLineAst));
/*     */     
/*     */ 
/* 170 */     return grokLine.compile(this.javaAndGrokAliases, javaAndGrokAliasesInDefinition);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/grok/Grok.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */