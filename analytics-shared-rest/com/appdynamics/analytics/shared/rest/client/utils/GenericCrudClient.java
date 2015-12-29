/*    */ package com.appdynamics.analytics.shared.rest.client.utils;
/*    */ 
/*    */ import com.appdynamics.analytics.shared.rest.dto.Identifiable;
/*    */ import java.beans.ConstructorProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GenericCrudClient
/*    */ {
/*    */   private final HttpRequestFactory requestFactory;
/*    */   
/*    */   @ConstructorProperties({"requestFactory"})
/*    */   public GenericCrudClient(HttpRequestFactory requestFactory)
/*    */   {
/* 19 */     this.requestFactory = requestFactory;
/*    */   }
/*    */   
/* 22 */   public HttpRequestFactory getRequestFactory() { return this.requestFactory; }
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
/*    */   public <ID, T extends Identifiable<ID>> T create(T entity, Class<T> entityClass, String base, String acctId)
/*    */   {
/* 35 */     return (Identifiable)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)this.requestFactory.post().appendPath(base, new String[] { acctId })).setRequestEntity(entity).expecting(200)).execute(entityClass);
/*    */   }
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
/*    */   public <ID, T extends Identifiable<ID>> T update(T entity, Class<T> entityClass, String base, String acctId)
/*    */   {
/* 52 */     return (Identifiable)((HttpEntityEnclosingRequestBuilder)((HttpEntityEnclosingRequestBuilder)this.requestFactory.put().appendPath(base, new String[] { acctId, entity.getId().toString() })).setRequestEntity(entity).expecting(200)).execute(entityClass);
/*    */   }
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
/*    */   public <ID> void delete(ID id, String base, String acctId)
/*    */   {
/* 66 */     ((HttpRequestBuilder)((HttpRequestBuilder)this.requestFactory.delete().appendPath(base, new String[] { acctId, id.toString() })).expecting(204)).execute(Void.class);
/*    */   }
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
/*    */   public <ID, T extends Identifiable<ID>> T[] list(Class<? extends T[]> entityArrayClass, String base, String acctId)
/*    */   {
/* 81 */     return (Identifiable[])((HttpRequestBuilder)((HttpRequestBuilder)this.requestFactory.get().appendPath(base, new String[] { acctId })).expecting(200)).execute(entityArrayClass);
/*    */   }
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
/*    */   public <ID, T extends Identifiable<ID>> T retrieve(ID id, Class<T> entityClass, String base, String acctId)
/*    */   {
/* 97 */     return (Identifiable)((HttpRequestBuilder)((HttpRequestBuilder)this.requestFactory.get().appendPath(base, new String[] { acctId, id.toString() })).expecting(200)).execute(entityClass);
/*    */   }
/*    */ }


/* Location:              /Users/gchen/Downloads/AppDynamics/events-service/lib/analytics-shared-rest.jar!/com/appdynamics/analytics/shared/rest/client/utils/GenericCrudClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */