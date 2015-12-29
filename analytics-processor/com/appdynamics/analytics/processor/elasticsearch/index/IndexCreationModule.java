/*    */ package com.appdynamics.analytics.processor.elasticsearch.index;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.IndexCreationModuleConfiguration;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.DefaultIndexCreationManager;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.EsCallTimeout;
/*    */ import com.appdynamics.analytics.processor.event.ClusterLock;
/*    */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import org.elasticsearch.common.unit.TimeValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IndexCreationModule
/*    */   extends Module<IndexCreationModuleConfiguration>
/*    */ {
/*    */   @Provides
/*    */   public IndexCreationModuleConfiguration provideConfiguration()
/*    */   {
/* 23 */     return (IndexCreationModuleConfiguration)getConfiguration();
/*    */   }
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   public IndexCreationManager getIndexCreationManager(@EsCallTimeout TimeValue esCallTimeout, ClusterLock lock, IndexNameResolver indexNameResolver)
/*    */   {
/* 30 */     return new DefaultIndexCreationManager(esCallTimeout, lock, ((IndexCreationModuleConfiguration)getConfiguration()).getEventIndexDefaults(), indexNameResolver);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/IndexCreationModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */