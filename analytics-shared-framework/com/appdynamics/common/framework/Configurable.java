package com.appdynamics.common.framework;

import com.google.common.reflect.TypeToken;

public abstract interface Configurable<C>
{
  public abstract TypeToken<C> getConfigurationType();
  
  public abstract String getUri();
  
  public abstract void setUri(String paramString);
  
  public abstract C getConfiguration();
  
  public abstract void setConfiguration(C paramC);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-framework.jar!/com/appdynamics/common/framework/Configurable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */