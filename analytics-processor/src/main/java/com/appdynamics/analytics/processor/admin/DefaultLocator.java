/*    */ package com.appdynamics.analytics.processor.admin;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ClusterRouter;
/*    */ import com.google.common.base.Throwables;
/*    */ import java.util.EnumMap;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultLocator
/*    */   implements Locator
/*    */ {
/*    */   final ClusterRouter router;
/*    */   final ConcurrentHashMap<String, EnumMap<ActionType, String>> clusterNameToTopicNameCache;
/*    */   
/*    */   DefaultLocator(ClusterRouter router)
/*    */   {
/* 24 */     this.router = router;
/*    */     
/* 26 */     this.clusterNameToTopicNameCache = new ConcurrentHashMap();
/*    */   }
/*    */   
/*    */   public String findActiveClusterName(String account)
/*    */   {
/*    */     try {
/* 32 */       return (String)this.router.findClusters(account).get(0);
/*    */     } catch (Exception e) {
/* 34 */       throw Throwables.propagate(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public String findTopicName(String clusterName, ActionType actionType)
/*    */   {
/* 40 */     EnumMap<ActionType, String> clusterTopics = (EnumMap)this.clusterNameToTopicNameCache.get(clusterName);
/* 41 */     if (clusterTopics == null) {
/* 42 */       EnumMap<ActionType, String> topics = new EnumMap(ActionType.class);
/* 43 */       for (ActionType type : ActionType.values()) {
/* 44 */         String topicName = makeTopicName(type, clusterName);
/* 45 */         topics.put(type, topicName);
/*    */       }
/*    */       
/* 48 */       clusterTopics = (EnumMap)this.clusterNameToTopicNameCache.putIfAbsent(clusterName, topics);
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 55 */       if (clusterTopics == null) {
/* 56 */         clusterTopics = topics;
/*    */       }
/*    */     }
/*    */     
/* 60 */     return (String)clusterTopics.get(actionType);
/*    */   }
/*    */   
/*    */   static String makeTopicName(ActionType actionType, String clusterName) {
/* 64 */     return clusterName + "-" + actionType.nameSuffix();
/*    */   }
/*    */   
/*    */   public String findTopicName(String account, String eventType, ActionType actionType)
/*    */   {
/* 69 */     String clusterName = findActiveClusterName(account);
/*    */     
/* 71 */     return findTopicName(clusterName, actionType);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/admin/DefaultLocator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */