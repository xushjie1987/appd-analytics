/*    */ package com.appdynamics.common.util.lifecycle;
/*    */ 
/*    */ import java.util.LinkedList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LifecycleHelper
/*    */ {
/*    */   public static void startAll(Iterable<? extends LifecycleAware> iterable)
/*    */   {
/* 25 */     LinkedList<LifecycleAware> started = new LinkedList();
/*    */     try {
/* 27 */       for (LifecycleAware lifecycleAware : iterable) {
/* 28 */         lifecycleAware.start();
/* 29 */         started.add(lifecycleAware);
/*    */       }
/*    */     } catch (Exception e) {
/* 32 */       for (LifecycleAware lifecycleAware : started) {
/*    */         try {
/* 34 */           lifecycleAware.stop();
/*    */         } catch (Exception e1) {
/* 36 */           e.addSuppressed(e1);
/*    */         }
/*    */       }
/*    */       
/* 40 */       throw e;
/*    */     } finally {
/* 42 */       started.clear();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void stopAll(Iterable<? extends LifecycleAware> iterable)
/*    */   {
/* 52 */     for (LifecycleAware lifecycleAware : iterable) {
/* 53 */       lifecycleAware.stop();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/lifecycle/LifecycleHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */