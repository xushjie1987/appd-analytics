/*     */ package com.appdynamics.common.util.configuration;
/*     */ 
/*     */ import com.appdynamics.common.framework.util.FrameworkHelper;
/*     */ import com.appdynamics.common.util.var.ChainedVariableResolver;
/*     */ import com.appdynamics.common.util.var.MapBasedVariableResolver;
/*     */ import com.appdynamics.common.util.var.VariableResolver;
/*     */ import com.appdynamics.common.util.var.Variables;
/*     */ import com.github.mustachejava.DefaultMustacheFactory;
/*     */ import com.github.mustachejava.Mustache;
/*     */ import com.github.mustachejava.MustacheFactory;
/*     */ import com.google.common.base.Charsets;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.annotation.concurrent.ThreadSafe;
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
/*     */ @ThreadSafe
/*     */ public class Templates
/*     */ {
/*  41 */   private static final Logger log = LoggerFactory.getLogger(Templates.class);
/*     */   
/*     */   final MapBasedVariableResolver propertiesResolver;
/*     */   final Map<String, String> templates;
/*     */   final MustacheFactory mustacheFactory;
/*     */   final ConcurrentMap<String, Mustache> compiledTemplates;
/*     */   
/*     */   Templates(Map<String, String> templates)
/*     */   {
/*  50 */     this(templates, FrameworkHelper.getProperties());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Templates(Map<String, String> templates, Properties properties)
/*     */   {
/*  59 */     this.propertiesResolver = Variables.loadVariables(properties);
/*  60 */     this.templates = templates;
/*  61 */     this.mustacheFactory = new DefaultMustacheFactory();
/*  62 */     this.compiledTemplates = new ConcurrentHashMap();
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
/*     */   public <T> T read(String templateName, Map<String, Object> variables, Class<T> readAsType)
/*     */   {
/*  77 */     String rawWithVariables = (String)this.templates.get(templateName);
/*  78 */     if (rawWithVariables == null) {
/*  79 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  83 */     Mustache mustache = (Mustache)this.compiledTemplates.get(templateName);
/*  84 */     if (mustache == null) {
/*  85 */       mustache = this.mustacheFactory.compile(new StringReader(rawWithVariables), templateName);
/*     */       
/*  87 */       Mustache oldMustache = (Mustache)this.compiledTemplates.putIfAbsent(templateName, mustache);
/*  88 */       mustache = oldMustache == null ? mustache : oldMustache;
/*     */     }
/*     */     try {
/*  91 */       StringWriter writer = new StringWriter();Throwable localThrowable2 = null;
/*  92 */       try { mustache.execute(writer, variables).flush();
/*  93 */         rawWithVariables = writer.toString();
/*  94 */         log.debug("The template [{}] has been expanded to [\n{}]", templateName, rawWithVariables);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/*  91 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */       }
/*     */       finally
/*     */       {
/*  95 */         if (writer != null) if (localThrowable2 != null) try { writer.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else writer.close();
/*  96 */       } } catch (IOException e) { throw new RuntimeException(e);
/*     */     }
/*     */     
/*     */ 
/* 100 */     ChainedVariableResolver chainResolver = Variables.newResolverChainPlusCustomResolver(new VariableResolver[] { this.propertiesResolver, new MapBasedVariableResolver(variables) });
/*     */     
/* 102 */     String rawTemplate = Variables.resolve(rawWithVariables, chainResolver);
/*     */     
/*     */ 
/* 105 */     log.info("The following template will be used to create an instance of [{}] [\n{}]", readAsType.getName(), rawTemplate);
/*     */     try
/*     */     {
/* 108 */       return (T)Reader.readFrom(readAsType, new ByteArrayInputStream(rawTemplate.getBytes(Charsets.UTF_8)));
/*     */     } catch (IOException e) {
/* 110 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/Templates.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */