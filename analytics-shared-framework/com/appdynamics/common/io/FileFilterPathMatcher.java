/*    */ package com.appdynamics.common.io;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileFilter;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.PathMatcher;
/*    */ import java.nio.file.Paths;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileFilterPathMatcher
/*    */   implements FileFilter
/*    */ {
/*    */   final PathMatcher pathMatcher;
/*    */   
/*    */   public FileFilterPathMatcher(PathMatcher pathMatcher)
/*    */   {
/* 23 */     this.pathMatcher = pathMatcher;
/*    */   }
/*    */   
/*    */   public boolean accept(File file)
/*    */   {
/* 28 */     Path path = Paths.get(file.getAbsolutePath(), new String[0]);
/* 29 */     return this.pathMatcher.matches(path);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/FileFilterPathMatcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */