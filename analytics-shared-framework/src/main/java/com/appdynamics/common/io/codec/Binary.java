/*     */ package com.appdynamics.common.io.codec;
/*     */ 
/*     */ import com.appdynamics.common.io.NioByteArrayOutputStream;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Binary
/*     */ {
/*     */   public static Encoder newEncoder()
/*     */   {
/*  50 */     return new Encoder();
/*     */   }
/*     */   
/*     */   public static Decoder newDecoder() {
/*  54 */     return new Decoder();
/*     */   }
/*     */   
/*     */   public static class Encoder {
/*     */     private final ByteBuffer reusableBBForInt;
/*     */     private final IntBuffer reusableIB;
/*     */     
/*     */     Encoder() {
/*  62 */       this.reusableBBForInt = ByteBuffer.allocate(4);
/*  63 */       this.reusableIB = this.reusableBBForInt.asIntBuffer();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public long encodedLength(List<ByteBuffer> sourceParts)
/*     */       throws IOException
/*     */     {
/*  74 */       if (sourceParts.size() == 0) {
/*  75 */         throw new IOException("There is no content to be encoded");
/*     */       }
/*     */       
/*     */ 
/*  79 */       long length = 4L;
/*     */       
/*  81 */       int numBuffers = sourceParts.size();
/*  82 */       for (int i = 0; i < numBuffers; i++)
/*     */       {
/*  84 */         length += 4L;
/*     */         
/*  86 */         ByteBuffer bb = (ByteBuffer)sourceParts.get(i);
/*  87 */         int remaining = bb.remaining();
/*  88 */         if (remaining <= 0) {
/*  89 */           throw new IOException("Part at position [" + i + "] has no content to be encoded");
/*     */         }
/*  91 */         length += remaining;
/*     */       }
/*     */       
/*  94 */       return length;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public long encode(List<ByteBuffer> sourceParts, WritableByteChannel destination)
/*     */       throws IOException
/*     */     {
/* 110 */       if (sourceParts.size() == 0) {
/* 111 */         throw new IOException("There is no content to be encoded");
/*     */       }
/*     */       
/* 114 */       long writeCount = 0L;
/*     */       
/*     */ 
/* 117 */       int numBuffers = sourceParts.size();
/* 118 */       writeCount += writeInt(numBuffers, destination);
/*     */       
/*     */ 
/* 121 */       for (int i = 0; i < numBuffers; i++) {
/* 122 */         ByteBuffer bb = (ByteBuffer)sourceParts.get(i);
/* 123 */         int length = bb.remaining();
/* 124 */         if (length <= 0) {
/* 125 */           throw new IOException("Part at position [" + i + "] has no content to be encoded");
/*     */         }
/* 127 */         writeCount += writeInt(length, destination);
/*     */       }
/*     */       
/*     */ 
/* 131 */       for (int i = 0; i < numBuffers; i++) {
/* 132 */         ByteBuffer bb = (ByteBuffer)sourceParts.get(i);
/* 133 */         writeCount += destination.write(bb);
/*     */       }
/*     */       
/* 136 */       return writeCount;
/*     */     }
/*     */     
/*     */     private int writeInt(int i, WritableByteChannel destination) throws IOException
/*     */     {
/* 141 */       this.reusableBBForInt.clear();
/* 142 */       this.reusableIB.clear();
/* 143 */       this.reusableIB.put(i);
/* 144 */       this.reusableBBForInt.position(4);
/* 145 */       this.reusableIB.flip();
/* 146 */       this.reusableBBForInt.flip();
/*     */       
/* 148 */       return destination.write(this.reusableBBForInt);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Decoder
/*     */   {
/*     */     private final ArrayList<Integer> reusableHeaderLengths;
/*     */     
/*     */     Decoder() {
/* 157 */       this.reusableHeaderLengths = new ArrayList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public List<ByteBuffer> decode(ReadableByteChannel source)
/*     */       throws IOException
/*     */     {
/* 167 */       NioByteArrayOutputStream nioBaos = new NioByteArrayOutputStream(1024);Throwable localThrowable3 = null;
/* 168 */       try { WritableByteChannel destination = Channels.newChannel(nioBaos);Object localObject1 = null;
/* 169 */         try { long numBytes = ByteStreams.copy(source, destination);
/* 170 */           if (numBytes == 0L) {
/* 171 */             throw new IOException("There is no content to be decoded");
/*     */           }
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 168 */           localObject1 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/* 173 */           if (destination != null) if (localObject1 != null) try { destination.close(); } catch (Throwable x2) { ((Throwable)localObject1).addSuppressed(x2); } else destination.close();
/*     */         }
/* 175 */         ByteBuffer copiedSource = nioBaos.bufferView();
/*     */         
/* 177 */         return decode(copiedSource, false);
/*     */       }
/*     */       catch (Throwable localThrowable2)
/*     */       {
/* 167 */         localThrowable3 = localThrowable2;throw localThrowable2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 178 */         if (nioBaos != null) { if (localThrowable3 != null) try { nioBaos.close(); } catch (Throwable x2) { localThrowable3.addSuppressed(x2); } else { nioBaos.close();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public List<ByteBuffer> decode(ByteBuffer source, boolean copy)
/*     */       throws IOException
/*     */     {
/* 191 */       if (source.remaining() <= 0) {
/* 192 */         throw new IOException("There is no content to be decoded");
/*     */       }
/*     */       
/* 195 */       IntBuffer intsOnlyBufferView = source.asIntBuffer();
/* 196 */       int fromPosition = 0;
/*     */       
/*     */ 
/* 199 */       int numHeaders = intsOnlyBufferView.get();
/* 200 */       if (numHeaders <= 0) {
/* 201 */         throw new IOException("Message header count appears to be corrupt");
/*     */       }
/* 203 */       fromPosition += 4;
/*     */       try
/*     */       {
/* 206 */         this.reusableHeaderLengths.ensureCapacity(numHeaders);
/*     */         
/* 208 */         for (int i = 0; i < numHeaders; i++) {
/* 209 */           int headerLength = intsOnlyBufferView.get();
/* 210 */           if (headerLength <= 0) {
/* 211 */             throw new IOException("Message header length appears to be corrupt");
/*     */           }
/* 213 */           this.reusableHeaderLengths.add(Integer.valueOf(headerLength));
/*     */         }
/* 215 */         fromPosition += 4 * numHeaders;
/*     */         
/* 217 */         ArrayList<ByteBuffer> results = new ArrayList(numHeaders);
/*     */         
/* 219 */         for (int i = 0; i < numHeaders; i++) {
/* 220 */           int bodyLength = ((Integer)this.reusableHeaderLengths.get(i)).intValue();
/*     */           
/* 222 */           ByteBuffer body = source.slice();
/*     */           
/* 224 */           body.position(fromPosition);
/* 225 */           body.limit(fromPosition + bodyLength);
/* 226 */           if (copy) {
/* 227 */             body = body.duplicate();
/*     */           }
/* 229 */           results.add(body);
/*     */           
/* 231 */           fromPosition += bodyLength;
/*     */         }
/*     */         
/* 234 */         return results;
/*     */       } finally {
/* 236 */         this.reusableHeaderLengths.clear();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/codec/Binary.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */