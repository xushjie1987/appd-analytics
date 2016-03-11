/*    */ package com.appdynamics.common.framework.util;
/*    */ 
/*    */ import com.sun.jersey.spi.resource.Singleton;
/*    */ import javax.ws.rs.Consumes;
/*    */ import javax.ws.rs.GET;
/*    */ import javax.ws.rs.Path;
/*    */ import javax.ws.rs.Produces;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ @Singleton
/*    */ @Path("_ping")
/*    */ @Produces({"text/plain"})
/*    */ @Consumes({"text/plain"})
/*    */ public class NoOpResource
/*    */ {
/* 28 */   private static final Logger log = LoggerFactory.getLogger(NoOpResource.class);
/*    */   
/*    */   public static final String URI_MAIN = "_ping";
/*    */   
/*    */   public static final String URI_MAIN_RESPONSE = "_pong";
/*    */   
/*    */ 
/*    */   @GET
/*    */   public String ping()
/*    */   {
/* 38 */     return "_pong";
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/NoOpResource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */