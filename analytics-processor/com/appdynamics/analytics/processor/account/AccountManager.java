package com.appdynamics.analytics.processor.account;

import com.appdynamics.analytics.processor.account.configuration.AccountConfiguration;
import com.appdynamics.analytics.processor.account.configuration.AccountLicensingConfiguration;
import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import com.google.inject.ImplementedBy;
import java.util.List;

@ImplementedBy(ElasticsearchAccountManager.class)
public abstract interface AccountManager
{
  public static final String AD_ACCOUNTS = "appdynamics_accounts";
  public static final String ACCOUNT_NAME = "accountName";
  public static final String ACCESS_KEY = "accessKey";
  public static final String EUM_ACCOUNT_NAME = "eumAccountName";
  public static final String EXPIRATION_DATE = "expirationDate";
  public static final String LICENSING_CONFIGURATIONS = "licensingConfigurations";
  public static final String LICENSE_EVENT_TYPE = "eventType";
  
  public abstract void upsertAccountConfigurations(List<AccountConfiguration> paramList);
  
  public abstract List<AccountConfiguration> findAccountConfigurations();
  
  public abstract List<AccountConfiguration> findAccountConfigurations(List<String> paramList);
  
  public abstract Optional<AccountConfiguration> findAccountConfiguration(String paramString);
  
  public abstract Multimap<String, AccountConfiguration> findSystemAccountConfigurationsAsMap();
  
  public abstract Optional<AccountLicensingConfiguration> findAccountLicensingConfiguration(String paramString1, String paramString2);
}


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-processor.jar!/com/appdynamics/analytics/processor/account/AccountManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */