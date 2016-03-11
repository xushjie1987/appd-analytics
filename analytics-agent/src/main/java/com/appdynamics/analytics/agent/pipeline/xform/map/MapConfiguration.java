/*    */ package com.appdynamics.analytics.agent.pipeline.xform.map;
/*    */ 
/*    */ import javax.validation.constraints.NotNull;
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
/*    */ public class MapConfiguration
/*    */ {
/* 16 */   public String toString() { return "MapConfiguration(fieldName=" + getFieldName() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $fieldName = getFieldName();result = result * 31 + ($fieldName == null ? 0 : $fieldName.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof MapConfiguration; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof MapConfiguration)) return false; MapConfiguration other = (MapConfiguration)o; if (!other.canEqual(this)) return false; Object this$fieldName = getFieldName();Object other$fieldName = other.getFieldName();return this$fieldName == null ? other$fieldName == null : this$fieldName.equals(other$fieldName); }
/*    */   @NotNull
/* 18 */   String fieldName = "message";
/* 19 */   public void setFieldName(String fieldName) { this.fieldName = fieldName; } public String getFieldName() { return this.fieldName; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/map/MapConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */