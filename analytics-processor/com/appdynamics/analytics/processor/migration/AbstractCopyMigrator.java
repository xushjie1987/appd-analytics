/*    */ package com.appdynamics.analytics.processor.migration;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.account.AccountManager;
/*    */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*    */ import com.appdynamics.analytics.processor.account.configuration.AccountLicensingConfiguration;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*    */ import com.google.common.base.Optional;
/*    */ import com.google.common.base.Strings;
/*    */ import com.google.common.collect.ArrayListMultimap;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.elasticsearch.action.ListenableActionFuture;
/*    */ import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
/*    */ import org.elasticsearch.client.AdminClient;
/*    */ import org.elasticsearch.client.Client;
/*    */ import org.elasticsearch.client.IndicesAdminClient;
/*    */ import org.kohsuke.args4j.Option;
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
/*    */ public abstract class AbstractCopyMigrator
/*    */   extends AbstractElasticSearchMigrationUtility
/*    */ {
/*    */   @Option(name="-x", aliases={"--suffix"}, usage="The index suffix added when index was restored", metaVar="SUFFIX", required=false)
/*    */   protected String suffix;
/*    */   
/*    */   protected void afterSuccessfulRun(Client client)
/*    */   {
/* 39 */     if (!Strings.isNullOrEmpty(this.suffix)) {
/* 40 */       client.admin().indices().prepareDelete(new String[] { getBaseIndexName() + this.suffix }).execute().actionGet();
/*    */     }
/*    */   }
/*    */   
/*    */   protected String getTargetIndex()
/*    */   {
/* 46 */     return getBaseIndexName() + (Strings.isNullOrEmpty(this.suffix) ? "" : this.suffix);
/*    */   }
/*    */   
/*    */   abstract String getBaseIndexName();
/*    */   
/*    */   protected AccountManager noOpAccountManager() {
/* 52 */     new AccountManager()
/*    */     {
/*    */       public void upsertAccountConfigurations(List<AccountConfiguration> accountConfig) {
/* 55 */         throw new UnsupportedOperationException();
/*    */       }
/*    */       
/*    */       public List<AccountConfiguration> findAccountConfigurations()
/*    */       {
/* 60 */         return Collections.emptyList();
/*    */       }
/*    */       
/*    */       public List<AccountConfiguration> findAccountConfigurations(List<String> accountNames)
/*    */       {
/* 65 */         return Collections.emptyList();
/*    */       }
/*    */       
/*    */       public Optional<AccountConfiguration> findAccountConfiguration(String accountName)
/*    */       {
/* 70 */         return Optional.absent();
/*    */       }
/*    */       
/*    */       public Multimap<String, AccountConfiguration> findSystemAccountConfigurationsAsMap()
/*    */       {
/* 75 */         return ArrayListMultimap.create();
/*    */       }
/*    */       
/*    */ 
/*    */       public Optional<AccountLicensingConfiguration> findAccountLicensingConfiguration(String accountName, String eventType)
/*    */       {
/* 81 */         return Optional.absent();
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */   protected ClientProvider clientProviderWrapper(final Client client) {
/* 87 */     new ClientProvider()
/*    */     {
/*    */       public Client getAdminClient() {
/* 90 */         return client;
/*    */       }
/*    */       
/*    */       public Client getClusterClient(String clusterName)
/*    */       {
/* 95 */         return client;
/*    */       }
/*    */       
/*    */       public Client getInsertClient(String account)
/*    */       {
/* :0 */         return client;
/*    */       }
/*    */       
/*    */       public List<Client> getAllInsertClients(String account)
/*    */       {
/* :5 */         return ImmutableList.of(client);
/*    */       }
/*    */       
/*    */       public Client getSearchClient(String account)
/*    */       {
/* ;0 */         return client;
/*    */       }
/*    */       
/*    */       public List<Client> getAllInsertClients()
/*    */       {
/* ;5 */         return ImmutableList.of(client);
/*    */       }
/*    */       
/*    */       public List<Client> getAllClients()
/*    */       {
/* <0 */         return ImmutableList.of(client);
/*    */       }
/*    */       
/*    */       public List<String> getAllClusterNames()
/*    */       {
/* <5 */         return ImmutableList.of("");
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/migration/AbstractCopyMigrator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */