/*    */ package com.appdynamics.analytics.processor.zookeeper.client;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.util.VersionManager;
/*    */ import com.google.common.base.Charsets;
/*    */ import org.apache.curator.framework.CuratorFramework;
/*    */ import org.apache.curator.framework.api.CreateBuilder;
/*    */ import org.apache.curator.framework.api.ExistsBuilder;
/*    */ import org.apache.curator.framework.api.GetDataBuilder;
/*    */ import org.apache.curator.framework.api.ProtectACLCreateModePathAndBytesable;
/*    */ import org.apache.curator.framework.api.SetDataBuilder;
/*    */ import org.apache.curator.utils.ZKPaths;
/*    */ import org.apache.zookeeper.KeeperException.NoNodeException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZookeeperVersionManager
/*    */   extends VersionManager
/*    */ {
/*    */   private CuratorFramework zkClient;
/*    */   private String zkSchemaPath;
/*    */   private String zkVersionPath;
/*    */   
/*    */   public ZookeeperVersionManager(CuratorFramework zkClient, String zkSchemaPath)
/*    */   {
/* 26 */     this.zkClient = zkClient;
/* 27 */     this.zkSchemaPath = zkSchemaPath;
/* 28 */     this.zkVersionPath = ZKPaths.makePath(this.zkSchemaPath, "version");
/*    */   }
/*    */   
/*    */   public int getVersion()
/*    */   {
/*    */     try {
/* 34 */       ensureZkPathExists();
/*    */       
/* 36 */       int version = 1;
/*    */       try {
/* 38 */         byte[] value = (byte[])this.zkClient.getData().forPath(this.zkVersionPath);
/* 39 */         version = Integer.parseInt(new String(value, Charsets.UTF_8));
/*    */       }
/*    */       catch (KeeperException.NoNodeException e) {
/* 42 */         updateVersion(version);
/*    */       }
/*    */       
/* 45 */       return version;
/*    */     } catch (Exception e) {
/* 47 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateVersion(int newVersion)
/*    */   {
/*    */     try {
/* 54 */       ensureZkPathExists();
/* 55 */       byte[] serializedVersion = String.valueOf(newVersion).getBytes(Charsets.UTF_8);
/*    */       try {
/* 57 */         this.zkClient.setData().forPath(this.zkVersionPath, serializedVersion);
/*    */       } catch (KeeperException.NoNodeException e) {
/* 59 */         this.zkClient.create().forPath(this.zkVersionPath, serializedVersion);
/*    */       }
/*    */     } catch (Exception e) {
/* 62 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getZkSchemaVersionPath() {
/* 67 */     return this.zkVersionPath;
/*    */   }
/*    */   
/*    */   private void ensureZkPathExists() throws Exception {
/* 71 */     if (this.zkClient.checkExists().forPath(this.zkSchemaPath) == null) {
/* 72 */       this.zkClient.create().creatingParentsIfNeeded().forPath(this.zkSchemaPath, "[]".getBytes(Charsets.UTF_8));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/client/ZookeeperVersionManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */