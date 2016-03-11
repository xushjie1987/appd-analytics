/*     */ package com.appdynamics.analytics.processor.rest.availability;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.rest.RestError;
/*     */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.google.common.cache.Cache;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.sun.jersey.api.core.HttpContext;
/*     */ import com.sun.jersey.api.core.HttpRequestContext;
/*     */ import com.sun.jersey.spi.dispatch.RequestDispatcher;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.curator.framework.listen.ListenerContainer;
/*     */ import org.apache.curator.framework.recipes.cache.ChildData;
/*     */ import org.apache.curator.framework.recipes.cache.NodeCache;
/*     */ import org.apache.curator.framework.recipes.cache.NodeCacheListener;
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
/*     */ public class ResourceAvailabilityRequestDispatcher
/*     */   implements RequestDispatcher
/*     */ {
/*  36 */   private static final Logger log = LoggerFactory.getLogger(ResourceAvailabilityRequestDispatcher.class);
/*     */   
/*     */   public static final int CACHE_SIZE = 5000;
/*     */   
/*     */   public static final int CACHE_EXPIRE_MINUTES = 5;
/*     */   
/*     */   public static final String RESOURCE_KEY = "resource";
/*     */   
/*     */   public static final String HTTP_METHODS_KEY = "httpMethods";
/*     */   
/*     */   final RequestDispatcher parentDispatcher;
/*     */   
/*     */   final NodeCache nodeCache;
/*     */   final Cache<PathMethodExclusionKey, Boolean> exclusionsCache;
/*     */   
/*     */   public ResourceAvailabilityRequestDispatcher(RequestDispatcher parentDispatcher, NodeCache nodeCache)
/*     */   {
/*  53 */     this.parentDispatcher = parentDispatcher;
/*  54 */     this.nodeCache = nodeCache;
/*  55 */     this.exclusionsCache = CacheBuilder.newBuilder().maximumSize(5000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
/*     */     
/*     */ 
/*     */ 
/*  59 */     addCacheInvalidationListener();
/*     */   }
/*     */   
/*     */   void addCacheInvalidationListener() {
/*  63 */     NodeCacheListener listener = new NodeCacheListener()
/*     */     {
/*     */       public void nodeChanged() throws Exception {
/*  66 */         ResourceAvailabilityRequestDispatcher.log.debug("ZK config for exclusions has changed - invalidating the exclusions cache");
/*  67 */         ResourceAvailabilityRequestDispatcher.this.exclusionsCache.invalidateAll();
/*     */       }
/*  69 */     };
/*  70 */     this.nodeCache.getListenable().addListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean determineIfExcluded(String path, String httpMethod)
/*     */   {
/*  79 */     boolean isExcluded = false;
/*     */     try {
/*  81 */       ChildData childData = this.nodeCache.getCurrentData();
/*  82 */       if (childData == null)
/*     */       {
/*  84 */         return false;
/*     */       }
/*     */       
/*  87 */       byte[] data = childData.getData();
/*  88 */       String json = new String(data, "UTF-8");
/*  89 */       if ("[]".equals(json))
/*     */       {
/*  91 */         return false;
/*     */       }
/*     */       ArrayNode exclusions;
/*     */       ArrayNode exclusions;
/*  95 */       if (StringUtils.isNotBlank(json)) {
/*  96 */         exclusions = (ArrayNode)Reader.DEFAULT_JSON_MAPPER.readTree(json);
/*     */       } else {
/*  98 */         exclusions = Reader.DEFAULT_JSON_MAPPER.createArrayNode();
/*     */       }
/*     */       
/* 101 */       log.warn("Determining if path [{}] method [{}] is excluded [{}]. Exclusions should only ever be temporary (i.e. during a release window).", new Object[] { path, httpMethod, Reader.DEFAULT_JSON_MAPPER.writeValueAsString(exclusions) });
/*     */       
/*     */ 
/*     */ 
/* 105 */       for (JsonNode exclusion : exclusions) {
/* 106 */         String resourceRegex = exclusion.get("resource").asText();
/* 107 */         ArrayNode httpMethodsNode = (ArrayNode)exclusion.get("httpMethods");
/* 108 */         List<String> httpMethods = new ArrayList();
/* 109 */         for (JsonNode httpMethodNode : httpMethodsNode) {
/* 110 */           httpMethods.add(httpMethodNode.asText());
/*     */         }
/*     */         
/* 113 */         if (path.matches(resourceRegex)) {
/* 114 */           for (String httpMethodRegex : httpMethods) {
/* 115 */             if (httpMethod.matches(httpMethodRegex)) {
/* 116 */               isExcluded = true;
/* 117 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 122 */         if (isExcluded) {
/* 123 */           log.warn("Path [{}] method [{}] is excluded.", path, httpMethod);
/* 124 */           break;
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 128 */       log.error("Could not determine resource exclusions.", e);
/*     */     }
/* 130 */     return isExcluded;
/*     */   }
/*     */   
/*     */   public void dispatch(Object resource, HttpContext context)
/*     */   {
/* 135 */     String path = context.getRequest().getPath();
/* 136 */     String httpMethod = context.getRequest().getMethod().toUpperCase();
/* 137 */     log.debug("Encountered request to path [{}] with http method [{}]", path, httpMethod);
/*     */     
/* 139 */     PathMethodExclusionKey key = new PathMethodExclusionKey(path, httpMethod);
/* 140 */     Boolean isExcluded = (Boolean)this.exclusionsCache.getIfPresent(key);
/* 141 */     if (isExcluded == null) {
/* 142 */       isExcluded = Boolean.valueOf(determineIfExcluded(path, httpMethod));
/* 143 */       this.exclusionsCache.put(key, isExcluded);
/*     */     }
/*     */     
/* 146 */     if (isExcluded.booleanValue()) {
/* 147 */       log.warn("Excluding request to path [{}] method [{}]", path, httpMethod);
/* 148 */       throw RestError.create(StandardErrorCode.CODE_RESOURCE_EXCLUDED, "This resource is temporarily unavailable. Try again later.");
/*     */     }
/*     */     
/* 151 */     this.parentDispatcher.dispatch(resource, context);
/*     */   }
/*     */   
/*     */   static class PathMethodExclusionKey { @ConstructorProperties({"path", "method"})
/* 155 */     public PathMethodExclusionKey(String path, String method) { this.path = path;this.method = method; }
/* 156 */     public String toString() { return "ResourceAvailabilityRequestDispatcher.PathMethodExclusionKey(path=" + getPath() + ", method=" + getMethod() + ")"; }
/* 157 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof PathMethodExclusionKey)) return false; PathMethodExclusionKey other = (PathMethodExclusionKey)o; if (!other.canEqual(this)) return false; Object this$path = getPath();Object other$path = other.getPath(); if (this$path == null ? other$path != null : !this$path.equals(other$path)) return false; Object this$method = getMethod();Object other$method = other.getMethod();return this$method == null ? other$method == null : this$method.equals(other$method); } public boolean canEqual(Object other) { return other instanceof PathMethodExclusionKey; } public int hashCode() { int PRIME = 31;int result = 1;Object $path = getPath();result = result * 31 + ($path == null ? 0 : $path.hashCode());Object $method = getMethod();result = result * 31 + ($method == null ? 0 : $method.hashCode());return result; }
/*     */     
/* 159 */     public String getPath() { return this.path; } public void setPath(String path) { this.path = path; }
/* 160 */     public String getMethod() { return this.method; } public void setMethod(String method) { this.method = method; }
/*     */     
/*     */     String path;
/*     */     String method;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/availability/ResourceAvailabilityRequestDispatcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */