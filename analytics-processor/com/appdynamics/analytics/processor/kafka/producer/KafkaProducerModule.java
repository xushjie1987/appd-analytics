/*    */ package com.appdynamics.analytics.processor.kafka.producer;
/*    */ 
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
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
/*    */ public class KafkaProducerModule
/*    */   extends Module<Map<String, String>>
/*    */ {
/* 24 */   private static final Logger log = LoggerFactory.getLogger(KafkaProducerModule.class);
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   KafkaProducerSupplier makeProducers(LifecycleInjector lifecycleInjector)
/*    */   {
/* 30 */     Properties props = new Properties();
/* 31 */     props.putAll((Map)getConfiguration());
/* 32 */     KafkaProducerSupplier supplier = new KafkaProducerSupplier(props);
/* 33 */     supplier = (KafkaProducerSupplier)lifecycleInjector.inject(supplier);
/*    */     
/* 35 */     return supplier;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/kafka/producer/KafkaProducerModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */