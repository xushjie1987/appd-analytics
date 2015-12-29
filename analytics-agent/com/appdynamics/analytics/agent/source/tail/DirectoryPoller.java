/*     */ package com.appdynamics.analytics.agent.source.tail;
/*     */ 
/*     */ import com.appdynamics.analytics.agent.input.tail.FileInfo;
/*     */ import com.appdynamics.analytics.agent.input.tail.FileSignature;
/*     */ import com.appdynamics.common.io.IoHelper;
/*     */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileSystem;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.FileVisitOption;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ 
/*     */ public class DirectoryPoller
/*     */ {
/*  38 */   private static final Logger log = LoggerFactory.getLogger(DirectoryPoller.class);
/*     */   private final Path rootPath;
/*     */   
/*  41 */   public Path getRootPath() { return this.rootPath; }
/*     */   
/*     */ 
/*     */ 
/*     */   private final PathMatcher matcher;
/*     */   
/*     */ 
/*     */   private final ConcurrentMap<String, FileInfo> fileInfos;
/*     */   
/*     */ 
/*     */   private final String globPattern;
/*     */   
/*     */ 
/*     */   private boolean firstTimePoll;
/*     */   
/*     */   public DirectoryPoller(FilePathConfiguration sourcePath)
/*     */   {
/*  58 */     this.globPattern = IoHelper.getGlobPatternFromPathGlob(sourcePath);
/*     */     
/*  60 */     this.matcher = FileSystems.getDefault().getPathMatcher(this.globPattern);
/*  61 */     this.rootPath = Paths.get(sourcePath.getPath(), new String[0]);
/*     */     
/*  63 */     log.debug("Initializing Directory [{}] and matcher [{}] with matcherPath [{}]", new Object[] { this.rootPath, this.matcher, this.globPattern });
/*  64 */     if (!Files.exists(this.rootPath, new LinkOption[0])) {
/*  65 */       log.error("The specified path [{}] does not exist, we will skip scan", this.rootPath.toString());
/*     */     }
/*  67 */     this.fileInfos = new ConcurrentHashMap();
/*     */     
/*  69 */     this.firstTimePoll = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearFileCacheWithFileSignature(String fileSignature)
/*     */   {
/*  78 */     for (Map.Entry<String, FileInfo> fileInfoKV : this.fileInfos.entrySet()) {
/*     */       try {
/*  80 */         FileSignature cachedFileSignature = ((FileInfo)fileInfoKV.getValue()).getFileSignature();
/*  81 */         if ((cachedFileSignature != null) && (fileSignature.equals(cachedFileSignature.getSignature()))) {
/*  82 */           this.fileInfos.remove(fileInfoKV.getKey());
/*     */         }
/*     */       } catch (IOException e) {
/*  85 */         log.error("Failed to retrieve signature for file [" + (String)fileInfoKV.getKey() + "]", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileInfo[] getFiles()
/*     */   {
/*  97 */     final ArrayList<FileInfo> files = new ArrayList();
/*     */     try {
/*  99 */       if ((log.isDebugEnabled()) || (this.firstTimePoll)) {
/* 100 */         log.info("Starting directory scan of [{}]", this.globPattern);
/*     */       }
/*     */       
/* 103 */       if (Files.exists(this.rootPath, new LinkOption[0]))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 108 */         Files.walkFileTree(this.rootPath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor()
/*     */         {
/*     */           static final int FILES_SCANNED_WARN_THRESHOLD = 1000;
/* 111 */           private int fileCount = 0;
/*     */           
/*     */           public FileVisitResult visitFileFailed(Path file, IOException ioExcept) throws IOException
/*     */           {
/* 115 */             if ((DirectoryPoller.log.isDebugEnabled()) || (DirectoryPoller.this.firstTimePoll)) {
/* 116 */               DirectoryPoller.log.info("Skipping file [{}] because of IO exception [{}]", file, ioExcept);
/*     */             }
/* 118 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */           
/*     */ 
/*     */           public FileVisitResult visitFile(Path filePath, BasicFileAttributes attr)
/*     */             throws IOException
/*     */           {
/* 125 */             boolean matches = DirectoryPoller.this.matcher.matches(filePath);
/*     */             
/* 127 */             if (DirectoryPoller.this.firstTimePoll) {
/* 128 */               if (this.fileCount == 1000) {
/* 129 */                 DirectoryPoller.log.warn("[{}] files scanned for a single directory, please consider more specific directory path rules, directory = [{}]", Integer.valueOf(1000), DirectoryPoller.this.rootPath.toString());
/*     */ 
/*     */ 
/*     */               }
/*     */               else
/*     */               {
/*     */ 
/* 136 */                 this.fileCount += 1;
/*     */               }
/*     */             }
/* 139 */             String fullPath = IoHelper.isDefaultFileSystemCaseSensitive() ? filePath.toAbsolutePath().toString() : filePath.toAbsolutePath().toString().toLowerCase();
/*     */             
/*     */ 
/*     */ 
/* 143 */             if (attr.isRegularFile()) {
/* 144 */               if (attr.size() > 0L) {
/* 145 */                 if (DirectoryPoller.log.isDebugEnabled()) {
/* 146 */                   DirectoryPoller.log.debug("Found file [{}] checking if matches with glob [{}], match result: [{}]", new Object[] { fullPath, DirectoryPoller.this.globPattern, Boolean.valueOf(matches) });
/*     */                 }
/*     */                 
/*     */ 
/*     */ 
/* 151 */                 if (matches) {
/* 152 */                   FileInfo fileInfo = (FileInfo)DirectoryPoller.this.fileInfos.get(fullPath);
/* 153 */                   if ((fileInfo != null) && (fileInfo.metadataMatches(attr)))
/*     */                   {
/* 155 */                     files.add(fileInfo);
/*     */                   } else {
/* 157 */                     files.add(new FileInfo(fullPath, attr));
/*     */                   }
/*     */                 }
/* 160 */                 else if ((DirectoryPoller.log.isDebugEnabled()) || (DirectoryPoller.this.firstTimePoll)) {
/* 161 */                   DirectoryPoller.log.info("Skipping file [{}] because it doesn't match the file mask pattern.", fullPath);
/*     */ 
/*     */                 }
/*     */                 
/*     */ 
/*     */ 
/*     */               }
/* 168 */               else if ((DirectoryPoller.log.isDebugEnabled()) || (DirectoryPoller.this.firstTimePoll)) {
/* 169 */                 DirectoryPoller.log.info("Skipping file [{}] because it's an empty file.", fullPath);
/*     */               }
/*     */               
/*     */             }
/* 173 */             else if ((DirectoryPoller.log.isDebugEnabled()) || (DirectoryPoller.this.firstTimePoll)) {
/* 174 */               DirectoryPoller.log.info("Found directory [{}], skipping.", fullPath);
/*     */             }
/*     */             
/* 177 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */         });
/*     */       }
/*     */     } catch (IOException except) {
/* 182 */       log.error(String.format("Failed to scan the directory. [%s]", new Object[] { this.rootPath.toString() }), except);
/*     */     }
/*     */     
/*     */ 
/* 186 */     this.fileInfos.clear();
/* 187 */     for (FileInfo fileInfo : files) {
/* 188 */       this.fileInfos.put(fileInfo.getFilename(), fileInfo);
/*     */     }
/*     */     
/* 191 */     if ((this.fileInfos.size() == 0) && ((log.isDebugEnabled()) || (this.firstTimePoll))) {
/* 192 */       log.info("No files found when polling directory [{}]", this.globPattern);
/*     */     }
/*     */     
/* 195 */     this.firstTimePoll = false;
/*     */     
/* 197 */     return (FileInfo[])files.toArray(new FileInfo[0]);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/tail/DirectoryPoller.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */