/*    */ package com.appdynamics.analytics.pipeline.mem;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*    */ import com.appdynamics.common.util.exception.Exceptions;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public class TapStage<I>
/*    */   extends AbstractPipelineStage<I, I>
/*    */ {
/* 21 */   private static final Logger log = LoggerFactory.getLogger(TapStage.class);
/*    */   private final BlockingQueue<I> queue;
/*    */   
/*    */   public TapStage(PipelineStageParameters<I> parameters, BlockingQueue<I> queue)
/*    */   {
/* 26 */     super(parameters);
/* 27 */     this.queue = queue;
/*    */   }
/*    */   
/*    */   public BlockingQueue<I> getQueue() {
/* 31 */     return this.queue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void process(I input)
/*    */   {
/*    */     try
/*    */     {
/* 42 */       boolean submitted = false;
/* 43 */       for (int i = 0; !submitted; i++) {
/* 44 */         submitted = this.queue.offer(input, 10L, TimeUnit.SECONDS);
/*    */         
/* 46 */         if ((!submitted) && (i % 6 == 0)) {
/* 47 */           log.warn("The queue into which [{}] is configured to offer messages appears to be full. Attempts will be made to retry until space becomes available", printableName());
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (InterruptedException e) {
/* 52 */       Exceptions.rethrowAsRuntimeException(e);
/*    */     }
/* 54 */     invokeNext(input);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/mem/TapStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */