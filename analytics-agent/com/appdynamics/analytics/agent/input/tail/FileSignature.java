/*    */ package com.appdynamics.analytics.agent.input.tail;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.channels.FileChannel;
/*    */ import lombok.NonNull;
/*    */ import org.apache.commons.codec.digest.DigestUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileSignature
/*    */ {
/*    */   public static final int MAX_FILE_SIGNATURE_SIZE = 2048;
/*    */   @JsonProperty
/*    */   @NonNull
/*    */   final String signature;
/*    */   @JsonProperty
/*    */   final int size;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 30 */     if (o == this) return true; if (!(o instanceof FileSignature)) return false; FileSignature other = (FileSignature)o; if (!other.canEqual(this)) return false; Object this$signature = getSignature();Object other$signature = other.getSignature(); if (this$signature == null ? other$signature != null : !this$signature.equals(other$signature)) return false; return getSize() == other.getSize(); } public boolean canEqual(Object other) { return other instanceof FileSignature; } public int hashCode() { int PRIME = 31;int result = 1;Object $signature = getSignature();result = result * 31 + ($signature == null ? 0 : $signature.hashCode());result = result * 31 + getSize();return result;
/*    */   }
/*    */   
/*    */ 
/*    */   @NonNull
/*    */   public String getSignature()
/*    */   {
/* 37 */     return this.signature;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 41 */     return this.size;
/*    */   }
/*    */   
/*    */   @JsonIgnore
/*    */   public boolean isComplete() {
/* 46 */     return this.size == 2048;
/*    */   }
/*    */   
/*    */   @JsonCreator
/*    */   public FileSignature(@JsonProperty("signature") String signature, @JsonProperty("size") int size) {
/* 51 */     this.signature = signature;
/* 52 */     this.size = size;
/*    */   }
/*    */   
/*    */   public static FileSignature calculateFileSignature(FileChannel raf) throws IOException {
/* 56 */     return calculateFileSignature(raf, 0, 2048);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static FileSignature calculateFileSignature(FileChannel raf, int minSize, int maxSize)
/*    */     throws IOException
/*    */   {
/* 67 */     long length = raf.size();
/* 68 */     if (length < minSize) {
/* 69 */       throw new IOException(String.format("File not long enough to compute signature, size is %d.", new Object[] { Long.valueOf(length) }));
/*    */     }
/*    */     
/* 72 */     int size = (int)Math.min(raf.size(), maxSize);
/* 73 */     byte[] bytes = new byte[size];
/* 74 */     int signatureSize = raf.read(ByteBuffer.wrap(bytes));
/* 75 */     String signature = DigestUtils.md2Hex(bytes);
/* 76 */     if (signatureSize == -1) {
/* 77 */       signatureSize = size;
/*    */     }
/*    */     
/* 80 */     return new FileSignature(signature, signatureSize);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 84 */     return String.format("%s^%d", new Object[] { this.signature, Integer.valueOf(this.size) });
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/input/tail/FileSignature.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */