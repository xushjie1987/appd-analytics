/*     */ package com.appdynamics.analytics.processor.kafka.util;
/*     */ 
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.yammer.metrics.core.Metric;
/*     */ import com.yammer.metrics.core.MetricName;
/*     */ import com.yammer.metrics.core.MetricPredicate;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import kafka.admin.AdminUtils;
/*     */ import kafka.admin.PreferredReplicaLeaderElectionCommand;
/*     */ import kafka.admin.TopicCommand;
/*     */ import kafka.admin.TopicCommand.TopicCommandOptions;
/*     */ import kafka.common.TopicAndPartition;
/*     */ import kafka.common.TopicExistsException;
/*     */ import kafka.controller.ReassignedPartitionsContext;
/*     */ import kafka.tools.ConsumerOffsetChecker;
/*     */ import kafka.utils.ZKStringSerializer.;
/*     */ import kafka.utils.ZkUtils;
/*     */ import org.I0Itec.zkclient.ZkClient;
/*     */ import org.I0Itec.zkclient.exception.ZkInterruptedException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import scala.Console;
/*     */ import scala.collection.JavaConversions;
/*     */ import scala.collection.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class KafkaTool
/*     */ {
/*  61 */   private static final Logger log = LoggerFactory.getLogger(KafkaTool.class);
/*     */   
/*     */   static final String ZK_CONSUMER_GROUPS_PATH = "/consumers";
/*     */   static final String CMD_UNDER_REPLICATED_PARTITIONS = "under-replicated-partitions";
/*     */   static final String CMD_UNAVAILABLE_PARTITIONS = "unavailable-partitions";
/*     */   static final String CMD_CONSUMER_OFFSETS = "consumer-offsets";
/*     */   static final int SCALA_CLI_TASK_TIMEOUT_SECONDS = 5;
/*  68 */   static final ExecutorService SCALA_CLI_TASK_EXECUTOR = Executors.newSingleThreadExecutor(ConcurrencyHelper.newDaemonThreadFactory("scala-cli-task-executor-%d"));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Report runReport(String zkCsvList, ZkClient zkClient)
/*     */   {
/*  91 */     Report report = new Report();
/*     */     
/*  93 */     String allPartitions = null;
/*     */     try {
/*  95 */       allPartitions = findAllPartitions(zkClient);
/*  96 */       report.setAllPartitionsStatus(allPartitions);
/*     */     } catch (Exception e) {
/*  98 */       report.setAllPartitionsException(e);
/*     */     }
/* 100 */     String underReplicated = null;
/*     */     try {
/* 102 */       underReplicated = findUnderReplicatedPartitions(zkClient);
/* 103 */       report.setUnderReplicatedPartitionsStatus(underReplicated);
/*     */     } catch (Exception e) {
/* 105 */       report.setUnderReplicatedPartitionsException(e);
/*     */     }
/* 107 */     String unavailable = null;
/*     */     try {
/* 109 */       unavailable = findUnavailablePartitions(zkClient);
/* 110 */       report.setUnavailablePartitionsStatus(unavailable);
/*     */     } catch (Exception e) {
/* 112 */       report.setUnavailablePartitionsException(e);
/*     */     }
/* 114 */     String movingPartitionsStr = null;
/*     */     try {
/* 116 */       java.util.Map<TopicAndPartition, ReassignedPartitionsContext> movingPartitions = findInProgressPartitionReassignments(zkClient);
/*     */       
/* 118 */       movingPartitionsStr = movingPartitions.isEmpty() ? "" : movingPartitions.toString();
/* 119 */       report.setMovingPartitionsStatus(movingPartitionsStr);
/*     */     } catch (Exception e) {
/* 121 */       report.setMetricsException(e);
/*     */     }
/* 123 */     String consumerOffsets = null;
/*     */     try {
/* 125 */       consumerOffsets = findConsumerOffsets(zkCsvList, zkClient);
/* 126 */       report.setConsumerOffsetsStatus(consumerOffsets);
/*     */     } catch (Exception e) {
/* 128 */       report.setConsumerOffsetsException(e);
/*     */     }
/* 130 */     String metricsReport = null;
/*     */     try {
/* 132 */       metricsReport = runMetricsReport();
/* 133 */       report.setMetricsReport(metricsReport);
/*     */     } catch (Exception e) {
/* 135 */       report.setMetricsException(e);
/*     */     }
/*     */     
/* 138 */     return report;
/*     */   }
/*     */   
/*     */   static String findAllPartitions(ZkClient zkClient) {
/* 142 */     return runTopicCommand(zkClient, Optional.absent());
/*     */   }
/*     */   
/*     */   static String findUnderReplicatedPartitions(ZkClient zkClient) {
/* 146 */     return runTopicCommand(zkClient, Optional.of("under-replicated-partitions"));
/*     */   }
/*     */   
/*     */   static String findUnavailablePartitions(ZkClient zkClient) {
/* 150 */     return runTopicCommand(zkClient, Optional.of("unavailable-partitions"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static java.util.Map<TopicAndPartition, ReassignedPartitionsContext> findInProgressPartitionReassignments(ZkClient zkClient)
/*     */   {
/*     */     try
/*     */     {
/* 161 */       scala.collection.Map<TopicAndPartition, ReassignedPartitionsContext> scalaMap = ZkUtils.getPartitionsBeingReassigned(zkClient);
/*     */       
/* 163 */       return JavaConversions.asJavaMap(scalaMap);
/*     */     } catch (RuntimeException e) {
/* 165 */       throw new RuntimeException("Error occurred while running [moving-partitions] command", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String findConsumerOffsets(final String zkCsvList, ZkClient zkClient)
/*     */   {
/* 176 */     List<String> consumerGroups = zkClient.getChildren("/consumers");
/* 177 */     if ((consumerGroups == null) || (consumerGroups.isEmpty())) {
/* 178 */       log.warn("Could not find any Kafka consumer groups");
/* 179 */       return "";
/*     */     }
/*     */     
/* 182 */     boolean isFirst = true;
/* 183 */     StringBuilder offsets = new StringBuilder();
/* 184 */     final Exception[] exceptions = new Exception[1];
/*     */     
/* 186 */     for (String consumerGroup : consumerGroups) {
/* 187 */       if (!isFirst) {
/* 188 */         offsets.append("\n");
/*     */       } else {
/* 190 */         isFirst = false;
/*     */       }
/*     */       
/* 193 */       offsets.append(runScalaCliTask("consumer-offsets", new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 197 */           String[] parameters = { "--group", this.val$consumerGroup, "--zookeeper", zkCsvList };
/*     */           try {
/* 199 */             ConsumerOffsetChecker.main(parameters);
/*     */           }
/*     */           catch (Exception e) {
/* 202 */             exceptions[0] = e;
/*     */           }
/*     */         }
/*     */       }));
/*     */       
/* 207 */       if (exceptions[0] != null) {
/* 208 */         log.warn("Exception occurred while running [{}] command for consumer group [{}]", new Object[] { "consumer-offsets", consumerGroup, exceptions[0] });
/*     */         
/* 210 */         return offsets + "\n(Exception: " + Throwables.getRootCause(exceptions[0]).getMessage() + ")";
/*     */       }
/*     */     }
/*     */     
/* 214 */     return offsets.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String runTopicCommand(final ZkClient zkClient, Optional<String> command)
/*     */   {
/* 224 */     runScalaCliTask("describe-topics", new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 228 */         String[] parameters = { this.val$command.isPresent() ? new String[] { "--list", "--" + (String)this.val$command.get() } : "--list" };
/*     */         
/* 230 */         TopicCommand.describeTopic(zkClient, new TopicCommand.TopicCommandOptions(parameters));
/*     */       }
/*     */     });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String runScalaCliTask(final String description, Runnable task)
/*     */   {
/* 251 */     Future<ByteArrayOutputStream> future = SCALA_CLI_TASK_EXECUTOR.submit(new Callable()
/*     */     {
/*     */       public ByteArrayOutputStream call() {
/* 254 */         PrintStream oldStream = Console.out();
/* 255 */         ByteArrayOutputStream newStream = new ByteArrayOutputStream();
/* 256 */         Console.setOut(newStream);
/*     */         try {
/* 258 */           this.val$task.run();
/* 259 */           return newStream;
/*     */         } catch (Exception e) {
/* 261 */           throw new RuntimeException("Error occurred while executing [" + description + "] command", e);
/*     */         } finally {
/* 263 */           Console.setOut(oldStream);
/*     */         }
/*     */       }
/* 266 */     });
/* 267 */     return new String(((ByteArrayOutputStream)ConcurrencyHelper.getOrCancel(future, 5, log)).toByteArray(), Charsets.UTF_8);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   static String runMetricsReport()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 70	java/io/ByteArrayOutputStream
/*     */     //   3: dup
/*     */     //   4: invokespecial 74	java/io/ByteArrayOutputStream:<init>	()V
/*     */     //   7: astore_0
/*     */     //   8: new 75	java/io/PrintStream
/*     */     //   11: dup
/*     */     //   12: aload_0
/*     */     //   13: iconst_1
/*     */     //   14: getstatic 72	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   17: invokevirtual 76	java/nio/charset/Charset:toString	()Ljava/lang/String;
/*     */     //   20: invokespecial 77	java/io/PrintStream:<init>	(Ljava/io/OutputStream;ZLjava/lang/String;)V
/*     */     //   23: astore_1
/*     */     //   24: aconst_null
/*     */     //   25: astore_2
/*     */     //   26: new 78	com/yammer/metrics/reporting/ConsoleReporter
/*     */     //   29: dup
/*     */     //   30: invokestatic 79	com/yammer/metrics/Metrics:defaultRegistry	()Lcom/yammer/metrics/core/MetricsRegistry;
/*     */     //   33: aload_1
/*     */     //   34: new 80	com/appdynamics/analytics/processor/kafka/util/KafkaTool$4
/*     */     //   37: dup
/*     */     //   38: invokespecial 81	com/appdynamics/analytics/processor/kafka/util/KafkaTool$4:<init>	()V
/*     */     //   41: invokespecial 82	com/yammer/metrics/reporting/ConsoleReporter:<init>	(Lcom/yammer/metrics/core/MetricsRegistry;Ljava/io/PrintStream;Lcom/yammer/metrics/core/MetricPredicate;)V
/*     */     //   44: invokevirtual 83	com/yammer/metrics/reporting/ConsoleReporter:run	()V
/*     */     //   47: aload_1
/*     */     //   48: invokevirtual 84	java/io/PrintStream:flush	()V
/*     */     //   51: new 46	java/lang/String
/*     */     //   54: dup
/*     */     //   55: aload_0
/*     */     //   56: invokevirtual 71	java/io/ByteArrayOutputStream:toByteArray	()[B
/*     */     //   59: getstatic 72	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   62: invokespecial 73	java/lang/String:<init>	([BLjava/nio/charset/Charset;)V
/*     */     //   65: astore_3
/*     */     //   66: aload_3
/*     */     //   67: bipush 10
/*     */     //   69: invokevirtual 85	java/lang/String:indexOf	(I)I
/*     */     //   72: istore 4
/*     */     //   74: iload 4
/*     */     //   76: iconst_m1
/*     */     //   77: if_icmple +12 -> 89
/*     */     //   80: aload_3
/*     */     //   81: iload 4
/*     */     //   83: invokevirtual 86	java/lang/String:substring	(I)Ljava/lang/String;
/*     */     //   86: goto +4 -> 90
/*     */     //   89: aload_3
/*     */     //   90: astore 5
/*     */     //   92: aload_1
/*     */     //   93: ifnull +29 -> 122
/*     */     //   96: aload_2
/*     */     //   97: ifnull +21 -> 118
/*     */     //   100: aload_1
/*     */     //   101: invokevirtual 87	java/io/PrintStream:close	()V
/*     */     //   104: goto +18 -> 122
/*     */     //   107: astore 6
/*     */     //   109: aload_2
/*     */     //   110: aload 6
/*     */     //   112: invokevirtual 89	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   115: goto +7 -> 122
/*     */     //   118: aload_1
/*     */     //   119: invokevirtual 87	java/io/PrintStream:close	()V
/*     */     //   122: aload 5
/*     */     //   124: areturn
/*     */     //   125: astore_3
/*     */     //   126: aload_3
/*     */     //   127: astore_2
/*     */     //   128: aload_3
/*     */     //   129: athrow
/*     */     //   130: astore 7
/*     */     //   132: aload_1
/*     */     //   133: ifnull +29 -> 162
/*     */     //   136: aload_2
/*     */     //   137: ifnull +21 -> 158
/*     */     //   140: aload_1
/*     */     //   141: invokevirtual 87	java/io/PrintStream:close	()V
/*     */     //   144: goto +18 -> 162
/*     */     //   147: astore 8
/*     */     //   149: aload_2
/*     */     //   150: aload 8
/*     */     //   152: invokevirtual 89	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
/*     */     //   155: goto +7 -> 162
/*     */     //   158: aload_1
/*     */     //   159: invokevirtual 87	java/io/PrintStream:close	()V
/*     */     //   162: aload 7
/*     */     //   164: athrow
/*     */     //   165: astore_1
/*     */     //   166: new 32	java/lang/RuntimeException
/*     */     //   169: dup
/*     */     //   170: ldc 91
/*     */     //   172: aload_1
/*     */     //   173: invokespecial 34	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   176: athrow
/*     */     // Line number table:
/*     */     //   Java source line #280	-> byte code offset #0
/*     */     //   Java source line #281	-> byte code offset #8
/*     */     //   Java source line #283	-> byte code offset #26
/*     */     //   Java source line #291	-> byte code offset #47
/*     */     //   Java source line #293	-> byte code offset #51
/*     */     //   Java source line #296	-> byte code offset #66
/*     */     //   Java source line #297	-> byte code offset #74
/*     */     //   Java source line #298	-> byte code offset #92
/*     */     //   Java source line #281	-> byte code offset #125
/*     */     //   Java source line #298	-> byte code offset #130
/*     */     //   Java source line #299	-> byte code offset #166
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   7	49	0	newStream	ByteArrayOutputStream
/*     */     //   23	136	1	newPrintStream	PrintStream
/*     */     //   165	8	1	e	Exception
/*     */     //   25	125	2	localThrowable2	Throwable
/*     */     //   65	25	3	s	String
/*     */     //   125	4	3	localThrowable1	Throwable
/*     */     //   72	10	4	firstNewline	int
/*     */     //   107	4	6	x2	Throwable
/*     */     //   130	33	7	localObject	Object
/*     */     //   147	4	8	x2	Throwable
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   100	104	107	java/lang/Throwable
/*     */     //   26	92	125	java/lang/Throwable
/*     */     //   26	92	130	finally
/*     */     //   125	132	130	finally
/*     */     //   140	144	147	java/lang/Throwable
/*     */     //   8	122	165	java/lang/RuntimeException
/*     */     //   8	122	165	java/io/UnsupportedEncodingException
/*     */     //   125	165	165	java/lang/RuntimeException
/*     */     //   125	165	165	java/io/UnsupportedEncodingException
/*     */   }
/*     */   
/*     */   public static void runLeaderElection(String zkCsvList)
/*     */   {
/* 308 */     ZkClient zkClient = newZkClient(zkCsvList);
/*     */     try
/*     */     {
/* 311 */       log.info("Fetching partitions for leader election");
/* 312 */       Set<TopicAndPartition> partitions = ZkUtils.getAllPartitions(zkClient);
/* 313 */       log.info("Fetched [{}] partitions", Integer.valueOf(partitions.size()));
/*     */       
/* 315 */       log.info("Starting preferred leader election");
/* 316 */       PreferredReplicaLeaderElectionCommand cmd = new PreferredReplicaLeaderElectionCommand(zkClient, partitions);
/*     */       
/* 318 */       cmd.moveLeaderToPreferredReplica();
/* 319 */       log.info("Completed preferred leader election");
/*     */     } finally {
/* 321 */       closeQuietly(zkClient, log);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void createTopic(String zkCsvList, String topicName, int partitions, int replicationFactor)
/*     */   {
/* 333 */     createTopic(zkCsvList, topicName, partitions, replicationFactor, Optional.absent());
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
/*     */   public static void createTopic(String zkCsvList, String topicName, int partitions, int replicationFactor, Optional<Long> retentionTimeMillis)
/*     */   {
/* 346 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(zkCsvList), "zkCsvList can't be blank");
/* 347 */     Preconditions.checkArgument(!Strings.isNullOrEmpty(topicName), "topicName can't be blank");
/* 348 */     Preconditions.checkArgument(partitions > 0, "partitions must be greater than 0");
/* 349 */     Preconditions.checkArgument(replicationFactor > 0, "replicationFactor must be greater than 0");
/*     */     
/* 351 */     ZkClient zkClient = newZkClient(zkCsvList);
/*     */     try
/*     */     {
/* 354 */       Properties properties = new Properties();
/* 355 */       if (retentionTimeMillis.isPresent()) {
/* 356 */         Preconditions.checkArgument(((Long)retentionTimeMillis.get()).longValue() > 0L, "retentionTimeMillis must be greater than 0");
/*     */         
/* 358 */         properties.setProperty("retention.ms", ((Long)retentionTimeMillis.get()).toString());
/*     */       }
/*     */       
/* 361 */       AdminUtils.createTopic(zkClient, topicName, partitions, replicationFactor, properties);
/*     */     } catch (TopicExistsException e) {
/* 363 */       log.warn("Attempt to create topic [{}] with partitions [{}] and replication factor [{}] failed because the topic already exists", new Object[] { topicName, Integer.valueOf(partitions), Integer.valueOf(replicationFactor), e });
/*     */     }
/*     */     finally {
/* 366 */       closeQuietly(zkClient, log);
/*     */     }
/*     */   }
/*     */   
/*     */   static ZkClient newZkClient(String zkCsvList) {
/* 371 */     ZkClient zkClient = new ZkClient(zkCsvList, 30000, 30000);
/* 372 */     zkClient.setZkSerializer(ZKStringSerializer..MODULE$);
/* 373 */     return zkClient;
/*     */   }
/*     */   
/*     */   static void closeQuietly(ZkClient zkClient, Logger logger) {
/*     */     try {
/* 378 */       zkClient.close();
/*     */     } catch (ZkInterruptedException e) {
/* 380 */       logger.warn("Error occurred while closing the ZooKeeper client connection", e);
/*     */     }
/*     */   }
/*     */   
/*     */   static void stop() {
/* 385 */     ConcurrencyHelper.stop(SCALA_CLI_TASK_EXECUTOR, log);
/*     */   }
/*     */   
/*     */   public static class Report {
/* 389 */     void setAllPartitionsStatus(String allPartitionsStatus) { this.allPartitionsStatus = allPartitionsStatus; } String allPartitionsStatus = "N/A";
/* 390 */     public String getAllPartitionsStatus() { return this.allPartitionsStatus; }
/*     */     
/* 392 */     void setAllPartitionsException(Exception allPartitionsException) { this.allPartitionsException = allPartitionsException; }
/*     */     
/*     */     Exception allPartitionsException;
/* 395 */     void setUnderReplicatedPartitionsStatus(String underReplicatedPartitionsStatus) { this.underReplicatedPartitionsStatus = underReplicatedPartitionsStatus; } String underReplicatedPartitionsStatus = "N/A";
/* 396 */     public String getUnderReplicatedPartitionsStatus() { return this.underReplicatedPartitionsStatus; }
/*     */     
/* 398 */     void setUnderReplicatedPartitionsException(Exception underReplicatedPartitionsException) { this.underReplicatedPartitionsException = underReplicatedPartitionsException; }
/*     */     
/*     */     Exception underReplicatedPartitionsException;
/* 401 */     void setUnavailablePartitionsStatus(String unavailablePartitionsStatus) { this.unavailablePartitionsStatus = unavailablePartitionsStatus; } String unavailablePartitionsStatus = "N/A";
/* 402 */     public String getUnavailablePartitionsStatus() { return this.unavailablePartitionsStatus; }
/*     */     
/* 404 */     void setUnavailablePartitionsException(Exception unavailablePartitionsException) { this.unavailablePartitionsException = unavailablePartitionsException; }
/*     */     
/*     */     Exception unavailablePartitionsException;
/* 407 */     void setMovingPartitionsStatus(String movingPartitionsStatus) { this.movingPartitionsStatus = movingPartitionsStatus; } String movingPartitionsStatus = "N/A";
/* 408 */     public String getMovingPartitionsStatus() { return this.movingPartitionsStatus; }
/*     */     
/* 410 */     void setMovingPartitionsException(Exception movingPartitionsException) { this.movingPartitionsException = movingPartitionsException; }
/*     */     
/*     */     Exception movingPartitionsException;
/* 413 */     void setConsumerOffsetsStatus(String consumerOffsetsStatus) { this.consumerOffsetsStatus = consumerOffsetsStatus; } String consumerOffsetsStatus = "N/A";
/* 414 */     public String getConsumerOffsetsStatus() { return this.consumerOffsetsStatus; }
/*     */     
/* 416 */     void setConsumerOffsetsException(Exception consumerOffsetsException) { this.consumerOffsetsException = consumerOffsetsException; }
/*     */     
/*     */     Exception consumerOffsetsException;
/* 419 */     void setMetricsReport(String metricsReport) { this.metricsReport = metricsReport; } String metricsReport = "N/A";
/* 420 */     public String getMetricsReport() { return this.metricsReport; }
/*     */     
/* 422 */     void setMetricsException(Exception metricsException) { this.metricsException = metricsException; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     Exception metricsException;
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 433 */       RuntimeException otherExceptions = null;
/* 434 */       if (this.allPartitionsException != null) {
/* 435 */         otherExceptions = addException(otherExceptions, this.allPartitionsException);
/*     */       }
/* 437 */       if (this.underReplicatedPartitionsException != null) {
/* 438 */         otherExceptions = addException(otherExceptions, this.underReplicatedPartitionsException);
/*     */       }
/* 440 */       if (this.unavailablePartitionsException != null) {
/* 441 */         otherExceptions = addException(otherExceptions, this.unavailablePartitionsException);
/*     */       }
/* 443 */       if (this.movingPartitionsException != null) {
/* 444 */         otherExceptions = addException(otherExceptions, this.movingPartitionsException);
/*     */       }
/* 446 */       if (this.consumerOffsetsException != null) {
/* 447 */         otherExceptions = addException(otherExceptions, this.consumerOffsetsException);
/*     */       }
/* 449 */       if (this.metricsException != null) {
/* 450 */         otherExceptions = addException(otherExceptions, this.metricsException);
/*     */       }
/*     */       
/* 453 */       StringBuilder sb = new StringBuilder("Kafka status [\n").append("\n*All partitions:*\n").append(this.allPartitionsStatus).append("\n").append("\n*Under replicated partitions:*\n").append(this.underReplicatedPartitionsStatus).append("\n").append("\n*Unavailable partitions:*\n").append(this.unavailablePartitionsStatus).append("\n").append("\n*Partition assignments in progress:*\n").append(this.movingPartitionsStatus).append("\n").append("\n*Consumer offsets:*\n").append(this.consumerOffsetsStatus).append("\n").append("\n*Metrics:*\n").append(this.metricsReport).append("\n");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 461 */       if (otherExceptions != null) {
/* 462 */         sb.append("\n*Status report errors:*\n").append(Throwables.getStackTraceAsString(otherExceptions)).append("\n");
/*     */       }
/*     */       
/* 465 */       sb.append("]");
/*     */       
/* 467 */       return sb.toString();
/*     */     }
/*     */     
/*     */     private static RuntimeException addException(RuntimeException main, Exception sub) {
/* 471 */       if (main == null) {
/* 472 */         main = new RuntimeException("Error occurred while retrieving status report");
/*     */       }
/* 474 */       main.addSuppressed(sub);
/* 475 */       return main;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/util/KafkaTool.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */