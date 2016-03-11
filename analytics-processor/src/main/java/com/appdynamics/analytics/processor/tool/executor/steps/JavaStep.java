/*     */ package com.appdynamics.analytics.processor.tool.executor.steps;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionContext;
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionStep;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.ExecutionResponse;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.FailedResponse;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.SuccessfulResponse;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.Permission;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ public class JavaStep
/*     */   extends AbstractExecutionStep
/*     */ {
/*  34 */   private static final Logger log = LoggerFactory.getLogger(JavaStep.class);
/*     */   
/*     */   public static class Properties { String mainClass;
/*  37 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Properties)) return false; Properties other = (Properties)o; if (!other.canEqual(this)) return false; Object this$mainClass = getMainClass();Object other$mainClass = other.getMainClass(); if (this$mainClass == null ? other$mainClass != null : !this$mainClass.equals(other$mainClass)) return false; Object this$programArguments = getProgramArguments();Object other$programArguments = other.getProgramArguments();return this$programArguments == null ? other$programArguments == null : this$programArguments.equals(other$programArguments); } public boolean canEqual(Object other) { return other instanceof Properties; } public int hashCode() { int PRIME = 31;int result = 1;Object $mainClass = getMainClass();result = result * 31 + ($mainClass == null ? 0 : $mainClass.hashCode());Object $programArguments = getProgramArguments();result = result * 31 + ($programArguments == null ? 0 : $programArguments.hashCode());return result; } public String toString() { return "JavaStep.Properties(mainClass=" + getMainClass() + ", programArguments=" + getProgramArguments() + ")"; } @ConstructorProperties({"mainClass", "programArguments"})
/*  38 */     public Properties(String mainClass, Map<String, String> programArguments) { this.mainClass = mainClass;this.programArguments = programArguments; } Map<String, String> programArguments = Collections.emptyMap();
/*     */     
/*     */ 
/*  41 */     public String getMainClass() { return this.mainClass; } public void setMainClass(String mainClass) { this.mainClass = mainClass; }
/*  42 */     public Map<String, String> getProgramArguments() { return this.programArguments; } public void setProgramArguments(Map<String, String> programArguments) { this.programArguments = programArguments; }
/*     */     
/*     */     public Properties copy() {
/*  45 */       return new Properties(this.mainClass, Maps.newHashMap(this.programArguments));
/*     */     }
/*     */     
/*     */     public Properties() {}
/*     */   }
/*     */   
/*     */   public JavaStep(Properties properties)
/*     */   {
/*  53 */     Preconditions.checkArgument((properties != null) && (!Strings.isNullOrEmpty(properties.mainClass)));
/*  54 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public ExecutionStep copy()
/*     */   {
/*  59 */     JavaStep step = new JavaStep(this.properties.copy());
/*  60 */     step.setName(getName());
/*  61 */     return step;
/*     */   }
/*     */   
/*     */   public ExecutionResponse executeStep(ExecutionContext executionContext)
/*     */   {
/*  66 */     log.info("Properties: " + this.properties);
/*     */     Class<?> c;
/*     */     try {
/*  69 */       c = getClass().getClassLoader().loadClass(this.properties.mainClass);
/*     */     } catch (ClassNotFoundException e) {
/*  71 */       throw new IllegalArgumentException("Could not load java class [" + this.properties.mainClass + "]");
/*     */     }
/*     */     Method m;
/*     */     try {
/*  75 */       m = c.getMethod("main", new Class[] { String[].class });
/*     */     } catch (NoSuchMethodException e) {
/*  77 */       throw new IllegalArgumentException("Specified main class [" + this.properties.mainClass + "] does not contain " + "a static 'main' method that takes a string array");
/*     */     }
/*     */     
/*  80 */     m.setAccessible(true);
/*  81 */     int mods = m.getModifiers();
/*  82 */     if ((m.getReturnType() != Void.TYPE) || (!Modifier.isStatic(mods)) || (!Modifier.isPublic(mods)))
/*     */     {
/*  84 */       throw new IllegalArgumentException("Specified main class [" + this.properties.mainClass + "] does not contain " + "a static 'main' method that takes a string array");
/*     */     }
/*     */     
/*     */ 
/*  88 */     SecurityManager oldSecurityManger = overrideSecurityManager();
/*     */     try {
/*  90 */       m.invoke(null, new Object[] { buildArgs() });
/*     */     } catch (Exception e) {
/*  92 */       if ((e instanceof InvocationTargetException)) {
/*  93 */         int status = 1;
/*  94 */         if ((e.getCause() instanceof ExitException)) {
/*  95 */           status = ((ExitException)e.getCause()).status;
/*     */         }
/*  97 */         if (status != 0) {
/*  98 */           return new FailedResponse(status, "Execution of Java command [" + this.properties.mainClass + "] failed: " + e.getMessage() + ":\n" + Throwables.getStackTraceAsString(e));
/*     */         }
/*     */       }
/*     */       else {
/* 102 */         throw Throwables.propagate(e);
/*     */       }
/*     */     } finally {
/* 105 */       System.setSecurityManager(oldSecurityManger);
/*     */     }
/* 107 */     return new SuccessfulResponse("Execution of Java command [" + this.properties.mainClass + "] successful");
/*     */   }
/*     */   
/*     */   private static class ExitException extends SecurityException {
/*     */     public final int status;
/*     */     
/*     */     public ExitException(int status) {
/* 114 */       this.status = status;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   final Properties properties;
/*     */   
/*     */   private static class NoExitSecurityManager
/*     */     extends SecurityManager
/*     */   {
/*     */     public void checkPermission(Permission perm) {}
/*     */     
/*     */ 
/*     */     public void checkPermission(Permission perm, Object context) {}
/*     */     
/*     */ 
/*     */     public void checkExit(int status)
/*     */     {
/* 132 */       super.checkExit(status);
/* 133 */       throw new JavaStep.ExitException(status);
/*     */     }
/*     */   }
/*     */   
/*     */   private SecurityManager overrideSecurityManager() {
/* 138 */     SecurityManager oldSecurityManger = System.getSecurityManager();
/* 139 */     System.setSecurityManager(new NoExitSecurityManager(null));
/* 140 */     return oldSecurityManger;
/*     */   }
/*     */   
/*     */   private String[] buildArgs() {
/* 144 */     String[] args = new String[this.properties.programArguments.size() * 2];
/* 145 */     int i = 0;
/* 146 */     for (Map.Entry<String, String> entry : this.properties.programArguments.entrySet()) {
/* 147 */       args[(i++)] = ("-" + (String)entry.getKey());
/* 148 */       args[(i++)] = ((String)entry.getValue());
/*     */     }
/* 150 */     return args;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/steps/JavaStep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */