/*    */ package com.appdynamics.common.framework.util;
/*    */ 
/*    */ import com.appdynamics.common.framework.AbstractApp;
/*    */ import com.appdynamics.common.framework.AbstractApp.LazyCommand;
/*    */ import com.appdynamics.common.framework.AppConfiguration;
/*    */ import com.google.common.base.Optional;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
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
/*    */ public class SimpleApp<C extends AppConfiguration>
/*    */   extends AbstractApp<C>
/*    */ {
/* 37 */   private static final Logger log = LoggerFactory.getLogger(SimpleApp.class);
/*    */   
/*    */ 
/*    */ 
/*    */   public SimpleApp(boolean embeddedMode)
/*    */   {
/* 43 */     super(embeddedMode);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public SimpleApp() {}
/*    */   
/*    */ 
/*    */ 
/*    */   protected final AbstractApp.LazyCommand makeCommand()
/*    */   {
/* 55 */     return new AbstractApp.LazyCommand("server", Optional.absent());
/*    */   }
/*    */   
/*    */   protected void beforeModuleLoading(C conf, Environment env)
/*    */   {
/* 60 */     FrameworkHelper.recordHttpServerPort(env);
/* 61 */     FrameworkHelper.createPidFile(conf.getName());
/*    */     
/* 63 */     FrameworkHelper.startJmxReporter(env);
/* 64 */     FrameworkHelper.addLifecycleLogger(conf, env, log);
/* 65 */     env.jersey().register(NoOpResource.class);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/SimpleApp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */