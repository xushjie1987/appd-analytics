/*    */ package com.appdynamics.analytics.processor.route;
/*    */ 
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
/*    */ public class ClusterRouteConfiguration
/*    */ {
/* 18 */   public String toString() { return "ClusterRouteConfiguration(leewayTimeInMillis=" + getLeewayTimeInMillis() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;long $leewayTimeInMillis = getLeewayTimeInMillis();result = result * 31 + (int)($leewayTimeInMillis >>> 32 ^ $leewayTimeInMillis);return result; } public boolean canEqual(Object other) { return other instanceof ClusterRouteConfiguration; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof ClusterRouteConfiguration)) return false; ClusterRouteConfiguration other = (ClusterRouteConfiguration)o; if (!other.canEqual(this)) return false; return getLeewayTimeInMillis() == other.getLeewayTimeInMillis();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @NotNull
/* 25 */   long leewayTimeInMillis = 900000L;
/* 26 */   public void setLeewayTimeInMillis(long leewayTimeInMillis) { this.leewayTimeInMillis = leewayTimeInMillis; } public long getLeewayTimeInMillis() { return this.leewayTimeInMillis; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/route/ClusterRouteConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */