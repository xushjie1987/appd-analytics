/*    */ package com.appdynamics.analytics.pipeline.mem;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.appdynamics.analytics.queue.QueueConfiguration;
/*    */ import com.appdynamics.analytics.queue.Queues;
/*    */ import com.google.inject.Inject;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractQueueFactory<I, O>
/*    */   extends AbstractPipelineStageFactory<I, O, Object, QueueConfiguration>
/*    */ {
/* 29 */   private static final Logger log = LoggerFactory.getLogger(AbstractQueueFactory.class);
/*    */   @Inject
/*    */   volatile Queues queues;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/mem/AbstractQueueFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */