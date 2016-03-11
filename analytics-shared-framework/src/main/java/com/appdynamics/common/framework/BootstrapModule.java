/*     */ package com.appdynamics.common.framework;
/*     */ 
/*     */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*     */ import com.appdynamics.common.util.health.SimpleHealthCheck;
/*     */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*     */ import com.codahale.metrics.MetricRegistry;
/*     */ import com.codahale.metrics.health.HealthCheck.Result;
/*     */ import com.google.inject.AbstractModule;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Singleton;
/*     */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*     */ import com.google.inject.binder.ScopedBindingBuilder;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BootstrapModule
/*     */   extends AbstractModule
/*     */ {
/*  33 */   private static final Logger log = LoggerFactory.getLogger(BootstrapModule.class);
/*     */   final AppConfiguration conf;
/*     */   final Environment env;
/*     */   final AppLifecycle appLifecycle;
/*     */   final String buildInfo;
/*     */   final ConsolidatedHealthCheck rootHealthCheck;
/*     */   
/*     */   BootstrapModule(AppConfiguration conf, Environment env, AppLifecycle appLifecycle)
/*     */   {
/*  42 */     this.conf = conf;
/*  43 */     this.env = env;
/*  44 */     this.appLifecycle = appLifecycle;
/*  45 */     this.buildInfo = findBuildInfo();
/*  46 */     log.info("Build information [{}]", this.buildInfo);
/*  47 */     this.rootHealthCheck = new ConsolidatedHealthCheck(conf.getName(), env.healthChecks());
/*     */   }
/*     */   
/*     */   private String findBuildInfo() {
/*  51 */     String buildInfo = "Not available";
/*  52 */     URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
/*  53 */     JarFile jar = null;
/*     */     try {
/*  55 */       File sourceLocation = new File(url.toURI());
/*  56 */       if (sourceLocation.isDirectory()) {
/*  57 */         log.info("Program is not running from a JAR file [{}]. Unable to get the build version from the manifest", sourceLocation.getAbsolutePath());
/*     */       }
/*     */       else {
/*  60 */         jar = new JarFile(sourceLocation);
/*  61 */         Manifest manifest = jar.getManifest();
/*  62 */         Attributes attributes = manifest.getMainAttributes();
/*  63 */         buildInfo = attributes.getValue("Implementation-Version");
/*  64 */         jar.close();
/*     */       }
/*     */     } catch (URISyntaxException|IOException e) {
/*  67 */       log.warn("Unable to get the build version from the manifest", e);
/*     */     }
/*  69 */     return buildInfo;
/*     */   }
/*     */   
/*     */   protected void configure()
/*     */   {
/*  74 */     bind(Environment.class).toInstance(this.env);
/*  75 */     bind(AppConfiguration.class).toInstance(this.conf);
/*  76 */     bind(AppInfo.class).toInstance(new AppInfo(this.buildInfo));
/*     */     
/*  78 */     this.appLifecycle.bind(binder());
/*     */     
/*  80 */     bind(LifecycleInjector.class).to(DefaultLifecycleInjector.class).in(Singleton.class);
/*     */     
/*  82 */     bind(ConsolidatedHealthCheck.class).toInstance(this.rootHealthCheck);
/*  83 */     this.rootHealthCheck.register(new SimpleHealthCheck("Build information")
/*     */     {
/*     */       public HealthCheck.Result check() {
/*  86 */         return HealthCheck.Result.healthy(BootstrapModule.this.buildInfo);
/*     */       }
/*     */       
/*  89 */     });
/*  90 */     bind(MetricRegistry.class).toInstance(this.env.metrics());
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   void onStop() {
/*  95 */     this.rootHealthCheck.discard();
/*     */   }
/*     */   
/*     */   static class DefaultLifecycleInjector implements LifecycleInjector {
/*     */     final Injector injector;
/*     */     
/*     */     @Inject
/*     */     DefaultLifecycleInjector(Injector injector) {
/* 103 */       this.injector = injector;
/*     */     }
/*     */     
/*     */     public <T> T inject(T annotatedTarget)
/*     */     {
/* 108 */       this.injector.injectMembers(annotatedTarget);
/* 109 */       return annotatedTarget;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/BootstrapModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */