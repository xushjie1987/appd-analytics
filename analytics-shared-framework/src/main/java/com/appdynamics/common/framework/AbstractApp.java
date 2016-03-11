/*     */ package com.appdynamics.common.framework;
/*     */ 
/*     */ import com.appdynamics.common.io.file.FileSource;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import com.appdynamics.common.util.var.Variables;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.inject.Injector;
/*     */ import io.dropwizard.Application;
/*     */ import io.dropwizard.cli.Command;
/*     */ import io.dropwizard.setup.Bootstrap;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractApp<C extends AppConfiguration>
/*     */   extends Application<C>
/*     */ {
/*  49 */   private static final Logger log = LoggerFactory.getLogger(AbstractApp.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String MSG_TEMPLATE_STARTUP = "\nConfiguration options/usage:\n  -y or --yml\n     YML file to use (required).\n\n     Variables in the YML file will be substituted using mappings provided in the\n     system properties, environment variables or the accompanying properties file\n     (if provided) and in that order.\n\n  -p or --prop\n     Properties file (optional)\n";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean embeddedMode;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private LazyCommand command;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractApp()
/*     */   {
/*  71 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractApp(boolean embeddedMode)
/*     */   {
/*  79 */     this.embeddedMode = embeddedMode;
/*     */   }
/*     */   
/*     */   public boolean isEmbeddedMode() {
/*  83 */     return this.embeddedMode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Pair<FileSource, Optional<String>> validate(String[] args)
/*     */   {
/*  93 */     if ((args == null) || (args.length == 0) || (args.length % 2 != 0)) {
/*  94 */       throw new IllegalArgumentException("Wrong number of arguments " + Arrays.toString(args) + "." + "\nConfiguration options/usage:\n  -y or --yml\n     YML file to use (required).\n\n     Variables in the YML file will be substituted using mappings provided in the\n     system properties, environment variables or the accompanying properties file\n     (if provided) and in that order.\n\n  -p or --prop\n     Properties file (optional)\n");
/*     */     }
/*     */     
/*     */ 
/*  98 */     FileSource yml = null;
/*  99 */     String props = null;
/*     */     
/* 101 */     for (int i = 0; i < args.length; i += 2) {
/* 102 */       String flag = args[i];
/* 103 */       String value = args[(i + 1)];
/* 104 */       switch (flag) {
/*     */       case "-y": 
/*     */       case "--yml": 
/* 107 */         yml = new FileSource(value, false);
/* 108 */         break;
/*     */       case "-yr": 
/*     */       case "--yml-resource": 
/* 111 */         yml = new FileSource(value, true);
/* 112 */         break;
/*     */       case "-p": 
/*     */       case "--prop": 
/* 115 */         props = value;
/* 116 */         break;
/*     */       default: 
/* 118 */         throw new IllegalArgumentException("Unsupported argument: " + flag + " " + value + "\nConfiguration options/usage:\n  -y or --yml\n     YML file to use (required).\n\n     Variables in the YML file will be substituted using mappings provided in the\n     system properties, environment variables or the accompanying properties file\n     (if provided) and in that order.\n\n  -p or --prop\n     Properties file (optional)\n");
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 123 */     return new Pair(yml, Optional.fromNullable(props));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void runUsingTemplate(String[] args)
/*     */     throws Exception
/*     */   {
/* 135 */     Pair<FileSource, Optional<String>> ymlAndTemplatePropsFile = validate(args);
/* 136 */     FileSource yml = (FileSource)ymlAndTemplatePropsFile.getLeft();
/* 137 */     String props = (String)((Optional)ymlAndTemplatePropsFile.getRight()).orNull();
/*     */     
/* 139 */     runUsingTemplate(yml, props);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void runUsingTemplate(FileSource serviceConfigurationTemplate)
/*     */     throws Exception
/*     */   {
/* 152 */     runUsingTemplate(serviceConfigurationTemplate, null);
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
/*     */   public final void runUsingTemplate(FileSource serviceConfigurationTemplate, String templateVariablesPropertiesFile)
/*     */     throws Exception
/*     */   {
/* 171 */     if (templateVariablesPropertiesFile != null) {
/* 172 */       File f = new File(templateVariablesPropertiesFile);
/* 173 */       if ((f.exists()) && (f.isFile()))
/*     */       {
/*     */ 
/* 176 */         System.setProperty("ad.prop.file.path", f.getAbsolutePath());
/*     */       }
/*     */     }
/*     */     
/* 180 */     File tmpFile = null;
/* 181 */     if (!serviceConfigurationTemplate.isResource()) {
/* 182 */       tmpFile = Variables.resolveVariables(serviceConfigurationTemplate, templateVariablesPropertiesFile);
/*     */     }
/*     */     else {
/* 185 */       InputStream is = AbstractApp.class.getResourceAsStream("/" + serviceConfigurationTemplate.getSource());Throwable localThrowable2 = null;
/*     */       try {
/* 187 */         if (is == null) {
/* 188 */           throw new IllegalArgumentException("The [" + serviceConfigurationTemplate.getSource() + "] file is expected to be present in the JAR/Classpath but it is not");
/*     */         }
/*     */         
/*     */ 
/* 192 */         tmpFile = Variables.resolveVariables(serviceConfigurationTemplate.getSource(), is, templateVariablesPropertiesFile);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 185 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/* 194 */         if (is != null) if (localThrowable2 != null) try { is.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else is.close();
/*     */       }
/*     */     }
/*     */     try {
/* 198 */       runUsingFile(tmpFile.getAbsolutePath());
/*     */     } finally {
/* 200 */       if (!tmpFile.delete()) {
/* 201 */         log.debug("The temporary configuration file [{}] could not be deleted", tmpFile.getAbsolutePath());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void runUsingFile(String serviceConfigurationFile)
/*     */   {
/* 211 */     callRunServer(serviceConfigurationFile);
/*     */   }
/*     */   
/*     */   private void callRunServer(String serviceConfigurationFile) {
/*     */     try {
/* 216 */       this.command = makeCommand();
/* 217 */       run(new String[] { this.command.name, serviceConfigurationFile });
/*     */     } catch (Throwable t) {
/* 219 */       handleSevereError(getName(), t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract LazyCommand makeCommand();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void initialize(Bootstrap<C> bootstrap)
/*     */   {
/*     */     try
/*     */     {
/* 236 */       if (this.command.optCommand.isPresent()) {
/* 237 */         bootstrap.addCommand((Command)this.command.optCommand.get());
/*     */       }
/* 239 */       Loaders.loadAndAddBundles(bootstrap);
/*     */     }
/*     */     catch (Throwable t) {
/* 242 */       handleSevereError(getName(), t);
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
/*     */   public final void run(C conf, Environment env)
/*     */   {
/*     */     try
/*     */     {
/* 260 */       beforeModuleLoading(conf, env);
/* 261 */       Injector injector = Loaders.loadAndInitializeModules(conf, env);
/* 262 */       afterModuleLoading(conf, env, injector);
/*     */     } catch (Throwable t) {
/* 264 */       handleSevereError(conf.getName(), t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void beforeModuleLoading(C conf, Environment env) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void afterModuleLoading(C conf, Environment env, Injector injector) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void handleSevereError(String appName, Throwable t)
/*     */   {
/* 286 */     String msg = "Severe error occurred while starting application [" + appName + "]." + " Shutdown procedure will commence soon" + "\n" + Throwables.getStackTraceAsString(t) + (this.embeddedMode ? "" : Strings.repeat(" ", 4096));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 292 */     log.error(msg);
/*     */     
/* 294 */     if (!this.embeddedMode) {
/* 295 */       String name = appName + "-error-shutdown-thread";
/* 296 */       new Thread(name)
/*     */       {
/*     */         public void run() {}
/*     */       }.start();
/*     */     }
/*     */     else
/*     */     {
/* 303 */       log.error("The application [" + appName + "] could not be shutdown cleanly. It is recommended that" + " the JVM be restarted after the problem is addressed" + Strings.repeat(" ", 4096));
/*     */       
/* 305 */       Throwables.propagate(t);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void quit() {
/*     */     try {
/* 311 */       Thread.sleep(5000L);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */     
/* 315 */     System.exit(-1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class LazyCommand
/*     */   {
/*     */     final String name;
/*     */     
/*     */ 
/*     */     final Optional<? extends Command> optCommand;
/*     */     
/*     */ 
/*     */ 
/*     */     public LazyCommand(String name, Optional<? extends Command> optCommand)
/*     */     {
/* 331 */       this.name = name;
/* 332 */       this.optCommand = optCommand;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/AbstractApp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */