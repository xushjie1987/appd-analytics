/*    */ package com.appdynamics.analytics.agent.pipeline.xform.text;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
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
/*    */ public class Multiline2CharsStageFactory
/*    */   extends AbstractMultilineStageFactory<CharSequence>
/*    */ {
/*    */   Multiline2CharsStage newMultilineStage(PipelineStageParameters<CharSequence> parameters, MatchType matchType, String matchValue, MatchAction matchAction, int bufferSizeWarnAbove)
/*    */   {
/* 20 */     return new Multiline2CharsStage(parameters, matchType, matchValue, matchAction, bufferSizeWarnAbove);
/*    */   }
/*    */   
/*    */   static class Multiline2CharsStage extends AbstractMultilineStage<CharSequence>
/*    */   {
/*    */     Multiline2CharsStage(PipelineStageParameters<CharSequence> parameters, MatchType matchType, String matchValue, MatchAction matchAction, int bufferSizeWarnAbove) {
/* 26 */       super(matchType, matchValue, matchAction, "\n", bufferSizeWarnAbove);
/*    */     }
/*    */     
/*    */     CharSequence extractForNextStage(StringBuilder reusableBuilder)
/*    */     {
/* 31 */       String output = reusableBuilder.toString();
/* 32 */       reusableBuilder.setLength(0);
/*    */       
/* 34 */       return output;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/text/Multiline2CharsStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */