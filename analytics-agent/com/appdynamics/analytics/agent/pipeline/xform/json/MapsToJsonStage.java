/*    */ package com.appdynamics.analytics.agent.pipeline.xform.json;
/*    */ 
/*    */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*    */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*    */ import com.appdynamics.common.io.NioByteArrayOutputStream;
/*    */ import com.appdynamics.common.util.configuration.Reader;
/*    */ import com.fasterxml.jackson.core.JsonEncoding;
/*    */ import com.fasterxml.jackson.core.JsonFactory;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.google.common.base.Charsets;
/*    */ import com.google.common.base.Throwables;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapsToJsonStage
/*    */   extends AbstractPipelineStage<Map<Object, Object>, String>
/*    */ {
/*    */   final boolean clearMaps;
/*    */   final JsonFactory jsonFactory;
/*    */   final NioByteArrayOutputStream reusableBaos;
/*    */   
/*    */   MapsToJsonStage(PipelineStageParameters<String> parameters, boolean clearMaps)
/*    */   {
/* 33 */     super(parameters);
/* 34 */     this.clearMaps = clearMaps;
/* 35 */     this.jsonFactory = new JsonFactory(Reader.DEFAULT_JSON_MAPPER);
/* 36 */     this.reusableBaos = new NioByteArrayOutputStream(512);
/*    */   }
/*    */   
/*    */   ByteBuffer encodeToBB(Map<Object, Object> map) throws IOException {
/* 40 */     this.reusableBaos.reset();
/*    */     
/* 42 */     JsonGenerator jsonGenerator = this.jsonFactory.createGenerator(this.reusableBaos, JsonEncoding.UTF8);
/*    */     
/* 44 */     jsonGenerator.writeObject(map);
/* 45 */     if (this.clearMaps) {
/* 46 */       map.clear();
/*    */     }
/*    */     
/* 49 */     jsonGenerator.close();
/* 50 */     this.reusableBaos.close();
/*    */     
/* 52 */     return this.reusableBaos.bufferView();
/*    */   }
/*    */   
/*    */   String encodeToString(Map<Object, Object> map) throws IOException {
/* 56 */     ByteBuffer utf8Bytes = encodeToBB(map);
/*    */     
/* 58 */     CharBuffer charBuffer = Charsets.UTF_8.decode(utf8Bytes);
/* 59 */     return charBuffer.toString();
/*    */   }
/*    */   
/*    */   public void process(Map<Object, Object> input)
/*    */   {
/* 64 */     String json = null;
/*    */     try {
/* 66 */       json = encodeToString(input);
/*    */     } catch (IOException e) {
/* 68 */       Throwables.propagate(e);
/*    */     }
/* 70 */     invokeNext(json);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/json/MapsToJsonStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */