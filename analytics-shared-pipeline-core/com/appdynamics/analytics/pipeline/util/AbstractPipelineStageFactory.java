/*    */ package com.appdynamics.analytics.pipeline.util;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageFactory;
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.common.framework.util.Module;
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.google.inject.binder.LinkedBindingBuilder;
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
/*    */ public abstract class AbstractPipelineStageFactory<IN, OUT, FACT_CFG, STG_CFG>
/*    */   extends Module<FACT_CFG>
/*    */   implements PipelineStageFactory<IN, OUT>
/*    */ {
/* 25 */   private static final Logger log = LoggerFactory.getLogger(AbstractPipelineStageFactory.class);
/*    */   
/*    */ 
/* 28 */   private final TypeToken<STG_CFG> stageConfigurationType = new TypeToken(getClass()) {};
/*    */   
/*    */   public final TypeToken<STG_CFG> getStageConfigurationType()
/*    */   {
/* 32 */     return this.stageConfigurationType;
/*    */   }
/*    */   
/*    */   public void configure()
/*    */   {
/* 37 */     super.configure();
/* 38 */     PipelineHelper.bind(binder(), getUri()).toInstance(this);
/*    */   }
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
/*    */   protected final STG_CFG extract(PipelineStageParameters<OUT> stageParameters)
/*    */   {
/* 52 */     if ((stageParameters instanceof SimplePipelineStageParameters)) {
/* 53 */       Object o = ((SimplePipelineStageParameters)stageParameters).getRawStageConfiguration();
/*    */       
/* 55 */       return (STG_CFG)Reader.readFrom(this.stageConfigurationType, o);
/*    */     }
/*    */     
/* 58 */     throw new ConfigurationException("Pipeline stage configuration could not be extracted");
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/util/AbstractPipelineStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */