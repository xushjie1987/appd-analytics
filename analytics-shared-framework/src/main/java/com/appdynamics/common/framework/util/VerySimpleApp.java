/*    */ package com.appdynamics.common.framework.util;
/*    */ 
/*    */ import com.appdynamics.common.framework.AbstractApp;
/*    */ import com.appdynamics.common.framework.AbstractApp.LazyCommand;
/*    */ import com.appdynamics.common.framework.AppConfiguration;
/*    */ import com.google.common.base.Optional;
/*    */ import com.google.inject.Injector;
/*    */ import io.dropwizard.Application;
/*    */ import io.dropwizard.cli.EnvironmentCommand;
/*    */ import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import net.sourceforge.argparse4j.inf.Namespace;
/*    */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
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
/*    */ public abstract class VerySimpleApp<C extends AppConfiguration>
/*    */   extends AbstractApp<C>
/*    */ {
/* 38 */   private static final Logger log = LoggerFactory.getLogger(VerySimpleApp.class);
/*    */   private final ContainerLifeCycle manualLifeCycleProducer;
/*    */   private Injector injector;
/*    */   
/*    */   public VerySimpleApp()
/*    */   {
/* 44 */     this.manualLifeCycleProducer = new ContainerLifeCycle();
/*    */   }
/*    */   
/*    */   protected final AbstractApp.LazyCommand makeCommand()
/*    */   {
/* 49 */     String name = "not-server";
/* 50 */     return new AbstractApp.LazyCommand(name, Optional.of(new SynchronousCommand(this, name, "Application")));
/*    */   }
/*    */   
/*    */ 
/*    */   protected final void beforeModuleLoading(C conf, Environment env)
/*    */   {
/* 56 */     FrameworkHelper.createPidFile(conf.getName());
/*    */     
/* 58 */     FrameworkHelper.startJmxReporter(env);
/* 59 */     FrameworkHelper.addLifecycleLogger(conf, env, log);
/* 60 */     env.lifecycle().attach(this.manualLifeCycleProducer);
/*    */   }
/*    */   
/*    */   protected final void afterModuleLoading(C conf, Environment env, Injector injector)
/*    */   {
/* 65 */     this.injector = injector;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract void afterAppReady(C paramC, Environment paramEnvironment, Injector paramInjector);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private class SynchronousCommand
/*    */     extends EnvironmentCommand<C>
/*    */   {
/*    */     SynchronousCommand(String application, String name)
/*    */     {
/* 85 */       super(name, description);
/*    */     }
/*    */     
/*    */     protected void run(Environment environment, Namespace namespace, C configuration)
/*    */     {
/*    */       try {
/* 91 */         VerySimpleApp.this.manualLifeCycleProducer.start();
/* 92 */         VerySimpleApp.this.afterAppReady(configuration, environment, VerySimpleApp.this.injector);
/* 93 */         VerySimpleApp.this.manualLifeCycleProducer.stop();
/*    */       } catch (Throwable t) {
/* 95 */         VerySimpleApp.log.error("Error occurred", t);
/*    */       } finally {
/* 97 */         cleanup();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/VerySimpleApp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */