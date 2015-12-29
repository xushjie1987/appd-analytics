/*    */ package com.appdynamics.analytics.pipeline.util;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.common.util.concurrent.SingleWriterCounter;
/*    */ import com.appdynamics.common.util.health.Countable;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractPipelineStage<IN, OUT>
/*    */   implements PipelineStage<IN, OUT>, Countable
/*    */ {
/* 19 */   private static final Logger log = LoggerFactory.getLogger(AbstractPipelineStage.class);
/*    */   protected final Object pipelineId;
/*    */   protected final PipelineStage<? super OUT, ?> optionalNextStage;
/*    */   private final SingleWriterCounter callCounter;
/*    */   
/*    */   protected AbstractPipelineStage(PipelineStageParameters<OUT> parameters)
/*    */   {
/* 26 */     this.pipelineId = parameters.getPipelineId();
/* 27 */     this.optionalNextStage = parameters.getOptionalNextStage();
/* 28 */     this.callCounter = new SingleWriterCounter();
/*    */   }
/*    */   
/*    */   public final long getCount()
/*    */   {
/* 33 */     return this.callCounter.get();
/*    */   }
/*    */   
/*    */   public final long incrementCount()
/*    */   {
/* 38 */     return this.callCounter.incrementAndGet();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void start()
/*    */   {
/* 46 */     log.debug("Starting pipeline stage [{}]", printableName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected final void invokeNext(OUT input)
/*    */   {
/* 55 */     if (this.optionalNextStage == null) {
/* 56 */       return;
/*    */     }
/* 58 */     if (log.isTraceEnabled()) {
/* 59 */       log.trace("Invoking stage [{}] with [{}]", this.optionalNextStage.getClass().getName(), input);
/*    */     }
/* 61 */     if ((this.optionalNextStage instanceof Countable)) {
/* 62 */       ((Countable)this.optionalNextStage).incrementCount();
/*    */     }
/* 64 */     this.optionalNextStage.process(input);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void stop()
/*    */   {
/* 72 */     log.debug("Stopped pipeline stage [{}]", printableName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected final String printableName()
/*    */   {
/* 79 */     String name = getClass().getSimpleName();
/*    */     
/* 81 */     if (name.length() == 0) {
/* 82 */       name = getClass().getName();
/*    */     }
/*    */     
/* 85 */     if (this.pipelineId != null) {
/* 86 */       return String.valueOf(this.pipelineId) + " (" + name + ")";
/*    */     }
/* 88 */     return name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 97 */     return printableName();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/util/AbstractPipelineStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */