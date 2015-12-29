/*    */ package com.appdynamics.analytics.pipeline.http;
/*    */ 
/*    */ import com.appdynamics.common.io.codec.Utf8String;
/*    */ import com.appdynamics.common.io.codec.Utf8String.Decoder;
/*    */ import java.io.IOException;
/*    */ import javax.servlet.ServletInputStream;
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
/*    */ public class Utf8StringPostReceiver
/*    */   extends AbstractPostReceiver<String, PostReceiverConfiguration>
/*    */ {
/* 21 */   private static final ThreadLocal<Utf8String.Decoder> TLS_UTF8_DECODERS = new ThreadLocal()
/*    */   {
/*    */     protected Utf8String.Decoder initialValue() {
/* 24 */       return Utf8String.newDecoder();
/*    */     }
/*    */   };
/*    */   
/*    */   protected String process(ServletInputStream inputStream) throws IOException
/*    */   {
/* 30 */     Utf8String.Decoder decoder = (Utf8String.Decoder)TLS_UTF8_DECODERS.get();
/*    */     try {
/* 32 */       return decoder.decode(inputStream);
/*    */     } catch (Throwable t) {
/* 34 */       inputStream.close();
/* 35 */       throw t;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-pipeline-core.jar!/com/appdynamics/analytics/pipeline/http/Utf8StringPostReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */