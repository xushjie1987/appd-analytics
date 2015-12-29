/*    */ package com.appdynamics.analytics.processor.kafka.producer;
/*    */ 
/*    */ import com.google.common.base.Supplier;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Properties;
/*    */ import java.util.concurrent.ThreadLocalRandom;
/*    */ import javax.annotation.PreDestroy;
/*    */ import kafka.javaapi.producer.Producer;
/*    */ import kafka.producer.ProducerConfig;
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
/*    */ public class KafkaProducerSupplier
/*    */   implements Supplier<Producer<byte[], byte[]>>
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(KafkaProducerSupplier.class);
/*    */   
/*    */   private List<Producer<byte[], byte[]>> producers;
/*    */   
/*    */ 
/*    */   public KafkaProducerSupplier(Properties props)
/*    */   {
/* 34 */     if (!props.containsKey("num.producers")) {
/* 35 */       throw new IllegalArgumentException("Please specify the number of producers in [num.producers ] property");
/*    */     }
/*    */     
/*    */ 
/* 39 */     int numProducers = Integer.parseInt((String)props.get("num.producers"));
/* 40 */     if (numProducers <= 0) {
/* 41 */       throw new IllegalArgumentException("Number of kafka producers specified by property [num.producers] has to be greater than zero");
/*    */     }
/*    */     
/*    */ 
/* 45 */     if (numProducers > 8) {
/* 46 */       log.warn("Currently number of producers is limited to a maximum value of [{}]", Integer.valueOf(8));
/*    */       
/* 48 */       numProducers = 8;
/*    */     }
/* 50 */     props.remove("num.producers");
/* 51 */     this.producers = new ArrayList(numProducers);
/* 52 */     ProducerConfig config = new ProducerConfig(props);
/* 53 */     for (int i = 0; i < numProducers; i++) {
/* 54 */       this.producers.add(new Producer(config));
/*    */     }
/* 56 */     log.info("Initialised Kafka producer pool with [{}] producers", Integer.valueOf(this.producers.size()));
/*    */   }
/*    */   
/*    */   List<Producer<byte[], byte[]>> getProducers() {
/* 60 */     return this.producers;
/*    */   }
/*    */   
/*    */   public Producer<byte[], byte[]> get()
/*    */   {
/* 65 */     ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
/* 66 */     int producerId = threadLocalRandom.nextInt(0, this.producers.size());
/* 67 */     return (Producer)this.producers.get(producerId);
/*    */   }
/*    */   
/*    */   @PreDestroy
/*    */   void stop()
/*    */   {
/* 73 */     if (this.producers != null) {
/* 74 */       for (Producer<byte[], byte[]> producer : this.producers) {
/* 75 */         producer.close();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/producer/KafkaProducerSupplier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */