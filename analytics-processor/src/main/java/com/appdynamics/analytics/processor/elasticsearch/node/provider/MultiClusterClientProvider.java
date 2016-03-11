/*     */ package com.appdynamics.analytics.processor.elasticsearch.node.provider;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ClusterRouter;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.node.multi.ElasticSearchMultiNode;
/*     */ import com.appdynamics.common.util.exception.TransientException;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ public class MultiClusterClientProvider
/*     */   implements ClientProvider
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(MultiClusterClientProvider.class);
/*     */   
/*     */ 
/*     */   @Inject
/*     */   volatile ElasticSearchMultiNode esMultiNode;
/*     */   
/*     */   @Inject
/*     */   ClusterRouter router;
/*     */   
/*     */ 
/*     */   public Client getAdminClient()
/*     */   {
/*  38 */     String clusterName = this.router.findAdminCluster();
/*  39 */     if (clusterName == null) {
/*  40 */       throw new IllegalStateException("No ES admin cluster could be resolved.  Does the zk property [cluster.admin] exist?");
/*     */     }
/*     */     
/*     */ 
/*  44 */     return this.esMultiNode.getClient(clusterName);
/*     */   }
/*     */   
/*     */   public Client getClusterClient(String clusterName)
/*     */   {
/*  49 */     return this.esMultiNode.getClient(clusterName);
/*     */   }
/*     */   
/*     */   public Client getInsertClient(String accountName)
/*     */   {
/*  54 */     log.trace("Finding insert client for account name [{}]", accountName);
/*  55 */     String clusterName = (String)findClusterNames(accountName).get(0);
/*  56 */     return this.esMultiNode.getClient(clusterName);
/*     */   }
/*     */   
/*     */   public List<Client> getAllInsertClients(String accountName)
/*     */   {
/*  61 */     log.trace("Finding all insert clients for account name [{}]", accountName);
/*  62 */     List<Client> clients = new ArrayList();
/*  63 */     List<String> clusterNames = findClusterNames(accountName);
/*  64 */     for (String clusterName : clusterNames) {
/*  65 */       clients.add(this.esMultiNode.getClient(clusterName));
/*     */     }
/*  67 */     return clients;
/*     */   }
/*     */   
/*     */   public Client getSearchClient(String accountName)
/*     */   {
/*  72 */     log.trace("Finding search client for account name [{}]", accountName);
/*  73 */     List<String> clusterNames = findClusterNames(accountName);
/*  74 */     return this.esMultiNode.getClient(clusterNames);
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
/*     */   List<String> findClusterNames(String accountName)
/*     */   {
/*     */     try
/*     */     {
/*  91 */       return this.router.findClusters(accountName);
/*     */     } catch (Exception e) {
/*  93 */       String message = "Could not find cluster(s) for account name [" + accountName + "]";
/*  94 */       log.error(message, e);
/*     */       
/*  96 */       Throwables.propagateIfInstanceOf(e, TransientException.class);
/*  97 */       throw new IllegalStateException(message, e);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Client> getAllInsertClients()
/*     */   {
/* 103 */     return ImmutableList.copyOf(this.esMultiNode.getAllClusterClients());
/*     */   }
/*     */   
/*     */   public List<Client> getAllClients()
/*     */   {
/* 108 */     return ImmutableList.copyOf(this.esMultiNode.getAllClients());
/*     */   }
/*     */   
/*     */   public List<String> getAllClusterNames()
/*     */   {
/* 113 */     return ImmutableList.copyOf(this.esMultiNode.getAllClusterNames());
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/provider/MultiClusterClientProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */