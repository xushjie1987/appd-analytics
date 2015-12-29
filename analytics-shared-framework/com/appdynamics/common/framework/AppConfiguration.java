/*    */ package com.appdynamics.common.framework;
/*    */ 
/*    */ import io.dropwizard.Configuration;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ public class AppConfiguration
/*    */   extends Configuration
/*    */ {
/* 21 */   public String toString() { return "AppConfiguration(description=" + getDescription() + ", name=" + getName() + ", modules=" + getModules() + ")"; }
/* 22 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof AppConfiguration)) return false; AppConfiguration other = (AppConfiguration)o; if (!other.canEqual(this)) return false; Object this$description = getDescription();Object other$description = other.getDescription(); if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false; Object this$name = getName();Object other$name = other.getName(); if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false; Object this$modules = getModules();Object other$modules = other.getModules();return this$modules == null ? other$modules == null : this$modules.equals(other$modules); } public boolean canEqual(Object other) { return other instanceof AppConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $description = getDescription();result = result * 31 + ($description == null ? 0 : $description.hashCode());Object $name = getName();result = result * 31 + ($name == null ? 0 : $name.hashCode());Object $modules = getModules();result = result * 31 + ($modules == null ? 0 : $modules.hashCode());return result; }
/*    */   @NotNull
/* 24 */   String description = "Application";
/* 25 */   public String getDescription() { return this.description; } public void setDescription(String description) { this.description = description; }
/*    */   @NotNull
/* 27 */   String name = "Application";
/* 28 */   public String getName() { return this.name; } public void setName(String name) { this.name = name; }
/*    */   
/*    */ 
/*    */   List<ModuleConfiguration> modules;
/*    */   
/*    */   public List<ModuleConfiguration> getModules()
/*    */   {
/* 35 */     return this.modules; } public void setModules(List<ModuleConfiguration> modules) { this.modules = modules; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/AppConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */