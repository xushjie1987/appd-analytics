/*     */ package com.appdynamics.analytics.processor.tool.executor.steps;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionContext;
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionStep;
/*     */ import com.appdynamics.analytics.processor.tool.executor.ExecutionStepDeserializer;
/*     */ import com.appdynamics.analytics.processor.tool.executor.Executor;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.BulkStepExecutionResponse;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.ExecutionResponse;
/*     */ import com.appdynamics.analytics.processor.tool.executor.response.FailedResponse;
/*     */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*     */ import com.appdynamics.common.util.concurrent.ConcurrencyHelper;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StepIterator
/*     */   extends AbstractExecutionStep
/*     */ {
/*  39 */   private static final Logger log = LoggerFactory.getLogger(StepIterator.class);
/*     */   public static final String ITEM = "item";
/*     */   public static final String STEP_COUNTER = "stepCounter";
/*     */   public static final String TOTAL_STEPS = "totalSteps";
/*     */   private final Properties properties;
/*     */   private ExecutorService executorService;
/*     */   
/*     */   public static class Properties { @ConstructorProperties({"items", "stepList", "inParallel"})
/*  47 */     public Properties(List<Map<String, Object>> items, List<ExecutionStep> stepList, boolean inParallel) { this.items = items;this.stepList = stepList;this.inParallel = inParallel;
/*     */     }
/*     */     
/*  50 */     public void setItems(List<Map<String, Object>> items) { this.items = items; }
/*     */     
/*     */     List<Map<String, Object>> items;
/*  53 */     public List<ExecutionStep> getStepList() { return this.stepList; } @JsonDeserialize(contentUsing=ExecutionStepDeserializer.class)
/*  53 */     List<ExecutionStep> stepList = new ArrayList();
/*  54 */     public void setStepList(List<ExecutionStep> stepList) { this.stepList = stepList; }
/*     */     
/*     */ 
/*     */ 
/*  58 */     public boolean isInParallel() { return this.inParallel; } boolean inParallel = true;
/*  59 */     public void setInParallel(boolean inParallel) { this.inParallel = inParallel; }
/*     */     
/*     */     public Properties copy()
/*     */     {
/*  63 */       return new Properties(Lists.newArrayList(this.items), Lists.newArrayList(this.stepList), this.inParallel);
/*     */     }
/*     */     
/*     */ 
/*     */     public Properties() {}
/*     */   }
/*     */   
/*     */   public StepIterator(Properties properties)
/*     */   {
/*  72 */     Preconditions.checkArgument((properties != null) && (properties.stepList != null) && (!properties.stepList.isEmpty()) && (properties.items != null) && (!properties.items.isEmpty()));
/*     */     
/*     */ 
/*  75 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   public ExecutionStep copy()
/*     */   {
/*  80 */     StepIterator step = new StepIterator(this.properties.copy());
/*  81 */     step.setName(getName());
/*  82 */     return step;
/*     */   }
/*     */   
/*     */   public ExecutionResponse executeStep(final ExecutionContext executionContext)
/*     */   {
/*  87 */     final BulkStepExecutionResponse responses = new BulkStepExecutionResponse();
/*  88 */     List<Future<Void>> futures = new ArrayList();
/*  89 */     for (final Map<String, Object> item : this.properties.items) {
/*  90 */       futures.add(getExecutorService().submit(new Callable()
/*     */       {
/*     */         public Void call() {
/*     */           try {
/*  94 */             StepIterator.log.info("Executing [" + StepIterator.this.properties.stepList.size() + "] steps");
/*  95 */             i = 0;
/*  96 */             wrappedExecutionContext = new StepIterator.WrappedExecutionContext(executionContext);
/*     */             
/*  98 */             wrappedExecutionContext.getProperties().put("item", item);
/*  99 */             wrappedExecutionContext.getProperties().put("totalSteps", Integer.valueOf(StepIterator.this.properties.stepList.size()));
/* 100 */             for (ExecutionStep step : StepIterator.this.properties.stepList) {
/* 101 */               StepIterator.log.info("Execution step: " + step.getName());
/* 102 */               wrappedExecutionContext.getProperties().put("stepCounter", Integer.valueOf(i++));
/* 103 */               ExecutionStep copy = step.copy();
/* 104 */               Executor.filterStepProperties(copy, wrappedExecutionContext);
/*     */               
/* 106 */               ExecutionResponse response = copy.executeStep(wrappedExecutionContext);
/* 107 */               StepIterator.log.info("Response from step [" + step.getName() + "]: " + response);
/* 108 */               responses.add(response);
/*     */             } } catch (RestException e) { int i;
/*     */             StepIterator.WrappedExecutionContext wrappedExecutionContext;
/* 111 */             responses.add(new FailedResponse(e.getStatusCode(), e.getCode() + "[" + e.getMessage() + "]:\n" + e.getDeveloperMessage()));
/*     */           }
/*     */           catch (Exception e) {
/* 114 */             responses.add(new FailedResponse(1, e.getMessage() + ":\n" + Throwables.getStackTraceAsString(e)));
/*     */           }
/*     */           
/* 117 */           return null;
/*     */         }
/*     */       }));
/*     */     }
/*     */     
/* 122 */     log.info("Waiting for steps to execute");
/* 123 */     for (Future<Void> future : futures) {
/*     */       try {
/* 125 */         future.get();
/*     */       } catch (InterruptedException e) {
/* 127 */         Thread.currentThread().interrupt();
/*     */       } catch (ExecutionException e) {
/* 129 */         throw Throwables.propagate(e);
/*     */       }
/*     */     }
/* 132 */     log.info("Done waiting for all steps");
/* 133 */     ConcurrencyHelper.stop(getExecutorService(), log);
/* 134 */     return responses;
/*     */   }
/*     */   
/*     */   private ExecutorService getExecutorService() {
/* 138 */     if (this.executorService == null) {
/* 139 */       int numberOfThreads = 1;
/* 140 */       if (this.properties.inParallel) {
/* 141 */         numberOfThreads = this.properties.items.size();
/*     */       }
/* 143 */       this.executorService = Executors.newFixedThreadPool(numberOfThreads);
/*     */     }
/* 145 */     return this.executorService; }
/*     */   
/*     */   private static class WrappedExecutionContext extends ExecutionContext { private final ExecutionContext executionContext;
/*     */     
/* 149 */     public Map<String, Object> getProperties() { return this.executionContext.getProperties(); } public void setProperties(Map<String, Object> properties) { this.executionContext.setProperties(properties); } public CloseableHttpClient getHttpClient() { return this.executionContext.getHttpClient(); } public void setHttpClient(CloseableHttpClient httpClient) { this.executionContext.setHttpClient(httpClient); }
/*     */     
/*     */ 
/*     */     public WrappedExecutionContext(ExecutionContext executionContext)
/*     */     {
/* 154 */       Map<String, Object> props = Maps.newHashMap();
/* 155 */       if (executionContext.getProperties() != null) {
/* 156 */         props.putAll(executionContext.getProperties());
/*     */       }
/*     */       
/* 159 */       this.executionContext = new ExecutionContext();
/* 160 */       if (executionContext.getHttpClient() != null) {
/* 161 */         this.executionContext.setHttpClient(executionContext.getHttpClient());
/*     */       }
/* 163 */       this.executionContext.setProperties(props);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/steps/StepIterator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */