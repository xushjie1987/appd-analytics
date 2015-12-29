/*    */ package com.appdynamics.analytics.shared.rest.client.eventservice.creator;
/*    */ 
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultEventTypeCreator
/*    */   implements EventTypeCreator
/*    */ {
/*    */   private String eventType;
/*    */   private String eventTypeMapping;
/*    */   
/*    */   @ConstructorProperties({"eventType", "eventTypeMapping"})
/*    */   public DefaultEventTypeCreator(String eventType, String eventTypeMapping)
/*    */   {
/* 20 */     this.eventType = eventType;this.eventTypeMapping = eventTypeMapping; }
/* 21 */   public String toString() { return "DefaultEventTypeCreator(eventType=" + this.eventType + ", eventTypeMapping=" + getEventTypeMapping() + ")"; }
/* 22 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof DefaultEventTypeCreator)) return false; DefaultEventTypeCreator other = (DefaultEventTypeCreator)o; if (!other.canEqual(this)) return false; Object this$eventType = this.eventType;Object other$eventType = other.eventType;return this$eventType == null ? other$eventType == null : this$eventType.equals(other$eventType); } public boolean canEqual(Object other) { return other instanceof DefaultEventTypeCreator; } public int hashCode() { int PRIME = 31;int result = 1;Object $eventType = this.eventType;result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getEventTypeMapping()
/*    */   {
/* 31 */     return this.eventTypeMapping;
/*    */   }
/*    */   
/*    */   public String getEventTypeName()
/*    */   {
/* 36 */     return this.eventType;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/creator/DefaultEventTypeCreator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */