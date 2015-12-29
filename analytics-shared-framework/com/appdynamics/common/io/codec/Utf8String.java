/*    */ package com.appdynamics.common.io.codec;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import org.apache.commons.io.Charsets;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.apache.commons.io.output.StringBuilderWriter;
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
/*    */ public abstract class Utf8String
/*    */ {
/*    */   public static Decoder newDecoder()
/*    */   {
/* 26 */     return new Decoder();
/*    */   }
/*    */   
/*    */   public static class Decoder {
/*    */     final char[] chars;
/*    */     final StringBuilderWriter writer;
/*    */     
/*    */     Decoder() {
/* 34 */       this.chars = new char['က'];
/* 35 */       this.writer = new StringBuilderWriter(1024);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     public String decode(InputStream inputStream)
/*    */       throws IOException
/*    */     {
/* 45 */       StringBuilder sb = this.writer.getBuilder();
/* 46 */       sb.setLength(0);
/*    */       
/*    */ 
/* 49 */       InputStreamReader in = new InputStreamReader(inputStream, Charsets.UTF_8);Throwable localThrowable2 = null;
/* 50 */       try { long numBytes = IOUtils.copyLarge(in, this.writer, this.chars);
/* 51 */         assert (numBytes >= 0L);
/* 52 */         this.writer.close();
/*    */       }
/*    */       catch (Throwable localThrowable1)
/*    */       {
/* 49 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*    */       }
/*    */       finally
/*    */       {
/* 53 */         if (in != null) if (localThrowable2 != null) try { in.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else in.close();
/*    */       }
/* 55 */       return sb.toString();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/codec/Utf8String.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */