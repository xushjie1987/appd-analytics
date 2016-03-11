package com.appdynamics.common.util.configuration;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy={ManualValidateableVisitor.class})
@Documented
public @interface ManualValidated
{
  public static final String VALIDATION_MESSAGE = "javax.validation.constraints.ManualValidated.message";
  
  String message() default "{javax.validation.constraints.ManualValidated.message}";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/ManualValidated.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */