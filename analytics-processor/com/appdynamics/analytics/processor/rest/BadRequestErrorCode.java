/*    */ package com.appdynamics.analytics.processor.rest;
/*    */ 
/*    */ import javax.ws.rs.core.Response.Status;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BadRequestErrorCode
/*    */   implements RestErrorCode
/*    */ {
/*    */   final String subStatus;
/*    */   
/*    */   public String toString()
/*    */   {
/* 17 */     return "BadRequestErrorCode(subStatus=" + getSubStatus() + ")";
/*    */   }
/*    */   
/*    */   public BadRequestErrorCode(String subStatus)
/*    */   {
/* 22 */     this.subStatus = subStatus;
/*    */   }
/*    */   
/*    */   public Response.Status getStatus()
/*    */   {
/* 27 */     return Response.Status.BAD_REQUEST;
/*    */   }
/*    */   
/*    */   public String getSubStatus()
/*    */   {
/* 32 */     return this.subStatus;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/rest/BadRequestErrorCode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */