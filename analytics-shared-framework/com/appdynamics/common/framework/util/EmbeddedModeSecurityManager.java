/*    */ package com.appdynamics.common.framework.util;
/*    */ 
/*    */ import java.security.Permission;
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
/*    */ public class EmbeddedModeSecurityManager
/*    */   extends SecurityManager
/*    */ {
/* 18 */   private static final Logger log = LoggerFactory.getLogger(EmbeddedModeSecurityManager.class);
/*    */   
/*    */ 
/*    */   private final SecurityManager optOldSecurityManager;
/*    */   
/*    */   private final String[] disallowedPackages;
/*    */   
/*    */ 
/*    */   EmbeddedModeSecurityManager(SecurityManager optOldSecurityManager, String[] disallowedPackages)
/*    */   {
/* 28 */     this.optOldSecurityManager = optOldSecurityManager;
/* 29 */     this.disallowedPackages = disallowedPackages;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void installSecurityManager(String... disallowedPackages)
/*    */   {
/* 39 */     SecurityManager oldSecurityManager = System.getSecurityManager();
/*    */     
/* 41 */     if (!(oldSecurityManager instanceof EmbeddedModeSecurityManager)) {
/* 42 */       SecurityManager securityManager = new EmbeddedModeSecurityManager(oldSecurityManager, disallowedPackages);
/* 43 */       System.setSecurityManager(securityManager);
/* 44 */       log.debug("Installed");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void checkExit(int status)
/*    */   {
/* 54 */     log.debug("Shutdown request from [" + Thread.currentThread().getName() + "]", new Throwable());
/*    */     
/*    */ 
/* 57 */     StackTraceElement[] elements = Thread.currentThread().getStackTrace();
/* 58 */     for (StackTraceElement element : elements) {
/* 59 */       String s = element.toString();
/* 60 */       for (String disallowedPackage : this.disallowedPackages) {
/* 61 */         if (s.contains(disallowedPackage)) {
/* 62 */           log.debug("Shutdown request suppressed as the thread stack contains [{}]", disallowedPackage);
/* 63 */           throw new SuppressedSystemExitException();
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 68 */     if (this.optOldSecurityManager != null) {
/* 69 */       this.optOldSecurityManager.checkExit(status);
/*    */     }
/*    */   }
/*    */   
/*    */   public void checkPermission(Permission perm)
/*    */   {
/* 75 */     if (this.optOldSecurityManager != null) {
/* 76 */       this.optOldSecurityManager.checkPermission(perm);
/*    */     }
/*    */   }
/*    */   
/*    */   public void checkPermission(Permission perm, Object context)
/*    */   {
/* 82 */     if (this.optOldSecurityManager != null) {
/* 83 */       this.optOldSecurityManager.checkPermission(perm, context);
/*    */     }
/*    */   }
/*    */   
/*    */   static class SuppressedSystemExitException extends SecurityException {
/*    */     SuppressedSystemExitException() {
/* 89 */       super();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/EmbeddedModeSecurityManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */