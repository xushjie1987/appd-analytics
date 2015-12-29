/*    */ package com.appdynamics.common.util.unit;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ByteSizes
/*    */ {
/* 21 */   private static final Logger log = LoggerFactory.getLogger(ByteSizes.class);
/*    */   
/*    */ 
/* 24 */   private static final Pattern SIZE_PATTERN = Pattern.compile("\\s*([\\d.]+)\\s*([TGMK]B)", 2);
/* 25 */   private static final Map<String, Integer> SIZE_POWER_MAP = new HashMap();
/*    */   
/*    */   static {
/* 28 */     SIZE_POWER_MAP.put("TB", Integer.valueOf(4));
/* 29 */     SIZE_POWER_MAP.put("GB", Integer.valueOf(3));
/* 30 */     SIZE_POWER_MAP.put("MB", Integer.valueOf(2));
/* 31 */     SIZE_POWER_MAP.put("KB", Integer.valueOf(1));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static long parseToBytes(String sizeStr)
/*    */   {
/* 44 */     if (sizeStr == null) {
/* 45 */       throw new IllegalArgumentException("Could not parse null into bytes.");
/*    */     }
/*    */     
/* 48 */     Matcher matcher = SIZE_PATTERN.matcher(sizeStr);
/*    */     long sizeBytes;
/* 50 */     if ((matcher.find()) && (matcher.groupCount() > 1)) {
/* 51 */       String number = matcher.group(1);
/* 52 */       int pow = ((Integer)SIZE_POWER_MAP.get(matcher.group(2).toUpperCase())).intValue();
/* 53 */       BigDecimal bytes = new BigDecimal(number).multiply(BigDecimal.valueOf(1024L).pow(pow));
/* 54 */       sizeBytes = bytes.longValue();
/*    */     } else {
/* 56 */       throw new IllegalArgumentException("Could not parse [" + sizeStr + "] into bytes.  Supported formats are [TB, GB, MB, KB]");
/*    */     }
/*    */     
/*    */     long sizeBytes;
/* 60 */     log.debug("Parsed [{}] into [{}] bytes", sizeStr, Long.valueOf(sizeBytes));
/* 61 */     return sizeBytes;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/unit/ByteSizes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */