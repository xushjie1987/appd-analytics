/*    */ package com.appdynamics.analytics.pipeline.mem;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.queue.QueueConfiguration;
/*    */ import com.appdynamics.analytics.queue.Queues;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UntapStageFactory
/*    */   extends AbstractQueueFactory<Void, Object>
/*    */ {
/*    */   public UntapStage<Object> create(PipelineStageParameters<Object> parameters)
/*    */   {
/* 24 */     QueueConfiguration queueConfiguration = (QueueConfiguration)extract(parameters);
/* 25 */     BlockingQueue<Object> queue = this.queues.findOrSetupQueue(queueConfiguration);
/*    */     
/* 27 */     return new UntapStage(parameters, queue);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/mem/UntapStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */