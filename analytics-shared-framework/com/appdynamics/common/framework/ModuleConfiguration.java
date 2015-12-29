/*    */ package com.appdynamics.common.framework;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModuleConfiguration
/*    */ {
/*    */   @NotNull
/*    */   String className;
/*    */   String[] classPath;
/*    */   String uri;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 20 */     if (o == this) return true; if (!(o instanceof ModuleConfiguration)) return false; ModuleConfiguration other = (ModuleConfiguration)o; if (!other.canEqual(this)) return false; Object this$className = getClassName();Object other$className = other.getClassName(); if (this$className == null ? other$className != null : !this$className.equals(other$className)) return false; if (!Arrays.deepEquals(getClassPath(), other.getClassPath())) return false; Object this$uri = getUri();Object other$uri = other.getUri(); if (this$uri == null ? other$uri != null : !this$uri.equals(other$uri)) return false; Object this$properties = getProperties();Object other$properties = other.getProperties();return this$properties == null ? other$properties == null : this$properties.equals(other$properties); } public boolean canEqual(Object other) { return other instanceof ModuleConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $className = getClassName();result = result * 31 + ($className == null ? 0 : $className.hashCode());result = result * 31 + Arrays.deepHashCode(getClassPath());Object $uri = getUri();result = result * 31 + ($uri == null ? 0 : $uri.hashCode());Object $properties = getProperties();result = result * 31 + ($properties == null ? 0 : $properties.hashCode());return result; }
/* 21 */   public String toString() { return "ModuleConfiguration(className=" + getClassName() + ", uri=" + getUri() + ")"; }
/*    */   
/*    */ 
/* 24 */   public String getClassName() { return this.className; } public void setClassName(String className) { this.className = className; }
/*    */   
/* 26 */   public String[] getClassPath() { return this.classPath; } public void setClassPath(String[] classPath) { this.classPath = classPath; }
/*    */   
/* 28 */   public String getUri() { return this.uri; } public void setUri(String uri) { this.uri = uri; }
/*    */   @NotNull
/* 30 */   Object properties = new HashMap();
/* 31 */   public Object getProperties() { return this.properties; } public void setProperties(Object properties) { this.properties = properties; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/ModuleConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */