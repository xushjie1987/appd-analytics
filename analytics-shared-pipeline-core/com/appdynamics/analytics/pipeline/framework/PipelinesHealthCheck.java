/*    */ package com.appdynamics.analytics.pipeline.framework;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStage;
/*    */ import com.appdynamics.common.util.health.Countable;
/*    */ import com.appdynamics.common.util.health.SimpleHealthCheck;
/*    */ import com.codahale.metrics.health.HealthCheck.Result;
/*    */ import java.util.TreeSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PipelinesHealthCheck
/*    */   extends SimpleHealthCheck
/*    */ {
/*    */   private final Pipelines pipelines;
/*    */   
/*    */   PipelinesHealthCheck(String name, Pipelines pipelines)
/*    */   {
/* 22 */     super(name);
/* 23 */     this.pipelines = pipelines;
/*    */   }
/*    */   
/*    */   public HealthCheck.Result check()
/*    */   {
/* 28 */     int numNotRunning = 0;
/* 29 */     int numTotal = 0;
/* 30 */     TreeSet<Object> notRunningNames = new TreeSet();
/* 31 */     StringBuilder pipelineStageDetails = new StringBuilder();
/*    */     
/* 33 */     for (Pipeline<?> pipeline : this.pipelines.getAll()) {
/* 34 */       Pipeline.State state = pipeline.getState();
/* 35 */       if (isUnhealthy(state)) {
/* 36 */         numNotRunning++;
/* 37 */         notRunningNames.add(pipeline.getId());
/*    */       }
/*    */       
/*    */ 
/* 41 */       if (numTotal > 0) {
/* 42 */         pipelineStageDetails.append(",    ");
/*    */       }
/* 44 */       printStatus(pipeline, pipelineStageDetails);
/*    */       
/* 46 */       numTotal++;
/*    */     }
/*    */     
/* 49 */     if (numNotRunning == 0) {
/* 50 */       return HealthCheck.Result.healthy(String.format("[%d] pipeline(s). %s", new Object[] { Integer.valueOf(numTotal), pipelineStageDetails }));
/*    */     }
/* 52 */     return HealthCheck.Result.unhealthy(String.format("[%d] out of [%d] pipeline(s) are not running %s. %s", new Object[] { Integer.valueOf(numNotRunning), Integer.valueOf(numTotal), notRunningNames, pipelineStageDetails }));
/*    */   }
/*    */   
/*    */   public static boolean isUnhealthy(Pipeline.State state)
/*    */   {
/* 57 */     return (state == Pipeline.State.IDLE) || (state == Pipeline.State.STOPPED);
/*    */   }
/*    */   
/*    */   public static void printStatus(Pipeline<?> pipeline, StringBuilder destination) {
/* 61 */     int i = 0;
/* 62 */     destination.append(pipeline.getId()).append(": ");
/* 63 */     destination.append('[');
/* 64 */     for (PipelineStage pipelineStage : pipeline.getPipelineStages()) {
/* 65 */       if (i > 0) {
/* 66 */         destination.append(", ");
/*    */       }
/* 68 */       destination.append(pipelineStage.getClass().getSimpleName()).append(": ");
/* 69 */       if ((pipelineStage instanceof Countable)) {
/* 70 */         long count = ((Countable)pipelineStage).getCount();
/* 71 */         destination.append(count);
/*    */       } else {
/* 73 */         destination.append("N/A");
/*    */       }
/* 75 */       i++;
/*    */     }
/* 77 */     destination.append(']');
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/framework/PipelinesHealthCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */