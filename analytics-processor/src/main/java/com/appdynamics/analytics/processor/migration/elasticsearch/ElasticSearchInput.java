/*    */ package com.appdynamics.analytics.processor.migration.elasticsearch;
/*    */ 
/*    */ import com.appdynamics.common.util.var.SystemVariableResolver;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import org.elasticsearch.client.transport.TransportClient;
/*    */ import org.elasticsearch.common.settings.ImmutableSettings;
/*    */ import org.elasticsearch.common.settings.ImmutableSettings.Builder;
/*    */ import org.elasticsearch.common.settings.Settings;
/*    */ import org.elasticsearch.common.transport.InetSocketTransportAddress;
/*    */ import org.kohsuke.args4j.Option;
/*    */ 
/*    */ 
/*    */ public class ElasticSearchInput
/*    */ {
/*    */   @Option(name="-esh", aliases={"--es-host"}, usage="The elasticsearch host", metaVar="ES-HOST", required=true)
/*    */   @NotNull
/*    */   String elasticSearchHost;
/*    */   @Option(name="-esp", aliases={"--es-port"}, usage="The elasticsearch port", metaVar="ES-PORT", required=true)
/*    */   @Min(1024L)
/*    */   int elasticSearchPort;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 25 */     if (o == this) return true; if (!(o instanceof ElasticSearchInput)) return false; ElasticSearchInput other = (ElasticSearchInput)o; if (!other.canEqual(this)) return false; Object this$elasticSearchHost = getElasticSearchHost();Object other$elasticSearchHost = other.getElasticSearchHost(); if (this$elasticSearchHost == null ? other$elasticSearchHost != null : !this$elasticSearchHost.equals(other$elasticSearchHost)) return false; if (getElasticSearchPort() != other.getElasticSearchPort()) return false; Object this$elasticSearchCluster = getElasticSearchCluster();Object other$elasticSearchCluster = other.getElasticSearchCluster();return this$elasticSearchCluster == null ? other$elasticSearchCluster == null : this$elasticSearchCluster.equals(other$elasticSearchCluster); } public boolean canEqual(Object other) { return other instanceof ElasticSearchInput; } public int hashCode() { int PRIME = 31;int result = 1;Object $elasticSearchHost = getElasticSearchHost();result = result * 31 + ($elasticSearchHost == null ? 0 : $elasticSearchHost.hashCode());result = result * 31 + getElasticSearchPort();Object $elasticSearchCluster = getElasticSearchCluster();result = result * 31 + ($elasticSearchCluster == null ? 0 : $elasticSearchCluster.hashCode());return result; }
/* 26 */   public String toString() { return "ElasticSearchInput(elasticSearchHost=" + getElasticSearchHost() + ", elasticSearchPort=" + getElasticSearchPort() + ", elasticSearchCluster=" + getElasticSearchCluster() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 31 */   public String getElasticSearchHost() { return this.elasticSearchHost; } public void setElasticSearchHost(String elasticSearchHost) { this.elasticSearchHost = elasticSearchHost; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 36 */   public int getElasticSearchPort() { return this.elasticSearchPort; } public void setElasticSearchPort(int elasticSearchPort) { this.elasticSearchPort = elasticSearchPort; } @Option(name="-esc", aliases={"--es-cluster-name"}, usage="The elasticsearch cluster name", metaVar="ES-CLUSTER-NAME", required=false)
/*    */   @NotNull
/* 38 */   String elasticSearchCluster = "appdynamics-analytics-cluster";
/*    */   
/*    */ 
/* 41 */   public String getElasticSearchCluster() { return this.elasticSearchCluster; } public void setElasticSearchCluster(String elasticSearchCluster) { this.elasticSearchCluster = elasticSearchCluster; }
/*    */   
/*    */   public TransportClient newTransportClient() {
/* 44 */     Settings settings = ImmutableSettings.builder().put("cluster.name", this.elasticSearchCluster).put("client.transport.sniff", true).put("node.name", "migration-task-" + SystemVariableResolver.getProcessId()).build();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 50 */     TransportClient client = new TransportClient(settings, false);
/* 51 */     client.addTransportAddress(new InetSocketTransportAddress(this.elasticSearchHost, this.elasticSearchPort));
/* 52 */     return client;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/migration/elasticsearch/ElasticSearchInput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */