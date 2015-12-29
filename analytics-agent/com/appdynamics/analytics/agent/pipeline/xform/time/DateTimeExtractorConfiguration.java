/*    */ package com.appdynamics.analytics.agent.pipeline.xform.time;
/*    */ 
/*    */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*    */ import com.appdynamics.common.util.configuration.ManualValidateable;
/*    */ import com.appdynamics.common.util.configuration.ManualValidated;
/*    */ import com.appdynamics.common.util.configuration.Parameters;
/*    */ import com.appdynamics.common.util.datetime.FastDateTimeParser;
/*    */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*    */ import javax.validation.ConstraintValidatorContext;
/*    */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
/*    */ import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
/*    */ import org.joda.time.format.DateTimeFormat;
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ 
/*    */ @ManualValidated
/*    */ public class DateTimeExtractorConfiguration
/*    */   implements ManualValidateable
/*    */ {
/*    */   String source;
/*    */   String pattern;
/*    */   String destination;
/*    */   @JsonIgnore
/*    */   FastDateTimeParser dateTimeParser;
/*    */   
/*    */   public String toString()
/*    */   {
/* 27 */     return "DateTimeExtractorConfiguration(source=" + getSource() + ", pattern=" + getPattern() + ", destination=" + getDestination() + ")";
/*    */   }
/*    */   
/* 30 */   public String getSource() { return this.source; }
/* 31 */   public void setSource(String source) { this.source = source; }
/*    */   
/*    */ 
/* 34 */   public String getPattern() { return this.pattern; }
/* 35 */   public void setPattern(String pattern) { this.pattern = pattern; }
/*    */   
/*    */ 
/* 38 */   public String getDestination() { return this.destination; }
/* 39 */   public void setDestination(String destination) { this.destination = destination; }
/*    */   
/*    */   public FastDateTimeParser getDateTimeParser()
/*    */   {
/* 43 */     return this.dateTimeParser;
/*    */   }
/*    */   
/*    */   public boolean validate(ConstraintValidatorContext context)
/*    */   {
/*    */     try {
/* 49 */       Parameters.firstNonNull(this.source, this.pattern, this.destination);
/*    */     } catch (NullPointerException e) {
/* 51 */       context.buildConstraintViolationWithTemplate("At least one field must be set").addPropertyNode("source / pattern / destination").addConstraintViolation();
/*    */       
/*    */ 
/* 54 */       return false;
/*    */     }
/* 56 */     return true;
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
/*    */   public FastDateTimeParser mergeAndValidate(DateTimeExtractorConfiguration defaults)
/*    */   {
/* 69 */     if (this.source == null) {
/* 70 */       this.source = defaults.source;
/*    */     }
/* 72 */     if (this.pattern == null) {
/* 73 */       this.pattern = defaults.pattern;
/*    */     }
/* 75 */     if (this.destination == null) {
/* 76 */       this.destination = defaults.destination;
/*    */     }
/*    */     
/* 79 */     Parameters.asMandatoryString(this.source, "Field [source] is mandatory");
/* 80 */     Parameters.asMandatoryString(this.destination, "Field [destination] is mandatory");
/* 81 */     this.pattern = Parameters.defaultIfBlank(this.pattern, null);
/*    */     
/* 83 */     if (this.pattern != null) {
/*    */       try {
/* 85 */         DateTimeFormatter formatter = DateTimeFormat.forPattern(this.pattern);
/* 86 */         return this.dateTimeParser = new FastDateTimeParser(formatter);
/*    */       } catch (IllegalArgumentException e) {
/* 88 */         throw new ConfigurationException("Error occurred while preparing the date time extractor. The pattern [" + this.pattern + "] appears to be invalid", e);
/*    */       }
/*    */     }
/*    */     
/* 92 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/time/DateTimeExtractorConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */