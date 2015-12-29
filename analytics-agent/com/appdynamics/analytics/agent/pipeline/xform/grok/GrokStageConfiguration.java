/*    */ package com.appdynamics.analytics.agent.pipeline.xform.grok;
/*    */ 
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.configuration.ManualValidateable;
/*    */ import com.appdynamics.common.util.configuration.ManualValidated;
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Strings;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
/*    */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
/*    */ 
/*    */ @ManualValidated
/*    */ public class GrokStageConfiguration implements ManualValidateable
/*    */ {
/*    */   String source;
/*    */   @Deprecated
/*    */   String pattern;
/*    */   List<String> patterns;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 24 */     if (o == this) return true; if (!(o instanceof GrokStageConfiguration)) return false; GrokStageConfiguration other = (GrokStageConfiguration)o; if (!other.canEqual(this)) return false; Object this$source = getSource();Object other$source = other.getSource(); if (this$source == null ? other$source != null : !this$source.equals(other$source)) return false; Object this$pattern = getPattern();Object other$pattern = other.getPattern(); if (this$pattern == null ? other$pattern != null : !this$pattern.equals(other$pattern)) return false; Object this$patterns = getPatterns();Object other$patterns = other.getPatterns();return this$patterns == null ? other$patterns == null : this$patterns.equals(other$patterns); } public boolean canEqual(Object other) { return other instanceof GrokStageConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $source = getSource();result = result * 31 + ($source == null ? 0 : $source.hashCode());Object $pattern = getPattern();result = result * 31 + ($pattern == null ? 0 : $pattern.hashCode());Object $patterns = getPatterns();result = result * 31 + ($patterns == null ? 0 : $patterns.hashCode());return result; } public String toString() { return "GrokStageConfiguration(source=" + getSource() + ", pattern=" + getPattern() + ", patterns=" + getPatterns() + ")"; }
/*    */   
/*    */ 
/* 27 */   public String getSource() { return this.source; } public void setSource(String source) { this.source = source; }
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
/*    */   @Deprecated
/* 40 */   public String getPattern() { return this.pattern; } @Deprecated
/* 40 */   public void setPattern(String pattern) { this.pattern = pattern; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 48 */   public List<String> getPatterns() { return this.patterns; } public void setPatterns(List<String> patterns) { this.patterns = patterns; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean validate(ConstraintValidatorContext context)
/*    */   {
/* 60 */     if ((Strings.isNullOrEmpty(this.pattern)) && ((this.patterns == null) || (this.patterns.isEmpty()))) {
/* 61 */       context.buildConstraintViolationWithTemplate("At least one pattern must be defined").addPropertyNode("pattern / patterns").addConstraintViolation();
/*    */       
/*    */ 
/* 64 */       return false;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 69 */     List<String> allPatterns = new ArrayList();
/* 70 */     if ((this.patterns != null) && (!this.patterns.isEmpty())) {
/* 71 */       allPatterns.addAll(this.patterns);
/*    */     }
/*    */     
/* 74 */     if (!Strings.isNullOrEmpty(this.pattern)) {
/* 75 */       allPatterns.add(this.pattern);
/*    */     }
/*    */     
/* 78 */     if (com.appdynamics.common.util.grok.GrokParser.duplicateAliasesExist(allPatterns)) {
/* 79 */       context.buildConstraintViolationWithTemplate("Duplicate fields defined in patterns").addPropertyNode("pattern / patterns").addConstraintViolation();
/*    */       
/*    */ 
/* 82 */       return false;
/*    */     }
/*    */     
/* 85 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String mergeSourceAndValidate(String thatOptSource)
/*    */   {
/*    */     try
/*    */     {
/* 95 */       this.source = ((String)Objects.firstNonNull(this.source, thatOptSource));
/*    */     } catch (NullPointerException e) {
/* 97 */       throw new ConfigurationException("The [source] field has not been specified", e);
/*    */     }
/* 99 */     return this.source;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/grok/GrokStageConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */