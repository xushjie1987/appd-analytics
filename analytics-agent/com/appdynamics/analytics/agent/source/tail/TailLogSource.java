/*     */ package com.appdynamics.analytics.agent.source.tail;
/*     */ 
/*     */ import com.appdynamics.analytics.agent.input.tail.FileSignature;
/*     */ import com.appdynamics.analytics.agent.input.tail.TailFileState;
/*     */ import com.appdynamics.analytics.agent.input.tail.TailLogInput;
/*     */ import com.appdynamics.analytics.agent.source.LogComponentFactory;
/*     */ import com.appdynamics.analytics.agent.source.LogSource;
/*     */ import com.appdynamics.analytics.agent.source.LogWatermarkState;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*     */ import com.appdynamics.common.util.priority.PriorityExecutorServiceFactory;
/*     */ import com.codahale.metrics.health.HealthCheck.Result;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.joda.time.DateTime;
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
/*     */ public class TailLogSource
/*     */   extends LogSource
/*     */ {
/*  42 */   private static final Logger log = LoggerFactory.getLogger(TailLogSource.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */   final ArrayList<TailLogInput> newInputs = new ArrayList();
/*     */   
/*     */ 
/*     */   long nextDiscardTailingStateTime;
/*     */   
/*     */ 
/*     */   final Map<TailFileState, TailLogInput> inputs;
/*     */   
/*     */ 
/*     */   final Map<FileSignature, TailFileState> partialIdToTailStates;
/*     */   
/*     */ 
/*     */   final Map<FileSignature, TailFileState> fileIdToTailStates;
/*     */   
/*     */   final PriorityBlockingQueue<Runnable> pendingInputQueue;
/*     */   
/*     */   final ThreadPoolExecutor inputThreadPool;
/*     */   
/*     */   Future scannerFuture;
/*     */   
/*     */   DirectoryPoller directoryPoller;
/*     */   
/*     */   InputScannerThread inputScannerThread;
/*     */   
/*     */   FileInputScanner inputScanner;
/*     */   
/*     */ 
/*     */   public TailLogSource(TailLogSourceConfiguration configuration, LogComponentFactory factory, ExecutorService executorService)
/*     */   {
/*  77 */     super(String.format("TailLogSource [%s]", new Object[] { configuration.getId() }), configuration, factory, executorService);
/*     */     
/*  79 */     this.inputs = new ConcurrentHashMap();
/*  80 */     this.partialIdToTailStates = new ConcurrentHashMap();
/*  81 */     this.fileIdToTailStates = new ConcurrentHashMap();
/*     */     
/*  83 */     this.pendingInputQueue = new PriorityBlockingQueue();
/*  84 */     this.inputThreadPool = PriorityExecutorServiceFactory.makeThreadPoolExecutor(configuration.getMaximumInputPoolSize(), configuration.getMaximumInputPoolSize(), 60L, TimeUnit.SECONDS, this.pendingInputQueue, ConcurrencyHelper.newDaemonThreadFactory("tail-log-source-thread-" + configuration.getId() + "-%d"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */     this.inputThreadPool.allowCoreThreadTimeOut(true);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void start()
/*     */   {
/*  98 */     if (!isRunning()) {
/*  99 */       super.start();
/*     */       
/* 101 */       updateNextDiscardTailingStateTime();
/* 102 */       startInputScannerThread();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 109 */     if (isRunning()) {
/* 110 */       super.stop();
/*     */       
/* 112 */       stopInputScannerThread();
/*     */       
/*     */ 
/* 115 */       this.pendingInputQueue.clear();
/*     */       
/*     */ 
/* 118 */       for (TailLogInput input : this.inputs.values()) {
/* 119 */         input.stop();
/*     */       }
/* 121 */       this.inputs.clear();
/*     */       
/*     */ 
/* 124 */       ConcurrencyHelper.stop(this.inputThreadPool, log);
/*     */       
/*     */ 
/*     */ 
/* 128 */       this.directoryPoller = null;
/* 129 */       this.inputScanner = null;
/* 130 */       this.scannerFuture = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void startNewInputs()
/*     */   {
/* 138 */     for (TailLogInput input : this.newInputs) {
/* 139 */       input.start();
/*     */     }
/*     */     
/* 142 */     this.newInputs.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void stopIdleInputs()
/*     */   {
/* 149 */     long currentTime = TimeKeeper.currentUtcTime().getMillis();
/*     */     
/* 151 */     for (TailLogInput input : this.inputs.values()) {
/* 152 */       if (input.isTailing()) {
/* 153 */         TailFileState tailState = input.getTailState();
/* 154 */         long idleTime = currentTime - tailState.getLastReadTimestamp();
/* 155 */         if (idleTime >= getTailLogSourceConfiguration().getStopIdleInputsTimeout().toMilliseconds()) {
/* 156 */           log.info("Input [{}] with signature [{}] has been idle for [{}] seconds, so it will be stopped", new Object[] { tailState.getFilename(), tailState.getSignature(), Long.valueOf(idleTime / 1000L) });
/*     */           
/* 158 */           this.inputs.remove(tailState);
/* 159 */           input.stop();
/* 160 */           this.inputScanner.clearFileCacheWithFileSignature(tailState.getSignature().getSignature());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void checkDiscardOldTailStates(Map<FileSignature, TailFileState> tailStatesMap) {
/* 167 */     long currentTime = TimeKeeper.currentUtcTime().getMillis();
/*     */     
/* 169 */     for (TailFileState tailFileState : tailStatesMap.values()) {
/* 170 */       long scannedTime = currentTime - tailFileState.getLastScanned();
/* 171 */       long discardTimeout = getTailLogSourceConfiguration().getDiscardTailingStateTimeout().toMilliseconds();
/* 172 */       if ((scannedTime >= discardTimeout) && 
/* 173 */         (!this.inputs.containsKey(tailFileState))) {
/* 174 */         tailStatesMap.remove(tailFileState.getSignature());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void checkDiscardOldTailStates()
/*     */   {
/* 181 */     long currentTime = TimeKeeper.currentUtcTime().getMillis();
/*     */     
/* 183 */     if (currentTime >= this.nextDiscardTailingStateTime) {
/* 184 */       checkDiscardOldTailStates(this.partialIdToTailStates);
/* 185 */       checkDiscardOldTailStates(this.fileIdToTailStates);
/* 186 */       updateNextDiscardTailingStateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void initializeDirectoryPoller() {
/* 191 */     this.directoryPoller = this.factory.createDirectoryPoller(getTailLogSourceConfiguration().getSourcePath());
/*     */   }
/*     */   
/*     */   protected void initializeInputScanner() {
/* 195 */     this.inputScanner = this.factory.createFileInputScanner(this.directoryPoller, this.partialIdToTailStates, this.fileIdToTailStates, new Function()
/*     */     {
/*     */ 
/*     */ 
/*     */       public Boolean apply(TailFileState tailState)
/*     */       {
/*     */ 
/* 202 */         if (!TailLogSource.this.inputs.containsKey(tailState)) {
/* 203 */           TailLogSource.this.addTailLogInput(tailState);
/* 204 */           return Boolean.valueOf(true);
/*     */         }
/* 206 */         return Boolean.valueOf(false);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   protected TailLogInput addTailLogInput(TailFileState tailState)
/*     */   {
/* 214 */     TailLogInput input = null;
/*     */     try {
/* 216 */       input = new TailLogInput(tailState, getTailLogSourceConfiguration().getTailLogInputConfiguration(), this.factory, this.inputThreadPool);
/*     */       
/* 218 */       this.inputs.put(tailState, input);
/* 219 */       this.newInputs.add(input);
/*     */     } catch (Exception except) {
/* 221 */       log.error(String.format("Failed to create log input [%s].", new Object[] { tailState.getFilename() }), except);
/*     */     }
/* 223 */     return input;
/*     */   }
/*     */   
/*     */   private TailLogSourceConfiguration getTailLogSourceConfiguration() {
/* 227 */     return (TailLogSourceConfiguration)getConfiguration();
/*     */   }
/*     */   
/*     */   void startInputScannerThread() {
/* 231 */     initializeDirectoryPoller();
/* 232 */     initializeInputScanner();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 237 */     this.inputScannerThread = new InputScannerThread();
/* 238 */     this.scannerFuture = this.executorService.submit(this.inputScannerThread);
/*     */   }
/*     */   
/*     */   void stopInputScannerThread() {
/* 242 */     if (this.scannerFuture != null) {
/* 243 */       ConcurrencyHelper.cancel(this.scannerFuture, 1000L, true, log);
/*     */     }
/*     */   }
/*     */   
/*     */   void updateNextDiscardTailingStateTime() {
/* 248 */     long currentTime = TimeKeeper.currentUtcTime().getMillis();
/* 249 */     updateNextDiscardTailingStateTime(currentTime);
/*     */   }
/*     */   
/*     */   void updateNextDiscardTailingStateTime(long baseTime) {
/* 253 */     this.nextDiscardTailingStateTime = (baseTime + getTailLogSourceConfiguration().getDiscardTailingStateCheckInterval().toMilliseconds());
/*     */   }
/*     */   
/*     */   public TailLogWatermarkState makeTailLogWatermarkState()
/*     */   {
/* 258 */     List<TailFileState> tailFileStates = Lists.newArrayList(Iterables.concat(this.partialIdToTailStates.values(), this.fileIdToTailStates.values()));
/*     */     
/* 260 */     return new TailLogWatermarkState(tailFileStates);
/*     */   }
/*     */   
/*     */   public HealthCheck.Result check()
/*     */   {
/* 265 */     StringBuilder sb = new StringBuilder();
/* 266 */     boolean allRunning = true;
/* 267 */     int j = 0;
/*     */     
/* 269 */     for (TailLogInput input : this.inputs.values()) {
/* 270 */       HealthCheck.Result inputHealth = input.check();
/* 271 */       if (j > 0) {
/* 272 */         sb.append(",    ");
/*     */       }
/* 274 */       j++;
/* 275 */       sb.append('[').append(inputHealth.getMessage()).append(' ').append(inputHealth.isHealthy() ? " running" : " idle").append(']');
/*     */       
/*     */ 
/*     */ 
/* 279 */       allRunning = (allRunning) && (inputHealth.isHealthy());
/*     */     }
/*     */     
/* 282 */     String msg = sb.length() == 0 ? "idle" : sb.toString();
/* 283 */     return allRunning ? HealthCheck.Result.healthy(msg) : HealthCheck.Result.unhealthy(msg);
/*     */   }
/*     */   
/*     */   public LogWatermarkState getWatermarkState()
/*     */   {
/* 288 */     return makeTailLogWatermarkState();
/*     */   }
/*     */   
/*     */   public void setWatermarkState(LogWatermarkState watermarkState)
/*     */   {
/* 293 */     if ((watermarkState instanceof TailLogWatermarkState)) {
/* 294 */       TailLogWatermarkState tailLogWatermarkState = (TailLogWatermarkState)watermarkState;
/*     */       
/* 296 */       for (TailFileState tailFileState : tailLogWatermarkState.getTailFileStates()) {
/* 297 */         log.debug("Log file [{}] with signature [{}] resuming from last read position [{}]", new Object[] { tailFileState.getFilename(), tailFileState.getSignature().getSignature(), Long.valueOf(tailFileState.getLastReadPosition()) });
/*     */         
/*     */ 
/* 300 */         FileSignature signature = tailFileState.getSignature();
/* 301 */         if (signature.isComplete()) {
/* 302 */           this.fileIdToTailStates.put(signature, tailFileState);
/*     */         } else {
/* 304 */           this.partialIdToTailStates.put(signature, tailFileState);
/*     */         }
/*     */       }
/*     */     } else {
/* 308 */       throw new IllegalArgumentException("Expected TailLogWatermarkState");
/*     */     }
/*     */   }
/*     */   
/*     */   class InputScannerThread implements Runnable
/*     */   {
/*     */     InputScannerThread() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/* 318 */         while (TailLogSource.this.isRunning()) {
/* 319 */           TailLogSource.this.inputScanner.scan();
/* 320 */           TailLogSource.this.stopIdleInputs();
/* 321 */           TailLogSource.this.startNewInputs();
/* 322 */           TailLogSource.this.checkDiscardOldTailStates();
/*     */           
/* 324 */           TailLogSource.this.sleepWhileRunning(TailLogSource.this.getTailLogSourceConfiguration().getDirectoryPollingInterval().toMilliseconds());
/*     */         }
/*     */       } catch (Throwable t) {
/* 327 */         TailLogSource.log.error("Error occurred at [{}] while scanning and tailing log sources", TailLogSource.this.getName(), t);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/tail/TailLogSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */