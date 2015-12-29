/*    */ package com.appdynamics.common.util.var;
/*    */ 
/*    */ import com.google.common.base.Charsets;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
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
/*    */ public enum FileIncludeVariableResolver
/*    */   implements VariableResolver
/*    */ {
/* 25 */   INSTANCE;
/*    */   
/* 23 */   private static final Logger log = LoggerFactory.getLogger(FileIncludeVariableResolver.class);
/*    */   
/*    */ 
/*    */ 
/*    */   public static final String VAR_INCLUDE = "include.file";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private FileIncludeVariableResolver() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object resolve(String variable)
/*    */   {
/* 39 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<String> resolve(String variable, String variableExtension)
/*    */   {
/* 51 */     if (!variable.equals("include.file")) {
/* 52 */       return null;
/*    */     }
/*    */     
/* 55 */     log.debug("Attempting to load file [{}]", variableExtension);
/* 56 */     File file = new File(variableExtension);
/* 57 */     if ((!file.exists()) || (!file.isFile())) {
/* 58 */       throw new IllegalArgumentException("The path [" + variableExtension + "] does not point to a valid file");
/*    */     }
/*    */     try
/*    */     {
/* 62 */       return Files.readAllLines(file.toPath(), Charsets.UTF_8);
/*    */     } catch (IOException e) {
/* 64 */       throw new RuntimeException("An error occurred while reading lines from the file [" + variableExtension + "]", e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/FileIncludeVariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */