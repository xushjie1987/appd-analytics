/*     */ package com.appdynamics.analytics.processor.migration;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper;
/*     */ import com.appdynamics.analytics.processor.migration.elasticsearch.ElasticSearchHelper.Listener;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.client.transport.TransportClient;
/*     */ import org.elasticsearch.common.settings.ImmutableSettings;
/*     */ import org.elasticsearch.common.settings.ImmutableSettings.Builder;
/*     */ import org.elasticsearch.common.settings.Settings;
/*     */ import org.elasticsearch.common.transport.InetSocketTransportAddress;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.kohsuke.args4j.Option;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractElasticSearchMigrationUtility
/*     */   extends AbstractMigrator
/*     */ {
/*  40 */   private static final Logger log = LoggerFactory.getLogger(AbstractElasticSearchMigrationUtility.class);
/*     */   
/*     */ 
/*     */ 
/*     */   static final long ES_TIMEOUT_MILLIS = 30000L;
/*     */   
/*     */ 
/*     */   @Option(name="-o", aliases={"--host"}, usage="The elasticsearch host", metaVar="HOST", required=true)
/*     */   String host;
/*     */   
/*     */ 
/*     */   @Option(name="-p", aliases={"--port"}, usage="The elasticsearch port", metaVar="PORT", required=true)
/*     */   int port;
/*     */   
/*     */ 
/*     */   @Option(name="-c", aliases={"--clusterName"}, usage="The elasticsearch cluster name", metaVar="CLUSTERNAME", required=true)
/*     */   String clusterName;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract String getTargetIndex();
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract ElasticSearchHelper.Listener<SearchHit[]> buildHitListener(Client paramClient);
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean beforeRun(Client client)
/*     */   {
/*  70 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void afterSuccessfulRun(Client client) {}
/*     */   
/*     */ 
/*     */   protected void afterFailedRun(Client client) {}
/*     */   
/*     */ 
/*     */   protected void run()
/*     */   {
/*  82 */     Client client = getClient();
/*  83 */     IndicesAdminClient adminClient = client.admin().indices();
/*     */     
/*  85 */     if (!beforeRun(client)) {
/*  86 */       return;
/*     */     }
/*     */     
/*  89 */     ElasticSearchHelper.Listener<SearchHit[]> transformer = buildHitListener(client);
/*     */     
/*     */ 
/*  92 */     ElasticSearchHelper.updateIndexSettings(adminClient, getTargetIndex(), ImmutableMap.of("refresh_interval", "-1"));
/*     */     try
/*     */     {
/*  95 */       ElasticSearchHelper.fetchResults(client, QueryBuilders.matchAllQuery(), new TimeValue(30L, TimeUnit.SECONDS), 500, 5, transformer, new String[] { getTargetIndex() });
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  99 */       afterFailedRun(client);
/* 100 */       throw Throwables.propagate(e);
/*     */     } finally {
/* 102 */       log.info("Attempting to restore index [{}] to default settings", getTargetIndex());
/*     */       
/*     */ 
/* 105 */       ElasticSearchHelper.updateIndexSettings(adminClient, getTargetIndex(), ImmutableMap.of("refresh_interval", "1s"));
/*     */     }
/*     */     
/* 108 */     ESUtils.refreshIndices(client, new String[0]);
/* 109 */     afterSuccessfulRun(client);
/*     */   }
/*     */   
/*     */   Client getClient() {
/* 113 */     Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", this.clusterName).put("client.transport.ping_timeout", 30000L).build();
/*     */     
/*     */ 
/* 116 */     return new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(this.host, this.port));
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/migration/AbstractElasticSearchMigrationUtility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */