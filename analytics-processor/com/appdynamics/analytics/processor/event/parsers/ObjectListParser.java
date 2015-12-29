/*     */ package com.appdynamics.analytics.processor.event.parsers;
/*     */ 
/*     */ import com.appdynamics.analytics.processor.exception.RequestParsingException;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Charsets;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Stack;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class ObjectListParser
/*     */ {
/*  33 */   private static final Logger log = LoggerFactory.getLogger(ObjectListParser.class);
/*     */   private final JsonFactory jsonFactory;
/*     */   private final int payloadOffset;
/*     */   private final int payloadLength;
/*     */   private byte[] currentPayload;
/*     */   private int rootObjectOffset;
/*     */   private int rootObjectLength;
/*     */   
/*     */   public int getRootObjectOffset() {
/*  42 */     return this.rootObjectOffset;
/*     */   }
/*     */   
/*  45 */   public int getRootObjectLength() { return this.rootObjectLength; }
/*     */   
/*     */ 
/*     */   private JsonParser parser;
/*     */   private ParserState state;
/*  50 */   private Stack<JsonNode> objectStack = new Stack();
/*  51 */   private ObjectMapper mapper = new ObjectMapper();
/*     */   
/*     */   protected ObjectListParser(byte[] payloads, int payloadOffset, int payloadLength) {
/*  54 */     this.currentPayload = payloads;
/*  55 */     this.payloadOffset = payloadOffset;
/*  56 */     this.payloadLength = payloadLength;
/*  57 */     this.jsonFactory = new JsonFactory();
/*  58 */     reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/*  65 */     this.state = ParserState.START;
/*     */     try {
/*  67 */       this.parser = this.jsonFactory.createParser(this.currentPayload, this.payloadOffset, this.payloadLength);
/*     */     } catch (IOException e) {
/*  69 */       throw new RequestParsingException("Could not parse payload", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getPayloadOffset() {
/*  74 */     return this.payloadOffset;
/*     */   }
/*     */   
/*     */   public int getPayloadLength() {
/*  78 */     return this.payloadLength;
/*     */   }
/*     */   
/*     */   public byte[] getPayloads() {
/*  82 */     return this.currentPayload;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean next()
/*     */   {
/*  90 */     int nestingLevel = 0;
/*     */     try {
/*  92 */       for (JsonToken token = this.parser.nextValue(); token != null; token = this.parser.nextValue()) {
/*  93 */         switch (this.state)
/*     */         {
/*     */         case START: 
/*  96 */           if (token != JsonToken.START_ARRAY) {
/*  97 */             throw new RequestParsingException("Expected an array of objects, got " + token.name());
/*     */           }
/*  99 */           this.state = ParserState.IN_ARRAY;
/* 100 */           break;
/*     */         
/*     */         case IN_ARRAY: 
/* 103 */           if (token == JsonToken.START_OBJECT) {
/* 104 */             this.rootObjectOffset = getStartOffset();
/* 105 */             log.trace("Object start at {}", Integer.valueOf(this.rootObjectOffset));
/* 106 */             startObject(null, 0, this.rootObjectOffset);
/* 107 */           } else if (token != JsonToken.END_ARRAY) {
/* 108 */             throw new RequestParsingException("Expected objects in array, got " + token.name());
/*     */           }
/* 110 */           this.state = ParserState.IN_OBJECT;
/* 111 */           nestingLevel = 1;
/* 112 */           break;
/*     */         case IN_OBJECT: 
/* 114 */           switch (token) {
/*     */           case END_OBJECT: 
/* 116 */             nestingLevel--;
/* 117 */             int end = getEndOffset();
/* 118 */             endObject(nestingLevel, end);
/* 119 */             if (nestingLevel == 0) {
/* 120 */               log.trace("Object end at {}", Integer.valueOf(end));
/* 121 */               this.rootObjectLength = (end - this.rootObjectOffset);
/* 122 */               if (log.isTraceEnabled()) {
/* 123 */                 String objStr = new String(this.currentPayload, this.rootObjectOffset, this.rootObjectLength, Charsets.UTF_8);
/*     */                 
/* 125 */                 log.trace("Found object {}", objStr);
/*     */               }
/* 127 */               this.objectStack.push(this.mapper.readTree(Arrays.copyOfRange(this.currentPayload, this.rootObjectOffset, this.rootObjectOffset + this.rootObjectLength)));
/*     */               
/*     */ 
/* 130 */               this.state = ParserState.IN_ARRAY;
/* 131 */               return true;
/*     */             }
/*     */             break;
/*     */           case START_OBJECT: 
/* 135 */             int start = getStartOffset();
/* 136 */             startObject(this.parser.getCurrentName(), nestingLevel, start);
/* 137 */             nestingLevel++;
/* 138 */             break;
/*     */           case VALUE_FALSE: 
/* 140 */             field(this.parser.getCurrentName(), false);
/* 141 */             break;
/*     */           case VALUE_TRUE: 
/* 143 */             field(this.parser.getCurrentName(), true);
/* 144 */             break;
/*     */           case VALUE_NULL: 
/* 146 */             nullValue(this.parser.getCurrentName());
/* 147 */             break;
/*     */           case VALUE_NUMBER_FLOAT: 
/* 149 */             field(this.parser.getCurrentName(), this.parser.getDoubleValue());
/* 150 */             break;
/*     */           case VALUE_NUMBER_INT: 
/* 152 */             field(this.parser.getCurrentName(), this.parser.getLongValue());
/* 153 */             break;
/*     */           case VALUE_STRING: 
/* 155 */             field(this.parser.getCurrentName(), this.parser.getValueAsString());
/*     */           }
/*     */           
/*     */           
/*     */ 
/* 160 */           break;
/*     */         
/*     */         default: 
/* 163 */           log.warn("Bad parser state {}", this.state);
/* 164 */           throw new RuntimeException("Bad parser state " + this.state);
/*     */         }
/*     */       }
/*     */     } catch (IOException e) {
/* 168 */       throw new RequestParsingException("Could not parse the payload " + new String(this.currentPayload, this.payloadOffset, this.payloadLength, Charsets.UTF_8), e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 173 */     return false;
/*     */   }
/*     */   
/*     */   public int getEndOffset() {
/* 177 */     return (int)this.parser.getCurrentLocation().getByteOffset() + this.payloadOffset;
/*     */   }
/*     */   
/*     */   public int getStartOffset() {
/* 181 */     return (int)(this.parser.getCurrentLocation().getByteOffset() - 1L + this.payloadOffset);
/*     */   }
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
/*     */   public void parseAndCheckFormat()
/*     */   {
/* 246 */     while (next()) {}
/*     */     
/* 248 */     this.state = ParserState.END;
/*     */   }
/*     */   
/*     */   public boolean hasParsed() {
/* 252 */     return this.state == ParserState.END;
/*     */   }
/*     */   
/*     */   public JsonNode nextObject() {
/* 256 */     if (!next()) {
/* 257 */       return null;
/*     */     }
/* 259 */     return (JsonNode)this.objectStack.pop();
/*     */   }
/*     */   
/*     */ 
/* 263 */   public long getCurrentNumberOfPayloads() { return this.objectStack.size(); }
/*     */   
/*     */   protected void field(String fieldName, String val) {}
/*     */   
/* 267 */   private static enum ParserState { START, 
/* 268 */     IN_ARRAY, 
/* 269 */     IN_OBJECT, 
/* 270 */     END;
/*     */     
/*     */     private ParserState() {}
/*     */   }
/*     */   
/*     */   protected void field(String fieldName, long val) {}
/*     */   
/*     */   protected void field(String fieldName, double val) {}
/*     */   
/*     */   protected void nullValue(String fieldName) {}
/*     */   
/*     */   protected void field(String fieldName, boolean val) {}
/*     */   
/*     */   protected void startObject(String fieldName, int nestingLevel, int offset) {}
/*     */   
/*     */   protected void endObject(int nestingLevel, int offset) {}
/*     */   
/* 287 */   public static class Factory { public ObjectListParser make(byte[] payloads, int payloadOffset, int payloadLength) { return new ObjectListParser(payloads, payloadOffset, payloadLength); }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/parsers/ObjectListParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */