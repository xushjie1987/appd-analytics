/*     */ package com.appdynamics.analytics.processor.auth;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.account.AccountManager;
/*     */ import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
/*     */ import com.appdynamics.analytics.processor.account.exception.IllegalAccountNameException;
/*     */ import com.appdynamics.analytics.processor.rest.BadRequestErrorCode;
/*     */ import com.appdynamics.analytics.processor.rest.RestError;
/*     */ import com.appdynamics.analytics.processor.rest.StandardErrorCode;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.cache.Cache;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.io.BaseEncoding;
/*     */ import com.sun.jersey.api.core.HttpContext;
/*     */ import com.sun.jersey.api.core.HttpRequestContext;
/*     */ import com.sun.jersey.api.model.AbstractResourceMethod;
/*     */ import com.sun.jersey.spi.dispatch.RequestDispatcher;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.ws.rs.WebApplicationException;
/*     */ import org.joda.time.DateTime;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SecureResourceRequestDispatcher
/*     */   implements RequestDispatcher
/*     */ {
/*     */   @ConstructorProperties({"parentDispatcher", "method", "accountManager", "authorized"})
/*     */   public SecureResourceRequestDispatcher(RequestDispatcher parentDispatcher, AbstractResourceMethod method, AccountManager accountManager, Cache<String, AccountConfiguration> authorized)
/*     */   {
/*  39 */     this.parentDispatcher = parentDispatcher;this.method = method;this.accountManager = accountManager;this.authorized = authorized; }
/*  40 */   private static final Logger log = LoggerFactory.getLogger(SecureResourceRequestDispatcher.class);
/*     */   
/*     */   final RequestDispatcher parentDispatcher;
/*     */   
/*     */   final AbstractResourceMethod method;
/*     */   final AccountManager accountManager;
/*     */   final Cache<String, AccountConfiguration> authorized;
/*     */   
/*     */   public void dispatch(Object resource, HttpContext context)
/*     */   {
/*  50 */     String authHeader = extractAuthHeader(context.getRequest());
/*     */     
/*     */ 
/*  53 */     AccountConfiguration accountConfig = (AccountConfiguration)this.authorized.getIfPresent(authHeader);
/*  54 */     String accountName; String accountName; if (accountConfig != null) {
/*  55 */       accountName = accountConfig.getAccountName();
/*     */     } else {
/*  57 */       String decodedAuthHeader = decodeAuthHeader(authHeader);
/*  58 */       Collection<AccountConfiguration> symmetricKeyConfig = this.accountManager.findSystemAccountConfigurationsAsMap().get(decodedAuthHeader);
/*     */       
/*     */       String accountName;
/*  61 */       if ((symmetricKeyConfig != null) && (symmetricKeyConfig.size() > 0))
/*     */       {
/*  63 */         accountName = ((AccountConfiguration)Iterables.get(symmetricKeyConfig, 0)).getAccountName();
/*     */       } else {
/*  65 */         AccountConfiguration account = convertAuthHeader(decodedAuthHeader);
/*  66 */         validateAuth(account);
/*  67 */         this.authorized.put(authHeader, account);
/*  68 */         accountName = account.getAccountName();
/*     */       }
/*     */     }
/*     */     
/*  72 */     protectResourceByRole(accountName);
/*  73 */     context.getProperties().put("AD-Account-Name", accountName);
/*  74 */     this.parentDispatcher.dispatch(resource, context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void protectResourceByRole(String accountName)
/*     */   {
/*  85 */     String[] secureResourceAnnotation = findSecureResourceAnnotationRoles();
/*  86 */     if (secureResourceAnnotation.length > 0) {
/*  87 */       boolean authorized = false;
/*  88 */       for (String role : secureResourceAnnotation) {
/*  89 */         if (role.toLowerCase().equals(accountName.toLowerCase())) {
/*  90 */           authorized = true;
/*     */         }
/*     */       }
/*  93 */       if (!authorized) {
/*  94 */         throw RestError.create(StandardErrorCode.CODE_ROLE_UNAUTHORIZED, "The supplied auth information is for a role which does not have access to this resource.");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   String[] findSecureResourceAnnotationRoles()
/*     */   {
/* 101 */     SecureResource secureResourceAnnotation = (SecureResource)this.method.getMethod().getAnnotation(SecureResource.class);
/* 102 */     if (secureResourceAnnotation == null) {
/* 103 */       secureResourceAnnotation = (SecureResource)this.method.getMethod().getDeclaringClass().getAnnotation(SecureResource.class);
/*     */     }
/* 105 */     return secureResourceAnnotation.roles();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void validateAuth(AccountConfiguration account)
/*     */   {
/* 114 */     Optional<AccountConfiguration> accountConfiguration = this.accountManager.findAccountConfiguration(account.getAccountName());
/*     */     
/* 116 */     if (!accountConfiguration.isPresent()) {
/* 117 */       throw raiseAuthUnauthorized();
/*     */     }
/*     */     
/* 120 */     if (!((AccountConfiguration)accountConfiguration.get()).getAccessKey().equals(account.getAccessKey())) {
/* 121 */       throw raiseAuthUnauthorized();
/*     */     }
/*     */     
/* 124 */     DateTime expirationDate = ((AccountConfiguration)accountConfiguration.get()).getExpirationDate();
/* 125 */     if (expirationDate == null) {
/* 126 */       log.debug("No expiration date exists for account [{}]", account.getAccountName());
/* 127 */     } else if (expirationDate.isBeforeNow()) {
/* 128 */       throw RestError.create(StandardErrorCode.CODE_AUTH_EXPIRED, "The supplied auth information is for an account that expired on [{" + expirationDate + "}]");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   WebApplicationException raiseAuthUnauthorized()
/*     */   {
/* 135 */     throw RestError.create(StandardErrorCode.CODE_AUTH_UNAUTHORIZED, "The supplied auth information is incorrect or not authorized.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   String extractAuthHeader(HttpRequestContext request)
/*     */   {
/* 147 */     String authHeader = request.getHeaderValue("Authorization");
/* 148 */     if (Strings.isNullOrEmpty(authHeader)) {
/* 149 */       throw RestError.create(new BadRequestErrorCode("Missing.Header[Authorization]"), "An auth header must be supplied for all requests.");
/*     */     }
/*     */     
/* 152 */     return authHeader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   String decodeAuthHeader(String authHeader)
/*     */   {
/* 165 */     if (!authHeader.toLowerCase().startsWith("basic")) {
/* 166 */       throw RestError.create(new BadRequestErrorCode("Invalid.Header[Authorization]"), "Header[Authorization] should be of form 'Basic base64Creds'");
/*     */     }
/*     */     
/*     */     String decodedAuthCreds;
/*     */     try
/*     */     {
/* 172 */       decodedAuthCreds = new String(BaseEncoding.base64().decode(authHeader.substring(6)), Charsets.UTF_8);
/*     */     } catch (IllegalArgumentException e) {
/* 174 */       throw RestError.create(new BadRequestErrorCode("Invalid.Header[Authorization]"), "Could not decode Header[Authorization] as base64Creds");
/*     */     }
/*     */     
/* 177 */     return decodedAuthCreds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   AccountConfiguration convertAuthHeader(String decodedAuthHeader)
/*     */   {
/* 190 */     String[] parts = decodedAuthHeader.split(":");
/* 191 */     if (parts.length != 2) {
/* 192 */       throw RestError.create(new BadRequestErrorCode("Invalid.Header[Authorization]"), "Decoded Header[Authorization] should be of form 'accountName:accessKey'");
/*     */     }
/*     */     try
/*     */     {
/* 196 */       return new AccountConfiguration(parts[0], parts[1], null);
/*     */     } catch (IllegalAccountNameException e) {
/* 198 */       throw RestError.create(StandardErrorCode.CODE_INVALID_ACCOUNT_NAME, e.getMessage());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/auth/SecureResourceRequestDispatcher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */