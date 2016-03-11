/*    */ package com.appdynamics.analytics.processor.util;
/*    */ 
/*    */ import com.appdynamics.common.util.version.VersionAware;
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
/*    */ public abstract class VersionManager
/*    */   implements VersionAware
/*    */ {
/*    */   public static final int DEFAULT_VERSION = 1;
/*    */   
/*    */   public void updateVersion(int newVersion)
/*    */   {
/* 24 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void checkSchemaVersion(String name, int expectingVersion)
/*    */   {
/* 33 */     int version = getVersion();
/* 34 */     if (version != expectingVersion) {
/* 35 */       throw new IllegalStateException(String.format("[%s] is from version [%d] while the expected version is [%d]", new Object[] { name, Integer.valueOf(version), Integer.valueOf(expectingVersion) }));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/util/VersionManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */