/*     */ package com.appdynamics.analytics.processor.elasticsearch.node.multi;
/*     */ 
/*     */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Joiner.MapJoiner;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.common.settings.ImmutableSettings;
/*     */ import org.elasticsearch.common.settings.ImmutableSettings.Builder;
/*     */ import org.elasticsearch.common.settings.Settings;
/*     */ import org.elasticsearch.node.Node;
/*     */ import org.elasticsearch.node.NodeBuilder;
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
/*     */ @Singleton
/*     */ public class ElasticSearchMultiNode
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchMultiNode.class);
/*     */   
/*     */ 
/*     */   static final String FRAGMENT_ENABLED = "ad.enabled";
/*     */   
/*  48 */   static final Pattern TRIBE_CONNECTION_NAME_PATTERN = Pattern.compile("tribe\\.(.*)\\.cluster\\.name");
/*     */   
/*     */ 
/*     */ 
/*     */   final ElasticSearchMultiNodeConfiguration config;
/*     */   
/*     */ 
/*     */ 
/*     */   final Map<String, Node> nodes;
/*     */   
/*     */ 
/*     */ 
/*     */   final Map<OrderlessMultiKey, Node> tribeNodes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Inject
/*     */   public ElasticSearchMultiNode(ElasticSearchMultiNodeConfiguration config)
/*     */   {
/*  68 */     this.config = config;
/*     */     
/*  70 */     this.nodes = new ConcurrentHashMap();
/*  71 */     this.tribeNodes = new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */   public Client getClient(String clusterName) {
/*  75 */     return getClient(Arrays.asList(new String[] { clusterName }));
/*     */   }
/*     */   
/*     */   public Client getClient(List<String> clusterNames) {
/*  79 */     if (clusterNames.size() == 1) {
/*  80 */       return getClusterNode((String)clusterNames.get(0)).client();
/*     */     }
/*     */     
/*     */ 
/*  84 */     OrderlessMultiKey key = new OrderlessMultiKey(clusterNames);
/*  85 */     Node node = (Node)this.tribeNodes.get(key);
/*  86 */     if (node == null) {
/*  87 */       log.info("Creating tribe node for clusters [{}]", Joiner.on(",").join(clusterNames));
/*  88 */       Map<String, String> tribeNodeConfig = createTribeNodeConfig(clusterNames);
/*  89 */       synchronized (this) {
/*  90 */         node = (Node)this.tribeNodes.get(key);
/*  91 */         if (node == null) {
/*  92 */           node = createTribeNode(tribeNodeConfig);
/*     */         }
/*     */       }
/*     */     }
/*  96 */     return node.client();
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
/*     */   Map<String, String> createTribeNodeConfig(List<String> clusterNames)
/*     */     throws IllegalArgumentException
/*     */   {
/* 126 */     Map<String, String> tribeNodeConfig = new HashMap();
/* 127 */     int index = 0;
/* 128 */     for (String clusterName : clusterNames) {
/* 129 */       Node clusterNode = getClusterNode(clusterName);
/* 130 */       Map<String, String> clusterNodeSettings = clusterNode.settings().getAsMap();
/*     */       
/* 132 */       prefix = "tribe.t" + ++index + ".";
/* 133 */       for (Map.Entry<String, String> entry : clusterNodeSettings.entrySet())
/*     */       {
/*     */ 
/*     */ 
/* 137 */         if (!((String)entry.getKey()).equals("transport.tcp.port"))
/*     */         {
/*     */ 
/*     */ 
/* 141 */           tribeNodeConfig.put(prefix + (String)entry.getKey(), entry.getValue()); } }
/*     */     }
/*     */     String prefix;
/* 144 */     return tribeNodeConfig;
/*     */   }
/*     */   
/*     */   Node getClusterNode(String clusterName) {
/* 148 */     Node node = (Node)this.nodes.get(clusterName);
/* 149 */     if (node == null) {
/* 150 */       throw new IllegalArgumentException("No elasticsearch node configured for cluster [" + clusterName + "]");
/*     */     }
/*     */     
/* 153 */     return node;
/*     */   }
/*     */   
/*     */   public List<Client> getAllClusterClients() {
/* 157 */     List<Client> clients = new ArrayList();
/* 158 */     for (Node node : this.nodes.values()) {
/* 159 */       clients.add(node.client());
/*     */     }
/* 161 */     return clients;
/*     */   }
/*     */   
/*     */   public List<Client> getAllClients() {
/* 165 */     List<Client> clients = new ArrayList();
/* 166 */     for (Node node : this.nodes.values()) {
/* 167 */       clients.add(node.client());
/*     */     }
/* 169 */     for (Node node : this.tribeNodes.values()) {
/* 170 */       clients.add(node.client());
/*     */     }
/* 172 */     return clients;
/*     */   }
/*     */   
/*     */   public List<String> getAllClusterNames() {
/* 176 */     return new ArrayList(this.nodes.keySet());
/*     */   }
/*     */   
/*     */   Collection<Node> getAllNodes() {
/* 180 */     Collection<Node> allNodes = Lists.newArrayList(this.nodes.values());
/* 181 */     allNodes.addAll(this.tribeNodes.values());
/* 182 */     return allNodes;
/*     */   }
/*     */   
/*     */   public void addClient(Map<String, String> config) {
/* 186 */     if (!isFragmentEnabled(config)) {
/* 187 */       log.info("Did not add ES client [{}] since it is not enabled", mapToString(config));
/* 188 */       return;
/*     */     }
/*     */     
/* 191 */     if (isTribeConfig(config)) {
/* 192 */       log.info("Adding ES tribe node client [{}]", mapToString(config));
/* 193 */       createTribeNode(config);
/*     */     } else {
/* 195 */       log.info("Adding ES client [{}]", mapToString(config));
/* 196 */       createNode(config);
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isTribeConfig(Map<String, String> config) {
/* 201 */     for (String key : config.keySet()) {
/* 202 */       if (key.startsWith("tribe.")) {
/* 203 */         return true;
/*     */       }
/*     */     }
/* 206 */     return false;
/*     */   }
/*     */   
/*     */   void updateClient(String oldClusterName, Map<String, String> config) {
/* 210 */     log.debug("Removing ES client [{}] and adding ES client [{}]", oldClusterName, mapToString(config));
/* 211 */     if (oldClusterName != null) {
/* 212 */       removeClient(oldClusterName);
/*     */     }
/* 214 */     addClient(config);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isFragmentEnabled(Map<String, String> config)
/*     */   {
/* 223 */     String fragmentEnabled = (String)config.get("ad.enabled");
/* 224 */     if ((fragmentEnabled != null) && (!Boolean.parseBoolean(fragmentEnabled))) {
/* 225 */       return false;
/*     */     }
/* 227 */     return true;
/*     */   }
/*     */   
/*     */   void removeClient(String clusterName) {
/* 231 */     log.debug("Removing ES client [{}]", clusterName);
/* 232 */     Node oldNode = (Node)this.nodes.remove(clusterName);
/* 233 */     if (oldNode != null) {
/* 234 */       oldNode.close();
/*     */     }
/*     */   }
/*     */   
/*     */   String mapToString(Map<String, String> config) {
/* 239 */     if (config == null) {
/* 240 */       return "{}";
/*     */     }
/* 242 */     return Joiner.on(",").withKeyValueSeparator("=").join(config);
/*     */   }
/*     */   
/*     */   Node createNode(Map<String, String> nodeConfig)
/*     */   {
/* 247 */     String clusterName = (String)nodeConfig.get("cluster.name");
/* 248 */     ensureNodeNotExists(clusterName);
/*     */     
/*     */ 
/* 251 */     Settings settings = ImmutableSettings.builder().put(this.config.getCommonNodeSettings()).put(nodeConfig).build();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 256 */     if (log.isInfoEnabled()) {
/* 257 */       log.info("Client node [{}] has been configured with [{}]", clusterName, Joiner.on(',').withKeyValueSeparator("=").join(this.config.getCommonNodeSettings()));
/*     */       
/* 259 */       log.info("Call timeout has been set to [{}] milliseconds", Long.valueOf(this.config.getCallTimeout().toMilliseconds()));
/*     */     }
/*     */     
/* 262 */     Node node = buildNode(settings);
/* 263 */     this.nodes.put(clusterName, node);
/*     */     
/* 265 */     return node;
/*     */   }
/*     */   
/*     */   Node createTribeNode(Map<String, String> nodeConfig) {
/* 269 */     Set<String> clusterNames = findTribeMemberClusterNames(nodeConfig);
/* 270 */     ensureTribeNodeNotExists(clusterNames);
/*     */     
/* 272 */     ImmutableSettings.Builder settingsBuilder = ImmutableSettings.builder().put(this.config.getCommonTribeNodeSettings());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 277 */     Set<String> connectionNames = findConnectionNames(nodeConfig);
/* 278 */     for (String connectionName : connectionNames) {
/* 279 */       Map<String, String> prefixedCommonNodeSettings = new HashMap();
/* 280 */       String prefix = "tribe." + connectionName + ".";
/* 281 */       for (Map.Entry<String, String> nonPrefixedSetting : this.config.getCommonNodeSettings().entrySet()) {
/* 282 */         prefixedCommonNodeSettings.put(prefix + (String)nonPrefixedSetting.getKey(), nonPrefixedSetting.getValue());
/*     */       }
/* 284 */       settingsBuilder.put(prefixedCommonNodeSettings);
/*     */     }
/*     */     
/*     */ 
/* 288 */     settingsBuilder.put("ad.tribe.cluster.name", Joiner.on("___").join(clusterNames));
/*     */     
/* 290 */     Settings settings = settingsBuilder.put(nodeConfig).build();
/*     */     
/* 292 */     if (log.isInfoEnabled()) {
/* 293 */       log.info("Client tribe node has been configured with [{}]", Joiner.on(',').withKeyValueSeparator("=").join(settings.getAsMap()));
/*     */       
/* 295 */       log.info("Call timeout has been set to [{}] milliseconds", Long.valueOf(this.config.getCallTimeout().toMilliseconds()));
/*     */     }
/*     */     
/* 298 */     Node node = buildNode(settings);
/* 299 */     this.tribeNodes.put(new OrderlessMultiKey(clusterNames), node);
/*     */     
/* 301 */     return node;
/*     */   }
/*     */   
/*     */   void ensureNodeNotExists(String clusterName) {
/* 305 */     if (this.nodes.get(clusterName) != null) {
/* 306 */       throw new IllegalArgumentException("Attempt made to create new node client for cluster [" + clusterName + "] but a node with that name already exists.");
/*     */     }
/*     */   }
/*     */   
/*     */   void ensureTribeNodeNotExists(Set<String> clusterNames)
/*     */   {
/* 312 */     if (this.tribeNodes.get(new OrderlessMultiKey(clusterNames)) != null) {
/* 313 */       throw new IllegalArgumentException("Attempt made to create new tribe node client for clusters [" + Joiner.on(",").join(clusterNames) + "] but one already exists.");
/*     */     }
/*     */   }
/*     */   
/*     */   Node buildNode(Settings settings)
/*     */   {
/* 319 */     Node node = NodeBuilder.nodeBuilder().loadConfigSettings(false).settings(settings).node();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 324 */     return node;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Set<String> findConnectionNames(Map<String, String> tribeNodeConfig)
/*     */   {
/* 332 */     Set<String> connectionNames = new HashSet();
/* 333 */     Matcher reuseableMatcher = TRIBE_CONNECTION_NAME_PATTERN.matcher("");
/* 334 */     for (String key : tribeNodeConfig.keySet()) {
/* 335 */       reuseableMatcher.reset(key);
/* 336 */       if ((reuseableMatcher.matches()) && (reuseableMatcher.groupCount() > 0)) {
/* 337 */         connectionNames.add(reuseableMatcher.group(1));
/*     */       }
/*     */     }
/* 340 */     return connectionNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Set<String> findTribeMemberClusterNames(Map<String, String> tribeNodeConfig)
/*     */   {
/* 348 */     Set<String> clusterNames = new HashSet();
/* 349 */     Matcher reuseableMatcher = TRIBE_CONNECTION_NAME_PATTERN.matcher("");
/* 350 */     for (Map.Entry<String, String> entry : tribeNodeConfig.entrySet()) {
/* 351 */       reuseableMatcher.reset((CharSequence)entry.getKey());
/* 352 */       if (reuseableMatcher.matches()) {
/* 353 */         clusterNames.add(entry.getValue());
/*     */       }
/*     */     }
/* 356 */     return clusterNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @PreDestroy
/*     */   void stop()
/*     */   {
/* 364 */     for (Node node : this.tribeNodes.values()) {
/* 365 */       close(node);
/*     */     }
/* 367 */     for (Node node : this.nodes.values()) {
/* 368 */       close(node);
/*     */     }
/*     */   }
/*     */   
/*     */   void close(Node node) {
/* 373 */     node.client().close();
/* 374 */     if (!node.isClosed()) {
/* 375 */       node.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class OrderlessMultiKey
/*     */   {
/*     */     final ImmutableList<String> keys;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     final int hashCode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public OrderlessMultiKey(Collection<String> keys)
/*     */     {
/* 398 */       List<String> tempKeys = new ArrayList(keys);
/* 399 */       Collections.sort(tempKeys);
/* 400 */       this.keys = ImmutableList.copyOf(tempKeys);
/* 401 */       this.hashCode = this.keys.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 406 */       if (obj == this) {
/* 407 */         return true;
/*     */       }
/* 409 */       if ((obj instanceof OrderlessMultiKey)) {
/* 410 */         OrderlessMultiKey other = (OrderlessMultiKey)obj;
/* 411 */         return this.keys.equals(other.keys);
/*     */       }
/* 413 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 418 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/multi/ElasticSearchMultiNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */