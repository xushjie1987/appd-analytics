/*    */ package com.appdynamics.analytics.agent.pipeline.xform.time;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.datetime.FastDateTimeParser;
/*    */ import com.google.inject.Inject;
/*    */ import io.dropwizard.jersey.setup.JerseyEnvironment;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateTimeExtractorStageFactory
/*    */   extends AbstractPipelineStageFactory<Map<String, Object>, Map<String, Object>, DateTimeExtractorConfiguration, DateTimeExtractorConfiguration>
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(DateTimeExtractorStageFactory.class);
/*    */   
/*    */ 
/*    */ 
/*    */   @Inject
/*    */   public void start(Environment environment)
/*    */   {
/* 30 */     DateTimeExtractorStageResource dateTimeExtractorStageResource = new DateTimeExtractorStageResource(this);
/* 31 */     environment.jersey().register(dateTimeExtractorStageResource);
/*    */   }
/*    */   
/*    */   public DateTimeExtractorStage create(PipelineStageParameters<Map<String, Object>> parameters)
/*    */   {
/* 36 */     DateTimeExtractorConfiguration factoryConfig = (DateTimeExtractorConfiguration)getConfiguration();
/* 37 */     DateTimeExtractorConfiguration stageConfig = null;
/*    */     try {
/* 39 */       stageConfig = (DateTimeExtractorConfiguration)extract(parameters);
/*    */     } catch (ConfigurationException e) {
/* 41 */       log.trace("No configuration provided for [" + getUri() + "] stage in [" + parameters.getPipelineId() + "]", e);
/*    */       
/* 43 */       stageConfig = new DateTimeExtractorConfiguration();
/*    */     }
/*    */     
/* 46 */     FastDateTimeParser parser = stageConfig.mergeAndValidate(factoryConfig);
/* 47 */     String sourceField = stageConfig.getSource();
/* 48 */     String destinationField = stageConfig.getDestination();
/*    */     
/* 50 */     return new DateTimeExtractorStage(parameters, sourceField, parser, destinationField);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/time/DateTimeExtractorStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */