/*     */ package com.appdynamics.analytics.pipeline.framework;
/*     */ 
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*     */ import com.appdynamics.common.util.health.Countable;
/*     */ import com.appdynamics.common.util.item.Item;
/*     */ import com.appdynamics.common.util.lifecycle.LifecycleAware;
/*     */ import com.appdynamics.common.util.lifecycle.LifecycleHelper;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class Pipeline<IN>
/*     */   implements Item<Object>, Callable<Void>, LifecycleAware
/*     */ {
/*  28 */   private static final Logger log = LoggerFactory.getLogger(Pipeline.class);
/*  29 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Pipeline)) return false; Pipeline<?> other = (Pipeline)o;Object this$pipelineId = this.pipelineId;Object other$pipelineId = other.pipelineId;return this$pipelineId == null ? other$pipelineId == null : this$pipelineId.equals(other$pipelineId); } public int hashCode() { int PRIME = 31;int result = 1;Object $pipelineId = this.pipelineId;result = result * 31 + ($pipelineId == null ? 0 : $pipelineId.hashCode());return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pipeline(Object pipelineId, PipelineStage[] pipelineStages)
/*     */   {
/*  37 */     this.pipelineId = pipelineId;
/*  38 */     this.pipelineStages = pipelineStages;
/*  39 */     this.state = new AtomicReference(State.CREATED);
/*  40 */     this.currentPipelineCallerThread = new AtomicReference();
/*     */   }
/*     */   
/*     */   public Object getId()
/*     */   {
/*  45 */     return this.pipelineId;
/*     */   }
/*     */   
/*     */   public State getState() {
/*  49 */     return (State)this.state.get();
/*     */   }
/*     */   
/*     */   PipelineStage[] getPipelineStages() {
/*  53 */     return this.pipelineStages;
/*     */   }
/*     */   
/*     */ 
/*     */   public void start()
/*     */   {
/*  59 */     log.debug("Starting pipeline [{}]", getId());
/*     */     
/*  61 */     LifecycleHelper.startAll(Lists.reverse(Arrays.asList(this.pipelineStages)));
/*     */     
/*  63 */     this.state.set(State.IDLE);
/*  64 */     log.info("Started pipeline [{}]", getId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final Object pipelineId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final PipelineStage[] pipelineStages;
/*     */   
/*     */ 
/*     */ 
/*     */   private final AtomicReference<State> state;
/*     */   
/*     */ 
/*     */ 
/*     */   private final AtomicReference<Thread> currentPipelineCallerThread;
/*     */   
/*     */ 
/*     */ 
/*     */   public void call(IN input)
/*     */   {
/*  90 */     PipelineStage<IN, ?> p = this.pipelineStages[0];
/*     */     
/*  92 */     if (!this.currentPipelineCallerThread.compareAndSet(null, Thread.currentThread())) {
/*  93 */       throw new IllegalStateException("Pipeline [" + getId() + "] is being executed by another thread." + " Concurrent access is not allowed");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  98 */       if (!this.state.compareAndSet(State.IDLE, State.RUNNING)) {
/*  99 */         throw new IllegalStateException("Pipeline [" + getId() + "] may already have been stopped." + " Current state is not [" + State.IDLE.name() + "]");
/*     */       }
/*     */       
/*     */ 
/* 103 */       String id = String.valueOf(getId());
/* 104 */       String originalThreadName = Thread.currentThread().getName();
/* 105 */       String newThreadName = originalThreadName + " (" + id + ")";
/*     */       try {
/* 107 */         Thread.currentThread().setName(newThreadName);
/* 108 */         doCall(input, p, id);
/*     */       } finally {
/* 110 */         Thread.currentThread().setName(originalThreadName);
/* 111 */         this.state.set(State.IDLE);
/*     */       }
/*     */     } finally {
/* 114 */       this.currentPipelineCallerThread.set(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Void call()
/*     */   {
/* 128 */     call(null);
/* 129 */     return null;
/*     */   }
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
/*     */ 
/*     */ 
/*     */   public void managedCall(IN input)
/*     */   {
/* 145 */     PipelineStage<IN, ?> p = this.pipelineStages[0];
/*     */     
/* 147 */     this.currentPipelineCallerThread.lazySet(Thread.currentThread());
/*     */     try {
/* 149 */       this.state.lazySet(State.RUNNING);
/* 150 */       Object id = getId();
/*     */       try {
/* 152 */         doCall(input, p, id);
/*     */       } finally {
/* 154 */         this.state.lazySet(State.IDLE);
/*     */       }
/*     */     } finally {
/* 157 */       this.currentPipelineCallerThread.lazySet(null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void doCall(IN input, PipelineStage<IN, ?> stage, Object id) {
/*     */     try {
/* 163 */       log.trace("Pipeline [{}] call starting with input [{}]", id, input);
/* 164 */       if ((stage instanceof Countable)) {
/* 165 */         ((Countable)stage).incrementCount();
/*     */       }
/* 167 */       stage.process(input);
/* 168 */       log.trace("Pipeline [{}] call completed", id);
/*     */     }
/*     */     catch (Throwable t) {
/* 171 */       if ((this.state.get() != State.RUNNING) && ((Throwables.getRootCause(t) instanceof InterruptedException))) {
/* 172 */         log.debug("Pipeline [" + id + "] call was interrupted", t);
/*     */       } else {
/* 174 */         log.error("Pipeline [" + id + "] call exited abruptly", t);
/* 175 */         throw t;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */   {
/* 188 */     log.debug("Stopping pipeline [{}]", getId());
/*     */     
/* 190 */     this.state.set(State.AWAITING_IDLE);
/*     */     Thread executor;
/* 192 */     while ((executor = (Thread)this.currentPipelineCallerThread.get()) != null) {
/* 193 */       log.debug("Pipeline thread [{}] will be interrupted", executor.getName());
/* 194 */       executor.interrupt();
/*     */       try {
/* 196 */         log.debug("Waiting for pipeline thread [{}] to complete", executor.getName());
/* 197 */         Thread.sleep(5L);
/*     */       } catch (InterruptedException e) {
/* 199 */         log.warn("Pipeline stop has been interrupted. Abandoning pipeline stop", e);
/* 200 */         Thread.currentThread().interrupt();
/* 201 */         return;
/*     */       }
/*     */     }
/*     */     
/* 205 */     LifecycleHelper.stopAll(Arrays.asList(this.pipelineStages));
/* 206 */     this.state.set(State.STOPPED);
/* 207 */     log.info("Stopped pipeline [{}]", getId());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 212 */     return "Pipeline{id=" + getId() + '}';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum State
/*     */   {
/* 225 */     CREATED, 
/* 226 */     IDLE, 
/* 227 */     RUNNING, 
/* 228 */     AWAITING_IDLE, 
/* 229 */     STOPPED;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/Pipeline.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */