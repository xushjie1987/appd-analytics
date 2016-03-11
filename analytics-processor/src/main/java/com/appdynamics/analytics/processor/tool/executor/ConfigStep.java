/*    */ package com.appdynamics.analytics.processor.tool.executor;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ConfigStep
/*    */ {
/*    */   JsonNode properties;
/*    */   String stepName;
/*    */   String className;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 16 */     if (o == this) return true; if (!(o instanceof ConfigStep)) return false; ConfigStep other = (ConfigStep)o; if (!other.canEqual(this)) return false; Object this$properties = getProperties();Object other$properties = other.getProperties(); if (this$properties == null ? other$properties != null : !this$properties.equals(other$properties)) return false; Object this$stepName = getStepName();Object other$stepName = other.getStepName(); if (this$stepName == null ? other$stepName != null : !this$stepName.equals(other$stepName)) return false; Object this$className = getClassName();Object other$className = other.getClassName();return this$className == null ? other$className == null : this$className.equals(other$className); } public boolean canEqual(Object other) { return other instanceof ConfigStep; } public int hashCode() { int PRIME = 31;int result = 1;Object $properties = getProperties();result = result * 31 + ($properties == null ? 0 : $properties.hashCode());Object $stepName = getStepName();result = result * 31 + ($stepName == null ? 0 : $stepName.hashCode());Object $className = getClassName();result = result * 31 + ($className == null ? 0 : $className.hashCode());return result; } public String toString() { return "ConfigStep(properties=" + getProperties() + ", stepName=" + getStepName() + ", className=" + getClassName() + ")"; }
/*    */   
/* 18 */   public JsonNode getProperties() { return this.properties; } public void setProperties(JsonNode properties) { this.properties = properties; }
/* 19 */   public String getStepName() { return this.stepName; } public void setStepName(String stepName) { this.stepName = stepName; }
/* 20 */   public String getClassName() { return this.className; } public void setClassName(String className) { this.className = className; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/ConfigStep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */