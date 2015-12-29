/*    */ package com.appdynamics.analytics.processor.migration.util;
/*    */ 
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.google.common.base.Charsets;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import org.kohsuke.args4j.CmdLineException;
/*    */ import org.kohsuke.args4j.CmdLineParser;
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
/*    */ public abstract class MigrationHelper
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(MigrationHelper.class);
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
/* 35 */     CmdLineParser parser = new CmdLineParser(destination);
/*    */     try {
/* 37 */       parser.parseArgument(args);
/* 38 */       Reader.validate(destination);
/*    */       
/* 40 */       log.info("Arguments were parsed [\n{}\n]", destination);
/*    */     } catch (CmdLineException|ConfigurationException e) {
/* 42 */       log.error("Arguments appear to be missing or invalid", e);
/* 43 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 44 */       parser.printUsage(baos);
/* 45 */       log.error("Correct usage:\n{}", new String(baos.toByteArray(), Charsets.UTF_8));
/* 46 */       System.exit(-1);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/migration/util/MigrationHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */