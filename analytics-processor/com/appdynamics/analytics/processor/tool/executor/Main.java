/*     */ package com.appdynamics.analytics.processor.tool.executor;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.tool.common.ToolHelper;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.var.ChainedVariableResolver;
/*     */ import com.appdynamics.common.util.var.EchoVariableResolver;
/*     */ import com.appdynamics.common.util.var.VariableResolver;
/*     */ import com.appdynamics.common.util.var.Variables;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import org.kohsuke.args4j.Option;
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
/*     */ public class Main
/*     */ {
/*  36 */   private static final Logger log = LoggerFactory.getLogger(Main.class);
/*     */   final Input input;
/*     */   
/*     */   static class Input { @Option(name="-y", usage="name of the yml file to drive program", required=true)
/*     */     String ymlFile; @Option(name="-p", usage="name of the properties file to drive program", required=true)
/*     */     String propertiesFile;
/*  42 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Input)) return false; Input other = (Input)o; if (!other.canEqual(this)) return false; Object this$ymlFile = getYmlFile();Object other$ymlFile = other.getYmlFile(); if (this$ymlFile == null ? other$ymlFile != null : !this$ymlFile.equals(other$ymlFile)) return false; Object this$propertiesFile = getPropertiesFile();Object other$propertiesFile = other.getPropertiesFile(); if (this$propertiesFile == null ? other$propertiesFile != null : !this$propertiesFile.equals(other$propertiesFile)) return false; if (isInteractive() != other.isInteractive()) return false; Object this$startStep = getStartStep();Object other$startStep = other.getStartStep();return this$startStep == null ? other$startStep == null : this$startStep.equals(other$startStep); } public boolean canEqual(Object other) { return other instanceof Input; } public int hashCode() { int PRIME = 31;int result = 1;Object $ymlFile = getYmlFile();result = result * 31 + ($ymlFile == null ? 0 : $ymlFile.hashCode());Object $propertiesFile = getPropertiesFile();result = result * 31 + ($propertiesFile == null ? 0 : $propertiesFile.hashCode());result = result * 31 + (isInteractive() ? 1231 : 1237);Object $startStep = getStartStep();result = result * 31 + ($startStep == null ? 0 : $startStep.hashCode());return result; } public String toString() { return "Main.Input(ymlFile=" + getYmlFile() + ", propertiesFile=" + getPropertiesFile() + ", interactive=" + isInteractive() + ", startStep=" + getStartStep() + ")"; }
/*     */     
/*     */     public String getYmlFile() {
/*  45 */       return this.ymlFile; } public void setYmlFile(String ymlFile) { this.ymlFile = ymlFile; }
/*     */     
/*     */     public String getPropertiesFile() {
/*  48 */       return this.propertiesFile; } public void setPropertiesFile(String propertiesFile) { this.propertiesFile = propertiesFile; }
/*     */     @Option(name="-interactive", usage="set if you want to be able to pause between steps if there is an error")
/*  50 */     boolean interactive = false;
/*  51 */     public boolean isInteractive() { return this.interactive; } public void setInteractive(boolean interactive) { this.interactive = interactive; }
/*     */     @Option(name="-startStep", usage="name of step to start on, defaults to the first step")
/*  53 */     String startStep = "";
/*  54 */     public String getStartStep() { return this.startStep; } public void setStartStep(String startStep) { this.startStep = startStep; }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  58 */     ToolHelper.setRootLogLevel();
/*  59 */     Input input = new Input();
/*  60 */     ToolHelper.readArgumentsOrExit(args, input);
/*     */     
/*  62 */     Main main = new Main(input);
/*     */     try {
/*  64 */       main.execute();
/*     */     } catch (ProcessHaltedException e) {
/*  66 */       System.out.println(e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   void execute() {
/*  71 */     Executor executor = new Executor(this.executionConfig, this.input.interactive, this.input.startStep);
/*  72 */     executor.executeSteps();
/*     */   }
/*     */   
/*     */   public Main(Input input)
/*     */   {
/*  77 */     Preconditions.checkArgument(input != null, "Must provide valid input");
/*  78 */     this.input = input;
/*  79 */     this.executionConfig = readConfig();
/*  80 */     Preconditions.checkArgument((this.executionConfig.getSteps() != null) && (!this.executionConfig.getSteps().isEmpty()), "No steps were configured in Yaml file [" + input.ymlFile + "]");
/*     */   }
/*     */   
/*     */   final ExecutionConfig executionConfig;
/*     */   private ExecutionConfig readConfig()
/*     */   {
/*  86 */     Path ymlFilePath = Paths.get(this.input.ymlFile, new String[0]);
/*  87 */     Preconditions.checkArgument(ymlFilePath.toFile().exists(), "Configuration file [" + this.input.ymlFile + "] does not exist!");
/*     */     
/*     */     try
/*     */     {
/*  91 */       String ymlFileContents = new String(Files.readAllBytes(ymlFilePath));
/*     */       
/*  93 */       Properties properties = new Properties();
/*  94 */       InputStream propStream = (InputStream)Preconditions.checkNotNull(Files.newInputStream(Paths.get(this.input.propertiesFile, new String[0]), new OpenOption[0]), "Property file [" + this.input.propertiesFile + "] does not exist!");
/*     */       
/*  96 */       properties.load(propStream);
/*  97 */       VariableResolver variableResolver = new ChainedVariableResolver(Variables.loadVariables(properties), EchoVariableResolver.INSTANCE, new VariableResolver[0]);
/*     */       
/*     */ 
/* 100 */       return (ExecutionConfig)Reader.DEFAULT_YAML_MAPPER.readValue(Variables.resolve(ymlFileContents, variableResolver), ExecutionConfig.class);
/*     */     }
/*     */     catch (IOException e) {
/* 103 */       throw Throwables.propagate(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */