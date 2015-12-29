/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.provider;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.single.ElasticSearchSingleNode;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.util.ElasticSearchVersionManager;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Scopes;
/*    */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*    */ import com.google.inject.binder.ScopedBindingBuilder;
/*    */ import javax.annotation.PostConstruct;
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
/*    */ public class SingleClientProviderModule
/*    */   extends Module
/*    */ {
/*    */   @Inject
/*    */   volatile ElasticSearchSingleNode esSingleNode;
/*    */   @Inject
/*    */   volatile IndexCreationManager indexCreationManager;
/*    */   
/*    */   public void configure()
/*    */   {
/* 33 */     bind(ClientProvider.class).to(SingleClientProvider.class).in(Scopes.SINGLETON);
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   public void onStart() {
/* 38 */     ElasticSearchVersionManager versionManager = new ElasticSearchVersionManager(this.esSingleNode.getClient(), this.indexCreationManager, "analytics");
/*    */     try
/*    */     {
/* 41 */       versionManager.checkSchemaVersion("Analytics event index", 0);
/*    */     }
/*    */     catch (Exception e) {}
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/provider/SingleClientProviderModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */