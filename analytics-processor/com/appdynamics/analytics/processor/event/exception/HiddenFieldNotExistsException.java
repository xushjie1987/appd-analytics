/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.PermanentException;
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
/*    */ public class HiddenFieldNotExistsException
/*    */   extends PermanentException
/*    */ {
/*    */   String hiddenFieldName;
/*    */   
/* 21 */   public String toString() { return "HiddenFieldNotExistsException(hiddenFieldName=" + getHiddenFieldName() + ")"; }
/* 22 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof HiddenFieldNotExistsException)) return false; HiddenFieldNotExistsException other = (HiddenFieldNotExistsException)o; if (!other.canEqual(this)) return false; Object this$hiddenFieldName = getHiddenFieldName();Object other$hiddenFieldName = other.getHiddenFieldName();return this$hiddenFieldName == null ? other$hiddenFieldName == null : this$hiddenFieldName.equals(other$hiddenFieldName); } public boolean canEqual(Object other) { return other instanceof HiddenFieldNotExistsException; } public int hashCode() { int PRIME = 31;int result = 1;Object $hiddenFieldName = getHiddenFieldName();result = result * 31 + ($hiddenFieldName == null ? 0 : $hiddenFieldName.hashCode());return result;
/*    */   }
/*    */   
/* 25 */   public String getHiddenFieldName() { return this.hiddenFieldName; }
/* 26 */   public void setHiddenFieldName(String hiddenFieldName) { this.hiddenFieldName = hiddenFieldName; }
/*    */   
/*    */   public HiddenFieldNotExistsException(String accountName, String eventType, String hiddenFieldName) {
/* 29 */     super("Hidden field " + hiddenFieldName + " does not exist for account [" + accountName + "] and event type [" + eventType + "]");
/*    */     
/* 31 */     this.hiddenFieldName = hiddenFieldName;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/HiddenFieldNotExistsException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */