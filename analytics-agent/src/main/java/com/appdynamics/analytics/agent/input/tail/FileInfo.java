/*     */ package com.appdynamics.analytics.agent.input.tail;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import javax.validation.constraints.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileInfo
/*     */   implements AutoCloseable
/*     */ {
/*  24 */   FileChannel fileChannel = null;
/*     */   @NotNull
/*     */   String filename;
/*  27 */   public String getFilename() { return this.filename; }
/*     */   long size;
/*     */   
/*  30 */   public long getSize() { return this.size; }
/*     */   long created;
/*     */   
/*  33 */   public long getCreated() { return this.created; }
/*     */   
/*     */   public long getLastModified() {
/*  36 */     return this.lastModified; }
/*     */   
/*     */   long lastModified;
/*     */   private final Path filePath;
/*  40 */   private FileSignature signature = null;
/*     */   
/*     */   public FileInfo(String filePath, BasicFileAttributes attr) {
/*  43 */     this(filePath, attr.size(), attr.creationTime().toMillis(), attr.lastModifiedTime().toMillis());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected FileInfo(String filename, long size, long created, long lastModified)
/*     */   {
/*  50 */     this.filename = filename;
/*  51 */     this.size = size;
/*  52 */     this.created = created;
/*  53 */     this.lastModified = lastModified;
/*  54 */     this.filePath = Paths.get(filename, new String[0]);
/*     */   }
/*     */   
/*     */   protected void finalize() throws IOException {
/*  58 */     close();
/*     */   }
/*     */   
/*     */ 
/*     */   public FileSignature getFileSignature()
/*     */     throws IOException
/*     */   {
/*  65 */     return getFileSignature(0, 2048);
/*     */   }
/*     */   
/*     */ 
/*     */   public FileSignature getFileSignature(int size)
/*     */     throws IOException
/*     */   {
/*  72 */     return getFileSignature(size, size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public FileSignature getFileSignature(int minSize, int maxSize)
/*     */     throws IOException
/*     */   {
/*  80 */     if ((this.signature != null) && (minSize <= 2048) && (2048 <= maxSize))
/*     */     {
/*  82 */       return this.signature;
/*     */     }
/*     */     
/*  85 */     FileSignature newSignature = getFileSignatureFromFile(minSize, maxSize);
/*  86 */     if (newSignature.isComplete()) {
/*  87 */       this.signature = newSignature;
/*     */     }
/*     */     
/*  90 */     return newSignature;
/*     */   }
/*     */   
/*     */   protected FileSignature getFileSignatureFromFile(int minSize, int maxSize) throws IOException {
/*  94 */     if (this.fileChannel == null) {
/*  95 */       this.fileChannel = FileChannel.open(this.filePath, new OpenOption[] { StandardOpenOption.READ });
/*     */     }
/*     */     
/*  98 */     return FileSignature.calculateFileSignature(this.fileChannel, minSize, maxSize);
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 103 */     if (this.fileChannel != null) {
/* 104 */       this.fileChannel.close();
/* 105 */       this.fileChannel = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileChannel takeFileChannelOwnership()
/*     */   {
/* 116 */     FileChannel takenRaf = this.fileChannel;
/* 117 */     this.fileChannel = null;
/* 118 */     return takenRaf;
/*     */   }
/*     */   
/*     */   public boolean metadataMatches(FileInfo fileInfo) {
/* 122 */     return (fileInfo.getSize() == getSize()) && (fileInfo.getCreated() == getCreated()) && (fileInfo.getLastModified() == getLastModified());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean metadataMatches(BasicFileAttributes attr)
/*     */   {
/* 128 */     return (attr.size() == getSize()) && (attr.creationTime().toMillis() == getCreated()) && (attr.lastModifiedTime().toMillis() == getLastModified());
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/input/tail/FileInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */