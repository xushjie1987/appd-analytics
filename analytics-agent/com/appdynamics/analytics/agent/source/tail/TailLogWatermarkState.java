/*    */ package com.appdynamics.analytics.agent.source.tail;
/*    */ 
/*    */ import com.appdynamics.analytics.agent.input.tail.TailFileState;
/*    */ import com.appdynamics.analytics.agent.source.LogWatermarkState;
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TailLogWatermarkState
/*    */   extends LogWatermarkState
/*    */ {
/*    */   private List<TailFileState> tailFileStates;
/*    */   
/* 20 */   public String toString() { return "TailLogWatermarkState(tailFileStates=" + getTailFileStates() + ")"; }
/* 21 */   public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof TailLogWatermarkState)) return false; TailLogWatermarkState other = (TailLogWatermarkState)o; if (!other.canEqual(this)) return false; if (!super.equals(o)) return false; Object this$tailFileStates = getTailFileStates();Object other$tailFileStates = other.getTailFileStates();return this$tailFileStates == null ? other$tailFileStates == null : this$tailFileStates.equals(other$tailFileStates); } public boolean canEqual(Object other) { return other instanceof TailLogWatermarkState; } public int hashCode() { int PRIME = 31;int result = 1;result = result * 31 + super.hashCode();Object $tailFileStates = getTailFileStates();result = result * 31 + ($tailFileStates == null ? 0 : $tailFileStates.hashCode());return result; }
/*    */   
/* 23 */   public List<TailFileState> getTailFileStates() { return this.tailFileStates; } public void setTailFileStates(List<TailFileState> tailFileStates) { this.tailFileStates = tailFileStates; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   @JsonCreator
/*    */   public TailLogWatermarkState(@JsonProperty("tailFileStates") List<TailFileState> tailFileStates)
/*    */   {
/* 31 */     this();
/*    */     
/* 33 */     this.tailFileStates = tailFileStates;
/*    */   }
/*    */   
/*    */   public TailLogWatermarkState() {}
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/source/tail/TailLogWatermarkState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */