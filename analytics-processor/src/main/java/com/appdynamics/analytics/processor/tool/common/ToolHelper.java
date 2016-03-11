/*    */ package com.appdynamics.analytics.processor.tool.common;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.appdynamics.common.util.standalone.ProxyMain;
/*    */ import com.google.common.base.Charsets;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import org.kohsuke.args4j.CmdLineException;
/*    */ import org.kohsuke.args4j.CmdLineParser;
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
/*    */ public abstract class ToolHelper
/*    */ {
/* 28 */   private static final org.slf4j.Logger log = LoggerFactory.getLogger(ToolHelper.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void readArgumentsOrExit(String[] args, Object destination)
/*    */   {
/* 40 */     CmdLineParser parser = new CmdLineParser(destination);
/*    */     try {
/* 42 */       parser.parseArgument(args);
/* 43 */       Reader.validate(destination);
/*    */       
/* 45 */       log.info("Arguments were parsed [\n{}\n]", destination);
/*    */     } catch (CmdLineException|ConfigurationException e) {
/* 47 */       log.error("Arguments appear to be missing or invalid: " + e.getMessage());
/* 48 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 49 */       parser.printUsage(baos);
/* 50 */       log.error("Correct usage:\n{}", new String(baos.toByteArray(), Charsets.UTF_8));
/* 51 */       System.exit(-1);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void setRootLogLevel() {
/* 56 */     LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
/* 57 */     ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger("ROOT");
/* 58 */     boolean verbose = ProxyMain.isLogLevelVerbose();
/* 59 */     rootLogger.setLevel(verbose ? Level.DEBUG : Level.INFO);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/common/ToolHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */