/*    */ package com.appdynamics.analytics.agent.pipeline.xform.time;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*    */ import com.appdynamics.common.util.datetime.FastDateTimeParser;
/*    */ import com.appdynamics.common.util.datetime.TimeKeeper;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class DateTimeExtractorStage
/*    */   extends AbstractPipelineStage<Map<String, Object>, Map<String, Object>>
/*    */ {
/*    */   final String sourceField;
/*    */   final FastDateTimeParser optParser;
/*    */   final String destinationField;
/*    */   
/*    */   DateTimeExtractorStage(PipelineStageParameters<Map<String, Object>> parameters, String sourceField, FastDateTimeParser optParser, String destinationField)
/*    */   {
/* 43 */     super(parameters);
/* 44 */     this.sourceField = sourceField;
/* 45 */     this.optParser = optParser;
/* 46 */     this.destinationField = destinationField;
/*    */   }
/*    */   
/*    */   public void process(Map<String, Object> input)
/*    */   {
/* 51 */     String preParsedTimestamp = (String)input.get(this.destinationField);
/* 52 */     String source = preParsedTimestamp != null ? preParsedTimestamp : (String)input.get(this.sourceField);
/*    */     
/* 54 */     long timestamp = -1L;
/*    */     
/* 56 */     if ((source != null) && (this.optParser != null))
/*    */     {
/* 58 */       this.optParser.reset(source);
/*    */       
/* 60 */       timestamp = this.optParser.next();
/*    */     }
/*    */     
/* 63 */     if (timestamp == -1L)
/*    */     {
/* 65 */       String pickupTimestamp = (String)input.get("pickupTimestamp");
/*    */       
/* 67 */       if (pickupTimestamp == null) {
/* 68 */         timestamp = System.currentTimeMillis();
/*    */       }
/*    */       else {
/* 71 */         input.put(this.destinationField, pickupTimestamp);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 76 */     if (timestamp != -1L) {
/* 77 */       String datetimeUtc = TimeKeeper.utcTimeIso8601(timestamp);
/* 78 */       input.put(this.destinationField, datetimeUtc);
/*    */     }
/*    */     
/* 81 */     invokeNext(input);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/time/DateTimeExtractorStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */