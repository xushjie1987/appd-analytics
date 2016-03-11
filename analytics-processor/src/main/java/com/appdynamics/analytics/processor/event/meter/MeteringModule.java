/*    */ package com.appdynamics.analytics.processor.event.meter;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.account.AccountManager;
/*    */ import com.appdynamics.analytics.processor.elasticsearch.node.provider.ClientProvider;
/*    */ import com.appdynamics.analytics.processor.event.EventService;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import com.appdynamics.common.util.lifecycle.LifecycleInjector;
/*    */ import com.google.inject.Inject;
/*    */ import com.google.inject.Provides;
/*    */ import com.google.inject.Singleton;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MeteringModule
/*    */   extends Module<MeteringModuleConfiguration>
/*    */ {
/* 25 */   private static final Logger log = LoggerFactory.getLogger(MeteringModule.class);
/*    */   
/*    */   @Inject
/*    */   private volatile MetersManager metersManager;
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   TimeKeeper makeTimeKeeper()
/*    */   {
/* 34 */     return new TimeKeeper();
/*    */   }
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   MetersStore makeMetersStore(ClientProvider clientProvider, AccountManager accountManager) {
/* 40 */     return new MetersStore(clientProvider, accountManager, ((MeteringModuleConfiguration)getConfiguration()).getUsageQueryTimeout().toMilliseconds());
/*    */   }
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   Meters makeMeters(TimeKeeper timeKeeper, MetersStore metersStore, EventService eventService)
/*    */   {
/* 47 */     log.info("Metering is [{}]", ((MeteringModuleConfiguration)getConfiguration()).isEnabled() ? "enabled" : "disabled");
/* 48 */     if (((MeteringModuleConfiguration)getConfiguration()).isEnabled()) {
/* 49 */       return new DefaultMeters(timeKeeper, metersStore, eventService, (MeteringModuleConfiguration)getConfiguration());
/*    */     }
/* 51 */     return new UnlimitedMeters();
/*    */   }
/*    */   
/*    */   @Provides
/*    */   @Singleton
/*    */   MetersManager makeMetersManager(LifecycleInjector lifecycleInjector, TimeKeeper timeKeeper, Meters meters, MetersStore metersStore)
/*    */   {
/* 58 */     MetersManager metersManager = new MetersManager(meters, metersStore, timeKeeper);
/*    */     
/* 60 */     return ((MeteringModuleConfiguration)getConfiguration()).isEnabled() ? (MetersManager)lifecycleInjector.inject(metersManager) : metersManager;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/meter/MeteringModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */