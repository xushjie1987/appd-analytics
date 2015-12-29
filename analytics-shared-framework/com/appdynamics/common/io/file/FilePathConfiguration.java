/*    */ package com.appdynamics.common.io.file;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilePathConfiguration
/*    */ {
/*    */   @NotNull
/*    */   String path;
/*    */   @NotNull
/*    */   String nameGlob;
/*    */   
/* 20 */   public String toString() { return "FilePathConfiguration(path=" + getPath() + ", nameGlob=" + getNameGlob() + ")"; }
/* 21 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof FilePathConfiguration)) return false; FilePathConfiguration other = (FilePathConfiguration)o; if (!other.canEqual(this)) return false; Object this$path = getPath();Object other$path = other.getPath(); if (this$path == null ? other$path != null : !this$path.equals(other$path)) return false; Object this$nameGlob = getNameGlob();Object other$nameGlob = other.getNameGlob();return this$nameGlob == null ? other$nameGlob == null : this$nameGlob.equals(other$nameGlob); } public boolean canEqual(Object other) { return other instanceof FilePathConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $path = getPath();result = result * 31 + ($path == null ? 0 : $path.hashCode());Object $nameGlob = getNameGlob();result = result * 31 + ($nameGlob == null ? 0 : $nameGlob.hashCode());return result;
/*    */   }
/*    */   
/* 24 */   public String getPath() { return this.path; } public void setPath(String path) { this.path = path; }
/*    */   
/*    */ 
/* 27 */   public String getNameGlob() { return this.nameGlob; } public void setNameGlob(String nameGlob) { this.nameGlob = nameGlob; }
/*    */   
/*    */ 
/*    */ 
/*    */   @JsonCreator
/*    */   public FilePathConfiguration(@JsonProperty("path") String path, @JsonProperty("nameGlob") String nameGlob)
/*    */   {
/* 34 */     this.path = path;
/* 35 */     this.nameGlob = nameGlob;
/*    */   }
/*    */   
/*    */   public FilePathConfiguration() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/file/FilePathConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */