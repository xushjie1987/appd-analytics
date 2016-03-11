/*     */ package com.appdynamics.common.util.var;
/*     */ 
/*     */ import com.appdynamics.common.io.file.FileSource;
/*     */ import com.appdynamics.common.util.configuration.Parameters;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.escape.CharEscaperBuilder;
/*     */ import com.google.common.escape.Escaper;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public abstract class Variables
/*     */ {
/*  41 */   private static final Logger log = LoggerFactory.getLogger(Variables.class);
/*     */   
/*  43 */   static final Escaper MATCHER_ESCAPER = new CharEscaperBuilder().addEscape('$', "\\$").addEscape('\\', "\\\\").toEscaper();
/*     */   
/*     */ 
/*     */   static final String VARIABLE_FRAGMENT = "[a-zA-Z_\\$][a-zA-Z_\\$0-9-]*";
/*     */   
/*     */ 
/*     */   static final String VARIABLE = "[a-zA-Z_\\$][a-zA-Z_\\$0-9-]*(?:\\.[a-zA-Z_\\$][a-zA-Z_\\$0-9-]*)*";
/*     */   
/*     */ 
/*     */   static final String VARIABLE_MARKER_START = "\\$\\{";
/*     */   
/*     */ 
/*     */   static final String VARIABLE_MARKER_END = "\\}";
/*     */   
/*     */ 
/*     */   public static final String VARIABLE_EXTENSION = "(?:\\(([^\\(\\)]+)\\))?";
/*     */   
/*     */   public static final String VARIABLE_SYNTAX = "\\$\\{([a-zA-Z_\\$][a-zA-Z_\\$0-9-]*(?:\\.[a-zA-Z_\\$][a-zA-Z_\\$0-9-]*)*)(?:\\(([^\\(\\)]+)\\))?\\}";
/*     */   
/*  62 */   public static final ChainedVariableResolver VARIABLE_RESOLVER_ONLY_SYSTEM = new ChainedVariableResolver(SystemPropertyResolver.INSTANCE, SystemVariableResolver.INSTANCE, new VariableResolver[] { EchoVariableResolver.INSTANCE });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  67 */   public static final ChainedVariableResolver VARIABLE_RESOLVER_SYSTEM_AND_INCLUDE = newResolverChainPlusCustomResolver(new VariableResolver[0]);
/*     */   
/*     */ 
/*  70 */   static final Pattern PATTERN_VARIABLE = Pattern.compile("\\$\\{([a-zA-Z_\\$][a-zA-Z_\\$0-9-]*(?:\\.[a-zA-Z_\\$][a-zA-Z_\\$0-9-]*)*)(?:\\(([^\\(\\)]+)\\))?\\}");
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
/*     */   public static String resolve(String stringWithVariables, VariableResolver variableResolver)
/*     */   {
/*  92 */     if (log.isTraceEnabled()) {
/*  93 */       log.trace("Attempting to resolve [{}]", stringWithVariables);
/*     */     }
/*     */     
/*  96 */     String oldInputString = null;
/*  97 */     String inputString = stringWithVariables;
/*     */     
/*  99 */     StringBuffer sb = null;
/*     */     for (;;) {
/* 101 */       Matcher matcher = PATTERN_VARIABLE.matcher(inputString);
/*     */       
/* 103 */       boolean replaced = false;
/* 104 */       while (matcher.find()) {
/* 105 */         String variable = matcher.group(1);
/* 106 */         String variableExt = matcher.group(2);
/*     */         
/* 108 */         Object value = null;
/* 109 */         if (variableExt == null) {
/* 110 */           value = variableResolver.resolve(variable);
/*     */         }
/*     */         else {
/* 113 */           String resolvedVariableExt = resolve(variableExt, variableResolver);
/* 114 */           value = variableResolver.resolve(variable, resolvedVariableExt);
/*     */         }
/*     */         
/* 117 */         if (sb == null) {
/* 118 */           sb = new StringBuffer(inputString.length());
/*     */         }
/* 120 */         String stringValue = asString(inputString, matcher.start(), variableResolver, value);
/*     */         
/*     */ 
/* 123 */         if (!inputString.equals(stringValue))
/*     */         {
/*     */           try {
/* 126 */             stringValue = MATCHER_ESCAPER.escape(stringValue);
/* 127 */             matcher.appendReplacement(sb, stringValue);
/*     */           } catch (IllegalArgumentException|IndexOutOfBoundsException e) {
/* 129 */             String msg = "Error occurred while resolving variable [" + variable + (variableExt == null ? "" : new StringBuilder().append(":").append(variableExt).toString()) + "] in string [" + stringWithVariables + "] with value [" + stringValue + "]";
/*     */             
/*     */ 
/*     */ 
/* 133 */             throw new IllegalArgumentException(msg, e);
/*     */           }
/* 135 */           replaced = true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 140 */       if (!replaced) {
/*     */         break;
/*     */       }
/* 143 */       matcher.appendTail(sb);
/* 144 */       oldInputString = inputString;
/* 145 */       inputString = sb.toString();
/* 146 */       sb.setLength(0);
/*     */       
/* 148 */       if (oldInputString.equals(inputString)) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 153 */     return inputString;
/*     */   }
/*     */   
/*     */   private static String asString(String inputString, int valueStartPosition, VariableResolver variableResolver, Object value)
/*     */   {
/* 158 */     if ((value instanceof Collection)) {
/* 159 */       if (log.isTraceEnabled()) {
/* 160 */         log.trace("Attempting to resolve collection [\n{}\n]", value);
/*     */       }
/*     */       
/*     */ 
/* 164 */       StringBuilder sb = new StringBuilder();
/*     */       
/* 166 */       for (int j = valueStartPosition - 1; j >= 0; j--) {
/* 167 */         char c = inputString.charAt(j);
/* 168 */         if (c != ' ') break;
/* 169 */         sb.append(c);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 174 */       String indent = sb.toString();
/*     */       
/* 176 */       sb.delete(0, indent.length());
/*     */       
/* 178 */       int i = 0;
/* 179 */       for (Object unresolvedItem : (Collection)value) {
/* 180 */         if (i > 0) {
/* 181 */           sb.append('\n').append(indent);
/*     */         }
/* 183 */         String unresolvedItemStr = Parameters.toStringOrBlank(unresolvedItem);
/*     */         
/* 185 */         String resolvedItem = resolve(unresolvedItemStr, variableResolver);
/* 186 */         sb.append(resolvedItem);
/* 187 */         i++;
/*     */       }
/* 189 */       return sb.toString();
/*     */     }
/*     */     
/*     */ 
/* 193 */     return Parameters.toStringOrBlank(value);
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
/*     */   public static Map<String, Object> resolveValues(String stringWithVariables, VariableResolver variableResolver)
/*     */   {
/* 208 */     Matcher matcher = PATTERN_VARIABLE.matcher(stringWithVariables);
/*     */     
/* 210 */     LinkedHashMap<String, Object> map = null;
/* 211 */     while (matcher.find()) {
/* 212 */       String variable = matcher.group(1);
/* 213 */       String variableExt = matcher.group(2);
/*     */       
/* 215 */       Object value = null;
/* 216 */       if (variableExt == null) {
/* 217 */         value = variableResolver.resolve(variable);
/*     */       } else {
/* 219 */         value = variableResolver.resolve(variable, variableExt);
/*     */       }
/*     */       
/* 222 */       if (map == null) {
/* 223 */         map = new LinkedHashMap();
/*     */       }
/* 225 */       map.put(variable, value);
/*     */     }
/*     */     
/* 228 */     return map;
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
/*     */   public static MapBasedVariableResolver loadVariables(Properties properties)
/*     */   {
/* 245 */     HashMap<String, String> map = new HashMap(properties.size());
/*     */     
/* 247 */     for (Map.Entry<Object, Object> entry : properties.entrySet()) {
/* 248 */       String key = (String)entry.getKey();
/* 249 */       key = key == null ? null : resolve(key, VARIABLE_RESOLVER_ONLY_SYSTEM);
/*     */       
/* 251 */       String value = (String)entry.getValue();
/* 252 */       value = value == null ? null : resolve(value, VARIABLE_RESOLVER_ONLY_SYSTEM);
/*     */       
/* 254 */       map.put(key, value);
/*     */     }
/*     */     
/* 257 */     return new MapBasedVariableResolver(map);
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
/*     */   public static void resolveVariables(InputStream templateSource, Properties optTemplateVariablesProps, OutputStream resolvedOutput)
/*     */     throws IOException
/*     */   {
/*     */     VariableResolver variableResolver;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 283 */     if (optTemplateVariablesProps != null) {
/* 284 */       VariableResolver variableResolver = loadVariables(optTemplateVariablesProps);
/* 285 */       variableResolver = newResolverChainPlusCustomResolver(new VariableResolver[] { variableResolver });
/*     */     } else {
/* 287 */       variableResolver = VARIABLE_RESOLVER_SYSTEM_AND_INCLUDE;
/*     */     }
/*     */     
/* 290 */     BufferedReader reader = new BufferedReader(new InputStreamReader(templateSource, "UTF-8"));Throwable localThrowable3 = null;
/* 291 */     try { PrintWriter writer = new PrintWriter(new OutputStreamWriter(resolvedOutput, "UTF-8"));Throwable localThrowable4 = null;
/*     */       try { String line;
/* 293 */         while ((line = reader.readLine()) != null) {
/*     */           try {
/* 295 */             line = resolve(line, variableResolver);
/*     */           } catch (RuntimeException e) {
/* 297 */             log.error("Error occurred while resolving variables in line [" + line + "]", e);
/* 298 */             throw e;
/*     */           }
/*     */           
/* 301 */           writer.println(line);
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 290 */         localThrowable4 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable2) { localThrowable3 = localThrowable2;throw localThrowable2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 303 */       if (reader != null) { if (localThrowable3 != null) try { reader.close(); } catch (Throwable x2) { localThrowable3.addSuppressed(x2); } else { reader.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static File resolveVariables(FileSource templateSource, String optVariableValuesPropFilePath)
/*     */     throws IOException
/*     */   {
/* 317 */     Properties optProperties = loadOptionalPropFile(optVariableValuesPropFilePath);
/*     */     
/* 319 */     File source = new File(templateSource.getSource());
/* 320 */     File destination = File.createTempFile("tmp-", "-" + source.getName());
/* 321 */     destination.deleteOnExit();
/* 322 */     FileInputStream fis = new FileInputStream(source);Throwable localThrowable3 = null;
/* 323 */     try { FileOutputStream fos = new FileOutputStream(destination);Throwable localThrowable4 = null;
/* 324 */       try { resolveVariables(fis, optProperties, fos);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 323 */         localThrowable4 = localThrowable1;throw localThrowable1;
/*     */       }
/*     */       finally {}
/*     */     }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 322 */       localThrowable3 = localThrowable2;throw localThrowable2;
/*     */     }
/*     */     finally
/*     */     {
/* 326 */       if (fis != null) if (localThrowable3 != null) try { fis.close(); } catch (Throwable x2) { localThrowable3.addSuppressed(x2); } else fis.close(); }
/* 327 */     return destination;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static File resolveVariables(String destinationFileSuffix, InputStream templateSource, String optVariableValuesPropFilePath)
/*     */     throws IOException
/*     */   {
/* 340 */     Properties optProperties = loadOptionalPropFile(optVariableValuesPropFilePath);
/*     */     
/* 342 */     File destination = File.createTempFile("tmp-", "-" + destinationFileSuffix);
/* 343 */     destination.deleteOnExit();
/*     */     try {
/* 345 */       FileOutputStream fos = new FileOutputStream(destination);Throwable localThrowable2 = null;
/* 346 */       try { resolveVariables(templateSource, optProperties, fos);
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 345 */         localThrowable2 = localThrowable1;throw localThrowable1;
/*     */       } finally {
/* 347 */         if (fos != null) if (localThrowable2 != null) try { fos.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else fos.close();
/*     */       }
/* 349 */     } finally { IOUtils.closeQuietly(templateSource);
/*     */     }
/* 351 */     return destination;
/*     */   }
/*     */   
/*     */   private static Properties loadOptionalPropFile(String optVariableValuesPropFilePath) {
/* 355 */     Properties optProperties = null;
/* 356 */     if (optVariableValuesPropFilePath != null) {
/* 357 */       File f = new File(optVariableValuesPropFilePath);
/* 358 */       Preconditions.checkArgument(f.exists(), "The properties file that is supposed to be located at [" + f.getAbsolutePath() + "] does not exist");
/*     */       
/* 360 */       optProperties = new Properties();
/* 361 */       try { FileInputStream fis = new FileInputStream(f);Throwable localThrowable2 = null;
/* 362 */         try { optProperties.load(fis);
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/* 361 */           localThrowable2 = localThrowable1;throw localThrowable1;
/*     */         } finally {
/* 363 */           if (fis != null) if (localThrowable2 != null) try { fis.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else fis.close();
/* 364 */         } } catch (IOException e) { Throwables.propagate(e);
/*     */       }
/*     */     }
/* 367 */     return optProperties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChainedVariableResolver newResolverChainPlusCustomResolver(VariableResolver... customResolvers)
/*     */   {
/* 379 */     VariableResolver[] resolvers = new VariableResolver[3 + customResolvers.length];
/* 380 */     int i = 0;
/* 381 */     for (VariableResolver cr : customResolvers) {
/* 382 */       resolvers[(i++)] = cr;
/*     */     }
/* 384 */     resolvers[(i++)] = FileIncludeVariableResolver.INSTANCE;
/* 385 */     resolvers[(i++)] = CalendarVariableResolver.INSTANCE;
/*     */     
/* 387 */     resolvers[i] = EchoVariableResolver.INSTANCE;
/* 388 */     return new ChainedVariableResolver(SystemPropertyResolver.INSTANCE, SystemVariableResolver.INSTANCE, resolvers);
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/Variables.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */