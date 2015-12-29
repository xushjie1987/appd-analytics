/*    */ package com.appdynamics.analytics.processor.elasticsearch.index;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.IndexManagementModuleConfiguration;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.configuration.MetaDataIndexConfiguration;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import org.elasticsearch.client.Client;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class MetaDataIndexCreator
/*    */ {
/* 24 */   private static final Logger log = LoggerFactory.getLogger(MetaDataIndexCreator.class);
/*    */   
/*    */ 
/*    */ 
/*    */   void createMetaDataIndicesOnStartup(IndexManagementModuleConfiguration storeConfig, ClientProvider clientProvider, IndexCreationManager indexCreationManager)
/*    */   {
/* 30 */     Client client = clientProvider.getAdminClient();
/* 31 */     if (storeConfig != null) {
/* 32 */       Map<String, MetaDataIndexConfiguration> indexConfigs = storeConfig.getIndexConfigurations();
/* 33 */       for (Map.Entry<String, MetaDataIndexConfiguration> indexConfig : indexConfigs.entrySet()) {
/* 34 */         indexCreationManager.createIndexLocking(client, (String)indexConfig.getKey(), (MetaDataIndexConfiguration)indexConfig.getValue());
/*    */       }
/*    */       
/*    */ 
/* 38 */       Set<String> indicesSet = indexConfigs.keySet();
/* 39 */       if (indexConfigs.keySet().size() != 0) {
/* 40 */         String[] indices = new String[indicesSet.size()];
/* 41 */         indices = (String[])indexConfigs.keySet().toArray(indices);
/* 42 */         ESUtils.waitForClusterHealthiness(client, indices);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/index/MetaDataIndexCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */