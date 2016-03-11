/*    */ package com.appdynamics.common.util.event;
/*    */ 
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventBusConfiguration
/*    */ {
/*    */   @NotNull
/*    */   String name;
/*    */   boolean async;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 15 */     if (o == this) return true; if (!(o instanceof EventBusConfiguration)) return false; EventBusConfiguration other = (EventBusConfiguration)o; if (!other.canEqual(this)) return false; Object this$name = getName();Object other$name = other.getName(); if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false; return isAsync() == other.isAsync(); } public boolean canEqual(Object other) { return other instanceof EventBusConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());result = result * 31 + (isAsync() ? 1231 : 1237);return result; } public String toString() { return "EventBusConfiguration(name=" + getName() + ", async=" + isAsync() + ")"; }
/*    */   
/*    */ 
/* 18 */   public String getName() { return this.name; } public void setName(String name) { this.name = name; }
/* 19 */   public boolean isAsync() { return this.async; } public void setAsync(boolean async) { this.async = async; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/event/EventBusConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */