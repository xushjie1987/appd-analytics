/*    */ package com.appdynamics.analytics.processor.auth;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.account.AccountManager;
/*    */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*    */ import com.google.common.cache.Cache;
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ import com.sun.jersey.api.model.AbstractResourceMethod;
/*    */ import com.sun.jersey.spi.container.ResourceMethodDispatchAdapter;
/*    */ import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
/*    */ import com.sun.jersey.spi.dispatch.RequestDispatcher;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ public class SecureDispatchAdapter
/*    */   implements ResourceMethodDispatchAdapter
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(SecureDispatchAdapter.class);
/*    */   
/*    */ 
/*    */   public static final String ACCOUNT_NAME_HTTP_CONTEXT_KEY = "AD-Account-Name";
/*    */   
/*    */ 
/*    */   public static final int CACHE_SIZE = 4000;
/*    */   
/*    */ 
/*    */   public static final int CACHE_EXPIRE_MINUTES = 5;
/*    */   
/*    */ 
/*    */   final AccountManager accountManager;
/*    */   
/*    */ 
/*    */   Cache<String, AccountConfiguration> authorized;
/*    */   
/*    */ 
/*    */ 
/*    */   public SecureDispatchAdapter(AccountManager accountManager)
/*    */   {
/* 47 */     this.accountManager = accountManager;
/* 48 */     this.authorized = CacheBuilder.newBuilder().maximumSize(4000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ResourceMethodDispatchProvider adapt(final ResourceMethodDispatchProvider provider)
/*    */   {
/* 56 */     new ResourceMethodDispatchProvider()
/*    */     {
/*    */       public RequestDispatcher create(AbstractResourceMethod method) {
/* 59 */         SecureDispatchAdapter.log.debug("Creating new dispatcher for [{}]", method);
/*    */         
/* 61 */         RequestDispatcher defaultDispatcher = provider.create(method);
/* 62 */         if ((method.getMethod().isAnnotationPresent(SecureResource.class)) || (method.getMethod().getDeclaringClass().isAnnotationPresent(SecureResource.class)))
/*    */         {
/* 64 */           return new SecureResourceRequestDispatcher(defaultDispatcher, method, SecureDispatchAdapter.this.accountManager, SecureDispatchAdapter.this.authorized);
/*    */         }
/* 66 */         return defaultDispatcher;
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/auth/SecureDispatchAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */