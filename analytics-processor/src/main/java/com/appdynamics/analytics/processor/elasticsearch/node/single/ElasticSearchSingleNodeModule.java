/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.single;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.EsCallTimeout;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.elasticsearch.common.unit.TimeValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElasticSearchSingleNodeModule
/*    */   extends Module<ElasticSearchSingleNodeConfiguration>
/*    */ {
/*    */   @Inject
/*    */   volatile ElasticSearchSingleNode esSingleNode;
/*    */   @Inject
/*    */   volatile ConsolidatedHealthCheck healthCheck;
/*    */   
/*    */   @Provides
/*    */   ElasticSearchSingleNodeConfiguration provideConfiguration()
/*    */   {
/* 30 */     return (ElasticSearchSingleNodeConfiguration)super.getConfiguration();
/*    */   }
/*    */   
/*    */   @Provides
/*    */   @EsCallTimeout
/*    */   TimeValue provideEsCallTimeout(ElasticSearchSingleNodeConfiguration config) {
/* 36 */     return TimeValue.timeValueMillis(config.getCallTimeout().toMilliseconds());
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   void onStart() {
/* 41 */     this.healthCheck.register(new ElasticSearchSingleNodeHealthCheck(getUri(), this.esSingleNode));
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/single/ElasticSearchSingleNodeModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */