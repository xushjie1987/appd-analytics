/*     */ package com.appdynamics.common.io.codec;
/*     */ 
/*     */ import com.appdynamics.common.io.NioByteArrayOutputStream;
/*     */ import com.appdynamics.common.util.configuration.Reader;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public abstract class Json
/*     */ {
/*     */   public static Encoder newEncoder()
/*     */   {
/*  55 */     return new Encoder();
/*     */   }
/*     */   
/*     */   public static Decoder newDecoder() {
/*  59 */     return new Decoder();
/*     */   }
/*     */   
/*     */   public static Walker newWalker() {
/*  63 */     return new Walker();
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
/*     */   public static Pair<Map<String, Object>, Object> parseJsonAndRead(String jsonUtf8, String... path)
/*     */     throws IOException
/*     */   {
/*  81 */     InputStream is = new ByteArrayInputStream(jsonUtf8.getBytes(Charsets.UTF_8));Throwable localThrowable2 = null;
/*  82 */     Map<String, Object> map; try { map = (Map)Reader.readFrom(Map.class, is);
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/*  81 */       localThrowable2 = localThrowable1;throw localThrowable1;
/*     */     } finally {
/*  83 */       if (is != null) if (localThrowable2 != null) try { is.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else is.close(); }
/*  84 */     Object item = navigateAndRead(map, path);
/*  85 */     return new Pair(map, item);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object navigateAndRead(Map<String, Object> map, String... path)
/*     */   {
/*  97 */     Object o = map;
/*  98 */     for (String pathElement : path) {
/*  99 */       if ((o instanceof Map)) {
/* 100 */         o = ((Map)o).get(pathElement);
/*     */       } else {
/* 102 */         throw new IllegalArgumentException("The path element [" + pathElement + "] does not appear to be a map");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 107 */     if (path.length == 0) {
/* 108 */       o = null;
/*     */     }
/* 110 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Encoder
/*     */   {
/*     */     private final HashMap<String, Object> reusableMap;
/*     */     
/*     */ 
/*     */ 
/*     */     private final NioByteArrayOutputStream reusableBaos;
/*     */     
/*     */ 
/*     */     private final ObjectMapper reusableObjectMapper;
/*     */     
/*     */ 
/*     */ 
/*     */     Encoder()
/*     */     {
/* 131 */       this.reusableMap = new HashMap();
/* 132 */       this.reusableBaos = new NioByteArrayOutputStream(512);
/* 133 */       this.reusableObjectMapper = Reader.DEFAULT_JSON_MAPPER;
/*     */     }
/*     */     
/*     */     public Map<String, Object> startEncode()
/*     */     {
/* 138 */       abortEncode();
/*     */       
/* 140 */       return this.reusableMap;
/*     */     }
/*     */     
/*     */     public void encodeBoolean(String fieldName, boolean value) throws IOException {
/* 144 */       this.reusableMap.put(fieldName, Boolean.valueOf(value));
/*     */     }
/*     */     
/*     */     public void encodeString(String fieldName, String value) throws IOException {
/* 148 */       this.reusableMap.put(fieldName, value);
/*     */     }
/*     */     
/*     */     public void encodeInt(String fieldName, int value) throws IOException {
/* 152 */       this.reusableMap.put(fieldName, Integer.valueOf(value));
/*     */     }
/*     */     
/*     */     public void encodeLong(String fieldName, long value) throws IOException {
/* 156 */       this.reusableMap.put(fieldName, Long.valueOf(value));
/*     */     }
/*     */     
/*     */     public void encodeDouble(String fieldName, double value) throws IOException {
/* 160 */       this.reusableMap.put(fieldName, Double.valueOf(value));
/*     */     }
/*     */     
/*     */     public void abortEncode() {
/* 164 */       this.reusableBaos.reset();
/* 165 */       if (this.reusableMap.size() > 0) {
/* 166 */         this.reusableMap.clear();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ByteBuffer endEncode()
/*     */       throws IOException
/*     */     {
/* 177 */       if (this.reusableMap.size() == 0) {
/* 178 */         throw new IllegalArgumentException("There is no data to encode");
/*     */       }
/*     */       try {
/* 181 */         this.reusableObjectMapper.writeValue(this.reusableBaos, this.reusableMap);
/*     */       } finally {
/* 183 */         this.reusableMap.clear();
/*     */       }
/*     */       
/* 186 */       return this.reusableBaos.bufferView();
/*     */     }
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
/*     */   public static final class Decoder
/*     */   {
/*     */     private JsonNode rootNode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void startDecode(ByteBuffer arrayBackedByteBuffer)
/*     */       throws IOException
/*     */     {
/* 214 */       if (!arrayBackedByteBuffer.hasArray()) {
/* 215 */         throw new IllegalArgumentException("Only byte buffers with underlying arrays are supported");
/*     */       }
/* 217 */       byte[] bytes = arrayBackedByteBuffer.array();
/* 218 */       int offset = arrayBackedByteBuffer.position();
/* 219 */       int length = arrayBackedByteBuffer.remaining();
/* 220 */       if (length == 0) {
/* 221 */         throw new IllegalArgumentException("The byte buffer does not have any content to read from");
/*     */       }
/*     */       
/* 224 */       startDecode(bytes, offset, length);
/*     */     }
/*     */     
/*     */     public void startDecode(byte[] bytes, int offset, int length) throws IOException {
/* 228 */       JsonFactory jsonFactory = new JsonFactory(Reader.DEFAULT_JSON_MAPPER);
/* 229 */       JsonParser jsonParser = jsonFactory.createParser(bytes, offset, offset + length);Throwable localThrowable2 = null;
/* 230 */       try { this.rootNode = ((JsonNode)jsonParser.readValueAsTree());
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 229 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */       } finally {
/* 231 */         if (jsonParser != null) if (localThrowable2 != null) try { jsonParser.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else jsonParser.close();
/*     */       }
/*     */     }
/*     */     
/* 235 */     public boolean decodeBoolean(String fieldName) throws IOException { return this.rootNode.get(fieldName).asBoolean(); }
/*     */     
/*     */     public String decodeString(String fieldName) throws IOException
/*     */     {
/* 239 */       return this.rootNode.get(fieldName).asText();
/*     */     }
/*     */     
/*     */     public int decodeInt(String fieldName) throws IOException {
/* 243 */       return this.rootNode.get(fieldName).asInt();
/*     */     }
/*     */     
/*     */     public long decodeLong(String fieldName) throws IOException {
/* 247 */       return this.rootNode.get(fieldName).asLong();
/*     */     }
/*     */     
/*     */     public double decodeDouble(String fieldName) throws IOException {
/* 251 */       return this.rootNode.get(fieldName).asDouble();
/*     */     }
/*     */     
/*     */     public void endDecode() {
/* 255 */       this.rootNode = null;
/*     */     }
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
/*     */   public static final class Walker
/*     */   {
/* 273 */     private static final Logger log = LoggerFactory.getLogger(Walker.class);
/*     */     private final JsonFactory jsonFactory;
/*     */     private byte[] bytes;
/*     */     private JsonParser jsonParser;
/*     */     private int nestingDepth;
/*     */     
/*     */     Walker()
/*     */     {
/* 281 */       this.jsonFactory = new JsonFactory(Reader.DEFAULT_JSON_MAPPER);
/* 282 */       reset();
/*     */     }
/*     */     
/*     */     private void reset() {
/* 286 */       this.bytes = null;
/* 287 */       this.jsonParser = null;
/* 288 */       this.nestingDepth = 0;
/*     */     }
/*     */     
/*     */     private void maintainNestingDepth(JsonToken optToken) {
/* 292 */       if (optToken != null) {
/* 293 */         switch (Json.1.$SwitchMap$com$fasterxml$jackson$core$JsonToken[optToken.ordinal()]) {
/*     */         case 1: 
/*     */         case 2: 
/* 296 */           pushNestingDepth();
/* 297 */           break;
/*     */         case 3: 
/*     */         case 4: 
/* 300 */           popNestingDepth();
/* 301 */           break;
/*     */         
/*     */         }
/*     */         
/*     */       } else {
/* 306 */         popNestingDepth();
/*     */       }
/*     */     }
/*     */     
/*     */     private void pushNestingDepth() {
/* 311 */       this.nestingDepth += 1;
/*     */     }
/*     */     
/*     */     private void popNestingDepth() {
/* 315 */       if (this.nestingDepth > 0) {
/* 316 */         this.nestingDepth -= 1;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void startWalk(ByteBuffer arrayBackedByteBuffer)
/*     */       throws IOException
/*     */     {
/* 327 */       if (!arrayBackedByteBuffer.hasArray()) {
/* 328 */         throw new IllegalArgumentException("Only byte buffers with underlying arrays are supported");
/*     */       }
/* 330 */       byte[] bytes = arrayBackedByteBuffer.array();
/* 331 */       int offset = arrayBackedByteBuffer.position();
/* 332 */       int length = arrayBackedByteBuffer.remaining();
/* 333 */       startWalk(bytes, offset, length);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void startWalk(byte[] bytes, int offset, int length)
/*     */       throws IOException
/*     */     {
/* 344 */       if (length == 0) {
/* 345 */         throw new IllegalArgumentException("The byte array does not have any content to read from");
/*     */       }
/*     */       
/* 348 */       this.jsonParser = this.jsonFactory.createParser(bytes, offset, length);
/* 349 */       this.bytes = bytes;
/*     */     }
/*     */     
/*     */     public boolean next() throws IOException {
/* 353 */       JsonToken token = this.jsonParser.nextToken();
/* 354 */       maintainNestingDepth(token);
/* 355 */       return token != null;
/*     */     }
/*     */     
/*     */     public int getNestingDepth() {
/* 359 */       return this.nestingDepth;
/*     */     }
/*     */     
/*     */     public boolean isArrayStart() throws IOException {
/* 363 */       return this.jsonParser.getCurrentToken() == JsonToken.START_ARRAY;
/*     */     }
/*     */     
/*     */     public boolean isObjectStart() throws IOException {
/* 367 */       return this.jsonParser.getCurrentToken() == JsonToken.START_OBJECT;
/*     */     }
/*     */     
/*     */     public boolean isArrayOrObjectStart() throws IOException {
/* 371 */       JsonToken token = this.jsonParser.getCurrentToken();
/* 372 */       return (token == JsonToken.START_ARRAY) || (token == JsonToken.START_OBJECT);
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
/*     */     public Json.ScanResult scanAndSkipArrayOrObject()
/*     */       throws IOException
/*     */     {
/* 386 */       boolean arrayStart = isArrayStart();
/* 387 */       boolean objectStart = isObjectStart();
/*     */       
/*     */ 
/* 390 */       Preconditions.checkState((arrayStart) || (objectStart));
/* 391 */       long start = Math.min(this.jsonParser.getCurrentLocation().getByteOffset(), this.jsonParser.getTokenLocation().getByteOffset());
/*     */       
/* 393 */       this.jsonParser.skipChildren();
/* 394 */       Preconditions.checkState(isArrayOrObjectEnd());
/* 395 */       long end = Math.max(this.jsonParser.getCurrentLocation().getByteOffset(), this.jsonParser.getTokenLocation().getByteOffset());
/*     */       
/* 397 */       JsonToken token = this.jsonParser.getCurrentToken();
/* 398 */       maintainNestingDepth(token);
/*     */       
/* 400 */       boolean hasNext = next();
/* 401 */       char startChar = arrayStart ? '[' : '{';
/* 402 */       char endChar = arrayStart ? ']' : '}';
/* 403 */       Pair<Integer, Integer> range = refineRange((int)start, (int)end, startChar, endChar);
/* 404 */       return new Json.ScanResult((Integer)range.getLeft(), (Integer)range.getRight(), hasNext);
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
/*     */ 
/*     */ 
/*     */     private Pair<Integer, Integer> refineRange(int proposedStart, int proposedEnd, char startChar, char endChar)
/*     */     {
/* 421 */       proposedStart = Math.max(0, proposedStart);
/* 422 */       proposedEnd = Math.min(this.bytes.length - 1, proposedEnd);
/*     */       
/*     */ 
/* 425 */       while ((proposedStart <= proposedEnd) && (this.bytes[proposedStart] != startChar)) {
/* 426 */         proposedStart++;
/*     */       }
/*     */       
/* 429 */       while ((proposedStart <= proposedEnd) && (this.bytes[proposedEnd] != endChar)) {
/* 430 */         proposedEnd--;
/*     */       }
/* 432 */       return new Pair(Integer.valueOf(proposedStart), Integer.valueOf(proposedEnd - proposedStart + 1));
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
/*     */     public Pair<JsonNode, Boolean> consumeAndReadAsTree()
/*     */       throws IOException
/*     */     {
/* 446 */       Preconditions.checkState(!isArrayOrObjectEnd());
/*     */       
/* 448 */       boolean popAfterReading = isArrayOrObjectStart();
/* 449 */       JsonNode jsonNode = (JsonNode)this.jsonParser.readValueAsTree();
/* 450 */       if (popAfterReading) {
/* 451 */         popNestingDepth();
/*     */       }
/* 453 */       boolean has = next();
/* 454 */       return new Pair(jsonNode, Boolean.valueOf(has));
/*     */     }
/*     */     
/*     */     public boolean isArrayEnd() throws IOException {
/* 458 */       return this.jsonParser.getCurrentToken() == JsonToken.END_ARRAY;
/*     */     }
/*     */     
/*     */     public boolean isObjectEnd() throws IOException {
/* 462 */       return this.jsonParser.getCurrentToken() == JsonToken.END_OBJECT;
/*     */     }
/*     */     
/*     */     public boolean isArrayOrObjectEnd() throws IOException {
/* 466 */       JsonToken token = this.jsonParser.getCurrentToken();
/* 467 */       return (token == JsonToken.END_ARRAY) || (token == JsonToken.END_OBJECT);
/*     */     }
/*     */     
/*     */     public boolean isFieldStart() throws IOException {
/* 471 */       return this.jsonParser.getCurrentToken() == JsonToken.FIELD_NAME;
/*     */     }
/*     */     
/*     */     public String readAsString() throws IOException {
/* 475 */       return this.jsonParser.getText();
/*     */     }
/*     */     
/*     */     public void endWalk() {
/* 479 */       if (this.jsonParser != null) {
/*     */         try {
/* 481 */           this.jsonParser.close();
/*     */         } catch (IOException e) {
/* 483 */           log.warn("Error occurred while closing the internal JSON parser", e);
/*     */         }
/*     */       }
/* 486 */       reset();
/*     */     }
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
/*     */   public static List<ScanResult> scanChildrenOfArray(Walker walker, byte[] jsonArray, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 506 */     List<ScanResult> children = new ArrayList(8);
/*     */     
/* 508 */     walker.startWalk(jsonArray, offset, length);
/*     */     try
/*     */     {
/* 511 */       if (walker.next()) {
/* 512 */         if (!walker.isArrayStart()) {
/* 513 */           throw new IllegalArgumentException("The JSON structure does not contain a top level array");
/*     */         }
/*     */         
/* 516 */         if (walker.next())
/*     */         {
/* 518 */           while (walker.isArrayOrObjectStart()) {
/* 519 */             children.add(walker.scanAndSkipArrayOrObject());
/*     */           }
/*     */         }
/*     */         
/* 523 */         if (!walker.isArrayEnd()) {
/* 524 */           throw new IllegalArgumentException("The top level array either does not end as an array or seems to have children that are not themselves objects or arrays");
/*     */         }
/*     */       }
/*     */     }
/*     */     finally {
/* 529 */       walker.endWalk();
/*     */     }
/*     */     
/* 532 */     return children;
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
/*     */   public static void readTopLevelFields(Walker walker, byte[] jsonBytes, int offset, int length, Map<String, JsonNode> fieldNamesToReadInto)
/*     */     throws IOException
/*     */   {
/* 553 */     walker.startWalk(jsonBytes, offset, length);
/*     */     try {
/* 555 */       int fieldNamesToRead = fieldNamesToReadInto.size();
/* 556 */       Preconditions.checkArgument(fieldNamesToRead > 0, "The field names to read must be provided");
/*     */       
/*     */ 
/* 559 */       if (walker.next()) {
/* 560 */         String fieldNameStarted = null;
/* 561 */         Boolean optionalHasNext = null;
/*     */         
/*     */ 
/* 564 */         while ((fieldNamesToRead > 0) && (((optionalHasNext != null) && (optionalHasNext.booleanValue())) || (walker.next()))) {
/* 565 */           String walkerString = null;
/* 566 */           if ((walker.isFieldStart()) && (fieldNamesToReadInto.containsKey(walkerString = walker.readAsString())))
/*     */           {
/*     */ 
/*     */ 
/* 570 */             fieldNameStarted = walkerString;
/* 571 */           } else { if (fieldNameStarted != null)
/*     */             {
/* 573 */               Pair<JsonNode, Boolean> pair = walker.consumeAndReadAsTree();
/* 574 */               fieldNamesToReadInto.put(fieldNameStarted, pair.getLeft());
/* 575 */               fieldNamesToRead--;
/*     */               
/* 577 */               fieldNameStarted = null;
/* 578 */               optionalHasNext = (Boolean)pair.getRight();
/*     */               
/* 580 */               continue; }
/* 581 */             if (walker.isArrayOrObjectStart())
/*     */             {
/* 583 */               optionalHasNext = Boolean.valueOf(walker.scanAndSkipArrayOrObject().hasMore());
/*     */               
/* 585 */               continue;
/*     */             } }
/* 587 */           optionalHasNext = null;
/*     */         }
/*     */       }
/*     */     } finally {
/* 591 */       walker.endWalk();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ScanResult extends Pair<Integer, Integer> {
/*     */     final boolean more;
/*     */     
/*     */     public boolean equals(Object o) {
/* 599 */       if (o == this) return true; if (!(o instanceof ScanResult)) return false; ScanResult other = (ScanResult)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; return this.more == other.more; } public boolean canEqual(Object other) { return other instanceof ScanResult; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();result = result * 31 + (this.more ? 1231 : 1237);return result; }
/* 600 */     public String toString() { return "Json.ScanResult(super=" + super.toString() + ", more=" + this.more + ")"; }
/*     */     
/*     */ 
/*     */     ScanResult(Integer left, Integer right, boolean more)
/*     */     {
/* 605 */       super(right);
/* 606 */       this.more = more;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean hasMore()
/*     */     {
/* 613 */       return this.more;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/codec/Json.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */