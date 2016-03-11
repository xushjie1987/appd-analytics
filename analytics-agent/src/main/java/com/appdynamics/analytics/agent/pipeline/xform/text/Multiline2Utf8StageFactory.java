/*    */ package com.appdynamics.analytics.agent.pipeline.xform.text;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.CharacterCodingException;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ public class Multiline2Utf8StageFactory
/*    */   extends AbstractMultilineStageFactory<ByteBuffer>
/*    */ {
/*    */   Multiline2Utf8Stage newMultilineStage(PipelineStageParameters<ByteBuffer> parameters, MatchType matchType, String matchValue, MatchAction matchAction, int bufferSizeWarnAbove)
/*    */   {
/* 26 */     return new Multiline2Utf8Stage(parameters, matchType, matchValue, matchAction, bufferSizeWarnAbove);
/*    */   }
/*    */   
/*    */   static class Multiline2Utf8Stage extends AbstractMultilineStage<ByteBuffer>
/*    */   {
/*    */     Multiline2Utf8Stage(PipelineStageParameters<ByteBuffer> parameters, MatchType matchType, String matchValue, MatchAction matchAction, int bufferSizeWarnAbove) {
/* 32 */       super(matchType, matchValue, matchAction, "\n", bufferSizeWarnAbove);
/*    */     }
/*    */     
/*    */     ByteBuffer extractForNextStage(StringBuilder reusableBuilder)
/*    */     {
/* 37 */       CharBuffer charBuffer = CharBuffer.wrap(reusableBuilder);
/* 38 */       CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
/* 39 */       ByteBuffer output = null;
/*    */       try {
/* 41 */         output = encoder.encode(charBuffer);
/*    */       } catch (CharacterCodingException e) {
/* 43 */         throw new RuntimeException(e);
/*    */       }
/* 45 */       reusableBuilder.setLength(0);
/*    */       
/* 47 */       return output;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/text/Multiline2Utf8StageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */