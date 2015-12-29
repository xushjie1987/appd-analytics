package com.appdynamics.common.util.var;

public abstract interface VariableResolver
{
  public abstract Object resolve(String paramString);
  
  public abstract Object resolve(String paramString1, String paramString2);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/util/var/VariableResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */