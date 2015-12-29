/*    */ package com.appdynamics.common.util.misc;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Collections
/*    */ {
/*    */   public static <T> List<T> castOrNewList(@Nullable Object existingSingletonOrList)
/*    */   {
/* 33 */     List<T> newList = null;
/*    */     
/* 35 */     if (existingSingletonOrList == null) {
/* 36 */       newList = new ArrayList(4);
/* 37 */     } else if ((existingSingletonOrList instanceof List)) {
/* 38 */       newList = (List)existingSingletonOrList;
/*    */     } else {
/* 40 */       newList = new ArrayList(4);
/* 41 */       newList.add(existingSingletonOrList);
/*    */     }
/*    */     
/* 44 */     return newList;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/misc/Collections.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */