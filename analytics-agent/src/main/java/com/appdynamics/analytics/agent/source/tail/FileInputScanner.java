/*     */ package com.appdynamics.analytics.agent.source.tail;
/*     */ 
/*     */ import com.appdynamics.analytics.agent.input.tail.FileInfo;
/*     */ import com.appdynamics.analytics.agent.input.tail.FileSignature;
/*     */ import com.appdynamics.analytics.agent.input.tail.TailFileState;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.google.common.base.Function;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import org.joda.time.DateTime;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileInputScanner
/*     */ {
/*  20 */   private static final Logger log = LoggerFactory.getLogger(FileInputScanner.class);
/*     */   
/*     */   private final DirectoryPoller directoryPoller;
/*     */   
/*     */   private final Map<FileSignature, TailFileState> partialIdToTailStates;
/*     */   
/*     */   private final Map<FileSignature, TailFileState> fileIdToTailStates;
/*     */   
/*     */   private final Function<TailFileState, Boolean> ensureInputExists;
/*     */   
/*     */   public FileInputScanner(DirectoryPoller directoryPoller, Map<FileSignature, TailFileState> partialIdToTailStates, Map<FileSignature, TailFileState> fileIdToTailStates, Function<TailFileState, Boolean> ensureInputExists)
/*     */   {
/*  32 */     this.directoryPoller = directoryPoller;
/*  33 */     this.partialIdToTailStates = partialIdToTailStates;
/*  34 */     this.fileIdToTailStates = fileIdToTailStates;
/*  35 */     this.ensureInputExists = ensureInputExists;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TailFileState getTailFileStateFromFileInfo(FileInfo fileInfo)
/*     */     throws IOException
/*     */   {
/*  45 */     TailFileState tailState = null;
/*     */     
/*  47 */     FileSignature fileId = fileInfo.getFileSignature();
/*     */     
/*  49 */     if ((fileId.isComplete()) && (this.fileIdToTailStates.containsKey(fileId))) {
/*  50 */       tailState = (TailFileState)this.fileIdToTailStates.get(fileId);
/*     */     } else {
/*  52 */       tailState = lookupTailStateByPartialSignature(fileInfo);
/*     */     }
/*     */     
/*  55 */     return tailState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TailFileState lookupTailStateByPartialSignature(FileInfo fileInfo)
/*     */     throws IOException
/*     */   {
/*  66 */     TailFileState tailState = null;
/*     */     
/*     */ 
/*     */ 
/*  70 */     FileSignature partialSignature = null;
/*  71 */     for (TailFileState candidate : this.partialIdToTailStates.values()) {
/*  72 */       int signatureSize = candidate.getSignature().getSize();
/*  73 */       if (fileInfo.getSize() >= signatureSize) {
/*  74 */         partialSignature = fileInfo.getFileSignature(signatureSize);
/*     */         
/*  76 */         if (partialSignature.equals(candidate.getSignature())) {
/*  77 */           tailState = candidate;
/*  78 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  84 */     if ((tailState != null) && (fileInfo.getFileSignature().isComplete())) {
/*  85 */       this.partialIdToTailStates.remove(partialSignature);
/*  86 */       tailState.setSignature(fileInfo.getFileSignature());
/*  87 */       this.fileIdToTailStates.put(fileInfo.getFileSignature(), tailState);
/*     */     }
/*     */     
/*  90 */     return tailState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TailFileState addNewTailFileStateFromFileInfo(FileInfo fileInfo)
/*     */     throws IOException
/*     */   {
/* 100 */     TailFileState tailState = new TailFileState(fileInfo);
/*     */     
/* 102 */     if (fileInfo.getFileSignature().isComplete()) {
/* 103 */       this.fileIdToTailStates.put(fileInfo.getFileSignature(), tailState);
/*     */     } else {
/* 105 */       this.partialIdToTailStates.put(fileInfo.getFileSignature(), tailState);
/*     */     }
/* 107 */     return tailState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearFileCacheWithFileSignature(String fileSignature)
/*     */   {
/* 116 */     this.directoryPoller.clearFileCacheWithFileSignature(fileSignature);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void scan()
/*     */   {
/* 124 */     FileInfo[] files = this.directoryPoller.getFiles();
/* 125 */     log.debug("Starting ... checking {} files.", Integer.valueOf(files.length));
/*     */     
/* 127 */     long lastScanned = TimeKeeper.currentUtcTime().getMillis();
/*     */     
/* 129 */     FileInfo[] arr$ = files;int len$ = arr$.length; for (int i$ = 0; i$ < len$;) { FileInfo fileInfo = arr$[i$];
/* 130 */       TailFileState tailState = null;
/*     */       try {
/* 132 */         tailState = getTailFileStateFromFileInfo(fileInfo);
/*     */         
/*     */ 
/* 135 */         if (tailState == null) {
/* 136 */           tailState = addNewTailFileStateFromFileInfo(fileInfo);
/*     */         } else {
/* 138 */           tailState.setLastScanned(lastScanned);
/*     */         }
/*     */         
/*     */ 
/* 142 */         if (tailState.getLastReadPosition() < fileInfo.getSize()) {
/* 143 */           tailState.ensureFileChannelOwnership(fileInfo);
/* 144 */           if (((Boolean)this.ensureInputExists.apply(tailState)).booleanValue()) {
/* 145 */             log.info("Added file tail for [{}] with file signature [{}]", fileInfo.getFilename(), fileInfo.getFileSignature());
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/* 154 */           fileInfo.close();
/* 155 */           if (tailState != null) {
/* 156 */             tailState.close();
/*     */           }
/*     */         } catch (IOException except) {
/* 159 */           log.error(String.format("Failed to close handle on file [%s].", new Object[] { fileInfo.getFilename() }), except);
/*     */         }
/* 129 */         i$++;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (IOException except)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */         log.error(String.format("Encountered an error with file [%s].  It will be skipped.", new Object[] { fileInfo.getFilename() }), except);
/*     */       }
/*     */       finally {
/*     */         try {
/* 154 */           fileInfo.close();
/* 155 */           if (tailState != null) {
/* 156 */             tailState.close();
/*     */           }
/*     */         } catch (IOException except) {
/* 159 */           log.error(String.format("Failed to close handle on file [%s].", new Object[] { fileInfo.getFilename() }), except);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/tail/FileInputScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */