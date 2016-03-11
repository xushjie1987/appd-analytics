/*     */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*     */ 
/*     */ import com.codahale.metrics.Histogram;
/*     */ import com.codahale.metrics.SlidingWindowReservoir;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ import org.elasticsearch.ElasticsearchTimeoutException;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
/*     */ import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequestBuilder;
/*     */ import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
/*     */ import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
/*     */ import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequestBuilder;
/*     */ import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
/*     */ import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
/*     */ import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.ClusterAdminClient;
/*     */ import org.elasticsearch.cluster.node.DiscoveryNode;
/*     */ import org.elasticsearch.common.hppc.cursors.ObjectCursor;
/*     */ import org.elasticsearch.common.unit.ByteSizeValue;
/*     */ import org.elasticsearch.common.unit.TimeValue;
/*     */ import org.elasticsearch.monitor.fs.FsStats;
/*     */ import org.elasticsearch.monitor.fs.FsStats.Info;
/*     */ import org.elasticsearch.monitor.jvm.JvmStats;
/*     */ import org.elasticsearch.monitor.jvm.JvmStats.GarbageCollector;
/*     */ import org.elasticsearch.monitor.jvm.JvmStats.GarbageCollectors;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ @ThreadSafe
/*     */ class ElasticSearchMetricsSampler
/*     */ {
/*  39 */   private static final Logger log = org.slf4j.LoggerFactory.getLogger(ElasticSearchMetricsSampler.class);
/*     */   private final int numSamplesToKeep;
/*     */   private final ConcurrentHashMap<String, ElasticSearchStoreHealth.NodeDetails> nodeDetailsMap;
/*     */   private final ConcurrentHashMap<String, Map<String, Histogram>> nodeHealthHistograms;
/*     */   
/*  44 */   public ConcurrentHashMap<String, ElasticSearchStoreHealth.NodeDetails> getNodeDetailsMap() { return this.nodeDetailsMap; }
/*     */   
/*  46 */   public ConcurrentHashMap<String, Map<String, Histogram>> getNodeHealthHistograms() { return this.nodeHealthHistograms; }
/*     */   
/*     */   ElasticSearchMetricsSampler(int numSamplesToKeep)
/*     */   {
/*  50 */     this.numSamplesToKeep = numSamplesToKeep;
/*  51 */     this.nodeDetailsMap = new ConcurrentHashMap();
/*  52 */     this.nodeHealthHistograms = new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void sample(Client client)
/*     */   {
/*  61 */     updateTrackedNodes(client);
/*  62 */     sampleActionableMetrics(this.nodeHealthHistograms, client);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void updateTrackedNodes(Client client)
/*     */   {
/*  70 */     List<String> currentNodeIds = getCurrentDataNodeIds(client);
/*  71 */     addNewNodesToHistMap(client, currentNodeIds);
/*  72 */     removeOldNodesFromHistMap(currentNodeIds);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addNewNodesToHistMap(Client client, List<String> currentNodeIds)
/*     */   {
/*  81 */     for (String newNodeId : currentNodeIds)
/*     */     {
/*  83 */       if (!this.nodeHealthHistograms.containsKey(newNodeId)) {
/*  84 */         registerNewNode(newNodeId, getDataNodeDetails(client, newNodeId));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElasticSearchStoreHealth.NodeDetails getDataNodeDetails(Client client, String nodeId)
/*     */   {
/*  96 */     NodesInfoResponse nodeInfoResponse = (NodesInfoResponse)client.admin().cluster().prepareNodesInfo(new String[] { nodeId }).execute().actionGet(5000L);
/*     */     
/*  98 */     NodeInfo nodeInfo = (NodeInfo)nodeInfoResponse.getNodesMap().get(nodeId);
/*  99 */     ElasticSearchStoreHealth.NodeDetails nodeDetails = new ElasticSearchStoreHealth.NodeDetails(nodeInfo.getNode().getName(), nodeInfo.getNode().getHostName(), nodeInfo.getNode().getHostAddress());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 104 */     return nodeDetails;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeOldNodesFromHistMap(List<String> currentNodeIds)
/*     */   {
/* 112 */     for (Iterator<Map.Entry<String, ElasticSearchStoreHealth.NodeDetails>> it = this.nodeDetailsMap.entrySet().iterator(); it.hasNext();) {
/* 113 */       Map.Entry<String, ElasticSearchStoreHealth.NodeDetails> entry = (Map.Entry)it.next();
/*     */       
/* 115 */       if (!currentNodeIds.contains(entry.getKey())) {
/* 116 */         this.nodeHealthHistograms.remove(entry.getKey());
/* 117 */         it.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerNewNode(String nodeId, ElasticSearchStoreHealth.NodeDetails nodeDetails)
/*     */   {
/* 128 */     Map<String, Histogram> allHistograms = getEmptyHistogramMap(this.numSamplesToKeep);
/* 129 */     this.nodeHealthHistograms.put(nodeId, allHistograms);
/* 130 */     this.nodeDetailsMap.put(nodeId, nodeDetails);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Map<String, Histogram> getEmptyHistogramMap(int numSamplesToKeep)
/*     */   {
/* 138 */     Map<String, Histogram> newHistograms = new java.util.HashMap();
/* 139 */     newHistograms.put("diskUsage", new Histogram(new SlidingWindowReservoir(numSamplesToKeep)));
/* 140 */     newHistograms.put("oldGcTimes", new Histogram(new SlidingWindowReservoir(numSamplesToKeep)));
/* 141 */     newHistograms.put("youngGcTimes", new Histogram(new SlidingWindowReservoir(numSamplesToKeep)));
/* 142 */     newHistograms.put("heapUsage", new Histogram(new SlidingWindowReservoir(numSamplesToKeep)));
/* 143 */     return newHistograms;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void sampleActionableMetrics(Map<String, Map<String, Histogram>> nodeNameToAllHistograms, Client client)
/*     */   {
/* 150 */     for (Map.Entry<String, Map<String, Histogram>> entry : nodeNameToAllHistograms.entrySet()) {
/* 151 */       String nodeId = (String)entry.getKey();
/* 152 */       Map<String, Histogram> allHistogramsForCurrentNode = (Map)entry.getValue();
/* 153 */       NodesStatsResponse nodeStatsResponse = null;
/*     */       try {
/* 155 */         nodeStatsResponse = (NodesStatsResponse)client.admin().cluster().prepareNodesStats(new String[] { nodeId }).setFs(true).setJvm(true).execute().actionGet(5000L);
/*     */       }
/*     */       catch (ElasticsearchTimeoutException e) {
/* 158 */         log.debug("Node [{}] is unresponsive, putting default values in metric histograms.", nodeId);
/* 159 */         ((Histogram)allHistogramsForCurrentNode.get("diskUsage")).update(0);
/* 160 */         ((Histogram)allHistogramsForCurrentNode.get("oldGcTimes")).update(0);
/* 161 */         ((Histogram)allHistogramsForCurrentNode.get("youngGcTimes")).update(0);
/* 162 */         ((Histogram)allHistogramsForCurrentNode.get("heapUsage")).update(0); }
/* 163 */       continue;
/*     */       
/*     */ 
/* 166 */       NodeStats nodeStats = (NodeStats)nodeStatsResponse.getNodesMap().get(nodeId);
/* 167 */       updateDiskUsageHistogram((Histogram)allHistogramsForCurrentNode.get("diskUsage"), nodeStats);
/* 168 */       updateOldGcTimesHistogram((Histogram)allHistogramsForCurrentNode.get("oldGcTimes"), nodeStats);
/* 169 */       updateYoungGcTimesHistogram((Histogram)allHistogramsForCurrentNode.get("youngGcTimes"), nodeStats);
/* 170 */       updateHeapUsageHistogram((Histogram)allHistogramsForCurrentNode.get("heapUsage"), nodeStats);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<String> getCurrentDataNodeIds(Client client)
/*     */   {
/* 181 */     List<String> nodeIds = new ArrayList();
/* 182 */     ClusterStateResponse clusterStateResponse = (ClusterStateResponse)client.admin().cluster().prepareState().setMetaData(false).execute().actionGet(5000L);
/*     */     
/* 184 */     for (ObjectCursor<String> nodeIdWrapper : clusterStateResponse.getState().getNodes().dataNodes().keys()) {
/* 185 */       nodeIds.add(nodeIdWrapper.value);
/*     */     }
/* 187 */     return nodeIds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateDiskUsageHistogram(Histogram diskHistogram, NodeStats esStatsResponse)
/*     */   {
/* 195 */     double diskAvailablePercent = esStatsResponse.getFs().total().getAvailable().getGb() / esStatsResponse.getFs().total().getTotal().getGb() * 100.0D;
/*     */     
/* 197 */     diskHistogram.update(diskAvailablePercent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateOldGcTimesHistogram(Histogram gcHistogram, NodeStats esStatsResponse)
/*     */   {
/* 205 */     for (JvmStats.GarbageCollector collector : esStatsResponse.getJvm().getGc().collectors()) {
/* 206 */       if (collector.getName().equals("old")) {
/* 207 */         gcHistogram.update(collector.collectionTime().getMillis());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateYoungGcTimesHistogram(Histogram gcHistogram, NodeStats esStatsResponse)
/*     */   {
/* 217 */     for (JvmStats.GarbageCollector collector : esStatsResponse.getJvm().getGc().collectors()) {
/* 218 */       if (collector.getName().equals("young")) {
/* 219 */         gcHistogram.update(collector.collectionTime().getMillis());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateHeapUsageHistogram(Histogram heapUsage, NodeStats esStatsResponse)
/*     */   {
/* 229 */     heapUsage.update(esStatsResponse.getJvm().mem().getHeapUsedPrecent());
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ElasticSearchMetricsSampler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */