/*    */ package com.appdynamics.analytics.processor.event.exception;
/*    */ 
/*    */ import com.appdynamics.common.util.exception.TransientException;
/*    */ import java.util.Set;
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
/*    */ public class MoveEventsException
/*    */   extends TransientException
/*    */ {
/*    */   final Set<String> movedButNotDeleted;
/*    */   final Set<String> unmoved;
/*    */   
/* 21 */   public String toString() { return "MoveEventsException(movedButNotDeleted=" + getMovedButNotDeleted() + ", unmoved=" + getUnmoved() + ")"; }
/* 22 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof MoveEventsException)) return false; MoveEventsException other = (MoveEventsException)o; if (!other.canEqual(this)) return false; Object this$movedButNotDeleted = getMovedButNotDeleted();Object other$movedButNotDeleted = other.getMovedButNotDeleted(); if (this$movedButNotDeleted == null ? other$movedButNotDeleted != null : !this$movedButNotDeleted.equals(other$movedButNotDeleted)) return false; Object this$unmoved = getUnmoved();Object other$unmoved = other.getUnmoved();return this$unmoved == null ? other$unmoved == null : this$unmoved.equals(other$unmoved); } public boolean canEqual(Object other) { return other instanceof MoveEventsException; } public int hashCode() { int PRIME = 31;int result = 1;Object $movedButNotDeleted = getMovedButNotDeleted();result = result * 31 + ($movedButNotDeleted == null ? 0 : $movedButNotDeleted.hashCode());Object $unmoved = getUnmoved();result = result * 31 + ($unmoved == null ? 0 : $unmoved.hashCode());return result; }
/*    */   
/* 24 */   public Set<String> getMovedButNotDeleted() { return this.movedButNotDeleted; }
/* 25 */   public Set<String> getUnmoved() { return this.unmoved; }
/*    */   
/*    */   public MoveEventsException(String message, Set<String> movedButNotDeleted, Set<String> unmovedDocIds) {
/* 28 */     super(message);
/* 29 */     this.movedButNotDeleted = movedButNotDeleted;
/* 30 */     this.unmoved = unmovedDocIds;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/event/exception/MoveEventsException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */