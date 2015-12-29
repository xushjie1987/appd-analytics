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
/*    */ 
/*    */ public class TapStageFactory
/*    */   extends AbstractQueueFactory<Object, Object>
/*    */ {
/*    */   public TapStage<Object> create(PipelineStageParameters<Object> parameters)
/*    */   {
/* 25 */     QueueConfiguration queueConfiguration = (QueueConfiguration)extract(parameters);
/* 26 */     BlockingQueue<Object> queue = this.queues.findOrSetupQueue(queueConfiguration);
/*    */     
/* 28 */     return new TapStage(parameters, queue);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/mem/TapStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */