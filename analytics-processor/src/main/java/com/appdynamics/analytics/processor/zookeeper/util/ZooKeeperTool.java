/*     */ package com.appdynamics.analytics.processor.zookeeper.util;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Throwables;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.curator.framework.CuratorFramework;
/*     */ import org.apache.curator.framework.api.BackgroundVersionable;
/*     */ import org.apache.curator.framework.api.CreateBuilder;
/*     */ import org.apache.curator.framework.api.DeleteBuilder;
/*     */ import org.apache.curator.framework.api.GetDataBuilder;
/*     */ import org.apache.curator.framework.api.ProtectACLCreateModePathAndBytesable;
/*     */ import org.apache.curator.framework.api.SetDataBuilder;
/*     */ import org.apache.curator.framework.api.SyncBuilder;
/*     */ import org.apache.curator.utils.ZKPaths;
/*     */ import org.apache.zookeeper.KeeperException.NoNodeException;
/*     */ import org.apache.zookeeper.KeeperException.NodeExistsException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public abstract class ZooKeeperTool
/*     */ {
/*  28 */   private static final Logger log = LoggerFactory.getLogger(ZooKeeperTool.class);
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String FIELD_DATA = "__data__";
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String PATH_ROOT = "/";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map<String, Object> exportTree(CuratorFramework curatorFramework, String path)
/*     */   {
/*     */     try
/*     */     {
/*  45 */       Map<String, Object> results = newMap();
/*  46 */       Object result = collectRecursive(curatorFramework, path);
/*  47 */       results.put("/", result);
/*  48 */       return results;
/*     */     } catch (Exception e) {
/*  50 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Object collectRecursive(CuratorFramework curatorFramework, String path)
/*     */     throws Exception
/*     */   {
/*  62 */     log.debug("Collection attempt at [{}]", path);
/*     */     
/*  64 */     byte[] rawData = (byte[])curatorFramework.getData().forPath(path);
/*  65 */     Object data = null;
/*  66 */     if ((rawData != null) && (rawData.length > 0)) {
/*  67 */       data = exportData(rawData);
/*     */     }
/*     */     
/*  70 */     Map<String, Object> childrenWithData = newMap();
/*  71 */     List<String> children = (List)curatorFramework.getChildren().forPath(path);
/*  72 */     for (String childSegment : children) {
/*  73 */       String newPath = ZKPaths.makePath(path, childSegment);
/*     */       
/*  75 */       Object result = collectRecursive(curatorFramework, newPath);
/*  76 */       childrenWithData.put(childSegment, result);
/*     */     }
/*  78 */     if (data != null)
/*     */     {
/*  80 */       if (children.isEmpty()) {
/*  81 */         return data;
/*     */       }
/*     */       
/*     */ 
/*  85 */       childrenWithData.put("__data__", data);
/*     */     }
/*     */     
/*  88 */     return childrenWithData;
/*     */   }
/*     */   
/*     */   private static Map<String, Object> newMap() {
/*  92 */     return new java.util.LinkedHashMap();
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
/*     */ 
/*     */   public static void importTree(CuratorFramework curatorFramework, String path, boolean merge, Map<String, Object> data)
/*     */   {
/* 106 */     if (data.size() != 1) {
/* 107 */       throw new IllegalArgumentException("The map containing the data to import has more than one root" + data.keySet() + ". It can have only 1 root key");
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 113 */       if (!merge) {
/* 114 */         String subTreeRoot = (String)data.keySet().iterator().next();
/* 115 */         String newPath = newPath(path, subTreeRoot);
/*     */         
/* 117 */         log.info("Attempting to delete [{}] and its children", newPath);
/*     */         try {
/* 119 */           curatorFramework.delete().deletingChildrenIfNeeded().forPath(newPath);
/*     */         } catch (KeeperException.NoNodeException e) {
/* 121 */           log.debug("Path [" + newPath + "] does not exist so there is not need to delete it", e);
/*     */         }
/* 123 */         log.info("Deleted");
/*     */       }
/*     */       
/* 126 */       storeRecursive(curatorFramework, path, data);
/* 127 */       curatorFramework.sync().forPath(path);
/*     */     } catch (Exception e) {
/* 129 */       Throwables.propagate(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void storeRecursive(CuratorFramework curatorFramework, String path, Map<String, Object> data)
/*     */   {
/* 135 */     log.debug("Storage attempt at [{}]", path);
/*     */     
/* 137 */     ensurePathExists(curatorFramework, path);
/*     */     
/* 139 */     for (Map.Entry<String, Object> entry : data.entrySet()) {
/* 140 */       String key = (String)entry.getKey();
/* 141 */       String newPath = newPath(path, key);
/* 142 */       Object value = entry.getValue();
/*     */       
/* 144 */       if (("__data__".equals(key)) || (!(value instanceof Map))) {
/* 145 */         ensurePathExists(curatorFramework, newPath);
/*     */         try {
/* 147 */           storeData(curatorFramework, newPath, value);
/*     */         } catch (Exception e) {
/* 149 */           Throwables.propagate(e);
/*     */         }
/*     */       } else {
/* 152 */         storeRecursive(curatorFramework, newPath, (Map)value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static void storeData(CuratorFramework curatorFramework, String path, Object value) throws Exception {
/* 158 */     byte[] bytes = importData(value);
/* 159 */     curatorFramework.setData().forPath(path, bytes);
/*     */   }
/*     */   
/*     */   static String newPath(String path, String suffix)
/*     */   {
/* 164 */     return "/".equals(suffix) ? path : ZKPaths.makePath(path, suffix);
/*     */   }
/*     */   
/*     */   static void ensurePathExists(CuratorFramework curatorFramework, String path) {
/*     */     try {
/* 169 */       String result = (String)curatorFramework.create().creatingParentsIfNeeded().forPath(path, new byte[0]);
/* 170 */       log.trace("Path creation of [{}] resulted in [{}]", path, result);
/*     */     } catch (KeeperException.NodeExistsException e) {
/* 172 */       log.debug("The path [" + path + "] already exists", e);
/*     */     } catch (Exception e) {
/* 174 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   static Object exportData(byte[] data) {
/* 179 */     return data.length == 0 ? "" : new String(data, Charsets.UTF_8);
/*     */   }
/*     */   
/*     */   static byte[] importData(Object data) {
/* 183 */     if ((data instanceof String)) {
/* 184 */       return ((String)data).getBytes(Charsets.UTF_8);
/*     */     }
/*     */     try {
/* 187 */       return Reader.DEFAULT_JSON_MAPPER.writeValueAsString(data).getBytes(Charsets.UTF_8);
/*     */     } catch (JsonProcessingException e) {
/* 189 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/zookeeper/util/ZooKeeperTool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */