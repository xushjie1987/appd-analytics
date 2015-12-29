/*    */ package com.appdynamics.analytics.agent.pipeline.dynamic;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.pipeline.xform.grok.GrokStageConfiguration;
/*    */ import com.appdynamics.analytics.agent.pipeline.xform.text.MatchType;
/*    */ import com.appdynamics.analytics.agent.pipeline.xform.time.DateTimeExtractorConfiguration;
/*    */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*    */ import java.util.List;
/*    */ import javax.validation.Valid;
/*    */ 
/*    */ public class TemplateConfiguration
/*    */ {
/*    */   boolean enabled;
/*    */   boolean startAtEnd;
/*    */   @javax.validation.constraints.NotNull
/*    */   @Valid
/*    */   FilePathConfiguration file;
/*    */   @javax.validation.constraints.Size(max=1)
/*    */   List<Pair<MatchType, String>> multiline;
/*    */   List<Pair<String, String>> fields;
/*    */   GrokStageConfiguration grok;
/*    */   DateTimeExtractorConfiguration eventTimestamp;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 25 */     if (o == this) return true; if (!(o instanceof TemplateConfiguration)) return false; TemplateConfiguration other = (TemplateConfiguration)o; if (!other.canEqual(this)) return false; if (isEnabled() != other.isEnabled()) return false; if (isStartAtEnd() != other.isStartAtEnd()) return false; Object this$file = getFile();Object other$file = other.getFile(); if (this$file == null ? other$file != null : !this$file.equals(other$file)) return false; Object this$multiline = getMultiline();Object other$multiline = other.getMultiline(); if (this$multiline == null ? other$multiline != null : !this$multiline.equals(other$multiline)) return false; Object this$fields = getFields();Object other$fields = other.getFields(); if (this$fields == null ? other$fields != null : !this$fields.equals(other$fields)) return false; Object this$grok = getGrok();Object other$grok = other.getGrok(); if (this$grok == null ? other$grok != null : !this$grok.equals(other$grok)) return false; Object this$eventTimestamp = getEventTimestamp();Object other$eventTimestamp = other.getEventTimestamp();return this$eventTimestamp == null ? other$eventTimestamp == null : this$eventTimestamp.equals(other$eventTimestamp); } public boolean canEqual(Object other) { return other instanceof TemplateConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + (isEnabled() ? 1231 : 1237);result = result * 31 + (isStartAtEnd() ? 1231 : 1237);Object $file = getFile();result = result * 31 + ($file == null ? 0 : $file.hashCode());Object $multiline = getMultiline();result = result * 31 + ($multiline == null ? 0 : $multiline.hashCode());Object $fields = getFields();result = result * 31 + ($fields == null ? 0 : $fields.hashCode());Object $grok = getGrok();result = result * 31 + ($grok == null ? 0 : $grok.hashCode());Object $eventTimestamp = getEventTimestamp();result = result * 31 + ($eventTimestamp == null ? 0 : $eventTimestamp.hashCode());return result; } public String toString() { return "TemplateConfiguration(enabled=" + isEnabled() + ", startAtEnd=" + isStartAtEnd() + ", file=" + getFile() + ", multiline=" + getMultiline() + ", fields=" + getFields() + ", grok=" + getGrok() + ", eventTimestamp=" + getEventTimestamp() + ")"; }
/*    */   
/* 27 */   public boolean isEnabled() { return this.enabled; } public void setEnabled(boolean enabled) { this.enabled = enabled; }
/* 28 */   public boolean isStartAtEnd() { return this.startAtEnd; } public void setStartAtEnd(boolean startAtEnd) { this.startAtEnd = startAtEnd; }
/*    */   
/*    */ 
/* 31 */   public FilePathConfiguration getFile() { return this.file; } public void setFile(FilePathConfiguration file) { this.file = file; }
/*    */   
/*    */ 
/* 34 */   public List<Pair<MatchType, String>> getMultiline() { return this.multiline; } public void setMultiline(List<Pair<MatchType, String>> multiline) { this.multiline = multiline; }
/*    */   
/* 36 */   public List<Pair<String, String>> getFields() { return this.fields; } public void setFields(List<Pair<String, String>> fields) { this.fields = fields; }
/*    */   
/* 38 */   public GrokStageConfiguration getGrok() { return this.grok; } public void setGrok(GrokStageConfiguration grok) { this.grok = grok; }
/*    */   
/* 40 */   public DateTimeExtractorConfiguration getEventTimestamp() { return this.eventTimestamp; } public void setEventTimestamp(DateTimeExtractorConfiguration eventTimestamp) { this.eventTimestamp = eventTimestamp; }
/*    */   
/*    */   public static TemplateConfiguration from(DslConfiguration dsl) {
/* 43 */     TemplateConfiguration tc = new TemplateConfiguration();
/*    */     
/* 45 */     tc.setEnabled(dsl.isEnabled());
/* 46 */     tc.setStartAtEnd(dsl.isStartAtEnd());
/*    */     
/* 48 */     tc.setFile(dsl.getFile());
/*    */     
/* 50 */     if (dsl.getMultiline() != null) {
/* 51 */       List<Pair<MatchType, String>> multilinePairs = Pair.from(dsl.getMultiline());
/* 52 */       tc.setMultiline(multilinePairs);
/*    */     }
/*    */     
/* 55 */     if (dsl.getFields() != null) {
/* 56 */       List<Pair<String, String>> fields = Pair.from(dsl.getFields());
/* 57 */       tc.setFields(fields);
/*    */     }
/*    */     
/* 60 */     tc.setGrok(dsl.getGrok());
/*    */     
/* 62 */     tc.setEventTimestamp(dsl.getEventTimestamp());
/*    */     
/* 64 */     return tc;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/dynamic/TemplateConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */