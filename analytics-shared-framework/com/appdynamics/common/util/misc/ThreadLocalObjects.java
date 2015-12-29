/*    */ package com.appdynamics.common.util.misc;
/*    */ 
/*    */ import com.appdynamics.common.io.codec.Json;
/*    */ import com.appdynamics.common.io.codec.Json.Walker;
/*    */ import java.util.Random;
/*    */ import java.util.concurrent.ThreadLocalRandom;
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
/*    */ public abstract class ThreadLocalObjects
/*    */ {
/* 20 */   private static final ThreadLocal<StringBuilder> TLS_STRING_BUILDERS = new ThreadLocal()
/*    */   {
/*    */     protected StringBuilder initialValue() {
/* 23 */       return new StringBuilder();
/*    */     }
/*    */   };
/*    */   
/* 27 */   private static final ThreadLocal<Json.Walker> TLS_JSON_WALKERS = new ThreadLocal()
/*    */   {
/*    */     protected Json.Walker initialValue() {
/* 30 */       return Json.newWalker();
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Json.Walker jsonWalker()
/*    */   {
/* 43 */     Json.Walker walker = (Json.Walker)TLS_JSON_WALKERS.get();
/* 44 */     walker.endWalk();
/* 45 */     return walker;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static StringBuilder stringBuilder()
/*    */   {
/* 54 */     StringBuilder stringBuilder = (StringBuilder)TLS_STRING_BUILDERS.get();
/* 55 */     stringBuilder.setLength(0);
/* 56 */     return stringBuilder;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Random random()
/*    */   {
/* 65 */     return ThreadLocalRandom.current();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/misc/ThreadLocalObjects.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */