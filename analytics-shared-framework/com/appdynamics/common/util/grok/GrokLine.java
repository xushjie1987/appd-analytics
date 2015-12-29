/*    */ package com.appdynamics.common.util.grok;
/*    */ 
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.configuration.Parameters;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.regex.Pattern;
/*    */ import java.util.regex.PatternSyntaxException;
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
/*    */ class GrokLine
/*    */ {
/*    */   public String toString()
/*    */   {
/* 27 */     return "GrokLine(name=" + getName() + ", patternString=" + getPatternString() + ", aliasAndTypes=" + getAliasAndTypes() + ", effectiveType=" + getEffectiveType() + ")"; }
/*    */   
/* 29 */   private static final Logger log = LoggerFactory.getLogger(GrokLine.class);
/*    */   
/*    */   final String name;
/*    */   final String patternString;
/*    */   
/* 34 */   public String getName() { return this.name; }
/* 35 */   public String getPatternString() { return this.patternString; }
/* 36 */   public Map<String, GrokValueType> getAliasAndTypes() { return this.aliasAndTypes; }
/* 37 */   public GrokValueType getEffectiveType() { return this.effectiveType; }
/*    */   
/*    */   GrokLine(String name, String patternString, Map<String, GrokValueType> aliasAndTypes, GrokValueType effectiveType) {
/* 40 */     this.name = name;
/* 41 */     this.patternString = patternString;
/* 42 */     this.aliasAndTypes = aliasAndTypes;
/* 43 */     this.effectiveType = effectiveType;
/*    */   }
/*    */   
/*    */ 
/*    */   final Map<String, GrokValueType> aliasAndTypes;
/*    */   final GrokValueType effectiveType;
/*    */   GrokPattern compile(Map<String, String> javaAndGrokAliases, Map<String, String> javaAndGrokAliasesInDefinition)
/*    */   {
/*    */     Pattern compiledPattern;
/*    */     try
/*    */     {
/* 54 */       compiledPattern = Pattern.compile(this.patternString);
/*    */     } catch (PatternSyntaxException e) {
/* 56 */       throw new ConfigurationException("The pattern [" + this.patternString + "] does not appear to resolve to a valid Java Regular Expression", e);
/*    */     }
/*    */     
/*    */ 
/* 60 */     Map<String, Integer> names = RegexHelper.namedGroups(compiledPattern);
/*    */     
/* 62 */     String[] aliases = new String[names.size()];
/* 63 */     String[] aliasesJavaCompliant = new String[names.size()];
/* 64 */     int[] aliasGroupIds = new int[names.size()];
/* 65 */     GrokValueType[] valueTypes = new GrokValueType[names.size()];
/* 66 */     int i = 0;
/* 67 */     for (Map.Entry<String, Integer> entry : names.entrySet()) {
/* 68 */       String key = (String)entry.getKey();
/* 69 */       aliasesJavaCompliant[i] = key;
/* 70 */       aliases[i] = ((String)Parameters.firstNonNull(javaAndGrokAliasesInDefinition.get(key), javaAndGrokAliases.get(key), key));
/*    */       
/* 72 */       aliasGroupIds[i] = ((Integer)entry.getValue()).intValue();
/* 73 */       valueTypes[i] = GrokValueType.defaultIfNull((GrokValueType)this.aliasAndTypes.get(key));
/* 74 */       i++;
/*    */     }
/*    */     
/* 77 */     if (log.isDebugEnabled()) {
/* 78 */       log.debug("Pattern [{}] has {} Named Capture Groups, {} aliases and their Java compliant aliases {}", new Object[] { this.patternString, names, Arrays.asList(aliases), Arrays.asList(aliasesJavaCompliant) });
/*    */     }
/*    */     
/* 81 */     return new GrokPattern(compiledPattern, aliases, aliasesJavaCompliant, aliasGroupIds, valueTypes);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/grok/GrokLine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */