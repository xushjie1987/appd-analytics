/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.provider;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.admin.ClusterChangeNotifier;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ElasticSearchMultiNode;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.util.ElasticSearchVersionManager;
/*    */ import com.appdynamics.analytics.processor.event.configuration.NewClusterEvent;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.event.EventBuses;
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Scopes;
/*    */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*    */ import com.google.inject.binder.ScopedBindingBuilder;
/*    */ import org.elasticsearch.client.Client;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MultiClusterClientProviderModule
/*    */   extends Module
/*    */ {
/* 28 */   private static final Logger log = LoggerFactory.getLogger(MultiClusterClientProviderModule.class);
/*    */   
/*    */ 
/*    */   @Inject
/*    */   volatile ElasticSearchMultiNode esMultiNode;
/*    */   
/*    */ 
/*    */   @Inject
/*    */   volatile IndexCreationManager indexCreationManager;
/*    */   
/*    */ 
/* 39 */   public void configure() { bind(ClientProvider.class).to(MultiClusterClientProvider.class).in(Scopes.SINGLETON); }
/*    */   
/*    */   private class ClusterVersionCheckListener {
/*    */     private ClusterVersionCheckListener() {}
/*    */     
/*    */     @Subscribe
/*    */     public void onNewCluster(NewClusterEvent newClusterEvent) {
/* 46 */       try { Client client = MultiClusterClientProviderModule.this.esMultiNode.getClient(newClusterEvent.getClusterName());
/* 47 */         ElasticSearchVersionManager versionManager = new ElasticSearchVersionManager(client, MultiClusterClientProviderModule.this.indexCreationManager, "analytics");
/*    */         
/* 49 */         versionManager.checkSchemaVersion("Analytics event index", 0);
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 53 */         MultiClusterClientProviderModule.log.debug("Failed to check cluster version for cluster [{}] because of [{}]", newClusterEvent.getClusterName(), e);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject
/*    */   public void onStart(ClusterChangeNotifier clusterChangeNotifier, EventBuses eventBuses)
/*    */   {
/* 61 */     ClusterVersionCheckListener listener = new ClusterVersionCheckListener(null);
/* 62 */     eventBuses.registerListener("default-event-bus", listener);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/provider/MultiClusterClientProviderModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */