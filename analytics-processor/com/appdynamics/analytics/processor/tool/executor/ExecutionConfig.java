/*    */ package com.appdynamics.analytics.processor.tool.executor;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ExecutionConfig
/*    */ {
/*    */   String name;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 18 */     if (o == this) return true; if (!(o instanceof ExecutionConfig)) return false; ExecutionConfig other = (ExecutionConfig)o; if (!other.canEqual(this)) return false; Object this$name = getName();Object other$name = other.getName(); if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false; Object this$steps = getSteps();Object other$steps = other.getSteps();return this$steps == null ? other$steps == null : this$steps.equals(other$steps); } public boolean canEqual(Object other) { return other instanceof ExecutionConfig; } public int hashCode() { int PRIME = 31;int result = 1;Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());Object $steps = getSteps();result = result * 31 + ($steps == null ? 0 : $steps.hashCode());return result; } public String toString() { return "ExecutionConfig(name=" + getName() + ", steps=" + getSteps() + ")"; }
/*    */   
/* 20 */   public String getName() { return this.name; } public void setName(String name) { this.name = name; }
/* 21 */   public List<ConfigStep> getSteps() { return this.steps; } public void setSteps(List<ConfigStep> steps) { this.steps = steps; } List<ConfigStep> steps = Collections.emptyList();
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/ExecutionConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */