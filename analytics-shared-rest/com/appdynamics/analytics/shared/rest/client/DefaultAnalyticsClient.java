/*    */ package com.appdynamics.analytics.shared.rest.client;
/*    */ 
/*    */ import com.appdynamics.analytics.shared.rest.client.auth.AccountServiceClient;
/*    */ import com.appdynamics.analytics.shared.rest.client.auth.AccountServiceClient.Account;
/*    */ import com.appdynamics.analytics.shared.rest.client.auth.DefaultAccountServiceClient;
/*    */ import com.appdynamics.analytics.shared.rest.client.eventservice.DefaultEventServiceClient;
/*    */ import com.appdynamics.analytics.shared.rest.client.eventservice.DefaultExtractedFieldsClient;
/*    */ import com.appdynamics.analytics.shared.rest.client.eventservice.EventServiceClient;
/*    */ import com.appdynamics.analytics.shared.rest.client.eventservice.EventServiceClient.HiddenField;
/*    */ import com.appdynamics.analytics.shared.rest.client.eventservice.ExtractedFieldsClient;
/*    */ import com.appdynamics.analytics.shared.rest.client.eventservice.ExtractedFieldsClient.ExtractedFieldDefinition;
/*    */ import com.appdynamics.analytics.shared.rest.client.eventservice.creator.EventTypeCreator;
/*    */ import com.appdynamics.analytics.shared.rest.client.utils.AbstractAnalyticsClient.Builder;
/*    */ import com.appdynamics.analytics.shared.rest.exceptions.BadRequestRestException;
/*    */ import com.appdynamics.analytics.shared.rest.exceptions.NotFoundRestException;
/*    */ import com.appdynamics.analytics.shared.rest.exceptions.RestException;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DefaultAnalyticsClient implements EventServiceClient, AccountServiceClient, ExtractedFieldsClient, Closeable
/*    */ {
/* 25 */   private static final Logger log = org.slf4j.LoggerFactory.getLogger(DefaultAnalyticsClient.class);
/*    */   
/*    */   protected EventServiceClient eventServiceClient;
/*    */   
/* 29 */   public void registerEventType(String accountName, String accessKey, String eventType, String body) throws RestException { this.eventServiceClient.registerEventType(accountName, accessKey, eventType, body); } public void updateEventType(String accountName, String accessKey, String eventType, String body) throws RestException { this.eventServiceClient.updateEventType(accountName, accessKey, eventType, body); } public String getEventType(String accountName, String accessKey, String eventType) throws RestException { return this.eventServiceClient.getEventType(accountName, accessKey, eventType); } public String getEventTypeUsageNumDocuments(String accountName, String accessKey, String eventType, String startDateTime, String endDateTime) throws RestException { return this.eventServiceClient.getEventTypeUsageNumDocuments(accountName, accessKey, eventType, startDateTime, endDateTime); } public String getEventTypeUsageNumDocumentFragments(String accountName, String accessKey, String eventType, String date, int hourOfDay) throws RestException { return this.eventServiceClient.getEventTypeUsageNumDocumentFragments(accountName, accessKey, eventType, date, hourOfDay); } public String getEventTypeUsageNumBytes(String accountName, String accessKey, String eventType, String date, int hourOfDay) throws RestException { return this.eventServiceClient.getEventTypeUsageNumBytes(accountName, accessKey, eventType, date, hourOfDay); } public void deleteEventType(String accountName, String accessKey, String eventType) { this.eventServiceClient.deleteEventType(accountName, accessKey, eventType); } public void publishEvents(String accountName, String accessKey, String eventType, String body) throws RestException { this.eventServiceClient.publishEvents(accountName, accessKey, eventType, body); } public void upsertEvents(String accountName, String accessKey, String eventType, String body, String idPath, List<String> mergeFields) throws RestException { this.eventServiceClient.upsertEvents(accountName, accessKey, eventType, body, idPath, mergeFields); } public String searchEvents(String accountName, String accessKey, String eventType, String searchRequest) throws RestException { return this.eventServiceClient.searchEvents(accountName, accessKey, eventType, searchRequest); } public String multiSearchEvents(String accountName, String accessKey, String multiSearchRequest) throws RestException { return this.eventServiceClient.multiSearchEvents(accountName, accessKey, multiSearchRequest); } public String queryEvents(String accountName, String accessKey, String queryString, String startTime, String endTime, String limitResults, boolean returnEsJson) throws RestException { return this.eventServiceClient.queryEvents(accountName, accessKey, queryString, startTime, endTime, limitResults, returnEsJson); } public void registerEventTypeCreator(EventTypeCreator[] creators) { this.eventServiceClient.registerEventTypeCreator(creators); } public void ping(String accountName, String accessKey) { this.eventServiceClient.ping(accountName, accessKey); } public void hideFields(String accountName, String accessKey, String eventType, List<EventServiceClient.HiddenField> fields) { this.eventServiceClient.hideFields(accountName, accessKey, eventType, fields); } public List<EventServiceClient.HiddenField> listHiddenFields(String accountName, String accessKey, String eventType) throws IOException { return this.eventServiceClient.listHiddenFields(accountName, accessKey, eventType); } public void unhideField(String accountName, String accessKey, String eventType, String fieldName) { this.eventServiceClient.unhideField(accountName, accessKey, eventType, fieldName); } public String relevantFields(String accountName, String accessKey, String eventType, String significantTermsQuery) throws RestException { return this.eventServiceClient.relevantFields(accountName, accessKey, eventType, significantTermsQuery); }
/*    */   
/*    */ 
/* 32 */   public List<ExtractedFieldsClient.ExtractedFieldDefinition> getExtractedFields(String accountName, String accessKey, String eventType) { return this.extractedFieldsServiceClient.getExtractedFields(accountName, accessKey, eventType); } public List<ExtractedFieldsClient.ExtractedFieldDefinition> getExtractedFields(String accountName, String accessKey, String eventType, List<String> sourceTypes) { return this.extractedFieldsServiceClient.getExtractedFields(accountName, accessKey, eventType, sourceTypes); } public ExtractedFieldsClient.ExtractedFieldDefinition getExtractedField(String accountName, String accessKey, String eventType, String fieldName) { return this.extractedFieldsServiceClient.getExtractedField(accountName, accessKey, eventType, fieldName); } public void validateExtractedField(String accountName, String accessKey, String eventType, ExtractedFieldsClient.ExtractedFieldDefinition extractedField) throws BadRequestRestException { this.extractedFieldsServiceClient.validateExtractedField(accountName, accessKey, eventType, extractedField); } public void createExtractedField(String accountName, String accessKey, String eventType, ExtractedFieldsClient.ExtractedFieldDefinition extractedField) throws BadRequestRestException, com.appdynamics.analytics.shared.rest.exceptions.ConflictRestException { this.extractedFieldsServiceClient.createExtractedField(accountName, accessKey, eventType, extractedField); } public void updateExtractedField(String accountName, String accessKey, String eventType, ExtractedFieldsClient.ExtractedFieldDefinition extractedField) throws BadRequestRestException, NotFoundRestException { this.extractedFieldsServiceClient.updateExtractedField(accountName, accessKey, eventType, extractedField); } public void deleteExtractedField(String accountName, String accessKey, String eventType, String name) throws NotFoundRestException { this.extractedFieldsServiceClient.deleteExtractedField(accountName, accessKey, eventType, name); }
/*    */   
/*    */ 
/* 35 */   public void addOrUpdateAccounts(String symmetricKey, List<AccountServiceClient.Account> accounts) throws RestException { this.accountServiceClient.addOrUpdateAccounts(symmetricKey, accounts); } public String getAccountConfiguration(String symmetricKey, String accountName) throws RestException { return this.accountServiceClient.getAccountConfiguration(symmetricKey, accountName); } public List<AccountServiceClient.Account> getAccountConfigurations(String symmetricKey, List<String> accountNames) throws IOException, RestException { return this.accountServiceClient.getAccountConfigurations(symmetricKey, accountNames); }
/*    */   
/*    */ 
/*    */ 
/*    */   protected ExtractedFieldsClient extractedFieldsServiceClient;
/*    */   
/*    */ 
/*    */   protected AccountServiceClient accountServiceClient;
/*    */   
/*    */ 
/*    */   public DefaultAnalyticsClient(String scheme, String hostname, int port, String proxyHost, Integer proxyPort, String proxyUsername, String proxyPassword, int socketTimeoutMillis, int connectionTimeoutMillis)
/*    */   {
/* 47 */     ObjectMapper mapper = new ObjectMapper();
/* 48 */     log.info("Creating events service client with the following parameters: scheme=[{}], hostName=[{}], port=[{}], proxyHost=[{}], proxyPort=[{}]", new Object[] { scheme, hostname, Integer.valueOf(port), proxyHost, proxyPort });
/*    */     
/*    */ 
/* 51 */     this.eventServiceClient = ((EventServiceClient)DefaultEventServiceClient.builder(scheme, hostname, port).proxyConfig(proxyHost, proxyPort, proxyUsername, proxyPassword).socketTimeoutMillis(socketTimeoutMillis).connectionTimeoutMillis(connectionTimeoutMillis).mapper(mapper).build());
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 56 */     this.extractedFieldsServiceClient = ((ExtractedFieldsClient)DefaultExtractedFieldsClient.builder(scheme, hostname, port).proxyConfig(proxyHost, proxyPort, proxyUsername, proxyPassword).socketTimeoutMillis(socketTimeoutMillis).connectionTimeoutMillis(connectionTimeoutMillis).mapper(mapper).build());
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 61 */     this.accountServiceClient = ((AccountServiceClient)DefaultAccountServiceClient.builder(scheme, hostname, port).proxyConfig(proxyHost, proxyPort, proxyUsername, proxyPassword).socketTimeoutMillis(socketTimeoutMillis).connectionTimeoutMillis(connectionTimeoutMillis).mapper(mapper).build());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 73 */     ((DefaultEventServiceClient)this.eventServiceClient).close();
/* 74 */     ((DefaultExtractedFieldsClient)this.extractedFieldsServiceClient).close();
/* 75 */     ((DefaultAccountServiceClient)this.accountServiceClient).close();
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/DefaultAnalyticsClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */