/*    */ package com.appdynamics.common.framework.util;
/*    */ 
/*    */ import com.appdynamics.common.framework.AppConfiguration;
/*    */ import com.appdynamics.common.util.version.VersionAware;
/*    */ import javax.validation.constraints.Min;
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
/*    */ 
/*    */ public class VersionedAppConfiguration
/*    */   extends AppConfiguration
/*    */   implements VersionAware
/*    */ {
/* 23 */   public String toString() { return "VersionedAppConfiguration(version=" + getVersion() + ")"; }
/* 24 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof VersionedAppConfiguration)) return false; VersionedAppConfiguration other = (VersionedAppConfiguration)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; return getVersion() == other.getVersion(); } public boolean canEqual(Object other) { return other instanceof VersionedAppConfiguration; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();result = result * 31 + getVersion();return result; }
/*    */   @Min(0L)
/* 26 */   int version = 0;
/* 27 */   public int getVersion() { return this.version; } public void setVersion(int version) { this.version = version; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/util/VersionedAppConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */