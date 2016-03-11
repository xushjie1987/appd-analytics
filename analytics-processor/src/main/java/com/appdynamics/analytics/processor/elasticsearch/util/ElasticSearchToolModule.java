/*    */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */ import com.google.common.base.Optional;
/*    */ import com.google.inject.Inject;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
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
/*    */ public class ElasticSearchToolModule
/*    */   extends Module<ElasticSearchToolConfiguration>
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchToolModule.class);
/*    */   
/*    */   ElasticSearchStoreTool storeTool;
/*    */   
/*    */   @Inject
/*    */   void onStart(Environment environment, ClientProvider clientProvider, LifecycleInjector injector)
/*    */   {
/* 30 */     ElasticSearchToolResource adminResource = new ElasticSearchToolResource(clientProvider);
/*    */     
/* 32 */     ElasticSearchToolConfiguration esToolConfig = (ElasticSearchToolConfiguration)getConfiguration();
/* 33 */     if ((esToolConfig.getStoreToolConfiguration() != null) && (esToolConfig.getStoreToolConfiguration().enabled)) {
/* 34 */       ElasticSearchStoreTool storeTool = makeStoreTool(clientProvider, injector);
/* 35 */       adminResource.setEsStoreTool(Optional.of(storeTool));
/*    */     }
/* 37 */     environment.jersey().register(adminResource);
/*    */   }
/*    */   
/*    */   ElasticSearchStoreTool makeStoreTool(ClientProvider clientProvider, LifecycleInjector injector) {
/* 41 */     if (this.storeTool == null) {
/* 42 */       ElasticSearchToolConfiguration.ElasticSearchStoreToolConfiguration config = ((ElasticSearchToolConfiguration)getConfiguration()).getStoreToolConfiguration();
/*    */       
/* 44 */       this.storeTool = new ElasticSearchStoreTool(clientProvider, config.samplePeriodInSeconds, config.slidingWindowLengthInMinutes);
/*    */       
/* 46 */       return (ElasticSearchStoreTool)injector.inject(this.storeTool);
/*    */     }
/* 48 */     return this.storeTool;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ElasticSearchToolModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */