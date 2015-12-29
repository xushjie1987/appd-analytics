/*     */ package com.appdynamics.analytics.processor.tool.healthcheck;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.tool.common.ToolHelper;
/*     */ import com.appdynamics.common.io.IoHelper;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.io.Closeables;
/*     */ import com.google.common.net.HostAndPort;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.config.RequestConfig.Builder;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.config.SocketConfig;
/*     */ import org.apache.http.config.SocketConfig.Builder;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClientBuilder;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ import org.kohsuke.args4j.Option;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Main
/*     */ {
/*  40 */   private static final Logger log = LoggerFactory.getLogger(Main.class);
/*     */   static final String STATUS_NOT_AVAILABLE = "Not available";
/*     */   static final String STATUS_NOT_HEALTHY = "Not healthy";
/*     */   static final String STATUS_HEALTHY = "Healthy";
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*  47 */     ToolHelper.setRootLogLevel();
/*  48 */     Input input = new Input();
/*  49 */     ToolHelper.readArgumentsOrExit(args, input);
/*  50 */     boolean allOk = true;
/*  51 */     List<HostAndPort> hostAndPorts = IoHelper.parseHostAndPortCsv(input.getCsvHostsAndPorts());
/*  52 */     List<String> statuses = new ArrayList(hostAndPorts.size());
/*  53 */     List<String> messages = new ArrayList(hostAndPorts.size());
/*     */     try
/*     */     {
/*  56 */       RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  61 */       CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultSocketConfig(SocketConfig.custom().setSoReuseAddress(true).build()).setDefaultRequestConfig(requestConfig).disableAutomaticRetries().build();
/*     */       
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/*  67 */         for (HostAndPort hostAndPort : hostAndPorts) {
/*  68 */           String url = makeHttpUrl(hostAndPort);
/*     */           
/*  70 */           HttpGet httpGet = new HttpGet(url);
/*     */           try {
/*  72 */             CloseableHttpResponse response = httpClient.execute(httpGet);Throwable localThrowable2 = null;
/*  73 */             try { HttpEntity entity = response.getEntity();
/*  74 */               String s = EntityUtils.toString(entity, Charsets.UTF_8);
/*  75 */               StatusLine statusLine = response.getStatusLine();
/*  76 */               statuses.add("HTTP " + statusLine.getStatusCode());
/*  77 */               messages.add(statusLine.getReasonPhrase() + "\n" + s);
/*     */               
/*  79 */               switch (statusLine.getStatusCode()) {
/*     */               case 200: 
/*     */               case 202: 
/*     */                 break;
/*     */               default: 
/*  84 */                 allOk = false;
/*     */               }
/*     */             }
/*     */             catch (Throwable localThrowable1)
/*     */             {
/*  72 */               localThrowable2 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/*     */             finally
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */               if (response != null) if (localThrowable2 != null) try { response.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else response.close();
/*     */             }
/*  88 */           } catch (Throwable t) { allOk = false;
/*  89 */             statuses.add("Not available");
/*  90 */             messages.add(Throwables.getStackTraceAsString(t));
/*     */           }
/*     */         }
/*     */       } finally {
/*  94 */         Closeables.close(httpClient, true);
/*     */       }
/*     */     } catch (Throwable t) {
/*  97 */       allOk = false;
/*  98 */       log.error("Error occurred while checking health", t);
/*     */     }
/*     */     
/* 101 */     reportAndEnd(input, allOk, hostAndPorts, statuses, messages);
/*     */   }
/*     */   
/*     */   static String makeHttpUrl(HostAndPort hostAndPort) {
/* 105 */     return "http://" + hostAndPort.getHostText() + ":" + hostAndPort.getPort() + "/healthcheck?pretty=true";
/*     */   }
/*     */   
/*     */   static void reportAndEnd(Input input, boolean allOk, List<HostAndPort> hostAndPorts, List<String> statuses, List<String> messages)
/*     */   {
/* 110 */     if (!allOk) {
/* 111 */       log.warn("Not healthy");
/*     */     } else {
/* 113 */       log.info("Healthy");
/*     */     }
/*     */     
/* 116 */     log.info("Individual statuses below:");
/* 117 */     int i = 0;
/* 118 */     for (HostAndPort hostAndPort : hostAndPorts) {
/* 119 */       String status = (String)statuses.get(i++);
/* 120 */       log.info("[{}] status is [{}]", hostAndPort, status);
/*     */     }
/*     */     
/* 123 */     if (input.isVerbose()) {
/* 124 */       log.info("Detailed messages below:");
/* 125 */       i = 0;
/* 126 */       for (HostAndPort hostAndPort : hostAndPorts) {
/* 127 */         String message = (String)messages.get(i++);
/* 128 */         log.info("[{}] sent\n[{}]", hostAndPort, message);
/*     */       }
/*     */     }
/*     */     
/* 132 */     if (!allOk)
/* 133 */       System.exit(-1);
/*     */   }
/*     */   
/*     */   public static class Input {
/* 137 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Input)) return false; Input other = (Input)o; if (!other.canEqual(this)) return false; Object this$csvHostsAndPorts = getCsvHostsAndPorts();Object other$csvHostsAndPorts = other.getCsvHostsAndPorts(); if (this$csvHostsAndPorts == null ? other$csvHostsAndPorts != null : !this$csvHostsAndPorts.equals(other$csvHostsAndPorts)) return false; return isVerbose() == other.isVerbose(); } public boolean canEqual(Object other) { return other instanceof Input; } public int hashCode() { int PRIME = 31;int result = 1;Object $csvHostsAndPorts = getCsvHostsAndPorts();result = result * 31 + ($csvHostsAndPorts == null ? 0 : $csvHostsAndPorts.hashCode());result = result * 31 + (isVerbose() ? 1231 : 1237);return result; }
/* 138 */     public String toString() { return "Main.Input(csvHostsAndPorts=" + getCsvHostsAndPorts() + ", verbose=" + isVerbose() + ")"; }
/*     */     
/*     */     @Option(name="-hp", aliases={"--http-hosts-ports"}, usage="Comma separated list of HTTP 'host:dw-admin-port'", required=true)
/*     */     @NotNull
/*     */     String csvHostsAndPorts;
/*     */     
/* 144 */     public String getCsvHostsAndPorts() { return this.csvHostsAndPorts; } public void setCsvHostsAndPorts(String csvHostsAndPorts) { this.csvHostsAndPorts = csvHostsAndPorts; }
/*     */     
/*     */ 
/* 147 */     public boolean isVerbose() { return this.verbose; } public void setVerbose(boolean verbose) { this.verbose = verbose; }
/*     */     
/*     */     @Option(name="-v", aliases={"--verbose"}, usage="More detailed messages", required=false)
/*     */     boolean verbose;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/healthcheck/Main.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */