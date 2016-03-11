/*     */ package com.appdynamics.analytics.pipeline.http;
/*     */ 
/*     */ import com.appdynamics.analytics.queue.Queues;
/*     */ import com.appdynamics.common.framework.util.Module;
/*     */ import com.appdynamics.common.util.health.ConsolidatedHealthCheck;
/*     */ import com.appdynamics.common.util.health.MeteredHealthCheck;
/*     */ import com.codahale.metrics.Meter;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.inject.Inject;
/*     */ import io.dropwizard.jetty.setup.ServletEnvironment;
/*     */ import io.dropwizard.setup.Environment;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ import javax.servlet.ServletInputStream;
/*     */ import javax.servlet.ServletRegistration.Dynamic;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ public abstract class AbstractPostReceiver<OUT, CFG extends PostReceiverConfiguration>
/*     */   extends Module<CFG>
/*     */ {
/*  37 */   private static final Logger log = LoggerFactory.getLogger(AbstractPostReceiver.class);
/*     */   
/*     */   @Inject
/*     */   private volatile Environment environment;
/*     */   
/*     */   @Inject
/*     */   protected volatile Queues queues;
/*     */   
/*     */   @Inject
/*     */   private volatile ConsolidatedHealthCheck healthCheck;
/*     */   
/*     */   private volatile boolean stopCalled;
/*     */   
/*     */   private BlockingQueue<OUT> dispatchQueue;
/*     */   private String urlPattern;
/*     */   private long timeoutSecs;
/*     */   private Meter meterSuccess;
/*     */   private Meter meterTimeout;
/*     */   private Meter meterError;
/*     */   
/*     */   @PostConstruct
/*     */   public void onStart()
/*     */   {
/*  60 */     PostReceiverConfiguration cfg = (PostReceiverConfiguration)getConfiguration();
/*  61 */     this.dispatchQueue = this.queues.findOrSetupQueue(cfg);
/*  62 */     this.urlPattern = cfg.getUrlPattern();
/*  63 */     this.timeoutSecs = cfg.getAckTimeoutSeconds();
/*     */     
/*  65 */     String name = getUri() + "[" + this.urlPattern + "]";
/*     */     
/*  67 */     MeteredHealthCheck meteredHealthCheck = new MeteredHealthCheck(name, this.environment);
/*  68 */     this.meterSuccess = meteredHealthCheck.getMeterSuccess();
/*  69 */     this.meterTimeout = meteredHealthCheck.getMeterTimeout();
/*  70 */     this.meterError = meteredHealthCheck.getMeterError();
/*     */     
/*     */ 
/*  73 */     AbstractPostReceiver<OUT, CFG>.StreamRelayServlet servlet = new StreamRelayServlet();
/*  74 */     ServletRegistration.Dynamic servletBuilder = this.environment.servlets().addServlet(name, servlet);
/*  75 */     servletBuilder.addMapping(new String[] { this.urlPattern });
/*  76 */     this.healthCheck.register(meteredHealthCheck);
/*     */     
/*  78 */     log.debug("[{}] will receive messages on [{}] and forward them to queue [{}]", new Object[] { getUri(), this.urlPattern, cfg.getQueueName() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract OUT process(ServletInputStream paramServletInputStream)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   @PreDestroy
/*     */   void onStop()
/*     */   {
/*  91 */     this.stopCalled = true;
/*     */   }
/*     */   
/*     */ 
/*     */   @ThreadSafe
/*     */   class StreamRelayServlet
/*     */     extends HttpServlet
/*     */   {
/*     */     StreamRelayServlet() {}
/*     */     
/*     */ 
/*     */     protected void doPost(HttpServletRequest request, HttpServletResponse response)
/*     */       throws IOException
/*     */     {
/* 105 */       ServletInputStream inputStream = request.getInputStream();
/*     */       try
/*     */       {
/* 108 */         OUT output = AbstractPostReceiver.this.process(inputStream);
/*     */         
/* 110 */         boolean success = AbstractPostReceiver.this.dispatchQueue.offer(output, AbstractPostReceiver.this.timeoutSecs, TimeUnit.SECONDS);
/* 111 */         if (success) {
/* 112 */           response.setStatus(202);
/* 113 */           AbstractPostReceiver.this.meterSuccess.mark();
/*     */         } else {
/* 115 */           AbstractPostReceiver.this.meterTimeout.mark();
/* 116 */           String msg = "Request could not be processed as the input queue is full";
/* 117 */           AbstractPostReceiver.log.error(msg);
/* 118 */           response.sendError(408, msg);
/*     */         }
/*     */       } catch (Exception e) {
/* 121 */         String msg = "Error occurred while processing message received over [" + AbstractPostReceiver.this.urlPattern + "]";
/* 122 */         if ((AbstractPostReceiver.this.stopCalled) && ((e instanceof InterruptedException))) {
/* 123 */           AbstractPostReceiver.log.debug(msg + ". Service is shutting down", e);
/* 124 */           response.setStatus(503);
/*     */         } else {
/* 126 */           AbstractPostReceiver.this.meterError.mark();
/*     */           
/* 128 */           AbstractPostReceiver.log.error(msg, e);
/* 129 */           response.sendError(500, Throwables.getRootCause(e).getMessage());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/http/AbstractPostReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */