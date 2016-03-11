/*    */ package com.appdynamics.analytics.processor.route;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.IndexManagementModuleConfiguration;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ClusterRouter;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*    */ import com.appdynamics.analytics.processor.event.ElasticSearchEventService;
/*    */ import com.appdynamics.analytics.processor.route.resource.ClusterRouteResource;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
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
/*    */ public class ClusterRouteModule
/*    */   extends Module<ClusterRouteConfiguration>
/*    */ {
/*    */   @Provides
/*    */   @Singleton
/*    */   protected ClusterRouteConfiguration provideClusterRouteConfig()
/*    */   {
/* 31 */     return (ClusterRouteConfiguration)getConfiguration();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Provides
/*    */   @Singleton
/*    */   protected RouteCleanupManager makeRouteCleanupManager(ElasticSearchEventService elasticSearchEventService, ClusterRouter clusterRouter, ClientProvider clientProvider, IndexManagementModuleConfiguration indexManagementModuleConfiguration, ClusterRouteConfiguration clusterRouteConfiguration)
/*    */   {
/* 40 */     return new RouteCleanupManager(elasticSearchEventService, clusterRouter, clientProvider, indexManagementModuleConfiguration, clusterRouteConfiguration.leewayTimeInMillis);
/*    */   }
/*    */   
/*    */   @Inject
/*    */   final void onStart(Environment environment, ClusterRouter clusterRouter, RouteCleanupManager routeCleanupManager)
/*    */   {
/* 46 */     ClusterRouteResource resource = new ClusterRouteResource(clusterRouter, routeCleanupManager);
/* 47 */     onResourceCreated(resource);
/* 48 */     environment.jersey().register(resource);
/*    */   }
/*    */   
/*    */   protected void onResourceCreated(ClusterRouteResource resource) {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/route/ClusterRouteModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */