/*     */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ElasticSearchHealthCheck;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.codahale.metrics.Histogram;
/*     */ import com.codahale.metrics.Snapshot;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.lang.ArrayUtils;
/*     */ import org.elasticsearch.action.ActionFuture;
/*     */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
/*     */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
/*     */ import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
/*     */ import org.elasticsearch.action.admin.indices.stats.CommonStats;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.ClusterAdminClient;
/*     */ import org.elasticsearch.client.IndicesAdminClient;
/*     */ import org.elasticsearch.common.unit.ByteSizeValue;
/*     */ import org.elasticsearch.index.store.StoreStats;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElasticSearchStoreTool
/*     */   extends ElasticSearchToolScheduler
/*     */ {
/*  41 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchStoreTool.class);
/*     */   
/*     */   private static final int DISK_USAGE_UNHEALTHY_THRESHOLD = 20;
/*     */   
/*     */   private static final int DISK_USAGE_APPROACHING_UNHEALTHY_THRESHOLD = 30;
/*     */   
/*     */   private static final int HEAP_USAGE_UNHEALTHY_THRESHOLD = 90;
/*     */   
/*     */   private static final int HEAP_USAGE_APPROACHING_UNHEALTHY_THRESHOLD = 80;
/*     */   public static final String MBEAN_NAME = "Application:name=Store";
/*     */   private volatile StoreReport clusterReport;
/*     */   private final ClientProvider clientProvider;
/*     */   private final ElasticSearchMetricsSampler metricsSampler;
/*     */   
/*     */   public ElasticSearchStoreTool(ClientProvider clientProvider, int samplePeriodInSeconds, int slidingWindowLengthInMinutes)
/*     */   {
/*  57 */     super(samplePeriodInSeconds);
/*  58 */     this.clientProvider = clientProvider;
/*     */     
/*  60 */     int numSamplesToKeep = (int)Math.ceil(slidingWindowLengthInMinutes * 60.0D / samplePeriodInSeconds);
/*  61 */     this.metricsSampler = new ElasticSearchMetricsSampler(numSamplesToKeep);
/*     */     
/*     */ 
/*  64 */     this.clusterReport = new StoreReport();
/*     */     try {
/*  66 */       MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/*  67 */       mbs.registerMBean(this.clusterReport, new ObjectName("Application:name=Store"));
/*     */     } catch (Exception e) {
/*  69 */       log.warn("Error occurred while registering MBean", e);
/*     */     }
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   public void start()
/*     */   {
/*  76 */     this.metricsSampler.updateTrackedNodes(this.clientProvider.getAdminClient());
/*     */   }
/*     */   
/*     */   public ElasticSearchStoreHealth getCurrentElasticSearchHealth() {
/*  80 */     Map<String, ElasticSearchStoreHealth.Node> nodeHealthMap = convertHistogramToNodeHealth(this.metricsSampler.getNodeHealthHistograms(), this.metricsSampler.getNodeDetailsMap());
/*     */     
/*     */ 
/*  83 */     ElasticSearchStoreHealth.OverallHealth overallHealth = analyzeClusterHealth(nodeHealthMap);
/*  84 */     return new ElasticSearchStoreHealth(overallHealth, nodeHealthMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, ElasticSearchStoreHealth.Node> convertHistogramToNodeHealth(Map<String, Map<String, Histogram>> nodeHealthHistograms, Map<String, ElasticSearchStoreHealth.NodeDetails> nodeInfo)
/*     */   {
/*  94 */     Map<String, ElasticSearchStoreHealth.Node> returnMap = new HashMap();
/*  95 */     for (Map.Entry<String, Map<String, Histogram>> entry : nodeHealthHistograms.entrySet()) {
/*  96 */       String nodeName = (String)entry.getKey();
/*  97 */       ElasticSearchStoreHealth.NodeHealth nodeHealth = new ElasticSearchStoreHealth.NodeHealth(getHistogramData((Histogram)((Map)entry.getValue()).get("diskUsage")), getHistogramData((Histogram)((Map)entry.getValue()).get("oldGcTimes")), getHistogramData((Histogram)((Map)entry.getValue()).get("youngGcTimes")), getHistogramData((Histogram)((Map)entry.getValue()).get("heapUsage")));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 104 */       returnMap.put(nodeName, new ElasticSearchStoreHealth.Node((ElasticSearchStoreHealth.NodeDetails)nodeInfo.get(nodeName), nodeHealth));
/*     */     }
/* 106 */     return returnMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElasticSearchStoreHealth.OverallHealth analyzeClusterHealth(Map<String, ElasticSearchStoreHealth.Node> nodeHealthMap)
/*     */   {
/* 118 */     ElasticSearchStoreHealth.OverallHealth health = analyzeIndividualNodes(nodeHealthMap);
/*     */     
/*     */ 
/* 121 */     analyzeESHealthCheck(health);
/*     */     
/* 123 */     return health;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void analyzeESHealthCheck(ElasticSearchStoreHealth.OverallHealth currentHealth)
/*     */   {
/* 131 */     ClusterHealthResponse response = (ClusterHealthResponse)this.clientProvider.getAdminClient().admin().cluster().health(new ClusterHealthRequest(new String[0])).actionGet(5000L);
/*     */     
/*     */ 
/* 134 */     if (response.getStatus() == ClusterHealthStatus.RED) {
/* 135 */       currentHealth.setStatus(ElasticSearchStoreHealth.Status.UNHEALTHY);
/* 136 */       currentHealth.descriptions.add(ElasticSearchHealthCheck.formatClusterHealthResponse(response));
/* 137 */     } else if (response.getStatus() == ClusterHealthStatus.YELLOW)
/*     */     {
/* 139 */       if (currentHealth.getStatus() != ElasticSearchStoreHealth.Status.UNHEALTHY) {
/* 140 */         currentHealth.setStatus(ElasticSearchStoreHealth.Status.APPROACHING_UNHEALTHY);
/* 141 */         currentHealth.descriptions.add(ElasticSearchHealthCheck.formatClusterHealthResponse(response));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElasticSearchStoreHealth.OverallHealth analyzeIndividualNodes(Map<String, ElasticSearchStoreHealth.Node> nodeHealthMap)
/*     */   {
/* 151 */     ElasticSearchStoreHealth.Status returnStatus = ElasticSearchStoreHealth.Status.HEALTHY;
/* 152 */     List<String> unhealthyDescriptions = new ArrayList();
/* 153 */     for (Map.Entry<String, ElasticSearchStoreHealth.Node> entry : nodeHealthMap.entrySet()) {
/* 154 */       ElasticSearchStoreHealth.NodeHealth healthData = ((ElasticSearchStoreHealth.Node)entry.getValue()).getNodeHealth();
/*     */       
/*     */ 
/* 157 */       if (healthData.getDiskUsageInPercentAvailable().getMedian() < 20.0D) {
/* 158 */         returnStatus = ElasticSearchStoreHealth.Status.UNHEALTHY;
/* 159 */         unhealthyDescriptions.add(getDiskUsageDescription((String)entry.getKey(), healthData));
/* 160 */       } else if (healthData.getDiskUsageInPercentAvailable().getMedian() < 30.0D)
/*     */       {
/* 162 */         if (returnStatus != ElasticSearchStoreHealth.Status.UNHEALTHY) {
/* 163 */           returnStatus = ElasticSearchStoreHealth.Status.APPROACHING_UNHEALTHY;
/*     */         }
/* 165 */         unhealthyDescriptions.add(getDiskUsageDescription((String)entry.getKey(), healthData));
/*     */       }
/*     */       
/*     */ 
/* 169 */       if (healthData.getHeapUsageInPercentUsed().getMedian() > 90.0D) {
/* 170 */         returnStatus = ElasticSearchStoreHealth.Status.UNHEALTHY;
/* 171 */         unhealthyDescriptions.add(getHeapUsageDescription((String)entry.getKey(), healthData));
/* 172 */       } else if (healthData.getHeapUsageInPercentUsed().getMedian() > 80.0D) {
/* 173 */         if (returnStatus != ElasticSearchStoreHealth.Status.UNHEALTHY) {
/* 174 */           returnStatus = ElasticSearchStoreHealth.Status.APPROACHING_UNHEALTHY;
/*     */         }
/* 176 */         unhealthyDescriptions.add(getHeapUsageDescription((String)entry.getKey(), healthData));
/*     */       }
/*     */     }
/* 179 */     return new ElasticSearchStoreHealth.OverallHealth(returnStatus, unhealthyDescriptions);
/*     */   }
/*     */   
/*     */   private String getDiskUsageDescription(String nodeId, ElasticSearchStoreHealth.NodeHealth nodeHealth) {
/* 183 */     return String.format("Median disk space available on node id: %s, name: %s is %d%%", new Object[] { nodeId, ((ElasticSearchStoreHealth.NodeDetails)this.metricsSampler.getNodeDetailsMap().get(nodeId)).getNodeName(), Integer.valueOf((int)nodeHealth.getDiskUsageInPercentAvailable().getMedian()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String getHeapUsageDescription(String nodeId, ElasticSearchStoreHealth.NodeHealth nodeHealth)
/*     */   {
/* 190 */     return String.format("Median heap usage (in percent) on node id: %s, name: %s is %d%%", new Object[] { nodeId, ((ElasticSearchStoreHealth.NodeDetails)this.metricsSampler.getNodeDetailsMap().get(nodeId)).getNodeName(), Integer.valueOf((int)nodeHealth.getHeapUsageInPercentUsed().getMedian()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElasticSearchStoreHealth.HistogramData getHistogramData(Histogram histogram)
/*     */   {
/* 201 */     Snapshot snapshot = histogram.getSnapshot();
/* 202 */     return new ElasticSearchStoreHealth.HistogramData(Arrays.asList(ArrayUtils.toObject(snapshot.getValues())), snapshot.getMin(), snapshot.getMax(), snapshot.getMean(), snapshot.getMedian(), snapshot.get95thPercentile());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void process()
/*     */   {
/*     */     try
/*     */     {
/* 214 */       this.metricsSampler.sample(this.clientProvider.getAdminClient());
/*     */     } catch (RuntimeException e) {
/* 216 */       log.error("Error while sampling ES data nodes for health metrics ", e);
/*     */     }
/*     */     try
/*     */     {
/* 220 */       collectStoreStatistics();
/*     */     } catch (ExecutionException e) {
/* 222 */       log.error("Error connecting to elasticsearch client for cluster status", e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void collectStoreStatistics() throws ExecutionException {
/* 227 */     Client client = this.clientProvider.getAdminClient();
/* 228 */     IndicesStatsResponse response = (IndicesStatsResponse)client.admin().indices().prepareStats(new String[0]).get();
/* 229 */     if (response != null) {
/* 230 */       this.clusterReport.setClusterSizeBytes(response.getTotal().getStore().getSize().getBytes());
/* 231 */       this.clusterReport.setNumberOfIndices(response.getIndices().size());
/* 232 */       this.clusterReport.setNumberOfShards(response.getTotalShards());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ElasticSearchStoreTool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */