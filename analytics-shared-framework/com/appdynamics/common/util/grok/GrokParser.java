/*     */ package com.appdynamics.common.util.grok;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.appdynamics.common.util.misc.Pair;
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ @ThreadSafe
/*     */ public abstract class GrokParser
/*     */ {
/*  63 */   private static final Logger log = LoggerFactory.getLogger(GrokParser.class);
/*     */   
/*     */ 
/*     */   public static final String PREFIX_GROK_LINE_COMMENT = "#";
/*     */   
/*     */   public static final String SEP_GROK_LINE_NAME_VALUE = " ";
/*     */   
/*     */   public static final String SAMPLE_PATTERN_REF = "%{that-pattern-name:alias}";
/*     */   
/*     */   public static final String MSG_EXPECTED_FORMAT = "pattern-name[:type] (literal? / regex? / pattern-name-reference? (%{that-pattern-name:alias}))+";
/*     */   
/*     */   static final String LINE_SUFFIX = "(?<lineSuffix>.*)?";
/*     */   
/*  76 */   static final Pattern PATTERN_GROK_LINE = Pattern.compile("(?<linePrefix>.*?)??%\\{(?<patternReference>[_a-zA-Z][_a-zA-Z\\d]*)(?:\\:(?<alias>[_a-zA-Z][_a-zA-Z\\d]*))?\\}(?<lineSuffix>.*)?");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   static final Pattern PATTERN_GROK_ALIAS = Pattern.compile("%\\{[_a-zA-Z][_a-zA-Z\\d]*(?:\\:(?<alias>[_a-zA-Z][_a-zA-Z\\d]*))\\}(?<lineSuffix>.*)?");
/*     */   
/*     */ 
/*     */ 
/*  85 */   static final Pattern PATTERN_JAVA_ALIAS = Pattern.compile("\\(\\?<(?<alias>[_a-zA-Z][_a-zA-Z\\d]*)>(?<lineSuffix>.*)?");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */   static final Pattern PATTERN_NAME = Pattern.compile("(?<patternName>[_a-zA-Z][_a-zA-Z\\d]*?)(?:\\:(?<patternValueType>[_a-zA-Z][_a-zA-Z\\d]*))?");
/*     */   
/*     */ 
/*     */   static final String PATTERN_GROUP_LINE_PREFIX = "linePrefix";
/*     */   
/*     */ 
/*     */   static final String PATTERN_GROUP_PATTERN_REF = "patternReference";
/*     */   
/*     */ 
/*     */   static final String PATTERN_GROUP_ALIAS = "alias";
/*     */   
/*     */ 
/*     */   static final String PATTERN_GROUP_LINE_SUFFIX = "lineSuffix";
/*     */   
/*     */ 
/*     */   public static void parseLines(InputStream utf8Source, Map<String, GrokLineAst> to)
/*     */     throws IOException
/*     */   {
/* 110 */     Set<String> patternNames = to.keySet();
/* 111 */     List<String> lines = IOUtils.readLines(utf8Source, Charsets.UTF_8);
/*     */     
/* 113 */     int matchCount = 0;
/* 114 */     for (String line : lines) {
/* 115 */       log.trace("Attempting to parse line [{}]", line);
/*     */       
/* 117 */       GrokLineAst grokLineAst = parseLine(line, patternNames);
/* 118 */       if (grokLineAst != null) {
/* 119 */         GrokLineAst oldGrokLineAst = (GrokLineAst)to.put(grokLineAst.name, grokLineAst);
/* 120 */         if (oldGrokLineAst != null) {
/* 121 */           log.info("A previously parsed Grok pattern [{}] has now been overridden", grokLineAst.name);
/*     */         }
/* 123 */         matchCount++;
/*     */       }
/*     */     }
/*     */     
/* 127 */     if (matchCount == 0) {
/* 128 */       log.warn("No patterns were found");
/*     */     } else {
/* 130 */       log.debug("Processed [{}] patterns", Integer.valueOf(matchCount));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GrokLineAst parseLine(String line, Set<String> patternNames)
/*     */   {
/* 141 */     line = line.trim();
/*     */     
/* 143 */     if (isGrokComment(line)) {
/* 144 */       log.trace("Ignoring line [{}]", line);
/* 145 */       return null;
/*     */     }
/*     */     try {
/* 148 */       return parseNonComment(line, patternNames);
/*     */     } catch (RuntimeException e) {
/* 150 */       throw new ConfigurationException("Error occurred while parsing line [" + line + "]", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GrokLineAst parseDefinition(String definition, Set<String> patternNames)
/*     */   {
/* 161 */     String tempRandomKey = "tempKey_" + System.nanoTime();
/* 162 */     String newLine = tempRandomKey + " " + definition;
/* 163 */     return parseLine(newLine, patternNames);
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
/*     */   public static boolean duplicateAliasesExist(List<String> definitions)
/*     */   {
/* 184 */     Set<String> aliases = new HashSet();
/*     */     
/* 186 */     for (String definition : definitions) {
/* 187 */       String trimmedDefinition = definition.trim();
/*     */       
/* 189 */       if (duplicateGrokAliasesExist(trimmedDefinition, aliases)) {
/* 190 */         return true;
/*     */       }
/* 192 */       if (duplicateJavaRegexAliasesExist(trimmedDefinition, aliases)) {
/* 193 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 197 */     return false;
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
/*     */   static boolean duplicateGrokAliasesExist(String definition, Set<String> aliases)
/*     */   {
/* 215 */     if (aliases == null) {
/* 216 */       aliases = new HashSet();
/*     */     }
/*     */     
/* 219 */     Matcher reusableMatcher = PATTERN_GROK_ALIAS.matcher("");
/* 220 */     return duplicateAliasesExist(definition, reusableMatcher, aliases);
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
/*     */   static boolean duplicateJavaRegexAliasesExist(String definition, Set<String> aliases)
/*     */   {
/* 238 */     if (aliases == null) {
/* 239 */       aliases = new HashSet();
/*     */     }
/*     */     
/* 242 */     Matcher reusableMatcher = PATTERN_JAVA_ALIAS.matcher("");
/* 243 */     return duplicateAliasesExist(definition, reusableMatcher, aliases);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<String> getGrokAliases(String grok)
/*     */   {
/* 253 */     Set<String> aliases = new HashSet();
/* 254 */     duplicateGrokAliasesExist(grok, aliases);
/* 255 */     return aliases;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<String> getJavaRegexGroupAliases(String regex)
/*     */   {
/* 265 */     Set<String> aliases = new HashSet();
/* 266 */     duplicateJavaRegexAliasesExist(regex, aliases);
/* 267 */     return aliases;
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
/*     */   static boolean duplicateAliasesExist(String definition, Matcher reusableMatcher, Set<String> aliases)
/*     */   {
/* 280 */     String candidate = definition;
/*     */     
/* 282 */     boolean duplicatesExist = false;
/* 283 */     while (candidate != null) {
/* 284 */       reusableMatcher.reset(candidate);
/* 285 */       if (!reusableMatcher.find()) break;
/* 286 */       String alias = reusableMatcher.group("alias");
/*     */       
/*     */ 
/* 289 */       candidate = reusableMatcher.group("lineSuffix");
/*     */       
/* 291 */       if (!aliases.add(alias)) {
/* 292 */         log.warn("Found duplicate alias [{}]", alias);
/* 293 */         duplicatesExist = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 300 */     return duplicatesExist;
/*     */   }
/*     */   
/*     */   static boolean isGrokComment(String trimmedLine) {
/* 304 */     return (trimmedLine.length() == 0) || (trimmedLine.startsWith("#"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static GrokLineAst parseNonComment(String trimmedLine, Set<String> patternNames)
/*     */   {
/* 313 */     int splitAt = trimmedLine.indexOf(" ");
/* 314 */     if (splitAt == -1) {
/* 315 */       log.warn("Line [{}] does not appear to be in the expected format:\n   [pattern-name[:type] (literal? / regex? / pattern-name-reference? (%{that-pattern-name:alias}))+]", trimmedLine);
/*     */       
/* 317 */       return null;
/*     */     }
/*     */     
/* 320 */     String patternNameAndTypeStr = trimmedLine.substring(0, splitAt);
/* 321 */     Pair<String, GrokValueType> patternNameAndType = parsePatternNameAndType(patternNameAndTypeStr);
/*     */     
/* 323 */     GrokValueType suggestedValueType = (GrokValueType)patternNameAndType.getRight();
/* 324 */     LinkedList<GrokLineAst.GrokLineAstItem> grokLineAstItems = new LinkedList();
/* 325 */     Matcher reusableMatcher = PATTERN_GROK_LINE.matcher("");
/* 326 */     String candidate = trimmedLine.substring(patternNameAndTypeStr.length() + " ".length());
/*     */     
/* 328 */     while (!Strings.isNullOrEmpty(candidate)) {
/* 329 */       reusableMatcher.reset(candidate);
/* 330 */       boolean matched = false;
/* 331 */       if (reusableMatcher.find())
/*     */       {
/*     */ 
/*     */ 
/* 335 */         String optLinePrefix = reusableMatcher.group("linePrefix");
/* 336 */         String patternReference = reusableMatcher.group("patternReference");
/*     */         
/* 338 */         String optAlias = reusableMatcher.group("alias");
/*     */         
/* 340 */         String optLineSuffix = reusableMatcher.group("lineSuffix");
/* 341 */         if (log.isTraceEnabled()) {
/* 342 */           log.trace("Parsed line\n    Prefix: [{}]\n    Pattern ref: [{}], alias: [{}]\n    Suffix: [{}]", new Object[] { optLinePrefix, patternReference, optAlias, optLineSuffix });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 351 */         if (!Strings.isNullOrEmpty(optLinePrefix)) {
/* 352 */           grokLineAstItems.add(new GrokLineAst.Literal(optLinePrefix));
/*     */         }
/*     */         
/*     */ 
/* 356 */         if ((patternNames != null) && (log.isTraceEnabled()) && (!patternNames.contains(patternReference))) {
/* 357 */           log.trace("Pattern reference [{}] could not be resolved", patternReference);
/*     */         }
/* 359 */         grokLineAstItems.add(new GrokLineAst.PatternRef(patternReference, Optional.fromNullable(optAlias)));
/*     */         
/*     */ 
/* 362 */         candidate = optLineSuffix;
/* 363 */         matched = true;
/*     */       }
/* 365 */       if (!matched)
/*     */       {
/* 367 */         grokLineAstItems.add(new GrokLineAst.Literal(candidate));
/* 368 */         break;
/*     */       }
/*     */     }
/*     */     
/* 372 */     GrokLineAst.GrokLineAstItem[] array = (GrokLineAst.GrokLineAstItem[])grokLineAstItems.toArray(new GrokLineAst.GrokLineAstItem[grokLineAstItems.size()]);
/* 373 */     return new GrokLineAst((String)patternNameAndType.getLeft(), suggestedValueType, array);
/*     */   }
/*     */   
/*     */   static Pair<String, GrokValueType> parsePatternNameAndType(String patternNameAndOptType) {
/* 377 */     String patternName = null;
/* 378 */     GrokValueType patternValueType = GrokValueType.UNKNOWN;
/*     */     
/* 380 */     Matcher patternNameMatcher = PATTERN_NAME.matcher(patternNameAndOptType);
/* 381 */     if (patternNameMatcher.matches()) {
/* 382 */       patternName = patternNameMatcher.group("patternName");
/* 383 */       String valueType = patternNameMatcher.group("patternValueType");
/* 384 */       if (valueType != null) {
/*     */         try {
/* 386 */           patternValueType = GrokValueType.valueOf(valueType.toUpperCase());
/*     */         } catch (IllegalArgumentException e) {
/* 388 */           throw new ConfigurationException(String.format("The value type [%s] is not recognized. Valid values are [%s]", new Object[] { valueType, Joiner.on('|').join(GrokValueType.VALID_VALUES) }), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 394 */       throw new ConfigurationException("The pattern name [" + patternNameAndOptType + "] does not follow" + " the correct syntax 'name[:" + Joiner.on('|').join(GrokValueType.VALID_VALUES) + "]'");
/*     */     }
/*     */     
/*     */ 
/* 398 */     return new Pair(patternName, patternValueType);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/grok/GrokParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */