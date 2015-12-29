/*    */ package com.appdynamics.analytics.agent;
/*    */ 
/*    */ import com.appdynamics.common.framework.AbstractApp;
/*    */ import com.appdynamics.common.framework.util.EmbeddedModeSecurityManager;
/*    */ import com.appdynamics.common.framework.util.FrameworkHelper;
/*    */ import com.appdynamics.common.framework.util.SimpleApp;
/*    */ import com.appdynamics.common.framework.util.VersionedAppConfiguration;
/*    */ import com.appdynamics.common.io.file.FileSource;
/*    */ import com.appdynamics.common.util.misc.Pair;
/*    */ import com.google.common.base.Optional;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnalyticsAgent
/*    */ {
/* 37 */   private static final Logger log = LoggerFactory.getLogger(AnalyticsAgent.class);
/*    */   
/*    */   static final String ILLEGAL_ARGS_MSG = "The path to the properties file must be provided";
/*    */   static final String PKG_ANALYTICS = "com.appdynamics.analytics";
/*    */   static final String PKG_COMMON_FRAMEWORK = "com.appdynamics.common.framework";
/*    */   
/*    */   static String[] validateAndMakeArgs(String[] args)
/*    */   {
/* 45 */     if ((args.length < 1) || (args.length > 2)) {
/* 46 */       throw new IllegalArgumentException("The path to the properties file must be provided");
/*    */     }
/* 48 */     List<String> appArgs = new ArrayList();
/* 49 */     if (args.length == 1)
/*    */     {
/* 51 */       appArgs.add("-p");
/* 52 */       appArgs.add(args[0]);
/*    */     } else {
/* 54 */       appArgs.add(args[0]);
/* 55 */       appArgs.add(args[1]);
/*    */     }
/* 57 */     appArgs.add("-yr");
/* 58 */     appArgs.add("analytics-agent.yml");
/* 59 */     String[] newArgs = (String[])appArgs.toArray(new String[appArgs.size()]);
/*    */     
/* 61 */     Pair<FileSource, Optional<String>> params = null;
/*    */     try {
/* 63 */       params = AbstractApp.validate(newArgs);
/*    */     }
/*    */     catch (IllegalArgumentException e)
/*    */     {
/* 67 */       log.trace("Internal exception", e);
/* 68 */       throw new IllegalArgumentException("The path to the properties file must be provided");
/*    */     }
/* 70 */     if ((params == null) || (!((Optional)params.getRight()).isPresent())) {
/* 71 */       throw new IllegalArgumentException("The path to the properties file must be provided");
/*    */     }
/*    */     
/* 74 */     log.info("Starting analytics agent with arguments " + Arrays.asList(newArgs));
/* 75 */     return newArgs;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 79 */     String embeddedStr = System.getProperty("analytics.mode.embedded", "false");
/*    */     
/* 81 */     boolean embedded = Boolean.parseBoolean(embeddedStr);
/* 82 */     if (embedded) {
/* 83 */       EmbeddedModeSecurityManager.installSecurityManager(new String[] { "com.appdynamics.common.framework", "com.appdynamics.analytics" });
/*    */     }
/*    */     
/* 86 */     String[] newArgs = validateAndMakeArgs(args);
/* 87 */     SimpleApp<VersionedAppConfiguration> app = new SimpleApp(embedded)
/*    */     {
/*    */       protected void beforeModuleLoading(VersionedAppConfiguration conf, Environment env) {
/* 90 */         FrameworkHelper.checkVersionsMatch(1, conf, Optional.of("ad.configuration.version"));
/*    */         
/*    */ 
/* 93 */         super.beforeModuleLoading(conf, env);
/*    */       }
/* 95 */     };
/* 96 */     app.runUsingTemplate(newArgs);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/AnalyticsAgent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */