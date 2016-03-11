/*    */ package com.appdynamics.analytics.agent.pipeline.xform.json;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapsToJsonStageConfiguration
/*    */ {
/* 13 */   public String toString() { return "MapsToJsonStageConfiguration(clearMaps=" + isClearMaps() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + (isClearMaps() ? 1231 : 1237);return result; } public boolean canEqual(Object other) { return other instanceof MapsToJsonStageConfiguration; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof MapsToJsonStageConfiguration)) return false; MapsToJsonStageConfiguration other = (MapsToJsonStageConfiguration)o; if (!other.canEqual(this)) return false; return isClearMaps() == other.isClearMaps(); }
/*    */   
/* 15 */   public void setClearMaps(boolean clearMaps) { this.clearMaps = clearMaps; } public boolean isClearMaps() { return this.clearMaps; } boolean clearMaps = false;
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/json/MapsToJsonStageConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */