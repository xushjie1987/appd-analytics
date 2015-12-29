/*    */ package com.appdynamics.analytics.processor.kafka;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.admin.ActionType;
/*    */ import com.appdynamics.analytics.processor.admin.Locator;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ZookeeperConstants;
/*    */ import org.apache.curator.utils.ZKPaths;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class EventServiceKafkaConstants
/*    */ {
/*    */   public static final String PROP_ZOOKEEPER_CONNECT_CSV = "zookeeper.connect";
/*    */   public static final String PROP_KAFKA_TOPIC_RETENTION_TIME_MILLIS = "retention.ms";
/* 36 */   public static final byte[] MESSAGE_KEY = new byte[0];
/*    */   
/*    */ 
/*    */ 
/* 40 */   public static final String KAFKA_ZOOKEEPER_PATH = ZKPaths.makePath(ZookeeperConstants.ZK_BASE_PATH, "kafka");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int KAFKA_SCHEMA_VERSION = 1;
/*    */   
/*    */ 
/*    */ 
/* 49 */   public static final String KAFKA_ZOOKEEPER_CHROOT = ZKPaths.makePath(KAFKA_ZOOKEEPER_PATH, "v1");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String NUM_PRODUCERS = "num.producers";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int MAX_NUM_PRODUCERS = 8;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String METADATA_BROKER_LIST = "metadata.broker.list";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String makeTransientDeadLetterTopicName(String consumerGroup, Locator locator, String clusterName)
/*    */   {
/* 71 */     return makeDeadLetterTopicName(consumerGroup, locator, clusterName, ActionType.EVENT_TRANSIENT_ERROR);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String makePermanentDeadLetterTopicName(String consumerGroup, Locator locator, String clusterName)
/*    */   {
/* 84 */     return makeDeadLetterTopicName(consumerGroup, locator, clusterName, ActionType.EVENT_PERMANENT_ERROR);
/*    */   }
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
/*    */   private static String makeDeadLetterTopicName(String consumerGroup, Locator locator, String clusterName, ActionType actionType)
/*    */   {
/* 99 */     return consumerGroup + "-" + locator.findTopicName(clusterName, actionType);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/EventServiceKafkaConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */