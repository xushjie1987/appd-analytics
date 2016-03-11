/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.single;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.google.common.base.Joiner;
/*    */ import com.google.common.base.Joiner.MapJoiner;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Singleton;
/*    */ import javax.annotation.PreDestroy;
/*    */ import org.elasticsearch.client.Client;
/*    */ import org.elasticsearch.common.settings.ImmutableSettings;
/*    */ import org.elasticsearch.common.settings.ImmutableSettings.Builder;
/*    */ import org.elasticsearch.common.settings.Settings;
/*    */ import org.elasticsearch.node.Node;
/*    */ import org.elasticsearch.node.NodeBuilder;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Singleton
/*    */ public class ElasticSearchSingleNode
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchSingleNode.class);
/*    */   
/*    */ 
/*    */   final Node node;
/*    */   
/*    */   final Client client;
/*    */   
/*    */ 
/*    */   @Inject
/*    */   ElasticSearchSingleNode(ElasticSearchSingleNodeConfiguration config)
/*    */   {
/* 37 */     Settings settings = ImmutableSettings.builder().put(config.getNodeSettings()).build();
/*    */     
/*    */ 
/*    */ 
/* 41 */     if (log.isInfoEnabled()) {
/* 42 */       log.info("Node has been configured with [{}]", Joiner.on(',').withKeyValueSeparator("=").join(config.getNodeSettings()));
/*    */       
/* 44 */       log.info("Call timeout has been set to [{}] milliseconds", Long.valueOf(config.getCallTimeout().toMilliseconds()));
/*    */     }
/*    */     
/*    */ 
/* 48 */     this.node = NodeBuilder.nodeBuilder().loadConfigSettings(false).settings(settings).node();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 53 */     this.client = this.node.client();
/*    */   }
/*    */   
/*    */   public Client getClient() {
/* 57 */     return this.client;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @PreDestroy
/*    */   void stop()
/*    */   {
/* 65 */     this.client.close();
/*    */     
/* 67 */     if (!this.node.isClosed()) {
/* 68 */       this.node.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/single/ElasticSearchSingleNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */