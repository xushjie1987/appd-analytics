/*    */ package com.appdynamics.analytics.agent.pipeline.xform.text;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */  enum MatchAction
/*    */ {
/* 12 */   NEW, 
/* 13 */   APPEND, 
/* 14 */   FLUSH;
/*    */   
/*    */   private MatchAction() {}
/* 17 */   MatchAction inverse() { switch (this) {
/*    */     case NEW: 
/* 19 */       return APPEND;
/*    */     
/*    */     case APPEND: 
/* 22 */       return NEW;
/*    */     }
/*    */     
/* 25 */     throw new IllegalArgumentException("Illegal inverse for MatchAction: " + name());
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/analytics-agent/lib/analytics-agent.jar!/com/appdynamics/analytics/agent/pipeline/xform/text/MatchAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */