/*    */ package com.appdynamics.analytics.processor.migration;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import org.kohsuke.args4j.CmdLineException;
/*    */ import org.kohsuke.args4j.CmdLineParser;
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
/*    */ public abstract class AbstractMigrator
/*    */ {
/*    */   protected CmdLineParser parser;
/*    */   
/*    */   protected AbstractMigrator() {}
/*    */   
/*    */   protected AbstractMigrator(String[] args)
/*    */   {
/* 26 */     this.parser = new CmdLineParser(this);
/*    */     try {
/* 28 */       this.parser.parseArgument(args);
/*    */     } catch (CmdLineException e) {
/* 30 */       log(e.getMessage());
/* 31 */       printUsage();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void migrate()
/*    */   {
/* 39 */     run();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void migrate(String[] args)
/*    */   {
/* 47 */     this.parser = new CmdLineParser(this);
/*    */     try {
/* 49 */       this.parser.parseArgument(args);
/*    */     } catch (CmdLineException e) {
/* 51 */       log(e.getMessage());
/* 52 */       printUsage();
/* 53 */       return;
/*    */     }
/* 55 */     run();
/*    */   }
/*    */   
/*    */ 
/*    */   protected abstract void run();
/*    */   
/*    */ 
/*    */   protected void log(String output)
/*    */   {
/* 64 */     System.out.println(output);
/*    */   }
/*    */   
/*    */   protected OutputStream getLogStream() {
/* 68 */     return System.out;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract String getProgramDescription();
/*    */   
/*    */ 
/*    */ 
/*    */   protected void printUsage()
/*    */   {
/* 80 */     log(getProgramDescription());
/* 81 */     this.parser.printUsage(getLogStream());
/* 82 */     exit();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void exit()
/*    */   {
/* 90 */     System.exit(1);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/migration/AbstractMigrator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */