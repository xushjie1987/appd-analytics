package com.appdynamics.common.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
@Documented
@Inherited
public @interface Mutates
{
  String value() default "";
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/annotation/Mutates.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */