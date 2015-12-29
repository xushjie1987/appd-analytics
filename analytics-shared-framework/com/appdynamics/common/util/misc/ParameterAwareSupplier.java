/*    */ package com.appdynamics.common.util.misc;
/*    */ 
/*    */ import com.google.common.base.Supplier;
/*    */ import com.google.common.reflect.TypeToken;
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
/*    */ public abstract interface ParameterAwareSupplier<P, T>
/*    */   extends Supplier<T>
/*    */ {
/*    */   public abstract T get(P paramP);
/*    */   
/*    */   public static abstract class Helper
/*    */   {
/*    */     public static TypeToken findParameterType(ParameterAwareSupplier<?, ?> pas)
/*    */     {
/* 33 */       TypeToken pasToken = TypeToken.of(pas.getClass()).getSupertype(ParameterAwareSupplier.class);
/*    */       
/* 35 */       return pasToken.resolveType(ParameterAwareSupplier.class.getTypeParameters()[0]);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/misc/ParameterAwareSupplier.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */