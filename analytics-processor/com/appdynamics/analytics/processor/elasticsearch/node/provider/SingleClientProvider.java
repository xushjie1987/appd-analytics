/*    */ package com.appdynamics.analytics.processor.elasticsearch.node.provider;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.single.ElasticSearchSingleNode;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Singleton;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.elasticsearch.client.Client;
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
/*    */ @Singleton
/*    */ public class SingleClientProvider
/*    */   implements ClientProvider
/*    */ {
/*    */   @Inject
/*    */   volatile ElasticSearchSingleNode esSingleNode;
/*    */   
/*    */   public Client getAdminClient()
/*    */   {
/* 29 */     return this.esSingleNode.getClient();
/*    */   }
/*    */   
/*    */   public Client getClusterClient(String clusterName)
/*    */   {
/* 34 */     return getAdminClient();
/*    */   }
/*    */   
/*    */   public Client getInsertClient(String account)
/*    */   {
/* 39 */     return getAdminClient();
/*    */   }
/*    */   
/*    */   public List<Client> getAllInsertClients(String account)
/*    */   {
/* 44 */     return Arrays.asList(new Client[] { getAdminClient() });
/*    */   }
/*    */   
/*    */   public Client getSearchClient(String account)
/*    */   {
/* 49 */     return getAdminClient();
/*    */   }
/*    */   
/*    */   public List<Client> getAllInsertClients()
/*    */   {
/* 54 */     return Arrays.asList(new Client[] { getAdminClient() });
/*    */   }
/*    */   
/*    */   public List<Client> getAllClients()
/*    */   {
/* 59 */     return Arrays.asList(new Client[] { getAdminClient() });
/*    */   }
/*    */   
/*    */   public List<String> getAllClusterNames()
/*    */   {
/* 64 */     return Arrays.asList(new String[] { "Single" });
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/provider/SingleClientProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */