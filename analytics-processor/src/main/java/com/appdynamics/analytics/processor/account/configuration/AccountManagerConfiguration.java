/*    */ package com.appdynamics.analytics.processor.account.configuration;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ public class AccountManagerConfiguration
/*    */ {
/* 20 */   public String toString() { return "AccountManagerConfiguration(systemAccounts=" + getSystemAccounts() + ")"; } public int hashCode() { int PRIME = 31;int result = 1;Object $systemAccounts = getSystemAccounts();result = result * 31 + ($systemAccounts == null ? 0 : $systemAccounts.hashCode());return result; } public boolean canEqual(Object other) { return other instanceof AccountManagerConfiguration; } public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof AccountManagerConfiguration)) return false; AccountManagerConfiguration other = (AccountManagerConfiguration)o; if (!other.canEqual(this)) return false; Object this$systemAccounts = getSystemAccounts();Object other$systemAccounts = other.getSystemAccounts();return this$systemAccounts == null ? other$systemAccounts == null : this$systemAccounts.equals(other$systemAccounts); }
/*    */   
/*    */   @NotNull
/* 23 */   List<AccountConfiguration> systemAccounts = new ArrayList(0);
/* 24 */   public void setSystemAccounts(List<AccountConfiguration> systemAccounts) { this.systemAccounts = systemAccounts; } public List<AccountConfiguration> getSystemAccounts() { return this.systemAccounts; }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/account/configuration/AccountManagerConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */