/*     */ package com.appdynamics.analytics.processor.elasticsearch.index;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ExecutionSuccessfulHealthCheck;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.compaction.IndexCompactionLeader;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.compaction.IndexCompactionManager;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.IndexCompactionConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.IndexManagementModuleConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.RollingIndexConfiguration;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.EventLifespanIndexShardSizingStrategy;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.RollingIndexController;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.RollingIndexLeader;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.RollingIndexManager;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.rolling.RollingIndexShardSizingStrategy;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.EsCallTimeout;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.event.ClusterLock;
/*     */ import com.appdynamics.analytics.processor.event.IndexNameResolver;
/*     */ import com.appdynamics.analytics.processor.event.configuration.EventServiceConfiguration;
/*     */ import com.appdynamics.analytics.processor.event.metadata.EventTypeMetaDataService;
/*     */ import com.appdynamics.analytics.processor.leader.LeaderElectionFactory;
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*     */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*     */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*     */ import com.appdynamics.common.util.health.RunningStateHealthCheck;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Provides;
/*     */ import com.google.inject.Singleton;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndexManagementModule
/*     */   extends Module<IndexManagementModuleConfiguration>
/*     */ {
/*  44 */   private static final Logger log = LoggerFactory.getLogger(IndexManagementModule.class);
/*     */   
/*     */   private volatile IndexCompactionLeader indexCompactionLeader;
/*     */   
/*     */   private volatile RollingIndexLeader rollingIndexLeader;
/*     */   
/*     */   private volatile RollingIndexShardSizingStrategy shardSizingStrategy;
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   IndexManagementModuleConfiguration provideStoreConfig()
/*     */   {
/*  56 */     return (IndexManagementModuleConfiguration)getConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Provides
/*     */   @Singleton
/*     */   protected IndexCompactionManager makeIndexCompactionManager(@EsCallTimeout TimeValue esCallTimeout, ClientProvider clientProvider, IndexNameResolver indexNameResolver, IndexManagementModuleConfiguration configuration, RollingIndexConfiguration rollingIndexConfiguration, IndexCreationManager indexCreationManager)
/*     */   {
/*  69 */     return new IndexCompactionManager(esCallTimeout, clientProvider, indexNameResolver, configuration.getIndexCompactionConfiguration().getFlushThresholdInPercent(), rollingIndexConfiguration.getShardSizeThreshold(), indexCreationManager, configuration.getIndexCompactionConfiguration().isTestMode());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Provides
/*     */   @Singleton
/*     */   IndexCompactionLeader makeIndexCompactionLeader(LeaderElectionFactory leaderElectionFactory, IndexCompactionManager manager, IndexManagementModuleConfiguration configuration)
/*     */   {
/*  80 */     return this.indexCompactionLeader = new IndexCompactionLeader(leaderElectionFactory, manager, configuration.getIndexCompactionConfiguration().isAutoRun(), configuration.getIndexCompactionConfiguration().getRunTime(), configuration.getIndexCompactionConfiguration().getTimeFormat());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Provides
/*     */   @Singleton
/*     */   protected RollingIndexManager makeRollingIndexManager(ClientProvider clientProvider, IndexNameResolver indexNameResolver, EventTypeMetaDataService eventTypeMetaDataService, RollingIndexShardSizingStrategy shardSizingStrategy, IndexCreationManager indexCreationManager, IndexManagementModuleConfiguration configuration, EventServiceConfiguration eventServiceConfiguration)
/*     */   {
/*  96 */     return new RollingIndexManager(clientProvider, indexNameResolver, eventTypeMetaDataService, shardSizingStrategy, indexCreationManager, configuration.getRollingIndexConfiguration(), eventServiceConfiguration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Provides
/*     */   @Singleton
/*     */   RollingIndexController makeRollingIndexController(LeaderElectionFactory leaderElectionFactory, RollingIndexManager manager, ClusterLock clusterLock, IndexManagementModuleConfiguration configuration)
/*     */   {
/* 113 */     long millis = configuration.getRollingIndexConfiguration().getRollingIndexCheckInterval().toMilliseconds();
/*     */     
/* 115 */     return this.rollingIndexLeader = new RollingIndexLeader(leaderElectionFactory, manager, clusterLock, millis);
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   RollingIndexShardSizingStrategy makeShardSizingStrategy(IndexNameResolver indexNameResolver, IndexManagementModuleConfiguration configuration, EventTypeMetaDataService eventTypeMetaDataService)
/*     */   {
/* 122 */     return this.shardSizingStrategy = new EventLifespanIndexShardSizingStrategy(new TimeKeeper(), configuration.getRollingIndexConfiguration().getTargetIndexLifeInDays(), indexNameResolver, eventTypeMetaDataService);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Inject
/*     */   void createMetaDataIndices(MetaDataIndexCreator metaDataIndexCreator, IndexManagementModuleConfiguration storeConfig, ClientProvider clientProvider, IndexCreationManager indexCreationManager)
/*     */   {
/* 133 */     metaDataIndexCreator.createMetaDataIndicesOnStartup(storeConfig, clientProvider, indexCreationManager);
/*     */   }
/*     */   
/*     */   @Inject
/*     */   void startAndRegisterIndexCompactionManager(ConsolidatedHealthCheck healthCheck, IndexCompactionLeader indexCompactionLeader)
/*     */   {
/* 139 */     indexCompactionLeader.start();
/* 140 */     ExecutionSuccessfulHealthCheck indexCompactionLeaderHealthCheck = new ExecutionSuccessfulHealthCheck("Index compaction management", indexCompactionLeader.getLeaderRunning(), indexCompactionLeader.getLastExecutionSuccessful());
/*     */     
/*     */ 
/* 143 */     healthCheck.register(indexCompactionLeaderHealthCheck);
/*     */   }
/*     */   
/*     */   @Inject
/*     */   void startAndRegisterRollover(ConsolidatedHealthCheck healthCheck, RollingIndexController rollingIndexController) {
/* 148 */     rollingIndexController.start();
/* 149 */     RunningStateHealthCheck rollingLeaderHealthCheck = new RunningStateHealthCheck("Rolling index management", rollingIndexController.getRunningState());
/*     */     
/* 151 */     healthCheck.register(rollingLeaderHealthCheck);
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   public void stop() {
/* 156 */     if (this.indexCompactionLeader != null) {
/* 157 */       this.indexCompactionLeader.close();
/*     */     }
/* 159 */     if (this.rollingIndexLeader != null) {
/* 160 */       this.rollingIndexLeader.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/IndexManagementModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */