/*    */ package com.appdynamics.common.io;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.nio.ByteBuffer;
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
/*    */ public class NioByteArrayOutputStream
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   public NioByteArrayOutputStream(int size)
/*    */   {
/* 24 */     super(size);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public synchronized ByteBuffer bufferView()
/*    */   {
/* 34 */     return ByteBuffer.wrap(this.buf, 0, this.count);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/NioByteArrayOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */