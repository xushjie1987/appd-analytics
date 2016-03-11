/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.multi;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.EsCallTimeout;
/*    */ import com.appdynamics.analytics.processor.event.ClusterLock;
/*    */ import com.appdynamics.analytics.processor.event.MultiNodeClusterLock;
/*    */ import com.appdynamics.common.framework.util.FrameworkHelper;
/*    */ import com.appdynamics.common.io.file.AbstractFilePollerModule;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Scopes;
/*    */ import com.google.inject.binder.AnnotatedBindingBuilder;
/*    */ import com.google.inject.binder.ScopedBindingBuilder;
/*    */ import java.nio.file.PathMatcher;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.apache.commons.io.monitor.FileAlterationListener;
/*    */ import org.apache.commons.io.monitor.FileAlterationMonitor;
/*    */ import org.apache.commons.io.monitor.FileAlterationObserver;
/*    */ import org.elasticsearch.common.unit.TimeValue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElasticSearchMultiNodeModule
/*    */   extends AbstractFilePollerModule<ElasticSearchMultiNodeConfiguration>
/*    */ {
/* 37 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchMultiNodeModule.class);
/*    */   
/*    */   @Inject
/*    */   volatile ElasticSearchMultiNode esMultiNode;
/*    */   
/*    */   @Inject
/*    */   volatile ClusterRouter clusterRouter;
/*    */   
/*    */   @Inject
/*    */   volatile ConsolidatedHealthCheck healthCheck;
/*    */   
/*    */   private volatile EsClientFilePollListener listener;
/*    */   
/*    */   public void configure()
/*    */   {
/* 52 */     super.configure();
/*    */     
/* 54 */     bind(ClusterLock.class).to(MultiNodeClusterLock.class).in(Scopes.SINGLETON);
/*    */   }
/*    */   
/*    */   @Provides
/*    */   ElasticSearchMultiNodeConfiguration provideConfiguration() {
/* 59 */     return (ElasticSearchMultiNodeConfiguration)super.getConfiguration();
/*    */   }
/*    */   
/*    */   @Provides
/*    */   @EsCallTimeout
/*    */   TimeValue provideEsCallTimeout(ElasticSearchMultiNodeConfiguration config) {
/* 65 */     return TimeValue.timeValueMillis(config.getCallTimeout().toMilliseconds());
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   protected void onStart() {
/* 70 */     super.onStart();
/*    */     
/*    */ 
/* 73 */     for (FileAlterationObserver observer : this.monitor.getObservers()) {
/* 74 */       observer.checkAndNotify();
/*    */     }
/*    */     
/* 77 */     this.clusterRouter.seedRoutes(((ElasticSearchMultiNodeConfiguration)getConfiguration()).getSeedClusterRoutes());
/* 78 */     this.healthCheck.register(new ElasticSearchMultiNodeHealthCheck(getUri(), this.esMultiNode));
/*    */   }
/*    */   
/*    */   protected FileAlterationListener newListener(String path, PathMatcher pathMatcher)
/*    */   {
/* 83 */     if (this.listener == null) {
/* 84 */       this.listener = new EsClientFilePollListener(FrameworkHelper.getProperties(), this.esMultiNode);
/*    */     }
/* 86 */     return this.listener;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/multi/ElasticSearchMultiNodeModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */