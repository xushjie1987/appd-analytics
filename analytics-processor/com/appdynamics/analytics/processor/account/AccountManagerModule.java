/*    */ package com.appdynamics.analytics.processor.account;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.account.configuration.AccountManagerConfiguration;
/*    */ import com.appdynamics.analytics.processor.account.resource.AccountResource;
/*    */ import com.appdynamics.analytics.processor.auth.SecureDispatchAdapter;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccountManagerModule
/*    */   extends Module<AccountManagerConfiguration>
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(AccountManagerModule.class);
/*    */   
/*    */   @Inject
/*    */   void onStart(Environment environment, AccountManager accountManager, AccountResource accountResource)
/*    */   {
/* 32 */     environment.jersey().register(new SecureDispatchAdapter(accountManager));
/* 33 */     environment.jersey().register(accountResource);
/*    */   }
/*    */   
/*    */   @Provides
/*    */   AccountManagerConfiguration provideAuthConfig() {
/* 38 */     return (AccountManagerConfiguration)getConfiguration();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/account/AccountManagerModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */