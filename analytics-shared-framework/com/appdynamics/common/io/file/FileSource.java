/*    */ package com.appdynamics.common.io.file;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileSource
/*    */ {
/*    */   final String source;
/*    */   
/*    */ 
/*    */ 
/*    */   final boolean resource;
/*    */   
/*    */ 
/*    */ 
/* 17 */   public String getSource() { return this.source; }
/* 18 */   public boolean isResource() { return this.resource; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FileSource(String source, boolean resource)
/*    */   {
/* 26 */     this.source = source;
/* 27 */     this.resource = resource;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/io/file/FileSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */