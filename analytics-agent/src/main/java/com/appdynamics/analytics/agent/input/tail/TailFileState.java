/*     */ package com.appdynamics.analytics.agent.input.tail;
/*     */ 
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.joda.time.DateTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TailFileState
/*     */   implements AutoCloseable
/*     */ {
/*     */   public String toString()
/*     */   {
/*  23 */     return "TailFileState(signature=" + getSignature() + ", filename=" + getFilename() + ", lastScanned=" + getLastScanned() + ", lastReadPosition=" + getLastReadPosition() + ", lastModifiedTimestamp=" + getLastModifiedTimestamp() + ", lastReadTimestamp=" + getLastReadTimestamp() + ")";
/*     */   }
/*     */   
/*  26 */   private static final AtomicInteger globalUniqueIdCounter = new AtomicInteger(0);
/*     */   @JsonIgnore
/*     */   private final int uniqueId;
/*     */   @JsonIgnore
/*     */   volatile FileChannel fileChannel;
/*     */   volatile FileSignature signature;
/*     */   volatile String filename;
/*     */   
/*  34 */   public int getUniqueId() { return this.uniqueId; }
/*     */   
/*     */ 
/*  37 */   public FileChannel getFileChannel() { return this.fileChannel; } public void setFileChannel(FileChannel fileChannel) { this.fileChannel = fileChannel; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  42 */   public FileSignature getSignature() { return this.signature; } public void setSignature(FileSignature signature) { this.signature = signature; }
/*     */   
/*     */ 
/*     */   volatile long lastScanned;
/*     */   volatile long lastReadPosition;
/*     */   
/*  48 */   public String getFilename() { return this.filename; } public void setFilename(String filename) { this.filename = filename; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   public long getLastScanned() { return this.lastScanned; } public void setLastScanned(long lastScanned) { this.lastScanned = lastScanned; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   public long getLastReadPosition() { return this.lastReadPosition; } public void setLastReadPosition(long lastReadPosition) { this.lastReadPosition = lastReadPosition; }
/*     */   
/*     */   @JsonIgnore
/*     */   long lastModifiedTimestamp;
/*     */   @JsonIgnore
/*     */   volatile long lastReadTimestamp;
/*  64 */   public long getLastModifiedTimestamp() { return this.lastModifiedTimestamp; } public void setLastModifiedTimestamp(long lastModifiedTimestamp) { this.lastModifiedTimestamp = lastModifiedTimestamp; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */   public long getLastReadTimestamp() { return this.lastReadTimestamp; } public void setLastReadTimestamp(long lastReadTimestamp) { this.lastReadTimestamp = lastReadTimestamp; }
/*     */   
/*     */ 
/*     */ 
/*     */   protected TailFileState()
/*     */   {
/*  76 */     this.uniqueId = globalUniqueIdCounter.addAndGet(1);
/*     */   }
/*     */   
/*     */   public TailFileState(FileInfo fileInfo) throws IOException {
/*  80 */     this();
/*  81 */     initializeFromFileInfo(fileInfo);
/*     */   }
/*     */   
/*     */   protected void initializeFromFileInfo(FileInfo fileInfo) throws IOException {
/*  85 */     this.signature = fileInfo.getFileSignature();
/*  86 */     this.filename = fileInfo.getFilename();
/*  87 */     this.lastScanned = TimeKeeper.currentUtcTime().getMillis();
/*  88 */     this.lastReadPosition = 0L;
/*  89 */     this.lastReadTimestamp = TimeKeeper.currentUtcTime().getMillis();
/*  90 */     this.lastModifiedTimestamp = fileInfo.getLastModified();
/*  91 */     this.fileChannel = fileInfo.takeFileChannelOwnership();
/*     */   }
/*     */   
/*     */   public void ensureFileChannelOwnership(FileInfo fileInfo) {
/*  95 */     if (this.fileChannel == null) {
/*  96 */       this.fileChannel = fileInfo.takeFileChannelOwnership();
/*     */       
/*  98 */       this.lastModifiedTimestamp = fileInfo.getLastModified();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void finalize() throws IOException {
/* 103 */     close();
/*     */   }
/*     */   
/*     */   public FileChannel takeRandomAccessFileOwnership() {
/* 107 */     FileChannel takenRaf = this.fileChannel;
/* 108 */     this.fileChannel = null;
/* 109 */     return takenRaf;
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 114 */     if (this.fileChannel != null) {
/* 115 */       this.fileChannel.close();
/* 116 */       this.fileChannel = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 126 */     return getUniqueId();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 131 */     return (o != null) && ((o instanceof TailFileState)) && (getUniqueId() == ((TailFileState)o).getUniqueId());
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/input/tail/TailFileState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */