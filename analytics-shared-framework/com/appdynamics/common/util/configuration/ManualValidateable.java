package com.appdynamics.common.util.configuration;

import javax.validation.ConstraintValidatorContext;

public abstract interface ManualValidateable
{
  public abstract boolean validate(ConstraintValidatorContext paramConstraintValidatorContext);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/configuration/ManualValidateable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */