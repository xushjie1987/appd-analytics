/*     */ package com.appdynamics.common.util.grok;
/*     */ 
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
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
/*     */ class GrokLineAst
/*     */ {
/*  28 */   public String toString() { return "GrokLineAst(name=" + getName() + ", suggestedValueType=" + getSuggestedValueType() + ", values=" + Arrays.deepToString(getValues()) + ")"; }
/*  29 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof GrokLineAst)) return false; GrokLineAst other = (GrokLineAst)o; if (!other.canEqual(this)) return false; Object this$name = getName();Object other$name = other.getName(); if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false; Object this$suggestedValueType = getSuggestedValueType();Object other$suggestedValueType = other.getSuggestedValueType(); if (this$suggestedValueType == null ? other$suggestedValueType != null : !this$suggestedValueType.equals(other$suggestedValueType)) return false; return Arrays.deepEquals(getValues(), other.getValues()); } public boolean canEqual(Object other) { return other instanceof GrokLineAst; } public int hashCode() { int PRIME = 31;int result = 1;Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());Object $suggestedValueType = getSuggestedValueType();result = result * 31 + ($suggestedValueType == null ? 0 : $suggestedValueType.hashCode());result = result * 31 + Arrays.deepHashCode(getValues());return result; }
/*  30 */   private static final Logger log = LoggerFactory.getLogger(GrokLineAst.class);
/*     */   
/*  32 */   public String getName() { return this.name; }
/*     */   
/*  34 */   public GrokValueType getSuggestedValueType() { return this.suggestedValueType; }
/*     */   
/*  36 */   public GrokLineAstItem[] getValues() { return this.values; }
/*     */   
/*     */   GrokLineAst(String name, GrokLineAstItem... values)
/*     */   {
/*  40 */     this(name, GrokValueType.UNKNOWN, values);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   GrokLineAst(String name, GrokValueType suggestedValueType, GrokLineAstItem... values)
/*     */   {
/*  50 */     this.name = name;
/*  51 */     this.suggestedValueType = suggestedValueType;
/*  52 */     this.values = values;
/*     */   }
/*     */   
/*     */   GrokLine resolve(Map<String, GrokLineAst> asts)
/*     */   {
/*  57 */     return resolve(ImmutableMap.of(), asts);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final String name;
/*     */   
/*     */ 
/*     */   final GrokValueType suggestedValueType;
/*     */   
/*     */ 
/*     */   final GrokLineAstItem[] values;
/*     */   
/*     */ 
/*     */   GrokLine resolve(Map<String, GrokLine> primary, Map<String, GrokLineAst> fallback)
/*     */   {
/*  73 */     GrokLineBuilder scratchpad = new GrokLineBuilder();
/*     */     
/*  75 */     for (GrokLineAstItem value : this.values) {
/*  76 */       if ((value instanceof Literal)) {
/*  77 */         scratchpad.appendPartial((Literal)value);
/*  78 */       } else if ((value instanceof PatternRef)) {
/*  79 */         PatternRef from = (PatternRef)value;
/*  80 */         String patternName = from.getPatternName();
/*  81 */         GrokLine referencedGrokLine = (GrokLine)primary.get(patternName);
/*  82 */         if (referencedGrokLine == null) {
/*  83 */           GrokLineAst referencedGrokLineAst = (GrokLineAst)fallback.get(patternName);
/*  84 */           if (referencedGrokLineAst == null) {
/*  85 */             throw new NullPointerException("There is a reference to a non-existent pattern [" + patternName + "] from [" + this.name + "]");
/*     */           }
/*     */           
/*  88 */           referencedGrokLine = referencedGrokLineAst.resolve(primary, fallback);
/*     */         }
/*     */         
/*  91 */         scratchpad.appendPartial(from.getAliasJavaCompliant(), referencedGrokLine);
/*     */       } else {
/*  93 */         throw new IllegalArgumentException("Unrecognized type [" + value.getClass().getName() + ']');
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  99 */     return scratchpad.build(this.suggestedValueType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static abstract class GrokLineAstItem {}
/*     */   
/*     */ 
/*     */   static class PatternRef
/*     */     extends GrokLineAst.GrokLineAstItem
/*     */   {
/*     */     final String patternName;
/*     */     final Optional<String> alias;
/*     */     final Optional<String> aliasJavaCompliant;
/*     */     
/* 114 */     public String toString() { return "GrokLineAst.PatternRef(patternName=" + getPatternName() + ", alias=" + getAlias() + ", aliasJavaCompliant=" + getAliasJavaCompliant() + ")"; }
/*     */     
/* 116 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof PatternRef)) return false; PatternRef other = (PatternRef)o; if (!other.canEqual(this)) return false; Object this$patternName = getPatternName();Object other$patternName = other.getPatternName(); if (this$patternName == null ? other$patternName != null : !this$patternName.equals(other$patternName)) return false; Object this$alias = getAlias();Object other$alias = other.getAlias(); if (this$alias == null ? other$alias != null : !this$alias.equals(other$alias)) return false; Object this$aliasJavaCompliant = getAliasJavaCompliant();Object other$aliasJavaCompliant = other.getAliasJavaCompliant();return this$aliasJavaCompliant == null ? other$aliasJavaCompliant == null : this$aliasJavaCompliant.equals(other$aliasJavaCompliant); } public boolean canEqual(Object other) { return other instanceof PatternRef; } public int hashCode() { int PRIME = 31;int result = 1;Object $patternName = getPatternName();result = result * 31 + ($patternName == null ? 0 : $patternName.hashCode());Object $alias = getAlias();result = result * 31 + ($alias == null ? 0 : $alias.hashCode());Object $aliasJavaCompliant = getAliasJavaCompliant();result = result * 31 + ($aliasJavaCompliant == null ? 0 : $aliasJavaCompliant.hashCode());return result; }
/*     */     
/* 118 */     public String getPatternName() { return this.patternName; }
/* 119 */     public Optional<String> getAlias() { return this.alias; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Optional<String> getAliasJavaCompliant()
/*     */     {
/* 126 */       return this.aliasJavaCompliant;
/*     */     }
/*     */     
/* 129 */     PatternRef(String patternName) { this(patternName, Optional.absent()); }
/*     */     
/*     */ 
/*     */ 
/* 133 */     PatternRef(String patternName, String alias) { this(patternName, Optional.fromNullable(alias)); }
/*     */     
/*     */     PatternRef(String patternName, Optional<String> alias) {
/* 136 */       super();
/* 137 */       this.patternName = patternName;
/* 138 */       this.alias = alias;
/* 139 */       this.aliasJavaCompliant = Optional.fromNullable(alias.isPresent() ? RegexHelper.removePunctuations((String)alias.get()) : null);
/*     */     }
/*     */   }
/*     */   
/*     */   static class Literal
/*     */     extends GrokLineAst.GrokLineAstItem {
/*     */     final String value;
/*     */     
/* 147 */     public String toString() { return "GrokLineAst.Literal(value=" + getValue() + ")"; }
/*     */     
/* 149 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Literal)) return false; Literal other = (Literal)o; if (!other.canEqual(this)) return false; Object this$value = getValue();Object other$value = other.getValue();return this$value == null ? other$value == null : this$value.equals(other$value); } public boolean canEqual(Object other) { return other instanceof Literal; } public int hashCode() { int PRIME = 31;int result = 1;Object $value = getValue();result = result * 31 + ($value == null ? 0 : $value.hashCode());return result; }
/*     */     
/* 151 */     public String getValue() { return this.value; }
/*     */     
/* 153 */     Literal(String value) { super();
/* 154 */       this.value = value;
/*     */     }
/*     */   }
/*     */   
/*     */   private class GrokLineBuilder
/*     */   {
/*     */     private final StringBuilder stringBuilder;
/*     */     private final Map<String, GrokValueType> aliasAndTypes;
/*     */     private GrokValueType inferredValueType;
/*     */     
/*     */     GrokLineBuilder() {
/* 165 */       this.stringBuilder = new StringBuilder();
/* 166 */       this.aliasAndTypes = new HashMap();
/*     */     }
/*     */     
/*     */     void appendPartial(GrokLineAst.Literal literal) {
/* 170 */       this.stringBuilder.append(literal.getValue());
/* 171 */       appendPartial(GrokValueType.UNKNOWN);
/*     */     }
/*     */     
/*     */     void appendPartial(Optional<String> alias, GrokLine grokLine)
/*     */     {
/* 176 */       if (alias.isPresent()) {
/* 177 */         this.stringBuilder.append("(?<").append((String)alias.get()).append('>');
/* 178 */         if (this.aliasAndTypes.containsKey(alias.get())) {
/* 179 */           throw new ConfigurationException("The alias [" + (String)alias.get() + "] has already been defined in the pattern");
/*     */         }
/*     */         
/* 182 */         this.aliasAndTypes.put(alias.get(), grokLine.getEffectiveType());
/*     */       }
/* 184 */       this.stringBuilder.append(grokLine.getPatternString());
/* 185 */       if (alias.isPresent()) {
/* 186 */         this.stringBuilder.append(')');
/*     */       }
/*     */       
/* 189 */       appendPartial(grokLine.getEffectiveType());
/*     */     }
/*     */     
/*     */     void appendPartial(GrokValueType valueType) {
/* 193 */       if (this.inferredValueType == null) {
/* 194 */         this.inferredValueType = valueType;
/*     */       } else {
/* 196 */         GrokValueType newInferredType = this.inferredValueType.and(valueType);
/* 197 */         if ((this.inferredValueType != newInferredType) && (GrokLineAst.log.isDebugEnabled())) {
/* 198 */           GrokLineAst.log.debug("Grok line [{}] was considered to be of type [{}] but with the addition of [{}] it will now be treated as [{}]", new Object[] { GrokLineAst.this.name, this.inferredValueType, valueType, newInferredType });
/*     */         }
/*     */         
/*     */ 
/* 202 */         this.inferredValueType = newInferredType;
/*     */       }
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
/*     */     GrokLine build(GrokValueType overrideValueType)
/*     */     {
/* 230 */       GrokValueType finalValueType = overrideValueType;
/*     */       
/*     */ 
/* 233 */       if ((overrideValueType == GrokValueType.UNKNOWN) && (this.inferredValueType != null) && (overrideValueType != this.inferredValueType))
/*     */       {
/* 235 */         GrokLineAst.log.debug("It could be inferred that the type is [{}] for pattern [{}]", this.inferredValueType, GrokLineAst.this.name);
/* 236 */         finalValueType = this.inferredValueType;
/*     */       }
/*     */       
/* 239 */       return new GrokLine(GrokLineAst.this.name, this.stringBuilder.toString(), this.aliasAndTypes, finalValueType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/grok/GrokLineAst.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */