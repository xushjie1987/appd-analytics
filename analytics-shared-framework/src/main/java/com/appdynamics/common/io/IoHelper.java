/*     */ package com.appdynamics.common.io;
/*     */ 
/*     */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.net.HostAndPort;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.FileVisitor;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IoHelper
/*     */ {
/*  28 */   private static final boolean IS_FILE_SYSTEM_CASE_SENSITIVE = !new File("a").equals(new File("A"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String GLOB_PATH_SEPARATOR = "/";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<HostAndPort> parseHostAndPortCsv(String csvList)
/*     */   {
/*  40 */     parseHostAndPortCsv(csvList, new Function()
/*     */     {
/*     */       public HostAndPort apply(@Nullable HostAndPort input)
/*     */       {
/*  44 */         if (input == null) {
/*  45 */           return null;
/*     */         }
/*  47 */         if (input.getHostText().equals("0.0.0.0")) {
/*  48 */           return HostAndPort.fromParts("localhost", input.getPort());
/*     */         }
/*  50 */         return input;
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<HostAndPort> parseHostAndPortCsv(String csvList, Function<HostAndPort, HostAndPort> rewriter)
/*     */   {
/*  61 */     Iterable<String> items = Splitter.on(',').omitEmptyStrings().split(csvList);
/*  62 */     LinkedList<HostAndPort> hostAndPorts = new LinkedList();
/*  63 */     for (String item : items) {
/*  64 */       HostAndPort hp = HostAndPort.fromString(item);
/*  65 */       hp = (HostAndPort)rewriter.apply(hp);
/*  66 */       if (hp != null) {
/*  67 */         hostAndPorts.add(hp);
/*     */       }
/*     */     }
/*  70 */     return hostAndPorts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<Path> findMatchingPaths(Path dir, PathMatcher pathMatcher)
/*     */     throws IOException
/*     */   {
/*  81 */     final List<Path> matchingPaths = new LinkedList();
/*     */     
/*  83 */     FileVisitor<Path> matcherVisitor = new SimpleFileVisitor()
/*     */     {
/*     */       public FileVisitResult visitFile(Path file, BasicFileAttributes baf) {
/*  86 */         if (this.val$pathMatcher.matches(file.getFileName())) {
/*  87 */           matchingPaths.add(file);
/*     */         }
/*  89 */         return FileVisitResult.CONTINUE;
/*     */       }
/*  91 */     };
/*  92 */     Files.walkFileTree(dir, matcherVisitor);
/*     */     
/*  94 */     return matchingPaths;
/*     */   }
/*     */   
/*     */   public static boolean isDefaultFileSystemCaseSensitive() {
/*  98 */     return IS_FILE_SYSTEM_CASE_SENSITIVE;
/*     */   }
/*     */   
/*     */   public static String getGlobPatternFromPathGlob(FilePathConfiguration filePath) {
/* 102 */     return getGlobPatternFromPathGlob(filePath.getPath(), filePath.getNameGlob());
/*     */   }
/*     */   
/*     */   public static String getGlobPatternFromPathGlob(String path, String glob) {
/* 106 */     return String.format("glob:%s%s%s", new Object[] { StringUtils.stripEnd(path.replace('\\', '/'), "\\/"), "/", glob });
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/IoHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */