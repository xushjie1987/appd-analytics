/*     */ package com.appdynamics.analytics.processor.zookeeper.server;
/*     */ 
/*     */ import com.appdynamics.common.util.health.SimpleHealthCheck;
/*     */ import com.codahale.metrics.health.HealthCheck.Result;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.net.HostAndPort;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZooKeeperHealthCheck
/*     */   extends SimpleHealthCheck
/*     */ {
/*  40 */   private static final Logger log = LoggerFactory.getLogger(ZooKeeperHealthCheck.class);
/*     */   public static final String QUERY = "mntr";
/*     */   public static final String RESPONSE_KEY = "zk_server_state";
/*     */   public static final String UNREACHABLE = "unreachable";
/*     */   static final int TIMEOUT_MILLIS = 5000;
/*     */   final Iterator<HostAndPort> hostAndPorts;
/*     */   final int numHostsAndPorts;
/*     */   
/*     */   public ZooKeeperHealthCheck(String name, Collection<HostAndPort> hostAndPorts)
/*     */   {
/*  50 */     super(name);
/*  51 */     Preconditions.checkArgument(hostAndPorts.size() > 0, "Hosts and ports cannot be empty");
/*  52 */     this.hostAndPorts = Iterators.cycle(hostAndPorts);
/*  53 */     this.numHostsAndPorts = hostAndPorts.size();
/*     */   }
/*     */   
/*     */   public HealthCheck.Result check()
/*     */   {
/*  58 */     LinkedList<String> failedTotally = null;
/*     */     
/*     */ 
/*  61 */     for (int i = 0; i < this.numHostsAndPorts; i++) {
/*  62 */       HostAndPort hostAndPort = null;
/*  63 */       synchronized (this.hostAndPorts) {
/*  64 */         hostAndPort = (HostAndPort)this.hostAndPorts.next();
/*     */       }
/*     */       try
/*     */       {
/*  68 */         return HealthCheck.Result.healthy("Status of [" + hostAndPort.toString() + "]" + " is [" + getHealth(hostAndPort) + "]");
/*     */       }
/*     */       catch (Exception e) {
/*  71 */         log.warn("Health check to server [" + hostAndPort + "] failed", e);
/*     */         
/*  73 */         if (failedTotally == null) {
/*  74 */           failedTotally = new LinkedList();
/*     */         }
/*  76 */         failedTotally.add(hostAndPort.toString() + ": " + e.getMessage());
/*     */       }
/*     */     }
/*  79 */     return HealthCheck.Result.unhealthy("Health checks to all servers failed: " + failedTotally);
/*     */   }
/*     */   
/*     */   public String getEnsembleStatus()
/*     */   {
/*  84 */     for (int i = 0; i < this.numHostsAndPorts; i++) {
/*  85 */       HostAndPort hostAndPort = null;
/*  86 */       synchronized (this.hostAndPorts) {
/*  87 */         hostAndPort = (HostAndPort)this.hostAndPorts.next();
/*     */       }
/*     */       try
/*     */       {
/*  91 */         return getHealth(hostAndPort);
/*     */       } catch (Exception e) {
/*  93 */         log.warn("Health check to server [" + hostAndPort + "] failed", e);
/*     */       }
/*     */     }
/*  96 */     return "unreachable";
/*     */   }
/*     */   
/*     */   public String getHealth(HostAndPort hostAndPort) throws Exception {
/* 100 */     Socket clientSocket = new Socket(hostAndPort.getHostText(), hostAndPort.getPort());Throwable localThrowable4 = null;
/* 101 */     try { clientSocket.setTcpNoDelay(true);
/* 102 */       clientSocket.setSoTimeout(5000);
/*     */       
/* 104 */       String unhealthyMsg = "";
/* 105 */       OutputStream os = clientSocket.getOutputStream();Throwable localThrowable5 = null;
/* 106 */       try { os.write("mntr".getBytes(Charsets.UTF_8));
/* 107 */         os.flush();
/*     */         
/*     */ 
/*     */ 
/* 111 */         InputStream is = clientSocket.getInputStream();Throwable localThrowable6 = null;
/* 112 */         try { List<String> lines = IOUtils.readLines(is);
/*     */           
/*     */ 
/*     */ 
/* 116 */           for (String line : lines) {
/* 117 */             if (line.contains("zk_server_state")) {
/* 118 */               String[] keyValue = line.split("\t");
/* 119 */               return keyValue[1];
/*     */             }
/*     */           }
/* 122 */           unhealthyMsg = lines.size() == 1 ? (String)lines.get(0) : lines.toString();
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 111 */           localThrowable6 = localThrowable1;throw localThrowable1;
/*     */         }
/*     */         finally {}
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 105 */         localThrowable5 = localThrowable2;throw localThrowable2;
/*     */       }
/*     */       finally {}
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
/* 125 */       throw new RuntimeException(unhealthyMsg);
/*     */     }
/*     */     catch (Throwable localThrowable3)
/*     */     {
/* 100 */       localThrowable4 = localThrowable3;throw localThrowable3;
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
/*     */     }
/*     */     finally
/*     */     {
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
/* 126 */       if (clientSocket != null) if (localThrowable4 != null) try { clientSocket.close(); } catch (Throwable x2) { localThrowable4.addSuppressed(x2); } else clientSocket.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/server/ZooKeeperHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */