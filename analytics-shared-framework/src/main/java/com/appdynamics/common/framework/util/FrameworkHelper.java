/*     */ package com.appdynamics.common.framework.util;
/*     */ 
/*     */ import com.appdynamics.common.framework.AppConfiguration;
/*     */ import com.appdynamics.common.util.var.ChainedVariableResolver;
/*     */ import com.appdynamics.common.util.var.MapBasedVariableResolver;
/*     */ import com.appdynamics.common.util.var.SystemVariableResolver;
/*     */ import com.appdynamics.common.util.var.VariableResolver;
/*     */ import com.appdynamics.common.util.var.Variables;
/*     */ import com.codahale.metrics.JmxReporter;
/*     */ import com.codahale.metrics.JmxReporter.Builder;
/*     */ import com.codahale.metrics.Metric;
/*     */ import com.codahale.metrics.MetricFilter;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.primitives.Ints;
/*     */ import io.dropwizard.lifecycle.ServerLifecycleListener;
/*     */ import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.eclipse.jetty.server.Connector;
/*     */ import org.eclipse.jetty.server.Server;
/*     */ import org.eclipse.jetty.server.ServerConnector;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FrameworkHelper
/*     */ {
/*  49 */   private static final Logger log = LoggerFactory.getLogger(FrameworkHelper.class);
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
/*     */   public static Properties getProperties()
/*     */   {
/*  65 */     String variableValuesPath = getPropertiesFileLocation();
/*  66 */     Properties variableValues; if (variableValuesPath == null) {
/*  67 */       Properties variableValues = new Properties();
/*  68 */       log.warn("No properties file is available under the key [{}]", "ad.prop.file.path");
/*     */     } else {
/*  70 */       File variableValuesFile = new File(variableValuesPath);
/*  71 */       Preconditions.checkArgument(variableValuesFile.exists(), "The properties file that is supposed to be located at [" + variableValuesFile.getAbsolutePath() + "] does not exist");
/*     */       
/*  73 */       variableValues = new Properties();
/*  74 */       try { FileInputStream fis = new FileInputStream(variableValuesFile);Throwable localThrowable2 = null;
/*  75 */         try { variableValues.load(fis);
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/*  74 */           localThrowable2 = localThrowable1;throw localThrowable1;
/*     */         } finally {
/*  76 */           if (fis != null) if (localThrowable2 != null) try { fis.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else fis.close();
/*  77 */         } } catch (IOException e) { Throwables.propagate(e);
/*     */       }
/*  79 */       log.debug("Properties file [{}] will be used to resolve variables", variableValuesFile.getAbsolutePath());
/*     */     }
/*     */     
/*  82 */     return variableValues;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String resolveProperty(String propertyName, String defaultValue)
/*     */   {
/*  93 */     String configProperty = "${" + propertyName + "}";
/*  94 */     Properties properties = getProperties();
/*  95 */     String valueFromProperties = properties.getProperty(propertyName, defaultValue);
/*  96 */     Map<String, String> mapAsProperties = Collections.singletonMap(propertyName, valueFromProperties);
/*  97 */     ChainedVariableResolver resolver = Variables.newResolverChainPlusCustomResolver(new VariableResolver[] { new MapBasedVariableResolver(mapAsProperties) });
/*     */     
/*     */ 
/* 100 */     return Variables.resolve(configProperty, resolver);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getPropertiesFileLocation()
/*     */   {
/* 107 */     return System.getProperty("ad.prop.file.path");
/*     */   }
/*     */   
/*     */   public static void createPidFile(String fileNamePrefix) {
/* 111 */     long pid = SystemVariableResolver.getProcessId();
/* 112 */     String appHome = System.getenv("APPLICATION_HOME");
/* 113 */     if (appHome == null) {
/* 114 */       log.warn("Environment variable [{}] is null. Process id file will not be created", "APPLICATION_HOME");
/*     */       
/* 116 */       return;
/*     */     }
/*     */     
/* 119 */     String pidFileName = fileNamePrefix + ".id";
/* 120 */     Path path = Paths.get(appHome, new String[] { pidFileName });
/* 121 */     if (Files.exists(path, new LinkOption[0])) {
/* 122 */       throw new RuntimeException("Unable to create file [" + path.toAbsolutePath().toString() + "] to store the " + "process id because it already exists. Please stop any currently running process and" + " delete the process id file");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 130 */       File pidFile = Files.createFile(path, new FileAttribute[0]).toFile();
/* 131 */       pidFile.deleteOnExit();
/* 132 */       log.info("Process id [{}] is stored in file [{}]", Long.valueOf(pid), pidFile.getAbsolutePath());
/*     */       
/* 134 */       PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(pidFile), Charsets.UTF_8));Throwable localThrowable2 = null;
/*     */       try {
/* 136 */         writer.println(pid);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 134 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */       }
/*     */       finally {
/* 137 */         if (writer != null) if (localThrowable2 != null) try { writer.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else writer.close();
/*     */       }
/* 139 */     } catch (IOException e) { throw new RuntimeException("Unable to create file [" + path.toAbsolutePath().toString() + "] to store the process id", e);
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
/*     */   public static void recordHttpServerPort(Environment env)
/*     */   {
/* 153 */     env.lifecycle().addServerLifecycleListener(new ServerLifecycleListener()
/*     */     {
/*     */       public void serverStarted(Server server)
/*     */       {
/* 157 */         for (Connector connector : server.getConnectors()) {
/* 158 */           if (System.getProperty("ad.dw.http.port") != null) {
/* 159 */             return;
/*     */           }
/*     */           
/*     */ 
/* 163 */           if ("application".equals(connector.getName())) {
/* 164 */             int port = ((ServerConnector)connector).getPort();
/* 165 */             System.setProperty("ad.dw.http.port", String.valueOf(port));
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startJmxReporter(Environment env)
/*     */   {
/* 178 */     JmxReporter reporter = JmxReporter.forRegistry(env.metrics()).inDomain("Application").filter(new MetricFilter()
/*     */     {
/*     */ 
/*     */ 
/*     */       public boolean matches(String name, Metric metric)
/*     */       {
/*     */ 
/* 185 */         return !name.startsWith("jvm.");
/*     */       }
/*     */       
/* 188 */     }).build();
/* 189 */     reporter.start();
/*     */   }
/*     */   
/*     */   public static void addLifecycleLogger(AppConfiguration conf, Environment env, Logger log) {
/* 193 */     env.lifecycle().addLifeCycleListener(new AppLifecycleLogger(log, conf.getName()));
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
/*     */   public static void checkVersionsMatch(int expectedVersion, VersionedAppConfiguration versionedAppCfg, Optional<String> versionPropertyName)
/*     */   {
/* 207 */     if (versionedAppCfg.getVersion() != expectedVersion) {
/* 208 */       throw new IllegalStateException("The current application version is [" + expectedVersion + "]" + " but the YML file seems to be from version [" + versionedAppCfg.getVersion() + "]");
/*     */     }
/*     */     
/*     */ 
/* 212 */     if (versionPropertyName.isPresent()) {
/* 213 */       String configPropStr = resolveProperty((String)versionPropertyName.get(), "");
/* 214 */       Integer configPropInt = Ints.tryParse(configPropStr);
/* 215 */       if ((configPropInt == null) || (configPropInt.intValue() != expectedVersion)) {
/* 216 */         throw new IllegalStateException("The current application version is [" + expectedVersion + "]" + " but the properties file seems to be from version [" + configPropStr + "]");
/*     */       }
/*     */       
/*     */ 
/* 220 */       log.info("Application configuration version is [{}], the YML file version is [{}] and the properties file version is [{}]", new Object[] { Integer.valueOf(expectedVersion), Integer.valueOf(versionedAppCfg.getVersion()), configPropInt });
/*     */     }
/*     */     else
/*     */     {
/* 224 */       log.info("Application configuration version is [{}] and the YML file version is [{}]", Integer.valueOf(expectedVersion), Integer.valueOf(versionedAppCfg.getVersion()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/FrameworkHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */