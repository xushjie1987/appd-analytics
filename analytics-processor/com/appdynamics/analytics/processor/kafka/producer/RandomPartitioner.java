/*    */ package com.appdynamics.analytics.processor.kafka.producer;
/*    */ 
/*    */ import java.util.Random;
/*    */ import java.util.concurrent.ThreadLocalRandom;
/*    */ import kafka.producer.DefaultPartitioner;
/*    */ import kafka.utils.VerifiableProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RandomPartitioner
/*    */   extends DefaultPartitioner
/*    */ {
/*    */   private final Random random;
/*    */   
/*    */   public RandomPartitioner(VerifiableProperties props)
/*    */   {
/* 23 */     super(props);
/* 24 */     this.random = ThreadLocalRandom.current();
/*    */   }
/*    */   
/*    */   public int partition(Object key, int numPartitions)
/*    */   {
/* 29 */     return this.random.nextInt(numPartitions);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/producer/RandomPartitioner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */