/*    */ package com.appdynamics.analytics.agent.pipeline.dynamic;
/*    */ 
/*    */ import com.appdynamics.common.io.file.FilePollerConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LogSourceJobFileConfiguration
/*    */   extends FilePollerConfiguration
/*    */ {
/* 14 */   public String toString() { return "LogSourceJobFileConfiguration()"; }
/* 15 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof LogSourceJobFileConfiguration)) return false; LogSourceJobFileConfiguration other = (LogSourceJobFileConfiguration)o; if (!other.canEqual(this)) return false; return super.equals(o); } public boolean canEqual(Object other) { return other instanceof LogSourceJobFileConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();return result;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/dynamic/LogSourceJobFileConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */