/*     */ package com.appdynamics.analytics.processor.elasticsearch.util;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ESUtils;
/*     */ import com.appdynamics.analytics.processor.elasticsearch.index.creation.IndexCreationManager;
/*     */ import com.appdynamics.analytics.processor.util.VersionManager;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.elasticsearch.action.ListenableActionFuture;
/*     */ import org.elasticsearch.action.index.IndexRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchRequestBuilder;
/*     */ import org.elasticsearch.action.search.SearchResponse;
/*     */ import org.elasticsearch.action.search.SearchType;
/*     */ import org.elasticsearch.client.Client;
/*     */ import org.elasticsearch.index.query.QueryBuilder;
/*     */ import org.elasticsearch.index.query.QueryBuilders;
/*     */ import org.elasticsearch.indices.IndexCreationException;
/*     */ import org.elasticsearch.indices.IndexMissingException;
/*     */ import org.elasticsearch.search.SearchHit;
/*     */ import org.elasticsearch.search.SearchHits;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElasticSearchVersionManager
/*     */   extends VersionManager
/*     */ {
/*  31 */   private static final Logger log = LoggerFactory.getLogger(ElasticSearchVersionManager.class);
/*     */   
/*     */   static final String SCHEMA_VERSION_DOC_TYPE = "schema_version";
/*     */   static final String SCHEMA_NAME_PROPERTY = "name";
/*     */   static final String SCHEMA_VERSION_PROPERTY = "version";
/*     */   private final Client esClient;
/*     */   private final IndexCreationManager indexCreationManager;
/*     */   private final String schemaName;
/*     */   
/*     */   public ElasticSearchVersionManager(Client esClient, IndexCreationManager indexCreationManager, String schemaName)
/*     */   {
/*  42 */     this.esClient = esClient;
/*  43 */     this.indexCreationManager = indexCreationManager;
/*  44 */     this.schemaName = schemaName;
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/*  49 */     int version = 1;
/*     */     try {
/*  51 */       SearchResponse response = getSchemaVersionSearchResponse();
/*     */       
/*  53 */       if ((response.getHits() != null) && (response.getHits().getTotalHits() != 0L)) {
/*  54 */         Map<String, Object> document = response.getHits().getAt(0).sourceAsMap();
/*  55 */         version = ((Integer)document.get("version")).intValue();
/*     */       } else {
/*  57 */         updateVersion(version);
/*     */       }
/*     */     }
/*     */     catch (IndexMissingException e) {
/*  61 */       updateVersion(version);
/*     */     }
/*     */     
/*  64 */     return version;
/*     */   }
/*     */   
/*     */   public void updateVersion(int newVersion)
/*     */   {
/*  69 */     SearchResponse searchResponse = null;
/*     */     try
/*     */     {
/*  72 */       searchResponse = getSchemaVersionSearchResponse();
/*     */     } catch (IndexMissingException e) {
/*  74 */       ensureSchemaVersionIndexExists();
/*     */     }
/*     */     
/*  77 */     Map<String, Object> versionDocument = new HashMap();
/*  78 */     versionDocument.put("name", this.schemaName);
/*  79 */     versionDocument.put("version", Integer.valueOf(newVersion));
/*     */     
/*  81 */     IndexRequestBuilder builder = this.esClient.prepareIndex("appdynamics_version", "schema_version").setSource(versionDocument);
/*     */     
/*     */ 
/*     */ 
/*  85 */     if ((searchResponse != null) && (searchResponse.getHits() != null) && (searchResponse.getHits().getTotalHits() != 0L))
/*     */     {
/*  87 */       SearchHit hit = searchResponse.getHits().getAt(0);
/*  88 */       builder.setId(hit.getId());
/*     */     }
/*     */     
/*  91 */     builder.execute().actionGet();
/*     */   }
/*     */   
/*     */   private void ensureSchemaVersionIndexExists() {
/*     */     try {
/*  96 */       if (ESUtils.indexExists(this.esClient, "appdynamics_version")) {
/*  97 */         return;
/*     */       }
/*     */       
/* 100 */       boolean success = this.indexCreationManager.createIndexLocking(this.esClient, "appdynamics_version", new HashMap());
/*     */       
/*     */ 
/* 103 */       if (!success) {
/* 104 */         throw new IllegalStateException(String.format("Failed to create version index [%s]", new Object[] { "appdynamics_version" }));
/*     */       }
/*     */     }
/*     */     catch (IndexCreationException e) {
/* 108 */       throw new IllegalStateException(String.format("Failed trying to create version index [%s]", new Object[] { "appdynamics_version" }), e);
/*     */     }
/*     */   }
/*     */   
/*     */   private SearchResponse getSchemaVersionSearchResponse()
/*     */   {
/* 114 */     ESUtils.refreshIndices(this.esClient, new String[0]);
/* 115 */     ESUtils.waitForClusterHealthiness(this.esClient, new String[0]);
/*     */     
/* 117 */     QueryBuilder qb = QueryBuilders.termQuery("name", this.schemaName);
/*     */     
/* 119 */     return (SearchResponse)this.esClient.prepareSearch(new String[] { "appdynamics_version" }).setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(qb).execute().actionGet();
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/util/ElasticSearchVersionManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */