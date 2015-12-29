/*    */ package com.appdynamics.analytics.queue;
/*    */ 
/*    */ import com.appdynamics.analytics.message.MessageSources;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.appdynamics.common.util.health.QueueCapacityHealthCheck;
/*    */ import com.google.common.base.Suppliers;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import java.math.BigDecimal;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueuesModule
/*    */   extends Module<QueuesModuleConfiguration>
/*    */ {
/* 25 */   private static final Logger log = LoggerFactory.getLogger(QueuesModule.class);
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   public Queues makeQueues(MessageSources messageSources, ConsolidatedHealthCheck healthCheck) {
/* 30 */     Queues queues = new Queues();
/* 31 */     QueuesModuleConfiguration moduleConfiguration = (QueuesModuleConfiguration)getConfiguration();
/* 32 */     List<QueueConfiguration> staticQueueConfigurations = moduleConfiguration.getQueues();
/*    */     
/* 34 */     if (!staticQueueConfigurations.isEmpty()) {
/* 35 */       log.debug("Attempting to create [{}] statically defined queues", Integer.valueOf(staticQueueConfigurations.size()));
/* 36 */       for (QueueConfiguration queueConfiguration : staticQueueConfigurations) {
/* 37 */         BlockingQueue<Object> queue = queues.findOrSetupQueue(queueConfiguration);
/* 38 */         if (queueConfiguration.isMessageSource()) {
/* 39 */           QueueSource<Object, Object> queueSource = new QueueSource(queueConfiguration.getQueueName(), queue);
/*    */           
/* 41 */           messageSources.register(queueConfiguration.getQueueName(), Suppliers.ofInstance(queueSource));
/* 42 */           log.debug("Registered [{}] queue as a message source", queueConfiguration.getQueueName());
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 47 */     QueueCapacityHealthCheck capacityHealthCheck = new QueueCapacityHealthCheck(getUri(), ((QueuesModuleConfiguration)getConfiguration()).getAlertRatio().doubleValue(), queues.getQueues());
/*    */     
/*    */ 
/* 50 */     healthCheck.register(capacityHealthCheck);
/*    */     
/* 52 */     return queues;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/queue/QueuesModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */