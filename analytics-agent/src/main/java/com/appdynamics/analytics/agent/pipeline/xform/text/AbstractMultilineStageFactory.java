/*    */ package com.appdynamics.analytics.agent.pipeline.xform.text;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStageFactory;
/*    */ import com.appdynamics.common.util.configuration.Parameters;
/*    */ import java.util.Arrays;
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
/*    */ abstract class AbstractMultilineStageFactory<O>
/*    */   extends AbstractPipelineStageFactory<CharSequence, O, Object, MultilineStageConfiguration>
/*    */ {
/*    */   public static final String LINE_SEPARATOR = "\n";
/*    */   
/*    */   public AbstractMultilineStage<O> create(PipelineStageParameters<O> parameters)
/*    */   {
/* 38 */     MultilineStageConfiguration configuration = (MultilineStageConfiguration)extract(parameters);
/*    */     
/* 40 */     MatchType matchType = null;
/* 41 */     String matchValue = configuration.getStartsWith();
/* 42 */     if (matchValue != null) {
/* 43 */       matchType = MatchType.startsWith;
/*    */     } else {
/* 45 */       matchValue = configuration.getRegex();
/* 46 */       if (matchValue != null) {
/* 47 */         matchType = MatchType.regex;
/*    */       }
/*    */     }
/* 50 */     if (matchType == null) {
/* 51 */       Parameters.asMandatoryString(null, "At least one of these properties have to be specified " + Arrays.asList(new MatchType[] { MatchType.startsWith, MatchType.regex }));
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 57 */     MatchAction matchAction = MatchAction.valueOf(configuration.getMatchAction().toUpperCase());
/*    */     
/* 59 */     return newMultilineStage(parameters, matchType, matchValue, matchAction, configuration.getBufferSizeWarnAboveChars());
/*    */   }
/*    */   
/*    */   abstract AbstractMultilineStage<O> newMultilineStage(PipelineStageParameters<O> paramPipelineStageParameters, MatchType paramMatchType, String paramString, MatchAction paramMatchAction, int paramInt);
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/text/AbstractMultilineStageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */