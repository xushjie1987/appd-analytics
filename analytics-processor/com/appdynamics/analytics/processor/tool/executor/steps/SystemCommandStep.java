/*    */ package com.appdynamics.analytics.processor.tool.executor.steps;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.tool.executor.ExecutionContext;
/*    */ import com.appdynamics.analytics.processor.tool.executor.ExecutionStep;
/*    */ import com.appdynamics.analytics.processor.tool.executor.response.ExecutionResponse;
/*    */ import com.appdynamics.analytics.processor.tool.executor.response.FailedResponse;
/*    */ import com.appdynamics.analytics.processor.tool.executor.response.SuccessfulResponse;
/*    */ import com.google.common.base.Charsets;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.base.Strings;
/*    */ import com.google.common.base.Throwables;
/*    */ import java.beans.ConstructorProperties;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import lombok.NonNull;
/*    */ import org.apache.commons.io.IOUtils;
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
/*    */ public class SystemCommandStep
/*    */   extends AbstractExecutionStep
/*    */ {
/* 30 */   private static final Logger log = LoggerFactory.getLogger(SystemCommandStep.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */   public SystemCommandStep(Properties properties) { this.properties = properties; }
/*    */   
/*    */   public static class Properties {
/* 40 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Properties)) return false; Properties other = (Properties)o; if (!other.canEqual(this)) return false; Object this$command = getCommand();Object other$command = other.getCommand();return this$command == null ? other$command == null : this$command.equals(other$command); } public boolean canEqual(Object other) { return other instanceof Properties; } public int hashCode() { int PRIME = 31;int result = 1;Object $command = getCommand();result = result * 31 + ($command == null ? 0 : $command.hashCode());return result; } public String toString() { return "SystemCommandStep.Properties(command=" + getCommand() + ")"; } @ConstructorProperties({"command"})
/* 41 */     public Properties(String command) { this.command = command; }
/*    */     
/*    */ 
/* 44 */     public String getCommand() { return this.command; } public void setCommand(String command) { this.command = command; }
/*    */     
/*    */ 
/* 47 */     public Properties copy() { return new Properties(this.command); }
/*    */     
/*    */     @NotNull
/*    */     String command; }
/*    */   
/* 52 */   public void setRuntime(Runtime runtime) { this.runtime = runtime; } Runtime runtime = Runtime.getRuntime();
/*    */   
/*    */   public void setProperties(@NonNull Properties properties)
/*    */   {
/* 56 */     if (properties == null) throw new NullPointerException("properties"); this.properties = properties;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   @NonNull
/*    */   Properties properties;
/* 62 */   protected Logger log() { return log; }
/*    */   
/*    */ 
/*    */   protected ExecutionResponse executeSystemCommand(String cmd)
/*    */   {
/* 67 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(cmd), "Command must be set before executing.");
/*    */     try {
/* 69 */       Process proc = this.runtime.exec(cmd);
/* 70 */       proc.waitFor();
/* 71 */       String response = String.format("Input stream was %n%s%nand error stream was %n%s", new Object[] { IOUtils.toString(proc.getInputStream(), Charsets.UTF_8), IOUtils.toString(proc.getErrorStream(), Charsets.UTF_8) });
/*    */       
/*    */ 
/* 74 */       if (proc.exitValue() == 0) {
/* 75 */         return new SuccessfulResponse(response);
/*    */       }
/* 77 */       return new FailedResponse(proc.exitValue(), response);
/*    */     }
/*    */     catch (Exception e) {
/* 80 */       return new FailedResponse(1, e.getMessage() + ":\n" + Throwables.getStackTraceAsString(e));
/*    */     }
/*    */   }
/*    */   
/*    */   public ExecutionResponse executeStep(ExecutionContext executionContext)
/*    */   {
/* 86 */     Preconditions.checkArgument(this.properties != null, "Must set properties before executing");
/* 87 */     return executeSystemCommand(this.properties.command);
/*    */   }
/*    */   
/*    */   public ExecutionStep copy()
/*    */   {
/* 92 */     ExecutionStep step = new SystemCommandStep(this.properties.copy());
/* 93 */     step.setName(getName());
/* 94 */     return step;
/*    */   }
/*    */   
/*    */   public SystemCommandStep() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/steps/SystemCommandStep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */