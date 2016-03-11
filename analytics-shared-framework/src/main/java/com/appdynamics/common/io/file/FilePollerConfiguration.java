/*    */ package com.appdynamics.common.io.file;
/*    */ 
/*    */ import com.appdynamics.common.util.datetime.TimeUnitConfiguration;
/*    */ import java.util.List;
/*    */ import javax.validation.Valid;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ public class FilePollerConfiguration
/*    */ {
/*    */   @NotNull
/*    */   @Valid
/*    */   TimeUnitConfiguration poll;
/*    */   @Valid
/*    */   @javax.validation.constraints.Size(min=1)
/*    */   List<FilePathConfiguration> paths;
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 19 */     if (o == this) return true; if (!(o instanceof FilePollerConfiguration)) return false; FilePollerConfiguration other = (FilePollerConfiguration)o; if (!other.canEqual(this)) return false; Object this$poll = getPoll();Object other$poll = other.getPoll(); if (this$poll == null ? other$poll != null : !this$poll.equals(other$poll)) return false; Object this$paths = getPaths();Object other$paths = other.getPaths();return this$paths == null ? other$paths == null : this$paths.equals(other$paths); } public boolean canEqual(Object other) { return other instanceof FilePollerConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;Object $poll = getPoll();result = result * 31 + ($poll == null ? 0 : $poll.hashCode());Object $paths = getPaths();result = result * 31 + ($paths == null ? 0 : $paths.hashCode());return result; } public String toString() { return "FilePollerConfiguration(poll=" + getPoll() + ", paths=" + getPaths() + ")"; }
/*    */   
/*    */ 
/*    */ 
/* 23 */   public TimeUnitConfiguration getPoll() { return this.poll; } public void setPoll(TimeUnitConfiguration poll) { this.poll = poll; }
/*    */   
/*    */ 
/*    */ 
/* 27 */   public List<FilePathConfiguration> getPaths() { return this.paths; } public void setPaths(List<FilePathConfiguration> paths) { this.paths = paths; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/file/FilePollerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */