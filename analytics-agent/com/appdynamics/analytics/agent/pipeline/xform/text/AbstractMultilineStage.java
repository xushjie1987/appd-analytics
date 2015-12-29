/*     */ package com.appdynamics.analytics.agent.pipeline.xform.text;
/*     */ 
/*     */ import com.appdynamics.analytics.pipeline.api.PipelineStageParameters;
/*     */ import com.appdynamics.analytics.pipeline.util.AbstractPipelineStage;
/*     */ import com.appdynamics.common.util.configuration.ConfigurationException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
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
/*     */ abstract class AbstractMultilineStage<O>
/*     */   extends AbstractPipelineStage<CharSequence, O>
/*     */ {
/*  31 */   private static final Logger log = LoggerFactory.getLogger(AbstractMultilineStage.class);
/*     */   
/*     */   final MatchType matchType;
/*     */   
/*     */   final String matchValue;
/*     */   final MatchAction matchAction;
/*     */   final MatchAction noMatchAction;
/*     */   final String lineSeparator;
/*     */   final int bufferSizeWarnAbove;
/*     */   private final Pattern optRegexPattern;
/*     */   private final Matcher optReusableMatcher;
/*     */   private final StringBuilder reusableBuilder;
/*     */   
/*     */   protected AbstractMultilineStage(PipelineStageParameters<O> parameters, MatchType matchType, String matchValue, MatchAction matchAction, String lineSeparator, int bufferSizeWarnAbove)
/*     */   {
/*  46 */     super(parameters);
/*  47 */     this.matchType = matchType;
/*  48 */     this.matchValue = matchValue;
/*  49 */     this.matchAction = matchAction;
/*  50 */     this.noMatchAction = matchAction.inverse();
/*  51 */     this.lineSeparator = lineSeparator;
/*  52 */     this.bufferSizeWarnAbove = bufferSizeWarnAbove;
/*  53 */     this.reusableBuilder = new StringBuilder();
/*     */     
/*  55 */     switch (matchType) {
/*     */     case regex: 
/*     */       try {
/*  58 */         this.optRegexPattern = Pattern.compile(matchValue);
/*     */       } catch (PatternSyntaxException e) {
/*  60 */         throw new ConfigurationException("Error occurred while applying the regular expression [" + matchValue + "] at [" + printableName() + "]", e);
/*     */       }
/*     */       
/*  63 */       this.optReusableMatcher = this.optRegexPattern.matcher("");
/*  64 */       break;
/*     */     
/*     */     case startsWith: 
/*  67 */       this.optRegexPattern = null;
/*  68 */       this.optReusableMatcher = null;
/*  69 */       break;
/*     */     
/*     */     default: 
/*  72 */       throw new IllegalArgumentException("Unrecognized match type [" + matchType + "]");
/*     */     }
/*  74 */     switch (matchAction)
/*     */     {
/*     */     case NEW: 
/*     */     case APPEND: 
/*     */       break;
/*     */     default: 
/*  80 */       throw new IllegalArgumentException("Unrecognized match action [" + matchAction + "]");
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void process(CharSequence inputChars)
/*     */   {
/*  91 */     MatchAction action = attemptMatch(inputChars, this.matchType, this.matchAction, this.noMatchAction, this.matchValue, this.optReusableMatcher);
/*     */     
/*  93 */     if (log.isTraceEnabled()) {
/*  94 */       log.trace("Match attempt on [{}] yielded [{}] action", inputChars, action);
/*     */     }
/*     */     
/*  97 */     switch (action) {
/*     */     case APPEND: 
/*  99 */       if (this.reusableBuilder.length() > 0) {
/* 100 */         this.reusableBuilder.append(this.lineSeparator);
/*     */       }
/*     */       
/* 103 */       this.reusableBuilder.append(inputChars);
/* 104 */       int size = this.reusableBuilder.length();
/* 105 */       if (size > this.bufferSizeWarnAbove) {
/* 106 */         log.warn("The [{}] stage appears to have accumulated too many characters [{}]. If this was intentional then consider increasing the threshold, which is currently set to [{}]. First few accumulated characters [{}...]", new Object[] { printableName(), Integer.valueOf(size), Integer.valueOf(this.bufferSizeWarnAbove), this.reusableBuilder.substring(0, Math.min(96, size)) });
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       break;
/*     */     case FLUSH: 
/* 115 */       flush();
/* 116 */       break;
/*     */     
/*     */     case NEW: 
/* 119 */       flush();
/*     */       
/* 121 */       this.reusableBuilder.append(inputChars);
/* 122 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void flush()
/*     */   {
/* 131 */     if (this.reusableBuilder.length() > 0) {
/* 132 */       O oldText = extractForNextStage(this.reusableBuilder);
/*     */       
/* 134 */       invokeNext(oldText);
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
/*     */   abstract O extractForNextStage(StringBuilder paramStringBuilder);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static MatchAction attemptMatch(CharSequence inputChars, MatchType matchType, MatchAction matchAction, MatchAction noMatchAction, String matchValue, Matcher optReusableMatcher)
/*     */   {
/* 156 */     if (inputChars == null) {
/* 157 */       return MatchAction.FLUSH;
/*     */     }
/* 159 */     MatchAction action = noMatchAction;
/* 160 */     switch (matchType) {
/*     */     case regex: 
/* 162 */       optReusableMatcher.reset(inputChars);
/* 163 */       if (optReusableMatcher.matches()) {
/* 164 */         action = matchAction;
/*     */       }
/* 166 */       optReusableMatcher.reset();
/* 167 */       break;
/*     */     
/*     */     case startsWith: 
/* 170 */       if ((inputChars instanceof String))
/*     */       {
/* 172 */         if (((String)inputChars).startsWith(matchValue)) {
/* 173 */           action = matchAction;
/*     */         }
/*     */       } else {
/* 176 */         int i = inputChars.length();
/* 177 */         int o = matchValue.length();
/* 178 */         if (i >= o) {
/* 179 */           for (int j = 0; 
/* 180 */               j < o; j++) {
/* 181 */             if (inputChars.charAt(j) != matchValue.charAt(j)) {
/*     */               break;
/*     */             }
/*     */           }
/* 185 */           if (j == o) {
/* 186 */             action = matchAction;
/*     */           }
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/* 192 */     return action;
/*     */   }
/*     */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/text/AbstractMultilineStage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */