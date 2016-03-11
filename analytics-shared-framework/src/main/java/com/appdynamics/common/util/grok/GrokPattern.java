/*    */ package com.appdynamics.common.util.grok;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.regex.Pattern;
/*    */ import javax.annotation.concurrent.ThreadSafe;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ThreadSafe
/*    */ public class GrokPattern
/*    */ {
/*    */   public String toString()
/*    */   {
/* 29 */     return "GrokPattern(pattern=" + getPattern() + ", aliases=" + Arrays.deepToString(getAliases()) + ", aliasesJavaCompliant=" + Arrays.deepToString(getAliasesJavaCompliant()) + ", aliasGroupIds=" + Arrays.toString(getAliasGroupIds()) + ", valueTypes=" + Arrays.deepToString(getValueTypes()) + ")"; }
/*    */   
/* 31 */   private static final Logger log = LoggerFactory.getLogger(GrokPattern.class);
/*    */   
/* 33 */   public Pattern getPattern() { return this.pattern; }
/* 34 */   public String[] getAliases() { return this.aliases; }
/* 35 */   public String[] getAliasesJavaCompliant() { return this.aliasesJavaCompliant; }
/* 36 */   public int[] getAliasGroupIds() { return this.aliasGroupIds; }
/* 37 */   public GrokValueType[] getValueTypes() { return this.valueTypes; }
/*    */   
/*    */   final Pattern pattern;
/*    */   
/* 41 */   GrokPattern(Pattern pattern, String[] aliases, String[] aliasesJavaCompliant, int[] aliasGroupIds, GrokValueType[] valueTypes) { this.pattern = pattern;
/* 42 */     this.aliases = aliases;
/* 43 */     this.aliasesJavaCompliant = aliasesJavaCompliant;
/* 44 */     this.aliasGroupIds = aliasGroupIds;
/* 45 */     this.valueTypes = valueTypes;
/*    */   }
/*    */   
/*    */   final String[] aliases;
/*    */   final String[] aliasesJavaCompliant;
/*    */   final int[] aliasGroupIds;
/*    */   final GrokValueType[] valueTypes;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/grok/GrokPattern.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */