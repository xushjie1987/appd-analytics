/*    */ package com.appdynamics.analytics.shared.rest.client.eventservice;
/*    */ 
/*    */ import com.appdynamics.analytics.shared.rest.client.eventservice.creator.EventTypeCreator;
/*    */ import java.beans.ConstructorProperties;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ 
/*    */ class CreatorRegisteredEventTypes
/*    */ {
/*    */   private final ThreadLocal<AccountNameEventTypeKey> tlsAccountNameEventTypeKey;
/*    */   private final Map<String, EventTypeCreator> eventTypeCreators;
/*    */   private final Map<AccountNameEventTypeKey, String> registered;
/*    */   
/*    */   CreatorRegisteredEventTypes()
/*    */   {
/* 30 */     this.eventTypeCreators = new ConcurrentHashMap();
/* 31 */     this.registered = new ConcurrentHashMap();
/* 32 */     this.tlsAccountNameEventTypeKey = new ThreadLocal()
/*    */     {
/*    */       protected CreatorRegisteredEventTypes.AccountNameEventTypeKey initialValue() {
/* 35 */         return new CreatorRegisteredEventTypes.AccountNameEventTypeKey("", "");
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   boolean isEventTypeRegisteredOrNoCreator(String accountName, String eventType)
/*    */   {
/* 46 */     if (!this.eventTypeCreators.containsKey(eventType))
/*    */     {
/* 48 */       return true;
/*    */     }
/* 50 */     AccountNameEventTypeKey key = (AccountNameEventTypeKey)this.tlsAccountNameEventTypeKey.get();
/* 51 */     key.reuse(accountName, eventType);
/*    */     
/* 53 */     return this.registered.containsKey(key);
/*    */   }
/*    */   
/*    */   void addRegisteredEventType(String accountName, String eventType) {
/* 57 */     this.registered.put(new AccountNameEventTypeKey(accountName, eventType), "");
/*    */   }
/*    */   
/*    */   EventTypeCreator findEventTypeCreator(String eventType) {
/* 61 */     return (EventTypeCreator)this.eventTypeCreators.get(eventType);
/*    */   }
/*    */   
/*    */   void registerEventTypeCreator(Collection<EventTypeCreator> creators) {
/* 65 */     if ((creators != null) && (!creators.isEmpty())) {
/* 66 */       for (EventTypeCreator creator : creators)
/* 67 */         this.eventTypeCreators.put(creator.getEventTypeName(), creator);
/*    */     }
/*    */   }
/*    */   
/*    */   private static class AccountNameEventTypeKey implements Cloneable {
/*    */     private String accountName;
/*    */     private String eventType;
/*    */     
/*    */     @ConstructorProperties({"accountName", "eventType"})
/*    */     public AccountNameEventTypeKey(String accountName, String eventType) {
/* 77 */       this.accountName = accountName;this.eventType = eventType; }
/* 78 */     public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof AccountNameEventTypeKey)) return false; AccountNameEventTypeKey other = (AccountNameEventTypeKey)o; if (!other.canEqual(this)) return false; Object this$accountName = this.accountName;Object other$accountName = other.accountName; if (this$accountName == null ? other$accountName != null : !this$accountName.equals(other$accountName)) return false; Object this$eventType = this.eventType;Object other$eventType = other.eventType;return this$eventType == null ? other$eventType == null : this$eventType.equals(other$eventType); } public boolean canEqual(Object other) { return other instanceof AccountNameEventTypeKey; } public int hashCode() { int PRIME = 31;int result = 1;Object $accountName = this.accountName;result = result * 31 + ($accountName == null ? 0 : $accountName.hashCode());Object $eventType = this.eventType;result = result * 31 + ($eventType == null ? 0 : $eventType.hashCode());return result;
/*    */     }
/*    */     
/*    */ 
/*    */     private void reuse(String accountName, String eventType)
/*    */     {
/* 84 */       this.accountName = accountName;
/* 85 */       this.eventType = eventType;
/*    */     }
/*    */     
/*    */     public AccountNameEventTypeKey clone()
/*    */     {
/* 90 */       return new AccountNameEventTypeKey(this.accountName, this.eventType);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/eventservice/CreatorRegisteredEventTypes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */