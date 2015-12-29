/*     */ package com.appdynamics.common.util.event;
/*     */ 
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.eventbus.AsyncEventBus;
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import com.google.common.eventbus.SubscriberExceptionContext;
/*     */ import com.google.common.eventbus.SubscriberExceptionHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import javax.annotation.PreDestroy;
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
/*     */ public class EventBuses
/*     */ {
/*  35 */   private static final Logger log = LoggerFactory.getLogger(EventBuses.class);
/*     */   public static final String NAME_DEFAULT_EVENT_BUS = "default-event-bus";
/*     */   final Map<String, EventBus> eventBuses;
/*     */   final List<ExecutorService> executorServices;
/*     */   
/*     */   EventBuses(List<EventBusConfiguration> busConfigurations)
/*     */   {
/*  42 */     this.eventBuses = new HashMap();
/*  43 */     this.executorServices = new ArrayList();
/*     */     
/*  45 */     for (EventBusConfiguration bc : busConfigurations) {
/*  46 */       EventBus eventBus = null;
/*  47 */       if (bc.isAsync()) {
/*  48 */         String poolName = "async-event-bus-" + bc.getName() + "-%d";
/*  49 */         ExecutorService es = Executors.newSingleThreadExecutor(ConcurrencyHelper.newDaemonThreadFactory(poolName));
/*  50 */         eventBus = new AsyncEventBus(es, new SubscriptionHandlingLogger(bc.getName()));
/*  51 */         this.executorServices.add(es);
/*     */       } else {
/*  53 */         eventBus = new EventBus(new SubscriptionHandlingLogger(bc.getName()));
/*     */       }
/*     */       
/*  56 */       EventBus oldBus = (EventBus)this.eventBuses.put(bc.getName(), eventBus);
/*  57 */       if (oldBus != null) {
/*  58 */         throw new ConfigurationException("There is more than one channel configured with the same name [" + bc.getName() + "]");
/*     */       }
/*     */       
/*  61 */       log.info("Started event bus [{}]", bc.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   EventBus findEventBus(String eventBusName) {
/*  66 */     EventBus eventBus = (EventBus)this.eventBuses.get(eventBusName);
/*  67 */     Preconditions.checkArgument(eventBus != null, "The event bus [" + eventBusName + "] does not exist");
/*  68 */     return eventBus;
/*     */   }
/*     */   
/*     */   public void registerListener(String eventBusName, Object listener) {
/*  72 */     EventBus eventBus = findEventBus(eventBusName);
/*  73 */     eventBus.register(listener);
/*  74 */     log.debug("Registered [{}] with event bus [{}]", listener, eventBusName);
/*     */   }
/*     */   
/*     */   public void postEvent(String eventBusName, Object event) {
/*  78 */     EventBus eventBus = findEventBus(eventBusName);
/*  79 */     log.debug("Posting event [{}] to event bus [{}]", event, eventBusName);
/*  80 */     eventBus.post(event);
/*     */   }
/*     */   
/*     */   public void unregisterListener(String eventBusName, Object listener) {
/*  84 */     EventBus eventBus = findEventBus(eventBusName);
/*  85 */     eventBus.unregister(listener);
/*  86 */     log.debug("Unregistered [{}] from event bus [{}]", listener, eventBusName);
/*     */   }
/*     */   
/*     */   @PreDestroy
/*     */   void onStop() {
/*  91 */     log.debug("Stopping");
/*  92 */     this.eventBuses.clear();
/*  93 */     for (ExecutorService executorService : this.executorServices) {
/*  94 */       ConcurrencyHelper.stop(executorService, 1, log);
/*     */     }
/*  96 */     log.info("Stopped");
/*     */   }
/*     */   
/*     */   static class SubscriptionHandlingLogger implements SubscriberExceptionHandler {
/*     */     final Logger logger;
/*     */     
/*     */     SubscriptionHandlingLogger(String name) {
/* 103 */       this.logger = LoggerFactory.getLogger(EventBus.class.getSimpleName() + "-" + name);
/*     */     }
/*     */     
/*     */     public void handleException(Throwable throwable, SubscriberExceptionContext context)
/*     */     {
/* 108 */       this.logger.error("An error occurred while dispatching event [" + context.getSubscriber() + "] to [" + context.getSubscriberMethod() + "]", throwable);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/event/EventBuses.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */