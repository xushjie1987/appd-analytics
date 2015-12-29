/*     */ package com.appdynamics.analytics.processor.kafka.server;
/*     */ 
/*     */ import com.appdynamics.common.util.health.SimpleHealthCheck;
/*     */ import com.codahale.metrics.health.HealthCheck.Result;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.JMException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.ReflectionException;
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
/*     */ public class KafkaBrokerHealthCheck
/*     */   extends SimpleHealthCheck
/*     */ {
/*     */   static final String JMX_NAME_BYTES_IN = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec";
/*     */   static final String JMX_NAME_BYTES_OUT = "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec";
/*     */   static final String JMX_NAME_MSGS_IN = "kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec";
/*     */   static final String JMX_NAME_FAILED_FETCH = "kafka.server:type=BrokerTopicMetrics,name=FailedFetchRequestsPerSec";
/*     */   static final String JMX_NAME_FAILED_PRODUCE = "kafka.server:type=BrokerTopicMetrics,name=FailedProduceRequestsPerSec";
/*     */   static final String JMX_NAME_LEADER_COUNT = "kafka.server:type=ReplicaManager,name=LeaderCount";
/*     */   static final String JMX_NAME_PARTITION_COUNT = "kafka.server:type=ReplicaManager,name=PartitionCount";
/*     */   private final MBeanServer mBeanServer;
/*     */   
/*     */   KafkaBrokerHealthCheck()
/*     */   {
/*  50 */     this(ManagementFactory.getPlatformMBeanServer());
/*     */   }
/*     */   
/*     */   KafkaBrokerHealthCheck(MBeanServer mBeanServer) {
/*  54 */     super("KafkaBroker");
/*  55 */     this.mBeanServer = mBeanServer;
/*     */   }
/*     */   
/*     */   public HealthCheck.Result check()
/*     */   {
/*     */     try
/*     */     {
/*  62 */       Long countBytesIn = (Long)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec"), "Count");
/*  63 */       Double bytesInRate = (Double)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec"), "FiveMinuteRate");
/*     */       
/*  65 */       Long countBytesOut = (Long)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec"), "Count");
/*     */       
/*  67 */       Double bytesOutRate = (Double)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec"), "FiveMinuteRate");
/*     */       
/*  69 */       Double msgInRate = (Double)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec"), "FiveMinuteRate");
/*     */       
/*  71 */       Double failedFetchReqRate = (Double)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=FailedFetchRequestsPerSec"), "FiveMinuteRate");
/*     */       
/*  73 */       Double failedProduceReqRate = (Double)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=BrokerTopicMetrics,name=FailedProduceRequestsPerSec"), "FiveMinuteRate");
/*     */       
/*     */ 
/*  76 */       Integer numPartitions = (Integer)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=ReplicaManager,name=PartitionCount"), "Value");
/*     */       
/*  78 */       Integer numLeader = (Integer)this.mBeanServer.getAttribute(new ObjectName("kafka.server:type=ReplicaManager,name=LeaderCount"), "Value");
/*     */       
/*     */ 
/*  81 */       String msg = String.format("CountBytesIn: %d bytes, CountBytesOut: %d bytes, FiveMinRateIn: %f bytes/s, FiveMinRateOut: %f bytes/s, FiveMinMsgRateIn: %f msgs/s, FiveMinFailedFetchReqsRate: %f reqs/s, FiveMinFailedProduceReqsRate: %f reqs/s, NumberOfAssignedPartitions: %d, NumberOfLeaderPartitions: %d", new Object[] { countBytesIn, countBytesOut, bytesInRate, bytesOutRate, msgInRate, failedFetchReqRate, failedProduceReqRate, numPartitions, numLeader });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */       if (numPartitions.intValue() == 0)
/* 102 */         return HealthCheck.Result.unhealthy("Not assigned any partitions. %s", new Object[] { msg });
/* 103 */       if (numLeader.intValue() == 0) {
/* 104 */         return HealthCheck.Result.unhealthy("Not leader for any partitions. %s", new Object[] { msg });
/*     */       }
/* 106 */       return HealthCheck.Result.healthy(msg);
/*     */     }
/*     */     catch (MBeanException|AttributeNotFoundException|InstanceNotFoundException|ReflectionException|MalformedObjectNameException e)
/*     */     {
/* 110 */       return HealthCheck.Result.unhealthy(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/server/KafkaBrokerHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */