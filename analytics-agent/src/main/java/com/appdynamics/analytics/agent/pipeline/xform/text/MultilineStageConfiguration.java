/*    */ package com.appdynamics.analytics.agent.pipeline.xform.text;
/*    */ 
/*    */ import javax.validation.constraints.Pattern;
/*    */ import javax.validation.constraints.Size;
/*    */ 
/*    */ public class MultilineStageConfiguration
/*    */ {
/*    */   public static final String MATCH_ACTION_NEW = "new";
/*    */   public static final String MATCH_ACTION_APPEND = "append";
/*    */   @Size(min=1)
/*    */   String regex;
/*    */   @Size(min=1)
/*    */   String startsWith;
/*    */   @javax.validation.constraints.NotNull
/*    */   @Pattern(regexp="new|append")
/*    */   String matchAction;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 20 */     if (o == this) return true; if (!(o instanceof MultilineStageConfiguration)) return false; MultilineStageConfiguration other = (MultilineStageConfiguration)o; if (!other.canEqual(this)) return false; Object this$regex = getRegex();Object other$regex = other.getRegex(); if (this$regex == null ? other$regex != null : !this$regex.equals(other$regex)) return false; Object this$startsWith = getStartsWith();Object other$startsWith = other.getStartsWith(); if (this$startsWith == null ? other$startsWith != null : !this$startsWith.equals(other$startsWith)) return false; Object this$matchAction = getMatchAction();Object other$matchAction = other.getMatchAction(); if (this$matchAction == null ? other$matchAction != null : !this$matchAction.equals(other$matchAction)) return false; return getBufferSizeWarnAboveChars() == other.getBufferSizeWarnAboveChars(); } public boolean canEqual(Object other) { return other instanceof MultilineStageConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $regex = getRegex();result = result * 31 + ($regex == null ? 0 : $regex.hashCode());Object $startsWith = getStartsWith();result = result * 31 + ($startsWith == null ? 0 : $startsWith.hashCode());Object $matchAction = getMatchAction();result = result * 31 + ($matchAction == null ? 0 : $matchAction.hashCode());result = result * 31 + getBufferSizeWarnAboveChars();return result; } public String toString() { return "MultilineStageConfiguration(regex=" + getRegex() + ", startsWith=" + getStartsWith() + ", matchAction=" + getMatchAction() + ", bufferSizeWarnAboveChars=" + getBufferSizeWarnAboveChars() + ")"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 26 */   public String getRegex() { return this.regex; } public void setRegex(String regex) { this.regex = regex; }
/*    */   
/*    */ 
/* 29 */   public String getStartsWith() { return this.startsWith; } public void setStartsWith(String startsWith) { this.startsWith = startsWith; }
/*    */   
/*    */ 
/*    */ 
/* 33 */   public String getMatchAction() { return this.matchAction; } public void setMatchAction(String matchAction) { this.matchAction = matchAction; }
/*    */   @javax.validation.constraints.Min(1L)
/* 35 */   int bufferSizeWarnAboveChars = 65536;
/* 36 */   public int getBufferSizeWarnAboveChars() { return this.bufferSizeWarnAboveChars; } public void setBufferSizeWarnAboveChars(int bufferSizeWarnAboveChars) { this.bufferSizeWarnAboveChars = bufferSizeWarnAboveChars; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/text/MultilineStageConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */