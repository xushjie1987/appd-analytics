/*     */ package com.appdynamics.analytics.processor.elasticsearch.node.multi;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*     */ import com.appdynamics.analytics.processor.zookeeper.client.ZookeeperVersionManager;
/*     */ import com.appdynamics.analytics.processor.zookeeper.exception.ZooKeeperExceptionUtils;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.cache.Cache;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMap.Builder;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.PreDestroy;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.curator.framework.CuratorFramework;
/*     */ import org.apache.curator.framework.api.DeleteBuilder;
/*     */ import org.apache.curator.framework.api.GetDataBuilder;
/*     */ import org.apache.curator.framework.api.SetDataBuilder;
/*     */ import org.apache.curator.framework.api.transaction.CuratorTransaction;
/*     */ import org.apache.curator.framework.api.transaction.CuratorTransactionBridge;
/*     */ import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
/*     */ import org.apache.curator.framework.api.transaction.TransactionDeleteBuilder;
/*     */ import org.apache.curator.framework.listen.ListenerContainer;
/*     */ import org.apache.curator.framework.recipes.cache.ChildData;
/*     */ import org.apache.curator.framework.recipes.cache.PathChildrenCache;
/*     */ import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
/*     */ import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
/*     */ import org.apache.curator.framework.recipes.locks.InterProcessMutex;
/*     */ import org.apache.curator.utils.ZKPaths;
/*     */ import org.apache.zookeeper.KeeperException;
/*     */ import org.apache.zookeeper.KeeperException.NoNodeException;
/*     */ import org.elasticsearch.ElasticsearchException;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.admin.cluster.stats.ClusterStatsIndices;
/*     */ import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
/*     */ import org.elasticsearch.client.AdminClient;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.client.ClusterAdminClient;
/*     */ import org.elasticsearch.common.settings.Settings;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ @Singleton
/*     */ public class DefaultClusterRouter implements ClusterRouter
/*     */ {
/*  61 */   private static final Logger log = LoggerFactory.getLogger(DefaultClusterRouter.class);
/*     */   
/*     */ 
/*     */   static final int CLUSTER_ROUTING_VERSION = 1;
/*     */   
/*     */ 
/*  67 */   static final Pattern DYNAMIC_ROUTED_CLUSTERS_RULE_PATTERN = Pattern.compile("^clusters\\.dynamic\\.rule\\.(.*?)$");
/*  68 */   static final String DYNAMIC_RULE_FORMAT_STRING = ZKPaths.makePath(ANALYTICS_ZK_PATH, "clusters.dynamic.rule.%s");
/*     */   
/*     */ 
/*     */   static final int LOCK_WAIT_TIME_MILLIS = 5000;
/*     */   
/*     */ 
/*     */   static final int CLUSTER_SIZE_CACHE_SIZE = 100;
/*     */   
/*     */ 
/*     */   static final int CLUSTER_SIZE_CACHE_EXPIRE_MINUTES = 120;
/*     */   
/*     */ 
/*     */   static final String CLUSTER_NAME_SETTING = "cluster.name";
/*     */   
/*     */ 
/*     */   volatile List<String> dynamicClusters;
/*     */   
/*     */ 
/*     */   volatile ImmutableMap<String, ImmutableList<String>> dynamicRoutes;
/*     */   
/*     */ 
/*     */   volatile Map<String, Integer> dynamicClusterCounts;
/*     */   
/*     */ 
/*     */   volatile Cache<String, Long> dynamicClusterSizes;
/*     */   
/*     */ 
/*     */   volatile String adminCluster;
/*     */   
/*     */ 
/*     */   PathChildrenCache propsCache;
/*     */   
/*     */ 
/*     */   final ClientProvider clientProvider;
/*     */   
/*     */ 
/*     */   final CuratorFramework zkClient;
/*     */   
/*     */ 
/*     */   final ElasticSearchMultiNodeConfiguration configuration;
/*     */   
/*     */ 
/*     */   final ZookeeperVersionManager versionManager;
/*     */   
/*     */ 
/*     */   public static String getAccountNameFromPath(String path)
/*     */   {
/* 115 */     String accountName = getAccountFromPathForPattern(DYNAMIC_ROUTED_CLUSTERS_RULE_PATTERN, path);
/*     */     
/* 117 */     return accountName;
/*     */   }
/*     */   
/*     */   private static String getAccountFromPathForPattern(Pattern pattern, String path)
/*     */   {
/* 122 */     if (path.startsWith("/")) {
/* 123 */       path = ZKPaths.getNodeFromPath(path);
/*     */     }
/* 125 */     Matcher matcher = pattern.matcher(path);
/* 126 */     if ((matcher.matches()) && (matcher.groupCount() >= 1)) {
/* 127 */       return matcher.group(1);
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */   
/*     */   @Inject
/*     */   public DefaultClusterRouter(ClientProvider clientProvider, CuratorFramework zkClient, ElasticSearchMultiNodeConfiguration configuration)
/*     */   {
/* 135 */     this.clientProvider = clientProvider;
/* 136 */     this.zkClient = zkClient;
/* 137 */     this.configuration = configuration;
/* 138 */     this.dynamicClusterSizes = CacheBuilder.newBuilder().maximumSize(100L).expireAfterWrite(120L, TimeUnit.MINUTES).build();
/*     */     
/*     */ 
/*     */ 
/* 142 */     this.versionManager = new ZookeeperVersionManager(zkClient, ANALYTICS_ZK_PATH);
/*     */   }
/*     */   
/*     */   @javax.annotation.PostConstruct
/*     */   void onStart() throws Exception {
/* 147 */     this.versionManager.checkSchemaVersion("Cluster routing table", 1);
/*     */     
/* 149 */     this.propsCache = new PathChildrenCache(this.zkClient, ANALYTICS_ZK_PATH, true);
/* 150 */     this.propsCache.start();
/*     */     
/* 152 */     PathChildrenCacheListener listener = new PathChildrenCacheListener()
/*     */     {
/*     */       public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
/* 155 */         if ((event == null) || (event.getData() == null)) {
/* 156 */           return;
/*     */         }
/*     */         
/* 159 */         String path = event.getData().getPath();
/* 160 */         byte[] rawData = event.getData().getData();
/* 161 */         String data = rawData != null ? new String(rawData, "UTF-8") : null;
/*     */         
/* 163 */         switch (DefaultClusterRouter.2.$SwitchMap$org$apache$curator$framework$recipes$cache$PathChildrenCacheEvent$Type[event.getType().ordinal()]) {
/*     */         case 1: 
/* 165 */           DefaultClusterRouter.log.info("Zookeeper node added [{}] with value [{}]", path, data);
/* 166 */           break;
/*     */         case 2: 
/* 168 */           DefaultClusterRouter.log.info("Zookeeper node updated [{}] with value [{}]", path, data);
/* 169 */           break;
/*     */         case 3: 
/* 171 */           DefaultClusterRouter.log.info("Zookeeper node removed [{}]", path);
/* 172 */           break;
/*     */         default: 
/* 174 */           DefaultClusterRouter.log.info("Received zookeeper event of unknown type [{}] value [{}]", path, data);
/*     */         }
/*     */         
/*     */         
/* 178 */         DefaultClusterRouter.this.buildRoutes();
/*     */       }
/* 180 */     };
/* 181 */     this.propsCache.getListenable().addListener(listener);
/* 182 */     buildRoutes();
/*     */     
/* 184 */     for (String clusterName : this.dynamicClusters) {
/* 185 */       Long clusterSize = findClusterSize(clusterName);
/* 186 */       if (clusterSize != null) {
/* 187 */         this.dynamicClusterSizes.put(clusterName, clusterSize);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   void onStop() {
/* 194 */     if (this.propsCache != null) {
/*     */       try {
/* 196 */         this.propsCache.close();
/*     */       } catch (IOException e) {
/* 198 */         log.error("Could not close zookeeper properties path cache.", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void buildRoutes()
/*     */   {
/* 207 */     synchronized (this)
/*     */     {
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/* 214 */         if (this.zkClient.getState() == org.apache.curator.framework.imps.CuratorFrameworkState.STARTED) {
/* 215 */           this.propsCache.rebuild();
/*     */         } else {
/* 217 */           log.debug("Failed to rebuild the PathChildrenCache due to the fact that the rebuild method was called after the associated CuratorFramework instance was stopped");
/*     */           
/* 219 */           return;
/*     */         }
/*     */       } catch (Exception e) {
/* 222 */         log.error("Could not rebuild zookeeper properties cache", e);
/* 223 */         return;
/*     */       }
/*     */       
/* 226 */       List<String> dynamicClusters = new ArrayList();
/* 227 */       Map<String, String> otherProps = new HashMap();
/*     */       
/* 229 */       List<ChildData> children = this.propsCache.getCurrentData();
/* 230 */       for (ChildData child : children) {
/*     */         try {
/* 232 */           String node = ZKPaths.getNodeFromPath(child.getPath());
/* 233 */           String data = new String(child.getData(), "UTF-8");
/*     */           
/* 235 */           if (StringUtils.isNotBlank(data)) {
/* 236 */             if ("clusters.dynamic".equals(node)) {
/* 237 */               dynamicClusters.addAll(Arrays.asList(data.split(",")));
/* 238 */             } else if ("cluster.admin".equals(node)) {
/* 239 */               this.adminCluster = data;
/* 240 */             } else if (!"version".equals(node))
/*     */             {
/* 242 */               otherProps.put(node, data);
/*     */             }
/*     */           }
/*     */         } catch (Exception e) {
/* 246 */           log.error("Could not build route rule for zookeeper child [" + child + "]", e);
/*     */         }
/*     */       }
/*     */       
/* 250 */       ImmutableMap<String, ImmutableList<String>> dynamicRoutes = buildMap(otherProps, DYNAMIC_ROUTED_CLUSTERS_RULE_PATTERN);
/*     */       
/*     */ 
/* 253 */       Map<String, Integer> dynamicClusterCounts = countCustomersPerCluster(otherProps, dynamicClusters);
/*     */       
/* 255 */       this.dynamicRoutes = dynamicRoutes;
/* 256 */       this.dynamicClusters = dynamicClusters;
/* 257 */       this.dynamicClusterCounts = dynamicClusterCounts;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Map<String, Integer> countCustomersPerCluster(Map<String, String> mappings, List<String> clusters)
/*     */   {
/* 266 */     HashMap<String, Integer> countMap = new HashMap();
/* 267 */     for (String cluster : clusters) {
/* 268 */       countMap.put(cluster, Integer.valueOf(0));
/*     */     }
/*     */     
/* 271 */     for (String cluster : mappings.values()) {
/* 272 */       Integer count = (Integer)countMap.get(cluster);
/* 273 */       if (count != null) {
/* 274 */         countMap.put(cluster, Integer.valueOf(count.intValue() + 1));
/*     */       }
/*     */     }
/*     */     
/* 278 */     return countMap;
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
/*     */   ImmutableMap<String, ImmutableList<String>> buildMap(Map<String, String> props, Pattern pattern)
/*     */   {
/* 292 */     ImmutableMap.Builder<String, ImmutableList<String>> routes = new ImmutableMap.Builder();
/* 293 */     for (String key : props.keySet()) {
/* 294 */       Matcher matcher = pattern.matcher(key);
/* 295 */       if ((matcher.matches()) && (matcher.groupCount() >= 1)) {
/* 296 */         String clusterNamesString = (String)props.get(key);
/* 297 */         ImmutableList<String> clusterNames = ImmutableList.copyOf(clusterNamesString.split(","));
/* 298 */         String accountName = matcher.group(1);
/* 299 */         if (StringUtils.isNotBlank(accountName)) {
/* 300 */           routes.put(accountName, ImmutableList.copyOf(clusterNames));
/*     */         } else {
/* 302 */           log.warn("A zookeeper routing property [" + key + "] was detected that did not contain an accountName");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 307 */     return routes.build();
/*     */   }
/*     */   
/*     */   public boolean setAdminCluster(String clusterName) throws Exception
/*     */   {
/* 312 */     String path = ZKPaths.makePath(ANALYTICS_ZK_PATH, "cluster.admin");
/* 313 */     return upsert(path, clusterName, true);
/*     */   }
/*     */   
/*     */   public boolean updateAdminCluster(String clusterName) throws Exception
/*     */   {
/* 318 */     String path = ZKPaths.makePath(ANALYTICS_ZK_PATH, "cluster.admin");
/* 319 */     return upsert(path, clusterName, false);
/*     */   }
/*     */   
/*     */   public String findAdminCluster()
/*     */   {
/* 324 */     return this.adminCluster;
/*     */   }
/*     */   
/*     */   public List<String> findClusters(String accountName) throws Exception
/*     */   {
/* 329 */     accountName = accountName != null ? accountName.toLowerCase() : null;
/*     */     
/* 331 */     List<String> routedClusters = (List)this.dynamicRoutes.get(accountName);
/* 332 */     if (routedClusters != null) {
/* 333 */       return routedClusters;
/*     */     }
/*     */     
/*     */ 
/* 337 */     String targetCluster = findLeastUtilizedCluster();
/* 338 */     if (targetCluster == null) {
/* 339 */       throw new Exception("Could not find a least utilized target cluster.  Configured dynamic clusters [" + Joiner.on(',').join(this.dynamicClusters) + "]");
/*     */     }
/*     */     
/*     */ 
/* 343 */     synchronized (this)
/*     */     {
/*     */ 
/* 346 */       routedClusters = (List)this.dynamicRoutes.get(accountName);
/* 347 */       if (routedClusters != null) {
/* 348 */         return routedClusters;
/*     */       }
/*     */       
/* 351 */       log.info("Assigning account [{}] to cluster [{}]", accountName, targetCluster);
/*     */       
/*     */ 
/* 354 */       String routingPropPath = String.format(DYNAMIC_RULE_FORMAT_STRING, new Object[] { accountName });
/*     */       
/*     */ 
/* 357 */       String pathPropValue = getNodeData(routingPropPath);
/* 358 */       if (pathPropValue != null) {
/* 359 */         targetCluster = pathPropValue;
/*     */       }
/*     */       else {
/* 362 */         InterProcessMutex lock = new InterProcessMutex(this.zkClient, routingPropPath);
/* 363 */         if (lock.acquire(5000L, TimeUnit.MILLISECONDS)) {
/*     */           try
/*     */           {
/* 366 */             String doubleCheckPathPropValue = getNodeData(routingPropPath);
/* 367 */             if (doubleCheckPathPropValue != null) {
/* 368 */               targetCluster = doubleCheckPathPropValue;
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 373 */               setNodeData(routingPropPath, targetCluster);
/* 374 */               log.info("Created dynamic routing rule for account [{}] to cluster [{}]", accountName, targetCluster);
/*     */               
/* 376 */               logThresholdBreaches(targetCluster);
/*     */             }
/*     */           } catch (JsonProcessingException e) {
/* 379 */             throw new Exception("Could not create json rule mapping for account [" + accountName + "]", e);
/*     */           } finally {
/*     */             try {
/* 382 */               lock.release();
/*     */             } catch (Exception e) {
/* 384 */               log.error("Could not release zookeeper write lock", e);
/*     */             }
/*     */           }
/*     */         }
/* 388 */         throw new Exception("Could not acquire lock to write [" + routingPropPath + "]");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 393 */       buildRoutes();
/*     */     }
/* 395 */     return Arrays.asList(new String[] { targetCluster });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void logThresholdBreaches(String targetCluster)
/*     */   {
/*     */     try
/*     */     {
/* 404 */       int numAccountsPerCluster = ((Integer)this.dynamicClusterCounts.get(targetCluster)).intValue() + 1;
/* 405 */       String logMessage = "Cluster [{}] has [{}] customers which is over the threshold of [{}] customers per cluster. A new ES cluster needs to be created and added as a routable cluster ";
/*     */       
/* 407 */       if (numAccountsPerCluster > this.configuration.getCustomersPerClusterErrorThreshold()) {
/* 408 */         log.error(logMessage + "immediately!", new Object[] { targetCluster, Integer.valueOf(numAccountsPerCluster), Integer.valueOf(this.configuration.getCustomersPerClusterErrorThreshold()) });
/*     */       }
/* 410 */       else if ((numAccountsPerCluster > this.configuration.getCustomersPerClusterWarnThreshold()) && (log.isWarnEnabled()))
/*     */       {
/* 412 */         log.warn(logMessage + "soon.", new Object[] { targetCluster, Integer.valueOf(numAccountsPerCluster), Integer.valueOf(this.configuration.getCustomersPerClusterWarnThreshold()) });
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 416 */       log.error("Could not logThresholdBreaches", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getAllDynamicClusters()
/*     */   {
/* 422 */     return new ArrayList(this.dynamicClusters);
/*     */   }
/*     */   
/*     */   public Map<String, String> listAllRouteProperties()
/*     */   {
/* 427 */     Map<String, String> allRouteProps = new HashMap();
/* 428 */     String ignorePath; synchronized (this) {
/*     */       try {
/* 430 */         this.propsCache.rebuild();
/*     */       } catch (Exception e) {
/* 432 */         log.error("Could not rebuild zookeeper properties cache", e);
/* 433 */         return allRouteProps;
/*     */       }
/*     */       
/* 436 */       List<ChildData> childData = this.propsCache.getCurrentData();
/* 437 */       ignorePath = this.versionManager.getZkSchemaVersionPath();
/* 438 */       for (ChildData child : childData) {
/*     */         try {
/* 440 */           String path = child.getPath();
/* 441 */           if (!ignorePath.equals(path)) {
/* 442 */             allRouteProps.put(child.getPath(), new String(child.getData(), "UTF-8"));
/*     */           }
/*     */         } catch (UnsupportedEncodingException e) {
/* 445 */           log.error("Could not parse data for ZK property " + child.getPath(), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 450 */     return allRouteProps;
/*     */   }
/*     */   
/*     */   public List<String> createDynamicRoute(List<String> accountNames, List<String> clusterNames) throws Exception
/*     */   {
/* 455 */     return handleRouteAction(accountNames, clusterNames, true);
/*     */   }
/*     */   
/*     */   public List<String> modifyDynamicRoute(List<String> accountNames, List<String> clusterNames) throws Exception
/*     */   {
/* 460 */     return handleRouteAction(accountNames, clusterNames, false);
/*     */   }
/*     */   
/*     */   List<String> handleRouteAction(List<String> accountNames, List<String> clusterNames, boolean isCreate) throws Exception
/*     */   {
/* 465 */     List<String> createdAccounts = new ArrayList();
/* 466 */     for (String accountName : accountNames) {
/* 467 */       String path = getRoutePath(DYNAMIC_RULE_FORMAT_STRING, accountName);
/* 468 */       if (upsert(path, clusterNames, isCreate)) {
/* 469 */         createdAccounts.add(accountName);
/*     */       }
/*     */     }
/* 472 */     return createdAccounts;
/*     */   }
/*     */   
/*     */   public void removeDynamicRoute(List<String> accountNames) throws Exception
/*     */   {
/* 477 */     for (String accountName : accountNames) {
/* 478 */       String zkPath = getRoutePath(DYNAMIC_RULE_FORMAT_STRING, accountName);
/* 479 */       removeNodeData(zkPath);
/*     */     }
/*     */   }
/*     */   
/*     */   public void bulkRemoveDynamicRoutes(Collection<String> accounts) throws Exception
/*     */   {
/* 485 */     if (accounts.isEmpty()) {
/* 486 */       return;
/*     */     }
/* 488 */     CuratorTransaction baseDelete = this.zkClient.inTransaction();
/* 489 */     for (String accountName : accounts) {
/* 490 */       String zkPath = getRoutePath(DYNAMIC_RULE_FORMAT_STRING, accountName);
/* 491 */       baseDelete = ((CuratorTransactionBridge)baseDelete.delete().forPath(zkPath)).and();
/*     */     }
/* 493 */     CuratorTransactionFinal finalDelete = (CuratorTransactionFinal)baseDelete;
/* 494 */     finalDelete.commit();
/*     */   }
/*     */   
/*     */   public ImmutableMap<String, ImmutableList<String>> getAllDynamicRoutes() throws Exception
/*     */   {
/* 499 */     return ImmutableMap.copyOf(this.dynamicRoutes);
/*     */   }
/*     */   
/*     */   public void seedRoutes(SeedClusterRoutes seed)
/*     */   {
/* 504 */     if (seed != null) {
/* 505 */       String adminCluster = seed.getAdminCluster();
/* 506 */       String dynamicClusters = seed.getDynamicClusters();
/*     */       
/* 508 */       if (!Strings.isNullOrEmpty(adminCluster)) {
/*     */         try {
/* 510 */           boolean created = upsert(ZKPaths.makePath(ANALYTICS_ZK_PATH, "cluster.admin"), adminCluster, true);
/* 511 */           if (created) {
/* 512 */             log.info("Admin cluster set to [{}]", adminCluster);
/*     */           } else {
/* 514 */             log.debug("Did not set admin cluster to [{}] since it was already set.", adminCluster);
/*     */           }
/*     */         } catch (Exception e) {
/* 517 */           log.error("Error while attempting to set the admin cluster to [" + adminCluster + "]", e);
/*     */         }
/*     */       }
/*     */       
/* 521 */       if (!Strings.isNullOrEmpty(dynamicClusters)) {
/*     */         try {
/* 523 */           boolean created = upsert(ZKPaths.makePath(ANALYTICS_ZK_PATH, "clusters.dynamic"), dynamicClusters, true);
/*     */           
/* 525 */           if (created) {
/* 526 */             log.info("Dynamic clusters set to [{}]", dynamicClusters);
/*     */           } else {
/* 528 */             log.debug("Did not set dynamic clusters to [{}] since it was already set.", dynamicClusters);
/*     */           }
/*     */         } catch (Exception e) {
/* 531 */           log.error("Error while attempting to set the dynamic clusters to [" + dynamicClusters + "]", e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   String getRoutePath(String formattingRule, String accountName)
/*     */   {
/* 542 */     accountName = accountName == null ? null : accountName.toLowerCase();
/*     */     
/* 544 */     String path = String.format(formattingRule, new Object[] { accountName });
/* 545 */     return path;
/*     */   }
/*     */   
/*     */   boolean upsert(String path, String clusterName, boolean create) throws Exception {
/* 549 */     return upsert(path, Arrays.asList(new String[] { clusterName }), create);
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
/*     */   boolean upsert(String path, List<String> clusterNames, boolean create)
/*     */     throws Exception
/*     */   {
/* 564 */     String data = getNodeData(path);
/* 565 */     if (!shouldUpsert(data, create)) {
/* 566 */       return false;
/*     */     }
/*     */     
/* 569 */     String clusterNamesAsString = Joiner.on(",").join(clusterNames);
/*     */     
/* 571 */     InterProcessMutex lock = new InterProcessMutex(this.zkClient, path);
/* 572 */     if (lock.acquire(5000L, TimeUnit.MILLISECONDS)) {
/*     */       try
/*     */       {
/* 575 */         data = getNodeData(path);
/* 576 */         boolean bool; if (!shouldUpsert(data, create)) {
/* 577 */           return false;
/*     */         }
/*     */         
/* 580 */         setNodeData(path, clusterNamesAsString);
/* 581 */         log.info(create ? "Added" : "Modified routing rule [{}] cluster(s) [{}]", path, clusterNamesAsString);
/*     */         
/* 583 */         buildRoutes();
/* 584 */         return true;
/*     */       } finally {
/*     */         try {
/* 587 */           lock.release();
/*     */         } catch (Exception e) {
/* 589 */           log.error("Could not release zookeeper write lock", e);
/*     */         }
/*     */       }
/*     */     }
/* 593 */     throw new Exception("Could not acquire lock to add route [" + path + "] clusters [" + clusterNamesAsString + "]");
/*     */   }
/*     */   
/*     */ 
/*     */   boolean shouldUpsert(String data, boolean isCreate)
/*     */   {
/* 599 */     if ((!Strings.isNullOrEmpty(data)) && (isCreate))
/*     */     {
/* 601 */       return false; }
/* 602 */     if ((Strings.isNullOrEmpty(data)) && (!isCreate))
/*     */     {
/* 604 */       return false;
/*     */     }
/* 606 */     return true;
/*     */   }
/*     */   
/*     */   public boolean addDynamicCluster(String clusterName) throws Exception
/*     */   {
/* 611 */     if (getAllDynamicClusters().contains(clusterName)) {
/* 612 */       return false;
/*     */     }
/* 614 */     return modifyClusterInList("clusters.dynamic", clusterName, true);
/*     */   }
/*     */   
/*     */   public boolean removeDynamicCluster(String clusterName) throws Exception
/*     */   {
/* 619 */     if (!getAllDynamicClusters().contains(clusterName)) {
/* 620 */       return false;
/*     */     }
/* 622 */     return modifyClusterInList("clusters.dynamic", clusterName, false);
/*     */   }
/*     */   
/*     */   boolean modifyClusterInList(String lastPathComponent, String clusterName, boolean isAdd) throws Exception {
/* 626 */     String path = ZKPaths.makePath(ClusterRouter.ANALYTICS_ZK_PATH, lastPathComponent);
/*     */     
/* 628 */     InterProcessMutex lock = new InterProcessMutex(this.zkClient, path);
/* 629 */     if (lock.acquire(5000L, TimeUnit.MILLISECONDS)) {
/*     */       try
/*     */       {
/* 632 */         String clusterNamesRaw = getNodeData(path);
/*     */         List<String> clusterNames;
/* 634 */         List<String> clusterNames; if (StringUtils.isNotBlank(clusterNamesRaw)) {
/* 635 */           clusterNames = com.google.common.collect.Lists.newArrayList(clusterNamesRaw.split(","));
/*     */         } else {
/* 637 */           clusterNames = new ArrayList();
/*     */         }
/*     */         boolean bool;
/* 640 */         if (isAdd) {
/* 641 */           if (clusterNames.contains(clusterName)) {
/* 642 */             return false;
/*     */           }
/*     */           
/* 645 */           clusterNames.add(clusterName);
/* 646 */           setNodeData(path, Joiner.on(",").join(clusterNames));
/* 647 */           log.info("Added cluster [{}] to path [{}]", clusterName, path);
/*     */         }
/*     */         else {
/* 650 */           if (!clusterNames.contains(clusterName)) {
/* 651 */             return false;
/*     */           }
/*     */           
/* 654 */           clusterNames.remove(clusterName);
/* 655 */           setNodeData(path, Joiner.on(",").join(clusterNames));
/* 656 */           log.info("Removed cluster [{}] from path [{}]", clusterName, path);
/*     */         }
/*     */         
/*     */ 
/* 660 */         buildRoutes();
/* 661 */         return true;
/*     */       } finally {
/*     */         try {
/* 664 */           lock.release();
/*     */         } catch (Exception e) {
/* 666 */           log.error("Could not release zookeeper write lock", e);
/*     */         }
/*     */       }
/*     */     }
/* 670 */     throw new Exception("Could not acquire lock to add cluster [" + clusterName + "] to [" + path + "]");
/*     */   }
/*     */   
/*     */   void setNodeData(String path, String value) throws Exception
/*     */   {
/*     */     try {
/* 676 */       this.zkClient.setData().forPath(path, value.getBytes("UTF-8"));
/*     */     } catch (KeeperException e) {
/* 678 */       ZooKeeperExceptionUtils.propagateAsInternalException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   String getNodeData(String path) throws Exception {
/* 683 */     String data = null;
/*     */     try {
/* 685 */       byte[] nodeData = (byte[])this.zkClient.getData().forPath(path);
/* 686 */       if ((nodeData != null) && (nodeData.length > 0)) {
/* 687 */         data = new String(nodeData, "UTF-8");
/*     */       }
/*     */     }
/*     */     catch (KeeperException.NoNodeException e) {
/* 691 */       log.debug("Attempt to query a node that doesn't exist", e.getMessage());
/*     */     } catch (KeeperException e) {
/* 693 */       ZooKeeperExceptionUtils.propagateAsInternalException(e);
/*     */     }
/* 695 */     return data;
/*     */   }
/*     */   
/*     */   void removeNodeData(String zkPath) throws Exception {
/*     */     try {
/* 700 */       this.zkClient.delete().forPath(zkPath);
/*     */     }
/*     */     catch (KeeperException.NoNodeException e) {
/* 703 */       log.debug("Attempt to remove a node that doesn't exist", e.getMessage());
/*     */     } catch (KeeperException e) {
/* 705 */       ZooKeeperExceptionUtils.propagateAsInternalException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   String findLeastUtilizedCluster()
/*     */   {
/* 714 */     String candidate = null;
/* 715 */     double minScore = Double.MAX_VALUE;
/*     */     
/* 717 */     StringBuilder sbLog = new StringBuilder();
/*     */     
/* 719 */     RangeScalar nameAccountScalar = getDynamicClusterNameAccountScalar();
/* 720 */     RangeScalar clusterSizeScalar = getDynamicClusterSizeScalar();
/* 721 */     for (String clusterName : this.dynamicClusters) {
/* 722 */       Long clusterSize = findClusterSize(clusterName);
/* 723 */       Integer numAccounts = (Integer)this.dynamicClusterCounts.get(clusterName);
/*     */       
/* 725 */       double scaledClusterSize = clusterSizeScalar.calculateScale(clusterSize);
/* 726 */       double scaledNumAccounts = nameAccountScalar.calculateScale(numAccounts);
/*     */       
/* 728 */       double clusterSizePercentileWeight = this.configuration.getClusterSizeRoutingWeight();
/* 729 */       double numAccountPercentileWeight = this.configuration.getNumAccountsRoutingWeight();
/* 730 */       double score = scaledClusterSize * clusterSizePercentileWeight + scaledNumAccounts * numAccountPercentileWeight;
/*     */       
/*     */ 
/* 733 */       sbLog.append(clusterName).append(":[clusterSize:").append(clusterSize).append(",numAccounts:").append(numAccounts).append(",scaledClusterSize:").append(scaledClusterSize).append(",scaledNumAccounts:").append(scaledNumAccounts).append(",clusterSizePercentileWeight:").append(clusterSizePercentileWeight).append(",numAccountPercentileWeight:").append(numAccountPercentileWeight).append(",score:").append(score).append("]");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 741 */       if (score < minScore) {
/* 742 */         candidate = clusterName;
/* 743 */         minScore = score;
/*     */       }
/*     */     }
/*     */     
/* 747 */     log.info("Found least utilized cluster candidate [{}] for data [{}]", candidate, sbLog.toString());
/* 748 */     return candidate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Long findClusterSize(String clusterName)
/*     */   {
/* 757 */     Long clusterSize = (Long)this.dynamicClusterSizes.getIfPresent(clusterName);
/* 758 */     if ((clusterSize != null) && (clusterSize.longValue() > this.configuration.getClusterSizeSeedThresholdBytes())) {
/* 759 */       return clusterSize;
/*     */     }
/*     */     
/* 762 */     for (Client client : this.clientProvider.getAllInsertClients()) {
/* 763 */       if (client.settings().get("cluster.name").equals(clusterName))
/*     */       {
/*     */ 
/* 766 */         return getClusterSize(client);
/*     */       }
/*     */     }
/* 769 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Long getClusterSize(Client client)
/*     */   {
/* 778 */     String clusterName = client.settings().get("cluster.name");
/* 779 */     Long clusterSize = (Long)this.dynamicClusterSizes.getIfPresent(clusterName);
/* 780 */     if ((clusterSize == null) || (clusterSize.longValue() < this.configuration.getClusterSizeSeedThresholdBytes())) {
/*     */       try {
/* 782 */         ClusterStatsResponse response = (ClusterStatsResponse)client.admin().cluster().prepareClusterStats().execute().actionGet();
/* 783 */         clusterSize = Long.valueOf(response.getIndicesStats().getStore().getSizeInBytes());
/* 784 */         this.dynamicClusterSizes.put(clusterName, clusterSize);
/*     */       } catch (ElasticsearchException e) {
/* 786 */         log.error("Could not make ES cluster stats call", e);
/*     */       }
/*     */     }
/*     */     
/* 790 */     log.debug("Found cluster size [{}] for cluster [{}]", clusterSize, clusterName);
/* 791 */     return clusterSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   RangeScalar getDynamicClusterSizeScalar()
/*     */   {
/* 799 */     RangeScalar sizeScalar = new RangeScalar();
/* 800 */     List<Client> clients = this.clientProvider.getAllInsertClients();
/* 801 */     for (Client client : clients) {
/* 802 */       String clusterName = client.settings().get("cluster.name");
/* 803 */       Long clusterSize = (Long)this.dynamicClusterSizes.getIfPresent(clusterName);
/* 804 */       if (clusterSize != null) {
/* 805 */         sizeScalar.addValue(clusterSize);
/*     */       }
/*     */       else
/*     */         try
/*     */         {
/* 810 */           clusterSize = getClusterSize(client);
/* 811 */           sizeScalar.addValue(clusterSize);
/*     */         } catch (ElasticsearchException e) {
/* 813 */           log.error("Could not make ES cluster stats call", e);
/*     */         }
/*     */     }
/* 816 */     return sizeScalar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   RangeScalar getDynamicClusterNameAccountScalar()
/*     */   {
/* 824 */     RangeScalar namAcctScalar = new RangeScalar();
/* 825 */     for (Map.Entry<String, Integer> entry : this.dynamicClusterCounts.entrySet()) {
/* 826 */       namAcctScalar.addValue((Number)entry.getValue());
/*     */     }
/* 828 */     return namAcctScalar;
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
/*     */   static class RangeScalar
/*     */   {
/* 841 */     final List<Double> values = new ArrayList();
/* 842 */     double maxValue = 0.0D;
/*     */     
/*     */     void addValue(Number value) {
/* 845 */       if (value == null) {
/* 846 */         return;
/*     */       }
/*     */       
/* 849 */       double valAsDouble = value.doubleValue();
/* 850 */       this.values.add(Double.valueOf(valAsDouble));
/* 851 */       this.maxValue = (valAsDouble > this.maxValue ? valAsDouble : this.maxValue);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     double calculateScale(Number value)
/*     */     {
/* 860 */       if ((this.maxValue == 0.0D) || (value == null)) {
/* 861 */         return 0.0D;
/*     */       }
/* 863 */       return 100.0D * (value.doubleValue() / this.maxValue);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/multi/DefaultClusterRouter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */