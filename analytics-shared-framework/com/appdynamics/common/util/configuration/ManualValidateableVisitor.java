/*    */ package com.appdynamics.common.util.configuration;
/*    */ 
/*    */ import javax.validation.ConstraintValidator;
/*    */ import javax.validation.ConstraintValidatorContext;
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
/*    */ public class ManualValidateableVisitor
/*    */   implements ConstraintValidator<ManualValidated, ManualValidateable>
/*    */ {
/*    */   public void initialize(ManualValidated constraintAnnotation) {}
/*    */   
/*    */   public boolean isValid(ManualValidateable value, ConstraintValidatorContext context)
/*    */   {
/* 24 */     return value.validate(context);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/ManualValidateableVisitor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */