/*    */ package com.appdynamics.common.io.payload;
/*    */ 
/*    */ import com.google.common.hash.HashCode;
/*    */ import com.google.common.hash.HashFunction;
/*    */ import com.google.common.hash.Hasher;
/*    */ import com.google.common.hash.Hashing;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Bytes
/*    */ {
/*    */   final byte[] array;
/*    */   final int offset;
/*    */   final int length;
/*    */   
/* 20 */   public byte[] getArray() { return this.array; }
/* 21 */   public int getOffset() { return this.offset; }
/* 22 */   public int getLength() { return this.length; }
/*    */   
/*    */   public Bytes(byte[] array) {
/* 25 */     this(array, 0, array.length);
/*    */   }
/*    */   
/*    */   public Bytes(byte[] array, int offset, int length) {
/* 29 */     this.array = array;
/* 30 */     this.offset = offset;
/* 31 */     this.length = length;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String contentHash()
/*    */   {
/* 38 */     return contentHash(this.array, this.offset, this.length);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] getOrCopyArray()
/*    */   {
/* 46 */     return (this.offset == 0) && (this.length == this.array.length) ? this.array : Arrays.copyOfRange(this.array, this.offset, this.offset + this.length);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String contentHash(byte[] array, int offset, int length)
/*    */   {
/* 56 */     return Hashing.murmur3_32().newHasher().putBytes(array, offset, length).hash().toString();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/payload/Bytes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */