/*    */ package com.appdynamics.common.util.var;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChainedVariableResolver
/*    */   implements VariableResolver
/*    */ {
/*    */   final List<VariableResolver> resolverChain;
/*    */   
/*    */   public ChainedVariableResolver(VariableResolver resolver1, VariableResolver resolver2, VariableResolver... remainingResolvers)
/*    */   {
/* 22 */     List<VariableResolver> chain = new ArrayList(remainingResolvers.length + 2);
/* 23 */     chain.add(resolver1);
/* 24 */     chain.add(resolver2);
/* 25 */     chain.addAll(Arrays.asList(remainingResolvers));
/* 26 */     this.resolverChain = chain;
/*    */   }
/*    */   
/*    */   public Object resolve(String variable)
/*    */   {
/* 31 */     for (VariableResolver variableResolver : this.resolverChain) {
/* 32 */       Object o = variableResolver.resolve(variable);
/* 33 */       if (o != null) {
/* 34 */         return o;
/*    */       }
/*    */     }
/*    */     
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public Object resolve(String variable, String variableExtension)
/*    */   {
/* 43 */     for (VariableResolver variableResolver : this.resolverChain) {
/* 44 */       Object o = variableResolver.resolve(variable, variableExtension);
/* 45 */       if (o != null) {
/* 46 */         return o;
/*    */       }
/*    */     }
/*    */     
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/ChainedVariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */