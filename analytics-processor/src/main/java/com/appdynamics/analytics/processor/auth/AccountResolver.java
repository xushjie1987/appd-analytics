/*    */ package com.appdynamics.analytics.processor.auth;
/*    */ 
/*    */ import com.sun.jersey.api.core.HttpContext;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccountResolver
/*    */ {
/* 16 */   private static final Logger log = LoggerFactory.getLogger(AccountResolver.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final String ACCOUNT_KEY = "AD-Account-Name";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String resolveAccountName(HttpContext context)
/*    */   {
/* 29 */     Object accountName = context.getProperties().get("AD-Account-Name");
/* 30 */     if ((accountName == null) || (!accountName.getClass().equals(String.class))) {
/* 31 */       log.error("No account name set on http context.  Is the AccountManagerModule configured in the yml?");
/* 32 */       return null;
/*    */     }
/*    */     
/* 35 */     return (String)accountName;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/auth/AccountResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */