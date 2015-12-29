/*    */ package com.appdynamics.analytics.agent.pipeline.xform.field;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class FieldAdditionStageFactoryConfiguration
/*    */ {
/* 18 */   public String toString() { return "FieldAdditionStageFactoryConfiguration(fields=" + getFields() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $fields = getFields();result = result * 31 + ($fields == null ? 0 : $fields.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof FieldAdditionStageFactoryConfiguration; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof FieldAdditionStageFactoryConfiguration)) return false; FieldAdditionStageFactoryConfiguration other = (FieldAdditionStageFactoryConfiguration)o; if (!other.canEqual(this)) return false; Object this$fields = getFields();Object other$fields = other.getFields();return this$fields == null ? other$fields == null : this$fields.equals(other$fields); }
/*    */   @NotNull
/* 20 */   Map<String, Object> fields = new HashMap();
/* 21 */   public void setFields(Map<String, Object> fields) { this.fields = fields; } public Map<String, Object> getFields() { return this.fields; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/field/FieldAdditionStageFactoryConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */