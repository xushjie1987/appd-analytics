/*    */ package com.appdynamics.analytics.agent.pipeline.dynamic;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.pipeline.xform.grok.GrokStageConfiguration;
/*    */ import com.appdynamics.analytics.agent.pipeline.xform.text.MatchType;
/*    */ import com.appdynamics.analytics.agent.pipeline.xform.time.DateTimeExtractorConfiguration;
/*    */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.Min;
/*    */ import javax.validation.constraints.NotNull;
/*    */ import javax.validation.constraints.Size;
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
/*    */ public class DslConfiguration
/*    */ {
/*    */   public boolean equals(Object o)
/*    */   {
/* 27 */     if (o == this) return true; if (!(o instanceof DslConfiguration)) return false; DslConfiguration other = (DslConfiguration)o; if (!other.canEqual(this)) return false; if (getVersion() != other.getVersion()) return false; if (isEnabled() != other.isEnabled()) return false; if (isStartAtEnd() != other.isStartAtEnd()) return false; Object this$file = getFile();Object other$file = other.getFile(); if (this$file == null ? other$file != null : !this$file.equals(other$file)) return false; Object this$multiline = getMultiline();Object other$multiline = other.getMultiline(); if (this$multiline == null ? other$multiline != null : !this$multiline.equals(other$multiline)) return false; Object this$fields = getFields();Object other$fields = other.getFields(); if (this$fields == null ? other$fields != null : !this$fields.equals(other$fields)) return false; Object this$grok = getGrok();Object other$grok = other.getGrok(); if (this$grok == null ? other$grok != null : !this$grok.equals(other$grok)) return false; Object this$eventTimestamp = getEventTimestamp();Object other$eventTimestamp = other.getEventTimestamp();return this$eventTimestamp == null ? other$eventTimestamp == null : this$eventTimestamp.equals(other$eventTimestamp); } public boolean canEqual(Object other) { return other instanceof DslConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + getVersion();result = result * 31 + (isEnabled() ? 1231 : 1237);result = result * 31 + (isStartAtEnd() ? 1231 : 1237);Object $file = getFile();result = result * 31 + ($file == null ? 0 : $file.hashCode());Object $multiline = getMultiline();result = result * 31 + ($multiline == null ? 0 : $multiline.hashCode());Object $fields = getFields();result = result * 31 + ($fields == null ? 0 : $fields.hashCode());Object $grok = getGrok();result = result * 31 + ($grok == null ? 0 : $grok.hashCode());Object $eventTimestamp = getEventTimestamp();result = result * 31 + ($eventTimestamp == null ? 0 : $eventTimestamp.hashCode());return result; } public String toString() { return "DslConfiguration(version=" + getVersion() + ", enabled=" + isEnabled() + ", startAtEnd=" + isStartAtEnd() + ", file=" + getFile() + ", multiline=" + getMultiline() + ", fields=" + getFields() + ", grok=" + getGrok() + ", eventTimestamp=" + getEventTimestamp() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */   @Min(1L)
/* 32 */   int version = 1;
/* 33 */   public int getVersion() { return this.version; } public void setVersion(int version) { this.version = version; }
/* 34 */   public boolean isEnabled() { return this.enabled; } public void setEnabled(boolean enabled) { this.enabled = enabled; } boolean enabled = true;
/* 35 */   public boolean isStartAtEnd() { return this.startAtEnd; } public void setStartAtEnd(boolean startAtEnd) { this.startAtEnd = startAtEnd; } boolean startAtEnd = false;
/*    */   
/*    */ 
/* 38 */   public FilePathConfiguration getFile() { return this.file; } public void setFile(FilePathConfiguration file) { this.file = file; }
/*    */   
/*    */ 
/* 41 */   public EnumMap<MatchType, String> getMultiline() { return this.multiline; } public void setMultiline(EnumMap<MatchType, String> multiline) { this.multiline = multiline; }
/*    */   
/* 43 */   public Map<String, String> getFields() { return this.fields; } public void setFields(Map<String, String> fields) { this.fields = fields; }
/*    */   
/* 45 */   public GrokStageConfiguration getGrok() { return this.grok; } public void setGrok(GrokStageConfiguration grok) { this.grok = grok; }
/*    */   
/*    */ 
/* 48 */   public DateTimeExtractorConfiguration getEventTimestamp() { return this.eventTimestamp; } public void setEventTimestamp(DateTimeExtractorConfiguration eventTimestamp) { this.eventTimestamp = eventTimestamp; }
/*    */   
/*    */   @NotNull
/*    */   @Valid
/*    */   FilePathConfiguration file;
/*    */   @Size(max=1)
/*    */   EnumMap<MatchType, String> multiline;
/*    */   Map<String, String> fields;
/*    */   GrokStageConfiguration grok;
/*    */   @Valid
/*    */   DateTimeExtractorConfiguration eventTimestamp;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/dynamic/DslConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */