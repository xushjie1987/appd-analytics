/*    */ package com.appdynamics.analytics.processor.tool.executor.steps;
/*    */ 
/*    */ import com.appdynamics.analytics.processor.tool.executor.ExecutionStep;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractExecutionStep
/*    */   implements ExecutionStep
/*    */ {
/*    */   String name;
/*    */   
/* 16 */   public String toString() { return "AbstractExecutionStep(name=" + getName() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof AbstractExecutionStep; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof AbstractExecutionStep)) return false; AbstractExecutionStep other = (AbstractExecutionStep)o; if (!other.canEqual(this)) return false; Object this$name = getName();Object other$name = other.getName();return this$name == null ? other$name == null : this$name.equals(other$name); }
/*    */   
/* 18 */   public void setName(String name) { this.name = name; } public String getName() { return this.name; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/tool/executor/steps/AbstractExecutionStep.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */