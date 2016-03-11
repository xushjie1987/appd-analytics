/*     */ package com.appdynamics.common.util.health;
/*     */ 
/*     */ import com.appdynamics.common.framework.AppConfiguration;
/*     */ import com.appdynamics.common.framework.AppInfo;
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.configuration.Parameters;
/*     */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*     */ import com.appdynamics.common.util.log.LogLevel;
/*     */ import com.codahale.metrics.health.HealthCheck.Result;
/*     */ import com.codahale.metrics.health.HealthCheckRegistry;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.google.inject.Inject;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.AbstractMap.SimpleImmutableEntry;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.SortedMap;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
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
/*     */ public class HealthReporterModule
/*     */   extends Module<HealthReporterModuleConfiguration>
/*     */ {
/*  48 */   private static final Logger log = LoggerFactory.getLogger(HealthReporterModule.class);
/*     */   public static final String REPORT_HEALTHY = "Report healthy";
/*     */   public static final String REPORT_UNHEALTHY = "Report unhealthy";
/*     */   public static final String MBEAN_NAME = "Application:name=Health report";
/*     */   private final ExecutorService executorService;
/*     */   private volatile HealthReport healthReport;
/*     */   private volatile Future<?> loggerTask;
/*     */   
/*     */   public HealthReporterModule()
/*     */   {
/*  58 */     ThreadFactory tf = new ThreadFactoryBuilder().setDaemon(true).setPriority(1).setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void uncaughtException(Thread t, Throwable e) {
/*  65 */         HealthReporterModule.log.error("Error occurred while reporting health on thread [" + t.getName() + "]. Health reports will not function" + " any more", e); } }).setNameFormat("health-report-thread-%d").build();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */     this.executorService = Executors.newSingleThreadExecutor(tf);
/*     */   }
/*     */   
/*     */   @Inject
/*     */   void start(AppConfiguration configuration, AppInfo appInfo, Environment environment) {
/*  77 */     this.healthReport = new HealthReport(configuration.getName(), appInfo.getBuild(), true);
/*     */     try {
/*  79 */       MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/*  80 */       mbs.registerMBean(this.healthReport, new ObjectName("Application:name=Health report"));
/*     */     } catch (Exception e) {
/*  82 */       log.warn("Error occurred while registering MBean", e);
/*     */     }
/*     */     
/*  85 */     HealthCheckRegistry registry = environment.healthChecks();
/*  86 */     TimeUnit waitTimeUnit = ((HealthReporterModuleConfiguration)getConfiguration()).getSchedule().getTimeUnit();
/*  87 */     final long waitTimeMillis = waitTimeUnit.toMillis(((HealthReporterModuleConfiguration)getConfiguration()).getSchedule().getTime());
/*  88 */     this.loggerTask = this.executorService.submit(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         for (;;) {
/*     */           try {
/*  94 */             Thread.sleep(waitTimeMillis);
/*     */           } catch (InterruptedException e) {
/*  96 */             HealthReporterModule.log.debug("Task has been interrupted and will exit", e);
/*  97 */             Thread.currentThread().interrupt();
/*  98 */             return;
/*     */           }
/*     */           
/* 101 */           Map.Entry<HealthCheck.Result, String> entry = HealthReporterModule.this.runCheckAndLog(this.val$registry);
/*     */           
/* 103 */           HealthReporterModule.this.healthReport.setHealthy(((HealthCheck.Result)entry.getKey()).isHealthy());
/*     */         }
/*     */         
/*     */       }
/* 107 */     });
/* 108 */     log.info("Started and is scheduled to run every [{} {}]", Long.valueOf(((HealthReporterModuleConfiguration)getConfiguration()).getSchedule().getTime()), waitTimeUnit);
/*     */   }
/*     */   
/*     */   Map.Entry<HealthCheck.Result, String> runCheckAndLog(HealthCheckRegistry registry)
/*     */   {
/* 113 */     log.debug("Starting scheduled health check");
/*     */     
/* 115 */     SortedMap<String, HealthCheck.Result> results = registry.runHealthChecks();
/* 116 */     StringBuilder sb = new StringBuilder();
/* 117 */     int i = 0;
/* 118 */     boolean healthy = true;
/* 119 */     for (Map.Entry<String, HealthCheck.Result> entry : results.entrySet()) {
/* 120 */       sb.append(i > 0 ? "\n\n" : "");
/*     */       
/* 122 */       sb.append((String)entry.getKey()).append(": ");
/* 123 */       HealthCheck.Result result = (HealthCheck.Result)entry.getValue();
/* 124 */       String msg = result.getMessage();
/* 125 */       sb.append(result.isHealthy() ? "(healthy)" : "(unhealthy)").append(' ').append(Parameters.asString(msg, ""));
/*     */       
/* 127 */       Throwable t = result.getError();
/* 128 */       if (t != null) {
/* 129 */         sb.append("\n").append(Throwables.getStackTraceAsString(t));
/*     */       }
/*     */       
/* 132 */       healthy = (healthy) && (result.isHealthy());
/* 133 */       i++;
/*     */     }
/*     */     
/* 136 */     String message = sb.toString();
/* 137 */     if (healthy) {
/* 138 */       LogLevel logLevel = ((HealthReporterModuleConfiguration)getConfiguration()).getHealthyLogLevel();
/* 139 */       logLevel.write(log, "Report healthy [\n" + message + "\n]");
/*     */     } else {
/* 141 */       log.warn("Report unhealthy [\n{}\n]", message);
/*     */     }
/* 143 */     return new AbstractMap.SimpleImmutableEntry(healthy ? HealthCheck.Result.healthy() : HealthCheck.Result.unhealthy(""), message);
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   void stop() {
/* 148 */     if (this.loggerTask != null) {
/*     */       try {
/* 150 */         ConcurrencyHelper.getOrCancel(this.loggerTask, 1, log);
/*     */       } catch (RuntimeException e) {
/* 152 */         log.trace("Task was stopped", e);
/*     */       }
/*     */     }
/* 155 */     ConcurrencyHelper.stop(this.executorService, log);
/* 156 */     log.info("Stopped");
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/health/HealthReporterModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */