/*     */ package com.appdynamics.common.util.var;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
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
/*     */ public enum SystemVariableResolver
/*     */   implements VariableResolver
/*     */ {
/*  21 */   INSTANCE;
/*     */   
/*     */   private static final Logger log;
/*     */   public static final String VAR_SYSTEM_PROCESS_ID = "system.process.id";
/*     */   public static final String VAR_SYSTEM_HOST_NAME = "system.host.name";
/*     */   public static final long HOST_NAME_CACHE_TIME_NANOS = 5000000000L;
/*     */   static final Long PROCESS_ID;
/*     */   static volatile String hostName;
/*     */   static volatile long hostNameResolvedAtNanos;
/*     */   
/*     */   static
/*     */   {
/*  19 */     log = LoggerFactory.getLogger(SystemVariableResolver.class);
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
/*  42 */     String pidStr = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
/*  43 */     long pid = 0L;
/*     */     try {
/*  45 */       pid = Long.parseLong(pidStr);
/*     */     } catch (NumberFormatException e) {
/*  47 */       log.warn("Process id could not be extracted", e);
/*     */     }
/*  49 */     PROCESS_ID = Long.valueOf(pid);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object resolve(String variable)
/*     */   {
/*  58 */     switch (variable) {
/*     */     case "system.process.id": 
/*  60 */       return PROCESS_ID;
/*     */     
/*     */     case "system.host.name": 
/*  63 */       return getHostName();
/*     */     }
/*     */     
/*     */     
/*     */ 
/*  68 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object resolve(String variable, String variableExtension)
/*     */   {
/*  79 */     return null;
/*     */   }
/*     */   
/*     */   public static long getProcessId() {
/*  83 */     return PROCESS_ID.longValue();
/*     */   }
/*     */   
/*     */   public static String getHostName() {
/*  87 */     long timeNanos = System.nanoTime();
/*     */     
/*  89 */     if ((hostName == null) || (timeNanos - hostNameResolvedAtNanos > 5000000000L)) {
/*     */       try {
/*  91 */         InetAddress localHost = InetAddress.getLocalHost();
/*  92 */         String s = localHost.getCanonicalHostName();
/*     */         
/*  94 */         s = s.equals(localHost.getHostAddress()) ? localHost.getHostName() : s;
/*  95 */         hostName = s;
/*     */       } catch (UnknownHostException e) {
/*  97 */         hostName = "localhost";
/*     */       }
/*  99 */       hostNameResolvedAtNanos = timeNanos;
/*     */     }
/*     */     
/* 102 */     return hostName;
/*     */   }
/*     */   
/*     */   private SystemVariableResolver() {}
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/SystemVariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */