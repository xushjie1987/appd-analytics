/*     */ package com.appdynamics.analytics.processor.elasticsearch.node.multi;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EsClientFilePollListener
/*     */   extends FileAlterationListenerAdaptor
/*     */ {
/*  33 */   private static final Logger log = LoggerFactory.getLogger(EsClientFilePollListener.class);
/*     */   
/*     */ 
/*     */   public static final String CLUSTER_NAME_PROP = "cluster.name";
/*     */   
/*     */   final Properties props;
/*     */   
/*     */   final ElasticSearchMultiNode esMultiNode;
/*     */   
/*     */   volatile Map<String, String> filePathtoClusterName;
/*     */   
/*     */ 
/*     */   public EsClientFilePollListener(Properties props, ElasticSearchMultiNode esMultiNode)
/*     */   {
/*  47 */     this.props = props;
/*  48 */     this.esMultiNode = esMultiNode;
/*  49 */     this.filePathtoClusterName = new ConcurrentHashMap();
/*     */   }
/*     */   
/*     */   public synchronized void onFileCreate(File file)
/*     */   {
/*  54 */     String path = file.getAbsolutePath();
/*     */     try {
/*  56 */       log.debug("File [{}] was created", path);
/*     */       
/*  58 */       if (this.filePathtoClusterName.get(path) != null) {
/*  59 */         log.info("Did not create new ES client for file [{}] since one already exists.", path);
/*  60 */         return;
/*     */       }
/*     */       
/*  63 */       Map<String, String> config = getClientConfig(file);
/*  64 */       String clusterName = getClusterName(config);
/*  65 */       this.esMultiNode.addClient(config);
/*     */       
/*  67 */       this.filePathtoClusterName.put(path, clusterName);
/*     */     } catch (Exception e) {
/*  69 */       log.error("Could not add client for file [{}]" + path, e);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void onFileChange(File file)
/*     */   {
/*     */     try {
/*  76 */       log.debug("File [{}] was modified", file.getAbsolutePath());
/*     */       
/*  78 */       Map<String, String> config = getClientConfig(file);
/*  79 */       String oldClusterName = (String)this.filePathtoClusterName.get(file.getAbsolutePath());
/*  80 */       String newClusterName = getClusterName(config);
/*  81 */       this.esMultiNode.updateClient(oldClusterName, config);
/*     */       
/*  83 */       this.filePathtoClusterName.remove(file.getAbsolutePath());
/*  84 */       this.filePathtoClusterName.put(file.getAbsolutePath(), newClusterName);
/*     */     } catch (Exception e) {
/*  86 */       log.error("Could not add client for file [{}]" + file.getAbsolutePath(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void onFileDelete(File file)
/*     */   {
/*  92 */     String path = file.getAbsolutePath();
/*     */     try {
/*  94 */       log.debug("File [{}] was deleted", path);
/*     */       
/*  96 */       String clusterNameToRemove = (String)this.filePathtoClusterName.get(path);
/*  97 */       if (clusterNameToRemove == null) {
/*  98 */         log.info("Did not remove ES client for file [{}] since one did not exist.", path);
/*  99 */         return;
/*     */       }
/*     */       
/* 102 */       this.esMultiNode.removeClient(clusterNameToRemove);
/* 103 */       this.filePathtoClusterName.remove(file.getAbsolutePath());
/*     */     } catch (Exception e) {
/* 105 */       log.error("Could not add client for file [{}]" + path, e);
/*     */     }
/*     */   }
/*     */   
/*     */   Map<String, String> getClientConfig(File file) throws IOException {
/* 110 */     String resolved = readAndResolveVariables(file);
/* 111 */     Map<String, String> config = (Map)Reader.DEFAULT_YAML_MAPPER.readValue(resolved, new TypeReference() {});
/* 114 */     return config;
/*     */   }
/*     */   
/*     */   String getClusterName(Map<String, String> config) {
/* 118 */     String clusterName = (String)config.get("cluster.name");
/* 119 */     if (clusterName == null) {
/* 120 */       throw new IllegalArgumentException("Property [cluster.name] missing from es client config");
/*     */     }
/* 122 */     return clusterName;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   String readAndResolveVariables(File file)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 42	java/io/FileInputStream
/*     */     //   3: dup
/*     */     //   4: aload_1
/*     */     //   5: invokespecial 43	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   8: astore_2
/*     */     //   9: aconst_null
/*     */     //   10: astore_3
/*     */     //   11: new 44	java/io/ByteArrayOutputStream
/*     */     //   14: dup
/*     */     //   15: invokespecial 45	java/io/ByteArrayOutputStream:<init>	()V
/*     */     //   18: astore 4
/*     */     //   20: aconst_null
/*     */     //   21: astore 5
/*     */     //   23: aload_2
/*     */     //   24: aload_0
/*     */     //   25: getfield 2	com/appdynamics/analytics/processor/elasticsearch/node/multi/EsClientFilePollListener:props	Ljava/util/Properties;
/*     */     //   28: aload 4
/*     */     //   30: invokestatic 46	com/appdynamics/common/util/var/Variables:resolveVariables	(Ljava/io/InputStream;Ljava/util/Properties;Ljava/io/OutputStream;)V
/*     */     //   33: getstatic 8	com/appdynamics/analytics/processor/elasticsearch/node/multi/EsClientFilePollListener:log	Lorg/slf4j/Logger;
/*     */     //   36: ldc 47
/*     */     //   38: aload_1
/*     */     //   39: invokevirtual 7	java/io/File:getAbsolutePath	()Ljava/lang/String;
/*     */     //   42: invokeinterface 10 3 0
/*     */     //   47: new 26	java/lang/String
/*     */     //   50: dup
/*     */     //   51: aload 4
/*     */     //   53: invokevirtual 48	java/io/ByteArrayOutputStream:toByteArray	()[B
/*     */     //   56: getstatic 49	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   59: invokespecial 50	java/lang/String:<init>	([BLjava/nio/charset/Charset;)V
/*     */     //   62: astore 6
/*     */     //   64: aload 4
/*     */     //   66: ifnull +33 -> 99
/*     */     //   69: aload 5
/*     */     //   71: ifnull +23 -> 94
/*     */     //   74: aload 4
/*     */     //   76: invokevirtual 51	java/io/ByteArrayOutputStream:close	()V
/*     */     //   79: goto +20 -> 99
/*     */     //   82: astore 7
/*     */     //   84: aload 5
/*     */     //   86: aload 7
/*     */     //   88: invokevirtual 53	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   91: goto +8 -> 99
/*     */     //   94: aload 4
/*     */     //   96: invokevirtual 51	java/io/ByteArrayOutputStream:close	()V
/*     */     //   99: aload_2
/*     */     //   100: ifnull +29 -> 129
/*     */     //   103: aload_3
/*     */     //   104: ifnull +21 -> 125
/*     */     //   107: aload_2
/*     */     //   108: invokevirtual 54	java/io/InputStream:close	()V
/*     */     //   111: goto +18 -> 129
/*     */     //   114: astore 7
/*     */     //   116: aload_3
/*     */     //   117: aload 7
/*     */     //   119: invokevirtual 53	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   122: goto +7 -> 129
/*     */     //   125: aload_2
/*     */     //   126: invokevirtual 54	java/io/InputStream:close	()V
/*     */     //   129: aload 6
/*     */     //   131: areturn
/*     */     //   132: astore 6
/*     */     //   134: aload 6
/*     */     //   136: astore 5
/*     */     //   138: aload 6
/*     */     //   140: athrow
/*     */     //   141: astore 8
/*     */     //   143: aload 4
/*     */     //   145: ifnull +33 -> 178
/*     */     //   148: aload 5
/*     */     //   150: ifnull +23 -> 173
/*     */     //   153: aload 4
/*     */     //   155: invokevirtual 51	java/io/ByteArrayOutputStream:close	()V
/*     */     //   158: goto +20 -> 178
/*     */     //   161: astore 9
/*     */     //   163: aload 5
/*     */     //   165: aload 9
/*     */     //   167: invokevirtual 53	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   170: goto +8 -> 178
/*     */     //   173: aload 4
/*     */     //   175: invokevirtual 51	java/io/ByteArrayOutputStream:close	()V
/*     */     //   178: aload 8
/*     */     //   180: athrow
/*     */     //   181: astore 4
/*     */     //   183: aload 4
/*     */     //   185: astore_3
/*     */     //   186: aload 4
/*     */     //   188: athrow
/*     */     //   189: astore 10
/*     */     //   191: aload_2
/*     */     //   192: ifnull +29 -> 221
/*     */     //   195: aload_3
/*     */     //   196: ifnull +21 -> 217
/*     */     //   199: aload_2
/*     */     //   200: invokevirtual 54	java/io/InputStream:close	()V
/*     */     //   203: goto +18 -> 221
/*     */     //   206: astore 11
/*     */     //   208: aload_3
/*     */     //   209: aload 11
/*     */     //   211: invokevirtual 53	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   214: goto +7 -> 221
/*     */     //   217: aload_2
/*     */     //   218: invokevirtual 54	java/io/InputStream:close	()V
/*     */     //   221: aload 10
/*     */     //   223: athrow
/*     */     // Line number table:
/*     */     //   Java source line #126	-> byte code offset #0
/*     */     //   Java source line #127	-> byte code offset #11
/*     */     //   Java source line #126	-> byte code offset #20
/*     */     //   Java source line #129	-> byte code offset #23
/*     */     //   Java source line #130	-> byte code offset #33
/*     */     //   Java source line #132	-> byte code offset #47
/*     */     //   Java source line #133	-> byte code offset #64
/*     */     //   Java source line #126	-> byte code offset #132
/*     */     //   Java source line #133	-> byte code offset #141
/*     */     //   Java source line #126	-> byte code offset #181
/*     */     //   Java source line #133	-> byte code offset #189
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	224	0	this	EsClientFilePollListener
/*     */     //   0	224	1	file	File
/*     */     //   8	210	2	inputStream	java.io.InputStream
/*     */     //   10	199	3	localThrowable3	Throwable
/*     */     //   18	156	4	outputStream	java.io.ByteArrayOutputStream
/*     */     //   181	6	4	localThrowable2	Throwable
/*     */     //   21	143	5	localThrowable4	Throwable
/*     */     //   62	68	6	str	String
/*     */     //   132	7	6	localThrowable1	Throwable
/*     */     //   132	7	6	localThrowable5	Throwable
/*     */     //   82	5	7	x2	Throwable
/*     */     //   114	4	7	x2	Throwable
/*     */     //   141	38	8	localObject1	Object
/*     */     //   161	5	9	x2	Throwable
/*     */     //   189	33	10	localObject2	Object
/*     */     //   206	4	11	x2	Throwable
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   74	79	82	java/lang/Throwable
/*     */     //   107	111	114	java/lang/Throwable
/*     */     //   23	64	132	java/lang/Throwable
/*     */     //   23	64	141	finally
/*     */     //   132	143	141	finally
/*     */     //   153	158	161	java/lang/Throwable
/*     */     //   11	99	181	java/lang/Throwable
/*     */     //   132	181	181	java/lang/Throwable
/*     */     //   11	99	189	finally
/*     */     //   132	191	189	finally
/*     */     //   199	203	206	java/lang/Throwable
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/elasticsearch/node/multi/EsClientFilePollListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */