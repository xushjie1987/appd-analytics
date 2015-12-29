/*    */ package com.appdynamics.analytics.agent.pipeline.file;
/*    */ 
/*    */ import com.appdynamics.common.io.file.FilePathConfiguration;
/*    */ import javax.validation.constraints.Min;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TailStageConfiguration
/*    */   extends FilePathConfiguration
/*    */ {
/* 17 */   public String toString() { return "TailStageConfiguration(pollMillis=" + getPollMillis() + ", startAtEnd=" + isStartAtEnd() + ")"; }
/* 18 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof TailStageConfiguration)) return false; TailStageConfiguration other = (TailStageConfiguration)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; if (getPollMillis() != other.getPollMillis()) return false; return isStartAtEnd() == other.isStartAtEnd(); } public boolean canEqual(Object other) { return other instanceof TailStageConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();long $pollMillis = getPollMillis();result = result * 31 + (int)($pollMillis >>> 32 ^ $pollMillis);result = result * 31 + (isStartAtEnd() ? 1231 : 1237);return result; }
/*    */   @Min(1L)
/* 20 */   long pollMillis = 2000L;
/* 21 */   public long getPollMillis() { return this.pollMillis; } public void setPollMillis(long pollMillis) { this.pollMillis = pollMillis; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 26 */   public boolean isStartAtEnd() { return this.startAtEnd; } public void setStartAtEnd(boolean startAtEnd) { this.startAtEnd = startAtEnd; } boolean startAtEnd = false;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/file/TailStageConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */