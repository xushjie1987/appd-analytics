/*     */ package com.appdynamics.analytics.processor.rest.availability;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.elasticsearch.ZookeeperConstants;
/*     */ import com.appdynamics.analytics.processor.event.resource.PATCH;
/*     */ import com.appdynamics.analytics.processor.zookeeper.client.ZookeeperVersionManager;
/*     */ import com.sun.jersey.api.model.AbstractResourceMethod;
/*     */ import com.sun.jersey.spi.container.ResourceMethodDispatchAdapter;
/*     */ import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
/*     */ import com.sun.jersey.spi.dispatch.RequestDispatcher;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.ws.rs.DELETE;
/*     */ import javax.ws.rs.GET;
/*     */ import javax.ws.rs.POST;
/*     */ import javax.ws.rs.PUT;
/*     */ import javax.ws.rs.Path;
/*     */ import org.apache.curator.framework.CuratorFramework;
/*     */ import org.apache.curator.framework.api.CreateBuilder;
/*     */ import org.apache.curator.framework.api.ExistsBuilder;
/*     */ import org.apache.curator.framework.api.ProtectACLCreateModePathAndBytesable;
/*     */ import org.apache.curator.framework.recipes.cache.NodeCache;
/*     */ import org.apache.curator.utils.ZKPaths;
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
/*     */ public class ResourceAvailabilityAdapter
/*     */   implements ResourceMethodDispatchAdapter
/*     */ {
/*  37 */   private static final Logger log = LoggerFactory.getLogger(ResourceAvailabilityAdapter.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final int RESOURCE_EXLCUSIONS_VERSION = 1;
/*     */   
/*     */ 
/*     */ 
/*  46 */   public static final String ANALYTICS_RESOURCE_EXCLUSIONS_ZK_PATH = ZKPaths.makePath(ZookeeperConstants.ZK_BASE_PATH, "resource.exclusions");
/*     */   final CuratorFramework zkClient;
/*     */   final NodeCache exclusionsCache;
/*     */   
/*     */   public ResourceAvailabilityAdapter(CuratorFramework zkClient)
/*     */     throws Exception
/*     */   {
/*  53 */     this.zkClient = zkClient;
/*     */     
/*     */ 
/*  56 */     if (zkClient.checkExists().forPath(ANALYTICS_RESOURCE_EXCLUSIONS_ZK_PATH) == null) {
/*  57 */       zkClient.create().creatingParentsIfNeeded().forPath(ANALYTICS_RESOURCE_EXCLUSIONS_ZK_PATH, "[]".getBytes("UTF-8"));
/*     */     }
/*     */     
/*     */ 
/*  61 */     ZookeeperVersionManager versionManager = new ZookeeperVersionManager(zkClient, ANALYTICS_RESOURCE_EXCLUSIONS_ZK_PATH);
/*     */     
/*  63 */     versionManager.checkSchemaVersion("Resource exclusions management", 1);
/*     */     
/*  65 */     this.exclusionsCache = new NodeCache(zkClient, ANALYTICS_RESOURCE_EXCLUSIONS_ZK_PATH);
/*  66 */     this.exclusionsCache.start();
/*     */   }
/*     */   
/*     */   public ResourceMethodDispatchProvider adapt(final ResourceMethodDispatchProvider provider)
/*     */   {
/*  71 */     new ResourceMethodDispatchProvider()
/*     */     {
/*     */       public RequestDispatcher create(AbstractResourceMethod method) {
/*  74 */         RequestDispatcher defaultDispatcher = provider.create(method);
/*     */         
/*  76 */         Class<?> methodClass = method.getMethod().getDeclaringClass();
/*  77 */         boolean hasMethodPathAnnotation = method.getMethod().isAnnotationPresent(Path.class);
/*  78 */         if ((hasMethodPathAnnotation) || (methodClass.isAnnotationPresent(Path.class))) {
/*  79 */           String path = ResourceAvailabilityAdapter.this.getPathForResource(methodClass, method);
/*  80 */           String httpMethod = ResourceAvailabilityAdapter.this.getHttpMethod(method.getMethod());
/*     */           
/*  82 */           ResourceAvailabilityAdapter.log.debug("Creating new ResourceAvailabilityRequestDispatcher for [{}] with path [{}] and http method [{}]", new Object[] { method, path, httpMethod });
/*     */           
/*  84 */           return new ResourceAvailabilityRequestDispatcher(defaultDispatcher, ResourceAvailabilityAdapter.this.exclusionsCache);
/*     */         }
/*  86 */         return defaultDispatcher;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   private String getPathForResource(Class<?> methodClass, AbstractResourceMethod method)
/*     */   {
/*  94 */     StringBuilder fullPath = new StringBuilder();
/*     */     
/*  96 */     Path classPathAnnotation = (Path)methodClass.getAnnotation(Path.class);
/*  97 */     if (classPathAnnotation != null) {
/*  98 */       fullPath.append(classPathAnnotation.value());
/*     */     }
/*     */     
/* 101 */     if (method.getMethod().isAnnotationPresent(Path.class)) {
/* 102 */       Path methodPathAnnotation = (Path)method.getMethod().getAnnotation(Path.class);
/* 103 */       if (methodPathAnnotation != null) {
/* 104 */         fullPath.append(classPathAnnotation != null ? "/" : "");
/* 105 */         fullPath.append(methodPathAnnotation.value());
/*     */       }
/*     */     }
/*     */     
/* 109 */     return fullPath.toString();
/*     */   }
/*     */   
/*     */   String getHttpMethod(Method method) {
/* 113 */     if (method.isAnnotationPresent(GET.class))
/* 114 */       return "GET";
/* 115 */     if (method.isAnnotationPresent(POST.class))
/* 116 */       return "POST";
/* 117 */     if (method.isAnnotationPresent(PUT.class))
/* 118 */       return "PUT";
/* 119 */     if (method.isAnnotationPresent(PATCH.class))
/* 120 */       return "PATCH";
/* 121 */     if (method.isAnnotationPresent(DELETE.class)) {
/* 122 */       return "DELETE";
/*     */     }
/* 124 */     throw new IllegalStateException("Http method annotation not present on method " + method);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/availability/ResourceAvailabilityAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */